package ru.akh.spring_jms;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Session;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

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

    }

    public static class MyDestinationResolver extends DynamicDestinationResolver {

        private final ApplicationProperties appProperties;

        public MyDestinationResolver(ApplicationProperties appProperties) {
            this.appProperties = appProperties;
        }

        @Override
        protected Queue resolveQueue(Session session, String queueName) throws JMSException {
            if (ApplicationConstants.QUEUE_NAME.equals(queueName)) {
                queueName = appProperties.getQueue();
            }

            return super.resolveQueue(session, queueName);
        }

    }

}
