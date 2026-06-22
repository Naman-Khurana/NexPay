package com.project.NexPay.merchant.service;

import com.project.NexPay.merchant.dto.request.ApiKeyCreateRequest;
import com.project.NexPay.merchant.dto.response.ApiKeyCreateResponse;
import com.project.NexPay.merchant.dto.response.ApiKeyResponse;

import java.util.List;
import java.util.UUID;

public interface ApiKeyService {
    ApiKeyCreateResponse create(UUID merchantId, ApiKeyCreateRequest request);

    List<ApiKeyResponse> listByMerchant(UUID merchantId);

    void revoke(UUID merchantId, UUID keyId);

    ApiKeyCreateResponse rotate(UUID merchantId, UUID keyId);
}
