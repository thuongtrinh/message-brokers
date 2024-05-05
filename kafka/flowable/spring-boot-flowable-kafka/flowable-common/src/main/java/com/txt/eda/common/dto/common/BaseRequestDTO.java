package com.txt.eda.common.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;


@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class BaseRequestDTO extends Header implements Serializable {

    private static final long serialVersionUID = 4255087675724682240L;

    private String systemName;

    private String exchangeId;

    private String functionUI;

    private String createdDate;

    private String createdBy;

}
