package xyz.wavey.rentalservice.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import xyz.wavey.rentalservice.base.exception.ServiceException;
import xyz.wavey.rentalservice.vo.ResponsePurchase;

import static xyz.wavey.rentalservice.base.exception.ErrorCode.JSON_PROCESSING_EXCEPTION;

@Service
public class KafkaProducer {

    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public ResponsePurchase send(String topic, ResponsePurchase responsePurchase) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "";
        try {
            jsonString = mapper.writeValueAsString(responsePurchase);
        } catch (JsonProcessingException e) {
            throw new ServiceException(JSON_PROCESSING_EXCEPTION.getMessage(),
                    JSON_PROCESSING_EXCEPTION.getHttpStatus());
        }
        kafkaTemplate.send(topic, jsonString);
        return responsePurchase;
    }


}
