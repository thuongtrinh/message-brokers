package com.txt.eda.verify.kafka;

import com.txt.eda.verify.common.JsonHelper;
import com.txt.eda.verify.dto.VerifyEventDTO;
import com.txt.eda.verify.dto.VerifyEventResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class VerifyEvent {

    private static final Logger logger = LoggerFactory.getLogger(VerifyEvent.class);

    public static final String VERIFY_EVENT_SERVICE = "VERIFY-EVENT-SERVICE";

    @Autowired
    private JsonHelper jsonHelper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value(value = "${verify.topic.out}")
    private String topicOut;

    @Value(value = "${verify.topic.outKey}")
    private String topicOutKey;


    @KafkaListener(topics = "${verify.topic.in}")
    @Transactional
    public void consumMessage(String messageJson) {

        logger.info("{} - process {} - Consum message: {}", VERIFY_EVENT_SERVICE, messageJson);

        String dataEventOut = verifyResponse(messageJson);

        logger.info("{} - Send data to next step: {}", VERIFY_EVENT_SERVICE, dataEventOut);

        kafkaTemplate.send(topicOut, dataEventOut);
    }

    private String verifyResponse(String messageJson) {
        VerifyEventDTO event = (VerifyEventDTO) jsonHelper.jsonToObject(messageJson, VerifyEventDTO.class);

        if (!StringUtils.hasText(event.getProcessInstanceId())) {
            throw new RuntimeException("ProcessInstanceId not found");
        }

        VerifyEventResponse verifyEventResponse = new VerifyEventResponse();
        verifyEventResponse.setEventKey(topicOutKey);
        verifyEventResponse.setProcessInstanceId(event.getProcessInstanceId());
        verifyEventResponse.setVerifyResult("Verify result 3");

        return jsonHelper.objectToJson(verifyEventResponse);
    }

}
