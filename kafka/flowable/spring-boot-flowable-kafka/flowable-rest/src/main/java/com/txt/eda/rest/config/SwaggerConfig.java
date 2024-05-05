package com.txt.eda.rest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.flowable.rest.service.api.RestResponseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Autowired
    protected ObjectMapper objectMapper;

    @Bean()
    public RestResponseFactory restResponseFactory() {
        RestResponseFactory restResponseFactory = new RestResponseFactory(objectMapper);
        return restResponseFactory;
    }

    @Bean
    public OpenAPI customOpenAPI
            (
                    @Value("${server.servlet.context-path}") String contextPath
//                    @Value("${application.description}") String appDesciption,
//                    @Value("${application.version}") String appVersion
//                    @Value("${build.date}") String buildDate
            ) {
        OpenAPI openAPI = new OpenAPI()
                .info(new Info()
                        .title("Syspro Rest API")
//                        .version(appVersion)
//                        .description(appDesciption.concat(" [flowable-rest").concat("]"))
                        .termsOfService("http://swagger.io/terms/")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));

        return openAPI;
    }
}
