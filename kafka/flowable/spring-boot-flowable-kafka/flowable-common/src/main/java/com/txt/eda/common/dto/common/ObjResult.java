package com.txt.eda.common.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ObjResult extends RequestDTO {

    private static final long serialVersionUID = -6639897983778995331L;

    private String status = "SUCCESS";

    @JsonProperty(value = "oErrorResult")
    private ObjError oErrorResult;

}
