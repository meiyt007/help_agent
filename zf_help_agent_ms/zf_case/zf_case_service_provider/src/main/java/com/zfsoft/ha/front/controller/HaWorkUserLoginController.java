package com.zfsoft.ha.front.controller;

import cn.hutool.core.util.StrUtil;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.zfsoft.ha.constant.RedisConstants;
import com.zfsoft.ha.data.HaLoginUserInfo;
import com.zfsoft.ha.data.responseData.HaWorkUserResponseData;
import com.zfsoft.ha.data.vo.HaWorkUserVo;
import com.zfsoft.ha.dbaccess.data.vo.DbHaWorkUserVo;
import com.zfsoft.ha.front.HaWorkUserLoginService;
import com.zfsoft.ha.managers.HaWorkUserLoginManager;
import com.zfsoft.ha.util.HaLoginUserHolder;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.data.SysConfig;
import com.zfsoft.platform.utils.feign.SysConfigFeignService;
import com.zfsoft.superwindow.util.BASE64Utils;
import com.zfsoft.superwindow.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.zfsoft.cases.constant.Constants.HA_HELP_GATEWAY_PATH;
import static com.zfsoft.cases.constant.Constants.HA_HELP_LOGIN_CHECKCODE;

/**
 * @description: 帮代办人员登录控制层
 * @author: kangax
 * @date: 2022-07-28 10:07
 **/
@RestController
@Slf4j
public class HaWorkUserLoginController implements HaWorkUserLoginService {

    private static final Logger logger = LoggerFactory.getLogger(HaWorkUserLoginController.class);

    /**
     * 登录接口数据层实现
     */
    @Resource
    private HaWorkUserLoginManager haWorkUserLoginManager;

    /**
     * redisTemplate
     */
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 随机验证码默认配置
     */
    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @Resource
    private SysConfigFeignService sysConfigFeignService;


