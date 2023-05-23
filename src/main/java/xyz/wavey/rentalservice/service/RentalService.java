package xyz.wavey.rentalservice.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import xyz.wavey.rentalservice.model.Rental;
import xyz.wavey.rentalservice.vo.*;

import java.util.List;

public interface RentalService {

    Rental addRental(RequestAddRental requestAddRental);

    List<ResponseGetAllRental> getAllRental(String uuid, String purchaseState);

    ResponseGetRental getRental(Long id);

    HttpStatus deleteRental(Long id);

    HttpStatus cancelRental(Long id);

    ResponseReturnVehicle returnVehicle(Long id, RequestReturn requestReturn);

    ResponseEntity<Object> openSmartKey(Long id);
}
