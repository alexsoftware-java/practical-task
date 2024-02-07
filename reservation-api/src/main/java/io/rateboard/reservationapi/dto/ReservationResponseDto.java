package io.rateboard.reservationapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Response object. errorCode will be used in case of error")
public record ReservationResponseDto(Integer errorCode, String errorMessage) {
}
