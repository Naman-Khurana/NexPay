package com.project.NexPay.merchant.cache;

import com.project.NexPay.merchant.cache.entry.ApiKeyCacheEntry;
import com.project.NexPay.merchant.entity.ApiKey;

import java.util.Optional;

public interface ApiKeyCache {

    Optional<ApiKeyCacheEntry> get(String keyId);

    void update(String keyId, ApiKeyCacheEntry apiKeyCacheEntry);

    void evict(String keyId);

}