    /**
     * 生成随机验证码
     *
     * @param request
     * @param response
     */
    @Override
    public void getLoginCode(HttpServletRequest request, HttpServletResponse response) {
        //定义response输出类型为image/jpeg
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        //---------------------------生成验证码----------------------
        //获取验证码文本内容
        String text = defaultKaptcha.createText();
        //将验证码放到session中
        request.getSession().setAttribute("captcha", text);
        //根据文本内容创建图形验证码
        BufferedImage image = defaultKaptcha.createImage(text);
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            //输出流输出图片,格式为jpg
            ImageIO.write(image, "jpg", outputStream);
            outputStream.flush();
            log.info("帮代办人员登录生成随机验证码成功，验证码文本 ：{}", text);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("帮代办人员生成随机验证码失败，errorMessage : {} ", e.getMessage());
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("帮代办人员生成随机验证码失败，errorMessage : {} ", e.getMessage());
                }
            }
        }
    }

    /**
     * 帮代办人员登录方法
     *
     * @param account   登录用户名
     * @param password  密码
     * @param checkCode 验证码
     * @return ApiResultSet<HaWorkUserResponseData>  获取帮代办人员登录详细信息
     * @author: wangyh
     * @Date: 2022年7月25日13:04:17
     */
    @Override
    public ApiResultSet<HaWorkUserResponseData> loginByAccount(HttpServletRequest request, String account, String password, String checkCode,String haType) {
        logger.info("帮代办人员登录方法，account:{},password:{},checkCode:{}", account, password, checkCode);
        DbHaWorkUserVo dbHaWorkUserVo = null;
        HaWorkUserResponseData haWorkUserResponseData = new HaWorkUserResponseData();
        try {
            SysConfig sysConfig = sysConfigFeignService.getSysConfigByCode("HA_HELP_LOGIN_CHECKCODE").getData();
            String codeFlag = "";
            if(null!=sysConfig){
                codeFlag = sysConfig.getValue();
            }
            if( "1".equals(codeFlag)){
                //获取session中验证码
                String checkCodes = (String) request.getSession().getAttribute("captcha");
                checkCode = BASE64Utils.base64Decode(checkCode);
                logger.debug("checkCode:{}", checkCode); //StrUtil.isEmpty(checkCode) && StrUtil.isEmpty(checkCodes) &&
                if (!checkCodes.equals(checkCode)) { //判断验证码是否正确
                    return new ApiResultSet<>(500, "用户验证码错误");
                }
            }
            if (StrUtil.isEmpty(account)||StrUtil.isEmpty(password)){
                    return new ApiResultSet<>(500, "账号或密码不能为空");
            }
            dbHaWorkUserVo = haWorkUserLoginManager.loginByAccounAndId(account,null);
            logger.debug("haWorkUser:{}", dbHaWorkUserVo);
            if (dbHaWorkUserVo != null) {//判断账号查询的ThaWorkUser对象是否为null
                String passwrod = CommonUtil.md5(dbHaWorkUserVo.getAccount() + BASE64Utils.base64Decode(password) + dbHaWorkUserVo.getSalt());
                if (!passwrod.equals(dbHaWorkUserVo.getPassword())) { //判断用户密码加密后是否相等
                    return new ApiResultSet<>(500, "账号或密码错误");
                }
                String token = UUID.randomUUID().toString().replace("-", "");
                HaLoginUserInfo loginUserInfo = new HaLoginUserInfo();
                BeanUtils.copyProperties(dbHaWorkUserVo, loginUserInfo);
                BeanUtils.copyProperties(dbHaWorkUserVo, haWorkUserResponseData);
                haWorkUserResponseData.setToken(token);
                loginUserInfo.setToken(token);
                //将token为key value为对象用户信息存入redis中 时间期限是2小时
                redisTemplate.opsForValue().set(RedisConstants.LOGIN_SESSION_TOKEN+token, loginUserInfo,
                        RedisConstants.LOGIN_SESSION_TTL,TimeUnit.HOURS);
                logger.debug("token:{}", token);
                //登录成功后并记录到用户在线时长表
                haWorkUserLoginManager.insertOnline(request, dbHaWorkUserVo, token, haType);
            } else {
                return new ApiResultSet<>(500, "账号或密码错误");
            }
        } catch (Exception e) {
            logger.error("帮代办人员登录方法错误：", e);
            return new ApiResultSet<>(500, "帮代办人员登录方法错误", e.getMessage());
        }
        return ApiResultSet.ok("登录成功", haWorkUserResponseData);
    }

    /**
     * @return ApiResultSet<HaLoginUserInfo>  获取登录人员信息
     * @description: 获取登录人员信息
     * @return: ApiResultSet<HaLoginUserInfo> 返回登录人员登录人员的详细信息
     * @author: kangax
     * @date: 2022-07-28 10:53
     */
    @Override
    public ApiResultSet<HaWorkUserVo> getLoginInfo() {
        log.info("进入获取当前登录用户信息Controller");
        //获取当前登录用户信息
        HaLoginUserInfo loginUser = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        DbHaWorkUserVo helpWorkUserById = haWorkUserLoginManager.loginByAccounAndId(null,loginUser.getId());
        HaWorkUserVo haWorkUserVo = new HaWorkUserVo();
        BeanUtils.copyProperties(helpWorkUserById, haWorkUserVo);
        if (loginUser != null) {
            log.info("获取当前登录用户信息成功，当前登录用户信息 ： {} ", haWorkUserVo);
            return ApiResultSet.ok("获取用户信息成功", haWorkUserVo);
        }
        log.error("获取当前登录用户信息失败！获取用户信息为空！");
        return new ApiResultSet<>(ApiResultSet.UNKNOWN_ERROR, "获取用户登录信息失败");
    }

    /**
     * @param // token  在header中
     * @return ApiResultSet  获取退出登录详细信息
     * @description: 退出登录
     * @author: wangyh
     * @Date: 2022-08-2 10:50
     **/
    @Override
    public ApiResultSet logout() throws Exception {
        log.info("退出登录");
        //获取当前用户信息
        HaLoginUserInfo haLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        if (haLoginUserInfo.getToken() != null) {
            haWorkUserLoginManager.logout(haLoginUserInfo.getToken(), haLoginUserInfo.getName());
        } else {
            return new ApiResultSet<>(ApiResultSet.UNKNOWN_ERROR, "token不能为空");
        }
        return ApiResultSet.ok("用户退出登录成功");
    }

    /**
     * @param // token  在header中
     * @return ApiResultSet  获取在线状态详细信息
     * @description: 在线状态
     * @author: wangyh
     * @Date: 2022-08-2 10:50
     **/
    @Override
    public ApiResultSet online() throws Exception {
        log.info("在线状态");
        //获取当前用户信息
        HaLoginUserInfo haLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        if (haLoginUserInfo.getToken() != null) {
            haWorkUserLoginManager.online(haLoginUserInfo.getToken(), haLoginUserInfo.getName());
        } else {
            return new ApiResultSet<>(ApiResultSet.UNKNOWN_ERROR, "token不能为空");
        }
        return ApiResultSet.ok("在线状态接口调用成功");
    }

    /**
     * @description: 登录时是否需要验证码
     * @author: zhaobf
     * @Date: 2022/8/8 11:15
     **/
    @RequestMapping(value = {"login/checkCodeFlag"},method = {RequestMethod.GET})
    public ApiResultSet getLoginCodeFlag(){
        SysConfig sysConfig = sysConfigFeignService.getSysConfigByCode(HA_HELP_LOGIN_CHECKCODE).getData();
        String codeFlag = "";
        Map<String,String> map = new HashMap<>();
        if(null!=sysConfig){
            codeFlag = sysConfig.getValue();
        }
        map.put("flag",codeFlag);
        return new ApiResultSet<>(map);
    }

    /**
     * @description: 登录时是否需要验证码
     * @author: zhaobf
     * @Date: 2022/8/8 11:15
     **/
    @RequestMapping(value = {"login/gatewayPath"},method = {RequestMethod.GET})
    public ApiResultSet getGatewayPath(){
        SysConfig sysConfig = sysConfigFeignService.getSysConfigByCode(HA_HELP_GATEWAY_PATH).getData();
        String codeFlag = "";
        Map<String,String> map = new HashMap<>();
        if(null!=sysConfig){
            codeFlag = sysConfig.getValue();
        }
        map.put("path",codeFlag);
        return new ApiResultSet<>(map);
    }

}
