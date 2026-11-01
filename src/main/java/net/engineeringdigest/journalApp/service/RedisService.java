package net.engineeringdigest.journalApp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public <T> Optional<T> get(String key, Class<T> entityClass) {
        try {
            String value = redisTemplate.opsForValue().get(key);
            return value == null
                    ? Optional.empty()
                    : Optional.ofNullable(objectMapper.readValue(value, entityClass));
        } catch (RuntimeException exception) {
            log.warn("Redis read failed for key {}; treating it as a cache miss", key, exception);
            return Optional.empty();
        }
    }

    public boolean set(String key, Object value, Duration ttl) {
        if (ttl == null || ttl.isZero() || ttl.isNegative()) {
            throw new IllegalArgumentException("Redis TTL must be positive");
        }
        try {
            redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(value), ttl);
            return true;
        } catch (RuntimeException exception) {
            log.error("Redis write failed for key {}", key, exception);
            return false;
        }
    }

    public void delete(String key) {
        try {
            redisTemplate.delete(key);
        } catch (RuntimeException exception) {
            log.warn("Redis delete failed for key {}", key, exception);
        }
    }
}
