package xyz.wavey.rentalservice.vo.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestAddRental {

    private String uuid;

    private Long vehicleId;

    private String carName;

    private String carBrandName;

    private String startDate;

    private String endDate;

    private Integer startZone;

    private Integer returnZone;

    private Integer price;

    private Integer insuranceId;

    private Integer reward;

    private String purchaseNumber;

    private String tid;

    private String purchaseMethod;

}
