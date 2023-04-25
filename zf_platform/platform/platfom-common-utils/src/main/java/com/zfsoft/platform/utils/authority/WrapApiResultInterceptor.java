package com.zfsoft.platform.utils.authority;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;

/**
 * @Auther: lijun@sstcsoft.com
 * @Date: 2019-10-12 11:42
 */
public class WrapApiResultInterceptor implements HandlerInterceptor {
    private final static Logger logger =LoggerFactory.getLogger(WrapApiResultInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        String json = request.getHeader("CUSTOM-REQUEST-HEADER");
        if(!StringUtils.isEmpty(json)){
            logger.debug(json);
            json = URLDecoder.decode(json, "UTF-8");
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                CurrentLoginUser loginUser = objectMapper.readValue(URLDecoder.decode(json, "UTF-8"), CurrentLoginUser.class);
                if(loginUser != null){
                    CurrentLoginUserHolder.setCurrentLoginUser(loginUser);
                } else {
                    // 防止因线程复用导致用户信息泄露
                    CurrentLoginUserHolder.setCurrentLoginUser(null);
                }
            }catch (Throwable e){
                logger.error("json 反序列化失败",e);
            }
        } else {
            // 防止因线程复用导致用户信息泄露
            CurrentLoginUserHolder.setCurrentLoginUser(null);
        }
        return true;
    }
}
