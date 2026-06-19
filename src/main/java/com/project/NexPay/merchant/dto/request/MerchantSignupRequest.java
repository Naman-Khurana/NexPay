package com.project.NexPay.merchant.dto.request;

import com.project.NexPay.comman.enums.BusinessType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MerchantSignupRequest(
        @NotNull(message = "Name should be provided")
        @Size(max = 50,message = "Name should not be of length more than 50 characters")
        String name,

        @NotNull(message = "Email is required")
        @Size(max = 50,message = "Email should not be of length more than 50 characters")
        String email,

        @NotNull(message = "Password is required")
        @Size(min = 8,message = "Password should be atleast 8 characters long")
        String password,

        @Size(max = 50, message = "Business Name cannot be of length more than 50 characters long")
        String businessName,

        BusinessType businessType

) {
}
