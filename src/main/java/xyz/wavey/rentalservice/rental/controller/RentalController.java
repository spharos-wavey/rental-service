package xyz.wavey.rentalservice.rental.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.wavey.rentalservice.rental.service.RentalService;
import xyz.wavey.rentalservice.rental.vo.RequestAddRental;

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

}
