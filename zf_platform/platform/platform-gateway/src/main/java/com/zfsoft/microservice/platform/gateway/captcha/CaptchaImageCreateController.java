package com.zfsoft.microservice.platform.gateway.captcha;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.RSA;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.google.gson.JsonObject;
import com.zfsoft.microservice.platform.data.sys.SysLogin;
import com.zfsoft.microservice.platform.gateway.config.ZhuofanConfig;
import com.zfsoft.microservice.platform.gateway.feign.SysConfigFeignService;
import com.zfsoft.microservice.platform.gateway.feign.SysUserFeignService;
import com.zfsoft.microservice.platform.gateway.security.exception.BadCaptchaException;
import com.zfsoft.microservice.settings.data.SysConfig;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufMono;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: lijun@sstcsoft.com
 * @Date: 2019/9/25 17:57
 */
@Controller
@RefreshScope
public class CaptchaImageCreateController {

    private Producer captchaProducer = null;

    @Autowired
    public void setCaptchaProducer(Producer captchaProducer) {
        this.captchaProducer = captchaProducer;
    }

    @Autowired
    private SysConfigFeignService sysConfigFeignService;

    @Autowired
    private SysUserFeignService sysUserFeignService;

    @Autowired
    StringEncryptor encryptor;

    @Autowired
    private ZhuofanConfig zhuofanConfig;


    @RequestMapping("/captcha-image")
    public Mono<Void> handleRequest(WebSession session, ServerHttpResponse response) throws Exception {
        DataBufferFactory bufferFactory = response.bufferFactory();
        response.getHeaders().set("Expires", "0");
        // Set standard HTTP/1.1 no-cache headers.
        response.getHeaders().set("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.getHeaders().set("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        response.getHeaders().set("Pragma", "no-cache");
        // return a jpeg
        response.getHeaders().set("Content-Type","image/jpeg");
        // create the text for the image
        String capText = captchaProducer.createText();
        session.getAttributes().put(Constants.KAPTCHA_SESSION_KEY, capText);
        session.getAttributes().put(Constants.KAPTCHA_SESSION_DATE,new Date());
        BufferedImage bi = captchaProducer.createImage(capText);
        byte[] uppedContent;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bi, "jpg", byteArrayOutputStream);
        uppedContent = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        DataBuffer dataBuffer = bufferFactory.wrap(uppedContent);
        return response.writeAndFlushWith(ByteBufMono.just(Mono.just(dataBuffer)));
    }

    /**
     * 生成加密密钥
     * @author zxx
     * @date 2020/9/29 2:18 下午
     * @param session
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/rsa-key")
    @ResponseBody
    public Mono<Void> handleRSA(WebSession session, ServerHttpResponse response) throws Exception {
        HttpHeaders httpHeaders = response.getHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        RSA rsa = SecureUtil.rsa();
        String privateKeyBase64 = rsa.getPrivateKeyBase64();
        String publicKeyBase64 = rsa.getPublicKeyBase64();
        SysConfig sysConfig = sysConfigFeignService.getSysConfigByCode(BaseStaticParameter.CONFIG_LOGIN_ENCRYPT).getData();

        Map<String,String> keyMap=new HashMap<>();
        keyMap.put(BaseStaticParameter.CONFIG_LOGIN_ENCRYPT, sysConfig.getValue());
        keyMap.put(BaseStaticParameter.PRIVATE_KEY, privateKeyBase64);
        session.getAttributes().put(BaseStaticParameter.RSA_KEY, keyMap);

        JsonObject result = new JsonObject();
        result.addProperty("config",sysConfig.getValue());
        result.addProperty(BaseStaticParameter.PUBLIC_KEY, publicKeyBase64);
        byte[] dataBytes = result.toString().getBytes();
        DataBuffer bodyDataBuffer = response.bufferFactory().wrap(dataBytes);
        return response.writeWith(Mono.just(bodyDataBuffer));
    }


    /**
     * @description: 获取登录验码的标识 true 开启  false关闭
     * @author: wuxx
     * @Date: 2020/10/26 9:30
     **/
    @RequestMapping(value = {"/getLoginCodeFlag"},method = {RequestMethod.GET})
    @ResponseBody
    public Mono<Void> handleCodeFlag(ServerHttpResponse response){
        HttpHeaders httpHeaders = response.getHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        //Boolean aBoolean = sysConfigFeignService.getLoginCodeFlag().getData();
        JsonObject result = new JsonObject();
        boolean validCode = zhuofanConfig.isValidCode();
        if(validCode && CurrentLoginUserHolder.getCurrentLoginUser()==null){
            validCode = false;
        }
        result.addProperty("codeFlag",validCode);
        byte[] dataBytes = result.toString().getBytes();
        DataBuffer bodyDataBuffer = response.bufferFactory().wrap(dataBytes);
        return response.writeWith(Mono.just(bodyDataBuffer));
    }

