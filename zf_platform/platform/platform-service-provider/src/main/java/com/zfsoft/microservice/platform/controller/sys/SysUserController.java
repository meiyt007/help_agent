package com.zfsoft.microservice.platform.controller.sys;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.json.JSONUtil;
import com.zfsoft.microservice.platform.data.sys.SysLoginRole;
import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.microservice.platform.data.vo.SysUserVo;
import com.zfsoft.microservice.platform.manager.sys.SysUserManager;
import com.zfsoft.microservice.platform.service.sys.SysUserService;
import com.zfsoft.microservice.platform.util.GenDataTreeUtil;
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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户controller
 *
 * @author zxx
 * @date 2020/9/15 8:13 下午
 */
@RestController
@Slf4j
public class SysUserController implements SysUserService {

    @Autowired
    private SysUserManager sysUserManager;

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * @description:  修改初始化用户信息
     * @param id
     * @author: wuxx
     * @Date: 2020/10/30 10:47
     **/
    @GetMapping(value = "/sysuser/init/{id}")
    public ApiResultSet<SysUser> initSysUserById(@PathVariable("id") Long id) {
        SysUser user = sysUserManager.getSysUserById(id);
        return new ApiResultSet(user);
    }


    /**
     * 获取用户信息
     *
     * @param id 主键
     * @return
     * @author zxx
     * @date 2020/9/15 8:15 下午
     */
    @GetMapping(value = "/sysuser/{id}")
    public ApiResultSet<SysUser> getSysUserById(@PathVariable("id") Long id) {
        SysUser user = sysUserManager.getSysUserById(id);
        if(null!=user){
            user.setDataAuthority(null==user.getDataAuthority()?1:user.getDataAuthority());
            user.setPassword(null);
            user.setMobile(CommonUtil.mobileEncode(user.getMobile()));
            user.setCardNo(CommonUtil.idCardEncode(user.getCardNo()));
            user.setEmail(CommonUtil.emailEncode(user.getEmail()));
            user.setTelphone(CommonUtil.phoneEncode(user.getTelphone()));
        }
        return new ApiResultSet(user);
    }

    @Override
    public ApiResultSet<SysUser> getSysUserByUserOid(String userOid) {
        SysUser user =  sysUserManager.getSysUserByUserOid(userOid);
        if(null!=user){
            user.setAdviStatus(null==user.getAdviStatus()?0:user.getAdviStatus());
            user.setDataAuthority(null==user.getDataAuthority()?1:user.getDataAuthority());
            user.setPassword(null);
            /*user.setMobile(CommonUtil.mobileEncode(user.getMobile()));
            user.setCardNo(CommonUtil.idCardEncode(user.getCardNo()));
            user.setEmail(CommonUtil.emailEncode(user.getEmail()));
            user.setTelphone(CommonUtil.phoneEncode(user.getTelphone()));*/
        }
        return new ApiResultSet(user);
    }

    @Override
    public ApiResultSet<List<SysUser>> getSysUserListByOrganOid(String organOid) {
        List<SysUser> sysUserList = sysUserManager.getSysUserListByOrganOid(organOid);
        return new ApiResultSet(sysUserList);
    }

    /**
     * 分页查询用户信息
     *
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return ApiResultSet
     * @author zxx
     * @date 2020/9/15 8:15 下午
     */
    @Override
    public ApiResultSet queryWithPage(String districtOid, String organOid, String name, Integer isAble, Integer pageNum, Integer pageSize) {
        SysUser sysUser = new SysUser();
        sysUser.setDistrictOid(districtOid);
        sysUser.setOrganOid(organOid);
        sysUser.setName(name);
        sysUser.setIsAble(isAble);
        PageResult<SysUser> sysUserPageResult = sysUserManager.querySysUserWithPage(sysUser, pageNum, pageSize);
        return new ApiResultSet(sysUserPageResult);
    }

    /**
     * 分页查询用户信息
     *
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return ApiResultSet
     * @author wuxx
     * @date 2021/1/6 10:15
     */
    @GetMapping(value = "/sysuser/queryWithPage")
    public ApiResultSet queryWithMapPage(String districtOid, String organOid,String name, Integer isAble, Integer pageNum, Integer pageSize) {
        SysUser sysUser = new SysUser();
        sysUser.setDistrictOid(districtOid);
        sysUser.setOrganOid(organOid);
        sysUser.setName(name);
        sysUser.setIsAble(isAble);
        PageResult<SysUser> sysUserPageResult = sysUserManager.querySysUserWithPage(sysUser, pageNum, pageSize);
        Map<String, Object> map = new HashMap<>();
        map.put("sysUserPageResult",sysUserPageResult);
        List<Map<String, Object>> mapList = new ArrayList<>();
        for(Map.Entry<Integer, String> entry :  BaseStaticParameter.DATA_AUTHORITY_MAP.entrySet()){
            Map<String, Object> map1 = new HashMap<>();
            map1.put("id",entry.getKey());
            map1.put("value",entry.getValue());
            mapList.add(map1);
        }
        map.put("dataDuthorityMap",mapList);
        return new ApiResultSet(map);
    }

