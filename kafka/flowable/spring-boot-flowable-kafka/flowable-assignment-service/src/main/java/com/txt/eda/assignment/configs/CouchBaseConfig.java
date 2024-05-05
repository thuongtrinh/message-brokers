package com.txt.eda.assignment.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;


@Configuration
public class CouchBaseConfig extends AbstractCouchbaseConfiguration {

    private final String bucketName;
    private final String connectionString;
    private final String userName;
    private final String password;
    private final Boolean autoIndex;

    public CouchBaseConfig(@Value("${spring.couchbase.bucketName}") String bucketName,
                           @Value("${spring.couchbase.connectionString}") String connectionString,
                           @Value("${spring.couchbase.username}") String userName,
                           @Value("${spring.couchbase.password}") String password,
                           @Value("${spring.couchbase.autoIndex}") Boolean autoIndex) {
        this.bucketName = bucketName;
        this.connectionString = connectionString;
        this.userName = userName;
        this.password = password;
        this.autoIndex = autoIndex;
    }

    @Override
    public String getConnectionString() {
        return "couchbase://" + connectionString;
    }

    @Override
    public String getBucketName() {
        return bucketName;
    }

    @Override
    protected boolean autoIndexCreation() {
        return autoIndex;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
