package com.project.NexPay.comman.exception;

import lombok.Getter;

@Getter
public class RateLimitException extends RuntimeException {
    private final int retryAfterSeconds;
    private final int remaining;

    public RateLimitException(String message, int retryAfterSeconds) {
        this.retryAfterSeconds = retryAfterSeconds;
        this.remaining = 0;
    }
}
