package com.git.ilselva.chat.server.service;

import com.git.ilselva.chat.server.model.UserSession;
import com.git.ilselva.chat.server.service.api.UserSessionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.integration.ip.tcp.connection.TcpConnection;
import org.springframework.integration.ip.tcp.connection.TcpConnectionCloseEvent;
import org.springframework.integration.ip.tcp.connection.TcpConnectionOpenEvent;
import org.springframework.stereotype.Service;

@Service
public class ConnectionListener {

    @Autowired
    private UserSessionRegistry sessionRegistry;

    @EventListener
    public void onTcpConnectionOpen(TcpConnectionOpenEvent event) {
        TcpConnection connection = (TcpConnection) event.getSource();
        String connectionId = connection.getConnectionId();

        UserSession session = new UserSession(connection);
        sessionRegistry.registerSession(connectionId, session);
    }

    @EventListener
    public void onTcpConnectionClose(TcpConnectionCloseEvent event) {
        sessionRegistry.removeSession(event.getConnectionId());
    }

}