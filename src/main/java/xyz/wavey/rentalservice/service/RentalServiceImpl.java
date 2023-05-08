package xyz.wavey.rentalservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import xyz.wavey.rentalservice.base.exception.ServiceException;
import xyz.wavey.rentalservice.repository.InsuranceRepo;
import xyz.wavey.rentalservice.model.Rental;
import xyz.wavey.rentalservice.repository.RentalRepo;
import xyz.wavey.rentalservice.vo.RequestAddRental;

import static xyz.wavey.rentalservice.base.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService{

    private final RentalRepo rentalRepo;
    private final InsuranceRepo insuranceRepo;

    @Override
    public ResponseEntity<Object> addRental(RequestAddRental requestAddRental) {
        Rental rental = rentalRepo.save(Rental.builder()
                .userId(requestAddRental.getUserId())
                .vehicleId(requestAddRental.getVehicleId())
                .startDate(requestAddRental.getStartDate())
                .endDate(requestAddRental.getEndDate())
                .startZone(requestAddRental.getStartZone())
                .returnZone(requestAddRental.getReturnZone())
                .keyAuth(false)
                .payment(requestAddRental.getPayment())
                .price(requestAddRental.getPrice())
                .insurance(insuranceRepo.findById(requestAddRental.getInsuranceId())
                        .orElseThrow(() -> new ServiceException(
                                NOT_FOUND_RENTAL.getMessage(),
                                NOT_FOUND_RENTAL.getHttpStatus())))
                .build());

        return ResponseEntity.status(HttpStatus.CREATED).body(rental.getId());
    }

    @Override
    public ResponseEntity<Object> getRental(Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(rentalRepo.findById(id)
                .orElseThrow(() -> new ServiceException(
                        NOT_FOUND_RENTAL.getMessage(),
                        NOT_FOUND_RENTAL.getHttpStatus())));
    }

    @Override
    public ResponseEntity<Object> deleteRental(Long id) {
        if (rentalRepo.findById(id).isPresent()) {
            rentalRepo.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity
                    .status(NOT_FOUND_RENTAL.getHttpStatus())
                    .body(NOT_FOUND_RENTAL.getMessage());
        }
    }

}
