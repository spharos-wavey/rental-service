package xyz.wavey.rentalservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.wavey.rentalservice.service.RentalService;
import xyz.wavey.rentalservice.vo.*;

import java.util.List;

@RestController
@RequestMapping("/rental")
@RequiredArgsConstructor
@Slf4j
public class RentalController {

    private final RentalService rentalService;


    @PostMapping()
    public ResponseEntity<Object> addRental(@RequestBody RequestAddRental requestAddRental) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(rentalService.addRental(requestAddRental));
    }

    @GetMapping("/{purchaseState}")
    public ResponseEntity<List<ResponseGetAllRental>> getAllRental(@RequestHeader("uuid") String uuid,
                                            @PathVariable("purchaseState") String purchaseState){
        List<ResponseGetAllRental> response = rentalService.getAllRental(uuid, purchaseState);

        if (response.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    @GetMapping()
    public ResponseEntity<Object> getRental(@RequestParam("id") Long id){
        ResponseGetRental response = rentalService.getRental(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRental(@PathVariable Long id) {
        return ResponseEntity.status(rentalService.deleteRental(id)).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> returnVehicle(@PathVariable Long id, @RequestBody RequestReturn requestReturn) {
        ResponseReturnVehicle returnVehicle = rentalService.returnVehicle(id, requestReturn);
        return ResponseEntity
                .status(returnVehicle.getHttpStatus())
                .body(returnVehicle.getMessage());
    }

    @PatchMapping("/openKey/{id}")
    public ResponseEntity<Object> openSmartKey(@PathVariable Long id){
        return rentalService.openSmartKey(id);
    }

}
