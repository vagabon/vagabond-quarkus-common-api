FROM registry.access.redhat.com/ubi8/openjdk-21:1.18-3

ENV LANGUAGE='en_US:en'

EXPOSE 8443

ENV JAVA_OPTS_APPEND="-Dquarkus.http.host=0.0.0.0 -Dquarkus.config.locations=/app/conf/application.properties -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"

ENTRYPOINT [ "/opt/jboss/container/java/run/run-java.sh" ]

