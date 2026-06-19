package com.project.NexPay.merchant.dto.response;

import com.project.NexPay.comman.enums.BusinessType;

import java.util.UUID;

public record MerchantResponse(
        UUID id,
        String name,
        String email,
        String password,
        String businessName,
        BusinessType businessType

) {
}
