package com.project.NexPay.merchant.service;

import com.project.NexPay.merchant.dto.request.MerchantSignupRequest;
import com.project.NexPay.merchant.dto.response.MerchantResponse;

public interface AuthService {

    MerchantResponse signup( MerchantSignupRequest merchantSignupRequest);
}
