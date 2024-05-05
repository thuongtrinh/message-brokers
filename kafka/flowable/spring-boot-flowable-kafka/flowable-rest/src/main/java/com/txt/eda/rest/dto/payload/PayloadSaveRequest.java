package com.txt.eda.rest.dto.payload;

import lombok.Data;
import lombok.ToString;


@Data
@ToString
public class PayloadSaveRequest {

    private String user;
    private String processInstanceId;
    private Object payload;
}
