package com.txt.eda.engine.config;


import com.txt.eda.engine.config.autodeloy.DefaultAppAutoDeploymentWithTenantIdStrategy;

import lombok.extern.log4j.Log4j2;
import org.flowable.app.spring.SpringAppEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.flowable.spring.boot.app.AppEngineServicesAutoConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;


@AutoConfigureBefore({
        AppEngineServicesAutoConfiguration.class
})
@Configuration
@Log4j2
public class FlowableSpringAppEngineCustomConfig implements EngineConfigurationConfigurer<SpringAppEngineConfiguration> {

    @Value("${syspro-tenant-id}")
    private String tenantId;

    @Value("${syspro-deployment-mode}")
    private String deploymenMode;

    @Override
    public void configure(SpringAppEngineConfiguration config) {
        log.info("Start App Engine with tenantId {} and deployment mode {}", tenantId, deploymenMode);

        config.setDefaultTenantValue(tenantId);
        config.setDeploymentMode(deploymenMode);
        config.getDeploymentStrategies().add(new DefaultAppAutoDeploymentWithTenantIdStrategy(tenantId));
    }

}
