package com.git.ilselva.chat.server.service;

import com.git.ilselva.chat.server.model.UserSession;
import com.git.ilselva.chat.server.service.api.UserSessionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.integration.ip.tcp.connection.TcpConnection;
import org.springframework.integration.ip.tcp.connection.TcpConnectionCloseEvent;
import org.springframework.integration.ip.tcp.connection.TcpConnectionOpenEvent;
import org.springframework.stereotype.Service;

@Service
public class ConnectionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionListener.class);
    @Autowired
    private UserSessionRegistry sessionRegistry;

    @EventListener
    public void onTcpConnectionOpen(TcpConnectionOpenEvent event) {
        TcpConnection connection = (TcpConnection) event.getSource();
        String connectionId = connection.getConnectionId();

        UserSession session = new UserSession(connection);
        sessionRegistry.registerSession(connectionId, session);
        LOGGER.debug("user session {} connected", connectionId);

        session.sendResponse("Welcome!\nTo use the app login with the command /connect <username> \n");
    }

    @EventListener
    public void onTcpConnectionClose(TcpConnectionCloseEvent event) {
        sessionRegistry.removeSession(event.getConnectionId());
        LOGGER.debug("user session {} disconnected", event.getConnectionId());
    }

}