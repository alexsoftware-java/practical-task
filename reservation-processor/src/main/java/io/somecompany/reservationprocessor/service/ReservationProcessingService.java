package io.somecompany.reservationprocessor.service;

import io.somecompany.reservationprocessor.dto.ReservationQueueRequestDto;
import io.somecompany.reservationprocessor.entity.MessageStoreEntity;
import io.somecompany.reservationprocessor.repositry.MessageStoreRepository;
import io.somecompany.reservationprocessor.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
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
     * @throws IllegalArgumentException if messageId is null
     */
    @RabbitListener(queues = Constants.RESERVATION_QUEUE)
    public void processMessage(ReservationQueueRequestDto message) {
        log.info("Got message from queue %s".formatted(message));
        if (message.getMessageId() == null) {
            throw new IllegalArgumentException("Message is invalid (empty messageId)");
        }
        var messageStore = MessageStoreEntity.builder()
                    .receivedAt(message.getReceivedAt())
                    .messageId(message.getMessageId())
                    .reservationId(message.getReservationId())
                    .processedAt(Instant.now())
                    .build();
        repository.save(messageStore);
        log.info("Processed messageId %s".formatted(message.getMessageId()));
    }
}
