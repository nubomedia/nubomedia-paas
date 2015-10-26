FROM alincalinciuc/nubomedia

MAINTAINER Nubomedia

ADD *.jar /usr/lib/jvm/java-1.7.0-openjdk-amd64/lib/
ENV CLASSPATH $CLASSPATH:/usr/lib/jvm/java-1.7.0-openjdk-amd64/lib/
RUN mkdir ~/.kurento
RUN echo "kms.url.provider=mypackage.myClass" > ~/.kurento/config.properties
