package io.rateboard.reservationprocessor.service;

import io.rateboard.reservationprocessor.dto.ReservationQueueRequestDto;
import io.rateboard.reservationprocessor.entity.MessageStoreEntity;
import io.rateboard.reservationprocessor.repositry.MessageStoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationProcessingServiceTest {
    @InjectMocks
    private ReservationProcessingService reservationProcessingService;

    @Mock
    private MessageStoreRepository messageStoreRepository;
    @Mock
    private Jackson2JsonMessageConverter converter;
    @Test
    void processMessage() {
        // given
        var request = new ReservationQueueRequestDto();
        ReflectionTestUtils.setField(request, "messageId", "123");
        when(converter.fromMessage(any(Message.class))).thenReturn(request);
        when(messageStoreRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(MessageStoreEntity.builder().messageId("123").build()));
        // when
        reservationProcessingService.processMessage(new Message(new byte[0]));
        // then
        ArgumentCaptor<MessageStoreEntity> captor = ArgumentCaptor.forClass(MessageStoreEntity.class);
        verify(messageStoreRepository).save(captor.capture());
        assertEquals("123", captor.getValue().getMessageId());
        assertNotNull(captor.getValue().getProcessedAt());
    }
}