package com.txt.eda.engine.config;

import com.txt.eda.engine.config.autodeloy.DefaultFormAutoDeploymentWithTenantIdStrategy;
import lombok.extern.log4j.Log4j2;
import org.flowable.form.spring.SpringFormEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.flowable.spring.boot.form.FormEngineServicesAutoConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;


@AutoConfigureBefore({
        FormEngineServicesAutoConfiguration.class
})
@Configuration
@Log4j2
public class FlowableSpringFormEngineCustomConfig implements EngineConfigurationConfigurer<SpringFormEngineConfiguration> {

    @Value("${syspro-tenant-id}")
    private String tenantId;

    @Value("${syspro-deployment-mode}")
    private String deploymenMode;

    @Override
    public void configure(SpringFormEngineConfiguration config) {
        log.info("Start FORM Engine with tenantId {} and deployment mode {}", tenantId, deploymenMode);

        config.setDefaultTenantValue(tenantId);
        config.setDeploymentMode(deploymenMode);
        config.getDeploymentStrategies().add(new DefaultFormAutoDeploymentWithTenantIdStrategy(tenantId));
    }

}
