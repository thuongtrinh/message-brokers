package com.txt.eda.rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.txt.eda.common.dto.SubmitDTO;
import com.txt.eda.common.dto.common.RequestWithBody;
import com.txt.eda.common.dto.common.ResponseWithBody;
import com.txt.eda.common.dto.common.TransactionCS;
import com.txt.eda.common.dto.common.TransactionSys;
import com.txt.eda.rest.constant.Constants;
import com.txt.eda.rest.service.KafkaServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@Tag(name = "Flowable Rest API Process", description = "Submit rest api to kafka")
public class FlowableRestController {

    @Autowired
    protected KafkaServices kafkaService;

    @Autowired
    private Environment env;

    @Autowired
    private ObjectMapper objectMapper;


    @PostMapping(value = "/submit-topic-trigger", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Submit Topic Trigger")
    public ResponseEntity<ResponseWithBody<?>> submit(
            @RequestHeader(value = "Authorization", required = false) String bearerToken,
            @RequestBody RequestWithBody<TransactionSys<SubmitDTO>> requestDTO) {

        ResponseEntity<ResponseWithBody<?>> response = null;
        try {
            String triggerTopic = env.getProperty("trigger.topic");

            TransactionSys<SubmitDTO> transactionSys = requestDTO.getBody();
            if(StringUtils.isBlank(transactionSys.getEventKey())) {
                transactionSys.setEventKey(Constants.KEY_EVENT_START);
            }
            kafkaService.sendDataToTopic(transactionSys, triggerTopic);

            log.info("SEND TO KAFKA: TOPIC {}, DATA {}", triggerTopic, objectMapper.writeValueAsString(transactionSys));

            response = new ResponseEntity<>(HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return response;
    }

    @PostMapping(value = "/cs-submit-topic-trigger", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "CS Submit Topic Trigger")
    public ResponseEntity<ResponseWithBody<?>> submitCS(
            @RequestHeader(value = "Authorization", required = false) String bearerToken,
            @RequestBody RequestWithBody<TransactionCS<SubmitDTO>> requestDTO) {

        ResponseEntity<ResponseWithBody<?>> response = null;
        try {
            String triggerTopic = env.getProperty("trigger.cs-topic");

            TransactionCS<SubmitDTO> transactionCS = requestDTO.getBody();
            if(StringUtils.isBlank(transactionCS.getEventKey())) {
                transactionCS.setEventKey(Constants.KEY_CS_EVENT_START);
            }
            kafkaService.sendDataToTopicCS(transactionCS, triggerTopic);

            log.info("SEND TO KAFKA: TOPIC {}, DATA {}", triggerTopic, objectMapper.writeValueAsString(transactionCS));

            response = new ResponseEntity<>(HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return response;
    }

}
