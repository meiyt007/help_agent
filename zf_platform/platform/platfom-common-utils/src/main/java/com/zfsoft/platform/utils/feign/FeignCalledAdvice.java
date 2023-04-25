package com.zfsoft.platform.utils.feign;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.validate.ParamValidException;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @ClassName FeignCalledAdvice
 * @Description
 * @Author
 * @Date2020-09-16 10:13
 * @Version V1.0
 **/

@Aspect
@Slf4j
public class FeignCalledAdvice {

    @Pointcut(value = "@annotation(com.zfsoft.platform.utils.feign.ProcessFeignCalledResult)")
    public void allFeignClients(){
    }

    @Around("allFeignClients()")
    public Object handleFeignCallResult(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            Object[] args = joinPoint.getArgs();
            Object result = joinPoint.proceed(args);
            if(result instanceof ApiResultSet){
                ApiResultSet apiResultSet = (ApiResultSet)result;
                if(apiResultSet.getCode()!=ApiResultSet.SUCCESS){
                    //String errorInfo ="call Method:\""+method.getName()+"\" 返回错误，错误码：\""+apiResultSet.getCode()
                    //        +",错误信息：\""+apiResultSet.getMessage()+"\"";
                    String errorInfo ="错误码：\""+apiResultSet.getCode()
                            +",错误信息：\""+apiResultSet.getMessage()+"\"";
                    errorInfo = apiResultSet.getMessage();
                    log.error(errorInfo);
                    throw new FeignCalledException(errorInfo);
                }
            }
            return result;
        }catch (Exception e){
            log.error(e.getMessage());
            throw new FeignCalledException(e.getMessage());
        }
    }
}
