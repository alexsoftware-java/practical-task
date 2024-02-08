package io.rateboard.reservationapi.repositry;

import io.rateboard.reservationapi.entity.MessageStoreEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageStoreRepository extends CrudRepository<MessageStoreEntity, String> {

    Optional<List<MessageStoreEntity>> findAllByReservationId(String reservationId);
}
