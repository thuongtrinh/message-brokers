package com.txt.eda.engine.config;


import com.txt.eda.engine.config.autodeloy.DefaultDmnAutoDeploymentWithTenantIdStrategy;

import lombok.extern.log4j.Log4j2;
import org.flowable.dmn.spring.SpringDmnEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.flowable.spring.boot.dmn.DmnEngineServicesAutoConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;


@AutoConfigureBefore({
        DmnEngineServicesAutoConfiguration.class
})
@Configuration
@Log4j2
public class FlowableSpringDmnEngineCustomConfig implements EngineConfigurationConfigurer<SpringDmnEngineConfiguration> {

    @Value("${syspro-tenant-id}")
    private String tenantId;

    @Value("${syspro-deployment-mode}")
    private String deploymenMode;

    @Override
    public void configure(SpringDmnEngineConfiguration config) {
        log.info("Start DMN Engine with tenantId {} and deployment mode {}", tenantId, deploymenMode);

        config.setDefaultTenantValue(tenantId);
        config.setDeploymentMode(deploymenMode);
        config.getDeploymentStrategies().add(new DefaultDmnAutoDeploymentWithTenantIdStrategy(tenantId));
    }

}
