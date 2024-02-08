package io.rateboard.reservationapi.controller.contract;

import io.rateboard.reservationapi.dto.MessageStoreDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Endpoints for Hotel reservation systems (PMS) - get message status", description = "API allow to get processing status from external systems")
@RestController
@RequestMapping("/api/v1/reservation")
public interface MessageStoreController {
    @Operation(summary = "Get message status (processed ot not)")
    @GetMapping("/message/{messageId}")
    ResponseEntity<MessageStoreDto> getStatus(
            @RequestHeader(name = "Authorization") String apiKey,
            @PathVariable String messageId
    );

    @Operation(summary = "Get list of messages by reservationId")
    @GetMapping("/{reservationId}")
    ResponseEntity<List<MessageStoreDto>> getMessages(
            @RequestHeader(name = "Authorization") String apiKey,
            @PathVariable String reservationId
    );
}