    /**
     * 根据岗位oid分页查询用户信息
     *
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return ApiResultSet
     * @author wuxx
     * @date 2021/1/22 10:15
     */
    @GetMapping(value = "/sysuser/queryUserPostPage")
    public ApiResultSet queryUserPostPage(String districtOid, String organOid, String postOid, String name,Integer pageNum, Integer pageSize) {
        SysUser sysUser = new SysUser();
        sysUser.setDistrictOid(districtOid);
        sysUser.setOrganOid(organOid);
        sysUser.setName(name);
        sysUser.setPostOids(postOid);
        PageResult<SysUser> sysUserPageResult = sysUserManager.queryUserPostPage(sysUser, pageNum, pageSize);
        /*Map<String, Object> map = new HashMap<>();
        map.put("sysUserPageResult",sysUserPageResult);
        List<Map<String, Object>> mapList = new ArrayList<>();
        for(Map.Entry<Integer, String> entry :  BaseStaticParameter.DATA_AUTHORITY_MAP.entrySet()){
            Map<String, Object> map1 = new HashMap<>();
            map1.put("id",entry.getKey());
            map1.put("value",entry.getValue());
            mapList.add(map1);
        }
        map.put("dataDuthorityMap",mapList);*/
        return new ApiResultSet(sysUserPageResult);
    }
    /**
     * 保存用户信息
     *
     * @param sysUser 用户信息
     * @return
     * @author zxx
     * @date 2020/9/15 8:16 下午
     */
    public ApiResultSet save(SysUser sysUser) {
        sysUserManager.saveSysUser(sysUser);
        return new ApiResultSet();
    }

    /**
     * @description: 数据加密保存用户信息
     * @param sysUserEncrypt 加密的字符串
     * @author: wuxx
     * @Date: 2020/11/25 11:18
     **/
    @PostMapping(value = "/sysuser/saveEncrypt")
    public ApiResultSet saveEncrypt(String sysUserEncrypt,String formObjectJson) {
        CurrentLoginUser currentLoginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        String privateKeyBase64 = (String)redisTemplate.opsForValue().get("rsa:encrypt:" + currentLoginUser.getClientId());
        if(StrUtil.isNotEmpty(sysUserEncrypt) && StrUtil.isNotEmpty(formObjectJson) ){
            RSA rsa = SecureUtil.rsa(privateKeyBase64, null);
            String decryptStr = rsa.decryptStr(sysUserEncrypt, KeyType.PrivateKey);
            SysUser sysUserDecrypt = JSONUtil.toBean(decryptStr, SysUser.class);
            SysUser sysUser = JSONUtil.toBean(formObjectJson, SysUser.class);
            if(StrUtil.isBlank(sysUser.getUserOid())){
                sysUser.setPassword(rsa.decryptStr(sysUser.getPassword(), KeyType.PrivateKey));
            }
            if(null!=sysUserDecrypt && null!=sysUser){
                sysUser.setTelphone(sysUserDecrypt.getTelphone());
                sysUser.setCardNo(sysUserDecrypt.getCardNo());
                sysUser.setMobile(sysUserDecrypt.getMobile());
                sysUser.setEmail(sysUserDecrypt.getEmail());
                sysUserManager.saveSysUser(sysUser);
            }else {
                throw new ParamValidException("用户信息不正确！");
            }
        }else {
            throw new ParamValidException("用户信息不正确！");
        }
        return new ApiResultSet();
    }

    /**
     * @description: 修改用户信息，只修改有值的
     * @param sysUser 用户信息
     * @author: wuxx
     * @Date: 2020/10/31 15:13
     **/
    @Override
    public ApiResultSet updateSysUser(SysUser sysUser) {
        sysUserManager.updateSysUser(sysUser);
        return new ApiResultSet();
    }

    /**
     * 启禁用用户信息
     *
     * @param id 主键
     * @return
     * @author zxx
     * @date 2020/9/15 8:16 下午
     */
    @Override
    public ApiResultSet able(Long id) {
        int i = sysUserManager.ableSysUser(id);
        if (BaseStaticParameter.Y == i){
            return new ApiResultSet();
        }else {
            return new ApiResultSet(ApiResultSet.DIRTY_DATA_TITLE,"禁用失败，该用户的登录账户为启用状态！",null);
        }
    }

    /**
     * 删除用户信息
     *
     * @param oid 主键
     * @return
     * @author zxx
     * @date 2020/9/15 8:16 下午
     */
    @Override
    public ApiResultSet delete(Long oid) {
        int i = sysUserManager.deleteSysUser(oid);
        if (BaseStaticParameter.Y == i){
            return new ApiResultSet();
        }else {
            return new ApiResultSet(ApiResultSet.DIRTY_DATA_TITLE,"删除失败，该用户的登录账户为启用状态！",null);
        }
    }

