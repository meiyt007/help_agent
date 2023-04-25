package com.zfsoft.superwindow.util;

import cn.hutool.core.lang.Assert;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;

@Slf4j
@Component
@RefreshScope
public class JwtHelper {

    /**
     * jwt从前台传递过来的key名称
     */
    public final String TOKEN_KEY = "access_token";

    /**
     * token私钥
     */
    @Value("${jwt.secret:\"\"}")
    private String TOKEN_SECRET;
    /**
     * 过期时间
     */
    @Value("${jwt.expire:120}")
    private long expireTime;

    /**
     * 生成签名
     * @Author dusd
     * @Date 2020/7/7 17:24
     * @param
     * @return
     */
    public String sign(String userId) {
        Assert.notBlank(TOKEN_SECRET,"jwt的secret不能为空");
        Assert.notBlank(TOKEN_SECRET,"jwt的过期时间配置不能为空");
        //过期时间 30天
        Date date = new Date(cn.hutool.core.date.DateUtil.offsetDay(new Date(), 30).getTime());
        //私钥及加密算法
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        //设置头信息
        HashMap<String, Object> header = new HashMap<>(2);
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        //附带username和userID生成签名
        return JWT.create().withHeader(header).withClaim("userId", userId).withExpiresAt(date).sign(algorithm);
    }

    /**
     * 获取jwt的DecodedJWT信息
     * @Author dusd
     * @Date 2020/7/8 10:27
     * @param
     * @return
     */
    public DecodedJWT getDecodedJWT(String token) throws Exception {
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt;
    }


    /**
     * 获取 过期时间
     *
     * @return expireTime 过期时间
     */
    public long getExpireTime() {
        return this.expireTime;
    }
}
