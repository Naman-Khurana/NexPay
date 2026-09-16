package com.project.NexPay.comman.ratelimit;

public interface RateLimiter {
    RateLimitResult check(String key, int maxRequestAllowed, long windowSeconds );
}
