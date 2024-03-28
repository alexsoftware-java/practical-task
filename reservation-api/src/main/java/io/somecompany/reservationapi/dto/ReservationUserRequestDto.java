package io.somecompany.reservationapi.dto;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Instant;

@Data
@Schema(description = "Request for reservation live update")
public class ReservationUserRequestDto {

    @Schema(description = "reservationId in source system", example = "d4a9eee4-829b-46f2-887d-ac6e963a3c1f")
    @Size(min = 3, message = "reservationId should be 3 or more symbols long")
    String reservationId;

    @Schema(description = "payload", example = """
            {"reservationDate": "2024-04-01T10:00:00Z", "numberOfAdults": 2, "numberOfChildren": 0, "roomType": "double"}
            """)
    JsonNode payload;

    @Schema(description = "updatedAt in source system", example = "2024-02-01T10:01:01.301Z")
    Instant updatedAt;
}
