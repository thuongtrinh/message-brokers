package com.txt.eda.rest.dto.payload;

import lombok.Data;
import lombok.ToString;

import java.util.Date;


@Data
@ToString
public class PayLoadSaveInfoResponse {
    private String processInstanceId;
    private String createdBy;
    private Date createdDate;
    private Object payload;
}
