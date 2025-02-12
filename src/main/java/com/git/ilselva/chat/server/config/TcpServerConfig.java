package com.git.ilselva.chat.server.config;

import com.git.ilselva.chat.server.handler.ChatMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.ip.tcp.TcpInboundGateway;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNetServerConnectionFactory;
import org.springframework.integration.ip.tcp.serializer.ByteArrayCrLfSerializer;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
@IntegrationComponentScan
public class TcpServerConfig {

    @Value("${tcp.server.port:8080}")
    private int port;
    @Autowired
    private ChatMessageHandler chatMessageHandler;

    @Bean
    public AbstractServerConnectionFactory serverConnectionFactory() {

        TcpNetServerConnectionFactory factory = new TcpNetServerConnectionFactory(port);

        factory.setSerializer(new ByteArrayCrLfSerializer());
        factory.setDeserializer(new ByteArrayCrLfSerializer());
        return factory;
    }

    @Bean
    public MessageChannel messageChannel() {
        return new DirectChannel();
    }

    @Bean
    public TcpInboundGateway inboundGateway(AbstractServerConnectionFactory serverConnectionFactory) {
        TcpInboundGateway gateway = new TcpInboundGateway();
        gateway.setConnectionFactory(serverConnectionFactory);
        gateway.setRequestChannel(messageChannel());
        return gateway;
    }

    @Bean
    @ServiceActivator(inputChannel = "messageChannel")
    public MessageHandler messageHandler() {
        return chatMessageHandler;
    }
}