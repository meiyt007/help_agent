package com.zfsoft.platform.auth.component;

import cn.hutool.http.HttpStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zfsoft.platform.auth.constant.CommonConstants;
import com.zfsoft.platform.auth.util.Oauth2SecurityMessageSourceUtil;
import com.zfsoft.platform.common.data.ApiResultSet;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


/**
 * 异常处理 {@link org.springframework.security.core.AuthenticationException } 不同细化异常处理
 */
@Slf4j
@Component
@AllArgsConstructor
public class CommenceAuthExceptionEntryPoint implements AuthenticationEntryPoint {

	private final ObjectMapper objectMapper;

	@Override
	@SneakyThrows
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) {
		response.setCharacterEncoding(CommonConstants.UTF8);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		ApiResultSet apiResultSet = new ApiResultSet();
		apiResultSet.setMessage(authException.getMessage());
		apiResultSet.setData(authException.getMessage());
		apiResultSet.setCode(CommonConstants.FAIL);

		if (authException instanceof CredentialsExpiredException
				|| authException instanceof InsufficientAuthenticationException) {
			String msg = Oauth2SecurityMessageSourceUtil.getAccessor().getMessage(
					"AbstractUserDetailsAuthenticationProvider.credentialsExpired", authException.getMessage());
			apiResultSet.setMessage(msg);
		}

		if (authException instanceof UsernameNotFoundException) {
			String msg = Oauth2SecurityMessageSourceUtil.getAccessor().getMessage(
					"AbstractUserDetailsAuthenticationProvider.noopBindAccount", authException.getMessage());
			apiResultSet.setMessage(msg);
		}

		if (authException instanceof BadCredentialsException) {
			String msg = Oauth2SecurityMessageSourceUtil.getAccessor().getMessage(
					"AbstractUserDetailsAuthenticationProvider.badClientCredentials", authException.getMessage());
			apiResultSet.setMessage(msg);
		}

		response.setStatus(HttpStatus.HTTP_UNAUTHORIZED);
		PrintWriter printWriter = response.getWriter();
		printWriter.append(objectMapper.writeValueAsString(apiResultSet));
	}

}
