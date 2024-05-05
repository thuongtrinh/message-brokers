package com.txt.eda.assignment.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;
import org.springframework.data.couchbase.core.mapping.id.IdPrefix;

import javax.validation.constraints.NotNull;

@Document
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentConfig {

    @IdPrefix
    private String prefix = "assignmentconfig";

    @Id
    @GeneratedValue(delimiter = "::", strategy = GenerationStrategy.UNIQUE)
    private String id;

    @NotNull
    @Field
    private String source;
}
