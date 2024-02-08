package io.rateboard.reservationapi.controller;

import io.rateboard.reservationapi.controller.contract.ReservationController;
import io.rateboard.reservationapi.dto.ReservationRequestDto;
import io.rateboard.reservationapi.dto.ReservationResponseDto;
import io.rateboard.reservationapi.exception.WrongApiKeyException;
import io.rateboard.reservationapi.service.ReservationService;
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
    public ReservationResponseDto makeReservation(String apiKey, ReservationRequestDto request) {
        if (!apiKey.equals(serviceApiKey)) {
            throw new WrongApiKeyException();
        }

        return reservationService.makeReservation(request);
    }
}
