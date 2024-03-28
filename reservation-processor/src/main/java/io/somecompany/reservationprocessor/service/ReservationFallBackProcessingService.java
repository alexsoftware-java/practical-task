package io.somecompany.reservationprocessor.service;

import io.somecompany.reservationprocessor.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationFallBackProcessingService {
    /**
     * (much different) Process t process messages from failure deliveries queue
     *
     * @param message payload from queue
     * @throws DataAccessException if failed to save to redis
     */
    @RabbitListener(queues = Constants.FALL_BACK_RESERVATION_QUEUE)
    public void processMessage(Message message) {
        if (message == null) {
            log.error("Got null fall-back message");
            return;
        }
        log.warn("Got fall-back message: " + message.getMessageProperties().toString());
    }

}
