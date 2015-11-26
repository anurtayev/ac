#!/usr/bin/env bash

apt-get -y install python-software-properties

add-apt-repository ppa:webupd8team/java
echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections
apt-get update
apt-get -qq install oracle-java8-installer

# Java 8
#echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections
#echo oracle-java7-installer shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections
#apt-get -qq install oracle-java8-installer
#apt-get -qq install oracle-java7-installer

# Git, curl, unzip
apt-get -y install git curl unzip

# GVM
#su - vagrant -c 'curl -s http://get.sdkman.io | bash'

# Groovy
#su - vagrant -c 'sdk install groovy'
#su - vagrant -c 'sdk default groovy'

# Gradle
#su - vagrant -c 'sdk install gradle'
#su - vagrant -c 'sdk default gradle'

# springboot
#su - vagrant -c 'sdk install springboot'
#su - vagrant -c 'sdk default springboot'

# MongoDB
#apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 7F0CEB10
#echo "deb http://repo.mongodb.org/apt/ubuntu "$(lsb_release -sc)"/mongodb-org/3.0 multiverse" | tee /etc/apt/sources.list.d/mongodb-org-3.0.list
#apt-get update
#apt-get -y install mongodb-org

####################################### GUI
# gnome-core
#apt-get -y install gnome-core
#apt-get -y install vnc4server

#su - vagrant -c 'echo vagrant >/tmp/file;echo vagrant >>/tmp/file;vncpasswd </tmp/file >/tmp/vncpasswd.1 2>/tmp/vncpasswd.2'
#su - vagrant -c 'cp /vagrant/provision/xstartup .vnc'
#su - vagrant -c 'cp /vagrant/provision/config .vnc'
#su - vagrant -c 'vncserver -geometry 1280x1024'

####################################### GitLab
#apt-get install curl openssh-server ca-certificates postfix
#curl https://packages.gitlab.com/install/repositories/gitlab/gitlab-ce/script.deb.sh | sudo bash
#apt-get install gitlab-ce
#gitlab-ctl reconfigure


