package com.zfsoft.microservice.platform.gateway.register;

import cn.hutool.json.JSONObject;
import com.google.code.kaptcha.Producer;
import com.zfsoft.microservice.platform.data.vo.RegisterLicense;
import com.zfsoft.microservice.platform.gateway.feign.RegisterFeignService;
import com.zfsoft.platform.common.data.ApiResultSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @description: 卓繁注册文件授权controller
 * @author: wuxx
 * @Date: 2020/11/3 15:20
 **/
@RestController
@RequestMapping("/register")
public class ZhuoFanRegisterController {

    private Producer captchaProducer = null;

    @Autowired
    public void setCaptchaProducer(Producer captchaProducer) {
        this.captchaProducer = captchaProducer;
    }

    @Autowired
    private RegisterFeignService registerFeignService;

    /**
     * @description: 验证上传注册文件是否正确
     * @author: wuxx
     * @Date: 2020/11/3 15:30
     **/
    @RequestMapping(value = {"/initRegister"},method = {RequestMethod.GET})
    public Mono<Void> initRegister(ServerHttpResponse response,String applyOid,String tomcatOid ){
        HttpHeaders httpHeaders = response.getHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        ApiResultSet resultSet = registerFeignService.initRegister(applyOid,tomcatOid);
        byte[] dataBytes = resultSet.getData().toString().getBytes();
        DataBuffer bodyDataBuffer = response.bufferFactory().wrap(dataBytes);
        return response.writeWith(Mono.just(bodyDataBuffer));
    }

    /**
     * @description: 上传注册授权文件的解析
     * @author: wuxx
     * @Date: 2020/11/3 15:30
     **/
    @RequestMapping(value = {"/uploadRegister"},method = {RequestMethod.GET})
    public Mono<Void> uploadRegister(ServerHttpResponse response, String attaOid){
        HttpHeaders httpHeaders = response.getHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        ApiResultSet resultSet = registerFeignService.uploadRegister(attaOid);
        int code = resultSet.getCode();
        String message = resultSet.getMessage();
        Object data = resultSet.getData();
        JSONObject json = new JSONObject();
        json.set("code",code);
        json.set("message",message);
        json.set("data",data);
        byte[] dataBytes = json.toString().getBytes();
        DataBuffer bodyDataBuffer = response.bufferFactory().wrap(dataBytes);
        return response.writeWith(Mono.just(bodyDataBuffer));
    }

    /**
     * @description: 验证上传注册文件是否正确
     * @author: wuxx
     * @Date: 2020/11/3 15:30
     **/
    @RequestMapping(value = {"/checkRegisterLicense"},method = {RequestMethod.POST})
    public Mono<Void> checkRegisterLicense(ServerHttpResponse response,@RequestBody RegisterLicense registerLicense){
        HttpHeaders httpHeaders = response.getHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        ApiResultSet resultSet = registerFeignService.checkRegisterLicense(registerLicense);
        int code = resultSet.getCode();
        String message = resultSet.getMessage();
        Object data = resultSet.getData();
        JSONObject json = new JSONObject();
        json.set("code",code);
        json.set("message",message);
        json.set("data",data);
        byte[] dataBytes = json.toString().getBytes();
        DataBuffer bodyDataBuffer = response.bufferFactory().wrap(dataBytes);
        return response.writeWith(Mono.just(bodyDataBuffer));
    }

}
