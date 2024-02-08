package io.rateboard.reservationapi.controller.contract;

import io.rateboard.reservationapi.entity.MessageStoreEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Endpoints for Hotel reservation systems (PMS) - get message status", description = "API allow to get processing status from external systems")
@RestController
@RequestMapping("/api/v1/reservation")
public interface MessageStoreController {
    @Operation(summary = "Get message status (processed ot not)")
    @GetMapping("/{messageId}")
    ResponseEntity<MessageStoreEntity> getStatus(
            @RequestHeader(name = "Authorization") String apiKey,
            @PathVariable String messageId
    );
}
