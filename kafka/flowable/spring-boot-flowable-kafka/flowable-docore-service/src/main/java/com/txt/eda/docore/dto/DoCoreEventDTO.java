package com.txt.eda.docore.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DoCoreEventDTO implements Serializable {

    private static final long serialVersionUID = -6188420844377318881L;
    private String processInstanceId;
    private String transType;
    private String resource;

}