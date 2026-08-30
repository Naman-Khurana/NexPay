package com.project.NexPay.comman.audit;

import com.project.NexPay.merchant.security.MerchantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.project.NexPay.comman.Constants.Audit.SYSTEM;

@Component("auditorAwareImpl")
@RequiredArgsConstructor
@Slf4j
public class AuditorAwareImpl implements AuditorAware<String> {
    private final MerchantContext merchantContext;

    @Override
    public Optional getCurrentAuditor() {
        try {
            String keyId = merchantContext.getKeyId();
            if(keyId != null && !keyId.isBlank() ) return Optional.of(merchantContext.getKeyId());

            if(merchantContext.getMerchantId() != null ) {
                return Optional.of( "merchant_id: " + merchantContext.getMerchantId());
            }
        } catch (Exception e) {
            // do nothing
        }

        return Optional.of(SYSTEM);
    }
}
