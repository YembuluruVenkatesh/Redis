package com.example.productservice.service;

import com.example.productservice.constant.RedisSessionConstants;
import com.example.productservice.dto.UserSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionServiceImpl
        implements SessionService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public UserSession createSession(String username) {

        String sessionId = UUID.randomUUID().toString();

        UserSession session =
                UserSession.builder()
                        .sessionId(sessionId)
                        .username(username)
                        .loginTime(LocalDateTime.now())
                        .lastAccessTime(LocalDateTime.now())
                        .build();

        redisTemplate.opsForValue().set(
                RedisSessionConstants.SESSION_PREFIX + sessionId,
                session,
                RedisSessionConstants.SESSION_TTL);

        log.info("Session Created : {}", sessionId);

        return session;
    }

    @Override
    public UserSession getSession(String sessionId) {

        return (UserSession) redisTemplate.opsForValue()
                .get(RedisSessionConstants.SESSION_PREFIX + sessionId);
    }

    @Override
    public void refreshSession(String sessionId) {

        String key =
                RedisSessionConstants.SESSION_PREFIX + sessionId;

        UserSession session =
                (UserSession) redisTemplate.opsForValue().get(key);

        if (session != null) {

            session.setLastAccessTime(LocalDateTime.now());

            redisTemplate.opsForValue().set(
                    key,
                    session,
                    RedisSessionConstants.SESSION_TTL);

            log.info("Session Refreshed");
        }

    }

    @Override
    public void deleteSession(String sessionId) {

        redisTemplate.delete(
                RedisSessionConstants.SESSION_PREFIX + sessionId);

        log.info("Session Deleted");

    }

}