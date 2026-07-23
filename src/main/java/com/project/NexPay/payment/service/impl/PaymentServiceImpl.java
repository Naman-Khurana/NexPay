package com.project.NexPay.payment.service.impl;

import com.project.NexPay.comman.enums.OrderStatus;
import com.project.NexPay.comman.enums.PaymentStatus;
import com.project.NexPay.comman.exception.BusinessRuleViolationException;
import com.project.NexPay.comman.exception.ResourceNotFoundException;
import com.project.NexPay.payment.dto.request.PaymentInitRequest;
import com.project.NexPay.payment.dto.response.PaymentResponse;
import com.project.NexPay.payment.entity.OrderRecord;
import com.project.NexPay.payment.entity.Payment;
import com.project.NexPay.payment.gateway.PaymentGatewayRouter;
import com.project.NexPay.payment.gateway.dto.PaymentRequest;
import com.project.NexPay.payment.repository.OrderRepository;
import com.project.NexPay.payment.repository.PaymentRepository;
import com.project.NexPay.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.project.NexPay.comman.exception.ErrorCodes.ORDER_NOT_PAYABLE;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentGatewayRouter paymentGatewayRouter;

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

        paymentGatewayRouter.initiate(paymentRequest);

        return null;
    }
}
