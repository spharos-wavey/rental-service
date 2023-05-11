package xyz.wavey.rentalservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import xyz.wavey.rentalservice.base.BaseTimeEntity;

import java.time.LocalDateTime;
import java.util.Date;

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
    private Long userId;

    @Column(nullable = false)
    private Long vehicleId;

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private Date endDate;

    private Date extendedTime;

    @Column(nullable = false)
    private Integer startZone;

    @Column(nullable = false)
    private Integer returnZone;

    private String payment;

    private Integer price;

    @Column(columnDefinition = "boolean default false")
    private Boolean keyAuth;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reqReturnTime;

    @ManyToOne
    private Insurance insurance;

}
