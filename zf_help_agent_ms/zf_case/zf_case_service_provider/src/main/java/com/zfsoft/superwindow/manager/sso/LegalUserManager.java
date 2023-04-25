package com.zfsoft.superwindow.manager.sso;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.superwindow.data.sso.LegalUser;
import com.zfsoft.superwindow.dbaccess.dao.DbSsoLegalUserMapper;
import com.zfsoft.superwindow.dbaccess.data.DbSsoLegalUser;
import com.zfsoft.superwindow.util.BeanUtils;
import com.zfsoft.superwindow.util.JwtUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class LegalUserManager {

    @Resource
    private DbSsoLegalUserMapper dbSsoLegalUserMapper;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * @description 法人登录
     * @param user
     * @author meiyt
     * @date 2022/6/6
     * @return
     **/
    public String login(LegalUser user) {
        if(StrUtil.isEmpty(user.getCorporateCode())) {
            throw new ResultInfoException("统一社会信用代码不能为空");
        }
        DbSsoLegalUser dbSsoLegalUser = dbSsoLegalUserMapper.getUserByCorporateCode(user.getCorporateCode());
        if(null == dbSsoLegalUser) {
            dbSsoLegalUser = new DbSsoLegalUser();
            dbSsoLegalUser.setCreateTime(new Date());
            BeanUtils.copyProperties(user, dbSsoLegalUser);
            if(StrUtil.isEmpty(dbSsoLegalUser.getCorporateCode())) {
                dbSsoLegalUser.setCorporateCode(IdUtil.simpleUUID());
            }
            // 获取的值为nul，转为空字符串
            if (StrUtil.isEmpty(user.getEmail()) || "null".equals(user.getEmail())) {
                dbSsoLegalUser.setEmail("");
            }
            if (StrUtil.isEmpty(user.getCorporatePhone()) || "null".equals(user.getCorporatePhone())) {
                dbSsoLegalUser.setCorporatePhone("");
            }
            if (StrUtil.isEmpty(user.getRegisterAddress()) || "null".equals(user.getRegisterAddress())) {
                dbSsoLegalUser.setRegisterAddress("");
            }
            if (StrUtil.isEmpty(user.getRegisterAgency()) || "null".equals(user.getRegisterAgency())) {
                dbSsoLegalUser.setRegisterAgency("");
            }
            if (StrUtil.isEmpty(user.getAgentMobile()) || "null".equals(user.getAgentMobile())) {
                dbSsoLegalUser.setAgentMobile("");
            }
            if (StrUtil.isEmpty(user.getAgentName()) || "null".equals(user.getAgentName())) {
                dbSsoLegalUser.setAgentName("");
            }
            if (StrUtil.isEmpty(user.getAgentCardNo()) || "null".equals(user.getAgentCardNo())) {
                dbSsoLegalUser.setAgentCardNo("");
            }
            dbSsoLegalUserMapper.insert(dbSsoLegalUser);
        } else {
            BeanUtils.copyProperties(user, dbSsoLegalUser);
            dbSsoLegalUserMapper.updateByPrimaryKeySelective(dbSsoLegalUser);
        }
        String token = null;
        if(null == redisTemplate.opsForValue().get("browser:jwt:" + dbSsoLegalUser.getCorporateCode())) {
            token = JwtUtil.sign(dbSsoLegalUser.getCorporateCode(), 1);
            redisTemplate.opsForValue().set("browser:jwt:" + dbSsoLegalUser.getCorporateCode(), token, 24, TimeUnit.HOURS);
        } else {
            token = redisTemplate.opsForValue().get("browser:jwt:" + dbSsoLegalUser.getCorporateCode()).toString();
        }
        return token;
    }

    /**
     * @description 根据统一社会信用代码查询
     * @param corporateCode
     * @author meiyt
     * @date 2022/6/6
     * @return
     **/
    public LegalUser getLegalUserByCorporateCode(String corporateCode) {
        if(StrUtil.isEmpty(corporateCode)) {
            throw new ResultInfoException("统一社会信用代码不能为空");
        }
        DbSsoLegalUser dbSsoLegalUser = dbSsoLegalUserMapper.getUserByCorporateCode(corporateCode);
        if(null != dbSsoLegalUser) {
            LegalUser legalUser = new LegalUser();
            BeanUtils.copyProperties(dbSsoLegalUser, legalUser);
            return legalUser;
        }
        return null;
    }

    /**
     * @description 根据token获取法人信息
     * @param token
     * @author meiyt
     * @date 2022/6/7
     * @return
     **/
    public LegalUser getLegalUserByToken(String token) {
        DecodedJWT decodedJWT = JwtUtil.getDecoded(token);
        if (decodedJWT == null) {
            throw new ResultInfoException("登录信息异常,非有效登录信息");
        }
        Claim signTypeClaim = decodedJWT.getClaim("signType");
        if(null == signTypeClaim || signTypeClaim.asInt() != 1) {
            throw new ResultInfoException("用户类型为空或类型查询错误");
        }
        Claim corporateCodeClaim = decodedJWT.getClaim("uniqueCode");
        if(null == corporateCodeClaim) {
            throw new ResultInfoException("token信息错误");
        }
        DbSsoLegalUser dbSsoLegalUser = dbSsoLegalUserMapper.getUserByCorporateCode(corporateCodeClaim.asString());
        if(null != dbSsoLegalUser) {
            LegalUser legalUser = new LegalUser();
            BeanUtils.copyProperties(dbSsoLegalUser, legalUser);
            return legalUser;
        }
        return null;
    }

    /**
     * @description 用户保存
     * @param dbSsoLegalUser
     * @author wangyg
     * @date 2022/6/21
     * @return
     */
    public void saveOrUpdate(DbSsoLegalUser dbSsoLegalUser) {
        if (dbSsoLegalUser.getId() == null) {
            dbSsoLegalUserMapper.insert(dbSsoLegalUser);
        } else {
            dbSsoLegalUserMapper.updateByPrimaryKey(dbSsoLegalUser);
        }
    }
}
