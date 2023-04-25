package com.zfsoft.microservice.platform.gateway.utils;

/**
 * @ClassName EnvironmentUtils
 * @Description
 * @Author
 * @Date2020-09-29 18:59
 * @Version V1.0
 **/
public class EnvironmentUtils {

    private static String applicationName;

    private static String env;

    public static String getApplicationName() {
        return applicationName;
    }

    public static void setApplicationName(String applicationName) {
        EnvironmentUtils.applicationName = applicationName;
    }

    public static String getEnv() {
        return env;
    }

    public static void setEnv(String env) {
        EnvironmentUtils.env = env;
    }

    public static String getAppEnv() {
        return applicationName + "[" + env + "]";
    }
}
