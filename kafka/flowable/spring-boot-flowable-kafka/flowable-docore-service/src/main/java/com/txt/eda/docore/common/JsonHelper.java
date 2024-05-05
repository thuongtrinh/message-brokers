package com.txt.eda.docore.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class JsonHelper {

    private static Logger log = LoggerFactory.getLogger(JsonHelper.class);

    private static final Map<String, ObjectMapper> MAP_OBJECT_MAPPER = new HashMap<String, ObjectMapper>();

    public ObjectMapper getObjectMapper(String clazzName) {
        ObjectMapper om = MAP_OBJECT_MAPPER.get(clazzName);
        if (om == null) {
            ObjectMapper mapper = new ObjectMapper();
            MAP_OBJECT_MAPPER.put(clazzName, mapper);
            return mapper;
        }
        return om;
    }

    public String objectToJson(Object obj) {
        if (obj == null) {
            return null;
        }
        ObjectMapper mapper = getObjectMapper(obj.getClass().getName());
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public Object jsonToObject(String json, Class<?> clazz) {
        if (json == null) {
            return null;
        }
        ObjectMapper mapper = getObjectMapper(clazz.getClass().getName());
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

}
