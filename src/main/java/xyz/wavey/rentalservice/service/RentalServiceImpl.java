package xyz.wavey.rentalservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import xyz.wavey.rentalservice.base.exception.ServiceException;
import xyz.wavey.rentalservice.model.PurchaseState;
import xyz.wavey.rentalservice.model.Rental;
import xyz.wavey.rentalservice.repository.RentalRepo;
import xyz.wavey.rentalservice.vo.*;

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
    public Rental addRental(RequestAddRental requestAddRental) {
        return rentalRepo.save(Rental.builder()
                .uuid(requestAddRental.getUuid())
                .purchaseState(PurchaseState.RESERVATION)
                .vehicleId(requestAddRental.getVehicleId())
                .price(requestAddRental.getPrice())
                .endDate(requestAddRental.getEndDate())
                .startDate(requestAddRental.getStartDate())
                .returnZone(requestAddRental.getReturnZone())
                .startZone(requestAddRental.getStartZone())
                .paymentMethod(requestAddRental.getPaymentMethod())
                .price(requestAddRental.getPrice())
                .insuranceId(requestAddRental.getInsuranceId())
                .keyAuth(false)
                .build());
    }

    @Override
    public List<ResponseGetAllRental> getAllRental(String uuid, String purchaseState) {
        List<Rental> rentalList;
        if(purchaseState.equals("ALL")){
            rentalList = rentalRepo.findAllByUuid(uuid);
        } else {
            try {
                PurchaseState.valueOf(purchaseState);
            } catch (Exception e) {
                throw new ServiceException(BAD_REQUEST_PURCHASE_STATE.getMessage(), BAD_REQUEST_PURCHASE_STATE.getHttpStatus());
            }
            rentalList = rentalRepo.findAllByUuidAndPurchaseState(uuid, purchaseState);
        }

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
                .finalPrice(rental.getFinalPrice())
                .paymentMethod(rental.getPaymentMethod())
                .insuranceId(rental.getInsuranceId())
                .build();
    }

    @Override
    public HttpStatus deleteRental(Long id) {
        if (rentalRepo.findById(id).isPresent()) {
            rentalRepo.deleteById(id);
            return HttpStatus.OK;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }

    @Override
    public HttpStatus cancelRental(Long id) {
        if (rentalRepo.findById(id).isPresent()){
            Rental rental = rentalRepo.findById(id).get();
            rental.setPurchaseState(PurchaseState.CANCELLED);
            rentalRepo.save(rental);
            return HttpStatus.OK;
        } else {
            return HttpStatus.NOT_FOUND;
        }
    }

    @Override
    public ResponseReturnVehicle returnVehicle(Long id, RequestReturn requestReturn) {
        Rental rental = rentalRepo.findById(id).orElseThrow(()->
                new ServiceException(NOT_FOUND_RENTAL.getMessage(),NOT_FOUND_RENTAL.getHttpStatus()));

        if(rental.getReqReturnTime() == null && rental.getEndDate().isAfter(requestReturn.getReturnTime())
                && rental.getStartDate().isBefore(requestReturn.getReturnTime())){
            rental.setFinalPrice(requestReturn.getFinalPrice());
            rental.setReqReturnTime(requestReturn.getReturnTime());
            rentalRepo.save(rental);
            return ResponseReturnVehicle.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("정상적으로 반납 처리 되었습니다.")
                    .build();
        } else if(rental.getEndDate().isBefore(requestReturn.getReturnTime())){
            rental.setFinalPrice(requestReturn.getFinalPrice());
            rental.setReqReturnTime(requestReturn.getReturnTime());
            rentalRepo.save(rental);
            return ResponseReturnVehicle.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("지연 반납 처리 되었습니다.")
                    .build();
        } else {
            return ResponseReturnVehicle.builder()
                    .httpStatus(CANNOT_RETURN.getHttpStatus())
                    .message(CANNOT_RETURN.getMessage())
                    .build();
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
