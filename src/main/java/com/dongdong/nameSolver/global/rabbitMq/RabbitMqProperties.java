package com.dongdong.nameSolver.global.rabbitMq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@AllArgsConstructor
@ConfigurationProperties(prefix = "spring.rabbitmq")
@Getter
public class RabbitMqProperties {
    private String host;
    private int port;
    private String username;
    private String password;
}
