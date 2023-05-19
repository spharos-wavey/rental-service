package xyz.wavey.rentalservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestAddInsurance {

    private String name;

    private Integer price;

    private String content;
}
