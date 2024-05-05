package com.txt.eda.common.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;


@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class ObjError {

    private String objectIdLas;
    private String programLas;
    private String functionUI;
    private String errorCode;
    private String errorDescription;
    private String errorTime;
    private List<ObjErrorList> oErrorList;

    public ObjError(String errorCode, String errorDescription) {
        super();
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

}
