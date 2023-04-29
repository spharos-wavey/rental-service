package xyz.wavey.rentalservice.rental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.wavey.rentalservice.rental.model.Rental;

public interface RentalRepo extends JpaRepository<Rental, Long> {

}
