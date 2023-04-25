package com.zfsoft.microservice.form.util;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.zfsoft.platform.utils.DESAuthorizeUtils;
import com.zfsoft.platform.utils.validate.ResultInfoException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName AuthorizeKeyUtil
 * @Description: 授权的解密判断工具类
 * @Author wuxx
 * @Date 2021/3/15
 **/
public class AuthorizeKeyUtil {

    /**
     * @description: 检测授权的KEY是否正确，并且有效时间不能超过5分钟
     * @param authorizeKey 授权key
     * @author: wuxx
     * @Date: 2021/3/15 12:00
     **/
    public static String checkAuthorizeKey(String authorizeKey){
        String decrypt = DESAuthorizeUtils.decrypt(authorizeKey);
        if(StrUtil.isNotEmpty(decrypt)){
            String[] splitAuthorizeKeyApi = decrypt.split("-");
            if(splitAuthorizeKeyApi.length>1){
                String stamp = splitAuthorizeKeyApi[1];
                Date suDate = new Date(Long.parseLong(stamp)); // 时间戳转换日期
                long between = DateUtil.between(suDate, new Date(), DateUnit.MINUTE);
                if(between>3 || between<0){
                    throw new ResultInfoException("授权KEY信息日期格式不正确！");
                }
                return splitAuthorizeKeyApi[0];
            }else {
                throw new ResultInfoException("授权KEY信息格式不正确！");
            }
        }else {
            throw new ResultInfoException("授权KEY信息为空！");
        }
    }
}
