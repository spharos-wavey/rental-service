package xyz.wavey.rentalservice.vo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseGetRental {

    public Long rentalId;
    public Long vehicleId;
    public String startTime;
    public String endTime;
    public String totalRentTime;
    public String insuranceName;
    public Integer insurancePrice;

}
