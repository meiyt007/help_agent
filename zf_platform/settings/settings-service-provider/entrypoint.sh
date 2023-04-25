#!/bin/sh
source /zfsoft/config/zfsoft.env
# pinpint客户端引入部分
if [[ ! -z "${COLLECT_IP}" ]]; then
JAVA_OPT="${JAVA_OPT} -javaagent:/zfsoft/pinpoint-agent/pinpoint-bootstrap-2.2.1.jar"
JAVA_OPT="${JAVA_OPT} -Dpinpoint.agentId=${COLLECT_AGENTID}"
JAVA_OPT="${JAVA_OPT} -Dpinpoint.applicationName=${COLLECT_APPNAME}"
sed -i "s/127.0.0.1/${COLLECT_IP}/g" /zfsoft/pinpoint-agent/pinpoint-root.config
sed -i "s/127.0.0.1/${COLLECT_IP}/g" /zfsoft/pinpoint-agent/profiles/local/pinpoint.config
sed -i "s/127.0.0.1/${COLLECT_IP}/g" /zfsoft/pinpoint-agent/profiles/release/pinpoint.config
fi

#springboot配置类型引入部分
if [[ ! -z "${PROFILE_MODE}" ]]; then
JAVA_OPT="${JAVA_OPT} -Dspring.profiles.active=${PROFILE_MODE}"
fi

#nacos探针注册宿主机ip部分
if [[ ! -z "${NACOS_DISIP}" ]]; then
JAVA_OPT="${JAVA_OPT} -Dspring.cloud.nacos.discovery.ip=${NACOS_DISIP}"
fi

if [[ ! -z "${NACOS_DISPORT}" ]]; then
JAVA_OPT="${JAVA_OPT} -Dspring.cloud.nacos.discovery.port=${NACOS_DISPORT}"
fi

#nacos服务端地址和namespace
if [[ ! -z "${NACOS_CFGNAMESPACE}" ]]; then
JAVA_OPT="${JAVA_OPT} -Dnacos.server.${PROFILE_MODE}.namespace=${NACOS_CFGNAMESPACE}"
fi

if [[ ! -z "${NACOS_CFGSERVERADDR}" ]]; then
JAVA_OPT="${JAVA_OPT} -Dnacos.server.${PROFILE_MODE}.host=${NACOS_CFGSERVERADDR}"
fi

if [[ ! -z "${NACOS_CFGGROUP}" ]]; then
JAVA_OPT="${JAVA_OPT} -Dnacos.server.group=${NACOS_CFGGROUP}"
fi
echo "${JAVA_OPT}"
ZFSOFT_SERVICE="${JAVA_OPT} -jar /zfsoft/settings-service-provider.jar"
java ${ZFSOFT_SERVICE}
