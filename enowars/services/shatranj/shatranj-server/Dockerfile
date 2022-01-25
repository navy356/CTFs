FROM gradle:jdk14 as cache
RUN mkdir -p /home/gradle/cache_home
ENV GRADLE_USER_HOME /home/gradle/cache_home
COPY build.gradle /home/gradle/java-code/
WORKDIR /home/gradle/java-code
RUN gradle clean build -i --stacktrace

FROM gradle:jdk14 as builder
COPY --from=cache /home/gradle/cache_home /home/gradle/.gradle
COPY --chown=gradle:gradle . /usr/src/java-code/
WORKDIR /usr/src/java-code
RUN gradle bootJar -i --stacktrace

FROM openjdk:14
WORKDIR /app
COPY --from=builder /usr/src/java-code/build/libs/*.jar /app/app.jar
COPY ./wait-for-it.sh /app/wait-for-it.sh
EXPOSE 8080
RUN chmod +x ./wait-for-it.sh
CMD ["sh", "./wait-for-it.sh", "db:3306", "--timeout=0", "--", "java", "-jar", "app.jar"]