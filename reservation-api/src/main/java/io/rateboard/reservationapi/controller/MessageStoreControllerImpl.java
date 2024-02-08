package io.rateboard.reservationapi.controller;

import io.rateboard.reservationapi.controller.contract.MessageStoreController;
import io.rateboard.reservationapi.entity.MessageStoreEntity;
import io.rateboard.reservationapi.exception.WrongApiKeyException;
import io.rateboard.reservationapi.service.MessageStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageStoreControllerImpl implements MessageStoreController {
    private final MessageStoreService messageStoreService;

    @Value("${api.key}")
    private String serviceApiKey;

    @Override
    public ResponseEntity<MessageStoreEntity> getStatus(String apiKey, String messageId) {
        if (!apiKey.equals(serviceApiKey)) {
            throw new WrongApiKeyException();
        }
        var response = messageStoreService.getMessage(messageId);
        return response
                .map(messageStoreEntity -> new ResponseEntity<>(messageStoreEntity, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }
}
