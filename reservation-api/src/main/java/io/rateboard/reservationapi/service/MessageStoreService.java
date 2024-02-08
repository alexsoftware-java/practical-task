package io.rateboard.reservationapi.service;

import io.rateboard.reservationapi.entity.MessageStoreEntity;
import io.rateboard.reservationapi.repositry.MessageStoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageStoreService {
    private final MessageStoreRepository messageStoreRepository;

    /**
     * Save message information to redis to store processing result
     *
     * @param messageId     unique message id
     * @param createdAt     message internal creation time
     * @param reservationId reservation id
     * @throws DataAccessException if failed to save to redis
     */

    public void saveMessage(String messageId, Instant createdAt, String reservationId) throws DataAccessException {
        var message = MessageStoreEntity.builder()
                .reservationId(reservationId)
                .createdAt(createdAt)
                .messageId(messageId)
                .build();
        messageStoreRepository.save(message);
        log.debug("Message %s saved to store".formatted(messageId));
    }

    public Optional<MessageStoreEntity> getMessage(String messageId) {
        log.debug("Getting message with id %s from store".formatted(messageId));
        return messageStoreRepository.findById(messageId);
    }

    public Optional<MessageStoreEntity> getReservation(String reservationId) {
        return messageStoreRepository.findByReservationId(reservationId);
    }
}
