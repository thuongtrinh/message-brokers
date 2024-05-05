package com.txt.eda.rest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.couchbase.CouchbaseClientFactory;
import org.springframework.data.couchbase.SimpleCouchbaseClientFactory;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.config.BeanNames;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.data.couchbase.core.convert.MappingCouchbaseConverter;
import org.springframework.data.couchbase.core.mapping.CouchbaseMappingContext;
import org.springframework.data.couchbase.repository.config.RepositoryOperationsMapping;

@Configuration
public class CouchBaseConfiguration extends AbstractCouchbaseConfiguration {

    private final String bucketName;
//    private final String bucketNameFilenet;
    private final String connectionString;
    private final String userName;
    private final String password;
    private final Boolean autoIndex;

    private final MappingCouchbaseConverter mappingCouchbaseConverter;

    @Autowired
    public CouchBaseConfiguration(@Value("${spring.couchbase.bucketName}") String bucketName,
//                                  @Value("${spring.couchbase.bucketName-filenet}") String bucketNameFilenet,
                                  @Value("${spring.couchbase.connectionString}") String connectionString,
                                  @Value("${spring.couchbase.username}") String userName,
                                  @Value("${spring.couchbase.password}") String password,
                                  @Value("${spring.couchbase.autoIndex}") Boolean autoIndex,
                                  @Lazy MappingCouchbaseConverter mappingCouchbaseConverter) {
        this.bucketName = bucketName;
//        this.bucketNameFilenet = bucketNameFilenet;
        this.connectionString = connectionString;
        this.userName = userName;
        this.password = password;
        this.autoIndex = autoIndex;
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

    @Override
    @Bean
    public CouchbaseMappingContext couchbaseMappingContext(@Qualifier(BeanNames.COUCHBASE_CUSTOM_CONVERSIONS) CustomConversions customConversions) throws Exception {
        CouchbaseMappingContext mappingContext = new CouchbaseMappingContext();
        mappingContext.setInitialEntitySet(getInitialEntitySet());
        mappingContext.setSimpleTypeHolder(customConversions.getSimpleTypeHolder());
        mappingContext.setFieldNamingStrategy(fieldNamingStrategy());
        mappingContext.setAutoIndexCreation(autoIndexCreation());

        return mappingContext;
    }

    @Override
    protected void configureRepositoryOperationsMapping(RepositoryOperationsMapping mapping) {
//        mapping.mapEntity(MetadataLog.class, secondBucketTransactionTemplate());
    }

//    @Bean
//    public CouchbaseTemplate secondBucketTransactionTemplate() {
//        return customCouchbaseTemplate(customCouchbaseClientFactory(bucketNameFilenet));
//    }

    public CouchbaseTemplate customCouchbaseTemplate(CouchbaseClientFactory couchbaseClientFactory) {
        return new CouchbaseTemplate(couchbaseClientFactory, mappingCouchbaseConverter);
    }

    public CouchbaseClientFactory customCouchbaseClientFactory(String bucketName) {
        return new SimpleCouchbaseClientFactory(getConnectionString(), authenticator(), bucketName);
    }

}
