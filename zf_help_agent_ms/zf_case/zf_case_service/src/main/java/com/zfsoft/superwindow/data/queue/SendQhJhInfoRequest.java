package com.zfsoft.superwindow.data.queue;

import lombok.Data;

/**
 * @author sky
 * @version 1.0
 * @description:  推送接口接口请求参数。
 * @date 2021/7/15 10:03
 */
@Data
public class SendQhJhInfoRequest {
    /**
     *  小屏mac地址
     */
    private  String smallScreenMacAddress;
}
