package io.rateboard.reservationprocessor.repositry;

import io.rateboard.reservationprocessor.entity.MessageStoreEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageStoreRepository extends CrudRepository<MessageStoreEntity, String> {}
