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

    HttpStatus deleteRental(String uuid, Long id);

    HttpStatus cancelRental(String uuid, Long id);

    ResponseReturnVehicle returnVehicle(String uuid, Long id, RequestReturn requestReturn);

    ResponseEntity<Object> openSmartKey(String uuid, Long id);

}
