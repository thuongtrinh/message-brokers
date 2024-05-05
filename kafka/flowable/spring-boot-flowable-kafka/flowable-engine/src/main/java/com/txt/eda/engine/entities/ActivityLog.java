package com.txt.eda.engine.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;
import org.springframework.data.couchbase.core.mapping.id.IdPrefix;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Document
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLog {

    @Builder
    public ActivityLog(String processInstanceId, String user, String workFlowStage, List<ActivityLogField> fields, String version) {
        this.processInstanceId = processInstanceId;
        this.user = user;
        this.createdDate = LocalDateTime.now();
        this.workFlowStage = workFlowStage;
        this.fields = fields;
        this.version = version;
    }

    @IdPrefix
    private String prefix = "activitylog";

    @Id
    @GeneratedValue(delimiter = "::", strategy = GenerationStrategy.UNIQUE)
    private String id;

    @Field
    @NotNull
    private String processInstanceId;

    @Field
    @NotNull
    private String user;

    @Field
    @NotNull
    private LocalDateTime createdDate;

    @Field
    private String workFlowStage;

    @Field
    private List<ActivityLogField> fields;

    @Field
    private String version;
}

