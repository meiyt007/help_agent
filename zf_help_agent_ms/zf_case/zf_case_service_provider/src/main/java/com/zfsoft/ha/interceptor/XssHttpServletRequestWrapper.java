package com.zfsoft.ha.interceptor;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.cases.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

/**
 * @Description //XssHttpServletRequestWrapper:对 HttpServletRequest 进行一次包装, 进行xss过滤.针对 POST application/x-www-form-urlencoded 或者 GET请求.
 * @Author: Wangyh
 * @Date: 2022/9/29 17:06
 */
@Slf4j
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private HttpServletRequest orgRequest;

    // html过滤
    private final static HTMLFilter htmlFilter = new HTMLFilter();

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        orgRequest = request;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        // 非json类型，直接返回
        if (!isJsonRequest()) {
            return super.getInputStream();
        }
        // 为空，直接返回
        String json = IOUtils.toString(super.getInputStream(), "utf-8");
        if (StrUtil.isBlank(json)) {
            return super.getInputStream();
        }

        // xss过滤
        json = xssEncode(json);
        final ByteArrayInputStream bis = new ByteArrayInputStream(json.getBytes("utf-8"));
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return true;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

            @Override
            public int read() throws IOException {
                return bis.read();
            }
        };
    }

    /**
     * 覆盖getParameter方法，将参数名和参数值都做xss过滤。<br/>
     */
    @Override
    public String getParameter(String rawName) {
        String value = super.getParameter(xssEncode(rawName));
        if (StrUtil.isNotBlank(value)) {
            value = xssEncode(value);
        }
        return value;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] parameters = super.getParameterValues(name);
        if (parameters == null || parameters.length == 0) {
            return null;
        }

        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = xssEncode(parameters[i]);
        }
        return parameters;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        Enumeration<String> parameterNames = super.getParameterNames();

        List<String> list = new LinkedList<>();
        if (parameterNames != null) {

            while (parameterNames.hasMoreElements()) {
                String rawName = parameterNames.nextElement();
                String safetyName = xssEncode(rawName);

                if (!Objects.equals(rawName, safetyName))
                {
                    log.warn("请求路径: {},参数键: {}, xss过滤后: {}. 疑似xss攻击",
                            orgRequest.getRequestURI(), rawName, safetyName);
                }
                list.add(safetyName);
            }
        }

        return Collections.enumeration(list);
    }

    @Override
    public Map<String, String[]> getParameterMap() {

        Map<String, String[]> map = new LinkedHashMap<>();
        Map<String, String[]> parameters = super.getParameterMap();
        for (String key : parameters.keySet()) {
            String[] values = parameters.get(key);
            for (int i = 0; i < values.length; i++) {
                values[i] = xssEncode(values[i]);
            }
            map.put(key, values);
        }
        return map;
    }

    /**
     * 覆盖getHeader方法，将参数名和参数值都做xss过滤。<br/>
     * 如果需要获得原始的值，则通过super.getHeaders(name)来获取<br/>
     * getHeaderNames 也可能需要覆盖
     */
    @Override
    public String getHeader(String name) {
        String value = super.getHeader(xssEncode(name));
        if (StrUtil.isNotBlank(value)) {
            value = xssEncode(value);
        }
        return value;
    }


    private String xssEncode(String input) {
        return htmlFilter.filter(input);
    }

    /**
     * 是否是Json请求
     */
    public boolean isJsonRequest()
    {
        String header = super.getHeader(HttpHeaders.CONTENT_TYPE);
        return StringUtils.startsWithIgnoreCase(header, MediaType.APPLICATION_JSON_VALUE);
    }
}


