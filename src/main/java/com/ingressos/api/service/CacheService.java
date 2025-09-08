package com.ingressos.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class CacheService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisTemplate<String, String> stringRedisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    // Operações básicas de cache
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, Object value, Duration timeout) {
        redisTemplate.opsForValue().set(key, value, timeout);
    }

    public void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public <T> T get(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        
        try {
            if (clazz.isInstance(value)) {
                return clazz.cast(value);
            }
            // Tentar converter usando ObjectMapper
            String json = objectMapper.writeValueAsString(value);
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    public long delete(String... keys) {
        return redisTemplate.delete(java.util.Arrays.asList(keys));
    }

    public boolean expire(String key, Duration timeout) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, timeout));
    }

    public boolean expire(String key, long timeout, TimeUnit unit) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, timeout, unit));
    }

    public long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    // Operações específicas para eventos
    public void cacheEvent(String eventId, Object event) {
        set("event:" + eventId, event, Duration.ofHours(1));
    }

    public <T> T getEvent(String eventId, Class<T> clazz) {
        return get("event:" + eventId, clazz);
    }

    public void invalidateEvent(String eventId) {
        delete("event:" + eventId);
    }

    // Operações para eventos populares
    public void cachePopularEvents(Object events) {
        set("popular-events", events, Duration.ofHours(2));
    }

    public <T> T getPopularEvents(Class<T> clazz) {
        return get("popular-events", clazz);
    }

    public void invalidatePopularEvents() {
        delete("popular-events");
    }

    // Operações para usuários
    public void cacheUser(String userId, Object user) {
        set("user:" + userId, user, Duration.ofMinutes(30));
    }

    public <T> T getUser(String userId, Class<T> clazz) {
        return get("user:" + userId, clazz);
    }

    public void invalidateUser(String userId) {
        delete("user:" + userId);
    }

    // Operações para sessões JWT
    public void cacheJwtSession(String sessionId, String token) {
        stringRedisTemplate.opsForValue().set("jwt:" + sessionId, token, Duration.ofHours(24));
    }

    public String getJwtSession(String sessionId) {
        return stringRedisTemplate.opsForValue().get("jwt:" + sessionId);
    }

    public void invalidateJwtSession(String sessionId) {
        stringRedisTemplate.delete("jwt:" + sessionId);
    }

    // Operações para refresh tokens
    public void cacheRefreshToken(String tokenId, String userId) {
        stringRedisTemplate.opsForValue().set("refresh:" + tokenId, userId, Duration.ofDays(7));
    }

    public String getRefreshToken(String tokenId) {
        return stringRedisTemplate.opsForValue().get("refresh:" + tokenId);
    }

    public void invalidateRefreshToken(String tokenId) {
        stringRedisTemplate.delete("refresh:" + tokenId);
    }

    // Operações para QR codes
    public void cacheQrCode(String ticketId, String qrCodeData) {
        stringRedisTemplate.opsForValue().set("qr:" + ticketId, qrCodeData, Duration.ofHours(1));
    }

    public String getQrCode(String ticketId) {
        return stringRedisTemplate.opsForValue().get("qr:" + ticketId);
    }

    public void invalidateQrCode(String ticketId) {
        stringRedisTemplate.delete("qr:" + ticketId);
    }

    // Operações para estatísticas
    public void cacheStatistics(String key, Object statistics) {
        set("stats:" + key, statistics, Duration.ofMinutes(5));
    }

    public <T> T getStatistics(String key, Class<T> clazz) {
        return get("stats:" + key, clazz);
    }

    public void invalidateStatistics(String key) {
        delete("stats:" + key);
    }

    // Operações para pagamentos
    public void cachePayment(String paymentId, Object payment) {
        set("payment:" + paymentId, payment, Duration.ofMinutes(10));
    }

    public <T> T getPayment(String paymentId, Class<T> clazz) {
        return get("payment:" + paymentId, clazz);
    }

    public void invalidatePayment(String paymentId) {
        delete("payment:" + paymentId);
    }

    // Operações de limpeza
    public void clearAllCache() {
        Set<String> keys = redisTemplate.keys("*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    public void clearCacheByPattern(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    // Operações de contador
    public long increment(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    public long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    public long decrement(String key) {
        return redisTemplate.opsForValue().decrement(key);
    }

    public long decrement(String key, long delta) {
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    // Operações de lista
    public long listPush(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    public Object listPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    public long listSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    // Operações de set
    public long setAdd(String key, Object... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    public boolean setIsMember(String key, Object value) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, value));
    }

    public long setRemove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    public long setSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    // Operações de hash
    public void hashPut(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    public Object hashGet(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    public boolean hashHasKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    public long hashDelete(String key, Object... hashKeys) {
        return redisTemplate.opsForHash().delete(key, hashKeys);
    }

    public long hashSize(String key) {
        return redisTemplate.opsForHash().size(key);
    }
}