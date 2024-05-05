package com.txt.eda.rest.service;


import com.txt.eda.rest.dto.payload.PayLoadSaveInfoResponse;
import com.txt.eda.rest.dto.payload.PayloadSaveRequest;

public interface PayloadInfoService {

    PayLoadSaveInfoResponse getPayLoadSaveInfo(String processInstanceId);

    PayLoadSaveInfoResponse savePayload(PayloadSaveRequest request, String taskId);

}
