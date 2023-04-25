package com.zfsoft.platform.utils.log;

import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName LogHandlerInterceptor
 * @Description
 * @Author
 * @Date2020-09-16 14:31
 * @Version V1.0
 **/
public class LogHandlerInterceptor implements HandlerInterceptor {
    private final static String REMOTE_IP = "remoteIp";
    private final static String USER_ACCOUNT = "userAccount";
    private final static String USER_NAME = "userName";
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        // 放SessionId
        String remoteIp = getIpAddr(request);
        MDC.put(REMOTE_IP, remoteIp);
        CurrentLoginUser user = CurrentLoginUserHolder.getCurrentLoginUser();
        if(user!=null){
            MDC.put(USER_ACCOUNT,user.getUserName());
        }
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {

    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        // 删除
        MDC.remove(REMOTE_IP);
        MDC.remove(USER_ACCOUNT);
    }

    private   String getIpAddr(HttpServletRequest request)  {
        String ip  =  request.getHeader( " x-forwarded-for " );
        if (ip  ==   null   ||  ip.length()  ==   0   ||   " unknown ".equalsIgnoreCase(ip))  {
            ip  =  request.getHeader( " Proxy-Client-IP " );
        }
        if (ip  ==   null   ||  ip.length()  ==   0   ||   " unknown " .equalsIgnoreCase(ip))  {
            ip  =  request.getHeader( " WL-Proxy-Client-IP " );
        }
        if (ip  ==   null   ||  ip.length()  ==   0   ||   " unknown " .equalsIgnoreCase(ip))  {
            ip  =  request.getRemoteAddr();
        }
        return  ip;
    }
}
