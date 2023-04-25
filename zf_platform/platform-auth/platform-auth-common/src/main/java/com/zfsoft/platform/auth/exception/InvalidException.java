package com.zfsoft.platform.auth.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zfsoft.platform.auth.component.Auth2ExceptionSerializer;

@JsonSerialize(using = Auth2ExceptionSerializer.class)
public class InvalidException extends Auth2Exception {

	public InvalidException(String msg, Throwable t) {
		super(msg);
	}

	@Override
	public String getOAuth2ErrorCode() {
		return "invalid_exception";
	}

//	@Override
//	public int getHttpErrorCode() {
//		return 426;
//	}

	@Override
	public int getHttpErrorCode() {
		return 200;
	}
}
