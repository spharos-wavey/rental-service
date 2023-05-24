package xyz.wavey.rentalservice.model;

import jakarta.persistence.*;
import lombok.*;
import xyz.wavey.rentalservice.base.BaseTimeEntity;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rental extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String uuid;

    @Column(nullable = false)
    private Long vehicleId;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    private Integer extendedTime;

    @Column(nullable = false)
    private Integer startZone;

    @Column(nullable = false)
    private Integer returnZone;

    private String paymentMethod;

    private Integer price;

    private Integer finalPrice;

    @Column(columnDefinition = "boolean default false")
    private Boolean keyAuth;

    private LocalDateTime reqReturnTime;

    @Enumerated(EnumType.STRING)
    private PurchaseState purchaseState;

    private Integer insuranceId;

}
