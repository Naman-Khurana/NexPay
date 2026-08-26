package com.project.NexPay.merchant.security;

import com.project.NexPay.comman.exception.ResourceNotFoundException;
import com.project.NexPay.merchant.entity.AppUser;
import com.project.NexPay.merchant.repository.AppUserRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MerchantUserDetailsService implements UserDetailsService {

    private final AppUserRespository appUserRespository;

    @Override
    public AppUser loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRespository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User",email)
        );
    }
}
