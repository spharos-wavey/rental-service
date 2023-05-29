package xyz.wavey.rentalservice.vo;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Builder
@Getter
public class ResponseReturnVehicle {

    private HttpStatus httpStatus;

    private String message;

}