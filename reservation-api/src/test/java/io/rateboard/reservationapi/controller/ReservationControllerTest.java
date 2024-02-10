package io.rateboard.reservationapi.controller;

import io.rateboard.reservationapi.dto.ReservationResponseDto;
import io.rateboard.reservationapi.exception.WrongApiKeyException;
import io.rateboard.reservationapi.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(value = ReservationControllerImpl.class, properties = {"api.key=somekey"})
@AutoConfigureMockMvc
class ReservationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ReservationService reservationService;

    @Test
    void TestControllerApiOk() throws Exception {
        //given
        when(reservationService.sendReservation(any())).thenReturn(ReservationResponseDto.builder()
                .messageId("123")
                .build());
        // when
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
                // then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"errorCode\": null, \"errorMessage\": null}"))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
        verify(reservationService).sendReservation(any());
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

}