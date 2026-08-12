package com.project.NexPay.vault.service.impl;

import com.project.NexPay.comman.enums.CardBrand;
import com.project.NexPay.comman.util.RandomizerUtil;
import com.project.NexPay.vault.config.VaultEncrypterConfig;
import com.project.NexPay.vault.dto.request.TokenizeRequest;
import com.project.NexPay.vault.dto.response.TokenizeResponse;
import com.project.NexPay.vault.entity.CardToken;
import com.project.NexPay.vault.entity.VaultCard;
import com.project.NexPay.vault.repository.CardTokenRepository;
import com.project.NexPay.vault.repository.VaultCardRepository;
import com.project.NexPay.vault.service.VaultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VaultServiceImpl implements VaultService {

    private final VaultCardRepository vaultCardRepository;
    private final CardTokenRepository cardTokenRepository;
    private final BytesEncryptor dekEncryptor;


    @Override
    @Transactional
    public TokenizeResponse tokenize(TokenizeRequest request, UUID merchantId) {

        String lastFour= request.pan().substring(request.pan().length() -4);
        String bin =  request.pan().substring(0,6); //first 6 digits of PAN
        CardBrand cardBrand=CardBrand.detectBrand(request.pan());

        byte[] dek= KeyGenerators.secureRandom(12).generateKey();
        byte[] encryptedPan= VaultEncrypterConfig
                .panEncryptor(dek)
                .encrypt(request.pan()
                        .getBytes(StandardCharsets.UTF_8));

        byte[] encryptedDek=dekEncryptor.encrypt(dek);

        VaultCard vaultCard = VaultCard.builder()
                .brand(cardBrand)
                .expiryMonth(request.expiryMonth().toString())
                .expiryYear(request.expiryYear().toString())
                .bin(bin)
                .lastFour(lastFour)
                .encryptedPan(encryptedPan)
                .encryptedDek(encryptedDek)
                .cardHolderName(request.cardHolderName())
                .build();

        String token = "tok_" + RandomizerUtil.randomBase64(32);

        cardTokenRepository.save(CardToken.builder()
                .vaultCard(vaultCard)
                .token(token)
                .merchantId(merchantId)
                .customerId(request.customerId())
                .build());


        return new TokenizeResponse(token,lastFour,cardBrand, request.expiryMonth(), request.expiryYear());

    }


}
