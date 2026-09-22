package com.project.NexPay.comman.idempotency;

import com.project.NexPay.comman.Constants.Idempotency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisIdempotencyStore implements IdempotencyStore{

    private final StringRedisTemplate redis;

    @Override
    public boolean setIfAbsent(String key, Duration ttl) {
        try {
            Boolean set = redis.opsForValue().setIfAbsent(Idempotency.IDEMPOTENCY_PREFIX + key, Idempotency.IN_PROGRESS, ttl);
            return Boolean.TRUE.equals(set);

        }catch (Exception e){
            log.warn("Idempotency store unavailable, failing open for key={}", key,e);
            return true;
        }
    }

    @Override
    public void store(String key, String value, Duration ttl) {
        try {
            redis.opsForValue().set(Idempotency.IDEMPOTENCY_PREFIX + key, value, ttl);
        }catch (Exception e){
            log.warn("Failed to persist, failing open for key={}", key, e);
        }
    }

    @Override
    public Optional<String> get(String key) {

        try {
            return Optional.ofNullable(redis.opsForValue().get(Idempotency.IDEMPOTENCY_PREFIX + key));
        } catch (Exception e) {
            log.warn("Failed to fetch from idempotency store, failing open for key={}", key, e);
            return Optional.empty();
        }
    }

    @Override
    public void delete(String key) {
        try {
            redis.delete(Idempotency.IDEMPOTENCY_PREFIX + key);
        } catch (Exception e) {
            log.warn("Failed to clear idempotency key={}", key, e);
        }

    }
}
