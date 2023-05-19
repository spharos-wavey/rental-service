package xyz.wavey.rentalservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import xyz.wavey.rentalservice.base.exception.ServiceException;
import xyz.wavey.rentalservice.model.Rental;
import xyz.wavey.rentalservice.repository.RentalRepo;
import xyz.wavey.rentalservice.vo.RequestAddRental;
import xyz.wavey.rentalservice.vo.RequestReturnTime;
import xyz.wavey.rentalservice.vo.ResponseGetAllRental;
import xyz.wavey.rentalservice.vo.ResponseGetRental;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static xyz.wavey.rentalservice.base.exception.ErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService{

    private final RentalRepo rentalRepo;

    @Override
    public ResponseEntity<Object> addRental(RequestAddRental requestAddRental) {
        Rental rental = rentalRepo.save(Rental.builder()
                .uuid(requestAddRental.getUuid())
                .purchaseState(requestAddRental.getPurchaseState())
                .vehicleId(requestAddRental.getVehicleId())
                .price(requestAddRental.getPrice())
                .endDate(requestAddRental.getEndDate())
                .startDate(requestAddRental.getStartDate())
                .returnZone(requestAddRental.getReturnZone())
                .startZone(requestAddRental.getStartZone())
                .payment(requestAddRental.getPayment())
                .price(requestAddRental.getPrice())
                .insuranceId(requestAddRental.getInsuranceId())
                .keyAuth(false)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).body(rental.getId());
    }

    @Override
    public List<ResponseGetAllRental> getAllRental(String uuid, String purchaseState) {
        List<Rental> rentalList = rentalRepo.findAllByUuidAndPurchaseState(uuid, purchaseState);
        if (rentalList.isEmpty())
            throw new ServiceException(NOT_FOUND_RENTAL.getMessage(),NOT_FOUND_RENTAL.getHttpStatus());
        List<ResponseGetAllRental> responseGetAllRentals = new ArrayList<>();
        for(Rental rental : rentalList){
            ResponseGetAllRental responseGetRental = ResponseGetAllRental.builder()
                    .rentalId(rental.getId())
                    .vehicleId(rental.getVehicleId())
                    .endDate(rental.getEndDate())
                    .startDate(rental.getStartDate())
                    .build();
            responseGetAllRentals.add(responseGetRental);
        }
        return responseGetAllRentals;
    }

    @Override
    public ResponseGetRental getRental(Long id) {
        Rental rental = rentalRepo.findById(id).orElseThrow(()->
                new ServiceException(NOT_FOUND_RENTAL.getMessage(),NOT_FOUND_RENTAL.getHttpStatus()));
        return ResponseGetRental.builder()
                .rentalId(rental.getId())
                .vehicleId(rental.getVehicleId())
                .endDate(rental.getEndDate())
                .startDate(rental.getStartDate())
                .billitaZoneId(rental.getReturnZone())
                .price(rental.getPrice())
                .payment(rental.getPayment())
                .insuranceId(rental.getInsuranceId())
                .build();
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
    public ResponseEntity<Object> returnVehicle(Long id, RequestReturnTime requestReturnTime) {
        Rental rental = rentalRepo.findById(id).orElseThrow(()->
                new ServiceException(NOT_FOUND_RENTAL.getMessage(),NOT_FOUND_RENTAL.getHttpStatus()));
        if(rental.getReqReturnTime() == null && rental.getEndDate().isAfter(requestReturnTime.getReturnTime())
                && rental.getStartDate().isBefore(requestReturnTime.getReturnTime())){
            rental.setReqReturnTime(requestReturnTime.getReturnTime());
            rentalRepo.save(rental);
            return ResponseEntity.status(HttpStatus.OK).body("정상적으로 반납 처리 되었습니다.");
        } else if(rental.getEndDate().isBefore(requestReturnTime.getReturnTime())){
            rental.setReqReturnTime(requestReturnTime.getReturnTime());
            rentalRepo.save(rental);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("지연 반납 처리 되었습니다.");
        } else {
            return ResponseEntity.status(CANNOT_RETURN.getHttpStatus()).body(CANNOT_RETURN.getMessage());
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
