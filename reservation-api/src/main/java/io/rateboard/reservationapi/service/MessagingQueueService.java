package io.rateboard.reservationapi.service;

import io.rateboard.reservationapi.dto.ReservationRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessagingQueueService {
    private final RabbitTemplate rabbitTemplate;

    private static final String RESERVATION_QUEUE = "q.reservation";


    public void sendToQueue(ReservationRequestDto request) throws AmqpException {
        rabbitTemplate.convertAndSend(RESERVATION_QUEUE, request);
    }

}
