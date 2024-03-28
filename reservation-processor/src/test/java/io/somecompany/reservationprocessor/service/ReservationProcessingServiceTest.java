package io.somecompany.reservationprocessor.service;

import io.somecompany.reservationprocessor.dto.ReservationQueueRequestDto;
import io.somecompany.reservationprocessor.entity.MessageStoreEntity;
import io.somecompany.reservationprocessor.repositry.MessageStoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReservationProcessingServiceTest {
    @InjectMocks
    private ReservationProcessingService reservationProcessingService;

    @Mock
    private MessageStoreRepository messageStoreRepository;

    @Test
    void processMessage() {
        // given
        var request = new ReservationQueueRequestDto();
        ReflectionTestUtils.setField(request, "messageId", "123");
        // when
        reservationProcessingService.processMessage(request);
        // then
        ArgumentCaptor<MessageStoreEntity> captor = ArgumentCaptor.forClass(MessageStoreEntity.class);
        verify(messageStoreRepository).save(captor.capture());
        assertEquals("123", captor.getValue().getMessageId());
        assertNotNull(captor.getValue().getProcessedAt());
    }
}