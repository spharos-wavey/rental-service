package xyz.wavey.rentalservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.wavey.rentalservice.service.RentalService;
import xyz.wavey.rentalservice.vo.RequestAddRental;
import xyz.wavey.rentalservice.vo.RequestReturnTime;

@RestController
@RequestMapping("/rental")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    @PostMapping()
    public ResponseEntity<Object> addRental(@RequestBody RequestAddRental requestAddRental) {
        return rentalService.addRental(requestAddRental);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getRental(@PathVariable Long id) {
        return rentalService.getRental(id);
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
