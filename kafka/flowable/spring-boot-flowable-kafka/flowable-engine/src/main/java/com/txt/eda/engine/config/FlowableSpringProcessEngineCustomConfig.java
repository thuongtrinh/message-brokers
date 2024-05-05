package com.txt.eda.engine.config;


import com.txt.eda.engine.config.autodeloy.DefaultProcessAutoDeploymentWithTenantIdStrategy;
import lombok.extern.log4j.Log4j2;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.flowable.spring.boot.ProcessEngineServicesAutoConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;


@AutoConfigureBefore({
        ProcessEngineServicesAutoConfiguration.class
})
@Configuration
@Log4j2
public class FlowableSpringProcessEngineCustomConfig implements EngineConfigurationConfigurer<SpringProcessEngineConfiguration> {

    @Value("${syspro-tenant-id}")
    private String tenantId;

    @Value("${syspro-deployment-mode}")
    private String deploymenMode;

    @Override
    public void configure(SpringProcessEngineConfiguration config) {
        log.info("Start Process Engine with tenantId {} and deployment mode {}", tenantId, deploymenMode);

        config.setAsyncExecutorActivate(true);
        config.setAsyncExecutorNumberOfRetries(3);

        // Async history configuration
        config.setAsyncHistoryEnabled(true);
        config.setAsyncHistoryExecutorActivate(true);
        config.getDeploymentStrategies().add(new DefaultProcessAutoDeploymentWithTenantIdStrategy(tenantId));
        config.setDefaultTenantValue(tenantId);
        config.setDeploymentMode(deploymenMode);
    }


}
