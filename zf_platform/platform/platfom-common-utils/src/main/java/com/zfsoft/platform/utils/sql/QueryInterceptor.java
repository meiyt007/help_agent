package com.zfsoft.platform.utils.sql;

import com.zfsoft.platform.utils.bean.ReflectionUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;


/**
 * @author: kkfan
 * @create: 2021-09-27 09:53:03
 * @description: sql查询特殊字符参数转义处理
 *     1、Signature Executor 拦截要在PageHelper之后 防止PageHelper分页错误
 *     2、在参数修改时不能直接对 parameter 修改 其可能为map 还可以为自定义的example、注解语句或参数实体等
 *     3、参数转义后要注意其它拦截器中对sql的处理，如PageHelper拦截器会导致参数处理多次
 * @1.0 Parameter 处理
 * @1.1 Map 处理
 * @1.2 Object  处理
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Intercepts(
    {
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
    }
)
@Log4j2
public class QueryInterceptor implements Interceptor {

    /**
     * 需转义字符，以配置为准
     */
    private String[] limitChars = {"%","_"};

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        Object target = invocation.getTarget();

        if (target instanceof CachingExecutor) {

            Object[] args = invocation.getArgs();
            MappedStatement ms = (MappedStatement) args[0];
            Object parameter = args[1];
            RowBounds rowBounds = (RowBounds) args[2];

            ResultHandler resultHandler = (ResultHandler) args[3];
            Executor executor = (Executor) invocation.getTarget();
            CacheKey cacheKey;
            BoundSql boundSql;
            //由于逻辑关系，只会进入一次
            if(args.length == 4){
                //4 个参数时
                boundSql = ms.getBoundSql(parameter);
                cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
            } else {
                //6 个参数时
                cacheKey = (CacheKey) args[4];
                boundSql = (BoundSql) args[5];
            }

            //获取参数
            List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
            try {
                for (int i = 0; i < parameterMappings.size(); i++) {
                    ParameterMapping parameterMapping = parameterMappings.get(i);
                    if (parameterMapping.getMode() != ParameterMode.OUT) {
                        String propertyName = parameterMapping.getProperty();
                        if (boundSql.hasAdditionalParameter(propertyName)) {
                            Object value = boundSql.getAdditionalParameter(propertyName);
                            boundSql.setAdditionalParameter(propertyName, dealValue(value));
                        } else {
                            if(parameter instanceof Map) {
                                Object value = null;
                                if(propertyName.contains(".")) {
                                    String[] propertyNames = propertyName.split("\\.");
                                    for(int j = 0, len = propertyNames.length; j < len; j++) {
                                        Object tempValue = (null != value) ? ReflectionUtils.getField(value, propertyNames[j]) : ((Map)parameter).get(propertyNames[j]);
                                        ReflectionUtils.setFieldValueByFieldName(value == null ? parameter : value, propertyNames[j], dealValue(tempValue));
                                        value = tempValue;
                                    }
                                } else {
                                    value = ((Map)parameter).get(propertyName);
                                    boundSql.setAdditionalParameter(propertyName, dealValue(value));
                                }

                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.error("参数转义处理异常", e);
            }


            //注：下面的方法可以根据自己的逻辑调用多次，在分页插件中，count 和 page 各调用了一次
            return executor.query(ms, parameter, rowBounds, resultHandler, cacheKey, boundSql);
        }

        return invocation.proceed();
    }

    /**
     * 参数值处理
     * @param value
     * @return
     */
    public Object dealValue(Object value) {
        if (null != value && value instanceof String) {
            for (String limitChar : limitChars) {
                value = ((String) value).replaceAll(Matcher.quoteReplacement("\\" + limitChar), Matcher.quoteReplacement(limitChar));
                if((String.valueOf(value).endsWith("%") || String.valueOf(value).startsWith("%"))
                        && StringUtils.equals(limitChar, "%")
                        && String.valueOf(value).length() != 1) {
                    String stringVal = (String) value;
                    value = stringVal.length() > 2
                            ? stringVal.substring(0, 1)
                                + stringVal.substring(1, stringVal.length() - 1).replaceAll(Matcher.quoteReplacement(limitChar), Matcher.quoteReplacement("\\" + limitChar))
                                + stringVal.substring(stringVal.length() - 1, stringVal.length())
                            : (stringVal.endsWith("%") && stringVal.startsWith("%")
                                ? stringVal
                                : (stringVal.endsWith("%")
                                    ? stringVal.substring(0, 1).replaceAll(Matcher.quoteReplacement(limitChar), Matcher.quoteReplacement("\\" + limitChar)) + "%"
                                    : "%" + stringVal.substring(1, 2).replaceAll(Matcher.quoteReplacement(limitChar), Matcher.quoteReplacement("\\" + limitChar)))
                                );
                } else if(((String) value).length() == 1 && limitChar.contains((String)value)) {
                    value =  String.valueOf(value).replaceAll(Matcher.quoteReplacement(limitChar), Matcher.quoteReplacement("\\" + limitChar));
                }
            }
        }
        return value;
    }

    @Override
    public Object plugin(Object target) {
        //TODO Spring bean 方式配置时，如果没有配置属性就不会执行下面的 setProperties 方法，就不会初始化，因此考虑在这个方法中做一次判断和初始化
        //TODO https://github.com/pagehelper/Mybatis-PageHelper/issues/26
        return Plugin.wrap(target, this);
    }

    @Value("${com.zfsoft.query.special.characters:%,_}")
    public void setLimitChars(String limitChars) {
        this.limitChars = StringUtils.isNotBlank(limitChars) ? limitChars.split(",") : new String[]{"%","_"};
    }

    @Override
    public void setProperties(Properties properties) {

    }

}
