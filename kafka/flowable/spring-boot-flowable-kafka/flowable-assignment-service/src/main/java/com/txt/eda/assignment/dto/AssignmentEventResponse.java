package com.txt.eda.assignment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssignmentEventResponse implements Serializable {

    private static final long serialVersionUID = -4825459922156463811L;

    private String eventKey;
    private String processInstanceId;
    private String assignResult;
    private String valueLogicCheck;

    @JsonProperty("is_logic_check")
    private boolean isLogicCheck;
}
