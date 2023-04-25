package com.zfsoft.cases.exception;

import com.mysql.cj.exceptions.DataTruncationException;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.validate.ParamValidException;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

/**
 * @Auther: lijun@sstcsoft.com
 * @Date: 2019/9/10 16:01
 */
@ControllerAdvice
public class GlobalExceptionHanddler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHanddler.class);

    @Value("${debug:false}")
    private Boolean debugValue;

    @ExceptionHandler(Throwable.class)
    public  @ResponseBody
    ApiResultSet handdlerUnknownException(Throwable e){
        logger.error("发生未知错误！",e);
        ApiResultSet apiResultSet = new ApiResultSet();
        apiResultSet.setCode(ApiResultSet.UNKNOWN_ERROR);
        apiResultSet.setMessage("发生未知错误："+e.getMessage());
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        String stackTrace = stringWriter.getBuffer().toString();
        apiResultSet.setStackTrace(debugValue?stackTrace:"");
        return apiResultSet;
    }

    @ExceptionHandler(ParamValidException.class)
    public  @ResponseBody
    ApiResultSet handdlerParamValidException(ParamValidException e){
        logger.error("参数验证错误！",e);
        ApiResultSet apiResultSet = new ApiResultSet();
        apiResultSet.setCode(ApiResultSet.PARAM_VALID_ERROR);

        apiResultSet.setMessage("参数验证错误："+e.getMessage());
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

    @ExceptionHandler(UncategorizedSQLException.class)
    public  @ResponseBody
    ApiResultSet handdlerUncategorizedSQLException(UncategorizedSQLException e){
        logger.error("SQL错误！",e);
        ApiResultSet apiResultSet = new ApiResultSet();
        apiResultSet.setCode(ApiResultSet.PARAM_VALID_ERROR);

        apiResultSet.setMessage("SQL错误");
        return apiResultSet;
    }

    @ExceptionHandler(DataTruncationException.class)
    public  @ResponseBody
    ApiResultSet handdlerDataTruncationException(DataTruncationException e){
        logger.error("SQL错误！",e);
        ApiResultSet apiResultSet = new ApiResultSet();
        apiResultSet.setCode(ApiResultSet.PARAM_VALID_ERROR);

        apiResultSet.setMessage("SQL错误");
        return apiResultSet;
    }


    @ExceptionHandler(BindException.class)
    public  @ResponseBody
    ApiResultSet handlerMethodArgumentNotValidException(BindException e){
        logger.error("参数验证错误！",e);
        ApiResultSet apiResultSet = new ApiResultSet();
        apiResultSet.setCode(ApiResultSet.UNKNOWN_ERROR);
        //打印校验住的所有的错误信息
        StringBuilder sb = new StringBuilder("参数错误：[");
        List<ObjectError> list = ((BindException) e).getAllErrors();
        for (ObjectError item : list) {
            sb.append(item.getDefaultMessage()).append(',');
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append(']');
        String msg  =  sb.toString();
        apiResultSet.setMessage(msg);
        return apiResultSet;
    }
}
