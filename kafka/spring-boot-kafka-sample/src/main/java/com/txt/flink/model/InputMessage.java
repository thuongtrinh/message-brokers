package com.txt.flink.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDateTime;
import java.util.Objects;

@JsonSerialize
public class InputMessage {

    String sender;
    String recipient;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    LocalDateTime sentAt;

    String message;

    public InputMessage() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public InputMessage(String sender, String recipient, LocalDateTime sentAt, String message) {
        this.sender = sender;
        this.recipient = recipient;
        this.sentAt = sentAt;
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        InputMessage message1 = (InputMessage) o;
        return Objects.equals(sender, message1.sender) &&
                Objects.equals(recipient, message1.recipient) &&
                Objects.equals(sentAt, message1.sentAt) &&
                Objects.equals(message, message1.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, recipient, sentAt, message);
    }
}
