package com.txt.eda.rest.service;


import com.txt.eda.rest.dto.processInstance.ProcessInstanceRequestDTO;
import com.txt.eda.rest.dto.processInstance.ProcessInstanceResponseDTO;

public interface ProcessInstanceService {

    ProcessInstanceResponseDTO queryProcessInstance(ProcessInstanceRequestDTO queryRequest);
}
