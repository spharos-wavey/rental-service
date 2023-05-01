package xyz.wavey.rentalservice.insurance.service;

import org.springframework.http.ResponseEntity;
import xyz.wavey.rentalservice.insurance.vo.RequestAddInsurance;

public interface InsuranceService {

    ResponseEntity<Object> addInsurance(RequestAddInsurance requestAddInsurance);

    ResponseEntity<Object> getInsurance(Integer id);

    ResponseEntity<Object> getAllInsurance();
}
