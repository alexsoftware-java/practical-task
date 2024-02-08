package io.rateboard.reservationapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.rateboard.reservationapi.dto.ReservationUserRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpIOException;
import org.springframework.data.redis.connection.RedisPipelineException;

import java.io.IOException;
import java.time.Instant;

import static io.rateboard.reservationapi.service.MessagingQueueRabbitTest.getReservationUserRequestDto;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {
    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private MessagingQueueService messagingQueueService;

    @Mock
    private MessageStoreService messageStoreService;

    @Test
    void sendReservation() throws JsonProcessingException {
        // given
        doNothing().when(messagingQueueService).sendToQueue(any(), any());
        doNothing().when(messageStoreService).saveMessage(anyString(), any(Instant.class), anyString());
        var request = getReservationUserRequestDto();
        // when
        var result = assertDoesNotThrow(() -> reservationService.sendReservation(request));
        // then
        Assertions.assertNotNull(result.messageId());
        verify(messageStoreService).saveMessage(anyString(), any(Instant.class), anyString());
        verify(messagingQueueService).sendToQueue(any(), any());
    }

    @Test
    void sendReservationAmqpException() {
        // given
        doThrow(new AmqpIOException(new IOException())).when(messagingQueueService).sendToQueue(any(), any());
        var request = new ReservationUserRequestDto();
        request.setReservationId("123");
        request.setPayload(null);
        request.setUpdatedAt(Instant.ofEpochMilli(1707408491));
        // when
        var result = assertDoesNotThrow(() -> reservationService.sendReservation(request));
        // then
        Assertions.assertNull(result.messageId());
        assertEquals("Failed to process the message", result.errorMessage());
        assertEquals(100500, result.errorCode());
        verify(messageStoreService, times(0)).saveMessage(anyString(), any(Instant.class), anyString());
    }

    @Test
    void sendReservationDataAccessException() {
        // given
        doThrow(new RedisPipelineException(new Exception())).when(messageStoreService).saveMessage(anyString(), any(Instant.class), anyString());
        var request = new ReservationUserRequestDto();
        request.setReservationId("123");
        request.setPayload(null);
        request.setUpdatedAt(Instant.ofEpochMilli(1707408491));
        // when
        var result = assertDoesNotThrow(() -> reservationService.sendReservation(request));
        // then
        Assertions.assertNull(result.messageId());
        assertEquals("Message sent to processor, but status may be unknown", result.errorMessage());
        assertEquals(100400, result.errorCode());
    }
}