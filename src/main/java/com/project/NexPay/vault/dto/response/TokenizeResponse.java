package com.project.NexPay.vault.dto.response;

import com.project.NexPay.comman.enums.CardBrand;

public record TokenizeResponse(

        String token,
        String lastFour,
        CardBrand brand,
        Integer expiryMonth,
        Integer expiryYear

) {
}
