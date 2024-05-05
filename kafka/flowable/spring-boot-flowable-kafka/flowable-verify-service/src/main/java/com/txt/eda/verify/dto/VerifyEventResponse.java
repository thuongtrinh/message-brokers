package com.txt.eda.verify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VerifyEventResponse implements Serializable {

    private static final long serialVersionUID = -6720574041696675006L;
    private String eventKey;
    private String processInstanceId;
    private String verifyResult;
}
