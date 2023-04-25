package com.zfsoft.microservice.workflow.interceptor;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zfsoft.microservice.workflow.data.WorkflowAuthUserDetails;
import com.zfsoft.microservice.workflow.redis.CacheService;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.spring.SpringContextHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                CurrentLoginUser loginUser = objectMapper.readValue(URLDecoder.decode(json, "UTF-8"),CurrentLoginUser.class);
                if(loginUser != null){
                    CurrentLoginUserHolder.setCurrentLoginUser(loginUser);
                    try {
                        CacheService redisTemplate = SpringContextHelper.getBean(CacheService.class);
                        Object object = redisTemplate.get(BaseStaticParameter.WORKFLOW_ACTIVITIUSER + loginUser.getUserOid());
                        if(null!=object){
                            Map<String, String> map = JSON.parseObject(object.toString(), HashMap.class);
                            WorkflowAuthUserDetails user = new WorkflowAuthUserDetails();
                            user.setPassword(map.get("password"));
                            user.setUsername(map.get("username"));
                            user.setAccount(map.get("account"));
                            user.setUserOid(map.get("userOid"));
                            redisTemplate.set(BaseStaticParameter.WORKFLOW_ACTIVITIUSER + loginUser.getUserOid(),JSONUtil.toJsonStr(map),3600);
                            SimpleGrantedAuthority grantedAuthorityUser = new SimpleGrantedAuthority("ACTIVITI_USER") ;
                            SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_ACTIVITI_USER") ;
                            SimpleGrantedAuthority grantedAuthorityTeam = new SimpleGrantedAuthority("GROUP_activitiTeam") ;
                            List<SimpleGrantedAuthority> list = new ArrayList<>();
                            list.add(grantedAuthorityUser);
                            list.add(grantedAuthority);
                            list.add(grantedAuthorityTeam);
                            PreAuthenticatedAuthenticationToken auth = new PreAuthenticatedAuthenticationToken(
                                    user,user.getPassword(),list
                            );
                            auth.setDetails(new WebAuthenticationDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(auth);
                        }
                    }catch (Exception e){
                        logger.error("workflow用户授权异常",e);
                        e.printStackTrace();
                    }
                }
            }catch (Throwable e){
                logger.error("json 反序列化失败",e);
            }
        }
        return true;
    }
}
