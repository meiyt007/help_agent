package com.zfsoft.microservice.platform.gateway.handler;

import com.zfsoft.platform.common.security.data.AuthUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import reactor.core.publisher.Mono;

/**
 * @author: kkfan
 * @create: 2021-02-10 09:19:09
 * @description: 用户退出
 */
@Slf4j
public class LogOutHandler implements ServerLogoutHandler {

    private TokenStore tokenStore;

    public LogOutHandler(TokenStore tokenStore){
        this.tokenStore = tokenStore;
    }

    @Override
    public Mono<Void> logout(WebFilterExchange webFilterExchange, Authentication authentication) {
        return Mono.justOrEmpty(authentication)
                .filter(a -> a instanceof OAuth2Authentication)
                .cast(OAuth2Authentication.class)
                .map(OAuth2Authentication::getPrincipal)
                .flatMap((object ->{
                    AuthUserDetails authUserDetails = (AuthUserDetails)object;
                    String accessToken = authUserDetails.getToken();
                    log.info("accessToken is :{}",accessToken);
                    OAuth2AccessToken oAuth2AccessToken = this.tokenStore.readAccessToken(accessToken);
                    //根据access_token从数据库获取不到OAuth2AccessToken
                    if(oAuth2AccessToken == null){
                        return Mono.error(new InvalidTokenException("invalid access token,please check"));
                    } else if(oAuth2AccessToken.isExpired()){
                        return Mono.error(new InvalidTokenException("access token has expired,please reacquire token"));
                    }

                    this.tokenStore.removeAccessToken(oAuth2AccessToken);
                    return Mono.empty();
                }));
    }
}
