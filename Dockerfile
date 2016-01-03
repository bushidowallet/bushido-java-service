###
#
#   Bushido Service Docker file
#
#   To run:
#
#   docker run -p 8080:8080 bushido/bushido-java-service
#
###
FROM ubuntu:14.04

RUN \
  echo "deb http://www.rabbitmq.com/debian/ testing main" >> /etc/apt/sources.list && \
  sed -i 's/# \(.*multiverse$\)/\1/g' /etc/apt/sources.list && \
  echo "deb http://repo.mongodb.org/apt/ubuntu "$(lsb_release -sc)"/mongodb-org/3.0 multiverse" | tee /etc/apt/sources.list.d/mongodb-org-3.0.list && \
  apt-get update && \
  apt-get -y upgrade && \
  apt-get install -y build-essential && \
  apt-get install -y software-properties-common && \
  apt-get install -y byobu curl git htop man unzip vim wget && \
  rm -rf /var/lib/apt/lists/*

   # apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 7F0CEB10 && \
   # curl http://www.rabbitmq.com/rabbitmq-signing-key-public.asc | sudo apt-key add - && \

ADD root/.bashrc /root/.bashrc
ADD root/.gitconfig /root/.gitconfig
ADD root/.scripts /root/.scripts

ENV HOME /root

WORKDIR /root

CMD ["bash"]

###
#
#   Java / Tomcat
#
###
RUN apt-get update && apt-get -y upgrade
RUN apt-get install default-jdk -y
RUN groupadd tomcat
RUN useradd -s /bin/false -g tomcat -d /opt/tomcat tomcat
RUN cd ~
RUN wget http://mirror.sdunix.com/apache/tomcat/tomcat-8/v8.0.30/bin/apache-tomcat-8.0.30.tar.gz
RUN mkdir /opt/tomcat
RUN tar xvf apache-tomcat-8*tar.gz -C /opt/tomcat --strip-components=1
RUN cd /opt/tomcat
RUN chgrp -R tomcat /opt/tomcat/conf
RUN chmod g+rwx /opt/tomcat/conf
RUN chmod g+r /opt/tomcat/conf/*
RUN chown -R tomcat /opt/tomcat/work/ /opt/tomcat/temp/ /opt/tomcat/logs/
COPY tomcat.conf /etc/init/tomcat.conf
RUN echo "service tomcat status"

EXPOSE 8080

