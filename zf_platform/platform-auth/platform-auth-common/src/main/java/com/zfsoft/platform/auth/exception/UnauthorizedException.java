package com.zfsoft.platform.auth.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zfsoft.platform.auth.component.Auth2ExceptionSerializer;
import org.springframework.http.HttpStatus;

@JsonSerialize(using = Auth2ExceptionSerializer.class)
public class UnauthorizedException extends Auth2Exception {

	public UnauthorizedException(String msg, Throwable t) {
		super(msg);
	}

	@Override
	public String getOAuth2ErrorCode() {
		return "unauthorized";
	}

//	@Override
//	public int getHttpErrorCode() {
//		return HttpStatus.UNAUTHORIZED.value();
//	}

	@Override
	public int getHttpErrorCode() {
		return HttpStatus.OK.value();
	}

}
