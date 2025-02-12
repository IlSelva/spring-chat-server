package com.git.ilselva.chat.server.service.api;

import com.git.ilselva.chat.server.model.UserSession;

public interface UserSessionRegistry {

    void registerSession(String connectionId, UserSession userSession);

    void removeSession(String connectionId);

    UserSession getSessionById(String connectionId);

}