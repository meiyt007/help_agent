
package com.zfsoft.superwindow.controller.easyquickcase;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.zfsoft.cases.feign.SysDistrictFeignService;
import com.zfsoft.microservice.platform.data.sys.SysDistrict;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.superwindow.controller.easyquickcase.data.UserAuthInfo;
import com.zfsoft.superwindow.data.sso.LegalUser;
import com.zfsoft.superwindow.data.sso.NatureUser;
import com.zfsoft.superwindow.dbaccess.data.DbPersonAuthInfo;
import com.zfsoft.superwindow.dbaccess.data.DbSsoLegalUser;
import com.zfsoft.superwindow.dbaccess.data.DbSsoNatureUser;
import com.zfsoft.superwindow.feign.settings.IdsLoginFeignService;
import com.zfsoft.superwindow.manager.sso.LegalUserManager;
import com.zfsoft.superwindow.manager.sso.NatureUserManager;
import com.zfsoft.superwindow.manager.sso.PersonAuthInfoManager;
import com.zfsoft.superwindow.service.easyquickcase.MobileService;
import com.zfsoft.superwindow.service.easyquickcase.data.ResponseData;
import com.zfsoft.superwindow.util.BeanUtils;
import com.zfsoft.superwindow.util.JwtUtil;
import com.zfsoft.superwindow.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class MobileController implements MobileService {

    @Resource
    private SysDistrictFeignService sysDistrictFeginService;

    @Resource
    private IdsLoginFeignService idsLoginFeignService;

    @Resource
    private NatureUserManager natureUserManager;

    @Resource
    private LegalUserManager legalUserManager;

    @Resource
    private PersonAuthInfoManager personAuthInfoManager;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public ApiResultSet<List<SysDistrict>> getDistListByParentOidForMobile(String parentOid) {
        return sysDistrictFeginService.querySysDistrictListByParentOid(parentOid);
    }

    @Override
    public ApiResultSet<String> login(String code) {
        Assert.notBlank(code,"授权编码不能为空");
        ApiResultSet<String> apiResultSet = new ApiResultSet<>();
        String callType = "1";
        String interId = "";
        ResponseData<UserAuthInfo> userAuthInfoResponseData = idsLoginFeignService.login(code,callType,interId);
        if(200!=userAuthInfoResponseData.getCode()){
            apiResultSet.setCode(500);
            apiResultSet.setMessage("token异常！");
            return apiResultSet;
        }
        UserAuthInfo userAuthInfo = userAuthInfoResponseData.getData();
        String token = null;
        // 如果统一社会信用代码不为空，则为法人
        if(StrUtil.isNotEmpty(userAuthInfo.getCreditCode())){
            DbSsoLegalUser dbSsoLegalUser = new DbSsoLegalUser();
            LegalUser legalUser = legalUserManager.getLegalUserByCorporateCode(userAuthInfo.getCreditCode());
            if (null == legalUser) {
                dbSsoLegalUser.setCorporateId(UUIDUtil.randomUUID());
                dbSsoLegalUser.setCorporateName(userAuthInfo.getCreditName());
                dbSsoLegalUser.setCorporateCode(userAuthInfo.getCreditCode());
                dbSsoLegalUser.setRepresentativeName(userAuthInfo.getRealName());
                dbSsoLegalUser.setRepresentativeCardNo(userAuthInfo.getIdCard());
                dbSsoLegalUser.setCorporatePhone(userAuthInfo.getMobilePhone());
                dbSsoLegalUser.setCreateTime(new Date());
                dbSsoLegalUser.setStatus("1");
                dbSsoLegalUser.setSortOrder("1");
                dbSsoLegalUser.setRegisterType("0");
            } else {
                // 公司可能改法人名字什么的
                legalUser.setCorporateName(userAuthInfo.getCreditName());
                legalUser.setCorporateCode(userAuthInfo.getCreditCode());
                legalUser.setRepresentativeName(userAuthInfo.getRealName());
                legalUser.setRepresentativeCardNo(userAuthInfo.getIdCard());
                legalUser.setCorporatePhone(userAuthInfo.getMobilePhone());
                BeanUtils.copyProperties(legalUser, dbSsoLegalUser);
            }
            legalUserManager.saveOrUpdate(dbSsoLegalUser);
            token = JwtUtil.sign(dbSsoLegalUser.getCorporateCode(), 1);
            redisTemplate.opsForValue().set("browser:jwt:" + token, token, 30, TimeUnit.MINUTES);
        }else {
            // 自然人登录
            NatureUser natureUser = natureUserManager.getUserByCardNo(userAuthInfo.getIdCard());
            DbSsoNatureUser dbSsoNatureUser = new DbSsoNatureUser();
            if (null == natureUser) {
                dbSsoNatureUser.setUserId(UUIDUtil.randomUUID());
                dbSsoNatureUser.setUserName(userAuthInfo.getRealName());
                dbSsoNatureUser.setCardNo(userAuthInfo.getIdCard());
                dbSsoNatureUser.setCardType("SFZ");
                dbSsoNatureUser.setCreateTime(new Date());
                dbSsoNatureUser.setStatus("1");
                dbSsoNatureUser.setSortOrder("1");
                dbSsoNatureUser.setRegisterType("0");
            } else {
                // app端登陆可能没有性别等一些信息
                natureUser.setUserName(userAuthInfo.getRealName());
                BeanUtils.copyProperties(natureUser, dbSsoNatureUser);
            }
            natureUserManager.saveOrUpdate(dbSsoNatureUser);
            token = JwtUtil.sign(dbSsoNatureUser.getCardNo(), 0);
            redisTemplate.opsForValue().set("browser:jwt:" + token, token, 30, TimeUnit.MINUTES);
        }
        DbPersonAuthInfo dbPersonAuthInfo = new DbPersonAuthInfo();
        dbPersonAuthInfo.setPersonOid(UUIDUtil.randomUUID());
        dbPersonAuthInfo.setUserName(userAuthInfo.getRealName());
        dbPersonAuthInfo.setUserCardNumber(userAuthInfo.getIdCard());
        dbPersonAuthInfo.setDeleteStatus("N");
        dbPersonAuthInfo.setCreateDate(new Date());
        dbPersonAuthInfo.setModifyDate(new Date());
        personAuthInfoManager.saveOrUpdatePersonAuthInfo(dbPersonAuthInfo);
        apiResultSet.setData(token);
        return apiResultSet;
    }
}
