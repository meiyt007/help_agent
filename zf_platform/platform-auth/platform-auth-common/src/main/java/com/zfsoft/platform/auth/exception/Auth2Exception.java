package com.zfsoft.platform.auth.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zfsoft.platform.auth.component.Auth2ExceptionSerializer;
import lombok.Getter;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * 自定义OAuth2Exception
 */
@JsonSerialize(using = Auth2ExceptionSerializer.class)
public class Auth2Exception extends OAuth2Exception {

	@Getter
	private String errorCode;

	public Auth2Exception(String msg) {
		super(msg);
	}

	public Auth2Exception(String msg, Throwable t) {
		super(msg, t);
	}

	public Auth2Exception(String msg, String errorCode) {
		super(msg);
		this.errorCode = errorCode;
	}

}
