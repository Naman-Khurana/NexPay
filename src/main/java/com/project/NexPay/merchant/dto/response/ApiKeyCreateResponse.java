package com.project.NexPay.merchant.dto.response;

import com.project.NexPay.comman.enums.Environment;

import java.util.UUID;

public record ApiKeyCreateResponse (
        UUID id,
        String keyId,
        String keySecret,
        Environment environment

){
}
