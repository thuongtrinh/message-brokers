FROM openjdk:8-jre

MAINTAINER ThuongTX <trinhxuan92@gmail.com>

USER root

ENV ROOT_DIR /app

ENV FUll_APP_NAME flowable-assignment-service*.jar
ENV APP_NAME flowable-assignment-service.jar
ENV APP_HOME ${ROOT_DIR}/${APP_NAME}

COPY target/${FUll_APP_NAME} ${APP_HOME}

COPY entrypoint.sh ./entrypoint.sh
RUN chmod +x ./entrypoint.sh

ENTRYPOINT ["/bin/sh", "-c", "./entrypoint.sh"]
