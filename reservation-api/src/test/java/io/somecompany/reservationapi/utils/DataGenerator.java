package io.somecompany.reservationapi.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.somecompany.reservationapi.dto.ReservationQueueRequestDto;
import io.somecompany.reservationapi.dto.ReservationUserRequestDto;

import java.time.Instant;

public class DataGenerator {
    public static ReservationUserRequestDto getReservationUserRequestDto() throws JsonProcessingException {
        var reservationUserRequestDto = new ReservationUserRequestDto();
        reservationUserRequestDto.setReservationId("1234");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{\"reservationDate\": \"2024-04-01T10:00:00Z\", \"numberOfAdults\": 2, \"numberOfChildren\": 0, \"roomType\": \"double\"}");
        reservationUserRequestDto.setPayload(jsonNode);
        reservationUserRequestDto.setUpdatedAt(Instant.ofEpochMilli(1707408491));
        return reservationUserRequestDto;
    }

    public static ReservationQueueRequestDto getReservationQueueRequestDto() throws JsonProcessingException {
        var reservationQueueRequestDto = new ReservationQueueRequestDto();
        reservationQueueRequestDto.setMessageId("2b4e0585-c1eb-4025-ab6e-a9e84a6d84bc");
        reservationQueueRequestDto.setReservationId("1234");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{\"reservationDate\": \"2024-04-01T10:00:00Z\", \"numberOfAdults\": 2, \"numberOfChildren\": 0, \"roomType\": \"double\"}");
        reservationQueueRequestDto.setPayload(jsonNode);
        reservationQueueRequestDto.setUpdatedAt(Instant.ofEpochMilli(1707408491));
        return reservationQueueRequestDto;
    }
}
