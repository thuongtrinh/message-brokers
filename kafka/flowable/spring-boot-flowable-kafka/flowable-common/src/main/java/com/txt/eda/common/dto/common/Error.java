package com.txt.eda.common.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;


@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class Error implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("code")

    private String code = null;

    @JsonProperty("type")
    private String type = null;

    @JsonProperty("message")
    private String message = null;

    public Error code(String code) {
        this.code = code;
        return this;
    }

}
