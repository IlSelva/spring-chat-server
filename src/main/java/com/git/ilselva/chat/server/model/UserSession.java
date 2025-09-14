package com.git.ilselva.chat.server.model;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.ip.tcp.connection.TcpConnection;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.nio.charset.StandardCharsets;

public class UserSession {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserSession.class);
    private User user;
    private String currentRoom;
    private final TcpConnection connection;

    public UserSession(TcpConnection connection) {
        this.connection = connection;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(String currentRoom) {
        this.currentRoom = currentRoom;
    }

    public TcpConnection getConnection() {
        return connection;
    }

    public String getConnectionId() {
        return connection.getConnectionId();
    }

    public void sendResponse(String content) {
        try {
            byte[] payload = (content + "\r\n").getBytes(StandardCharsets.UTF_8);
            Message<byte[]> message = MessageBuilder.withPayload(payload).build();
            connection.send(message);
        } catch (Exception e) {
            LOGGER.error("Failed to send response", e);
        }
    }

    public void sendClosingResponse(String content) {
        sendResponse(content);
        closeSession();
    }

    public void closeSession() {
        try {
            connection.close();
        } catch (Exception e) {
            LOGGER.warn("Failed to close session", e);
        }
    }

}