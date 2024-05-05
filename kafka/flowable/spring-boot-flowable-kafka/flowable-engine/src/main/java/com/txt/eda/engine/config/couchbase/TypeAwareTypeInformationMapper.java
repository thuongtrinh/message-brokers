package com.txt.eda.engine.config.couchbase;

import org.springframework.data.convert.SimpleTypeInformationMapper;
import org.springframework.data.mapping.Alias;
import org.springframework.data.util.TypeInformation;

public class TypeAwareTypeInformationMapper extends SimpleTypeInformationMapper {

    @Override
    public Alias createAliasFor(TypeInformation<?> type){
        DocumentType[] documentType = type.getType().getAnnotationsByType(DocumentType.class);
        if(documentType.length == 1){
            return Alias.of(documentType[0].value());
        }
        return super.createAliasFor(type);
    }
}
