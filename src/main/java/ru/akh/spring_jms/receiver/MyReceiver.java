package ru.akh.spring_jms.receiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import ru.akh.spring_jms.constants.ApplicationConstants;

@Component
public class MyReceiver {

    private static final Logger logger = LoggerFactory.getLogger(MyReceiver.class);

    @JmsListener(destination = ApplicationConstants.QUEUE_NAME)
    public void processMessage(String text) {
        logger.info("Received message: {}", text);
    }

}
