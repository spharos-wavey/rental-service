package xyz.wavey.rentalservice.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestReturn {

    private String returnTime;

    private Integer finalPrice;

}
