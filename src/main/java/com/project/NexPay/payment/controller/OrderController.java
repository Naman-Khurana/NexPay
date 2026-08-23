package com.project.NexPay.payment.controller;

import com.project.NexPay.payment.dto.request.CreateOrderRequest;
import com.project.NexPay.payment.dto.response.OrderResponse;
import com.project.NexPay.payment.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${mock.merchantId}")
    private UUID merchantId; //TODO : replace and get merchant id from merchant context
//    UUID merchantId= UUID.fromString("5ed2d6a6-5194-408e-b87e-db5b7bb5cc77"); //TODO : replace and get merchant id from merchant context

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody @Valid CreateOrderRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.create( merchantId,request));
    }

}
