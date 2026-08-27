package com.project.NexPay.merchant.controller;

import com.project.NexPay.merchant.dto.request.ApiKeyCreateRequest;
import com.project.NexPay.merchant.dto.response.ApiKeyCreateResponse;
import com.project.NexPay.merchant.dto.response.ApiKeyResponse;
import com.project.NexPay.merchant.security.MerchantContext;
import com.project.NexPay.merchant.service.ApiKeyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/merchants/api-keys")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService apiKeyService;
    private final MerchantContext merchantContext;

    @PostMapping
    public ResponseEntity<ApiKeyCreateResponse> create(@RequestBody @Valid ApiKeyCreateRequest request){

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(apiKeyService.create(merchantContext.getMerchantId(),request));

    }

    @GetMapping
    public ResponseEntity<List<ApiKeyResponse>> list(){
        return ResponseEntity.ok(apiKeyService.listByMerchant(merchantContext.getMerchantId()));
    }

    @DeleteMapping("/{keyId}")
    public ResponseEntity<Void> revoke( @PathVariable UUID keyId){
        apiKeyService.revoke(merchantContext.getMerchantId(),keyId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{keyId}/rotate")
    public ResponseEntity<ApiKeyCreateResponse> rotate( @PathVariable UUID keyId){
        return ResponseEntity.ok(apiKeyService.rotate(merchantContext.getMerchantId(),keyId));
    }


}
