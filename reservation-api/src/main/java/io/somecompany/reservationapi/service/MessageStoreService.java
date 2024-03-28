package io.somecompany.reservationapi.service;

import io.somecompany.reservationapi.dto.MessageStoreDto;
import io.somecompany.reservationapi.repositry.MessageStoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageStoreService {
    private final MessageStoreRepository messageStoreRepository;

    /**
     * Get message by id
     *
     * @param messageId uuid
     * @return Optional.empty() if not found, MessageStoreDto if found
     */

    public Optional<MessageStoreDto> getMessage(String messageId) {
        log.debug("Getting message with id %s from store".formatted(messageId));
        var message = messageStoreRepository.findById(messageId);
        return message.map(messageStoreEntity -> MessageStoreDto.builder()
                .messageId(messageStoreEntity.getMessageId())
                .processedAt(messageStoreEntity.getProcessedAt())
                .receivedAt(messageStoreEntity.getReceivedAt())
                .build());
    }

    /**
     * Fetch messages by reservation id
     *
     * @param reservationId reservation id
     * @return Optional.empty() if not found or List of MessageStoreDto
     */
    public Optional<List<MessageStoreDto>> getMessagesByReservationId(String reservationId) {
        log.debug("Getting messages with reservation id %s from store".formatted(reservationId));
        var messages = messageStoreRepository.findAllByReservationId(reservationId);
        log.debug("Got %d messages with reservation id %s from store".formatted(messages.size(), reservationId));
        var result = new ArrayList<MessageStoreDto>();
        messages.forEach(message -> {
            if (message.getReservationId().equals(reservationId)) {
                result.add(MessageStoreDto.builder()
                        .messageId(message.getMessageId())
                        .processedAt(message.getProcessedAt())
                        .receivedAt(message.getReceivedAt())
                        .build());
            }
        });
        return result.isEmpty() ? Optional.empty() : Optional.of(result);
    }
}
