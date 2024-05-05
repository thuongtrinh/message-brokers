package com.txt.eda.engine.controller;

import com.txt.eda.engine.entities.TransactionConfig;
import com.txt.eda.engine.repositories.TransactionConfigRespository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Configuration
@Slf4j
public class AppRunner implements ApplicationRunner {

    @Value("${syspro-version}")
    String version;

    @Autowired
    TransactionConfigRespository transactionConfigRespository;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("AppRunner - init TransactionConfig Couchbase " + version);
        if (version != null) {
            List<TransactionConfig> configList = new ArrayList<>();

            TransactionConfig config = new TransactionConfig();
            config.setVersion(version);
            config.setCreatedDate(new Date());
            config.setTransType("T01");
            config.setIsSkipFCheck(true);
            configList.add(config);

//            transactionConfigRespository.saveAll(configList);
        }
    }
}
