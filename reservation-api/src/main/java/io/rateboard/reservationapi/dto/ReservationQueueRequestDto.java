package io.rateboard.reservationapi.dto;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Builder
public class ReservationQueueRequestDto {
    /**
     * messageId from reservation-api example = 51fa895a-4ede-4af4-bfa0-27e1c98a0cb5
     */
    String messageId;
    /**
     * reservationId in source system, example = d4a9eee4-829b-46f2-887d-ac6e963a3c1f
     */
    String reservationId;
    /**
     * payload, example = {"reservationDate": "2024-04-01T10:00:00Z", "numberOfAdults": 2, "numberOfChildren": 0, "roomType": "double"}
     */
    JsonNode payload;
    /**
     * updatedAt in source system, example = "2024-02-01T10:01:01.301Z"
     */
    Instant updatedAt;
}
