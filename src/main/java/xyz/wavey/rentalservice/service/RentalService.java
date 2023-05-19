package xyz.wavey.rentalservice.service;

import org.springframework.http.ResponseEntity;
import xyz.wavey.rentalservice.vo.RequestAddRental;
import xyz.wavey.rentalservice.vo.RequestReturnTime;
import xyz.wavey.rentalservice.vo.ResponseGetAllRental;
import xyz.wavey.rentalservice.vo.ResponseGetRental;

import java.util.List;

public interface RentalService {

    ResponseEntity<Object> addRental(RequestAddRental requestAddRental);

    List<ResponseGetAllRental> getAllRental(String uuid, String purchaseState);

    ResponseGetRental getRental(Long id);

    ResponseEntity<Object> deleteRental(Long id);

    ResponseEntity<Object> returnVehicle(Long id, RequestReturnTime requestReturnTime);

    ResponseEntity<Object>  openSmartKey(Long id);
}
