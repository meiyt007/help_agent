package com.zfsoft.outer.inter.pojo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/9/21 16:32
 */
@ConfigurationProperties(prefix = "cert")
@Component
@Data
public class ZzkProperties {
    private String url;
    private String account;
    private String password;
    private String login;
    private String scanCertQrCode;
    private String orgName;
    //物理地址
    public String userName;


}
