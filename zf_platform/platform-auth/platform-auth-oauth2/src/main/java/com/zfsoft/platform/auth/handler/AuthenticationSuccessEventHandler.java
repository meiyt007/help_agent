package com.zfsoft.platform.auth.handler;

import cn.hutool.json.JSONUtil;
import com.google.gson.JsonObject;
import com.zfsoft.platform.auth.feign.SysUserFeignService;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.security.data.AuthUserDetails;
import com.zfsoft.platform.utils.spring.SpringContextHelper;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@AllArgsConstructor
public class AuthenticationSuccessEventHandler implements AuthenticationSuccessHandler {

	@Autowired
	private RedisTemplate redisTemplate;

	/**
	 * 处理登录成功方法
	 * <p>
	 * 获取到登录的authentication 对象
	 * @param authentication 登录对象
	 * @param request 请求
	 * @param response 返回
	 */
	@SneakyThrows
	@Override
	public void handle(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
		String username = authentication.getName();
		// ToDo 日志记录
		log.info("用户：{} 登录成功", username);

		//设置headers
		response.setHeader("Content-Type", "application/json; charset=UTF-8");
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");

		// 登录成功后，清空失败次数
		// 测试性能需要注释
		SysUserFeignService sysUserFeignService = SpringContextHelper.getBean(SysUserFeignService.class);
		sysUserFeignService.emptyFailNum(username);

		//设置body
		byte[] dataBytes = {};
		try {
			AuthUserDetails user = (AuthUserDetails) authentication.getPrincipal();
			try {
				//登录成功后给activiti授权
				Map<String, String> map = new HashMap<>();
				map.put("username",user.getUsername());
				map.put("password",user.getPassword());
				map.put("account",user.getAccount());
				map.put("userOid",user.getUserOid());
				redisTemplate.opsForValue().set(BaseStaticParameter.WORKFLOW_ACTIVITIUSER + user.getUserOid(), JSONUtil.toJsonStr(map),3600, TimeUnit.SECONDS);
			}catch (Exception e){
				//异常了不做处理
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JsonObject result = new JsonObject();
			result.addProperty("code", 1);
			result.addProperty("msg", ex.getMessage());
			dataBytes = result.toString().getBytes();
			@Cleanup ServletOutputStream stream = response.getOutputStream();
			stream.write(dataBytes);
		}
	}

}
