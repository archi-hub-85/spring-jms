package ru.akh.spring_jms;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jms.support.converter.MarshallingMessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.DynamicDestinationResolver;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import ru.akh.spring_jms.constants.ApplicationConstants;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Configuration
    // @EnableJms
    public static class JmsConfig {

        @Bean
        public DestinationResolver destinationResolver(ApplicationProperties appProperties) {
            return new MyDestinationResolver(appProperties);
        }

        @Bean
        public MessageConverter messageConverter(@Value("classpath:book.xsd") Resource schemaResource) {
            Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
            marshaller.setContextPath("ru.akh.spring_jms.schema");
            marshaller.setSchema(schemaResource);

            MarshallingMessageConverter messageConverter = new MarshallingMessageConverter(marshaller);
            messageConverter.setTargetType(MessageType.TEXT);
            return messageConverter;
        }

    }

    public static class MyDestinationResolver extends DynamicDestinationResolver {

        private final ApplicationProperties appProperties;

        public MyDestinationResolver(ApplicationProperties appProperties) {
            this.appProperties = appProperties;
        }

        @Override
        protected Queue resolveQueue(Session session, String queueName) throws JMSException {
            switch (queueName) {
            case ApplicationConstants.REQUEST_QUEUE:
                queueName = appProperties.getRequestQueue();
                break;
            case ApplicationConstants.RESPONSE_QUEUE:
                queueName = appProperties.getResponseQueue();
                break;
            default:
            }

            return super.resolveQueue(session, queueName);
        }

    }

}
