package xyz.wavey.rentalservice.insurance.vo;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestAddInsurance {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    private String content;
}
