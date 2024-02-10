package io.rateboard.reservationapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.rateboard.reservationapi.RabbitTestContainer;
import io.rateboard.reservationapi.dto.ReservationUserRequestDto;
import io.rateboard.reservationapi.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
public class MessagingQueueRabbitTest implements RabbitTestContainer {

    @Autowired
    RabbitTemplate rabbitTemplate;
    @BeforeEach
    void check() {
        assertThat(RabbitTestContainer.container.isRunning()).isTrue();
    }


    @Test
    public void rabbitMQConfigurationCorrectnessTest(CapturedOutput output) throws JsonProcessingException {
        // given
        var reservationUserRequestDto = getReservationUserRequestDto();
        // when
        assertDoesNotThrow(() -> rabbitTemplate.convertAndSend(
                Constants.RESERVATION_QUEUE,
                reservationUserRequestDto
        ));

        assertThat(output.getErr()).isEmpty();
    }

    static ReservationUserRequestDto getReservationUserRequestDto() throws JsonProcessingException {
        var reservationUserRequestDto = new ReservationUserRequestDto();
        reservationUserRequestDto.setReservationId("1234");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{\"reservationDate\": \"2024-04-01T10:00:00Z\", \"numberOfAdults\": 2, \"numberOfChildren\": 0, \"roomType\": \"double\"}");
        reservationUserRequestDto.setPayload(jsonNode);
        reservationUserRequestDto.setUpdatedAt(Instant.ofEpochMilli(1707408491));
        return reservationUserRequestDto;
    }

}
