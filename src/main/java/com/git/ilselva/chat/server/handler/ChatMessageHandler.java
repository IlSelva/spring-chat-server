package com.git.ilselva.chat.server.handler;

import com.git.ilselva.chat.server.exception.CommandFormatException;
import com.git.ilselva.chat.server.model.User;
import com.git.ilselva.chat.server.model.UserSession;
import com.git.ilselva.chat.server.service.api.RoomRepository;
import com.git.ilselva.chat.server.service.api.UserSessionRegistry;
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
    private final UserSessionRegistry userSessionRegistry;
    private UserSessionRegistry sessionRegistry;
    private RoomRepository roomRepository;

    @Autowired
    public ChatMessageHandler(UserSessionRegistry sessionRegistry, RoomRepository roomRepository, UserSessionRegistry userSessionRegistry) {
        this.sessionRegistry = sessionRegistry;
        this.roomRepository = roomRepository;
        this.userSessionRegistry = userSessionRegistry;
    }

    @Override
    public void handleMessage(@NonNull Message<?> message) throws MessagingException {

        LOGGER.debug("received message: {}", message);

        String data = new String((byte[]) message.getPayload()).translateEscapes().trim();
        String connectionId = (String) message.getHeaders().get(CONNECTION_ID);
        LOGGER.debug("received message content: {}", data);

        UserSession session = sessionRegistry.getSessionById(connectionId);
        if (session.getUser() == null) {
            handleLogin(session, data);
        } else {
            handleCommand(session, data);
        }
    }

    private void handleLogin(UserSession session, String message) {

        try {
            String[] parts = message.split(" ", 2);
            String command = parts[0];
            String content = parts[1];

            if (command.equalsIgnoreCase("/connect")) {
                //TODO validate username
                session.setUser(new User(content.trim()));
                LOGGER.info("user {} connected", session.getUser().getUsername());
                session.sendResponse("user connected\r\n use /help to see all available commands");
            } else {
                throw new CommandFormatException("Wrong command format during Connection phase");
            }
        } catch (Exception e) {
            LOGGER.debug("Error during Login on connectionId: {}, exception: {}", session.getConnectionId(), e.getMessage());
            sendError(session, "Command not supported, please login first using the command /connect <username>");
        }
    }


    private void handleCommand(UserSession session, String message) {
        String[] parts = message.split(" ", 2);
        String command = parts[0];
        String content = parts.length > 1 ? parts[1] : null;

        switch (command) {
            case "/help" -> showCommands(session);
            case "/create" -> createRoom(session, content);
            case "/join" -> joinRoom(session, content);
            case "/list" -> showRooms(session);
            case "/users" -> showUsers(session);
            case "/msg" -> sendMessageToCurrentRoom(session, content);
            case "/pm" -> sendPrivateMessage(session, content);
            case "/leave" -> logout(session);
            default -> sendError(session, "Unknown command");
        }
    }

    private void showCommands(UserSession session) {
        session.sendResponse(
                "/create <room> to create a new room\r\n" +
                        "/join to join an existing room\r\n" +
                        "/list to list all existing rooms\r\n" +
                        "/users to show all users in the current room\r\n" +
                        "/msg <content> to send a message in the current room\r\n" +
                        "/pm <user> <content> to send a private message to <user>\r\n" +
                        "/help to see all available commands (you are here!)\r\n"
        );
    }

    private void createRoom(UserSession session, String data) {

        roomRepository.createRoom(data, session);
        session.sendResponse("room created");
    }

    private void joinRoom(UserSession session, String roomName) {
        //TODO
        roomRepository.getRoom(roomName).addMember(session);
        roomRepository.getRoom(roomName).broadcastMessage("User " + session.getUser().getUsername() + " joined room " + roomName);
    }

    private void showRooms(UserSession session) {
        //TODO
        session.sendResponse(roomRepository.getAllRooms().toString());
    }

    private void showUsers(UserSession session) {

        if (session.getCurrentRoom() != null) {
            session.sendResponse(roomRepository.getRoom(session.getCurrentRoom()).getMembers().toString());
        }
    }

    private void sendMessageToCurrentRoom(UserSession session, String data) {
        //TODO
        if (session.getCurrentRoom() != null) {
            roomRepository.getRoom(session.getCurrentRoom()).broadcastMessage(data);
        }
    }

    private void sendPrivateMessage(UserSession session, String data) {
        //TODO
    }

    private void sendError(UserSession session, String data) {
        //TODO
        session.sendClosingResponse(data);
    }

    private void logout(UserSession session) {
        //TODO
        session.sendClosingResponse("goodbye");
    }

}