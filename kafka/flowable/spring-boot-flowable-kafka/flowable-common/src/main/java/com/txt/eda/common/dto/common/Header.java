package com.txt.eda.common.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.threeten.bp.OffsetDateTime;

import java.io.Serializable;
import java.util.UUID;


@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class Header implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("msgId")
    private UUID msgId = null;

    @JsonProperty("timestamp")
    private OffsetDateTime timestamp = null;

    @JsonProperty("resource")
    private String resource = null;

    @JsonProperty("correlationId")
    private String correlationId = null;

    @JsonProperty("access_token")
    private String accessToken = null;

    @JsonProperty("refresh_token")
    private String refreshToken = null;

    public Header msgId(UUID msgId) {
        this.msgId = msgId;
        return this;
    }

}
