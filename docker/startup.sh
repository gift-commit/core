#!/bin/sh

app_jar=`ls -1 $API_HOME/*-api.jar`

java -jar $app_jar -server "$@"