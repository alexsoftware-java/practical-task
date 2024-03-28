package io.somecompany.reservationapi.service;

import io.somecompany.reservationapi.entity.MessageStoreEntity;
import io.somecompany.reservationapi.repositry.MessageStoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageStoreServiceTest {
    @InjectMocks
    private MessageStoreService messageStoreService;

    @Mock
    private MessageStoreRepository messageStoreRepository;


    @Test
    void getMessage() {
        // given
        when(messageStoreRepository.findById(anyString())).thenReturn(Optional.of(MessageStoreEntity.builder().messageId("123").receivedAt(Instant.ofEpochMilli(1707408491)).reservationId("1234").build()));
        // when
        var result = assertDoesNotThrow(() -> messageStoreService.getMessage("123"));
        // then
        assertFalse(result.isEmpty());
        assertEquals("123", result.get().getMessageId());
        assertEquals(Instant.ofEpochMilli(1707408491), result.get().getReceivedAt());
    }

    @Test
    void getMessagesByReservationId() {
        // given
        when(messageStoreRepository.findAllByReservationId(anyString())).thenReturn(List.of(
                MessageStoreEntity.builder().messageId("123").receivedAt(Instant.ofEpochMilli(1707408491)).reservationId("123").build(),
                MessageStoreEntity.builder().messageId("1234").receivedAt(Instant.ofEpochMilli(1807408491)).reservationId("123").build()));
        // when
        var result = assertDoesNotThrow(() -> messageStoreService.getMessagesByReservationId("123"));
        // then
        assertFalse(result.isEmpty());
        assertEquals(2, result.get().size());
        assertEquals("1234", result.get().get(1).getMessageId());
        assertEquals(Instant.ofEpochMilli(1707408491), result.get().get(0).getReceivedAt());
    }
}