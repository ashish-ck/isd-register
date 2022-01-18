#!/bin/bash

APP_NAME="isd-register"
APP_FULL_NAME="${APP_NAME}"
APP_JAR="${WORK_DIR}/opsmx-isd-register-0.0.1-SNAPSHOT.jar"
PORT=8010

JAVA_OPTS+="\
    -Dspring.application.name=${APP_FULL_NAME} \
    -Dspring.config.location=classpath:/,file:///opsmx/conf/ \
    -Dspring.config.name=application,${APP_NAME} \
    -Dspring.cloud.bootstrap.location=classpath:/,file:///opsmx/conf/ \
    -Dspring.profiles.active=default,vault,local \
    -XX:+UseG1GC \
    -jar"

JAVA_NEWRELIC_AGENT=""
JAVA_NEWRELIC_PROPERTIES=""

APP_FLAGS=""

java \
    -Dserver.port=${PORT} $JAVA_OPTS \
    $JAVA_NEWRELIC_AGENT $JAVA_NEWRELIC_PROPERTIES \
    ${APP_JAR} ${APP_FLAGS}

echo "${APP_FULL_NAME} service started ..."

