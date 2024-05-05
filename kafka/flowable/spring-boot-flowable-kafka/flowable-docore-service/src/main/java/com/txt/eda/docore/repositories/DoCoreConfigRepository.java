package com.txt.eda.docore.repositories;

import com.txt.eda.docore.entities.DoCoreConfig;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

public interface DoCoreConfigRepository extends CouchbaseRepository<DoCoreConfig, String> {
}
