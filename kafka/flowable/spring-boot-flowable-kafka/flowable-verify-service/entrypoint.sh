#!/bin/bash
for file in /etc/secrets/*; do source "$file"; done && java -XX:MaxRAMPercentage=60.0 -XshowSettings:vm -jar ${APP_HOME} --spring.profiles.active=${SPRING_PROFILE}

