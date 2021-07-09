package ru.akh.spring_jms;

import javax.validation.constraints.NotEmpty;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "ru.akh.spring-jms")
@ConstructorBinding
@Validated
public class ApplicationProperties {

    @NotEmpty
    private final String queue;

    public ApplicationProperties(String queue) {
        this.queue = queue;
    }

    public String getQueue() {
        return queue;
    }

}
