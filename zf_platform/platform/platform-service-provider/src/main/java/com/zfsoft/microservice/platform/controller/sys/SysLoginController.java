package com.zfsoft.microservice.platform.controller.sys;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.zfsoft.microservice.platform.data.sys.*;
import com.zfsoft.microservice.platform.data.vo.SysLoginVo;
import com.zfsoft.microservice.platform.feign.settings.SysConfigFeignService;
import com.zfsoft.microservice.platform.manager.sys.*;
import com.zfsoft.microservice.platform.service.sys.SysLoginService;
import com.zfsoft.microservice.settings.data.SysConfig;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.validate.ParamValidException;
import com.zfsoft.platform.utils.web.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @ClassName SysLoginServiceController
 * @Description
 * @Author
 * @Date2020-09-11 17:29
 * @Version V1.0
 **/
@RestController
@Slf4j
public class SysLoginController implements  SysLoginService{

    @Autowired
    private SysPermissionManager sysPermissionManager;

    @Autowired
    private SysLoginManager sysLoginManager;

    @Autowired
    private SysUserManager sysUserManager;

    @Autowired
    private SysLoginRoleManager sysLoginRoleManager;

    @Autowired
    private SysAppManager sysAppManager;

    @Autowired
    private SysConfigFeignService sysConfigFeignService;

    @Autowired
    private RedisTemplate redisTemplate;

    public ApiResultSet<SysLogin> initSysLoginById(Long oid) {
        SysLogin sysLogin = sysLoginManager.getSysLoginById(oid);
        return new ApiResultSet(sysLogin);
    }


    @Override
    public ApiResultSet<SysLogin> getSysLoginByAccount(String account) {
        ApiResultSet<SysLogin> ret = new ApiResultSet<>();
        SysLogin sysLogin = sysLoginManager.getSysLoginByAccount(account);
        if (sysLogin == null) {
            ret.setMessage("用户名或密码错误");
        }
        ret.setData(sysLogin);
        return ret;
    }

    @Override
    public ApiResultSet<SysLogin> getSysLoginByUserOidForEeds(String userOid) {
        ApiResultSet<SysLogin> ret = new ApiResultSet<>();
        SysLogin sysLoginByUserOid = sysLoginManager.getSysLoginByUserOid(userOid);
        SysLogin sysLogin = sysLoginManager.getSysLoginByAccount(sysLoginByUserOid.getAccount());
        if (sysLogin == null) {
            ret.setMessage("用户名或密码错误");
        }
        ret.setData(sysLogin);
        return ret;
    }

    public ApiResultSet<SysLogin> getSysLoginById( Long oid) {
        SysLogin sysLogin = sysLoginManager.getSysLoginById(oid);
        return new ApiResultSet(sysLogin);
    }

    @Override
    public ApiResultSet<SysLogin> getSysLoginByUserOid(String userOid) {
        SysLogin sysLogin = sysLoginManager.getSysLoginByUserOid(userOid);
        return new ApiResultSet(sysLogin);
    }


    public ApiResultSet<SysLoginVo> getOneVo(Long oid) {
        SysLoginVo sysLogin = sysLoginManager.getSysLoginVoById(oid);
        return new ApiResultSet(sysLogin);
    }

    public ApiResultSet page(String districtOid,String organOid,String account,String userName,Integer lockStatus, Integer pageNum, Integer pageSize) {

        SysLoginVo sysLoginVo = new SysLoginVo();
        sysLoginVo.setDistrictOid(districtOid);
        sysLoginVo.setOrganOid(organOid);
        sysLoginVo.setAccount(account);
        if(null==lockStatus){
            lockStatus = BaseStaticParameter.N;
        }
        sysLoginVo.setLockStatus(lockStatus);
        sysLoginVo.setUserName(userName);
        PageResult<SysLoginVo> sysLoginPageResult = sysLoginManager.querySysLoginVoWithPage(sysLoginVo, pageNum, pageSize);
        return new ApiResultSet(sysLoginPageResult);
    }
    public ApiResultSet save(SysLogin sysLogin) {
        sysLoginManager.saveSysLogin(sysLogin);
        return new ApiResultSet();
    }

