package com.project.NexPay.payment.service.impl;

import com.project.NexPay.comman.enums.OrderStatus;
import com.project.NexPay.comman.enums.PaymentEvent;
import com.project.NexPay.comman.enums.PaymentStatus;
import com.project.NexPay.comman.exception.BusinessRuleViolationException;
import com.project.NexPay.comman.exception.ResourceNotFoundException;
import com.project.NexPay.payment.dto.request.PaymentInitRequest;
import com.project.NexPay.payment.dto.response.PaymentResponse;
import com.project.NexPay.payment.entity.OrderRecord;
import com.project.NexPay.payment.entity.Payment;
import com.project.NexPay.payment.gateway.PaymentGatewayRouter;
import com.project.NexPay.payment.gateway.dto.PaymentRequest;
import com.project.NexPay.payment.gateway.dto.PaymentResult;
import com.project.NexPay.payment.mapper.PaymentMapper;
import com.project.NexPay.payment.repository.OrderRepository;
import com.project.NexPay.payment.repository.PaymentRepository;
import com.project.NexPay.payment.service.PaymentService;
import com.project.NexPay.payment.stateMachine.PaymentTransitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.project.NexPay.comman.exception.ErrorCodes.ORDER_NOT_PAYABLE;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentGatewayRouter paymentGatewayRouter;
    private final PaymentMapper paymentMapper;
    private final PaymentTransitionService paymentTransitionService;

    @Override
    @Transactional
    public PaymentResponse initiate(UUID merchantId, PaymentInitRequest request) {

        OrderRecord order = orderRepository.findByIdAndMerchantId(request.orderId(), merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", request.orderId()));

        if (order.getOrderStatus() == OrderStatus.PAID || order.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new BusinessRuleViolationException(ORDER_NOT_PAYABLE,
                    "Order cannot accept payment is status : " + order.getOrderStatus());
        }

        order.setOrderStatus(OrderStatus.ATTEMPTED);
        order.setAttempts(order.getAttempts() + 1);

        Payment payment = Payment.builder()
                .order(order)
                .merchantId(merchantId)
                .amount(order.getAmount())
                .status(PaymentStatus.CREATED)
                .method(request.method())
                .methodDetails(request.methodDetails())
                .build();

        payment = paymentRepository.save(payment);

        PaymentRequest paymentRequest =new PaymentRequest(payment.getId(),order.getId(),merchantId,order.getAmount(),request.method(),request.methodDetails());

        PaymentResult result=paymentGatewayRouter.initiate(paymentRequest);

        switch (result) {
            case PaymentResult.Pending pending -> payment.setProcessorReference(pending.registrationReference());
            case PaymentResult.Failure failure -> {
                paymentTransitionService.apply(payment, PaymentEvent.AUTHORIZE_FAIL);
                payment.setErrorCode(failure.errorCode());
                payment.setErrorDescription(failure.errorDescription());
            }
            case PaymentResult.Success success -> {
                log.warn("Invalid state");
                return null;
            }
        }

        paymentRepository.save(payment);

        //TODO: send outbox(kafka event)

        return paymentMapper.toResponse(payment);
    }

    @Override
    @Transactional
    public PaymentResponse capture(UUID paymentId, UUID merchantId)   {
        Payment payment = paymentRepository.findByIdAndMerchantId(paymentId,merchantId).
                orElseThrow(() -> new ResourceNotFoundException("Payment",paymentId));

        paymentTransitionService.apply(payment,PaymentEvent.CAPTURE_REQUEST);

        PaymentResult paymentResult= paymentGatewayRouter.capture(payment.getMethod(),paymentId);

        if(paymentResult instanceof PaymentResult.Success success){
            log.info("Payment captured, paymentId: {}",paymentId);
            paymentTransitionService.apply(payment,PaymentEvent.CAPTURE_SUCCESS);
            payment.setCapturedAt(LocalDateTime.now());
        }else if(paymentResult instanceof PaymentResult.Failure failure){
            paymentTransitionService.apply(payment,PaymentEvent.CAPTURE_FAIL);
            payment.setErrorCode(failure.errorCode());
            payment.setErrorDescription(failure.errorDescription());
            log.warn("Payment capture failed,paymentId : {}", paymentId);
        }

        //TODO: send outbox(kafka event)

        return paymentMapper.toResponse(payment);
    }
}
