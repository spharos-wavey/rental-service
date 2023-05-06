package xyz.wavey.rentalservice.vo;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestAddRental {

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long vehicleId;

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private Date endDate;

    @Column(nullable = false)
    private Integer startZone;

    @Column(nullable = false)
    private Integer returnZone;

    private String payment;

    private Integer price;

    @Column(nullable = false)
    private Integer insuranceId;

}
