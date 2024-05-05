package com.txt.eda.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SubmitDTO {

    private String idNumber;
    private String name;
    private String age;
    private String country;
    private String job;
    private String note;

}
