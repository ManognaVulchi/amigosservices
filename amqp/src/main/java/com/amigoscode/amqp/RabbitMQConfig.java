package com.amigoscode.amqp;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class RabbitMQConfig {
    private final ConnectionFactory connectionFactory;
    @Bean
    //ability to publish a message to the queue
    //allows us to send messages to queue
    public AmqpTemplate amqpTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        //when a producer sends a message this will be sent as a json and at the other end of the queue it will converted
        //to its respective object and sent to consumer
        //send message convereted is used to send messages in json
        rabbitTemplate.setMessageConverter(jacksonConverter());
        return rabbitTemplate;
    }
    //allows us to receive messages from queue using jackson convertor
    @Bean
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jacksonConverter());
        return factory;
    }

    @Bean
    public MessageConverter jacksonConverter(){
        MessageConverter jackson2JasonMessageConverter = new Jackson2JsonMessageConverter();
        return jackson2JasonMessageConverter;
    }
}
