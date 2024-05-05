package com.txt.eda.docore.kafka;

import com.txt.eda.docore.common.JsonHelper;
import com.txt.eda.docore.dto.DoCoreEventDTO;
import com.txt.eda.docore.dto.DoCoreEventResponse;
import com.txt.eda.docore.service.DoCoreService;
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
public class DoCoreEvent {

    private static final Logger logger = LoggerFactory.getLogger(DoCoreEvent.class);

    public static final String DO_CORE_EVENT_SERVICE = "DO-CORE-EVENT-SERVICE";

    @Autowired
    private JsonHelper jsonHelper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value(value = "${docore.topic.out}")
    private String topicOut;

    @Value(value = "${docore.topic.outKey}")
    private String topicOutKey;


    @Autowired
    private DoCoreService doCoreService;


    @KafkaListener(topics = "${docore.topic.in}")
    @Transactional
    public void consumMessage(String messageJson) throws Exception {

        logger.info("{} - process {} - Consum message: {}", DO_CORE_EVENT_SERVICE, messageJson);

        String dataEventOut = doCoreResponse(messageJson);

        logger.info("{} - Send data to next step: {}", DO_CORE_EVENT_SERVICE, dataEventOut);

        kafkaTemplate.send(topicOut, dataEventOut);
    }

    private String doCoreResponse(String messageJson) {
        DoCoreEventDTO event = (DoCoreEventDTO) jsonHelper.jsonToObject(messageJson, DoCoreEventDTO.class);

        if (!StringUtils.hasText(event.getProcessInstanceId())) {
            throw new RuntimeException("ProcessInstanceId not found");
        }

        doCoreService.doSomeThing();
        DoCoreEventResponse doCoreEventResponse = new DoCoreEventResponse();
        doCoreEventResponse.setEventKey(topicOutKey);
        doCoreEventResponse.setProcessInstanceId(event.getProcessInstanceId());
        doCoreEventResponse.setDoCoreResult(true);

        doCoreEventResponse.setAqAssignee("AQ111");
        doCoreEventResponse.setCandidateGroupAq("GroupAQ");

        if("T01".equals(event.getTransType())) {
            doCoreEventResponse.setIsSkipAq(false);
        } else {
            doCoreEventResponse.setIsSkipAq(true);
        }

        return jsonHelper.objectToJson(doCoreEventResponse);
    }

}
