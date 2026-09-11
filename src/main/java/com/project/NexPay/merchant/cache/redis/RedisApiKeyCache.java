package com.project.NexPay.merchant.cache.redis;

import com.project.NexPay.merchant.cache.ApiKeyCache;
import com.project.NexPay.merchant.cache.entry.ApiKeyCacheEntry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisApiKeyCache implements ApiKeyCache {

    private static final String PREFIX = "apikey:";
    private static final Duration TTL = Duration.ofMinutes(5);

    private StringRedisTemplate stringRedisTemplate;
    private ObjectMapper objectMapper;

    @Override
    public Optional<ApiKeyCacheEntry> get(String keyId) {
        try {
            String json =stringRedisTemplate.opsForValue().get( PREFIX + keyId );
            if(json == null) {
                 return Optional.empty();
            }
            return Optional.of(objectMapper.readValue(json, ApiKeyCacheEntry.class));
        } catch (Exception e) {
            log.warn("ApiKey cache failed, keyId: {}" , keyId);
            return Optional.empty();

        }


    }

    @Override
    public void update(String keyId, ApiKeyCacheEntry apiKeyCacheEntry) {

    }

    @Override
    public void evict(String keyId) {

    }
}
