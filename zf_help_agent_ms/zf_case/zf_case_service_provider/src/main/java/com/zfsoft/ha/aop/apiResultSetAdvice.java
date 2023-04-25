package com.zfsoft.ha.aop;


import com.zfsoft.platform.common.data.ApiResultSet;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/11/18 11:35
 */
@Aspect
@Component
public class apiResultSetAdvice {

    @Pointcut("execution(public com.zfsoft.platform.common.data.ApiResultSet com.zfsoft.ha..*.*(..))")
    public void respontApi() {
    }

    @Around("respontApi()")
    public Object handleFeignCallResult(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        long start = System.currentTimeMillis();//开始时间
        Object[] args = joinPoint.getArgs();
        Object result = joinPoint.proceed(args);
        long end = System.currentTimeMillis();//介绍绍时间
        long takeTime = end - start;//执行需要时间
        if (result instanceof ApiResultSet) {
            ApiResultSet apiResultSet = (ApiResultSet) result;
            apiResultSet.setTime(takeTime / 1000.0 + "s");
        }
        return result;

    }
}
