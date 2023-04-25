package com.zfsoft.outer.inter.pojo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/10/28 10:01
 */
@ConfigurationProperties(prefix = "tasknumber")
@Component
@Data
public class TaskProperties {

    private String account;
    private String password;
    private String url;
    private String getToken;
    private String getMachineCategoryTreeById;
    private String takeNumber;
    private String takePriorityNumber;
}
