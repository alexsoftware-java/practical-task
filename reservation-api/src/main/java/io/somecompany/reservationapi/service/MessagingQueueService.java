package io.somecompany.reservationapi.service;

import io.somecompany.reservationapi.dto.ReservationQueueRequestDto;
import io.somecompany.reservationapi.dto.ReservationUserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static io.somecompany.reservationapi.utils.Constants.*;

@Service
@RequiredArgsConstructor
public class MessagingQueueService {
    private final RabbitTemplate rabbitTemplate;

    /**
     * Send reservation to RabbitMQ queue
     *
     * @param messageId uuid
     * @param request   ReservationUserRequestDto represents reservation information
     * @throws AmqpException if failed to send
     */
    public void sendToQueue(String messageId, ReservationUserRequestDto request) throws AmqpException {
        var reservationQueueRequestDto = new ReservationQueueRequestDto();
        reservationQueueRequestDto.setMessageId(messageId);
        reservationQueueRequestDto.setReservationId(request.getReservationId());
        reservationQueueRequestDto.setPayload(request.getPayload());
        reservationQueueRequestDto.setUpdatedAt(request.getUpdatedAt());
        reservationQueueRequestDto.setReceivedAt(Instant.now());
        rabbitTemplate.convertAndSend(RESERVATION_EXCHANGE, RESERVATION_ROUTING_KEY, reservationQueueRequestDto);
    }

}
