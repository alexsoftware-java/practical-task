package io.somecompany.reservationapi.repositry;

import io.somecompany.reservationapi.entity.MessageStoreEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageStoreRepository extends CrudRepository<MessageStoreEntity, String> {
    List<MessageStoreEntity> findAllByReservationId(String reservationId);
}
