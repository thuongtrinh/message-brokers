package com.txt.eda.engine.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActivityLogField {
    private String name;
    private String oldValue;
    private String newValue;
}
