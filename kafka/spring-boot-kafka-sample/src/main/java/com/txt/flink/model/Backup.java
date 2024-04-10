package com.txt.flink.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Backup {

    @JsonProperty("inputMessages")
    List<InputMessage> inputMessages;

    @JsonProperty("backupTimestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    LocalDateTime backupTimestamp;

    @JsonProperty("uuid")
    UUID uuid;

    public Backup(List<InputMessage> inputMessages, LocalDateTime backupTimestamp) {
        this.inputMessages = inputMessages;
        this.backupTimestamp = backupTimestamp;
        this.uuid = UUID.randomUUID();
    }

    public List<InputMessage> getInputMessages() {
        return inputMessages;
    }
}
