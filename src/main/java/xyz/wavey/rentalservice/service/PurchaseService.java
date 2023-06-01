package xyz.wavey.rentalservice.service;

import xyz.wavey.rentalservice.vo.request.RequestAddRental;
import xyz.wavey.rentalservice.vo.request.RequestKakaoPayApprove;
import xyz.wavey.rentalservice.vo.response.ResponseKakaoPayApprove;
import xyz.wavey.rentalservice.vo.response.ResponseKakaoPayReady;

public interface PurchaseService {

    ResponseKakaoPayReady kakaoPayReady(RequestAddRental requestAddRental);

    ResponseKakaoPayApprove kakaoPayApprove(RequestKakaoPayApprove requestKakaoPayApprove);

}
