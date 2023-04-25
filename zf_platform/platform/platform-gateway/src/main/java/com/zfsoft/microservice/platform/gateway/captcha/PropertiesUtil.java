package com.zfsoft.microservice.platform.gateway.captcha;
import org.springframework.core.env.Environment;

/**
 * @Auther: lijun@sstcsoft.com
 * @Date: 2019/9/25 17:37
 */
public class PropertiesUtil {
    private static Environment env = null;

    public static void setEnvironment(Environment env) {
        PropertiesUtil.env = env;
    }

    public static String getProperty(String key) {
        return PropertiesUtil.env.getProperty(key);
    }
}
