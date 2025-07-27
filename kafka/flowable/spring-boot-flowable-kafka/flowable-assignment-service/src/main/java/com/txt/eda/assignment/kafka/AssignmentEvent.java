package com.txt.eda.assignment.kafka;

import com.txt.eda.assignment.common.Constants;
import com.txt.eda.assignment.dto.AssignmentEventDTO;
import com.txt.eda.assignment.dto.AssignmentEventResponse;
import com.txt.eda.assignment.common.JsonHelper;
import com.txt.eda.assignment.exception.BussinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class AssignmentEvent {

    private static final Logger logger = LoggerFactory.getLogger(AssignmentEvent.class);

    public static final String ASSIGNMENT_EVENT = "ASSIGNMENT-EVENT-SERVICE";

    @Autowired
    private JsonHelper jsonHelper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value(value = "${assignment.topic.out}")
    private String topicOut;

    @Value(value = "${assignment.topic.outKey}")
    private String topicOutKey;


    @KafkaListener(topics = "${assignment.topic.in}")
    @Transactional
    public void consumMessage(String messageJson) throws Exception {

        logger.info("{} - process {} - Consum message: {}", ASSIGNMENT_EVENT, messageJson);

        String dataEventOut = getFirstCheckResponse(messageJson);

        logger.info("{} - Send data to next step: {}", ASSIGNMENT_EVENT, dataEventOut);

        kafkaTemplate.send(topicOut, dataEventOut);
    }

    private String getFirstCheckResponse(String messageJson) {
        AssignmentEventDTO event = (AssignmentEventDTO) jsonHelper.jsonToObject(messageJson, AssignmentEventDTO.class);
        if (!StringUtils.hasText(event.getProcessInstanceId())) {
            throw new BussinessException("ProcessInstanceId not found");
        }

        AssignmentEventResponse assignmentEventResponse = new AssignmentEventResponse();
        assignmentEventResponse.setEventKey(topicOutKey);
        assignmentEventResponse.setProcessInstanceId(event.getProcessInstanceId());
        assignmentEventResponse.setAssignResult("AssignmentResult: 12345-abc");

        boolean isLogic = event.getIsLogicCheck() != null && event.getIsLogicCheck();
        if (Constants.TRANSACTION_LOGIC_CHECK.equals(event.getTransType()) && !isLogic) {
            assignmentEventResponse.setLogicCheck(Boolean.TRUE);
            assignmentEventResponse.setValueLogicCheck(getLogicCheck());
        } else {
            assignmentEventResponse.setLogicCheck(Boolean.FALSE);
        }

        return jsonHelper.objectToJson(assignmentEventResponse);
    }

    private String getLogicCheck() {
        Map<String, String> mapValueCheck = new HashMap<>();
        mapValueCheck.put("resource", "NEW_RESOURCE_TEST");
        String valueCheck = JsonHelper.objectToJson(mapValueCheck);
        System.out.println(valueCheck);
        return valueCheck;
    }

}
