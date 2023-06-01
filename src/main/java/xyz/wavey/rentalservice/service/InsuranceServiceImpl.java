package xyz.wavey.rentalservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.wavey.rentalservice.base.exception.ServiceException;
import xyz.wavey.rentalservice.model.Insurance;
import xyz.wavey.rentalservice.repository.InsuranceRepo;
import xyz.wavey.rentalservice.vo.request.RequestAddInsurance;

import java.util.List;

import static xyz.wavey.rentalservice.base.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class InsuranceServiceImpl implements InsuranceService {

    private final InsuranceRepo insuranceRepo;

    @Override
    public Insurance addInsurance(RequestAddInsurance requestAddInsurance) {
        return insuranceRepo.save(Insurance.builder()
                .name(requestAddInsurance.getName())
                .price(requestAddInsurance.getPrice())
                .content(requestAddInsurance.getContent())
                .build());
    }

    @Override
    public Insurance getInsurance(Integer id) {
        return insuranceRepo.findById(id).orElseThrow(()
                -> new ServiceException(
                    NOT_FOUND_INSURANCE.getMessage(),
                    NOT_FOUND_INSURANCE.getHttpStatus()
        ));
    }

    @Override
    public List<Insurance> getAllInsurance() {
        return insuranceRepo.findAll();
    }

}
