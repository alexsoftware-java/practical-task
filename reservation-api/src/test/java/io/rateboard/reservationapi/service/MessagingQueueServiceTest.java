package io.rateboard.reservationapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.rateboard.reservationapi.dto.ReservationQueueRequestDto;
import io.rateboard.reservationapi.dto.ReservationUserRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.Instant;

import static io.rateboard.reservationapi.service.MessagingQueueRabbitTest.getReservationUserRequestDto;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MessagingQueueServiceTest {

    @InjectMocks
    private MessagingQueueService messagingQueueService;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Test
    void sendToQueue() throws JsonProcessingException {
        // given
        var reservationUserRequestDto = getReservationUserRequestDto();
        // when
        assertDoesNotThrow(() -> messagingQueueService.sendToQueue("123", reservationUserRequestDto));
        // then
        ArgumentCaptor<ReservationQueueRequestDto> captor = ArgumentCaptor.forClass(ReservationQueueRequestDto.class);
        verify(rabbitTemplate).convertAndSend(ArgumentMatchers.anyString(), captor.capture());
        assertEquals("123", captor.getValue().getMessageId());
        assertEquals("1234", captor.getValue().getReservationId());
        assertEquals(Instant.ofEpochMilli(1707408491), captor.getValue().getUpdatedAt());
    }
}