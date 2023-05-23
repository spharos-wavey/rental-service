package xyz.wavey.rentalservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String paymentMethod;

    private Integer price;

    private Integer finalPrice;

    private Integer insuranceId;

}
