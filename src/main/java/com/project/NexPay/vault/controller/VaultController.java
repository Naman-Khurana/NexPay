package com.project.NexPay.vault.controller;


import com.project.NexPay.vault.dto.request.TokenizeRequest;
import com.project.NexPay.vault.dto.response.TokenizeResponse;
import com.project.NexPay.vault.service.VaultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/vault")
@RequiredArgsConstructor
public class VaultController {

    private final VaultService vaultService;

    @Value("${mock.merchantId}")
    private UUID merchantId; //TODO : replace and get merchant id from merchant context
//    UUID merchantId = UUID.fromString("5ed2d6a6-5194-408e-b87e-db5b7bb5cc77"); //TODO : replace and get merchant id from merchant context

    public ResponseEntity<TokenizeResponse> tokenize(@RequestBody @Valid TokenizeRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(vaultService.tokenize(request,merchantId));
    }
}
