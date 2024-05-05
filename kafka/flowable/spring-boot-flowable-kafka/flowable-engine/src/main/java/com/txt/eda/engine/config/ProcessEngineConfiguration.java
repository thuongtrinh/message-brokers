package com.txt.eda.engine.config;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcessEngineConfiguration {

    @Autowired
    private ProcessEngine processEngine;

//    @Autowired
//    private RuntimeService runtimeService;

    @Value("${syspro-tenant-id}")
    private String tenantId;

//    @Value("${cs-tenant-id}")
//    private String cstenantId;

    @Bean
    public void dmnDecisionService() {
//        repositoryService.createDeployment()
//                .tenantId(cstenantId)
//                .addClasspathResource("processes/tenant2/CS_Flowable_Process.bpmn20.xml")
//                .deploy();

        processEngine.getRepositoryService().createDeployment()
                .name("ProcessEngine CS")
                .tenantId(tenantId)
                .addClasspathResource("processes/CS_Flowable_Process.bpmn20.xml")
                .deploy();
    }

}
