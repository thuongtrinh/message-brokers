# GitHub
    - URL: https://xxx/xxx-service.git
    - SSH: ssh://git@xxx/xxx-service.git

# Artifactory
    - Maven: 
    - Docker: ``
# Docker
    - Build
        docker build -t ${DOCKER_URL}/${POM_ARTIFACTID}:${POM_VERSION} .

    - Deploy to repository
        docker push ${DOCKER_URL}/${POM_ARTIFACTID}:${POM_VERSION}

    - Run and remove container when exit
        docker run -v -rm /u01/apps/api/config:/app/config -v /u01/apps/api/log:/app/log -e APP_CONFIG_FILE='file:/app/config/application.properties,file:/app/config/application.yml,file:/app/config/application-dev.yml' -e APP_CONFIG_LOG_FILE='file:/app/config/logback-spring.xml' -p 8180:8080 IMAGE

    - Run in backgroud
        #docker run -d -rm -v /u01/apps/api/config:/app/config -v /u01/apps/api/log:/app/log -e APP_CONFIG_FILE='file:/app/config/application.properties,file:/app/config/application.yml,file:/app/config/application-dev.yml' -e APP_CONFIG_LOG_FILE='file:/app/config/logback-spring.xml' -p 8180:8080 IMAGE
        docker run -d -v /u01/apps/config:/app/config -v /u01/apps/log:/app/log -p 8088:9090 name-service

# Development
    - Run unit test
        mvn -Pdev -Dtest=com.txt.SyncControllerTest#testClientAction test
        mvn -Pdev -Dtest=com.txt.SyncControllerTest#testMethod test

    - Run code gen from swagger
        mvn -Pcodegen compile    
###
