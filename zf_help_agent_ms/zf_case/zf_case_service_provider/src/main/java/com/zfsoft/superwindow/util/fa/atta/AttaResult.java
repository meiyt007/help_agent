package com.zfsoft.superwindow.util.fa.atta;

import cn.hutool.core.util.StrUtil;

import java.io.Serializable;

/**
 * 附件请求结果类
 *
 * @author gaolh
 * @date 2016-11-21 13:50:03
 */
public class AttaResult implements Serializable {
    private static final long serialVersionUID = 195643586981551852L;
    /**
     * 状态码，200代表成功
     */
    private String code;
    /**
     * 状态说明
     */
    private String message;

    /**
     * 请求响应是否成功
     *
     * @return 当code为200时，表示成功，否则为失败
     */
    public boolean isSuccess() {
        if (StrUtil.isNotBlank(code) && "200".equalsIgnoreCase(code)) {
            return true;
        }
        return false;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
