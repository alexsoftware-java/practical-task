package io.rateboard.reservationprocessor.service;

import io.rateboard.reservationprocessor.dto.ReservationQueueRequestDto;
import io.rateboard.reservationprocessor.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationFallBackProcessingService {
    private final Jackson2JsonMessageConverter converter;
    /**
     * (much different) Process t process messages from failure deliveries queue
     *
     * @param originalMessage payload from queue
     * @throws DataAccessException if failed to save to redis
     */
    @RabbitListener(queues = Constants.FALL_BACK_RESERVATION_QUEUE)
    public void processMessage(Message originalMessage) {
        log.trace(originalMessage.getMessageProperties().toString());
        ReservationQueueRequestDto message = (ReservationQueueRequestDto) converter.fromMessage(originalMessage);
        if (message.getMessageId() == null) {
            log.error("Got Invalid message");
            return;
        }
        log.warn(message.toString());
    }

}
