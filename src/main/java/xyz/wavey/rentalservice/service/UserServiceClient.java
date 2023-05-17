package xyz.wavey.rentalservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("USER-SERVICE")
public interface UserServiceClient {

    @GetMapping("/user/{uuid}")
    ResponseEntity<Object> getUserPk(@PathVariable("uuid") String uuid);

}
