package io.rateboard.reservationapi.service;

import io.rateboard.reservationapi.dto.ReservationRequestDto;
import io.rateboard.reservationapi.dto.ReservationResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {
    private final MessagingQueueService messagingQueueService;
    private final MessageStoreService messageStoreService;

    public ReservationResponseDto sendReservation(ReservationRequestDto request) {
        String messageId = UUID.randomUUID().toString();
        Instant createdAt = Instant.now();
        log.debug("Create message to send to queue, messageId: %s, createdAt: %s".formatted(messageId, createdAt));
        try {
            messagingQueueService.sendToQueue(request);
            messageStoreService.saveMessage(messageId, createdAt, request.getReservationId());
        } catch (AmqpException e) {
            // TODO possible retry via resilence4j?
            log.error("Failed to send message to queue, messageId: %s".formatted(messageId), e);
            return ReservationResponseDto.builder().errorCode(100500).errorMessage("Failed to process the message").build();
        } catch (DataAccessException dataAccessException) {
            log.error("Failed to save message to store, messageId: %s".formatted(messageId), dataAccessException);
            return ReservationResponseDto.builder().errorCode(100400).errorMessage("Message sent to processor, but status may be unknown").build();
        }
        log.debug("Message successfully sent to queue, messageId: %s".formatted(messageId));
        return ReservationResponseDto.builder().messageId(messageId).build();
    }
}
