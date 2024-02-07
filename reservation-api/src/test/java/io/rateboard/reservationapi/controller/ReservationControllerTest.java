package io.rateboard.reservationapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.rateboard.reservationapi.dto.ReservationRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import io.rateboard.reservationapi.exception.WrongApiKeyException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(properties = {"api.key=somekey"})
@AutoConfigureMockMvc
class ReservationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ReservationControllerImpl controller;

    @Test
    void TestControllerApiOk() throws Exception {
        mockMvc.perform(post("/api/v1/reservation")
                        .header("Authorization", "somekey")
                        .contentType("application/json")
                        .content("""
                                {
                                    "reservationId": "123",
                                    "payload": {
                                        "reservationDate": "2024-04-01T10:00:00Z",
                                        "numberOfAdults": 2,
                                        "numberOfChildren": 0,
                                        "roomType": "double"
                                    },
                                    "updatedAt": "2024-02-01T10:00:00Z"
                                }"""))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"errorCode\": null, \"errorMessage\": null}"))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    @Test
    void TestControllerApiBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/reservation")
                        .header("Authorization", "somekey")
                        .contentType("application/json")
                        .content("""
                                {
                                    "reservationId": "12",
                                    "payload": null,
                                    "updatedAt": "2024-02-01T10:00:00Z"
                                }"""))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    void TestWrongApiKeyException() throws Exception {
        mockMvc.perform(post("/api/v1/reservation")
                .header("Authorization", "bad-api-key")
                .contentType("application/json")
                .content("{}"))
                .andExpect(result -> assertInstanceOf(WrongApiKeyException.class, result.getResolvedException()));
    }

    @Test
    void TestControllerMethod() throws JsonProcessingException {
        // given
        var request = new ReservationRequestDto();
        request.setReservationId("1234");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{\"reservationDate\": \"2024-04-01T10:00:00Z\", \"numberOfAdults\": 2, \"numberOfChildren\": 0, \"roomType\": \"double\"}");
        request.setPayload(jsonNode);
        request.setUpdatedAt(Instant.ofEpochMilli(1707326577));
        // when
        var result = controller.makeReservation("somekey", request);
        // then
        assertNotNull(result);
        assertNull(result.errorCode());
        assertNull(result.errorMessage());
    }

}