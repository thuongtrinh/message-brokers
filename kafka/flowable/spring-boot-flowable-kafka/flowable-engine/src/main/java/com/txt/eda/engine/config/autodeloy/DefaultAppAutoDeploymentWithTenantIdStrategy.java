package com.txt.eda.engine.config.autodeloy;

import org.apache.commons.lang3.StringUtils;
import org.flowable.app.api.AppRepositoryService;
import org.flowable.app.api.repository.AppDeploymentBuilder;
import org.flowable.app.engine.AppEngine;
import org.flowable.app.spring.autodeployment.AbstractAppAutoDeploymentStrategy;
import org.flowable.app.spring.autodeployment.DefaultAutoDeploymentStrategy;
import org.flowable.common.spring.CommonAutoDeploymentProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

public class DefaultAppAutoDeploymentWithTenantIdStrategy extends AbstractAppAutoDeploymentStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAutoDeploymentStrategy.class);

    public static final String DEPLOYMENT_MODE = "default-tenantid";

    private String tenantId = null;

    public DefaultAppAutoDeploymentWithTenantIdStrategy(String tenantId) {
        this.tenantId = tenantId;
    }

    public DefaultAppAutoDeploymentWithTenantIdStrategy() {
    }

    public DefaultAppAutoDeploymentWithTenantIdStrategy(CommonAutoDeploymentProperties deploymentProperties) {
        super(deploymentProperties);
    }

    @Override
    protected String getDeploymentMode() {
        return DEPLOYMENT_MODE;
    }

    @Override
    protected void deployResourcesInternal(String deploymentNameHint, Resource[] resources, AppEngine engine) {
        if (StringUtils.isBlank(tenantId)) {
            throw new RuntimeException("Please set value for syspro-version property");
        }

        AppRepositoryService repositoryService = engine.getAppRepositoryService();

        // Create a separate deployment for each resource using the resource name

        for (final Resource resource : resources) {

            String resourceName = determineResourceName(resource);
            if (resourceName.contains("/")) {
                resourceName = resourceName.substring(resourceName.lastIndexOf('/') + 1);

            } else if (resourceName.contains("\\")) {
                resourceName = resourceName.substring(resourceName.lastIndexOf('\\') + 1);
            }
            final AppDeploymentBuilder deploymentBuilder = repositoryService.createDeployment().enableDuplicateFiltering().name(resourceName).tenantId(this.tenantId);
            addResource(resource, resourceName, deploymentBuilder);

            try {

                deploymentBuilder.deploy();

            } catch (RuntimeException e) {
                if (isThrowExceptionOnDeploymentFailure()) {
                    throw e;
                } else {
                    LOGGER.warn("Exception while autodeploying app definition for resource {}. "
                            + "This exception can be ignored if the root cause indicates a unique constraint violation, "
                            + "which is typically caused by two (or more) servers booting up at the exact same time and deploying the same definitions. ", resource, e);
                }
            }
        }
    }
}
