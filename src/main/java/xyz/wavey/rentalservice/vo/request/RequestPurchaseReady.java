package xyz.wavey.rentalservice.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestPurchaseReady {

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

}
