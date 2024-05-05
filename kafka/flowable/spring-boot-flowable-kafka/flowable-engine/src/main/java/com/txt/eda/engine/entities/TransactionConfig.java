package com.txt.eda.engine.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;
import org.springframework.data.couchbase.core.mapping.id.IdPrefix;

import java.io.Serializable;
import java.util.Date;


@Document
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionConfig implements Serializable {

    private static final long serialVersionUID = -1179386716356222127L;

    @IdPrefix
    @JsonIgnore
    private String prefix = "transactionconfig";

    @Id
    @GeneratedValue(delimiter = "::", strategy = GenerationStrategy.UNIQUE)
    private String id;

    @Field
    private String transType;

    @Field
    private Boolean isSkipFCheck;

    @Field
    private Date createdDate;

    @Field
    private String createdBy = "system";

    @Field
    private Date updatedDate;

    @Field
    private String updatedBy;

    @Field
    private String version;

}
