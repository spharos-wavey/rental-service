package xyz.wavey.rentalservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xyz.wavey.rentalservice.model.Rental;
import java.util.List;
import java.util.Optional;

public interface RentalRepo extends JpaRepository<Rental, Long> {

    @Query(value = "select * from rental where uuid = :uuid and purchase_state = :purchaseState", nativeQuery = true)
    List<Rental> findAllByUuidAndPurchaseState(@Param("uuid") String uuid, @Param("purchaseState") String purchaseState);

    List<Rental> findAllByUuid(String uuid);

    Optional<Rental> findByIdAndUuid(Long id, String uuid);

}
