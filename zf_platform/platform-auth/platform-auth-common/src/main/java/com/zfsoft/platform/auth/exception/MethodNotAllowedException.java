package com.zfsoft.platform.auth.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zfsoft.platform.auth.component.Auth2ExceptionSerializer;
import org.springframework.http.HttpStatus;

@JsonSerialize(using = Auth2ExceptionSerializer.class)
public class MethodNotAllowedException extends Auth2Exception {

	public MethodNotAllowedException(String msg, Throwable t) {
		super(msg);
	}

	@Override
	public String getOAuth2ErrorCode() {
		return "method_not_allowed";
	}

	@Override
	public int getHttpErrorCode() {
		return HttpStatus.METHOD_NOT_ALLOWED.value();
	}

}
