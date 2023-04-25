package com.zfsoft.microservice.workflow.exception;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.validate.ParamValidException;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.ActivitiOptimisticLockingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @Auther: lijun@sstcsoft.com
 * @Date: 2019/9/10 16:01
 */
@RefreshScope
@ControllerAdvice
public class GlobalExceptionHanddler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHanddler.class);

    //异常栈信息 不可打印在生产环境中
    @Value("${debug:false}")
    private Boolean debug ;

    @ExceptionHandler(Throwable.class)
    public  @ResponseBody
    ApiResultSet handdlerUnknownException(Throwable e){
        logger.error("发生未知错误！",e);
        ApiResultSet apiResultSet = new ApiResultSet();
        apiResultSet.setCode(ApiResultSet.UNKNOWN_ERROR);
        apiResultSet.setMessage(debug?"发生未知错误："+e.getMessage():null);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        String stackTrace = stringWriter.getBuffer().toString();
        apiResultSet.setStackTrace(debug?stackTrace:null);
        return apiResultSet;
    }

    @ExceptionHandler(ParamValidException.class)
    public  @ResponseBody
    ApiResultSet handdlerParamValidException(ParamValidException e){
        logger.error("参数验证错误！",e);
        ApiResultSet apiResultSet = new ApiResultSet();
        apiResultSet.setCode(ApiResultSet.PARAM_VALID_ERROR);

        //apiResultSet.setMessage("参数验证错误："+e.getMessage());
        //截取掉属性名
        String err = e.getMessage().substring(e.getMessage().indexOf(":")+1);
        apiResultSet.setMessage("参数验证错误："+err);

        return apiResultSet;
    }

    /**
     * @description:  验证信息给出返回提示信息
     * @author: wuxx
     * @Date: 2020/9/1 13:52
     **/
    @ExceptionHandler(ResultInfoException.class)
    public  @ResponseBody
    ApiResultSet handdlerResultInfoException(ResultInfoException e){
        //logger.error("数据验证提示！",e);
        ApiResultSet apiResultSet = new ApiResultSet();
        apiResultSet.setCode(ApiResultSet.DIRTY_DATA_TITLE);
        apiResultSet.setMessage(e.getMessage());
        return apiResultSet;
    }

    /**
     * @description:  参数非空验证给出返回提示信息
     * @author: wuxx
     * @Date: 2021/5/10 13:52
     **/
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public  @ResponseBody
    ApiResultSet handdlerMissingServletRequestParameterException(MissingServletRequestParameterException e){
        //logger.error("数据验证提示！",e);
        ApiResultSet apiResultSet = new ApiResultSet();
        apiResultSet.setCode(ApiResultSet.DIRTY_DATA_TITLE);
        String message = e.getMessage();
        message = message.replaceAll("Required String parameter '","");
        message = message.replaceAll("Required Integer parameter '","");
        message = message.replaceAll("' is not present","");
        apiResultSet.setMessage("参数"+message+"不能为空！");
        return apiResultSet;
    }

    /**
     * @description:  参数非空验证给出返回提示信息
     * @author: wuxx
     * @Date: 2021/5/10 13:52
     **/
    @ExceptionHandler(ActivitiException.class)
    public  @ResponseBody
    ApiResultSet handdlerActivitiException(ActivitiException e){
        //logger.error("数据验证提示！",e);
        ApiResultSet apiResultSet = new ApiResultSet();
        apiResultSet.setCode(ApiResultSet.DIRTY_DATA_TITLE);
        String message = e.getMessage();
        if(message.contains("Unknown property used in expression:")){
            message = message.replaceAll("Unknown property used in expression:","");
            apiResultSet.setMessage("办理条件"+message+"缺少参数！");
        }else {
            apiResultSet.setMessage(message);
        }
        return apiResultSet;
    }


    /**
     * @description:  数组越界
     * @author: wuxx
     * @Date: 2021/6/21 13:52
     **/
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public  @ResponseBody
    ApiResultSet handdlerIndexOutOfBoundsException(IndexOutOfBoundsException e){
        //logger.error("数据验证提示！",e);
        ApiResultSet apiResultSet = new ApiResultSet();
        apiResultSet.setCode(ApiResultSet.DIRTY_DATA_TITLE);
        String message = e.getMessage();
        apiResultSet.setMessage(message);
        return apiResultSet;
    }

    /**
     * @description: 性能异常
     * @author: wuxx
     * @Date: 2021/6/21 13:52
     **/
    @ExceptionHandler(ActivitiOptimisticLockingException.class)
    public  @ResponseBody
    ApiResultSet handdlerActivitiOptimisticLockingException (ActivitiOptimisticLockingException e){
        //logger.error("数据验证提示！",e);
        ApiResultSet apiResultSet = new ApiResultSet();
        apiResultSet.setCode(200);
        String message = e.getMessage();
        apiResultSet.setData("重试");
        apiResultSet.setMessage(message);
        return apiResultSet;
    }
}
