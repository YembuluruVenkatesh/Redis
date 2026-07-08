package com.example.productservice.service;

import com.example.productservice.dto.UserSession;

public interface SessionService {

    UserSession createSession(String username);

    UserSession getSession(String sessionId);

    void refreshSession(String sessionId);

    void deleteSession(String sessionId);

}