package com.project.NexPay.merchant.cache.entry;

import com.project.NexPay.comman.enums.Environment;

import java.time.LocalDateTime;

public record ApiKeyCacheEntry (

        String keyId,
        String keySecretHash,
        String previousKeySecretHash,
        LocalDateTime gracePeriodExpiresAt,
        Environment environment,
        boolean enabled

) {
    public boolean isInGracePeriod(){
        return gracePeriodExpiresAt != null && LocalDateTime.now().isBefore(gracePeriodExpiresAt);
    }
}
