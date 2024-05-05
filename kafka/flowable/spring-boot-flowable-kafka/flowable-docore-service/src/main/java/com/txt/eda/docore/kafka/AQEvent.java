package com.txt.eda.docore.kafka;

import com.txt.eda.docore.common.JsonHelper;
import com.txt.eda.docore.dto.AQResponse;
import com.txt.eda.docore.dto.AQRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("aqEvent")
@RequiredArgsConstructor
@Slf4j
public class AQEvent {

    private final JsonHelper jsonHelper;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value(value = "${docore.topic.outKey}")
    private String topicOutKey;

    @Value(value = "${aq.topic.out}")
    private String topicOut;


    @KafkaListener(topics = "${docore.topic.out}")
    @Transactional
    public void consumMessage(String messageJson) {
        log.info("Consum message: {}", messageJson);

        AQRequestDTO event = (AQRequestDTO) jsonHelper.jsonToObject(messageJson, AQRequestDTO.class);
        if (event == null || event.getAqAssignee() == null || event.getCandidateGroupAq() == null || event.getProcessInstanceId() == null) {
            throw new RuntimeException("Event is null");
        }

        AQResponse response = new AQResponse();
        response.setEventKey(topicOutKey);
        response.setProcessInstanceId(event.getProcessInstanceId());
        response.setCandidateGroupAq(event.getCandidateGroupAq());
        response.setAqAssignee(event.getAqAssignee());
        response.setIsSkipAq(Boolean.TRUE);
        String data = jsonHelper.objectToJson(response);

        this.kafkaTemplate.send(topicOut, data);
    }
}
