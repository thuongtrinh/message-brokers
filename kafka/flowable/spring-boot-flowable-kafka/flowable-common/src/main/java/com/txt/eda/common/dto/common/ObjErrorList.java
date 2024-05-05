package com.txt.eda.common.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;


@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class ObjErrorList {

    private String errorCode;
    private String errorDescription;
    private String errorFieldPrefix;
    private String errorField;

}
