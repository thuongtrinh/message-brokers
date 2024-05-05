//package com.txt.eda.engine.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.flowable.common.engine.api.repository.EngineDeployment;
//import org.flowable.common.engine.api.repository.EngineResource;
//import org.flowable.common.engine.impl.EngineDeployer;
//import org.flowable.eventregistry.api.EventDeploymentBuilder;
//import org.flowable.eventregistry.api.EventRepositoryService;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.Map;
//
//@Configuration
//@Slf4j
//public class EventDeployer implements EngineDeployer {
//
//    @Override
//    public void deploy(EngineDeployment deployment, Map<String, Object> deploymentSettings) {
////        if (!deployment.isNew()) {
////            return;
////        }
//
//        log.debug("EventDeployer: processing deployment {}", deployment.getName());
//
//        EventDeploymentBuilder eventDeploymentBuilder = null;
//
//        Map<String, EngineResource> resources = deployment.getResources();
//        for (String resourceName : resources.keySet()) {
//            System.out.println(resourceName);
//            /*if (resourceName.endsWith(".event")) {
//                log.info("EventDeployer: processing resource {}", resourceName);
//                if (eventDeploymentBuilder == null) {
//                    EventRepositoryService eventRepositoryService = CommandContextUtil.getEventRepositoryService();
//                    eventDeploymentBuilder = eventRepositoryService.createDeployment().name(deployment.getName());
//                }
//
//                eventDeploymentBuilder.addEventDefinitionBytes(resourceName, resources.get(resourceName).getBytes());
//
//            } else if (resourceName.endsWith(".channel")) {
//                log.info("EventDeployer: processing resource {}", resourceName);
//                if (eventDeploymentBuilder == null) {
//                    EventRepositoryService eventRepositoryService = CommandContextUtil.getEventRepositoryService();
//                    eventDeploymentBuilder = eventRepositoryService.createDeployment().name(deployment.getName());
//                }
//
//                eventDeploymentBuilder.addChannelDefinitionBytes(resourceName, resources.get(resourceName).getBytes());
//            }*/
//        }
//
//        /*if (eventDeploymentBuilder != null) {
//            eventDeploymentBuilder.parentDeploymentId(deployment.getId());
//            if (deployment.getTenantId() != null && deployment.getTenantId().length() > 0) {
//                eventDeploymentBuilder.tenantId(deployment.getTenantId());
//            }
//
//            eventDeploymentBuilder.deploy();
//        }*/
//    }
//}
