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
        var reservationUserRequestDto = new ReservationUserRequestDto();
        reservationUserRequestDto.setReservationId("1234");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{\"reservationDate\": \"2024-04-01T10:00:00Z\", \"numberOfAdults\": 2, \"numberOfChildren\": 0, \"roomType\": \"double\"}");
        reservationUserRequestDto.setPayload(jsonNode);
        reservationUserRequestDto.setUpdatedAt(Instant.ofEpochMilli(1707408491));
        // when
        assertDoesNotThrow(() -> messagingQueueService.sendToQueue("123", reservationUserRequestDto));
        // then
        ArgumentCaptor<ReservationQueueRequestDto> captor = ArgumentCaptor.forClass(ReservationQueueRequestDto.class);
        verify(rabbitTemplate).convertAndSend(ArgumentMatchers.anyString(), captor.capture());
        assertEquals("123", captor.getValue().getMessageId());
        assertEquals("1234", captor.getValue().getReservationId());
        assertEquals(jsonNode, captor.getValue().getPayload());
        assertEquals(Instant.ofEpochMilli(1707408491), captor.getValue().getUpdatedAt());
    }
}