package com.txt.flink.schema;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.txt.flink.model.Backup;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class BackupSerializationSchema implements SerializationSchema<Backup> {

    private ObjectMapper objectMapper;
    private Logger logger = LoggerFactory.getLogger(BackupSerializationSchema.class);

    @Override
    public void open(InitializationContext context) throws Exception {
        SerializationSchema.super.open(context);
    }

    @Override
    public byte[] serialize(Backup backupMessage) {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.registerModule(new Jdk8Module());
        }
        try {
            String json = objectMapper.writeValueAsString(backupMessage);
            return json.getBytes(StandardCharsets.UTF_8);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            logger.error("Failed to parse JSON", e);
        }
        return new byte[0];
    }

}
