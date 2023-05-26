package xyz.wavey.rentalservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PostMapping;
import xyz.wavey.rentalservice.base.KakaoOpenFeignConfig;
import xyz.wavey.rentalservice.vo.RequestKakaoPayApprove;
import xyz.wavey.rentalservice.vo.RequestKakaoPayReady;
import xyz.wavey.rentalservice.vo.ResponseKakaoPayReady;

@FeignClient(name = "kakaopay-service", url = "https://kapi.kakao.com", configuration = KakaoOpenFeignConfig.class)
public interface KakaoPayOpenFeign {

    @PostMapping(value = "/v1/payment/ready")
    ResponseKakaoPayReady kakaoPayReady(@SpringQueryMap RequestKakaoPayReady requestKakaoPayReady);

//    @PostMapping(value = "/v1/payment/approve")
//    KakaoPayApprovalResponse approval(@SpringQueryMap RequestKakaoPayApprove requestKakaoPayApprove);
//
//    @PostMapping(value = "/v1/payment/cancel")
//    KakaoPayRefundResponse refund(@SpringQueryMap KakaoPayRefundParams kakaoPayRefundParams);

}
