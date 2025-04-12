package com.dongdong.nameSolver.redis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
public class RedisTest {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    void 레디스() {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        String key = "name";
        valueOperations.set(key, "giraffe");
        String value = valueOperations.get(key);
        Assertions.assertEquals(value, "giraffe");
    }
}
