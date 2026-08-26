package com.project.NexPay.merchant.service.impl;

import com.project.NexPay.comman.enums.MerchantStatus;
import com.project.NexPay.comman.enums.UserRole;
import com.project.NexPay.comman.exception.DuplicateResourceException;
import com.project.NexPay.comman.exception.ResourceNotFoundException;
import com.project.NexPay.merchant.dto.request.LoginRequest;
import com.project.NexPay.merchant.dto.request.MerchantSignupRequest;
import com.project.NexPay.merchant.dto.response.LoginResponse;
import com.project.NexPay.merchant.dto.response.MerchantResponse;
import com.project.NexPay.merchant.entity.AppUser;
import com.project.NexPay.merchant.entity.Merchant;
import com.project.NexPay.merchant.mapper.MerchantMapper;
import com.project.NexPay.merchant.repository.AppUserRespository;
import com.project.NexPay.merchant.repository.MerchantRepository;
import com.project.NexPay.merchant.security.JwtUtil;
import com.project.NexPay.merchant.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final MerchantRepository merchantRepository;
    private final AppUserRespository appUserRespository;
    private final MerchantMapper merchantMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

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
                .passwordHash(passwordEncoder.encode(merchantSignupRequest.password()))
                .build();

        appUserRespository.save(appUser);

        return merchantMapper.toResponse(merchant);

    }

    @Override
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(),request.password())
        );

        AppUser appUser= appUserRespository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User",request.email()));

        String accessToken = jwtUtil.generateAccessToken(
                request.email(),appUser.getMerchant().getId(),appUser.getRole().toString());

        return new LoginResponse(accessToken);
    }
}
