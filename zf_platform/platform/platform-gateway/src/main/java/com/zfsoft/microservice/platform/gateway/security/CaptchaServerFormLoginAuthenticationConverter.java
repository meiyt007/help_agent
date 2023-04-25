package com.zfsoft.microservice.platform.gateway.security;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.google.code.kaptcha.Constants;
import com.zfsoft.microservice.platform.gateway.config.ZhuofanConfig;
import com.zfsoft.microservice.platform.gateway.security.data.UsernamePasswordCaptchaAuthenticationToken;
import com.zfsoft.microservice.platform.gateway.security.exception.BadCaptchaException;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName CaptchaServerFormLoginAuthenticationConverter
 * @Description
 * @Author
 * @Date2020-08-29 0:15
 * @Version V1.0
 **/
@Component
public class CaptchaServerFormLoginAuthenticationConverter implements ServerAuthenticationConverter {
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    private String usernameParameter = "username";
    private String passwordParameter = "password";
    private String captchaParameter = "captcha";

    @Autowired
    private ZhuofanConfig zhuofanConfig;

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        Mono<String> captchaMono = exchange.getSession().flatMap(session -> {
            //ZhuofanConfig zhuofanConfig = SpringContextHelper.getBean(ZhuofanConfig.class);
            //ApiResultSet<Boolean> loginCodeFlag = sysConfigFeignService.getLoginCodeFlag();
            if(zhuofanConfig.isValidCode()){
                Date lastDate = (Date) session.getAttribute(Constants.KAPTCHA_SESSION_DATE);
                if (lastDate == null) {
                    //return Mono.error(new BadCaptchaException(this.messages.getMessage("CaptchaServerFormLoginAuthenticationConverter.captcha_empty", "Captcha is empty!")));
                    return Mono.error(new BadCaptchaException("验证码失效，请刷新页面重新操作！"));
                }
                Date nowDate = new Date();
                long costTime = nowDate.getTime() - lastDate.getTime();
                if (costTime > 120000) {
                    //return Mono.error(new BadCaptchaException(this.messages.getMessage("CaptchaServerFormLoginAuthenticationConverter.captcha_expired", "Captcha is timeOut!")));
                    return Mono.error(new BadCaptchaException("验证码已经过期！"));
                }
            }
            String kaptcha = session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
            if(null==kaptcha){
                kaptcha="";
            }
            // 解决 登录功能出存在验证码绕过导致可进行用户名撞库
            session.getAttributes().remove(Constants.KAPTCHA_SESSION_KEY);

            return Mono.just(kaptcha);
        });
        Mono<Map> rsaKeyMono = exchange.getSession().flatMap(session -> {
            Map<String,String> rsaKey= session.getAttribute(BaseStaticParameter.RSA_KEY);
            if(rsaKey==null){
                rsaKey=new HashMap<>();
            }
            return Mono.just(rsaKey);
        });
        return rsaKeyMono.flatMap(rsaKey -> {
            return captchaMono.flatMap(captcha2 -> {
                return exchange.getFormData().map((data) -> {
                    return this.createAuthentication(data, captcha2, rsaKey);
                });
            });
        });
    }

    private UsernamePasswordCaptchaAuthenticationToken createAuthentication(MultiValueMap<String, String> data, String captcha2, Map<String,String> rsaKey) {
        /*//验证是否注册授权
        RegisterFeignService registerFeignService = SpringContextHelper.getBean(RegisterFeignService.class);
        ApiResultSet checkRegisterResult = registerFeignService.checkRegister();
        if(200 != checkRegisterResult.getCode()) {
            throw new UnRegisteredException(null!=checkRegisterResult.getMessage()?checkRegisterResult.getMessage():"注册授权文件异常，请重新注册！");
        }*/
        String username = (String) data.getFirst(this.usernameParameter);
        String password = (String) data.getFirst(this.passwordParameter);
        String captcha = (String) data.getFirst(this.captchaParameter);
        //SysConfigFeignService sysConfigFeignService = SpringContextHelper.getBean(SysConfigFeignService.class);
        //ApiResultSet<Boolean> loginCodeFlag = sysConfigFeignService.getLoginCodeFlag();
        if(zhuofanConfig.isValidCode()){
            if (!captcha.equals(captcha2)) {
                throw new BadCaptchaException("验证码不正确!");
            }
        }
        if(rsaKey!=null){
            String loginEncrypt = rsaKey.get(BaseStaticParameter.CONFIG_LOGIN_ENCRYPT);
            String privateKey = rsaKey.get(BaseStaticParameter.PRIVATE_KEY);
            //加密传输，进行解密
            if("1".equals(loginEncrypt)){
                password=password.replaceAll(" ", "+");
                RSA rsa = SecureUtil.rsa(privateKey,null);
                password = rsa.decryptStr(password, KeyType.PrivateKey);
            }

        }
        return new UsernamePasswordCaptchaAuthenticationToken(username, password, captcha);
    }

    public void setUsernameParameter(String usernameParameter) {
        Assert.notNull(usernameParameter, "usernameParameter cannot be null");
        this.usernameParameter = usernameParameter;
    }

    public void setPasswordParameter(String passwordParameter) {
        Assert.notNull(passwordParameter, "passwordParameter cannot be null");
        this.passwordParameter = passwordParameter;
    }

    public void setCaptchaParameter(String captchaParameter) {
        Assert.notNull(passwordParameter, "captchaParameter cannot be null");
        this.captchaParameter = captchaParameter;
    }
}
