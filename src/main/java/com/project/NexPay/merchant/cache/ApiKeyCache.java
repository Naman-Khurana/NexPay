package com.project.NexPay.merchant.cache;

import com.project.NexPay.merchant.cache.entry.ApiKeyCacheEntry;

import java.util.Optional;

public interface ApiKeyCache {

    Optional<ApiKeyCacheEntry> get(String keyId);

    void put(String keyId, ApiKeyCacheEntry apiKeyCacheEntry);

    void evict(String keyId);

}
