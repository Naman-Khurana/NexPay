package com.project.NexPay.vault.service;

import com.project.NexPay.vault.dto.request.TokenizeRequest;
import com.project.NexPay.vault.dto.response.TokenizeResponse;
import jakarta.validation.Valid;

import java.util.UUID;

public interface VaultService {
    TokenizeResponse tokenize(@Valid TokenizeRequest request, UUID merchantId);
}
