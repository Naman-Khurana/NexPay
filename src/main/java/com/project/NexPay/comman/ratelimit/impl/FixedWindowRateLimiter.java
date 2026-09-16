package com.project.NexPay.comman.ratelimit.impl;

import com.project.NexPay.comman.ratelimit.RateLimitResult;
import com.project.NexPay.comman.ratelimit.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "app.rate-limit.method", havingValue = "fixed")
public class FixedWindowRateLimiter implements RateLimiter {

    private final StringRedisTemplate redis;

    @Override
    public RateLimitResult check(String key, int maxRequestAllowed, long windowSeconds) {
        String redisKey = "ratelimit:fixed:" + key;

        Long count = redis.opsForValue().increment(redisKey);

        // key not exists in redis : default -> 0 | redis unavailable -> null
        if(count == null)   return RateLimitResult.allowed(maxRequestAllowed); //redis unavailable -> fail open

        if(count == 1) {
            redis.expire(redisKey, Duration.ofSeconds(windowSeconds));
        }

        if(count > maxRequestAllowed){
            Long ttl = redis.getExpire(redisKey, TimeUnit.SECONDS);
            int retryAfter = (ttl != null && ttl > 0) ? ttl.intValue() : (int)windowSeconds;
            return RateLimitResult.denied(retryAfter);
        }

        return RateLimitResult.allowed( (int)( maxRequestAllowed - count ) );
    }
}
