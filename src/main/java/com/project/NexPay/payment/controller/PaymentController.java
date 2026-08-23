package com.project.NexPay.payment.controller;

import com.project.NexPay.payment.dto.request.PaymentInitRequest;
import com.project.NexPay.payment.dto.response.PaymentResponse;
import com.project.NexPay.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/payments")
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @Value("${mock.merchantId}")
    private UUID merchantId; //TODO : replace and get merchant id from merchant context
//    private UUID merchantId = UUID.fromString("5ed2d6a6-5194-408e-b87e-db5b7bb5cc77"); //TODO : replace and get merchant id from merchant context

    @PostMapping()
    public ResponseEntity<PaymentResponse> initiate(@Valid @RequestBody PaymentInitRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(paymentService.initiate(merchantId, request));
    }


    @PostMapping("/{paymentId}/capture")
    public ResponseEntity<PaymentResponse> capture(@PathVariable UUID paymentId){
        return ResponseEntity.ok(paymentService.capture(paymentId,merchantId    ));

    }
}
