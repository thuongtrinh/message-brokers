package com.txt.eda.rest.dto.processInstance;

import lombok.Data;

import java.util.List;

@Data
public class ProcessInstanceResponseDTO {

    List<ProcessInstanceDTO> data;

    long total;
    int start;
    String sort;
    String order;
    int size;
}
