package io.rateboard.reservationapi.service;

import io.rateboard.reservationapi.dto.ReservationResponseDto;
import io.rateboard.reservationapi.dto.ReservationUserRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {
    private final MessagingQueueService messagingQueueService;

    /**
     * Process reservation request - generate messageId, send to RabbitMQ
     * @param request ReservationUserRequestDto
     * @return messageId in case of success, non-null errorCode and errorMessage in case of Exception during send to queue
     */
    // TODO possible retry via resilience4j on AmqpException?
    public ReservationResponseDto sendReservation(ReservationUserRequestDto request) {
        String messageId = UUID.randomUUID().toString();
        log.debug("Create message to send to queue, messageId: %s, updatedAt in source system: %s".formatted(messageId, request.getUpdatedAt()));
        try {
            messagingQueueService.sendToQueue(messageId, request);
        } catch (AmqpException e) {
            log.error("Failed to send message to queue, messageId: %s".formatted(messageId), e);
            return ReservationResponseDto.builder().errorCode(100500).errorMessage("Failed to process the message").build();
        }
        log.debug("Message successfully sent to queue, messageId: %s".formatted(messageId));
        return ReservationResponseDto.builder().messageId(messageId).build();
    }
}
