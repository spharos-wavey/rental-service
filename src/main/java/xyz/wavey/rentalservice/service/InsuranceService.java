package xyz.wavey.rentalservice.service;

import xyz.wavey.rentalservice.model.Insurance;
import xyz.wavey.rentalservice.vo.request.RequestAddInsurance;

import java.util.List;

public interface InsuranceService {

    Insurance addInsurance(RequestAddInsurance requestAddInsurance);

    Insurance getInsurance(Integer id);

    List<Insurance> getAllInsurance();

}
