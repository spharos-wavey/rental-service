package xyz.wavey.rentalservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import xyz.wavey.rentalservice.base.exception.ServiceException;
import xyz.wavey.rentalservice.model.Insurance;
import xyz.wavey.rentalservice.repository.InsuranceRepo;
import xyz.wavey.rentalservice.model.Rental;
import xyz.wavey.rentalservice.repository.RentalRepo;
import xyz.wavey.rentalservice.vo.RequestAddRental;
import xyz.wavey.rentalservice.vo.ResponseGetRental;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        Rental rental = rentalRepo.findById(id)
                .orElseThrow(() -> new ServiceException(
                        NOT_FOUND_RENTAL.getMessage(),
                        NOT_FOUND_RENTAL.getHttpStatus()));

        Insurance insurance = rental.getInsurance();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM-dd HH:mm");

        long durationSecond = Duration.between(rental.getStartDate(), rental.getEndDate()).toSeconds();
        long day = durationSecond / (60 * 60 * 24);
        long hour = (durationSecond % (60 * 60 * 24)) / (60 * 60);
        long minute = ((durationSecond % (60 * 60 * 24)) % (60 * 60)) / 60;

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseGetRental.builder()
                        .rentalId(id)
                        .vehicleId(rental.getVehicleId())
                        .startTime(rental.getStartDate().format(dateTimeFormatter))
                        .endTime(rental.getEndDate().format(dateTimeFormatter))
                        .totalRentTime(String.format("%d %d:%d", day, hour, minute))
                        .insuranceName(insurance.getName())
                        .insurancePrice(insurance.getPrice())
                        .build());
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

    @Override
    public ResponseEntity<Object> returnVehicle(Long id) {
        Rental rental = rentalRepo.findById(id).orElseThrow(()->
                new ServiceException(NOT_FOUND_RENTAL.getMessage(),NOT_FOUND_RENTAL.getHttpStatus()));
        if(rental.getReqReturnTime() == null){
            LocalDateTime now = LocalDateTime.now();
            rental.setReqReturnTime(now);
            rentalRepo.save(rental);
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity
                    .status(ALREADY_PROCESSED_RETURN.getHttpStatus())
                    .body(ALREADY_PROCESSED_RETURN.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> openSmartKey(Long id) {
        Rental rental = rentalRepo.findById(id).orElseThrow(()->
                new ServiceException(NOT_FOUND_RENTAL.getMessage(),NOT_FOUND_RENTAL.getHttpStatus()));
        if(rental.getKeyAuth() == Boolean.FALSE && rental.getStartDate().minusMinutes(16).isBefore(LocalDateTime.now())){
            rental.setKeyAuth(Boolean.TRUE);
            rentalRepo.save(rental);
            return ResponseEntity.status(HttpStatus.OK).build();
        } else if(rental.getKeyAuth() == Boolean.FALSE && rental.getEndDate().isBefore(LocalDateTime.now())){
            return ResponseEntity.status(ENDED_RENTAL_TIME.getHttpStatus()).body(ENDED_RENTAL_TIME.getMessage());
        } else {
            return ResponseEntity.status(FORBIDDDEN_SMARTKEY.getHttpStatus()).body(FORBIDDDEN_SMARTKEY.getMessage());
        }
    }

}
