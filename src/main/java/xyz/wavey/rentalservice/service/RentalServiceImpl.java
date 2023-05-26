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
    public Rental addRental(RequestAddRental requestAddRental) {

        LocalDateTime startDate;
        LocalDateTime endDate;
        try {
            startDate = LocalDateTime.parse(requestAddRental.getStartDate(), dateTimeFormatter);
            endDate = LocalDateTime.parse(requestAddRental.getEndDate(), dateTimeFormatter);
        } catch (Exception e) {
            throw new ServiceException(BAD_REQUEST_DATEFORMAT.getMessage(), BAD_REQUEST_DATEFORMAT.getHttpStatus());
        }

        if (!rentalRepo.checkUserCanBool(requestAddRental.getUuid(), startDate, endDate).isEmpty()) {
            throw new ServiceException(
                    BAD_REQUEST_RENTAL_DUPLICATED.getMessage(),
                    BAD_REQUEST_RENTAL_DUPLICATED.getHttpStatus()
            );
        }

        return rentalRepo.save(Rental.builder()
                .uuid(requestAddRental.getUuid())
                .purchaseState(PurchaseState.RESERVATION)
                .vehicleId(requestAddRental.getVehicleId())
                .endDate(endDate)
                .startDate(startDate)
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
            Rental rental = rentalRepo.findById(id).get();
            rental.setPurchaseState(PurchaseState.CANCELED);
            rentalRepo.save(rental);
            return HttpStatus.OK;
        } else {
            return HttpStatus.NOT_FOUND;
        }
    }

    @Override
    public ResponseReturnVehicle returnVehicle(String uuid, Long id, RequestReturn requestReturn) {
        Rental rental = rentalRepo.findByIdAndUuid(id, uuid).orElseThrow(()->
                new ServiceException(NOT_FOUND_RENTAL.getMessage(),NOT_FOUND_RENTAL.getHttpStatus()));

        if (rental.getReqReturnTime() == null) {
            rental.setFinalPrice(requestReturn.getFinalPrice());
            rental.setReqReturnTime(LocalDateTime.parse(requestReturn.getReturnTime(), dateTimeFormatter));
            rental.setPurchaseState(PurchaseState.RETURNED);
            rental.setKeyAuth(false);
            rentalRepo.save(rental);

            //todo 대여 시작 시간보다 이른 시간에 반납이 가능한가? 가능하다면 어떻게 처리할 것인가에 대한 논의 필요 - 05/24 - 김지욱
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
            return ResponseEntity.status(FORBIDDDEN_SMARTKEY.getHttpStatus()).body(FORBIDDDEN_SMARTKEY.getMessage());
        }
    }

}
