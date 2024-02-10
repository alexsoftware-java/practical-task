package io.rateboard.reservationapi.service;

import io.rateboard.reservationapi.entity.MessageStoreEntity;
import io.rateboard.reservationapi.repositry.MessageStoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageStoreServiceTest {
    @InjectMocks
    private MessageStoreService messageStoreService;

    @Mock
    private MessageStoreRepository messageStoreRepository;

    @Test
    void saveMessage() {
        // given
        ArgumentCaptor<MessageStoreEntity> captor = ArgumentCaptor.forClass(MessageStoreEntity.class);
        when(messageStoreRepository.save(any())).thenReturn(null);
        // when
        assertDoesNotThrow(() -> messageStoreService.saveMessage("123", Instant.ofEpochMilli(1707408491), "1234"));
        // then
        verify(messageStoreRepository).save(captor.capture());
        assertEquals("123", captor.getValue().getMessageId());
        assertEquals(Instant.ofEpochMilli(1707408491), captor.getValue().getCreatedAt());
        assertEquals("1234", captor.getValue().getReservationId());

    }

    @Test
    void getMessage() {
        // given
        when(messageStoreRepository.findById(anyString())).thenReturn(Optional.of(MessageStoreEntity.builder().messageId("123").createdAt(Instant.ofEpochMilli(1707408491)).reservationId("1234").build()));
        // when
        var result = assertDoesNotThrow(() -> messageStoreService.getMessage("123"));
        // then
        assertFalse(result.isEmpty());
        assertEquals("123", result.get().getMessageId());
        assertEquals(Instant.ofEpochMilli(1707408491), result.get().getCreatedAt());
    }

    @Test
    void getMessagesByReservationId() {
        // given
        when(messageStoreRepository.findAllByReservationId(anyString())).thenReturn(List.of(
                MessageStoreEntity.builder().messageId("123").createdAt(Instant.ofEpochMilli(1707408491)).reservationId("123").build(),
                MessageStoreEntity.builder().messageId("1234").createdAt(Instant.ofEpochMilli(1807408491)).reservationId("123").build()));
        // when
        var result = assertDoesNotThrow(() -> messageStoreService.getMessagesByReservationId("123"));
        // then
        assertFalse(result.isEmpty());
        assertEquals(2, result.get().size());
        assertEquals("1234", result.get().get(1).getMessageId());
        assertEquals(Instant.ofEpochMilli(1707408491), result.get().get(0).getCreatedAt());
    }
}