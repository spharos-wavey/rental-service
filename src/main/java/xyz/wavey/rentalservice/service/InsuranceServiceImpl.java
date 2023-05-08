package xyz.wavey.rentalservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import xyz.wavey.rentalservice.base.exception.ServiceException;
import xyz.wavey.rentalservice.model.Insurance;
import xyz.wavey.rentalservice.repository.InsuranceRepo;
import xyz.wavey.rentalservice.vo.RequestAddInsurance;

import static xyz.wavey.rentalservice.base.exception.ErrorCode.*;

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
        return ResponseEntity.status(HttpStatus.OK).body(insuranceRepo.findById(id)
                .orElseThrow(() -> new ServiceException(
                        NOT_FOUND_INSURANCE.getMessage(),
                        NOT_FOUND_INSURANCE.getHttpStatus()
                )));
    }

    @Override
    public ResponseEntity<Object> getAllInsurance() {
        if (!insuranceRepo.findAll().isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(insuranceRepo.findAll());
        } else {
            return ResponseEntity
                    .status(NOT_FOUND_INSURANCE.getHttpStatus())
                    .body(NOT_FOUND_INSURANCE.getMessage());
        }
    }

}
