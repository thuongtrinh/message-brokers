package com.txt.eda.engine.config.couchbase;

import org.springframework.data.convert.DefaultTypeMapper;
import org.springframework.data.couchbase.core.convert.CouchbaseTypeMapper;
import org.springframework.data.couchbase.core.convert.DefaultCouchbaseTypeMapper;
import org.springframework.data.couchbase.core.mapping.CouchbaseDocument;
import org.springframework.data.mapping.Alias;
import org.springframework.data.util.TypeInformation;

import java.util.Collections;

public class TypeBaseCouchbaseTypeMapper extends DefaultTypeMapper<CouchbaseDocument> implements CouchbaseTypeMapper {
    private final String typeKey;

    public TypeBaseCouchbaseTypeMapper(final String typeKey){
        super(new DefaultCouchbaseTypeMapper.CouchbaseDocumentTypeAliasAccessor(typeKey),
                Collections.singletonList(new TypeAwareTypeInformationMapper()));
        this.typeKey = typeKey;
    }

    @Override
    public String getTypeKey(){
        return typeKey;
    }

    @Override
    public Alias getTypeAlias(TypeInformation<?> info) {
        return Alias.of(info.getType().getName());
    }
}
