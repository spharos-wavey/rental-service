package xyz.wavey.rentalservice.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestKakaoPayApprove {

    private String pg_token;
    private String purchaseNumber;

}
