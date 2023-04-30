package xyz.wavey.rentalservice.rental.service;

import org.springframework.http.ResponseEntity;
import xyz.wavey.rentalservice.rental.vo.RequestAddRental;

public interface RentalService {

    ResponseEntity<Object> addRental(RequestAddRental requestAddRental);

    ResponseEntity<Object> getRental(Long id);

    ResponseEntity<Object> deleteRental(Long id);
}
