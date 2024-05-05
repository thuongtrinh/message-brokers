package com.txt.eda.common.dto.common;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;


@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class RequestWithBody<T> extends RequestDTO {

    private static final long serialVersionUID = 4832405318892260916L;

    private T body;

}
