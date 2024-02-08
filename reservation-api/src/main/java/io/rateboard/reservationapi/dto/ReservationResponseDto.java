package io.rateboard.reservationapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Reservation response. messageId will be filled in case of success. errorCode will be used in case of error")
public record ReservationResponseDto(String messageId, Integer errorCode, String errorMessage) {
}
