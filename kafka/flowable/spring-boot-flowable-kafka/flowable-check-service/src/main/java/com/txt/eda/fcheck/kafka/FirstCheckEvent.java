package com.txt.eda.fcheck.kafka;

import com.txt.eda.fcheck.dto.FCheckEventDTO;
import com.txt.eda.fcheck.dto.FCheckEventResponse;
import com.txt.eda.fcheck.common.JsonHelper;
import com.txt.eda.fcheck.exception.BussinessException;
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
public class FirstCheckEvent {

    private static final Logger logger = LoggerFactory.getLogger(FirstCheckEvent.class);

    public static final String FIRST_CHECK_EVENT = "FIRST-CHECK-EVENT-SERVICE";

    @Autowired
    private JsonHelper jsonHelper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value(value = "${fcheck.topic.out}")
    private String topicOut;

    @Value(value = "${fcheck.topic.outKey}")
    private String topicOutKey;


    @KafkaListener(topics = "${fcheck.topic.in}")
    @Transactional
    public void consumMessage(String messageJson) throws Exception {

        logger.info("{} - process {} - Consum message: {}", FIRST_CHECK_EVENT, messageJson);

        String dataEventOut = getFirstCheckResponse(messageJson);

        logger.info("{} - Send data to next step: {}", FIRST_CHECK_EVENT, dataEventOut);

        kafkaTemplate.send(topicOut, dataEventOut);
    }

    private String getFirstCheckResponse(String messageJson) {
        FCheckEventDTO event = (FCheckEventDTO) jsonHelper.jsonToObject(messageJson, FCheckEventDTO.class);

        if (!StringUtils.hasText(event.getProcessInstanceId())) {
            throw new BussinessException("ProcessInstanceId not found");
        }

        FCheckEventResponse fCheckEventResponse = new FCheckEventResponse();
        fCheckEventResponse.setEventKey(topicOutKey);
        fCheckEventResponse.setProcessInstanceId(event.getProcessInstanceId());
        fCheckEventResponse.setFcheckResult("FcheckResult: 12345test");
        return jsonHelper.objectToJson(fCheckEventResponse);
    }

}
