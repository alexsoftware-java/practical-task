package io.rateboard.reservationprocessor.service;

import io.rateboard.reservationprocessor.dto.ReservationQueueRequestDto;
import io.rateboard.reservationprocessor.repositry.MessageStoreRepository;
import io.rateboard.reservationprocessor.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationProcessingService {
    private final MessageStoreRepository repository;

    /**
     * Process message from queue
     *
     * @param message from queue
     * @throws DataAccessException if failed to save to redis
     */
    @RabbitListener(queues = Constants.RESERVATION_QUEUE)
    public void processMessage(ReservationQueueRequestDto message) {
        log.info("Got message from queue %s".formatted(message));
        if (message.getMessageId() == null) {
            log.error("Got Invalid message");
            return;
        }
        var messageStoreEntity = repository.findById(message.getMessageId());
        if (messageStoreEntity.isEmpty()) {
            log.error("Can't find message %s in MessageStore!".formatted(message.getMessageId()));
            return;
        }
        messageStoreEntity.get().setProcessedAt(Instant.now());
        repository.save(messageStoreEntity.get());
        log.info("Processed messageId %s".formatted(message.getMessageId()));
    }

}
