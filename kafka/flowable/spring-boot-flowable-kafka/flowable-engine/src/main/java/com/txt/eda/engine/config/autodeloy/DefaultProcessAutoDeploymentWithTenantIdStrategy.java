package com.txt.eda.engine.config.autodeloy;

import org.apache.commons.lang3.StringUtils;
import org.flowable.app.spring.autodeployment.DefaultAutoDeploymentStrategy;
import org.flowable.common.spring.CommonAutoDeploymentProperties;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.spring.configurator.AbstractProcessAutoDeploymentStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

public class DefaultProcessAutoDeploymentWithTenantIdStrategy extends AbstractProcessAutoDeploymentStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAutoDeploymentStrategy.class);

    /**
     * The deployment mode this strategy handles.
     */
    public static final String DEPLOYMENT_MODE = "default-tenantid";

    private String tenantId = null;

    public DefaultProcessAutoDeploymentWithTenantIdStrategy(String tenantId) {
        this.tenantId = tenantId;
    }

    public DefaultProcessAutoDeploymentWithTenantIdStrategy() {
    }

    public DefaultProcessAutoDeploymentWithTenantIdStrategy(CommonAutoDeploymentProperties deploymentProperties) {
        super(deploymentProperties);
    }

    @Override
    protected String getDeploymentMode() {
        return DEPLOYMENT_MODE;
    }

    @Override
    protected void deployResourcesInternal(String deploymentNameHint, Resource[] resources, ProcessEngine engine) {
        if (StringUtils.isBlank(tenantId)) {
            throw new RuntimeException("Please set value for syspro-version property");
        }

        RepositoryService repositoryService = engine.getRepositoryService();

        // Create a single deployment for all resources using the name hint as the literal name
        final DeploymentBuilder deploymentBuilder = repositoryService.createDeployment().enableDuplicateFiltering().name(deploymentNameHint).tenantId(this.tenantId);

        for (final Resource resource : resources) {
            addResource(resource, deploymentBuilder);
        }

        try {
            deploymentBuilder.deploy();
        } catch (RuntimeException e) {
            if (isThrowExceptionOnDeploymentFailure()) {
                throw e;
            } else {
                LOGGER.warn("Exception while autodeploying process definitions. "
                        + "This exception can be ignored if the root cause indicates a unique constraint violation, "
                        + "which is typically caused by two (or more) servers booting up at the exact same time and deploying the same definitions. ", e);
            }
        }

    }
}
