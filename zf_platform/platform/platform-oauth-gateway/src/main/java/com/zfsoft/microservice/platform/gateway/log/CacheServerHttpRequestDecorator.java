package com.zfsoft.microservice.platform.gateway.log;

/**
 * @ClassName CacheServerHttpRequestDecorator
 * @Description
 * @Author
 * @Date2020-09-29 19:19
 * @Version V1.0
 **/

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.NettyDataBuffer;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

/**
 * 对ServerHttpRequest进行二次封装，解决requestBody只能读取一次的问题
 * @author: Li Fengdi
 * @date: 2020-03-17 18:02
 */
@Slf4j
public class CacheServerHttpRequestDecorator extends ServerHttpRequestDecorator {
    private DataBuffer bodyDataBuffer;
    private int getBufferTime = 0;
    private boolean firstSubscribe = true;
    private List<byte[]> listByteArray = new ArrayList<>();


    public CacheServerHttpRequestDecorator(ServerHttpRequest delegate) {
        super(delegate);
    }

    @Override
    public Flux<DataBuffer> getBody() {
        if (getBufferTime == 0) {
            return super.getBody().map(dataBuffer -> {
                firstSubscribe = false;
                ByteBuf copy = ((NettyDataBuffer) dataBuffer).getNativeBuffer().copy();
                byte[] dst = new byte[copy.readableBytes()];
                copy.readBytes(dst);
                listByteArray.add(dst);
                copy.release();
                return dataBuffer;
            });
        } else {
            return Flux.fromIterable(listByteArray).map(bytes -> getBodyMore(bytes));
        }
    }

    private DataBuffer getBodyMore(byte[] bytes) {
        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(new UnpooledByteBufAllocator(false));
        bodyDataBuffer = nettyDataBufferFactory.wrap(bytes);
        return bodyDataBuffer;
    }

//    private DataBuffer cache(DataBuffer buffer) {
//        try {
//            InputStream dataBuffer = buffer.asInputStream();
//            bytes = IOUtils.toByteArray(dataBuffer);
//            NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(new UnpooledByteBufAllocator(false));
//            bodyDataBuffer = nettyDataBufferFactory.wrap(bytes);
//            return bodyDataBuffer;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    private void trace(ServerHttpRequest request) {
//        URI requestUri = request.getURI();
//        String uriQuery = requestUri.getQuery();
//        String url = requestUri.getPath() + (StringUtils.isNotBlank(uriQuery) ? "?" + uriQuery : "");
//        HttpHeaders headers = request.getHeaders();
//        MediaType mediaType = headers.getContentType();
//        String schema = requestUri.getScheme();
//        String method = request.getMethodValue().toUpperCase();
//        if ((!"http".equals(schema) && !"https".equals(schema))) {
//            return;
//        }
//        String reqBody = null;
//        if (Objects.nonNull(mediaType) && LogHelper.isUploadFile(mediaType)) {
//            reqBody = "上传文件";
//        } else {
//            if (method.equals("GET")) {
//                if (StringUtils.isNotBlank(uriQuery)) {
//                    reqBody = uriQuery;
//                }
//            } else if (headers.getContentLength() > 0) {
//                reqBody = LogHelper.readRequestBody(request);
//            }
//        }
//        final Log logDTO = new Log();
//        logDTO.setLevel(Log.LEVEL.INFO);
//        logDTO.setRequestUrl(url);
//        logDTO.setRequestBody(reqBody);
//        logDTO.setRequestMethod(method);
//        logDTO.setRequestId(headers.getFirst(HeaderConstant.REQUEST_ID));
//        logDTO.setIp(IpUtils.getClientIp(request));
//        log.info(LogHelper.toJsonString(logDTO));
//    }

}
