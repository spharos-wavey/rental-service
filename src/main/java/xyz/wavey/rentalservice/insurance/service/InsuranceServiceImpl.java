package xyz.wavey.rentalservice.insurance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import xyz.wavey.rentalservice.insurance.model.Insurance;
import xyz.wavey.rentalservice.insurance.ropository.InsuranceRepo;
import xyz.wavey.rentalservice.insurance.vo.RequestAddInsurance;

@Service
@RequiredArgsConstructor
public class InsuranceServiceImpl implements InsuranceService {

    private final InsuranceRepo insuranceRepo;

    @Override
    public ResponseEntity<Object> addInsurance(RequestAddInsurance requestAddInsurance) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(insuranceRepo.save(Insurance.builder()
                        .name(requestAddInsurance.getName())
                        .price(requestAddInsurance.getPrice())
                        .content(requestAddInsurance.getContent())
                        .build()));
    }

    @Override
    public ResponseEntity<Object> getInsurance(Integer id) {
        if (insuranceRepo.findById(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(insuranceRepo.findById(id));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ReqInsuranceId is not exist");
        }
    }

    @Override
    public ResponseEntity<Object> getAllInsurance() {
        if (!insuranceRepo.findAll().isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(insuranceRepo.findAll());
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Insurances are not exist");
        }
    }

}
