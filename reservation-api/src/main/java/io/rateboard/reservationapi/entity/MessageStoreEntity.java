package io.rateboard.reservationapi.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.Instant;

@RedisHash("MessageStore")
@Getter
@Setter
@Builder
public class MessageStoreEntity implements Serializable {
    @Id
    private String messageId;
    private String reservationId;
    private Instant createdAt;
    private Instant processedAt;
}
