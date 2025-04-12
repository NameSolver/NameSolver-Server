package com.dongdong.nameSolver.redis;

import com.dongdong.nameSolver.global.util.RedisUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
public class RedisTest {
    @Autowired
    private RedisUtil redisUtil;

    @Test
    void 레디스() {
        String key = "name";

        redisUtil.setDate(key, "donghyun", 1000L);
        String value = redisUtil.getDate(key);
        Assertions.assertEquals(value, "donghyun");
    }

    @Test
    void 레디스_유효시간() {
        String key = "name";

        redisUtil.setDate(key, "donghyun", 1000L);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        String value = redisUtil.getDate(key);
        Assertions.assertEquals(value, null);
    }
}
