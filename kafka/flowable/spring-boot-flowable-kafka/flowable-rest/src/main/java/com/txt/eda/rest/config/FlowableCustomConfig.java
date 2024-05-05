package com.txt.eda.rest.config;

import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.flowable.spring.boot.ProcessEngineServicesAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;


@AutoConfigureBefore({
        ProcessEngineServicesAutoConfiguration.class
})
@Configuration
public class FlowableCustomConfig implements EngineConfigurationConfigurer<SpringProcessEngineConfiguration> {


    @Override
    public void configure(SpringProcessEngineConfiguration config) {
        //flowable.process.async-executor-activate=true
        config.setAsyncExecutorActivate(false);
        config.setAsyncHistoryEnabled(false);
        config.setAsyncExecutorAsyncJobAcquisitionEnabled(false);
        config.setAsyncExecutorTimerJobAcquisitionEnabled(false);
        config.setAsyncExecutorResetExpiredJobsEnabled(false);
    }

}
