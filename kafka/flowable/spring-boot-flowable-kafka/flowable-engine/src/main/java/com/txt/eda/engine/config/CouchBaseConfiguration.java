package com.txt.eda.engine.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.core.convert.MappingCouchbaseConverter;


@Configuration
public class CouchBaseConfiguration extends AbstractCouchbaseConfiguration {

    @Value("${spring.couchbase.bucketName}")
    private String bucketName;
    @Value("${spring.couchbase.connectionString}")
    private String connectionString;
    @Value("${spring.couchbase.username}")
    private String userName;
    @Value("${spring.couchbase.password}")
    private String password;
    @Value("${spring.couchbase.autoIndex}")
    private Boolean autoIndex;

    private final MappingCouchbaseConverter mappingCouchbaseConverter;

    @Autowired
    public CouchBaseConfiguration(@Lazy MappingCouchbaseConverter mappingCouchbaseConverter) {
        this.mappingCouchbaseConverter = mappingCouchbaseConverter;
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
