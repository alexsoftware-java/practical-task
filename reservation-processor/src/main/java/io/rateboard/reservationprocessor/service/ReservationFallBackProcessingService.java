package io.rateboard.reservationprocessor.service;

import io.rateboard.reservationprocessor.dto.ReservationRequestDto;
import io.rateboard.reservationprocessor.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationFallBackProcessingService {

    /**
     * Process message from failured deliveries queue
     *
     * @param message payload from queue
     * @throws DataAccessException if failed to save to redis
     */
    @RabbitListener(queues = Constants.FALL_BACK_RESERVATION_QUEUE)
    public void processMessage(ReservationRequestDto message) {
        log.warn(message.toString());
    }


}
