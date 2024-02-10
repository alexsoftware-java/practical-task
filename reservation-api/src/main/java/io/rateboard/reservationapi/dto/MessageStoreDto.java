package io.rateboard.reservationapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
@Schema(description = "Message and it's processing status")
public class MessageStoreDto {
    @Schema(description = "Message id", example = "d4a9eee4-829b-46f2-887d-ac6e963a3c1f")
    private String messageId;
    @Schema(description = "Created timestamp", example = "2024-02-08T10:49:28.684878674Z")
    private Instant createdAt;
    @Schema(description = "Processed timestamp", example = "null or 2024-02-08T10:49:29.014878674Z")
    private Instant processedAt;
}
