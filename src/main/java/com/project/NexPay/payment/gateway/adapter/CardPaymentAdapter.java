package com.project.NexPay.payment.gateway.adapter;

import com.project.NexPay.comman.Constants;
import com.project.NexPay.comman.entity.Money;
import com.project.NexPay.comman.exception.ErrorCodes;
import com.project.NexPay.comman.exception.ResourceNotFoundException;
import com.project.NexPay.payment.gateway.PaymentAdapter;
import com.project.NexPay.payment.gateway.dto.PaymentRequest;
import com.project.NexPay.payment.gateway.dto.PaymentResult;
import com.project.NexPay.payment.processor.PaymentProcessor;
import com.project.NexPay.payment.processor.PaymentProcessorRouter;
import com.project.NexPay.payment.processor.dto.PaymentProcessorRequest;
import com.project.NexPay.payment.processor.dto.PaymentProcessorResponse;
import com.project.NexPay.vault.config.VaultEncrypterConfig;
import com.project.NexPay.vault.entity.CardToken;
import com.project.NexPay.vault.entity.VaultCard;
import com.project.NexPay.vault.repository.CardTokenRepository;
import com.project.NexPay.vault.service.VaultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class CardPaymentAdapter implements PaymentAdapter {

    private final CardTokenRepository cardTokenRepository;
    private final BytesEncryptor dekEncrypter;
    private final PaymentProcessorRouter paymentProcessorRouter;
    private final VaultService vaultService;

    @Override
    public PaymentResult initiate(PaymentRequest request) {
        String token = request.methodDetails().get(Constants.Card.TOKEN).toString();

        PaymentProcessorResponse response=vaultService.charge(
                request.paymentId(),token, request.amount(),request.methodDetails()
        );

        return switch (response){
            case PaymentProcessorResponse.Success success -> new PaymentResult.Success(success.bankReference());
            case PaymentProcessorResponse.Failure failure -> new PaymentResult.Failure(failure.error(), failure.errorDescription());
            case PaymentProcessorResponse.Pending pending -> new PaymentResult.Pending(pending.processorReference());
        };
    }

    @Override
    public PaymentResult capture(UUID paymentId) {
        return null;
    }
}
