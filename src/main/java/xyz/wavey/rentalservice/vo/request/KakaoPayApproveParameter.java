package xyz.wavey.rentalservice.vo.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class KakaoPayApproveParameter {

    private String cid;
    private String tid;
    private String partner_order_id;
    private String partner_user_id;
    private String pg_token;

}