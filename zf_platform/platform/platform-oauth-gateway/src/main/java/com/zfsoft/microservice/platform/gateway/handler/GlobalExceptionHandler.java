//package com.zfsoft.microservice.platform.gateway.handler;
//
//import com.alibaba.fastjson.JSONObject;
//import com.zfsoft.platform.common.data.ApiResultSet;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//@Slf4j
//@Component
//public class GlobalExceptionHandler implements ErrorWebExceptionHandler {
//
//  @Autowired
//  private DataBufferWriter bufferWriter;
//
//  @Override
//  public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
//    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
////    AppError appError = ErrorCode.GENERIC.toAppError();
//
//      ApiResultSet apiResultSet = new ApiResultSet();
//
//    if (ex instanceof InvalidTokenException) {
//        InvalidTokenException ae = (InvalidTokenException) ex;
//        apiResultSet.setCode(401);
//        apiResultSet.setMessage(ex.getMessage());
////        status = HttpStatus.UNAUTHORIZED;
//        status = HttpStatus.OK;
//        log.debug(JSONObject.toJSONString(apiResultSet));
//    } else {
//        log.error(ex.getMessage(), ex);
//    }
//
//    if (exchange.getResponse().isCommitted()) {
//        return Mono.error(ex);
//    }
//
//    exchange.getResponse().setStatusCode(status);
//    return bufferWriter.write(exchange.getResponse(), apiResultSet);
//  }
//}