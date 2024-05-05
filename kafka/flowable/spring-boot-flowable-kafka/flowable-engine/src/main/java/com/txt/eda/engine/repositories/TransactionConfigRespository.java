package com.txt.eda.engine.repositories;

import com.txt.eda.engine.entities.TransactionConfig;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TransactionConfigRespository extends CouchbaseRepository<TransactionConfig, Integer> {

    TransactionConfig findFirstByVersionAndTransType(String version, String transactionType);

    List<TransactionConfig> findAllByVersionAndTransType(String version, String transactionType);

    List<TransactionConfig> findAllByVersion(String version);

}
