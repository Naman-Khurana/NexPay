package com.project.NexPay.payment.controller;

import com.project.NexPay.payment.dto.request.CreateOrderRequest;
import com.project.NexPay.payment.dto.response.OrderResponse;
import com.project.NexPay.payment.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    UUID merchantId= UUID.fromString("9c86bf71-bcb0-4fa9-a6c2-e2a5ec63f5dd"); //TODO : replace and get merchant id from merchant context

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody @Valid CreateOrderRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.create( merchantId,request));
    }

}
