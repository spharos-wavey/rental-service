package xyz.wavey.rentalservice.service;

import org.springframework.http.ResponseEntity;
import xyz.wavey.rentalservice.vo.RequestAddRental;

public interface RentalService {

    ResponseEntity<Object> addRental(RequestAddRental requestAddRental);

    ResponseEntity<Object> getRental(Long id);

    ResponseEntity<Object> deleteRental(Long id);

    ResponseEntity<Object> returnVehicle(Long id);

    ResponseEntity<Object>  openSmartKey(Long id);
}
