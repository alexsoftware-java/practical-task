package io.rateboard.reservationapi.controller.contract;

import io.rateboard.reservationapi.dto.MessageStoreDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Endpoints for Hotel reservation systems (PMS) - get message status", description = "API allow to get processing status from external systems")
@RestController
@RequestMapping("/api/v1/reservation")
public interface MessageStoreController {
    @Operation(summary = "Get message status (processed ot not)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "messageId": "65109a5f-1a97-45bb-bb6b-037d65d5c914",
                      "receivedAt": "2024-02-08T18:22:07.367233939Z",
                      "processedAt": "2024-02-08T18:22:07.748190600Z"
                    }""")))})
    @GetMapping("/message/{messageId}")
    ResponseEntity<MessageStoreDto> getStatus(
            @RequestHeader(name = "Authorization") String apiKey,
            @PathVariable String messageId
    );

    @Operation(summary = "Get list of messages by reservationId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    [
                     {
                        "messageId": "28d922f9-0c42-4570-a93e-4ba5111489de",
                        "receivedAt": "2024-02-08T18:18:06.127756400Z",
                        "processedAt": "2024-02-08T18:18:06.146148300Z"
                      },
                      {
                        "messageId": "65109a5f-1a97-45bb-bb6b-037d65d5c914",
                        "receivedAt": "2024-02-08T18:22:07.367233939Z",
                        "processedAt": "2024-02-08T18:22:07.748190600Z"
                      }
                     ]""")))})
    @GetMapping("/{reservationId}")
    ResponseEntity<List<MessageStoreDto>> getMessages(
            @RequestHeader(name = "Authorization") String apiKey,
            @PathVariable String reservationId
    );
}
