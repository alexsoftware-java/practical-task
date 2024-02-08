package io.rateboard.reservationapi.controller.contract;

import io.rateboard.reservationapi.dto.ReservationRequestDto;
import io.rateboard.reservationapi.dto.ReservationResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Endpoints for Hotel reservation systems (PMS)", description = "API allow to stream reservation details to external systems")
@RestController
@RequestMapping("/api/v1/reservation")
public interface ReservationController {

    @Operation(summary = "Reservation request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"errorCode\": null}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Please provide valid API key", content = @Content),
            @ApiResponse(responseCode = "400", description = "Request is not valid", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error occurred", content = @Content)
    })
    @PostMapping
    ReservationResponseDto sendReservation(
            @RequestHeader(name = "Authorization") String apiKey,
            @Valid @Parameter(description = "JSON represents search request") @RequestBody ReservationRequestDto request);
    
}
