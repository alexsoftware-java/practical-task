package io.rateboard.reservationapi.service;

import io.rateboard.reservationapi.dto.ReservationRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendToQueueAsyncService {
    private final RabbitTemplate rabbitTemplate;
    @Async
    public void sendToQueue(ReservationRequestDto request) {
        rabbitTemplate.convertAndSend("reservation-queue", request);
    }

}
