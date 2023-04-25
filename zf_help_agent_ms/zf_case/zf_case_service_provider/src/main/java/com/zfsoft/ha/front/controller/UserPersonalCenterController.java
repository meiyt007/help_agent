package com.zfsoft.ha.front.controller;

import com.zfsoft.ha.data.HaLoginUserInfo;
import com.zfsoft.ha.data.HaWorkUser;
import com.zfsoft.ha.front.UserPersonalCenterService;
import com.zfsoft.ha.managers.HaWorkUserServiceManager;
import com.zfsoft.ha.util.HaLoginUserHolder;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.superwindow.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;



@RestController
@Slf4j
public class UserPersonalCenterController implements UserPersonalCenterService {


    @Resource
    private HaWorkUserServiceManager userServiceManager;

    @Override
    public ApiResultSet<Map<String,String>> updatePass(String oldPass, String newPass, String newPassConfirm, HttpServletRequest request) {
        log.info("用户个人中心：进入更新密码,入参:"+oldPass,newPass,newPassConfirm);
        Map<String,String> resMap = new HashMap<>();
        try{
            HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
            HaWorkUser helpWorkUser= userServiceManager.getHelpWorkUserById(currentHaLoginUserInfo.getId());
            if(StringUtil.isEmpty(oldPass)||StringUtil.isEmpty(newPass)){
                return  new ApiResultSet<>(500,"用户个人中心：密码为空",null);
            }

            String passwrod = helpWorkUser.getPassword();
            String oldPasswrod = CommonUtil.md5(helpWorkUser.getAccount()+oldPass+helpWorkUser.getSalt());

            if(!StringUtil.equals(passwrod,oldPasswrod)){
                return  new ApiResultSet<>(500,"用户个人中心：旧密码错误",null);
            }
            if(!StringUtil.equals(newPass,newPassConfirm)){
                return  new ApiResultSet<>(500,"用户个人中心：两次密码不一致",null);
            }
            if(StringUtil.equals(oldPass,newPass)){
                return  new ApiResultSet<>(500,"用户个人中心：新密码和旧密码相同",null);
            }
            String newPasswrod = CommonUtil.md5(helpWorkUser.getAccount()+newPass+helpWorkUser.getSalt());

            helpWorkUser.setPassword(newPasswrod);
            Map<String, Object> result = userServiceManager.saveOrUpdateThaWorkUser(helpWorkUser);

            resMap.put("name",helpWorkUser.getName());
            log.info("用户个人中心：更新密码成功");
        }catch (Exception e){
            log.info("用户个人中心：更新密码失败");
            return  new ApiResultSet<>(500,"用户个人中心：更新密码失败",e.getMessage());
        }
        return ApiResultSet.ok("用户个人中心：更新密码成功",resMap);
    }

    @Override
    public ApiResultSet<Map<String,String> > updateInfo(HaWorkUser haWorkUser) {
        log.info("用户个人中心：进入更新用户数据,入参:");
        Map<String,String> resMap = new HashMap<>();
        try{

            //验证token 获取userId
            HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
            HaWorkUser helpWorkUser= userServiceManager.getHelpWorkUserById(currentHaLoginUserInfo.getId());
            HaWorkUser haWorkUserNew = mergeObjects(haWorkUser, helpWorkUser);
            Map<String, Object> result = userServiceManager.saveOrUpdateThaWorkUser(haWorkUserNew);

            resMap.put("avgServiceTime",haWorkUserNew.getAvgServiceTime().toString());
            resMap.put("maxServiceNum",haWorkUserNew.getMaxServiceNum().toString());
            resMap.put("servicePostion",haWorkUserNew.getServicePostion());
            resMap.put("turnConfig",haWorkUserNew.getTurnConfig());

            log.info("用户个人中心：更新用户数据成功:");
        }catch (Exception e){
            log.info("用户个人中心：更新用户数据失败："+e.getMessage());
            return  new ApiResultSet<>(500,"用户个人中心：更新用户数据失败",e.getMessage());
        }

        return ApiResultSet.ok("用户个人中心：更新用户数据成功",resMap);
    }

    /**
     * 合并两个对象的值，将第一个对象不为空的值赋值到目标对象
     * @param sourceBean
     * @param targetBean
     * @param <T>
     * @return
     */
    public static <T> T mergeObjects(T sourceBean, T targetBean) {

        Class sourceBeanClass = sourceBean.getClass();
        Class targetBeanClass = targetBean.getClass();

        Field[] sourceFields = sourceBeanClass.getDeclaredFields();
        Field[] targetFields = targetBeanClass.getDeclaredFields();
        for (int i = 0; i < sourceFields.length; i++) {
            Field sourceField = sourceFields[i];
            Field targetField = targetFields[i];
            sourceField.setAccessible(true);
            targetField.setAccessible(true);

            Class<?> type = sourceField.getType();

            try {
                if (!ObjectUtils.isEmpty(sourceField.get(sourceBean))) {
                    targetField.set(targetBean, sourceField.get(sourceBean));
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return targetBean;
    }
}
