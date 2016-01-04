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
  apt-get update && \
  apt-get -y upgrade && \
  apt-get install -y build-essential && \
  apt-get install -y software-properties-common && \
  apt-get install -y byobu curl git htop man unzip vim wget && \
  rm -rf /var/lib/apt/lists/*

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
RUN \
  apt-get update && apt-get -y upgrade && \
  apt-get install default-jdk -y && \
  groupadd tomcat && \
  useradd -s /bin/false -g tomcat -d /opt/tomcat tomcat && \
  cd ~ && \
  wget http://mirror.sdunix.com/apache/tomcat/tomcat-8/v8.0.30/bin/apache-tomcat-8.0.30.tar.gz && \
  mkdir /opt/tomcat && \
  tar xvf apache-tomcat-8*tar.gz -C /opt/tomcat --strip-components=1 && \
  cd /opt/tomcat && \
  chgrp -R tomcat /opt/tomcat/conf && \
  chmod g+rwx /opt/tomcat/conf && \
  chmod g+r /opt/tomcat/conf/* && \
  chown -R tomcat /opt/tomcat/work/ /opt/tomcat/temp/ /opt/tomcat/logs/

ADD conf/tomcat/tomcat-dev.conf /etc/init/tomcat.conf

EXPOSE 8080

###
#
#   Mongo DB
#
###
RUN \
  apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 7F0CEB10 && \
  echo "deb http://repo.mongodb.org/apt/ubuntu "$(lsb_release -sc)"/mongodb-org/3.0 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-3.0.list && \
  apt-get update && \
  apt-get install -y mongodb-org


ADD conf/mongodb/mongod.conf /etc/mongod.conf
ADD conf/mongodb/init-mongo.sh /root/init-mongo.sh

CMD \
  service mongod start $$ \
  /root/init-mongo.sh

###
#
#   Rabbit MQ
#
###
RUN \
  apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 7F0CEB10 && \
  curl http://www.rabbitmq.com/rabbitmq-signing-key-public.asc | sudo apt-key add - && \
  apt-get update && \
  apt-get -y install rabbitmq-server

EXPOSE 5672

CMD \
  service rabbitmq-server start $$ \
  rabbitmqctl add_user bushido bushido

###
#
#   Nginx
#
###
RUN \
  add-apt-repository -y ppa:nginx/stable && \
  apt-get update && \
  apt-get install -y nginx && \
  rm -rf /var/lib/apt/lists/* && \
  echo "\ndaemon off;" >> /etc/nginx/nginx.conf && \
  chown -R www-data:www-data /var/lib/nginx

VOLUME ["/etc/nginx/sites-available", "/etc/nginx/sites-enabled", "/etc/nginx/conf.d", "/var/log/nginx", "/var/www/app.bushidowallet.com/html"]

EXPOSE 80
EXPOSE 443