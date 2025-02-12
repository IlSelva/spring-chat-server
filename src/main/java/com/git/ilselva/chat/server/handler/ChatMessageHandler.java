package com.git.ilselva.chat.server.handler;

import com.git.ilselva.chat.server.model.UserSession;
import com.git.ilselva.chat.server.service.api.RoomRepository;
import com.git.ilselva.chat.server.service.impl.DefaultUserSessionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;
import reactor.util.annotation.NonNull;

import static org.springframework.integration.ip.IpHeaders.CONNECTION_ID;

@Component
public class ChatMessageHandler implements MessageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatMessageHandler.class);
    @Autowired
    private DefaultUserSessionRegistry sessionRegistry;
    @Autowired
    private RoomRepository roomRepository;

    @Override
    public void handleMessage(@NonNull Message<?> message) throws MessagingException {

        LOGGER.debug("received message: {}", message);

        String data = new String((byte[]) message.getPayload()).trim();
        String connectionId = (String) message.getHeaders().get(CONNECTION_ID);

        UserSession user = sessionRegistry.getSessionById(connectionId);
        LOGGER.info("user {} connected", connectionId);
        user.sendResponse("user connected");
        //handleCommand(user, data);
    }

    private void handleCommand(UserSession session, String message) {
        String[] parts = message.split(" ", 2);
        String command = parts[0];
        String content = parts[1];

        switch (command) {
            case "/connect" -> connect(session, content);
            case "/create" -> createRoom(session, content);
            case "/join" -> joinRoom(session, content);
            case "/msg" -> sendMessageToRoom(session, content);
            case "/pm" -> sendPrivateMessage(session, content);
            default -> sendError(session, "Unknown command");
        }
    }

    private void connect(UserSession session, String data) {
        //TODO
    }

    private void createRoom(UserSession session, String data) {
        //TODO
    }

    private void joinRoom(UserSession session, String data) {
        //TODO
    }

    private void sendMessageToRoom(UserSession session, String data) {
        //TODO
    }

    private void sendPrivateMessage(UserSession session, String data) {
        //TODO
    }

    private void sendError(UserSession session, String data) {
        //TODO
    }

}