package com.dongdong.nameSolver.global.rabbitMq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class RabbitMqConfig {
    private final RabbitMqProperties rabbitMqProperties;

    @Value("${rabbitmq.queue.name}")
    private String queueName;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    @Bean
    public Queue queue() {
        return new Queue(queueName);
    }

    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(exchangeName, "x-delayed-message", true, false, args);
    }

    @Bean
    public Binding binding(Queue queue, CustomExchange delayedExchange) {
        return BindingBuilder.bind(queue).to(delayedExchange).with(routingKey).noargs();
    }

    @Bean
    public CachingConnectionFactory cachingConnectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setHost(rabbitMqProperties.getHost());
        cachingConnectionFactory.setPort(rabbitMqProperties.getPort());
        cachingConnectionFactory.setUsername(rabbitMqProperties.getUsername());
        cachingConnectionFactory.setPassword(rabbitMqProperties.getPassword());
        return cachingConnectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory cachingConnectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
