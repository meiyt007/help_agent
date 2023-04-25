package com.zfsoft.microservice.platform.gateway.web.context;
import com.zfsoft.platform.common.security.data.AuthUserDetails;
import reactor.core.publisher.Mono;

/**
 * @Auther: lijun@sstcsoft.com
 * @Date: 2019-10-12 17:47
 */
public class ReactiveApplicationContextHolder {
    static final Class<AuthUserDetails> USER_CONTEXT_KEY = AuthUserDetails.class;

    /**
     * 设置用户信息到上下文
     * @param userDetails
     * @return
     */
    public static Mono<Void> setAuthUserDetails(AuthUserDetails userDetails){
          Mono.subscriberContext().map(ctx->ctx.put(ReactiveApplicationContextHolder.USER_CONTEXT_KEY, userDetails));
          Mono<Void> ret = Mono.empty();
          ret.subscriberContext(ctx -> ctx.put(ReactiveApplicationContextHolder.USER_CONTEXT_KEY, userDetails));
          return  ret;
    }

    /**
     * 从上下文获取用户信息
     * @return
     */
    public static Mono<AuthUserDetails> getAuthUserDetails(){
        return Mono.subscriberContext().map(context ->
                context.get(ReactiveApplicationContextHolder.USER_CONTEXT_KEY));
    }
}
