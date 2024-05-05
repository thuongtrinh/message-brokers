# GitHub
    - URL: xxx
    - SSH: xxx

# Artifactory
    - Maven: 

# Build
    - Maven
        + Version 3.6.0 or above
        + Open JDK 1.8
        + Command: 
            * UAT: mvn -Puat -Dmaven.test.skip clean package
            * PROD: mvn -Pprod -Dmaven.test.skip clean package

# Technical Stack
    - Open JDK 1.8
    - Spring Boot 2.x

# Development
    - Run unit test
        + mvn -Pdev -Dtest=SyncControllerTest# test

    - Import SSl certificate to JDK
        + download file from web which u want to import trustcacerts
        + keytool -import -trustcacerts -file [certificate] -alias [alias] -keystore %JAVA_HOME%/jre/lib/security/cacerts 
        + default passoword: changeit

    - Run code gen from swagger
        mvn -Pcodegen compile

    - Compile code
        + mvn -Dspring.profiles.active=dev compile
        + mvn -Dspring.profiles.active=dev spring-boot:run

    - OpenAPI Guideline 
        https://springdoc.org/migrating-from-springfox.html