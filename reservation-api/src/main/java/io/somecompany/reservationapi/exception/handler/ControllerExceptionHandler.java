package io.somecompany.reservationapi.exception.handler;


import io.somecompany.reservationapi.dto.ReservationResponseDto;
import io.somecompany.reservationapi.exception.WrongApiKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(WrongApiKeyException.class)
    public ResponseEntity<ReservationResponseDto> handleWrongApiKeyException() {
        return new ResponseEntity<>(ReservationResponseDto.builder().errorCode(100401).build(), HttpStatus.UNAUTHORIZED);
    }
}
