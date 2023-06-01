package xyz.wavey.rentalservice.service;

import xyz.wavey.rentalservice.vo.request.RequestKakaoPayApprove;
import xyz.wavey.rentalservice.vo.request.RequestPurchaseReady;
import xyz.wavey.rentalservice.vo.response.ResponseAddRental;
import xyz.wavey.rentalservice.vo.response.ResponseKakaoPayReady;

public interface PurchaseService {

    ResponseKakaoPayReady kakaoPayReady(RequestPurchaseReady requestPurchaseReady);

    ResponseAddRental kakaoPayApprove(RequestKakaoPayApprove requestKakaoPayApprove);

}
