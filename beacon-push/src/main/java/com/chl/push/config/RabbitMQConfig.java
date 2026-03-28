package com.chl.push.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {
    public static final String PUSH_DELAYED_EXCHANGE = "push_delayed_exchange";

    public static final String PUSH_DELAYED_QUEUE = "push_delayed_queue";

    private static final String PUSH_DELAYED_TYPE = "x-delayed-message";

    private static final String DELAYED_ROUTING_TYPE_KEY = "x-delayed-type";

    private static final String DELAYED_ROUTING_TYPE_FANOUT = "fanout";

    @Bean
    public Exchange delayedExchange(){
        Map<String, Object> args = new HashMap<>();
        args.put(DELAYED_ROUTING_TYPE_KEY,DELAYED_ROUTING_TYPE_FANOUT);
        return new CustomExchange(PUSH_DELAYED_EXCHANGE,PUSH_DELAYED_TYPE,false,false,args);
    }

    @Bean
    public Queue delayedQueue(){
        return QueueBuilder.durable(PUSH_DELAYED_QUEUE).build();
    }

    @Bean
    public Binding binding(Exchange delayedExchange,Queue delayedQueue){
        return BindingBuilder.bind(delayedQueue).to(delayedExchange).with("").noargs();
    }
}
