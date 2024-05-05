package com.txt.eda.engine.config.autodeloy;

import org.apache.commons.lang3.StringUtils;
import org.flowable.app.spring.autodeployment.DefaultAutoDeploymentStrategy;
import org.flowable.common.spring.CommonAutoDeploymentProperties;
import org.flowable.eventregistry.api.EventDeploymentBuilder;
import org.flowable.eventregistry.api.EventRepositoryService;
import org.flowable.eventregistry.impl.EventRegistryEngine;
import org.flowable.eventregistry.spring.autodeployment.AbstractEventAutoDeploymentStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

public class DefaultEventRegistryAutoDeploymentWithTenantIdStrategy extends AbstractEventAutoDeploymentStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAutoDeploymentStrategy.class);

    /**
     * The deployment mode this strategy handles.
     */
    public static final String DEPLOYMENT_MODE = "default-tenantid";

    private String tenantId = null;

    public DefaultEventRegistryAutoDeploymentWithTenantIdStrategy(String tenantId) {
        this.tenantId = tenantId;
    }

    public DefaultEventRegistryAutoDeploymentWithTenantIdStrategy() {
    }

    public DefaultEventRegistryAutoDeploymentWithTenantIdStrategy(CommonAutoDeploymentProperties deploymentProperties) {
        super(deploymentProperties);
    }

    @Override
    protected String getDeploymentMode() {
        return DEPLOYMENT_MODE;
    }

    @Override
    protected void deployResourcesInternal(String deploymentNameHint, Resource[] resources, EventRegistryEngine engine) {
        if (StringUtils.isBlank(tenantId)) {
            throw new RuntimeException("Please set value for syspro-version property");
        }

        EventRepositoryService repositoryService = engine.getEventRepositoryService();

        // Create a single deployment for all resources using the name hint as the literal name
        final EventDeploymentBuilder deploymentBuilder = repositoryService.createDeployment().enableDuplicateFiltering().name(deploymentNameHint).tenantId(this.tenantId);
        for (final Resource resource : resources) {
            addResource(resource, deploymentBuilder);
        }

        try {

            deploymentBuilder.deploy();

        } catch (RuntimeException e) {
            if (isThrowExceptionOnDeploymentFailure()) {
                throw e;
            } else {
                LOGGER.warn("Exception while autodeploying event definitions. "
                        + "This exception can be ignored if the root cause indicates a unique constraint violation, "
                        + "which is typically caused by two (or more) servers booting up at the exact same time and deploying the same definitions. ", e);
            }
        }

    }
}
