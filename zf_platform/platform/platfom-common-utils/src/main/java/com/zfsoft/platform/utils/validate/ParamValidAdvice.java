package com.zfsoft.platform.utils.validate;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @Auther: lijun@sstcsoft.com
 * @Date: 2019/9/11 10:27
 */

@Aspect
public class ParamValidAdvice {

    @Pointcut(value = "@annotation(com.zfsoft.platform.utils.validate.ParamValid)")
    public void paramValid(){

    }

    @Before("paramValid()")
    public void validParamData(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Annotation[][] methodAnnotations = method.getParameterAnnotations();
        Object[] args = joinPoint.getArgs();
        Object param;
        ValidGroups validGroups;
        Class<?> groups[];
        Annotation[] paramAnnotations;
        StringBuilder sb = new StringBuilder();
//        boolean validPass = true;
        String validMsg;

        for(int i=0;i<methodAnnotations.length;i++){
            paramAnnotations = methodAnnotations[i];
            for(int j=0;j<paramAnnotations.length;j++){
                if(paramAnnotations[j] instanceof ValidGroups){
                    validGroups = (ValidGroups)paramAnnotations[j];
                    groups = validGroups.groups();
                    param = args[i];
                    validMsg = ValidationUtils.validateEntity(param,groups);
                    if(validMsg.length()>0){
                        //sb.append(param.getClass().getName());
                        //sb.append("\r\n");
                        sb.append(validMsg);
                        //sb.append("\r\n");
                    }
                }
            }
        }
        if(sb.length()>0){
            throw new ParamValidException(sb.toString());
        }
    }
}
