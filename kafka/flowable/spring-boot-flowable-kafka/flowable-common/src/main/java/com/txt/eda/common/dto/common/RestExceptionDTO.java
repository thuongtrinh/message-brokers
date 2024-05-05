package com.txt.eda.common.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;


@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class RestExceptionDTO {

    protected String errorCode;
    protected String errorDescription;
    protected String errorTime;
    protected List<ErrorReasonDTO> errorList;

}
