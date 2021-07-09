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
    private final String requestQueue;

    @NotEmpty
    private final String responseQueue;

    public ApplicationProperties(String requestQueue, String responseQueue) {
        this.requestQueue = requestQueue;
        this.responseQueue = responseQueue;
    }

    public String getRequestQueue() {
        return requestQueue;
    }

    public String getResponseQueue() {
        return responseQueue;
    }

}
