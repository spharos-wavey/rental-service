package xyz.wavey.rentalservice.base.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    NOT_FOUND_RENTAL("등록된 대여 정보가 존재하지 않습니다.", NOT_FOUND, 404),

    NOT_FOUND_INSURANCE("등록된 보험 정보가 존재하지 않습니다.", NOT_FOUND, 404),

    CANNOT_RETURN("반납 처리 할 수 없습니다.", BAD_REQUEST, 400),

    FORBIDDEN_SMART_KEY("스마트키는 대여 시작 15분 전부터 이용할 수 있습니다.", FORBIDDEN, 403),

    ENDED_RENTAL_TIME("대여 가능 시각이 종료 되었습니다.", BAD_REQUEST, 400),

    BAD_REQUEST_PURCHASE_STATE("잘못된 결제 상태값입니다.", BAD_REQUEST, 400),

    BAD_REQUEST_RENTAL_DUPLICATED("해당 시간대에 이미 대여된 차량이 존재합니다.", BAD_REQUEST, 400),

    BAD_REQUEST_DATEFORMAT("날짜 형식이 잘못되었습니다.", BAD_REQUEST, 400),

    JSON_PROCESSING_EXCEPTION("JSON 처리 중 오류가 발생하였습니다.", INTERNAL_SERVER_ERROR, 500),

    ;

    private final String message;
    private final HttpStatus httpStatus;
    private final Integer errorCode;
}
