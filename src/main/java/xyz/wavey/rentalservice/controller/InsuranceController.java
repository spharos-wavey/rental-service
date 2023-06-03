package xyz.wavey.rentalservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.wavey.rentalservice.model.Insurance;
import xyz.wavey.rentalservice.service.InsuranceService;
import xyz.wavey.rentalservice.vo.request.RequestAddInsurance;

import java.util.List;

@RestController
@RequestMapping("/insurance")
@RequiredArgsConstructor
public class InsuranceController {

    private final InsuranceService insuranceService;

    @PostMapping()
    public ResponseEntity<Object> addInsurance(@RequestBody RequestAddInsurance requestAddInsurance) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(insuranceService.addInsurance(requestAddInsurance));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getInsurance(@PathVariable Integer id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(insuranceService.getInsurance(id));
    }

    @GetMapping()
    public ResponseEntity<Object> getAllInsurance() {
        List<Insurance> insuranceList = insuranceService.getAllInsurance();
        if (insuranceList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(insuranceList);
        }
    }
}
