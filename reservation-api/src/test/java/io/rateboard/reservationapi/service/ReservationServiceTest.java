package io.rateboard.reservationapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.rateboard.reservationapi.dto.ReservationUserRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpIOException;

import java.io.IOException;
import java.time.Instant;

import static io.rateboard.reservationapi.service.MessagingQueueRabbitTest.getReservationUserRequestDto;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {
    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private MessagingQueueService messagingQueueService;


    @Test
    void sendReservation() throws JsonProcessingException {
        // given
        doNothing().when(messagingQueueService).sendToQueue(any(), any());
        var request = getReservationUserRequestDto();
        // when
        var result = assertDoesNotThrow(() -> reservationService.sendReservation(request));
        // then
        Assertions.assertNotNull(result.messageId());
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
    }

}