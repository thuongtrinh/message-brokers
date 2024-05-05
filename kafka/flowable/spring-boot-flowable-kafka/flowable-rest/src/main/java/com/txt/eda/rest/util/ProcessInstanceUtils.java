package com.txt.eda.rest.util;

import static com.txt.eda.rest.constant.Constants.*;

import com.txt.eda.rest.dto.processInstance.GenericProcessInstanceDTO;
import lombok.extern.slf4j.Slf4j;
import org.flowable.rest.service.api.engine.variable.RestVariable;

import java.util.List;

@Slf4j
public class ProcessInstanceUtils {


    public static void convertGenericProcessInstance(
            GenericProcessInstanceDTO genericProcessInstanceDTO, List<RestVariable> restVariables) {

        for (RestVariable element : restVariables) {
            if (element.getValue() != null) {
                if (TRANS_TYPE.equals(element.getName()))
                    genericProcessInstanceDTO.setTransType(element.getValue().toString());
                else if (CORRELATION_ID.equals(element.getName()))
                    genericProcessInstanceDTO.setCorrelationId(element.getValue().toString());
                else if (RESOURCE.equals(element.getName()))
                    genericProcessInstanceDTO.setResource(element.getValue().toString());
                else if (CATEGORY.equals(element.getName()))
                    genericProcessInstanceDTO.setCategory(element.getValue().toString());
                else if (PAYLOAD.equals(element.getName()))
                    genericProcessInstanceDTO.setPayload(element.getValue().toString());
                else if (EXCHANGE_ID.equals(element.getName()))
                    genericProcessInstanceDTO.setExchangeId(element.getValue().toString());
                else if (CREATE_DATE.equals(element.getName()))
                    genericProcessInstanceDTO.setCreatedDate(element.getValue().toString());
                else if (CREATE_BY.equals(element.getName()))
                    genericProcessInstanceDTO.setCreatedBy(element.getValue().toString());
            }
        }
    }

    public static void convertDetailGenericProcessInstance(GenericProcessInstanceDTO genericProcessInstanceDTO, List<RestVariable> restVariables) {
        convertGenericProcessInstance(genericProcessInstanceDTO, restVariables);
        genericProcessInstanceDTO.setPayload(getGenericProcessInstancePayload(genericProcessInstanceDTO, restVariables));
//        genericProcessInstanceDTO.setResponseDetail(getGenericProcessInstanceResponseDeatil(restVariables));
    }

    private static Object getGenericProcessInstancePayload(GenericProcessInstanceDTO genericProcessInstanceDTO, List<RestVariable> restVariables) {
        for (RestVariable element : restVariables) {
            if (element.getValue() != null) {
                if (PAYLOAD.equals(element.getName()) && element.getValue() != null) {
                    try {
                        String payload = element.getValue().toString();
                        return JsonHelper.jsonToObject(payload, Object.class);
                    } catch (Exception e) {
                        log.error("Cannot decrypt process instance payload", e);
                    }
                    return null;
                }
            }
        }
        return null;
    }
}