    @Override
    public ApiResultSet updatePassword(SysLoginVo sysLoginVo) {

        SysConfig sysConfig = sysConfigFeignService.getSysConfigByCode(BaseStaticParameter.CONFIG_LOGIN_ENCRYPT).getData();
        CurrentLoginUser currentLoginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        String privateKeyBase64 = (String)redisTemplate.opsForValue().get("rsa:encrypt:" + currentLoginUser.getClientId());
        if(sysConfig!=null&&"1".equals(sysConfig.getValue())){
            if(StrUtil.isBlank(privateKeyBase64)){
                throw new ParamValidException("页面等待时间过长，请刷新后重试！");
            }
            RSA rsa = SecureUtil.rsa(privateKeyBase64, null);
            if(StrUtil.isNotBlank(sysLoginVo.getOldPassword())){
                sysLoginVo.setOldPassword(rsa.decryptStr(sysLoginVo.getOldPassword(), KeyType.PrivateKey));
            }
            if(StrUtil.isNotBlank(sysLoginVo.getPassword())){
                sysLoginVo.setPassword(rsa.decryptStr(sysLoginVo.getPassword(), KeyType.PrivateKey));
            }
            if(StrUtil.isNotBlank(sysLoginVo.getConfirmPassword())){
                sysLoginVo.setConfirmPassword(rsa.decryptStr(sysLoginVo.getConfirmPassword(), KeyType.PrivateKey));
            }
        }
        /*String oid = CurrentLoginUserHolder.getCurrentLoginUser().getLoginOid();
        sysLoginVo.setLoginOid(oid);
        sysLoginManager.updatePassword(sysLoginVo.getOldPassword(),sysLoginVo.getPassword(), sysLoginVo.getConfirmPassword(),sysLoginVo.getLoginOid());*/
        sysLoginManager.updatePassword(sysLoginVo.getOldPassword(),sysLoginVo.getPassword(), sysLoginVo.getConfirmPassword(),sysLoginVo.getId(), sysLoginVo.getUserOid());
        return new ApiResultSet();
    }
    /**
     * 启禁用登录信息
     *
     * @param oid 主键
     * @return
     * @author zxx
     * @date 2020/9/15 3:33 下午
     */
    public ApiResultSet able(Long oid) {
        sysLoginManager.ableSysLogin(oid);
        return new ApiResultSet();
    }

    /**
     * 删除登录信息
     *
     * @param oid 主键
     * @return
     * @author zxx
     * @date 2020/9/15 3:33 下午
     */
    public ApiResultSet delete(Long oid) {
        sysLoginManager.deleteSysLogin(oid);
        return new ApiResultSet();
    }

    /**
     * 获取路由
     * @author zxx
     * @date 2020/9/18 8:57 上午
     * @return
     */
    public ApiResultSet getRouters(String appOid){
        CurrentLoginUser currentLoginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        List<SysPermission> routersPermission = sysPermissionManager.getRoutersPermission(currentLoginUser.getLoginOid(),appOid);
        //List<SysPermission> routersPermission = sysPermissionService.getRoutersPermission(null,null);
        return new ApiResultSet(sysPermissionManager.buildMenus(routersPermission));
    }


    /**
     * 获取公钥
     * @author zxx
     * @date 2020/9/30 11:30 上午
     * @return
     */
    public ApiResultSet getPublicKey(){
        Map<String,Object> map=new HashMap<>();
        CurrentLoginUser currentLoginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        RSA rsa = SecureUtil.rsa();
        String privateKeyBase64 = rsa.getPrivateKeyBase64();
        String publicKeyBase64 = rsa.getPublicKeyBase64();
        SysConfig sysConfig = sysConfigFeignService.getSysConfigByCode(BaseStaticParameter.CONFIG_LOGIN_ENCRYPT).getData();
        redisTemplate.opsForValue().set("rsa:encrypt:"+currentLoginUser.getClientId(),privateKeyBase64);
        redisTemplate.expire("rsa:encrypt:"+currentLoginUser.getClientId(),30, TimeUnit.MINUTES);

        map.put("config",sysConfig.getValue());
        map.put(BaseStaticParameter.PUBLIC_KEY, publicKeyBase64);
        return new ApiResultSet(map);
    }