    /**
     * @description: 加密的字符串用与配置文件的加密
     * 配置文件使用 ENC(加密后字符串)
     * @param encryptStr 加密的字符串
     * @author: wuxx
     * @Date: 2020/11/4 19:17
     **/
    @RequestMapping(value = {"/encrypt"},method = {RequestMethod.GET})
    @ResponseBody
    public Mono<Void> jacketEncrypt(ServerHttpResponse response,String encryptStr){
        HttpHeaders httpHeaders = response.getHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        if(null!=encryptStr){
            if(encryptStr.contains("jdbc:mysql")){
                encryptStr=encryptStr + "?useUnicode=true&characterEncoding=utf8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8";
            }
            //加密
            String encryptMessage = encryptor.encrypt(encryptStr);
            //解密
            String decrypt1 = encryptor.decrypt(encryptMessage);
            JsonObject result = new JsonObject();
            result.addProperty("encryptStr",encryptStr);
            result.addProperty("encryptMessage",encryptMessage);
            byte[] dataBytes = result.toString().getBytes();
            DataBuffer bodyDataBuffer = response.bufferFactory().wrap(dataBytes);
            return response.writeWith(Mono.just(bodyDataBuffer));
        }
        return Mono.error(new ResultInfoException("字符串加密错误！"));
    }

    /**
     * @description: 验证找回密码的登录名和验证码
     * @author: wuxx
     * @Date: 2020/11/24 13:07
     **/
    @RequestMapping(value = {"/checkAccountAndCode"},method = {RequestMethod.POST})
    @ResponseBody
    public Mono<Void> checkAccountAndCode(WebSession session, ServerHttpResponse response,String account,String code){
        HttpHeaders httpHeaders = response.getHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");

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
        String kaptcha = (String) session.getAttributes().get(Constants.KAPTCHA_SESSION_KEY);
        if(null==kaptcha || null == code){
            return Mono.error(new BadCaptchaException("验证码不能为空！"));
        }
        if(!kaptcha.equals(code)){
            return Mono.error(new BadCaptchaException("验证码不正确！"));
        }
        if(null==account){
            return Mono.error(new BadCaptchaException("登录名不能为空！"));
        }

        // 通过以上流程后，需将KAPTCHA_SESSION_KEY清除，防止用户名枚举漏洞
        session.getAttributes().put(Constants.KAPTCHA_SESSION_KEY, null);

        ApiResultSet<SysLogin> byAccount = sysUserFeignService.getSysLoginByAccount(account);
        if(byAccount.getCode() == 200 && null!=byAccount.getData()){
            JsonObject result = new JsonObject();
            result.addProperty("previous",byAccount.getData().getPassword());
            result.addProperty("userOid",byAccount.getData().getUserOid());
            result.addProperty("code",200);
            byte[] dataBytes = result.toString().getBytes();
            DataBuffer bodyDataBuffer = response.bufferFactory().wrap(dataBytes);
            return response.writeWith(Mono.just(bodyDataBuffer));
        }
        return Mono.error(new ResultInfoException("登录名不存在！"));
    }

    /**
     * 生成短信验证码接口（示例）
     * @param session
     * @param response
     * @param mobile
     * @return
     * @throws Exception
     */
    @RequestMapping("/genSMSVerification")
    public Mono<Void> genSMSVerification(WebSession session, ServerHttpResponse response,String mobile) throws Exception {
        HttpHeaders httpHeaders = response.getHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        int code=RandomUtil.randomInt(1000, 9999);
        session.getAttributes().put(BaseStaticParameter.SMS_CODE,String.valueOf(code));
        session.getAttributes().put(BaseStaticParameter.SMS_SESSION_DATE,new Date());
        System.out.println("-----------------------------------"+code);
        JsonObject result = new JsonObject();
        result.addProperty("code",200);
        result.addProperty(BaseStaticParameter.SMS_CODE,code);
        byte[] dataBytes = result.toString().getBytes();
        DataBuffer bodyDataBuffer = response.bufferFactory().wrap(dataBytes);
        return response.writeWith(Mono.just(bodyDataBuffer));
    }

    /**
     * 校验短信验证码接口（示例）
     * @param code
     * @param session
     * @param response
     * @return
     */
    @RequestMapping(value = {"/checkSMSVerification"})
    @ResponseBody
    public Mono<Void> checkSMSVerification(@RequestParam(value = "code", required = false)String code,WebSession session, ServerHttpResponse response){
        HttpHeaders httpHeaders = response.getHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");

//        Date lastDate = (Date) session.getAttribute(BaseStaticParameter.SMS_SESSION_DATE);
//        if (lastDate == null) {
//            return Mono.error(new BadCaptchaException("验证码失效，请重新获取验证码！"));
//        }
//        Date nowDate = new Date();
//        long costTime = nowDate.getTime() - lastDate.getTime();
//        if (costTime > 120000) {
//            return Mono.error(new BadCaptchaException("验证码已经过期！"));
//        }

        String captcha = (String) session.getAttributes().get(BaseStaticParameter.SMS_CODE);
        if(StrUtil.isBlank(code)){
            return Mono.error(new BadCaptchaException("验证码不能为空！"));
        }
        if(StrUtil.isBlank(captcha)){
            return Mono.error(new BadCaptchaException("验证码不正确！"));
        }
        if(!captcha.equals(code)){
            return Mono.error(new BadCaptchaException("验证码不正确！"));
        }
        JsonObject result = new JsonObject();
        result.addProperty("code",200);
        byte[] dataBytes = result.toString().getBytes();
        DataBuffer bodyDataBuffer = response.bufferFactory().wrap(dataBytes);
        return response.writeWith(Mono.just(bodyDataBuffer));
    }
}
