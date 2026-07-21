package com.project.NexPay.merchant.service.impl;

import com.project.NexPay.comman.enums.MerchantStatus;
import com.project.NexPay.comman.enums.UserRole;
import com.project.NexPay.comman.exception.DuplicateResourceException;
import com.project.NexPay.merchant.dto.request.MerchantSignupRequest;
import com.project.NexPay.merchant.dto.response.MerchantResponse;
import com.project.NexPay.merchant.entity.AppUser;
import com.project.NexPay.merchant.entity.Merchant;
import com.project.NexPay.merchant.mapper.MerchantMapper;
import com.project.NexPay.merchant.repository.AppUserRespository;
import com.project.NexPay.merchant.repository.MerchantRepository;
import com.project.NexPay.merchant.service.AuthService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final MerchantRepository merchantRepository;
    private final AppUserRespository appUserRespository;
    private final MerchantMapper merchantMapper;

    @Transactional
    @Override
    public MerchantResponse signup(MerchantSignupRequest merchantSignupRequest) {
        if(merchantRepository.existsByEmail(merchantSignupRequest.email())){
            throw new DuplicateResourceException("DUPLICATE_MERCHANT_EMAIL","Merchant with email already exists : "+ merchantSignupRequest.email());
        }

        Merchant merchant= merchantMapper.toEntityFromSignUpRequest(merchantSignupRequest);
        merchant.setStatus(MerchantStatus.KYC_PENDING);

        merchantRepository.save(merchant);

        AppUser appUser= AppUser
                .builder()
                .role(UserRole.OWNER)
                .merchant(merchant)
                .email(merchantSignupRequest.email())
                .passwordHash(merchantSignupRequest.password()) //TODO :  store password in hashed form
                .build();

        appUserRespository.save(appUser);

        return merchantMapper.toResponse(merchant);

    }
}
