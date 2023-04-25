package com.zfsoft.microservice.workflow.util;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.zfsoft.microservice.workflow.data.WorkflowAuthUserDetails;
import com.zfsoft.microservice.workflow.redis.CacheService;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.utils.spring.SpringContextHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName AuthenticationUtil
 * @Description: 设置登录用户信息
 * @Author wuxx
 * @Date 2021/2/25
 **/
public class AuthenticationUtil {

    /**
     * @description: 设置登录用户信息
     * @param userOid 用户oid
     * @author: wuxx
     * @Date: 2021/2/25 10:27
     **/
    public static void setAuthentication(String userOid){
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            WorkflowAuthUserDetails user = new WorkflowAuthUserDetails();
            //user.setPassword("");
            //user.setUsername("");
            //user.setAccount("");
            user.setUserOid(userOid);
            SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_ACTIVITI_USER") ;
            SimpleGrantedAuthority grantedAuthorityTeam = new SimpleGrantedAuthority("GROUP_activitiTeam") ;
            List<SimpleGrantedAuthority> list = new ArrayList<>();
            list.add(grantedAuthority);
            list.add(grantedAuthorityTeam);
            PreAuthenticatedAuthenticationToken auth = new PreAuthenticatedAuthenticationToken(
                    user,user.getPassword(),list
            );
            auth.setDetails(new WebAuthenticationDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
