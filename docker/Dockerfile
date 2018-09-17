FROM anapsix/alpine-java:8_server-jre_unlimited

ENV API_HOME /api

COPY startup.sh $API_HOME/
COPY jars/*.jar $API_HOME/

#Add this if we want logging configuration
#ADD ./conf ${API_HOME}/conf/

RUN chmod +x $API_HOME/startup.sh

ENTRYPOINT ["/bin/sh", "-c"]
CMD ["/api/startup.sh"]