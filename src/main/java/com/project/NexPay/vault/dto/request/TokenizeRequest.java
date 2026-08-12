package com.project.NexPay.vault.dto.request;

import com.project.NexPay.vault.validation.ExpiryYear;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.LuhnCheck;

import java.util.UUID;

public record TokenizeRequest(

        @NotBlank(message = "PAN is required")
        @LuhnCheck(message = "Invalid card number")
        @Pattern(regexp = "^[0-9]{13,19}$", message = "PAN length is invalid" )
        String pan,

        @NotBlank(message = "CVV is required")
        @Pattern(regexp = "^[0-9]{3,4}$", message = "CVV length is invalid")
        String cvv,

        @NotNull(message = "Expiry month is required")
        @Min(value = 1, message = "Expiry must be between 1 to 12")
        @Max(value= 12, message = "Expiry must be between 1 to 12")
        Integer expiryMonth,

        @NotNull(message = "Expiry Year is required")
        @ExpiryYear
        Integer expiryYear,

        UUID customerId,

        @Size(min = 3, message = "Card Holder name should have minimum length of 3")
        String cardHolderName
) {
}
