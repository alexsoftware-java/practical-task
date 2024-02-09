package io.rateboard.reservationprocessor.service;

import io.rateboard.reservationprocessor.dto.ReservationQueueRequestDto;
import io.rateboard.reservationprocessor.entity.MessageStoreEntity;
import io.rateboard.reservationprocessor.repositry.MessageStoreRepository;
import io.rateboard.reservationprocessor.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationProcessingService {
    private final MessageStoreRepository repository;

    /**
     * Process message from queue
     *
     * @param message from queue
     * @throws IllegalArgumentException if messageId is null
     */
    @RabbitListener(queues = Constants.RESERVATION_QUEUE)
    public void processMessage(ReservationQueueRequestDto message) {
        log.info("Got message from queue %s".formatted(message));
        if (message.getMessageId() == null) {
            throw new IllegalArgumentException("Message is invalid (empty messageId)");
        }
        var messageStoreEntityOpt = repository.findById(message.getMessageId());
        if (messageStoreEntityOpt.isEmpty()) {
            log.info("Can't find message %s in MessageStore (yet)! Will create by my own".formatted(message.getMessageId()));
            messageStoreEntityOpt = Optional.of(MessageStoreEntity.builder()
                    .createdAt(Instant.now())
                    .messageId(message.getMessageId())
                    .reservationId(message.getReservationId())
                    .build());
        }
        messageStoreEntityOpt.get().setProcessedAt(Instant.now());
        repository.save(messageStoreEntityOpt.get());
        log.info("Processed messageId %s".formatted(message.getMessageId()));
    }
}
