package com.project.NexPay.vault.service;

import com.project.NexPay.comman.entity.Money;
import com.project.NexPay.payment.processor.dto.PaymentProcessorResponse;
import com.project.NexPay.vault.dto.request.TokenizeRequest;
import com.project.NexPay.vault.dto.response.TokenizeResponse;
import jakarta.validation.Valid;

import java.util.Map;
import java.util.UUID;

public interface VaultService {
    TokenizeResponse tokenize(@Valid TokenizeRequest request, UUID merchantId);

    PaymentProcessorResponse charge(UUID uuid, String token, Money amount, Map<String, Object> methodDetails);
}