    /**
     * 获取用户信息
     * @return
     */
    public ApiResultSet getUserInfo(){
        Map<String,Object> map=new HashMap<>();
        CurrentLoginUser currentLoginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        SysUser user = sysUserManager.getSysUserByUserOid(currentLoginUser.getUserOid());
        // modify by kkfan  质控 董秀丽 要求给修改不脱敏
//        if(null!=user){
//            user.setDataAuthority(null==user.getDataAuthority()?1:user.getDataAuthority());
//            user.setPassword(null);
//            user.setMobile(CommonUtil.mobileEncode(user.getMobile()));
//            user.setCardNo(CommonUtil.idCardEncode(user.getCardNo()));
//            user.setEmail(CommonUtil.emailEncode(user.getEmail()));
//            user.setTelphone(CommonUtil.phoneEncode(user.getTelphone()));
//        }
        map.put("user",user);
        //权限、角色等
        SysLogin login = sysLoginManager.getSysLoginByUserOid(user.getUserOid());
        List<String> permissions = sysPermissionManager.getPermissionStrList(login.getLoginOid(), null);
        map.put("permissions",permissions);
        SysLoginRole sysLoginRole = new SysLoginRole();
        sysLoginRole.setLoginOid(login.getLoginOid());
        List<SysLoginRole> sysLoginRoles = sysLoginRoleManager.querySysLoginRole(sysLoginRole);
        Set<String> appOidSet = new HashSet<>();
        Set<String> roleList=sysLoginRoles.stream().map(loginRoles -> {
            appOidSet.add(loginRoles.getAppOid());
            return loginRoles.getRoleOid();
        }).collect(Collectors.toSet());
        map.put("roles",roleList);

        //所属应用
        JSONArray jsonArray = new JSONArray();
        List <SysApp> sysAppList = new ArrayList<>();
        for (String appOidRoles :appOidSet){
            SysApp app = sysAppManager.getSysAppByAppOid(appOidRoles);
            if(BaseStaticParameter.Y==app.getIsDelete() || BaseStaticParameter.N==app.getIsAble()){
                continue;
            }
            sysAppList.add(app);
        }
        if(null!=sysAppList && sysAppList.size()>0){
            sysAppList.sort((SysApp o1, SysApp o2)->o1.getSort().compareTo(o2.getSort()));
        }
        for (SysApp app:sysAppList){
            JSONObject jsonObject = JSONUtil.createObj().set("oid", app.getAppOid()).set("name",app.getDisplayName())
                    .set("iconName",app.getIconName());
            jsonArray.add(jsonObject);
        }
        map.put("appList",jsonArray);
        String currentAppOid = null!=sysAppList&& sysAppList.size()>0?sysAppList.get(0).getAppOid():"";
        //获取第一个app
        map.put("fristAppOid",currentAppOid);
        return new ApiResultSet(map);
    }

    /**
     * 锁定账户
     * @author zxx
     * @date 2020/10/9 4:41 下午
     * @param account
     * @return
     */
    public ApiResultSet lockSysLogin(String account) {
        SysLogin sysLogin = sysLoginManager.getSysLoginByAccount(account);
        SysConfig sysConfig = sysConfigFeignService.getSysConfigByCode("LOGIN_LOCK_NUM").getData();
        if(sysLogin!=null&&sysConfig!=null){
            int i = Integer.parseInt(sysConfig.getValue());
            Integer num = sysLogin.getFailNum()+1;
            if(num >= i){
                sysLogin.setLockStatus(BaseStaticParameter.Y);
                sysLogin.setLockDate(new Date());
            }
            sysLogin.setFailNum(num);
            sysLoginManager.updateByPrimaryKey(sysLogin);
        }
        return new ApiResultSet();
    }

   /**
    * @description:  解锁账户
    * @param oid 登录主键
    * @author: wuxx
    * @Date: 2020/10/22 11:58
    **/
    public ApiResultSet delockSysLogin(Long oid) {
        SysLogin sysLogin = sysLoginManager.getSysLoginById(oid);
        if(sysLogin!=null){
            sysLogin.setFailNum(0);
            sysLogin.setLockStatus(BaseStaticParameter.N);
            sysLogin.setLockDate(null);
            sysLoginManager.updateByPrimaryKey(sysLogin);
        }
        return new ApiResultSet();
    }

    /**
     * 清空失败次数
     *
     * @param account 登录名
     * @return ApiResultSet
     * @author ningzz
     * @Date 2020/11/9
     **/
    @Override
    public ApiResultSet emptyFailNum(String account) {
        SysLogin sysLogin = sysLoginManager.getSysLoginByAccount(account);
        if (sysLogin != null) {
            sysLogin.setFailNum(0);
            sysLoginManager.updateByPrimaryKey(sysLogin);
        }
        return new ApiResultSet();
    }


    //找回密码部分

