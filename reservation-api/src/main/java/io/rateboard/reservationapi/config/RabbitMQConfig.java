package io.rateboard.reservationapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {
    private final CachingConnectionFactory cachingConnectionFactory;


    /**
     * Create a queue for reservation and dead letter queue in case of ttl exceeded
     *
     * @return "q.reservation" queue in rabbit
     */
    @Bean
    public Queue createReservationQueue() {
        return QueueBuilder.durable("q.reservation")
                .deadLetterExchange("x.reservation-failure")
                .deadLetterRoutingKey("fall-back")
                .build();
    }

    /**
     * Create a queue for fallback
     *
     * @return "q.fall-back-reservation" queue in rabbit
     */
    @Bean
    public Queue createFallBackQueue() {
        return QueueBuilder.durable("q.fall-back-reservation")
                .build();
    }

    /**
     * Set up a dead letter exchange
     *
     * @return
     */
    @Bean
    public Declarables createDeadLetterSchema() {
        return new Declarables(
                new DirectExchange("x.reservation-failure"),
                new Queue("q.fall-back-reservation"),
                new Binding("q.fall-back-reservation", Binding.DestinationType.QUEUE, "x.reservation-failure", "fall-back", null)
        );
    }

    /**
     * To see messages in readable format
     *
     * @return Jackson2JsonMessageConverter
     */
    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(cachingConnectionFactory);
        template.setMessageConverter(converter);
        return template;
    }
}
