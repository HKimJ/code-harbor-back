package com.example.codeHarbor.tool.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    protected final RedisTemplate<String, String> redisTemplate;

    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void storeDataInRedis(String email, String verifyCode, int expireTime) {
        redisTemplate.opsForValue().set(email, verifyCode, expireTime, TimeUnit.MINUTES);
    }

    public String retrieveDataFromRedis(String email) {
        return redisTemplate.opsForValue().get(email);
    }

    public String generateCode(String fragment) {
        Random random = new Random();
        int code = random.nextInt(9000) + 1000; // Generates a 4-digit number between 1000 and 9999
        return String.valueOf(fragment + code);
    }
}