    @Override
    public ApiResultSet<SysUser> queryWithPageBySysUser(SysUser sysUser, Integer pageNum, Integer pageSize) {
        PageResult<SysUser> sysUserPageResult = sysUserManager.querySysUserWithPage(sysUser, pageNum, pageSize);
        return new ApiResultSet(sysUserPageResult);
    }

    /**
     * 获取用户的角色
     *
     * @param userOid 主键
     * @return
     * @author zxx
     * @date 2020/9/24 2:56 下午
     */
    public ApiResultSet getUserRole(String userOid) {
        List<SysLoginRole> userRole = sysUserManager.getUserRoleByUserOid(userOid);
        return new ApiResultSet(userRole);
    }

    /**
     * 保存用户授权
     *
     * @param sysUser 用户
     * @return
     * @author zxx
     * @date 2020/9/24 6:52 下午
     */
    public ApiResultSet saveUserRole(SysUser sysUser) {
        sysUserManager.saveUserRole(sysUser.getUserOid(), sysUser.getRoleOids());
        return new ApiResultSet();
    }

    /**
     * 根据机构获取用户树
     *
     * @param organOid 机构主键
     * @return
     * @author zxx
     * @date 2020/9/24 6:53 下午
     */
    public ApiResultSet queryUserTreeByOrganOid(String organOid, boolean disable) {
        if (organOid == null) {
            return new ApiResultSet();
        }
        SysUser sysUser = new SysUser();
        sysUser.setOrganOid(organOid);
        List<SysUser> sysUserList = sysUserManager.querySysUser(sysUser);
        if (sysUserList == null || sysUserList.size() == 0) {
            return new ApiResultSet();
        }
        return new ApiResultSet(GenDataTreeUtil.buildUserTreeSelect(sysUserList, disable));
    }

    /**
     * 更新用户头像
     * @author zxx
     * @date 2020/9/27 4:22 下午
     * @param imageOid
     * @return
     */
    public ApiResultSet updateUserAvatar(String imageOid) {
        CurrentLoginUser currentLoginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        sysUserManager.updateUserAvatar(currentLoginUser.getUserOid(), imageOid);
        return new ApiResultSet();
    }

    /**
     * @description: 根据岗位oid获取用户信息
     * @param postOid 岗位oid
     * @author: wuxx
     * @Date: 2021/1/22 10:34
     **/
    @Override
    public ApiResultSet<List<SysUser>> getSysUserListByPostOid(String postOid) {
        return new ApiResultSet(sysUserManager.getSysUserListByPostOid(postOid));
    }

    @Override
    public ApiResultSet<List<SysUser>> getSysUserListByPostOids(String postOids) {
        return new ApiResultSet(sysUserManager.getSysUserListByPostOids(postOids));
    }

    @Override
    public ApiResultSet<List<SysUser>> getSysUserListByRoleOids(String roleOids) {
        return new ApiResultSet(sysUserManager.getSysUserListByRoleOids(roleOids));
    }

    @Override
    public ApiResultSet<List<SysUser>> getSysUserListByOrganOids(String organOids) {
        return new ApiResultSet(sysUserManager.getSysUserListByOrganOids(organOids));
    }

    @Override
    public ApiResultSet<List<SysUserVo>> getAllUserVoList() {
        return new ApiResultSet(sysUserManager.getAllUserVoList());
    }

    /**
    * @description:  更新用户的皮肤
    * @param userOid
    * @param skinClassname
    * @author: wuxx
    * @Date: 2020/10/31 14:23
    **/
    @PostMapping(value = "/sysuser/updateUserSkinClassname")
    public ApiResultSet updateUserSkinClassname(@RequestParam("userOid") String userOid, @RequestParam("skinClassname") String skinClassname) {
        sysUserManager.updateUserSkinClassname(userOid, skinClassname);
        return new ApiResultSet();
    }

    @Override
    public ApiResultSet changeStatisticTypes(String statisticTypes) {
        this.sysUserManager.changeStatisticTypes(statisticTypes);
        return new ApiResultSet();
    }

    public static void main(String[] args) {
        RSA rsa = SecureUtil.rsa(null, "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCqLPZPPfhQo9RPwWmDYTtmRD0XkauYzKm0VSQYEG9UAorIlpE2mVAa6dh0kQ+1C05ReJSHuClEO971civefJCfR9e0nxMVJnnT7nUTG0O1LEEpogON9sIHXAs8vrdvCZsie3wUkrLGcinI+4Yx34lmPINxpqxRe38JZ042lCqoNwIDAQAB");
        String decryptStr = rsa.decryptStr("gZV3HIpeP0h2PZAnVbHgt9BRZkdUJF+0mHIBtGyHnFlNZhzXych5zOGNFzmHkcs0VNfonLDlnjlnLsWgCPSzOLhidikwvsBiCD6D64ngOPM+G2crgSfkJDieXsUi13OoeEKEa3+Xbly1psCFBArlUrmbvEVGunE6xANcv3GZvJM=", KeyType.PublicKey);
        System.out.println(decryptStr);
    }
}
