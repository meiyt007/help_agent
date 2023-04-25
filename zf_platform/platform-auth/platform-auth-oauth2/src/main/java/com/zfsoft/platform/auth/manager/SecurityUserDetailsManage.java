package com.zfsoft.platform.auth.manager;


import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.zfsoft.microservice.platform.data.sys.SysLogin;
import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.microservice.settings.data.SysDict;
import com.zfsoft.platform.auth.exception.UnRegisteredException;
import com.zfsoft.platform.auth.feign.RegisterFeignService;
import com.zfsoft.platform.auth.feign.SysDictFeignService;
import com.zfsoft.platform.auth.feign.SysUserFeignService;
import com.zfsoft.platform.auth.service.SecurityUserDetailsService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.security.data.AuthUserDetails;
import com.zfsoft.platform.utils.spring.SpringContextHelper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 用户登录验证逻辑相关操作
 * @author kkfan
 * @date 2020-02-07
 */
@Service
public class SecurityUserDetailsManage implements SecurityUserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        String warnDay = null;
        //验证是否注册授权
        RegisterFeignService registerFeignService = SpringContextHelper.getBean(RegisterFeignService.class);
        ApiResultSet checkRegisterResult = registerFeignService.checkRegister();
        if(200 != checkRegisterResult.getCode()) {
            throw new UnRegisteredException(null!=checkRegisterResult.getMessage()?checkRegisterResult.getMessage():"注册授权文件异常，请重新注册！");
        }else {
            warnDay = checkRegisterResult.getMessage();
        }
        SysUserFeignService sysUserFeignService = SpringContextHelper.getBean(SysUserFeignService.class);
        ApiResultSet<SysLogin> sysLoginResult = sysUserFeignService.getSysLoginByAccount(username);
        if(sysLoginResult.getCode()==ApiResultSet.SUCCESS && sysLoginResult.getData() != null){
            SysLogin sysLogin = sysLoginResult.getData();

            AuthUserDetails user=new AuthUserDetails();
            Date passwordDate = sysLogin.getPasswordDate();
            if (passwordDate != null){
                long l = DateUtil.between(passwordDate, new Date(), DateUnit.DAY);
                if (l > 30){
                    user.setPsFlag(BaseStaticParameter.Y);
                } else {
                    user.setPsFlag(BaseStaticParameter.N);
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
            ApiResultSet<SysUser> sysUserResult = sysUserFeignService.getSysUserByUserOid(sysLogin.getUserOid());

            if(sysLogin.getLockStatus() == 1) {
                throw new UnRegisteredException("账户已锁定，请联系管理员！");
            }

            if(sysLogin.getIsAble() == 0) {
                throw new UnRegisteredException("账户被禁用，请联系管理员！");
            }

            if(sysUserResult.getCode()==ApiResultSet.SUCCESS){
                SysUser sysUser = sysUserResult.getData();
                //判断检查用户所在区划是否注册
                ApiResultSet resultSet = registerFeignService.checkUserDistrictCodeRegister(sysUser.getDistrictOid());
                if(200!=resultSet.getCode()){
                    throw new UnRegisteredException(null!=resultSet.getMessage()?resultSet.getMessage():"用户区划未授权，登录失败！");
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
//            List<SysRole> roles = sysUserFeignService.getRolesByLoginId(sysLogin.getLoginOid()).getData();
//            List<String> roleNames = roles.stream().map(SysRole::getName).collect(Collectors.toList());
//            user.setRoles(roleNames);
            return user;
        }
        throw new BadCredentialsException(sysLoginResult.getMessage());

    }
}
