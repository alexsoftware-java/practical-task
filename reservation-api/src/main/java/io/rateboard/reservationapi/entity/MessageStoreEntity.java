package io.rateboard.reservationapi.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.time.Instant;

@RedisHash("MessageStore")
@TypeAlias("MessageStore")
@Getter
@Setter
@Builder
public class MessageStoreEntity implements Serializable {
    @Id
    private String messageId;
    @Indexed
    private String reservationId;
    private Instant createdAt;
    private Instant processedAt;
}
