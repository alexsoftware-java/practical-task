package io.rateboard.reservationapi.service;

import io.rateboard.reservationapi.dto.MessageStoreDto;
import io.rateboard.reservationapi.entity.MessageStoreEntity;
import io.rateboard.reservationapi.repositry.MessageStoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
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

    /**
     * Get message by id
     * @param messageId uuid
     * @return Optional.empty() if not found, MessageStoreDto if found
     */

    public Optional<MessageStoreDto> getMessage(String messageId) {
        log.debug("Getting message with id %s from store".formatted(messageId));
        var message = messageStoreRepository.findById(messageId);
        return message.map(messageStoreEntity -> MessageStoreDto.builder()
                .messageId(messageStoreEntity.getMessageId())
                .processedAt(messageStoreEntity.getProcessedAt())
                .createdAt(messageStoreEntity.getCreatedAt())
                .build());
    }

    /**
     * Fetch messages by reservation id
     * @param reservationId reservation id
     * @return Optional.empty() if not found or List of MessageStoreDto
     */
    public Optional<List<MessageStoreDto>> getMessagesByReservationId(String reservationId) {
        log.debug("Getting messages with reservation id %s from store".formatted(reservationId));
        var messages = messageStoreRepository.findAllByReservationId(reservationId);
        log.debug("Found %s messages with reservation id %s".formatted(messages.map(List::size).orElse(0), reservationId));
        return messages.map(messageStoreEntities -> messageStoreEntities.stream().map(messageStoreEntity -> MessageStoreDto.builder()
                .messageId(messageStoreEntity.getMessageId())
                .processedAt(messageStoreEntity.getProcessedAt())
                .createdAt(messageStoreEntity.getCreatedAt())
                .build()).toList());
    }
}
