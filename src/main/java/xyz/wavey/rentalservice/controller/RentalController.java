package xyz.wavey.rentalservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.wavey.rentalservice.messagequeue.KafkaProducer;
import xyz.wavey.rentalservice.service.RentalService;
import xyz.wavey.rentalservice.vo.request.RequestAddRental;
import xyz.wavey.rentalservice.vo.request.RequestReturn;
import xyz.wavey.rentalservice.vo.response.ResponseGetAllRental;
import xyz.wavey.rentalservice.vo.response.ResponsePurchase;
import xyz.wavey.rentalservice.vo.response.ResponseReturnVehicle;

import java.util.List;

@RestController
@RequestMapping("/rental")
@RequiredArgsConstructor
@Slf4j
public class RentalController {

    private final RentalService rentalService;
    private final KafkaProducer kafkaProducer;


    @PostMapping()
    public ResponseEntity<Object> addRental(@RequestBody RequestAddRental requestAddRental) {
        ResponsePurchase responsePurchase= rentalService.addRental(requestAddRental);
        kafkaProducer.send("user-reward", responsePurchase);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{purchaseState}")
    public ResponseEntity<List<ResponseGetAllRental>> getAllRental(
            @RequestHeader("uid") String uuid,
            @PathVariable("purchaseState") String purchaseState){

        List<ResponseGetAllRental> response = rentalService.getAllRental(uuid, purchaseState);

        if (response.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    @GetMapping()
    public ResponseEntity<Object> getRental(
            @RequestHeader("uid") String uuid,
            @RequestParam("id") Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(rentalService.getRental(uuid, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRental(
            @RequestHeader("uid") String uuid,
            @PathVariable Long id) {
        return ResponseEntity.status(rentalService.deleteRental(uuid, id)).build();
    }

    @GetMapping("/cancel/{id}")
    public ResponseEntity<Object> cancelRental(
            @RequestHeader("uid") String uuid,
            @PathVariable Long id) {
        return ResponseEntity.status(rentalService.cancelRental(uuid, id)).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> returnVehicle(
            @RequestHeader("uid") String uuid,
            @PathVariable Long id,
            @RequestBody RequestReturn requestReturn) {
        ResponseReturnVehicle returnVehicle = rentalService.returnVehicle(uuid, id, requestReturn);
        return ResponseEntity
                .status(returnVehicle.getHttpStatus())
                .body(returnVehicle.getMessage());
    }

    @PatchMapping("/openKey/{id}")
    public ResponseEntity<Object> openSmartKey(
            @RequestHeader("uid") String uuid,
            @PathVariable Long id){
        return rentalService.openSmartKey(uuid, id);
    }

    @GetMapping("/can-rental")
    public ResponseEntity<Object> checkCanRent(@RequestHeader("uid") String uuid) {
        return ResponseEntity.status(HttpStatus.OK).body(rentalService.checkCanRent(uuid));
    }

}
