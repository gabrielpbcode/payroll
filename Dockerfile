FROM adoptopenjdk:11-openj9

ARG JAR_FILE
ADD target/${JAR_FILE} app.jar

ENTRYPOINT java $JAVA_OPTS -Xsharedclasses -Xquickstart -jar app.jar

EXPOSE 8080