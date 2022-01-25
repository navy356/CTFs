FROM tomcat:8.0.51-jre7-alpine

ENV RUN_USER tomcat
ENV RUN_GROUP tomcat

RUN addgroup ${RUN_GROUP} && adduser -G ${RUN_GROUP} -D -h ${CATALINA_HOME} ${RUN_USER}
RUN chown -R tomcat:tomcat $CATALINA_HOME
RUN ls -lah $CATALINA_HOME

RUN rm -rf /usr/local/tomcat/webapps/ROOT/
COPY flag /flag
COPY ROOT.war /usr/local/tomcat/webapps/ROOT.war
COPY tomcat-users.xml /usr/local/tomcat/conf/tomcat-users.xml

USER tomcat
EXPOSE 8080