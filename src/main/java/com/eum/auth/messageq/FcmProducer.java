package com.eum.auth.messageq;

import com.eum.auth.service.DTO.FcmTokenDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class FcmProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    public static String TOPIC = "eum-fcm-create";

    public FcmTokenDTO.FCMDTO send( FcmTokenDTO.FCMDTO fcmdto) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
//        주문 정보 Json 포맷
        try {
            jsonInString = mapper.writeValueAsString(fcmdto);
        } catch(JsonProcessingException ex) {
            ex.printStackTrace();
        }

        kafkaTemplate.send(TOPIC, jsonInString);
        log.info("create producer >> : " + fcmdto);

        return fcmdto;
    }
}
