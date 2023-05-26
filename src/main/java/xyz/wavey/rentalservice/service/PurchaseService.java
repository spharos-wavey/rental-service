package xyz.wavey.rentalservice.service;

import xyz.wavey.rentalservice.vo.RequestAddRental;
import xyz.wavey.rentalservice.vo.ResponseKakaoPayReady;

public interface PurchaseService {

    ResponseKakaoPayReady kakaoPayReady(RequestAddRental requestAddRental);
}
