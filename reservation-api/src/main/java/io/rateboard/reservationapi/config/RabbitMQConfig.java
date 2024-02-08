package io.rateboard.reservationapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.rateboard.reservationapi.utils.Constants.*;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {
    private final CachingConnectionFactory cachingConnectionFactory;


    /**
     * Create a queue for reservation and dead letter queue in case of ttl exceeded
     * <a href="https://www.rabbitmq.com/dlx.html">Rabbit MQ dead-letter doc</a>
     *
     * @return "q.reservation" queue in rabbit
     */
    @Bean
    public Queue createReservationQueue() {
        return QueueBuilder.durable(RESERVATION_QUEUE)
                .maxLength(RESERVATION_QUEUE_MAX_LENGTH) // after 1MLN messages in q, oldest one will be rejected to q.fall-back-reservation
                .deadLetterExchange(FALL_BACK_RESERVATION_EXCHANGE)
                .deadLetterRoutingKey(FALL_BACK_ROUTING_KEY)
                .build();
    }

    /**
     * Create a queue for fallback
     *
     * @return "q.fall-back-reservation" queue in rabbit
     */
    @Bean
    public Queue createFallBackQueue() {
        return QueueBuilder.durable(FALL_BACK_RESERVATION_QUEUE)
                .build();
    }

    /**
     * Set up a dead letter exchange
     *
     * @return new exchange to fall-back queue
     */
    @Bean
    public Declarables createDeadLetterSchema() {
        return new Declarables(
                new DirectExchange(FALL_BACK_RESERVATION_EXCHANGE),
                new Queue(FALL_BACK_RESERVATION_QUEUE),
                new Binding(FALL_BACK_RESERVATION_QUEUE, Binding.DestinationType.QUEUE, FALL_BACK_RESERVATION_EXCHANGE, FALL_BACK_ROUTING_KEY, null)
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
