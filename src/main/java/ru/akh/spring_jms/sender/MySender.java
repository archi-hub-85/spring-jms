package ru.akh.spring_jms.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import ru.akh.spring_jms.constants.ApplicationConstants;

@Service
public class MySender {

    private static final Logger logger = LoggerFactory.getLogger(MySender.class);

    private final JmsTemplate jmsTemplate;

    public MySender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendMessage(String text) {
        jmsTemplate.convertAndSend(ApplicationConstants.QUEUE_NAME, text);
        logger.info("Sent message: {}", text);
    }

}
