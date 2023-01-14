#! /bin/bash

mvn -f pom-nameserver.xml clean package
mv target/nameserver-0.1-jar-with-dependencies.jar ./


