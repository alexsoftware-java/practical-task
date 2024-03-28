package io.somecompany.reservationapi.controller;

import io.somecompany.reservationapi.controller.contract.ReservationController;
import io.somecompany.reservationapi.dto.ReservationUserRequestDto;
import io.somecompany.reservationapi.dto.ReservationResponseDto;
import io.somecompany.reservationapi.exception.WrongApiKeyException;
import io.somecompany.reservationapi.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReservationControllerImpl implements ReservationController {

    @Value("${api.key}")
    private String serviceApiKey;

    private final ReservationService reservationService;

    @Override
    public ReservationResponseDto sendReservation(String apiKey, ReservationUserRequestDto request) {
        if (!apiKey.equals(serviceApiKey)) {
            throw new WrongApiKeyException();
        }

        return reservationService.sendReservation(request);
    }
}
