package com.zfsoft.platform.auth.component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.zfsoft.platform.auth.constant.CommonConstants;
import com.zfsoft.platform.auth.exception.Auth2Exception;
import lombok.SneakyThrows;

/**
 * OAuth2 异常格式化
 */
public class Auth2ExceptionSerializer extends StdSerializer<Auth2Exception> {

	public Auth2ExceptionSerializer() {
		super(Auth2Exception.class);
	}

	@Override
	@SneakyThrows
	public void serialize(Auth2Exception value, JsonGenerator gen, SerializerProvider provider) {
		gen.writeStartObject();
		gen.writeObjectField("code", CommonConstants.FAIL);
		gen.writeStringField("msg", value.getMessage());
		gen.writeStringField("data", value.getErrorCode());
		gen.writeEndObject();
	}

}
