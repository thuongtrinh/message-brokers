package com.txt.eda.rest.service;

import com.txt.eda.rest.dto.processInstance.HistoricProcessInstanceDTO;
import org.springframework.stereotype.Service;

@Service
public interface HistoricProcessInstanceService {

    HistoricProcessInstanceDTO getHistoricProcessInstance(String processInstanceId);

}
