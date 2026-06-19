package com.project.NexPay.merchant.service.impl;

import com.project.NexPay.comman.enums.UserRole;
import com.project.NexPay.comman.exception.DuplicateResourceException;
import com.project.NexPay.merchant.dto.request.MerchantSignupRequest;
import com.project.NexPay.merchant.dto.response.MerchantResponse;
import com.project.NexPay.merchant.entity.AppUser;
import com.project.NexPay.merchant.entity.Merchant;
import com.project.NexPay.merchant.repository.AppUserRespository;
import com.project.NexPay.merchant.repository.MerchantRepository;
import com.project.NexPay.merchant.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final MerchantRepository merchantRepository;
    private final AppUserRespository appUserRespository;

    @Transactional
    @Override
    public MerchantResponse signup(MerchantSignupRequest merchantSignupRequest) {
        if(merchantRepository.existsByEmail(merchantSignupRequest.email())){
            throw new DuplicateResourceException("DUPLICATE_MERCHANT_EMAIL","Merchant with email already exists : "+ merchantSignupRequest.email());
        }

        Merchant merchant= Merchant
                .builder()
                .businessName(merchantSignupRequest.businessName())
                .businessType(merchantSignupRequest.businessType())
                .name(merchantSignupRequest.name())
                .email(merchantSignupRequest.email())
                .build();

        merchantRepository.save(merchant);

        AppUser appUser= AppUser
                .builder()
                .role(UserRole.OWNER)
                .merchant(merchant)
                .email(merchantSignupRequest.email())
                .passwordHash(merchantSignupRequest.password()) //TODO :  store password in hashed form
                .build();

        appUserRespository.save(appUser);

        //TODO : change and do conversion from merchant -> merchant response using mapper
        return new MerchantResponse(merchant.getId(),merchant.getName(),merchant.getEmail(),appUser.getPasswordHash(),merchant.getBusinessName(),merchant.getBusinessType());


    }
}
