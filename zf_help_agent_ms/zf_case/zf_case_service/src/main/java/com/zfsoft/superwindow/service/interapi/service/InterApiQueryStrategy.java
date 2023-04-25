package com.zfsoft.superwindow.service.interapi.service;


import com.zfsoft.superwindow.service.easyquickcase.data.ResponseData;
import com.zfsoft.superwindow.service.interapi.data.ApiReqParams;

/**
 * @Author: qiaol
 * @Description: 通用接口调用策略类
 * @Date: 2022/5/16 17:09
 */
public interface InterApiQueryStrategy {

    ResponseData query(ApiReqParams params);
}
