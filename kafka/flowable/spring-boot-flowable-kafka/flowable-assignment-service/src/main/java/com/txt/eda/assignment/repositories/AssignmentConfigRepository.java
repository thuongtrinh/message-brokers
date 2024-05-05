package com.txt.eda.assignment.repositories;


import com.txt.eda.assignment.entities.AssignmentConfig;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

public interface AssignmentConfigRepository extends CouchbaseRepository<AssignmentConfig, String> {
}
