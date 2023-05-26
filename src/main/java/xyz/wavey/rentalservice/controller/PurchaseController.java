package xyz.wavey.rentalservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.wavey.rentalservice.service.PurchaseService;
import xyz.wavey.rentalservice.vo.RequestAddRental;

@RestController
@RequiredArgsConstructor
@RequestMapping("/purchase")
@Slf4j
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping("/kakao/ready")
    public ResponseEntity<Object> kakaoPayReady(@RequestBody RequestAddRental requestAddRental) {
        return ResponseEntity.status(HttpStatus.OK).body(purchaseService.kakaoPayReady(requestAddRental));
    }

    //todo 결제 승인 요청하기
    /*
    * pg 토큰 받아서 처리
    * Request vo 를 생성 or header 로 값 받기
    * */
}
