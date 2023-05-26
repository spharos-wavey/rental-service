package xyz.wavey.rentalservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import xyz.wavey.rentalservice.vo.RequestAddRental;
import xyz.wavey.rentalservice.vo.RequestKakaoPayReady;
import xyz.wavey.rentalservice.vo.ResponseKakaoPayReady;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService{

    private final KakaoPayOpenFeign kakaoPayOpenFeign;
    private final RedisTemplate<String, RequestAddRental> requestAddRentalRedisTemplate;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${kakao.pay.cid}")
    private String CID;
    @Value("${kakao.pay.approval_url}")
    private String APPROVAL_URL;
    @Value("${kakao.pay.cancel_url}")
    private String CANCEL_URL;
    @Value("${kakao.pay.fail_url}")
    private String FAIL_URL;

    public ResponseKakaoPayReady kakaoPayReady(RequestAddRental requestAddRental) {
        /*
        * 1. 클라이인터로 부터 구매와 관련된 정보를 받으면서 카카오 페이 준비하기가 시작됨
        * 2. set 을 이용하여 구매번호 생성
        * 3. https://developers.kakao.com/docs/latest/ko/kakaopay/single-payment#prepare-request 에 맞춰 api 호출
        * 4. 반환값의 tid 를 포함하여 구매 정보를 구매번호를 키값으로 사용하여 레디스에 저장
        * */
        requestAddRental.setPurchaseNumber(UUID.randomUUID().toString());

        ResponseKakaoPayReady responseKakaoPayReady = kakaoPayOpenFeign.kakaoPayReady(RequestKakaoPayReady.builder()
                .cid(CID)
                .partner_order_id(requestAddRental.getPurchaseNumber())
                .partner_user_id(requestAddRental.getUuid())
                .item_name(requestAddRental.getCarBrandName() + requestAddRental.getCarName())
                .quantity(1)
                .total_amount(requestAddRental.getPrice())
                .tax_free_amount((int) (requestAddRental.getPrice() * 0.05))
                .approval_url(APPROVAL_URL)
                .cancel_url(CANCEL_URL)
                .fail_url(FAIL_URL)
                .build());

        requestAddRental.setTid(responseKakaoPayReady.getTid());

        ValueOperations<String, RequestAddRental> vop = requestAddRentalRedisTemplate.opsForValue();
        vop.set(requestAddRental.getPurchaseNumber(), requestAddRental);
        requestAddRentalRedisTemplate.expire(requestAddRental.getPurchaseNumber(), 10, TimeUnit.MINUTES);
        return responseKakaoPayReady;
    }
}
