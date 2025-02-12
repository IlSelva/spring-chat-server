package com.git.ilselva.chat.server.model;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.ip.tcp.connection.TcpConnection;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import java.nio.charset.StandardCharsets;

public class UserSession {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserSession.class);
    private String username;
    private String currentRoom;
    private final TcpConnection connection;

    public UserSession(TcpConnection connection) {
        this.connection = connection;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public void sendResponse(String content) {
        try {
            byte[] payload = (content + "\r\n").getBytes(StandardCharsets.UTF_8);
            Message<byte[]> message = MessageBuilder.withPayload(payload).build();
            connection.send(message);
        } catch (Exception e) {
            LOGGER.error("Failed to send response", e);
        }
    }
}