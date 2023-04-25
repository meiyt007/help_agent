package com.zfsoft.superwindow.util.fa;


import cn.hutool.core.util.StrUtil;
import com.zfsoft.rest.pojo.auth.AuthTokenRequest;
import com.zfsoft.rest.pojo.auth.AuthTokenResponse;
import com.zfsoft.rest.service.auth.IAuthRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.core.env.Environment;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;


/**
 * ai服务token工具类
 *
 * @author chenbw
 * @date 2019年7月1日
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@Component
public class AiTokenUtil {

    /**
     * redis 操作
     */
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 接口权限相关service
     */
    @Autowired(required = false)
    private IAuthRestService authRestService;

    @Autowired
    private Environment environment;

    /**
     * redis存放token的key值
     */
    private String AI_SERVICE_TOKEN = "ai:service:token";

    /**
     * 用户名（用于生成token，存放于feign.properties）
     */
    private String USERNAME;

    /**
     * 密码（用于生成token，存放于feign.properties）
     */
    private String PASSWORD;

    /**
     * 获取token
     *
     * @return
     * @author chenbw
     * @date 2019年7月1日
     */
    public String getToken() {
        if(StrUtil.isBlank(USERNAME)) {
            USERNAME = environment.getProperty("feign.username");
            PASSWORD = environment.getProperty("feign.password");
        }
        String token = redisTemplate.opsForValue().get(AI_SERVICE_TOKEN);
        if (StrUtil.isBlank(token)) {
            AuthTokenRequest authTokenReqest = new AuthTokenRequest();
            authTokenReqest.setUserName(USERNAME);
            authTokenReqest.setPassword(PASSWORD);
            AuthTokenResponse authTokenResponse = authRestService.getAuthToken(authTokenReqest);
            if (authTokenResponse.getCode().equals(200)) {
                token = authTokenResponse.getToken();
                redisTemplate.opsForValue().set(AI_SERVICE_TOKEN, token, 7000, TimeUnit.SECONDS);
            }
        }
        return token;
    }

    /**
     * 获取初始化token的请求对象
     *
     * @param tClass 请求对象
     * @return 初始化Token的请求对象
     * @throws Exception
     * @author gaolh
     * @date 2019-7-4 13:31:03
     */
    public <T> T getTokenRequest(Class<T> tClass)  {
        T instance = null;
        try {
            instance = tClass.newInstance();
            Method setTokenMethod = tClass.getMethod("setToken", String.class);
            setTokenMethod.invoke(instance, getToken());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }
}

