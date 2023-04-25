FROM openjdk:14-jdk-alpine3.10
##注释清楚dockerfile作者
MAINTAINER wuxx <wuxinxin@zhuofansoft.com>
USER root
WORKDIR /zfsoft
ENV PINPOINT_VERSION=2.2.1 \
    TIME_ZONE="Asia/Shanghai" \
    COLLECT_IP="127.0.0.1"
ENV ENV="/zfsoft/config/zfsoft.env"
COPY ./target/platform-gateway*.jar /zfsoft/gateway-service-provider.jar
COPY entrypoint.sh /zfsoft/entrypoint.sh

EXPOSE 8080
RUN cd /zfsoft \
    && echo -e http://mirrors.ustc.edu.cn/alpine/v3.12/main/ > /etc/apk/repositories \
    && apk add --update ttf-dejavu fontconfig \
    && wget http://hf.zhuofansoft.com:9999/download/attachments/6488086/pinpoint-agent-${PINPOINT_VERSION}.tar.gz \
    && chmod a+x /zfsoft/entrypoint.sh \
    && tar -zxvf pinpoint-agent-${PINPOINT_VERSION}.tar.gz \
    && mv pinpoint-agent-${PINPOINT_VERSION}  pinpoint-agent \
    && rm -rf  /zfsoft/pinpoint-agent-${PINPOINT_VERSION}.tar.gz \
    && ln -snf /usr/share/zoneinfo/$TIME_ZONE /etc/localtime && echo '$TIME_ZONE' > /etc/timezone
ENTRYPOINT ["sh","-c","/zfsoft/entrypoint.sh"]
