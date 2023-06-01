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
import xyz.wavey.rentalservice.vo.request.RequestAddRental;
import xyz.wavey.rentalservice.vo.request.RequestKakaoPayApprove;

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

    @PostMapping("/kakao/approve")
    public ResponseEntity<Object> kakaoPayApprove(@RequestBody RequestKakaoPayApprove requestKakaoPayApprove) {
        return ResponseEntity.status(HttpStatus.OK).body(purchaseService.kakaoPayApprove(requestKakaoPayApprove));
    }
}
