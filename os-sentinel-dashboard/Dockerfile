FROM pig4cloud/java:8-jre

MAINTAINER changhongyuan@126.com

ENV TZ=Asia/Shanghai
ENV JAVA_OPTS="-Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom"

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /os-sentinel-dashboard

WORKDIR /os-sentinel-dashboard

EXPOSE 8080

ADD ./target/os-sentinel-dashboard.jar ./

CMD sleep 60;java $JAVA_OPTS -jar os-sentinel-dashboard.jar
