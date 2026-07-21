package com.project.NexPay.payment.service.impl;

import com.project.NexPay.comman.enums.OrderStatus;
import com.project.NexPay.comman.exception.BusinessRuleViolationException;
import com.project.NexPay.comman.exception.DuplicateResourceException;
import com.project.NexPay.comman.exception.ResourceNotFoundException;
import com.project.NexPay.payment.dto.request.CreateOrderRequest;
import com.project.NexPay.payment.dto.response.OrderResponse;
import com.project.NexPay.payment.dto.response.PaymentResponse;
import com.project.NexPay.payment.entity.OrderRecord;
import com.project.NexPay.payment.entity.Payment;
import com.project.NexPay.payment.mapper.OrderMapper;
import com.project.NexPay.payment.mapper.PaymentMapper;
import com.project.NexPay.payment.repository.OrderRepository;
import com.project.NexPay.payment.repository.PaymentRepository;
import com.project.NexPay.payment.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;
    private final OrderMapper orderMapper;

    @Value("${payment.order.default-order-expiry-minutes:30}")
    private int defaultOrderExpiryMinutes;

    @Transactional
    @Override
    public OrderResponse create(UUID merchantId, CreateOrderRequest request) {
        if (request.receipt() != null && orderRepository.existsByReceiptAndMerchantId(request.receipt(), merchantId))
            throw new DuplicateResourceException("ORDER_RECEIPT_DUPLICATE", "Order with receipt already exists");

        OrderRecord order = OrderRecord.builder()
                .merchantId(merchantId)
                .receipt(request.receipt())
                .orderStatus(OrderStatus.CREATED)
                .notes(request.notes())
                .amount(request.amount())
                .expiresAt(request.expiresAt() != null ? request.expiresAt()
                        : LocalDateTime.now().plusMinutes(defaultOrderExpiryMinutes))
                .attempts(0)
                .build();
        order = orderRepository.save(order);

        // TODO: publish kafka event about order creation
        return orderMapper.toResponse(order);
//        return new OrderResponse(order.getId(),
//                order.getMerchantId(),
//                order.getReceipt(),
//                order.getAmount(),
//                order.getOrderStatus(),
//                order.getAttempts(),
//                order.getNotes(),
//                order.getExpiresAt(),
//                null);
    }

    @Override
    public OrderResponse getById(UUID merchantId, UUID orderId) {
        OrderRecord order = orderRepository.findByIdAndMerchantId(orderId, merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));
        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse cancel(UUID merchantId, UUID orderId) {
        OrderRecord order = orderRepository.findByIdAndMerchantId(orderId, merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));

        if (order.getOrderStatus() == OrderStatus.CANCELLED || order.getOrderStatus() == OrderStatus.PAID) {
            throw new BusinessRuleViolationException("ORDER_CANNOT_CANCEL",
                    "Cannot Cancel Order with Status : " + order.getOrderStatus().name());
        }

        order.setOrderStatus(OrderStatus.CANCELLED);

        return orderMapper.toResponse(order);
    }

    @Override
    public List<PaymentResponse> listPayments(UUID merchantId, UUID orderId) {
        orderRepository.findByIdAndMerchantId(orderId, merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));

        List<Payment> paymentList = paymentRepository.findByOrder_Id(orderId);
        return paymentMapper.toResponseList(paymentList);

    }
}
