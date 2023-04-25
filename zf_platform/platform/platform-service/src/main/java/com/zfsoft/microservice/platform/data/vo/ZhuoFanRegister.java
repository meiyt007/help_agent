package com.zfsoft.microservice.platform.data.vo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 卓繁注册文件配置
 *
 * @author zxx
 * @date 2020-08-14 13:37
 */
@Configuration
@ConfigurationProperties(prefix = "zhuofan.register")
@Data
public class ZhuoFanRegister {

    private Boolean isAble;

    private String path;

    private String code;

    private String message;

    private String version;

    private String register;

    private String authorization;

    private String monitor;

}
