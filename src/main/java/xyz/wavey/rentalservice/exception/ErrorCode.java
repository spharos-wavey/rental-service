package xyz.wavey.rentalservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    NOT_FOUND_RENTAL("등록된 대여 정보가 존재하지 않습니다.", NOT_FOUND, 404)
    ;

    private final String message;
    private final HttpStatus httpStatus;
    private final Integer errorCode;
}
