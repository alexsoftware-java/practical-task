package io.rateboard.reservationapi.integration;

import io.rateboard.reservationapi.dto.ReservationQueueRequestDto;
import io.rateboard.reservationapi.utils.RabbitTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.io.IOException;

import static io.rateboard.reservationapi.utils.Constants.*;
import static io.rateboard.reservationapi.utils.DataGenerator.getReservationQueueRequestDto;
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
    public void rabbitMQAutoConfigurationTest() throws InterruptedException, IOException {
        // when (first connection to rabbit happens)
        rabbitTemplate.convertAndSend("RK", new ReservationQueueRequestDto());
        // then
        assertThat(container.execInContainer("rabbitmqctl", "list_exchanges").getStdout())
                .containsPattern(RESERVATION_EXCHANGE+"\\s+direct")
                .containsPattern(FALL_BACK_RESERVATION_EXCHANGE+"\\s+direct");
        assertThat(container.execInContainer("rabbitmqctl", "list_queues", "name").getStdout())
                .containsPattern(RESERVATION_QUEUE)
                .containsPattern(FALL_BACK_RESERVATION_QUEUE);
    }

    @Test
    public void messageSendTest(CapturedOutput output) throws IOException, InterruptedException {
        // given
        var reservationQueueRequestDto = getReservationQueueRequestDto();
        // when
        assertDoesNotThrow(() -> rabbitTemplate.convertAndSend(
                RESERVATION_EXCHANGE,
                RESERVATION_ROUTING_KEY,
                reservationQueueRequestDto
        ));
        // then
        assertThat(container.execInContainer("rabbitmqctl", "list_queues", "messages").getStdout())
                .containsPattern("1");
        assertThat(output.getErr()).isEmpty();
    }

}
