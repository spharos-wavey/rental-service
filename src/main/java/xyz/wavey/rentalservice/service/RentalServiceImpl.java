package xyz.wavey.rentalservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.wavey.rentalservice.base.exception.ServiceException;
import xyz.wavey.rentalservice.model.PurchaseState;
import xyz.wavey.rentalservice.model.Rental;
import xyz.wavey.rentalservice.repository.RentalRepo;
import xyz.wavey.rentalservice.vo.request.RequestAddRental;
import xyz.wavey.rentalservice.vo.request.RequestReturn;
import xyz.wavey.rentalservice.vo.response.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static xyz.wavey.rentalservice.base.exception.ErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService{

    private final RentalRepo rentalRepo;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public ResponseAddRental addRental(RequestAddRental requestAddRental) {

        Rental rental = rentalRepo.save(Rental.builder()
                .uuid(requestAddRental.getUuid())
                .purchaseState(PurchaseState.RESERVATION)
                .vehicleId(requestAddRental.getVehicleId())
                .endDate(LocalDateTime.parse(requestAddRental.getEndDate(), dateTimeFormatter))
                .startDate(LocalDateTime.parse(requestAddRental.getStartDate(), dateTimeFormatter))
                .returnZone(requestAddRental.getReturnZone())
                .startZone(requestAddRental.getStartZone())
                .paymentMethod(requestAddRental.getPurchaseMethod())
                .price(requestAddRental.getPrice())
                .insuranceId(requestAddRental.getInsuranceId())
                .keyAuth(false)
                .build());

        return ResponseAddRental.builder()
                .rentId(rental.getId())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponseGetAllRental> getAllRental(String uuid, String purchaseState) {
        List<Rental> rentalList;
        if(purchaseState.equals("ALL")){
            rentalList = rentalRepo.findAllByUuid(uuid);
        } else {
            try {
                rentalList = rentalRepo.findAllByUuidAndPurchaseState(uuid, PurchaseState.valueOf(purchaseState));
            } catch (Exception e) {
                throw new ServiceException(BAD_REQUEST_PURCHASE_STATE.getMessage(), BAD_REQUEST_PURCHASE_STATE.getHttpStatus());
            }
        }

        List<ResponseGetAllRental> responseGetAllRentals = new ArrayList<>();
        for(Rental rental : rentalList){
            responseGetAllRentals.add(ResponseGetAllRental.builder()
                    .rentalId(rental.getId())
                    .vehicleId(rental.getVehicleId())
                    .endDate(rental.getEndDate())
                    .startDate(rental.getStartDate())
                    .build());
        }
        return responseGetAllRentals;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseGetRental getRental(String uuid, Long id) {
        Rental rental = rentalRepo.findByIdAndUuid(id, uuid).orElseThrow(()->
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
    public HttpStatus deleteRental(String uuid, Long id) {
        if (rentalRepo.findByIdAndUuid(id, uuid).isPresent()) {
            rentalRepo.deleteById(id);
            return HttpStatus.OK;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }

    @Override
    public HttpStatus cancelRental(String uuid, Long id) {
        if (rentalRepo.findByIdAndUuid(id, uuid).isPresent()){
            Rental rental = rentalRepo.findByIdAndUuid(id, uuid).get();
            rental.setPurchaseState(PurchaseState.CANCELED);
            rentalRepo.save(rental);
            return HttpStatus.OK;
        } else {
            return HttpStatus.NOT_FOUND;
        }
    }

    @Override
    @Transactional(readOnly = false)
    public ResponseReturnVehicle returnVehicle(String uuid, Long id, RequestReturn requestReturn) {
        Rental rental = rentalRepo.findByIdAndUuid(id, uuid).orElseThrow(()->
                new ServiceException(NOT_FOUND_RENTAL.getMessage(),NOT_FOUND_RENTAL.getHttpStatus()));

        if (rental.getReqReturnTime() == null) {
            rental.setFinalPrice(requestReturn.getFinalPrice());
            rental.setReqReturnTime(LocalDateTime.parse(requestReturn.getReturnTime(), dateTimeFormatter));
            rental.setPurchaseState(PurchaseState.RETURNED);
            rental.setKeyAuth(false);
            rentalRepo.save(rental);

            String message = null;
            if (rental.getEndDate().isAfter(LocalDateTime.parse(requestReturn.getReturnTime(), dateTimeFormatter)) &&
                    rental.getStartDate().isBefore(LocalDateTime.parse(requestReturn.getReturnTime(), dateTimeFormatter))) {
                message = "정상적으로 반납 처리 되었습니다.";
            } else if (rental.getEndDate().isBefore(LocalDateTime.parse(requestReturn.getReturnTime(), dateTimeFormatter))) {
                message = "지연 반납 처리 되었습니다.";
            }

            return ResponseReturnVehicle.builder()
                    .httpStatus(HttpStatus.OK)
                    .message(message)
                    .build();
        } else {
            return ResponseReturnVehicle.builder()
                    .httpStatus(CANNOT_RETURN.getHttpStatus())
                    .message(CANNOT_RETURN.getMessage())
                    .build();
        }
    }

    @Override
    @Transactional(readOnly = false)
    public ResponseEntity<Object> openSmartKey(String uuid, Long id) {
        Rental rental = rentalRepo.findByIdAndUuid(id, uuid).orElseThrow(()->
                new ServiceException(NOT_FOUND_RENTAL.getMessage(),NOT_FOUND_RENTAL.getHttpStatus()));
        if(rental.getKeyAuth() == Boolean.FALSE && rental.getStartDate().minusMinutes(16).isBefore(LocalDateTime.now())){
            rental.setKeyAuth(Boolean.TRUE);
            rentalRepo.save(rental);
            return ResponseEntity.status(HttpStatus.OK).build();
        } else if(rental.getKeyAuth() == Boolean.FALSE && rental.getEndDate().isBefore(LocalDateTime.now())){
            return ResponseEntity.status(ENDED_RENTAL_TIME.getHttpStatus()).body(ENDED_RENTAL_TIME.getMessage());
        } else {
            return ResponseEntity.status(FORBIDDEN_SMART_KEY.getHttpStatus()).body(FORBIDDEN_SMART_KEY.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean checkCanRent(String uuid) {
        return !rentalRepo.existsByUuidAndPurchaseState(uuid, PurchaseState.RESERVATION);
    }

}
