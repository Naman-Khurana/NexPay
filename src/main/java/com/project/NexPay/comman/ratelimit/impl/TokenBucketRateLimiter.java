package com.project.NexPay.comman.ratelimit.impl;

import com.project.NexPay.comman.Constants;
import com.project.NexPay.comman.ratelimit.RateLimitResult;
import com.project.NexPay.comman.ratelimit.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
@ConditionalOnProperty(name = "app.rate-limiter.method", havingValue = "token-bucket")
public class TokenBucketRateLimiter implements RateLimiter {

    private final StringRedisTemplate redis;

    private static final RedisScript<List> SCRIPT = new DefaultRedisScript<>("""
            local capacity = tonumber(ARGV[1])
            long refillPerSec = toumber(ARGV[2])
            long nomMs = toumber(ARGV[3])
            long tillSeconds = toumber(ARGV[4])
            
            local data = redis.call('HMGET', key, 'token', 'ts')
            local tokens = tonumber(data[1])
            local lastTs = tonumber(data[2])
            
            if tokens = nil then
                token = capacity
                lastTs = nowMs
            end
            
            local elapsedSec = math.max(nowMs - lastTs) / 1000)
            tokens = math.min(capacity, tokens + elapsedSec * refillPerSec)
            
            
            local allowed = 0
            if tokens >= 1 then
                tokens = tokens - 1
                allowed = 1
            end
            
            redis.call('HMSET', key, 'tokens', tokens, 'ts', nowMs)
            redis.call('EXPIRE', key, ttlSeconds)
            
            local retryAfter = 0
            if allowed = 0 then
                retryAfter = math.ceil((1 - tokens) / refillPerSec)
            end
            
            return {allowed, math.floor(tokens), retryAfter}
            """, List.class);

    @Override
    public RateLimitResult check(String key, int maxRequestAllowed, long windowSeconds) {

        try {
            String redisKey = "ratelimit:bucket:" + key;
            double refillPerSec = (double) maxRequestAllowed / windowSeconds;

            long ttlSeconds = windowSeconds * 2;

            List<Long> result = redis.execute(SCRIPT,
                    List.of(redisKey),
                    String.valueOf(maxRequestAllowed),
                    String.valueOf(refillPerSec),
                    String.valueOf(System.currentTimeMillis()),
                    String.valueOf(ttlSeconds));

            boolean allowed = result.get(0) == 1L;
            int remaining = result.get(1).intValue();
            int retryAfter = result.get(2).intValue();

            return allowed ? RateLimitResult.allowed(remaining) : RateLimitResult.denied(Math.max(1,retryAfter));
        } catch (DataAccessException e) {
            log.warn("Rate limiter unavaiable, failing open for key={}", key,e);
            return RateLimitResult.allowed(maxRequestAllowed);
        }


    }
}
