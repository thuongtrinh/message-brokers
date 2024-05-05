package com.txt.eda.fcheck.repositories;


import com.txt.eda.fcheck.entities.FCheckConfig;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

public interface FCheckConfigRepository extends CouchbaseRepository<FCheckConfig, String> {
}
