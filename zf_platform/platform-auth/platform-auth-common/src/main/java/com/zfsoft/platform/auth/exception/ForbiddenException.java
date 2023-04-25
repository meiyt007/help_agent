package com.zfsoft.platform.auth.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zfsoft.platform.auth.component.Auth2ExceptionSerializer;
import org.springframework.http.HttpStatus;

@JsonSerialize(using = Auth2ExceptionSerializer.class)
public class ForbiddenException extends Auth2Exception {

	public ForbiddenException(String msg) {
		super(msg);
	}

	public ForbiddenException(String msg, Throwable t) {
		super(msg, t);
	}

	@Override
	public String getOAuth2ErrorCode() {
		return "access_denied";
	}

	@Override
	public int getHttpErrorCode() {
		return HttpStatus.FORBIDDEN.value();
	}

}
