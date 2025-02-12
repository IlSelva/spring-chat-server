package com.git.ilselva.chat.server.service.impl;

import com.git.ilselva.chat.server.model.UserSession;
import com.git.ilselva.chat.server.service.api.UserSessionRegistry;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class DefaultUserSessionRegistry implements UserSessionRegistry {
    private final ConcurrentHashMap<String, UserSession> activeSessions = new ConcurrentHashMap<>();

    @Override
    public void registerSession(String connectionId, UserSession userSession) {
        activeSessions.put(connectionId, userSession);
    }

    @Override
    public void removeSession(String connectionId) {
        activeSessions.remove(connectionId);
    }

    @Override
    public UserSession getSessionById(String connectionId) {
        return activeSessions.get(connectionId);
    }

}