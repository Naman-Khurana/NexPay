package com.project.NexPay.payment.service;

import com.project.NexPay.payment.dto.request.CreateOrderRequest;
import com.project.NexPay.payment.dto.response.OrderResponse;

import java.util.UUID;

public interface OrderService {
    OrderResponse create(UUID merchantId, CreateOrderRequest request);
}
