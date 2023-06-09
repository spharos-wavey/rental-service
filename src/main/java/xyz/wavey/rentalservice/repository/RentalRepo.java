package xyz.wavey.rentalservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xyz.wavey.rentalservice.model.PurchaseState;
import xyz.wavey.rentalservice.model.Rental;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RentalRepo extends JpaRepository<Rental, Long> {

    @Query(value = "select r from Rental as r where r.uuid = :uuid and r.purchaseState = :purchaseState")
    List<Rental> findAllByUuidAndPurchaseState(@Param("uuid") String uuid, @Param("purchaseState") PurchaseState purchaseState);

    List<Rental> findAllByUuid(String uuid);

    Optional<Rental> findByIdAndUuid(Long id, String uuid);

    @Query(value = "select r from Rental as r where r.uuid = :uuid and r.endDate > :startDate and r.startDate < :endDate and r.purchaseState = :purchaseState")
    List<Rental> checkUserCanBook(@Param("uuid") String uuid, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("purchaseState") PurchaseState purchaseState);

    Boolean existsByUuidAndPurchaseState(String uuid, PurchaseState purchaseState);
}
