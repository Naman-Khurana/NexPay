package com.project.NexPay.merchant.controller;

import com.project.NexPay.merchant.dto.request.ApiKeyCreateRequest;
import com.project.NexPay.merchant.dto.response.ApiKeyCreateResponse;
import com.project.NexPay.merchant.dto.response.ApiKeyResponse;
import com.project.NexPay.merchant.service.ApiKeyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/merchants/{merchantId}/api-keys")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    @PostMapping
    public ResponseEntity<ApiKeyCreateResponse> create(@PathVariable UUID merchantId,
                                                       @RequestBody @Valid ApiKeyCreateRequest request){

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(apiKeyService.create(merchantId,request));



    }

    @GetMapping
    public ResponseEntity<List<ApiKeyResponse>> list(@PathVariable UUID merchantId){
        return ResponseEntity.ok(apiKeyService.listByMerchant(merchantId));
    }

    @DeleteMapping("/{keyId}")
    public ResponseEntity<Void> revoke(@PathVariable UUID merchantId, @PathVariable UUID keyId){
        apiKeyService.revoke(merchantId,keyId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{keyId}/rotate")
    public ResponseEntity<ApiKeyCreateResponse> rotate(@PathVariable UUID merchantId, @PathVariable UUID keyId){
        return ResponseEntity.ok(apiKeyService.rotate(merchantId,keyId));
    }


}
