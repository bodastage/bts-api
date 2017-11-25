package com.bodastage.cm.websockets;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import com.bodastage.cm.common.CustomDTQueries;

@Configuration 
@EnableWebSocketMessageBroker
//@Controller
//@RequestMapping("/api/websocket")
public class WebSocketConfiguration  implements WebSocketMessageBrokerConfigurer  {
	
	private static final Logger logger = LoggerFactory.getLogger(WebSocketConfiguration.class);
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableStompBrokerRelay("/topic","/queue")
                .setRelayHost("0.0.0.0") //@TODO: Pick from configuration file
                .setRelayPort(61613) //@TODO: Pick from configuration file
                .setClientLogin("admin")
                .setClientPasscode("admin")
            	;
		        registry.setApplicationDestinationPrefixes("/app");

    }

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.taskExecutor().corePoolSize(Runtime.getRuntime().availableProcessors()*4);
		
		logger.debug("Start configureClientInboundChannel... ");
		
		registration.setInterceptors(new ChannelInterceptorAdapter() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
            	logger.debug("PRESEND: " + message.toString());
            	if (StompCommand.MESSAGE.equals(message.getHeaders().get("stompCommand"))) {
                    StompHeaderAccessor headers = StompHeaderAccessor.wrap(message);
                    headers.setLeaveMutable(true);
                    headers.removeHeader("stompCommand");
                    headers.updateStompCommandAsClientMessage();
                    
                    logger.debug("HEADERS: " + headers.toString());
                    logger.debug("MESSAGE: " + message.toString());
                    return MessageBuilder.createMessage(message.getPayload(), headers.getMessageHeaders());
                    
                }
                else {
                    return message;
                }
            }
		 });

		logger.debug("End configureClientInboundChannel... ");
	}

	@Override
	public void configureClientOutboundChannel(ChannelRegistration registration) {
		registration.taskExecutor().corePoolSize(Runtime.getRuntime().availableProcessors()*4);
	}

	@Override
	public boolean configureMessageConverters(List<MessageConverter> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void configureWebSocketTransport(WebSocketTransportRegistration arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/websocket")
			.setAllowedOrigins("*" )
			.withSockJS();
	}

}