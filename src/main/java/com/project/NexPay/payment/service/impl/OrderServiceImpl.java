package com.project.NexPay.payment.service.impl;

import com.project.NexPay.comman.enums.OrderStatus;
import com.project.NexPay.comman.exception.DuplicateResourceException;
import com.project.NexPay.payment.dto.request.CreateOrderRequest;
import com.project.NexPay.payment.dto.response.OrderResponse;
import com.project.NexPay.payment.entity.OrderRecord;
import com.project.NexPay.payment.repository.OrderRepository;
import com.project.NexPay.payment.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Value("${payment.order.default-order-expiry-minutes:30}")
    private int defaultOrderExpiryMinutes;

    @Transactional
    @Override
    public OrderResponse create(UUID merchantId, CreateOrderRequest request) {
        if (request.receipt() != null && orderRepository.existsByReceiptAndMerchantId(request.receipt(), merchantId))
            throw new DuplicateResourceException("ORDER_RECEIPT_DUPLICATE", "Order with receipt already exists");

        OrderRecord order= OrderRecord.builder()
                .merchantId(merchantId)
                .receipt(request.receipt())
                .orderStatus(OrderStatus.CREATED)
                .notes(request.notes())
                .amount(request.amount())
                .expiresAt(request.expiresAt() !=null ? request.expiresAt()
                        : LocalDateTime.now().plusMinutes(defaultOrderExpiryMinutes))
                .attempts(0)
                .build();
        order= orderRepository.save(order);

        // TODO: publish kafka event about order creation

        return new OrderResponse(order.getId(),
                order.getMerchantId(),
                order.getReceipt(),
                order.getAmount(),
                order.getOrderStatus(),
                order.getAttempts(),
                order.getNotes(),
                order.getExpiresAt(),
                null);
    }
}
