package com.project.NexPay.comman.idempotency;

import java.time.Duration;
import java.util.Optional;

public interface IdempotencyStore {

    boolean setIfAbsent(String key, Duration ttl);

    void store(String key, String value, Duration ttl);

    Optional<String> get(String key);

    void delete(String key);

}
