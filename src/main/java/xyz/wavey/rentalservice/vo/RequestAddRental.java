package xyz.wavey.rentalservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.wavey.rentalservice.model.PurchaseState;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestAddRental {

    private String uuid;

    private Long vehicleId;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Integer startZone;

    private Integer returnZone;

    private String payment;

    private Integer price;

    private PurchaseState purchaseState;

    private Integer insuranceId;

}
