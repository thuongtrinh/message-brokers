package com.txt.eda.engine.config;


import com.txt.eda.engine.config.autodeloy.DefaultCmmnAutoDeploymentWithTenantIdStrategy;

import lombok.extern.log4j.Log4j2;
import org.flowable.cmmn.spring.SpringCmmnEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.flowable.spring.boot.cmmn.CmmnEngineServicesAutoConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;


@AutoConfigureBefore({
        CmmnEngineServicesAutoConfiguration.class
})
@Configuration
@Log4j2
public class FlowableSpringCmmnEngineCustomConfig implements EngineConfigurationConfigurer<SpringCmmnEngineConfiguration> {

    @Value("${syspro-tenant-id}")
    private String tenantId;

    @Value("${syspro-deployment-mode}")
    private String deploymenMode;

    @Override
    public void configure(SpringCmmnEngineConfiguration config) {
        log.info("Start CMMN Engine with tenantId {} and deployment mode {}", tenantId, deploymenMode);

        config.setDefaultTenantValue(tenantId);
        config.setDeploymentMode(deploymenMode);
        config.getDeploymentStrategies().add(new DefaultCmmnAutoDeploymentWithTenantIdStrategy(tenantId));
    }

}
