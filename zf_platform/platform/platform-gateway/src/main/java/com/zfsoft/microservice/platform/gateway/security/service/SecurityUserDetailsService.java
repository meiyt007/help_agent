package com.zfsoft.microservice.platform.gateway.security.service;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.zfsoft.microservice.platform.data.sys.SysLogin;
import com.zfsoft.microservice.platform.gateway.feign.RegisterFeignService;
import com.zfsoft.microservice.platform.gateway.feign.SysConfigFeignService;
import com.zfsoft.microservice.platform.gateway.feign.SysDictFeignService;
import com.zfsoft.microservice.platform.gateway.feign.SysUserFeignService;
import com.zfsoft.microservice.platform.gateway.security.data.SysUser;
import com.zfsoft.microservice.platform.gateway.security.exception.UnRegisteredException;
import com.zfsoft.microservice.settings.data.SysConfig;
import com.zfsoft.microservice.settings.data.SysDict;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.security.data.AuthUserDetails;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.spring.SpringContextHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Set;

/**
 * 用户登录验证逻辑相关操作
 * @author zhanglei
 * @date 2019-12-05
 */
@Component
public class SecurityUserDetailsService implements ReactiveUserDetailsService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        String warnDay = null;
        //验证是否注册授权
        RegisterFeignService registerFeignService = SpringContextHelper.getBean(RegisterFeignService.class);
        ApiResultSet checkRegisterResult = registerFeignService.checkRegister();
        if(200 != checkRegisterResult.getCode()) {
            return Mono.error(new UnRegisteredException(null!=checkRegisterResult.getMessage()?checkRegisterResult.getMessage():"注册授权文件异常，请重新注册！"));
        }else {
            warnDay = checkRegisterResult.getMessage();
        }
        SysUserFeignService sysUserFeignService = SpringContextHelper.getBean(SysUserFeignService.class);
        ApiResultSet<SysLogin> sysLoginResult = sysUserFeignService.getSysLoginByAccount(username);
        if (sysLoginResult.getData() == null) {
            sysLoginResult = sysUserFeignService.getSysLoginByUserOidForEeds(username);
            sysLoginResult.getData().setPassword("nm-eeds-form-123");
        }

        if(sysLoginResult.getCode()==ApiResultSet.SUCCESS && sysLoginResult.getData() != null){
            SysLogin sysLogin = sysLoginResult.getData();
            AuthUserDetails user=new AuthUserDetails();
            Date passwordDate = sysLogin.getPasswordDate();
            if (passwordDate != null){
                long l = DateUtil.between(passwordDate, new Date(), DateUnit.DAY);
                if (l > 30){
                    user.setPsFlag(BaseStaticParameter.Y);
                }
            }
            user.setWarnDay(warnDay);
            user.setAccount(sysLogin.getAccount());
            user.setPassword(sysLogin.getPassword());
            user.setOid(sysLogin.getLoginOid());
//            user.setRoles(new ArrayList<>());
            user.setUserOid(sysLogin.getUserOid());
            user.setLockStatus(sysLogin.getLockStatus());
            user.setIsAble(sysLogin.getIsAble());
            ApiResultSet<SysUser> sysUserResult= sysUserFeignService.getSysUserByUserOid(sysLogin.getUserOid());
            if(sysUserResult.getCode()==ApiResultSet.SUCCESS){
                SysUser sysUser = sysUserResult.getData();
                //判断检查用户所在区划是否注册
                ApiResultSet resultSet = registerFeignService.checkUserDistrictCodeRegister(sysUser.getDistrictOid());
                if(200!=resultSet.getCode()){
                    return Mono.error(new UnRegisteredException(null!=resultSet.getMessage()?resultSet.getMessage():"用户区划未授权，登录失败！"));
                }
                user.setDistrictOid(sysUser.getDistrictOid());
                user.setOrganOid(sysUser.getOrganOid());
                //测试性能需要注释
                if(StrUtil.isNotBlank(sysUser.getType())){
                    SysDictFeignService sysDictFeignService = SpringContextHelper.getBean(SysDictFeignService.class);
                    SysDict sysDict = sysDictFeignService.getSysDictByDictOid(sysUser.getType()).getData();
                    user.setUserCode(null!=sysDict?sysDict.getCode():null);
                }
                //用源字符编码解码字符串
                try {
                    /*if(StrUtil.isNotEmpty(sysUser.getName())){
                        String userName = URLEncoder.encode(sysUser.getName(),"UTF-8");
                        user.setUsername(userName);
                    }
                    if(StrUtil.isNotEmpty(sysUser.getOrganName())){
                        String organName = URLEncoder.encode(sysUser.getOrganName(),"UTF-8");
                        user.setOrganName(organName);
                    }
                    if(StrUtil.isNotEmpty(sysUser.getDistrictName())){
                        String districtName = URLEncoder.encode(sysUser.getDistrictName(),"UTF-8");
                        user.setDistrictName(districtName);
                    }*/
                    user.setUsername(sysUser.getName());
                    user.setOrganName(sysUser.getOrganName());
                    user.setDistrictName(sysUser.getDistrictName());
                }catch (Exception e){
                    //编码异常暂不做处理
                }
                user.setAdviStatus(sysUser.getAdviStatus());
                user.setDataAuthority(null==sysUser.getDataAuthority()?1:sysUser.getDataAuthority());
            }
            //List<SysRole> roles = sysUserFeignService.getRolesByLoginId(sysLogin.getLoginOid()).getData();
            //List<String> roleNames = roles.stream().map(SysRole::getName).collect(Collectors.toList());
            //user.setRoles(roleNames);

            //登录成功后，判断是否可以重复登录
            //测试性能需要注释
            SysConfigFeignService sysConfigFeignService = SpringContextHelper.getBean(SysConfigFeignService.class);
            ApiResultSet<SysConfig> configApiResultSet = sysConfigFeignService.getSysConfigByCode("LOGIN_REPEAT");
            if(200 == configApiResultSet.getCode()){
                SysConfig sysConfig = configApiResultSet.getData();
                //重复登录开关，默认允许重复登录，为N时不允许重复登录，其他值允许重复登录
                if(BaseStaticParameter.NO.equals(sysConfig.getValue())){
                    //清除其他登录session
                    Set<String> keys = redisTemplate.keys("platform:sessions:"+user.getUserOid()+":" + "*");
                    if(keys.size()>0)
                    redisTemplate.delete(keys);
                }
            }
            //设置当前登录人
            CurrentLoginUserHolder.loginUserOid = user.getUserOid();
            return Mono.just(user);
        }
        return Mono.error(new BadCredentialsException(sysLoginResult.getMessage()));
    }

    /**
     * 密码加密
     * @param password
     * @return
     */
    private String encodePswd(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(4);
        String encodedPassword = encoder.encode(password);
        return encodedPassword;
    }

    /**
     * 获取用户信息
     * @param username 用户名
     * @return 用户信息
     */
    private Mono<SysUser> getSysUserInfo(String username){
        SysUser sysUser = new SysUser();
        Mono<SysUser> userMono = Mono.just(sysUser);
        return userMono;
    }
}
