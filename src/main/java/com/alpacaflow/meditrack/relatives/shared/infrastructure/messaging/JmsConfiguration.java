package com.alpacaflow.meditrack.relatives.shared.infrastructure.messaging;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.ConnectionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJms
@ConditionalOnProperty(name = "app.messaging.enabled", havingValue = "true")
public class JmsConfiguration {

    private static final String LEGACY_IAM_RELATIVE_REGISTRATION_TYPE =
            "com.alpacaflow.meditrack.iam.shared.infrastructure.messaging.RelativeRegistrationRequestedMessage";

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        var objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        var converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(objectMapper);
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");

        Map<String, Class<?>> typeIdMappings = new HashMap<>();
        typeIdMappings.put(MessagingTypeIds.RELATIVE_REGISTRATION_REQUEST, RelativeRegistrationRequestedMessage.class);
        typeIdMappings.put(LEGACY_IAM_RELATIVE_REGISTRATION_TYPE, RelativeRegistrationRequestedMessage.class);
        converter.setTypeIdMappings(typeIdMappings);
        return converter;
    }

    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter jacksonJmsMessageConverter) {
        var factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jacksonJmsMessageConverter);
        return factory;
    }
}
