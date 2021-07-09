package ru.akh.spring_jms;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsTemplate;

import ru.akh.spring_jms.sender.BookServiceClient;

@TestConfiguration
public class ClientConfig {

    @Bean
    public BookServiceClient bookServiceClient(JmsTemplate template) {
        return new BookServiceClient(template);
    }

}
