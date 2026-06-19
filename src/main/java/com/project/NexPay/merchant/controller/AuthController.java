package com.project.NexPay.merchant.controller;

import com.project.NexPay.merchant.dto.request.MerchantSignupRequest;
import com.project.NexPay.merchant.dto.response.MerchantResponse;
import com.project.NexPay.merchant.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<MerchantResponse> signup(@RequestBody @Valid MerchantSignupRequest merchantSignupRequest){

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.signup(merchantSignupRequest));
    }
}
