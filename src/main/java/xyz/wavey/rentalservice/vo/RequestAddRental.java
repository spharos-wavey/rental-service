package xyz.wavey.rentalservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestAddRental {

    private String uuid;

    private Long vehicleId;

    private String startDate;

    private String endDate;

    private Integer startZone;

    private Integer returnZone;

    private String paymentMethod;

    private Integer price;

    private Integer insuranceId;

}
