package xyz.wavey.rentalservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.wavey.rentalservice.model.Rental;

public interface RentalRepo extends JpaRepository<Rental, Long> {

}
