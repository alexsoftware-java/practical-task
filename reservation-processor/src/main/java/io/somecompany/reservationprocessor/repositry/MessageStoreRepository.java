package io.somecompany.reservationprocessor.repositry;

import io.somecompany.reservationprocessor.entity.MessageStoreEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageStoreRepository extends CrudRepository<MessageStoreEntity, String> {}
