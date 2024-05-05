package com.txt.eda.verify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VerifyEventDTO implements Serializable {

    private static final long serialVersionUID = -6188420844377318881L;
    private String processInstanceId;
    private String eventKey;
    private String transType;
    private String resource;
    private String category;

}