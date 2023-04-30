package xyz.wavey.rentalservice.insurance.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.wavey.rentalservice.insurance.service.InsuranceService;
import xyz.wavey.rentalservice.insurance.vo.RequestAddInsurance;

@RestController
@RequestMapping("/insurance")
@RequiredArgsConstructor
public class InsuranceController {

    private final InsuranceService insuranceService;

    @PostMapping()
    public ResponseEntity<Object> addInsurance(@RequestBody RequestAddInsurance requestAddInsurance) {
        return insuranceService.addInsurance(requestAddInsurance);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getInsurance(@PathVariable Integer id) {
        return insuranceService.getInsurance(id);
    }

    @GetMapping()
    public ResponseEntity<Object> getAllInsurance() {
        return insuranceService.getAllInsurance();
    }
}
