package com.zfsoft.superwindow.manager.sso;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.superwindow.data.sso.LegalUser;
import com.zfsoft.superwindow.data.sso.NatureUser;
import com.zfsoft.superwindow.dbaccess.dao.DbSsoLegalUserMapper;
import com.zfsoft.superwindow.dbaccess.dao.DbSsoNatureUserMapper;
import com.zfsoft.superwindow.dbaccess.data.DbSsoLegalUser;
import com.zfsoft.superwindow.dbaccess.data.DbSsoNatureUser;
import com.zfsoft.superwindow.util.BeanUtils;
import com.zfsoft.superwindow.util.JwtUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class NatureUserManager {

    @Resource
    private DbSsoNatureUserMapper dbSsoNatureUserMapper;

    @Resource
    private DbSsoLegalUserMapper dbSsoLegalUserMapper;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * @description 自然人登录
     * @param user
     * @author meiyt
     * @date 2022/6/6
     * @return
     **/
    public String login(NatureUser user) {
        if (StrUtil.isEmpty(user.getCardNo())) {
            throw new ResultInfoException("身份证号不能为空");
        }
        DbSsoNatureUser dbSsoNatureUser = dbSsoNatureUserMapper.getUserByCardNo(user.getCardNo());
        if (null == dbSsoNatureUser) {
            dbSsoNatureUser = new DbSsoNatureUser();
            BeanUtils.copyProperties(user, dbSsoNatureUser);
            if(StrUtil.isEmpty(dbSsoNatureUser.getUserId()) || "null".equals(dbSsoNatureUser.getUserId())) {
                dbSsoNatureUser.setUserId(IdUtil.simpleUUID());
            }
            if (StrUtil.isEmpty(user.getAlipayNo()) || "null".equals(dbSsoNatureUser.getAlipayNo())) {
                dbSsoNatureUser.setAlipayNo("");
            }
            if (StrUtil.isEmpty(user.getEmail()) || "null".equals(dbSsoNatureUser.getEmail())) {
                dbSsoNatureUser.setEmail("");
            }
            dbSsoNatureUserMapper.insert(dbSsoNatureUser);
        } else {
            BeanUtils.copyProperties(user, dbSsoNatureUser);
            dbSsoNatureUserMapper.updateByPrimaryKeySelective(dbSsoNatureUser);
        }
        String token = null;
        if (null == redisTemplate.opsForValue().get("browser:jwt:" + dbSsoNatureUser.getCardNo())) {
            token = JwtUtil.sign(dbSsoNatureUser.getCardNo(), 0);
            redisTemplate.opsForValue().set("browser:jwt:" + dbSsoNatureUser.getCardNo(), token, 24, TimeUnit.HOURS);
        } else {
            token = redisTemplate.opsForValue().get("browser:jwt:" + dbSsoNatureUser.getCardNo()).toString();
        }
        return token;
    }

    /**
     * @description 根据身份证号查询
     * @param cardNo
     * @author meiyt
     * @date 2022/6/6
     * @return
     **/
    public NatureUser getUserByCardNo(String cardNo) {
        if(StrUtil.isEmpty(cardNo)) {
            throw new ResultInfoException("身份证号不能为空");
        }
        DbSsoNatureUser dbSsoNatureUser = dbSsoNatureUserMapper.getUserByCardNo(cardNo);
        if(null != dbSsoNatureUser) {
            NatureUser natureUser = new NatureUser();
            BeanUtils.copyProperties(dbSsoNatureUser, natureUser);
            return natureUser;
        }
        return null;
    }

    /**
     * @description 根据token查询用户信息
     * @param token
     * @author meiyt
     * @date 2022/6/7
     * @return
     **/
    public NatureUser getNatureUserByToken(String token) {
        DecodedJWT decodedJWT = JwtUtil.getDecoded(token);
        if (decodedJWT == null) {
            throw new ResultInfoException("登录信息异常,非有效登录信息");
        }
        Claim signTypeClaim = decodedJWT.getClaim("signType");
        if(null == signTypeClaim || signTypeClaim.asInt() != 0) {
            throw new ResultInfoException("用户类型为空或类型查询错误");
        }
        Claim cardNoClaim = decodedJWT.getClaim("uniqueCode");
        if(null == cardNoClaim) {
            throw new ResultInfoException("token信息错误");
        }
        DbSsoNatureUser dbSsoNatureUser = dbSsoNatureUserMapper.getUserByCardNo(cardNoClaim.asString());
        if(null != dbSsoNatureUser) {
            NatureUser natureUser = new NatureUser();
            BeanUtils.copyProperties(dbSsoNatureUser, natureUser);
            return natureUser;
        }
        return null;
    }

    /**
     * 获取用户类型及用户信息
     * @param token
     * @return
     */
    public Map<String, Object> getLoginUser(String token) {
        DecodedJWT decoded = JwtUtil.getDecoded(token);
        Map<String, Object> map = new HashMap<>(2);
        if (decoded == null) {
            throw new ResultInfoException("登录信息异常,非有效登录信息");
        }
        Claim signTypeClaim = decoded.getClaim("signType");
        Claim uniqueCode = decoded.getClaim("uniqueCode");
        if(signTypeClaim == null || uniqueCode == null) {
            throw new ResultInfoException("Token信息异常，请重新登录！");
        }
        int signType = signTypeClaim.asInt();
        map.put("type", signType);
        if (signType == 0) {
            DbSsoNatureUser dbSsoNatureUser = dbSsoNatureUserMapper.getUserByCardNo(uniqueCode.asString());
            if(null != dbSsoNatureUser) {
                NatureUser natureUser = new NatureUser();
                BeanUtils.copyProperties(dbSsoNatureUser, natureUser);
                map.put("user", natureUser);
            }
        }
        if (signType == 1) {
            DbSsoLegalUser dbSsoLegalUser = dbSsoLegalUserMapper.getUserByCorporateCode(uniqueCode.asString());
            if(null != dbSsoLegalUser) {
                LegalUser legalUser = new LegalUser();
                BeanUtils.copyProperties(dbSsoLegalUser, legalUser);
                map.put("user", legalUser);
            }
        }
        return map;
    }

    /**
     * @description 用户保存
     * @param user
     * @author wangyg
     * @date 2022/6/21
     * @return
     */
    public void saveOrUpdate(DbSsoNatureUser user) {
        if (user.getId() == null) {
            dbSsoNatureUserMapper.insert(user);
        } else {
            dbSsoNatureUserMapper.updateByPrimaryKey(user);
        }
    }
}
