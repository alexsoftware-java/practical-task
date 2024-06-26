package io.somecompany.reservationapi.controller;

import io.somecompany.reservationapi.controller.contract.MessageStoreController;
import io.somecompany.reservationapi.dto.MessageStoreDto;
import io.somecompany.reservationapi.exception.WrongApiKeyException;
import io.somecompany.reservationapi.service.MessageStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessageStoreControllerImpl implements MessageStoreController {
    private final MessageStoreService messageStoreService;

    @Value("${api.key}")
    private String serviceApiKey;

    @Override
    public ResponseEntity<MessageStoreDto> getStatus(String apiKey, String messageId) {
        if (!apiKey.equals(serviceApiKey)) {
            throw new WrongApiKeyException();
        }
        return messageStoreService.getMessage(messageId)
                .map(messageStoreEntity -> new ResponseEntity<>(messageStoreEntity, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @Override
    public ResponseEntity<List<MessageStoreDto>> getMessages(String apiKey, String reservationId) {
        if (!apiKey.equals(serviceApiKey)) {
            throw new WrongApiKeyException();
        }
        var messages = messageStoreService.getMessagesByReservationId(reservationId);
        return messages
                .map(messageStoreDto -> new ResponseEntity<>(messageStoreDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
