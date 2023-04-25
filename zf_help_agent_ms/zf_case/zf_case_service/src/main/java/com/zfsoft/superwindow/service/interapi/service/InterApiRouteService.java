package com.zfsoft.superwindow.service.interapi.service;

import com.zfsoft.superwindow.service.easyquickcase.data.ResponseData;
import com.zfsoft.superwindow.service.interapi.data.ApiReqParams;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: qiaol
 * @Description:
 * @Date: 2022/5/16 17:00
 */
@RequestMapping("/interRouteApi")
public interface InterApiRouteService<T> {


    @PostMapping("/query/{code}")
    ResponseData<T> routeQueryApi(@PathVariable String code, @RequestBody ApiReqParams params);

    @PostMapping("/validate/{code}")
    ResponseData<T> routeValidateApi(@PathVariable String code, @RequestBody ApiReqParams params);
}
