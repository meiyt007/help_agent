package com.zfsoft.microservice.platform.gateway.security;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {
//    @Autowired
//    private RedisTemplate<String,Object> redisTemplate;
//
    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        //从Redis中获取当前路径可访问角色列表
//        URI uri = authorizationContext.getExchange().getRequest().getURI();
//        Object obj = redisTemplate.opsForHash().get("key", uri.getPath());
//        List<String> authorities = Convert.toList(String.class,obj);
//        authorities = authorities.stream().map(i -> i = AuthConstant.AUTHORITY_PREFIX + i).collect(Collectors.toList());
        //认证通过且角色匹配的用户可访问当前路径
        return Mono.just(new AuthorizationDecision(true));
//        return mono
//                .filter(Authentication::isAuthenticated)
//                .flatMapIterable(Authentication::getAuthorities)
//                .map(GrantedAuthority::getAuthority)
//                .any(authorities::contains)
//                .map(AuthorizationDecision::new)
//                .defaultIfEmpty(new AuthorizationDecision(false));
    }
 
}
