package com.example.productservice.controller;

import com.example.productservice.dto.UserSession;
import com.example.productservice.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @PostMapping("/{username}")
    public UserSession login(
            @PathVariable String username) {

        return sessionService.createSession(username);
    }

    @GetMapping("/{sessionId}")
    public UserSession getSession(
            @PathVariable String sessionId) {

        return sessionService.getSession(sessionId);
    }

    @PutMapping("/{sessionId}")
    public String refreshSession(
            @PathVariable String sessionId) {

        sessionService.refreshSession(sessionId);

        return "Session Refreshed";
    }

    @DeleteMapping("/{sessionId}")
    public String logout(
            @PathVariable String sessionId) {

        sessionService.deleteSession(sessionId);

        return "Session Deleted";
    }

}