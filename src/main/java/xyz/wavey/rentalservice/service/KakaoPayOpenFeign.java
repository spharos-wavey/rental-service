package xyz.wavey.rentalservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PostMapping;
import xyz.wavey.rentalservice.base.KakaoOpenFeignConfig;
import xyz.wavey.rentalservice.vo.request.KakaoPayApproveParameter;
import xyz.wavey.rentalservice.vo.request.KakaoPayReadyParameter;
import xyz.wavey.rentalservice.vo.response.ResponseKakaoPayApprove;
import xyz.wavey.rentalservice.vo.response.ResponseKakaoPayReady;

@FeignClient(name = "kakaopay-service", url = "https://kapi.kakao.com", configuration = KakaoOpenFeignConfig.class)
public interface KakaoPayOpenFeign {

    @PostMapping(value = "/v1/payment/ready")
    ResponseKakaoPayReady kakaoPayReady(@SpringQueryMap KakaoPayReadyParameter requestKakaoPayReady);

    @PostMapping(value = "/v1/payment/approve")
    ResponseKakaoPayApprove approval(@SpringQueryMap KakaoPayApproveParameter kakaoPayApproveParameter);

//    @PostMapping(value = "/v1/payment/cancel")
//    KakaoPayRefundResponse refund(@SpringQueryMap KakaoPayRefundParams kakaoPayRefundParams);

}
