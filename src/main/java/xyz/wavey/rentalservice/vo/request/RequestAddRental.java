package xyz.wavey.rentalservice.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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

    // 크라이언트로 부터 받지 않고 비즈니스 로직에서 추가되는 값
    private String purchaseNumber;

    private String tid;

}