    /**
     * 获取公钥
     * @author zxx
     * @date 2020/9/30 11:30 上午
     * @return
     */
    @GetMapping("/findPassword/getFindPasswordPublicKey")
    public ApiResultSet getFindPasswordPublicKey(){
        Map<String,Object> map=new HashMap<>();
        RSA rsa = SecureUtil.rsa();
        String privateKeyBase64 = rsa.getPrivateKeyBase64();
        String publicKeyBase64 = rsa.getPublicKeyBase64();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        SysConfig sysConfig = sysConfigFeignService.getSysConfigByCode(BaseStaticParameter.CONFIG_LOGIN_ENCRYPT).getData();
        redisTemplate.opsForValue().set("rsa:encrypt:"+request.getSession().getId(),privateKeyBase64);
        redisTemplate.expire(request.getSession().getId(),30, TimeUnit.MINUTES);
        map.put("config",sysConfig.getValue());
        map.put(BaseStaticParameter.PUBLIC_KEY, publicKeyBase64);
        return new ApiResultSet(map);
    }

    /**
     * @description: 验证找回密码的身份证和手机号
     * @author: wuxx
     * @Date: 2020/11/24 13:07
     **/
    @PostMapping("/findPassword/checkCardNoAndMobile")
    public ApiResultSet checkCardNoAndMobile(String cardNo,String mobile,String userOid) {
        ApiResultSet apiResultSet = new ApiResultSet();
        SysUser sysUser = new SysUser();
        sysUser.setMobile(mobile);
        sysUser.setCardNo(cardNo);
        sysUser.setUserOid(userOid);
        List<SysUser> users = sysUserManager.querySysUserNoLogin(sysUser);
        if (null!=users && users.size()>0){

            //安全测试提 userOid传给前端，前端在下一步更新密码时把userOid回传存在安全问题。
            // 实际这里是找回密码，除非知道数据库里的用户名才会有安全问题，既然提了就暂时放在session中，更新密码再从session中取
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            HttpSession session = request.getSession();
            session.setAttribute("UserOid",userOid);

            apiResultSet.setCode(ApiResultSet.SUCCESS);
            return apiResultSet;
        }
        apiResultSet.setCode(ApiResultSet.PARAM_VALID_ERROR);
        apiResultSet.setMessage("请输入正确的身份证号和手机号！");
        return apiResultSet;
    }

    /**
     * @description: 验证找回密码的更新
     * @author: wuxx
     * @Date: 2020/11/24 13:07
     **/
    @PostMapping( value = "/findPassword/updatePassword")
    public ApiResultSet updateFindPassword(@RequestBody SysLoginVo sysLoginVo) {
        SysConfig sysConfig = sysConfigFeignService.getSysConfigByCode(BaseStaticParameter.CONFIG_LOGIN_ENCRYPT).getData();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String privateKeyBase64 = (String)redisTemplate.opsForValue().get("rsa:encrypt:" + request.getSession().getId());
        if(sysConfig!=null&&"1".equals(sysConfig.getValue())){
            if(StrUtil.isBlank(privateKeyBase64)){
                throw new ParamValidException("页面等待时间过长，请刷新后重试！");
            }
            RSA rsa = SecureUtil.rsa(privateKeyBase64, null);
            if(StrUtil.isNotEmpty(sysLoginVo.getOldPassword())){
                sysLoginVo.setOldPassword(rsa.decryptStr(sysLoginVo.getOldPassword(), KeyType.PrivateKey));
            }else {
                if(StrUtil.isNotEmpty(sysLoginVo.getPrevious())){
                    sysLoginVo.setOldPassword(sysLoginVo.getPrevious());
                }
            }
            if(StrUtil.isNotEmpty(sysLoginVo.getPassword())){
                sysLoginVo.setPassword(rsa.decryptStr(sysLoginVo.getPassword(), KeyType.PrivateKey));
            }
            if(StrUtil.isNotEmpty(sysLoginVo.getConfirmPassword())){
                sysLoginVo.setConfirmPassword(rsa.decryptStr(sysLoginVo.getConfirmPassword(), KeyType.PrivateKey));
            }
        }else {
            if(StrUtil.isNotEmpty(sysLoginVo.getPrevious())){
                sysLoginVo.setOldPassword(sysLoginVo.getPrevious());
            }
        }
        //从session中取，先过安全测试要求
        String userOid = (String) request.getSession().getAttribute("UserOid");
        request.getSession().removeAttribute("UserOid");

        sysLoginManager.updatePasswordLogin(sysLoginVo.getOldPassword(),sysLoginVo.getPassword(), sysLoginVo.getConfirmPassword(),sysLoginVo.getId(), userOid);
        SysUser user = sysUserManager.getSysUserByUserOid(userOid);
        return new ApiResultSet(null!=user?user.getName():"");
    }
}
