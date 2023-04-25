package com.zfsoft.microservice.form.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.zfsoft.microservice.form.config.MyRequestWrapper;
import com.zfsoft.microservice.form.data.FormAuthorize;
import com.zfsoft.microservice.form.manager.FormAuthorizeManager;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.parameters.P;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

/**
 * @ClassName PermissionFilter
 * @Description:
 * @Author wuxx
 * @Date 2021/4/28
 **/
@Order(1)
@WebFilter(filterName = "permissionFilter",urlPatterns = {"/manager/*"})
public class PermissionFilter implements Filter {

    //权限的集合
    private List<String> permissionList = new ArrayList();

    @Autowired
    private FormAuthorizeManager formAuthorizeManager;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //System.out.println("初始化过滤器");
        //打开设计页面和编辑页面
        permissionList.add("/manager/getFormDesign");
        //获取填报数据
        permissionList.add("/manager/getFormData");
        permissionList.add("/manager/getFormDataByReportOid");
        permissionList.add("/manager/queryFormColumnListByObjectCode");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        if(permissionList.contains(requestURI)){
            if("GET".equals(method.toUpperCase())){
                String authorizeKey = servletRequest.getParameter("authorizeKey");
                this.checkFormAuthorize(authorizeKey);
                filterChain.doFilter(servletRequest,servletResponse);
            }else if("POST".equals(method.toUpperCase())){
                //取Body数据
                MyRequestWrapper requestWrapper = new MyRequestWrapper(request);
                String body = requestWrapper.getBody();
                if(StrUtil.isNotBlank(body)){
                    JSONObject jsonObject = JSONUtil.parseObj(body);
                    String authorizeKey =  jsonObject.getStr("authorizeKey");
                    this.checkFormAuthorize(authorizeKey);
                }
                filterChain.doFilter(requestWrapper,servletResponse);
            }else {
                filterChain.doFilter(servletRequest,servletResponse);
            }
        }else {
            filterChain.doFilter(servletRequest,servletResponse);
        }

    }

    @Override
    public void destroy() {
        //System.out.println("过滤器被销毁了");
    }

    /**
     * @description: 检查授权key是否有效
     * @param authorizeKey 授权key
     * @author: wuxx
     * @Date: 2021/4/28 15:41
     **/
    private void checkFormAuthorize(String authorizeKey){
        if(StrUtil.isBlank(authorizeKey)){
            throw new ResultInfoException("授权key不能为空！");
        }
        FormAuthorize authorize = formAuthorizeManager.getFormAuthorizeByAuthorizeKey(authorizeKey);
        if(null!=authorize){
            //授权期限类型（0永久 1临时）
            Integer timeType = authorize.getTimeType();
            if(BaseStaticParameter.Y == timeType){
                Date startTime = authorize.getStartTime();
                Date endTime = authorize.getEndTime();
                Date date = new Date();
                //未开始
                if(date.before(startTime)){
                    throw new ResultInfoException("未到授权开始时间！");
                }
                //过期
                if(DateUtils.addDays(date, 1).before(date)){
                    throw new ResultInfoException("授权已到期！");
                }
            }

        }else {
            throw new ResultInfoException("参数authorizeKey不正确！");
        }
    }

}
