package com.txt.eda.engine.config.couchbase;

import org.springframework.data.couchbase.core.convert.MappingCouchbaseConverter;
import org.springframework.data.couchbase.core.mapping.CouchbasePersistentEntity;
import org.springframework.data.couchbase.core.mapping.CouchbasePersistentProperty;
import org.springframework.data.mapping.context.MappingContext;

public class CustomMappingCouchbaseConverter extends MappingCouchbaseConverter {

    public CustomMappingCouchbaseConverter(final MappingContext< ? extends CouchbasePersistentEntity<?>,
            CouchbasePersistentProperty> mappingContext, final String typeKey){
        super(mappingContext,typeKey);
        this.typeMapper = new TypeBaseCouchbaseTypeMapper(typeKey);
    }
}
