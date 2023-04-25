package com.zfsoft.superwindow.service.easyquickcase.data;

import cn.hutool.core.util.StrUtil;

import java.text.MessageFormat;

/**
 * 业务异常
 *
 * @Auther dusd
 * @Date 2019/10/11 14:53
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@lombok.Data
public class ServiceException extends RuntimeException {
    private Integer code;

    private String errorMessage;

    public ServiceException(Integer code, String errorMessage, Object... objects) {
        super(errorMessage);
        this.code = code;
        this.errorMessage = getMsg(errorMessage, objects);
    }

    public ServiceException(String errorMessage, Object... objects) {
        super(errorMessage);
        this.code = 500;
        this.errorMessage = getMsg(errorMessage, objects);
    }

    /**
     * 占位符数据替换
     * @Author dusd
     * @Date 2020/3/24 16:57
     * @param
     * @return
     */
    private String getMsg(String message,Object[] objects) {
        if(StrUtil.isBlank(message))
            return message;
        if(objects == null)
            return message;
        String[] arrMessage = message.split("");
        StringBuffer sb = new StringBuffer();
        int index = 0;
        for (int i = 0; i < arrMessage.length; i++) {
            sb.append(arrMessage[i]);
            if(i < (arrMessage.length-1) && arrMessage[i].equals("{") && arrMessage[i+1].equals("}")) {
                sb.append(index++);
            }
        }
        return MessageFormat.format(sb.toString(), objects);
    }
}
