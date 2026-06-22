package com.project.NexPay.merchant.service.impl;

import com.project.NexPay.comman.exception.ResourceNotFoundException;
import com.project.NexPay.comman.util.RandomizerUtil;
import com.project.NexPay.merchant.dto.request.ApiKeyCreateRequest;
import com.project.NexPay.merchant.dto.response.ApiKeyCreateResponse;
import com.project.NexPay.merchant.dto.response.ApiKeyResponse;
import com.project.NexPay.merchant.entity.ApiKey;
import com.project.NexPay.merchant.entity.Merchant;
import com.project.NexPay.merchant.repository.ApiKeyRepository;
import com.project.NexPay.merchant.repository.MerchantRepository;
import com.project.NexPay.merchant.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ApiKeyServiceImpl implements ApiKeyService {

    private final MerchantRepository merchantRepository;
    private final ApiKeyRepository apiKeyRepository;



    @Transactional
    @Override
    public ApiKeyCreateResponse create(UUID merchantId, ApiKeyCreateRequest request) {
        Merchant merchant = merchantRepository.findById(merchantId).orElseThrow(() -> new ResourceNotFoundException("merchant",merchantId));

        String keyId= "nxp_" +request.environment().name().toUpperCase()+ RandomizerUtil.randomBase64(24); // will return 32 lenght string
        String rawSecret=RandomizerUtil.randomBase64(40);

        ApiKey apiKey= ApiKey
                .builder()
                .merchant(merchant)
                .keyId(keyId)
                .keySecretHash(rawSecret) //TODO : encode with BcryptEncoder
                .environment(request.environment())
                .build();

        apiKeyRepository.save(apiKey);
        return new ApiKeyCreateResponse(apiKey.getId(), keyId,rawSecret,request.environment());
    }

    @Override
    public List<ApiKeyResponse> listByMerchant(UUID merchantId) {


        return apiKeyRepository.findByMerchant_Id(merchantId)
                .stream()
                .map(apiKey -> new ApiKeyResponse(
                        apiKey.getId(),
                        apiKey.getKeyId(),
                        apiKey.getEnvironment(),
                        apiKey.isEnabled(),
                        apiKey.getLastUsedAt(),
                        null
                )).toList();
    }

    @Transactional
    @Override
    public void revoke(UUID merchantId, UUID keyId) {
        ApiKey apiKey= apiKeyRepository.findByIdAndMerchant_Id(keyId,merchantId).orElseThrow(()->
                new ResourceNotFoundException("ApiKey" ,keyId)
        );

        apiKey.setEnabled(false);

    }

    @Transactional
    @Override
    public ApiKeyCreateResponse rotate(UUID merchantId, UUID keyId) {
        ApiKey apiKey= apiKeyRepository.findByIdAndMerchant_Id(keyId,merchantId).orElseThrow(()->
                new ResourceNotFoundException("ApiKey" ,keyId)
        );

        String newRawKeySecretHash = RandomizerUtil.randomBase64(40);

        apiKey.setPreviousKeySecretHash(apiKey.getKeySecretHash());
        apiKey.setKeySecretHash(newRawKeySecretHash);
        apiKey.setRotatedAt(LocalDateTime.now());
        apiKey.setGracePeriodExpiresAt(LocalDateTime.now().plusDays(2));

        return new ApiKeyCreateResponse(apiKey.getId(), apiKey.getKeyId(),apiKey.getKeySecretHash(),apiKey.getEnvironment());



    }


}
