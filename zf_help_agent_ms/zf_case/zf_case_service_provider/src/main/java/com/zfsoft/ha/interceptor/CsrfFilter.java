package com.zfsoft.ha.interceptor;


import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

 /**
 * @Description //CSRF过滤器
 * @Author: Wangyh
 * @Date: 2022/9/28 17:02
 */
@Slf4j
@Component
@ConfigurationProperties(prefix = "security.csrf")
@WebFilter(filterName = "CsrfFilter", urlPatterns = "/*")
public class CsrfFilter implements Filter {

    /**
     * 过滤器配置对象
     */
    FilterConfig filterConfig = null;

    /**
     * 是否启用
     */
    private boolean enable;

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * 忽略的URL
     */
    private List<String> excludes;

    public void setExcludes(List<String> excludes) {
        this.excludes = excludes;
    }

    /**
     * 初始化
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    /**
     * 拦截
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        // 不启用或者已忽略的URL不拦截
        if (!enable || isExcludeUrl(request.getServletPath())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String referer = request.getHeader("Referer");
        String serverName = request.getServerName();

        // 判断是否存在外链请求本站
        if (null != referer && referer.indexOf(serverName) < 0) {
            log.error("CSRF过滤器 => 服务器：{} => 当前域名：{}", serverName, referer);
            servletResponse.setContentType("text/html; charset=utf-8");
            servletResponse.getWriter().write("系统不支持当前域名的访问！");
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    /**
     * 销毁
     */
    @Override
    public void destroy() {
        this.filterConfig = null;
    }

    /**
     * 判断是否为忽略的URL
     *
     * @param url URL路径
     * @return true-忽略，false-过滤
     */
    private boolean isExcludeUrl(String url) {
        if (excludes == null || excludes.isEmpty()) {
            return false;
        }
        return excludes.stream().map(pattern -> Pattern.compile("^" + pattern)).map(p -> p.matcher(url))
                .anyMatch(Matcher::find);
    }
}
