package com.nals.tf7.service.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public Object getValue(final Object key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void setValue(final String key, final Object value, final long durationInSeconds) {
        redisTemplate.opsForValue()
                     .set(key, value, Duration.ofSeconds(durationInSeconds));
    }
}
