package xyz.wavey.rentalservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.wavey.rentalservice.base.exception.ServiceException;
import xyz.wavey.rentalservice.messagequeue.KafkaProducer;
import xyz.wavey.rentalservice.repository.RentalRepo;
import xyz.wavey.rentalservice.vo.request.*;
import xyz.wavey.rentalservice.vo.response.ResponseAddRental;
import xyz.wavey.rentalservice.vo.response.ResponseKakaoPayApprove;
import xyz.wavey.rentalservice.vo.response.ResponseKakaoPayReady;
import xyz.wavey.rentalservice.vo.response.ResponsePurchase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static xyz.wavey.rentalservice.base.exception.ErrorCode.BAD_REQUEST_DATEFORMAT;
import static xyz.wavey.rentalservice.base.exception.ErrorCode.BAD_REQUEST_RENTAL_DUPLICATED;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseServiceImpl implements PurchaseService{

    private final RentalService rentalService;
    private final KakaoPayOpenFeign kakaoPayOpenFeign;
    private final RentalRepo rentalRepo;
    private final RedisTemplate<String, RequestAddRental> requestAddRentalRedisTemplate;
    private final KafkaProducer kafkaProducer;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Value("${kakao.pay.cid}")
    private String CID;
    @Value("${kakao.pay.approval_url}")
    private String APPROVAL_URL;
    @Value("${kakao.pay.cancel_url}")
    private String CANCEL_URL;
    @Value("${kakao.pay.fail_url}")
    private String FAIL_URL;

    @Transactional(readOnly = true)
    public ResponseKakaoPayReady kakaoPayReady(RequestPurchaseReady requestPurchaseReady) {

        LocalDateTime startDate;
        LocalDateTime endDate;
        try {
            startDate = LocalDateTime.parse(requestPurchaseReady.getStartDate(), dateTimeFormatter);
            endDate = LocalDateTime.parse(requestPurchaseReady.getEndDate(), dateTimeFormatter);
        } catch (Exception e) {
            throw new ServiceException(BAD_REQUEST_DATEFORMAT.getMessage(), BAD_REQUEST_DATEFORMAT.getHttpStatus());
        }

        if (!rentalRepo.checkUserCanBook(requestPurchaseReady.getUuid(), startDate, endDate).isEmpty()) {
            throw new ServiceException(
                    BAD_REQUEST_RENTAL_DUPLICATED.getMessage(),
                    BAD_REQUEST_RENTAL_DUPLICATED.getHttpStatus()
            );
        }

        RequestAddRental requestAddRental = RequestAddRental.builder()
                .uuid(requestPurchaseReady.getUuid())
                .vehicleId(requestPurchaseReady.getVehicleId())
                .carName(requestPurchaseReady.getCarName())
                .carBrandName(requestPurchaseReady.getCarBrandName())
                .startDate(requestPurchaseReady.getStartDate())
                .endDate(requestPurchaseReady.getEndDate())
                .startZone(requestPurchaseReady.getStartZone())
                .returnZone(requestPurchaseReady.getReturnZone())
                .price(requestPurchaseReady.getPrice())
                .insuranceId(requestPurchaseReady.getInsuranceId())
                .reward(requestPurchaseReady.getReward())
                .purchaseNumber(UUID.randomUUID().toString())
                .build();

        ResponseKakaoPayReady responseKakaoPayReady = kakaoPayOpenFeign.kakaoPayReady(KakaoPayReadyParameter.builder()
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

        responseKakaoPayReady.setPurchaseNumber(requestAddRental.getPurchaseNumber());

        ValueOperations<String, RequestAddRental> vop = requestAddRentalRedisTemplate.opsForValue();
        vop.set(requestAddRental.getPurchaseNumber(), requestAddRental);
        requestAddRentalRedisTemplate.expire(requestAddRental.getPurchaseNumber(), 15, TimeUnit.MINUTES);
        return responseKakaoPayReady;
    }

    @Override
    public ResponseAddRental kakaoPayApprove(RequestKakaoPayApprove requestKakaoPayApprove) {
        ValueOperations<String, RequestAddRental> vop = requestAddRentalRedisTemplate.opsForValue();
        RequestAddRental purchaseInfo = vop.get(requestKakaoPayApprove.getPurchaseNumber());

        kafkaProducer.send("user-reward", ResponsePurchase.builder()
                        .uuid(Objects.requireNonNull(purchaseInfo).getUuid())
                        .reward(purchaseInfo.getReward())
                .build());

        ResponseKakaoPayApprove responseKakaoPayApprove = kakaoPayOpenFeign.approval(KakaoPayApproveParameter.builder()
                .cid(CID)
                .tid(Objects.requireNonNull(purchaseInfo).getTid())
                .partner_order_id(purchaseInfo.getPurchaseNumber())
                .partner_user_id(purchaseInfo.getUuid())
                .pg_token(requestKakaoPayApprove.getPg_token())
                .build());

        String purchaseMethod;
        if (responseKakaoPayApprove.getCard_info() == null) {
            purchaseMethod = responseKakaoPayApprove.getPayment_method_type();
        } else {
            purchaseMethod = responseKakaoPayApprove.getCard_info().getIssuer_corp();
        }

        purchaseInfo.setPurchaseMethod(purchaseMethod);

        return rentalService.addRental(purchaseInfo);
    }
}
