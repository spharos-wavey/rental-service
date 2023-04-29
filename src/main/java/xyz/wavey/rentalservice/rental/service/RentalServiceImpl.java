package xyz.wavey.rentalservice.rental.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import xyz.wavey.rentalservice.rental.model.Rental;
import xyz.wavey.rentalservice.rental.repository.RentalRepo;
import xyz.wavey.rentalservice.rental.vo.RequestAddRental;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService{

    private final RentalRepo rentalRepo;

    @Override
    public ResponseEntity<Object> addRental(RequestAddRental requestAddRental) {
        rentalRepo.save(Rental.builder()
                .userId(requestAddRental.getUserId())
                .vehicleId(requestAddRental.getVehicleId())
                .startDate(requestAddRental.getStartDate())
                .endDate(requestAddRental.getEndDate())
                .startZone(requestAddRental.getStartZone())
                .returnZone(requestAddRental.getReturnZone())
                .payment(requestAddRental.getPayment())
                .price(requestAddRental.getPrice())
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
