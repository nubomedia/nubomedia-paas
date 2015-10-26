FROM alincalinciuc/nubomedia

MAINTAINER Nubomedia

ADD . /usr/lib/jvm/java-1.7.0-openjdk-amd64/lib/
RUN mkdir ~/.kurento
RUN echo "kms.url.provider=mypackage.myClass" > ~/.kurento/config.properties
