package xyz.wavey.rentalservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.wavey.rentalservice.service.RentalService;
import xyz.wavey.rentalservice.vo.RequestAddRental;
import xyz.wavey.rentalservice.vo.RequestReturnTime;
import xyz.wavey.rentalservice.vo.ResponseGetAllRental;
import xyz.wavey.rentalservice.vo.ResponseGetRental;

import java.util.List;

@RestController
@RequestMapping("/rental")
@RequiredArgsConstructor
@Slf4j
public class RentalController {

    private final RentalService rentalService;


    @PostMapping()
    public ResponseEntity<Object> addRental(@RequestBody RequestAddRental requestAddRental) {
        return rentalService.addRental(requestAddRental);
    }

    @GetMapping("/{purchaseState}")
    public ResponseEntity<List<ResponseGetAllRental>> getAllRental(@RequestHeader("uuid") String uuid,
                                            @PathVariable("purchaseState") String purchaseState){
        List<ResponseGetAllRental> response = rentalService.getAllRental(uuid, purchaseState);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping()
    public ResponseEntity<Object> getRental(@RequestParam("id") Long id){
        ResponseGetRental response = rentalService.getRental(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRental(@PathVariable Long id) {
        return rentalService.deleteRental(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> returnVehicle(@PathVariable Long id, @RequestBody RequestReturnTime requestReturnTime) {
        return rentalService.returnVehicle(id,requestReturnTime);
    }

    @PatchMapping("/openKey/{id}")
    public ResponseEntity<Object> openSmartKey(@PathVariable Long id){
        return rentalService.openSmartKey(id);
    }

}
