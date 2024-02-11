package io.rateboard.reservationapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.rateboard.reservationapi.utils.Constants.*;
import static org.springframework.amqp.core.Binding.DestinationType.QUEUE;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {
    private final CachingConnectionFactory cachingConnectionFactory;

    @Bean
    public Declarables createMessagingSchema() {
        return new Declarables(
                // Create a queue with name "q.reservation"
                QueueBuilder.durable(RESERVATION_QUEUE)
                        .deadLetterExchange(FALL_BACK_RESERVATION_EXCHANGE)
                        .deadLetterRoutingKey(FALL_BACK_ROUTING_KEY)
                        .build(),
                // Create an exchange for reservation
                new DirectExchange(RESERVATION_EXCHANGE),
                // All messages sent with "reservation" routing key will be routed to "q.reservation"
                new Binding(RESERVATION_QUEUE, QUEUE, RESERVATION_EXCHANGE, RESERVATION_ROUTING_KEY, null),
                // Exchange for fall-back messages on subscriber processing exception
                new DirectExchange(FALL_BACK_RESERVATION_EXCHANGE),
                // Create a queue with name "q.fall-back-reservation"
                new Queue(FALL_BACK_RESERVATION_QUEUE),
                // All messages sent with "fall-back" routing key will be routed to "q.fall-back-reservation"
                new Binding(FALL_BACK_RESERVATION_QUEUE, Binding.DestinationType.QUEUE, FALL_BACK_RESERVATION_EXCHANGE, FALL_BACK_ROUTING_KEY, null)
        );
    }

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

    /**
     * Force start connection to rabbit to create new queues without waiting for any request
     * @param cf CachingConnectionFactory
     * @return ApplicationRunner
     */
    @Bean
    ApplicationRunner runner(ConnectionFactory cf) {
        return args -> cf.createConnection().close();
    }
}
