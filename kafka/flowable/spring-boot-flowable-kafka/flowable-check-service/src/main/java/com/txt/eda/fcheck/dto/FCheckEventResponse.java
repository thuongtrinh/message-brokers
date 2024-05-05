package com.txt.eda.fcheck.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FCheckEventResponse implements Serializable {

    private static final long serialVersionUID = -4825459922156463811L;

    private String eventKey;
    private String processInstanceId;
    private String fcheckResult;
}
