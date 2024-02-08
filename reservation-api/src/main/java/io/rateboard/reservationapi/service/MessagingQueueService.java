package io.rateboard.reservationapi.service;

import io.rateboard.reservationapi.dto.ReservationQueueRequestDto;
import io.rateboard.reservationapi.dto.ReservationUserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static io.rateboard.reservationapi.utils.Constants.RESERVATION_QUEUE;

@Service
@RequiredArgsConstructor
public class MessagingQueueService {
    private final RabbitTemplate rabbitTemplate;

    public void sendToQueue(String messageId, ReservationUserRequestDto request) throws AmqpException {
        rabbitTemplate.convertAndSend(RESERVATION_QUEUE, ReservationQueueRequestDto.builder()
                .messageId(messageId)
                .payload(request.getPayload())
                .reservationId(request.getReservationId())
                .updatedAt(request.getUpdatedAt()));
    }

}
