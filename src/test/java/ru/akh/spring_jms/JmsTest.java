package ru.akh.spring_jms;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import ru.akh.spring_jms.receiver.MyReceiver;
import ru.akh.spring_jms.sender.MySender;

@SpringBootTest
public class JmsTest extends AbstractTest {

    @SpyBean
    private MyReceiver receiver;

    @Autowired
    private MySender sender;

    @Test
    public void test() {
        String text = "test message";

        sender.sendMessage(text);

        Mockito.verify(receiver).processMessage(text);
    }

}
