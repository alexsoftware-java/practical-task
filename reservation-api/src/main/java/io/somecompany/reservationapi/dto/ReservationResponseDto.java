package io.somecompany.reservationapi.dto;

import lombok.Builder;

@Builder
public record ReservationResponseDto(String messageId, Integer errorCode, String errorMessage) {}
