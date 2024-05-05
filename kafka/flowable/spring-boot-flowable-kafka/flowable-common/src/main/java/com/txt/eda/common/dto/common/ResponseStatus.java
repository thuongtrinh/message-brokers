package com.txt.eda.common.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;


@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ResponseStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("code")
    private Integer code = null;

    @JsonProperty("message")
    private String message = null;

    @JsonProperty("errors")
    private List<Error> errors = null;

}
