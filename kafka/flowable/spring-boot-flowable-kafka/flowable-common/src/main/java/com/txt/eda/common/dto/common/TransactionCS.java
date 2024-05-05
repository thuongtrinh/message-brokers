package com.txt.eda.common.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionCS<T> {

    private String exchangeId;
    private String correlationId;
    private String eventKey = "triggerCSEvent";
    private String resource;
    private String transType;
    private String category;
    private T payload;
    private String createdDate;
    private String createdBy;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
