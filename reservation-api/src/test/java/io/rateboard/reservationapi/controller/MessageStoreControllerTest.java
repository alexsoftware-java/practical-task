package io.rateboard.reservationapi.controller;

import io.rateboard.reservationapi.controller.contract.MessageStoreController;
import io.rateboard.reservationapi.dto.MessageStoreDto;
import io.rateboard.reservationapi.service.MessageStoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(value = MessageStoreController.class, properties = {"api.key=somekey"})
@AutoConfigureMockMvc
class MessageStoreControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MessageStoreService messageStoreService;

    @Test
    void TestMessageStoreGetMessageOk() throws Exception {
        //given
        when(messageStoreService.getMessage(anyString())).thenReturn(Optional.of(MessageStoreDto.builder()
                .messageId("123")
                .receivedAt(Instant.ofEpochMilli(1707408491))
                .processedAt(Instant.ofEpochMilli(1707408492))
                .build()));
        // when
        mockMvc.perform(get("/api/v1/reservation/message/123")
                        .header("Authorization", "somekey")
                        .contentType("application/json")
                )
                // then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"messageId\":\"123\",\"receivedAt\":\"1970-01-20T18:16:48.491Z\",\"processedAt\":\"1970-01-20T18:16:48.492Z\"}"))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
        verify(messageStoreService).getMessage(anyString());
    }

    @Test
    void TestMessageStoreGetMessageNotFound() throws Exception {
        //given
        when(messageStoreService.getMessage(anyString())).thenReturn(Optional.empty());
        // when
        mockMvc.perform(get("/api/v1/reservation/message/123")
                        .header("Authorization", "somekey")
                        .contentType("application/json")
                )
                // then
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        verify(messageStoreService).getMessage(anyString());
    }

    @Test
    void TestMessageStoreGetMessagesByReservationOk() throws Exception {
        //given
        when(messageStoreService.getMessagesByReservationId(anyString())).thenReturn(Optional.of(List.of(
                MessageStoreDto.builder()
                        .messageId("123")
                        .receivedAt(Instant.ofEpochMilli(1707408491))
                        .processedAt(Instant.ofEpochMilli(1707408492))
                        .build(),
                MessageStoreDto.builder()
                        .messageId("124")
                        .receivedAt(Instant.ofEpochMilli(1807408491))
                        .processedAt(Instant.ofEpochMilli(1807408492))
                        .build()
        )));
        // when
        mockMvc.perform(get("/api/v1/reservation/123")
                        .header("Authorization", "somekey")
                        .contentType("application/json")
                )
                // then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[{\"messageId\":\"123\",\"receivedAt\":\"1970-01-20T18:16:48.491Z\",\"processedAt\":\"1970-01-20T18:16:48.492Z\"},{\"messageId\":\"124\",\"receivedAt\":\"1970-01-21T22:03:28.491Z\",\"processedAt\":\"1970-01-21T22:03:28.492Z\"}]"))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
        verify(messageStoreService).getMessagesByReservationId(anyString());
    }

}