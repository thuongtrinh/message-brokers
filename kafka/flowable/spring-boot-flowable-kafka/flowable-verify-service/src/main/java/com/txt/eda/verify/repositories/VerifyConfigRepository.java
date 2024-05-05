package com.txt.eda.verify.repositories;

import com.txt.eda.verify.entities.VerifyConfig;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

public interface VerifyConfigRepository extends CouchbaseRepository<VerifyConfig, String> {
}
