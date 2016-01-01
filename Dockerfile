FROM ubuntu:14.04
RUN apt-get update && apt-get -y upgrade
RUN apt-get install default-jdk -y
RUN groupadd tomcat
RUN useradd -s /bin/false -g tomcat -d /opt/tomcat tomcat
RUN cd ~
RUN wget http://mirror.sdunix.com/apache/tomcat/tomcat-8/v8.0.30/bin/apache-tomcat-8.0.30.tar.gz
RUN mkdir /opt/tomcat
RUN tar xvf apache-tomcat-8*tar.gz -C /opt/tomcat --strip-components=1
RUN cd /opt/tomcat
RUN chgrp -R tomcat conf
RUN chmod g+rwx conf
RUN chmod g+r conf/*
RUN chown -R tomcat work/ temp/ logs/
//todo, continue with adding tomcat.conf to /etc/init/tomcat.conf