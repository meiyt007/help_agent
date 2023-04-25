package com.zfsoft.ha.interceptor;


import com.zfsoft.ha.constant.RedisConstants;
import com.zfsoft.ha.data.HaLoginUserInfo;
import com.zfsoft.ha.util.HaLoginUserHolder;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.service.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * 用户登录拦截器
 *
 * @author kangax
 * @version 1.0
 * @date 2022/7/25 下午1:43
 */
@Slf4j
public class HaUserLoginInterceptor implements HandlerInterceptor {

    /**
     * redis
     */
    @Autowired(required = false)
    private RedisTemplate redisTemplate;

    /***
     * 在请求处理之前进行调用(Controller方法调用之前)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("执行了用户登录拦截器的preHandle方法");
        try {
            ApiResultSet<Object> resultSet = new ApiResultSet<>();
            //统一拦截（查询当前redis是否存在user）(这里user会在每次登录成功后，写入redis)
            String token = request.getHeader("token");
            log.info("执行用户登录拦截器的preHandle方法获取当前用户token信息:{}", token);
            if (token != null && !"".equals(token)) {
                //从redis中取出登录user信息
                HaLoginUserInfo userInfo = (HaLoginUserInfo) redisTemplate.opsForValue().get(RedisConstants.LOGIN_SESSION_TOKEN+token);
                log.info("执行用户登录拦截器的preHandle方法获取当前用户信息:{}", userInfo);
                if (userInfo != null) {
                    //将登录用户信息存入ThreadLocal
                    HaLoginUserHolder.addCurrentHaUserInfo(userInfo);
                    redisTemplate.expire(RedisConstants.LOGIN_SESSION_TOKEN+token,RedisConstants.LOGIN_SESSION_TTL, TimeUnit.HOURS);
                    return true;
                } else {
                    //未获取用户信息返回错误信息
                    resultSet.setCode(ApiResultSet.PARAM_VALID_ERROR);
                    resultSet.setMessage("token不正确!");
                    response.reset();
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().print(JsonUtil.objToJsonStr(resultSet));
                    return false;
                }
            } else {
                //token为空返回信息
                resultSet.setCode(ApiResultSet.PARAM_VALID_ERROR);
                resultSet.setMessage("token不能为空!");
                response.reset();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().print(JsonUtil.objToJsonStr(resultSet));
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("用户登录信息校验失败！" + e.getMessage());
        }

        return false;
        //如果设置为false时，被请求时，拦截器执行到此处将不会继续操作
        //如果设置为true时，请求将会继续执行后面的操作
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("执行了拦截器的postHandle方法");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("执行了拦截器的afterCompletion方法");
        //避免内存泄漏
        HaLoginUserHolder.removeHaThreadLocal();
    }


}
