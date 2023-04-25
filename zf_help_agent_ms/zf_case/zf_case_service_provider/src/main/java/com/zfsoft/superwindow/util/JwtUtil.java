package com.zfsoft.superwindow.util;

import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hut
 * @date 2022/5/31
 */
@Slf4j
public class JwtUtil {

    /**
     * 过期时间(redis使用控制)
     */
    public static final long EXPIRE_TIME = 120 * 60 * 1000;

    /**
     * token秘钥
     */
    private static final String TOKEN_SECRET = "1d337154-112b-a5f9-26fc-4e40003f36f0";

    /**
     * jwt从前台传递过来的key名称
     */
    public static final String TOKEN_KEY = "access_token";

    /**
     * @description 生成token
     * @param uniqueCode 唯一标识：自然人就是身份证号；法人就是统一信用代码
     * @param signType 0-自然人，1-法人
     * @author meiyt
     * @date 2022/6/6
     * @return
     **/
    public static String sign(String uniqueCode, Integer signType) {
        log.info("sign begin.");
        try {
            //过期时间 30天
            Date date = new Date(DateUtil.offsetDay(new Date(), 30).getTime());
            // 秘钥和加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            // headers
            Map<String, Object> herders = new HashMap<>(2);
            herders.put("typ", "JWT");
            herders.put("alg", "HS256");
            log.info("sign end.");
            return JWT.create()
                    .withHeader(herders)
                    .withClaim("signType", signType)
                    .withClaim("uniqueCode", uniqueCode)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("sign error.");
            return ex.getMessage();
        }
    }

    /**
     * 校验方法-token校验
     * @param token
     * @return
     */
    public static boolean verify(String token) {
        log.info("verify begin.");
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            log.info("verify end.");
            return true;
        } catch (Exception ex) {
            log.error("verify error.");
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * 获取签名信息
     * @param token
     * @return
     */
    public static DecodedJWT getDecoded(String token) {
        log.info("getDecodedJWT begin.");
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            log.info("getDecodedJWT end.");
            return verifier.verify(token);
        } catch (Exception ex) {
            log.error("getDecodedJWT error.");
            ex.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String token = sign("123456", 0);
        System.out.println("token = " + token);
        DecodedJWT decodedJWT = getDecoded(token);
        System.out.println(decodedJWT.getClaim("signType").asInt());
        System.out.println(decodedJWT.getClaim("uniqueCode").asString());
    }
}
