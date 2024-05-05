package com.txt.eda.engine.config.autodeloy;

import org.apache.commons.lang3.StringUtils;
import org.flowable.app.spring.autodeployment.DefaultAutoDeploymentStrategy;
import org.flowable.common.spring.CommonAutoDeploymentProperties;
import org.flowable.form.api.FormDeploymentBuilder;
import org.flowable.form.api.FormRepositoryService;
import org.flowable.form.engine.FormEngine;
import org.flowable.form.spring.autodeployment.AbstractFormAutoDeploymentStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

public class DefaultFormAutoDeploymentWithTenantIdStrategy extends AbstractFormAutoDeploymentStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAutoDeploymentStrategy.class);

    /**
     * The deployment mode this strategy handles.
     */
    public static final String DEPLOYMENT_MODE = "default-tenantid";

    private String tenantId = null;

    public DefaultFormAutoDeploymentWithTenantIdStrategy(String tenantId) {
        this.tenantId = tenantId;
    }

    public DefaultFormAutoDeploymentWithTenantIdStrategy() {
    }

    public DefaultFormAutoDeploymentWithTenantIdStrategy(CommonAutoDeploymentProperties deploymentProperties) {
        super(deploymentProperties);
    }

    @Override
    protected String getDeploymentMode() {
        return DEPLOYMENT_MODE;
    }

    @Override
    protected void deployResourcesInternal(String deploymentNameHint, Resource[] resources, FormEngine engine) {
        if (StringUtils.isBlank(tenantId)) {
            throw new RuntimeException("Please set value for syspro-version property");
        }

        FormRepositoryService repositoryService = engine.getFormRepositoryService();

        // Create a single deployment for all resources using the name hint as the literal name
        final FormDeploymentBuilder deploymentBuilder = repositoryService.createDeployment().enableDuplicateFiltering().name(deploymentNameHint).tenantId(this.tenantId);
        for (final Resource resource : resources) {
            addResource(resource, deploymentBuilder);
        }

        try {

            deploymentBuilder.deploy();

        } catch (RuntimeException e) {
            if (isThrowExceptionOnDeploymentFailure()) {
                throw e;
            } else {
                LOGGER.warn("Exception while autodeploying form definitions. "
                        + "This exception can be ignored if the root cause indicates a unique constraint violation, "
                        + "which is typically caused by two (or more) servers booting up at the exact same time and deploying the same definitions. ", e);
            }
        }

    }
}
