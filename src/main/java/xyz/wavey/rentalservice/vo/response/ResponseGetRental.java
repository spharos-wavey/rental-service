package xyz.wavey.rentalservice.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseGetRental {

    private Long rentalId;
    private Long vehicleId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer billitaZoneId;
    private Integer price;
    private Integer finalPrice;
    private Integer insuranceId;
    private String paymentMethod;

}
