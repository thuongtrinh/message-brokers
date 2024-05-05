package com.txt.eda.common.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class RequestDTO extends BaseRequestDTO {

}
