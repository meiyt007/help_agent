package com.zfsoft.microservice.platform.gateway.security.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zfsoft.platform.common.security.data.AuthUserDetails;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Auther: lijun@sstcsoft.com
 * @Date: 2019-10-14 09:42
 */
@Configuration
@Slf4j
public class CustomFilterConfig {

    @Bean
    GlobalFilter customerGlobalFilter() {
        return (exchange, chain) ->
                exchange.getPrincipal()
                        .flatMap(principal -> Mono.just(Optional.of(principal)))
                        .defaultIfEmpty(Optional.empty())
                        .map(principal -> {
                            Mono<String> sessionId = exchange.getSession().flatMap(webSession -> Mono.just(webSession.getId()));
                            return sessionId.map(s -> {

                                if (principal.isPresent()) {
                                    ObjectMapper objectMapper = new ObjectMapper();
                                    try {
                                        CurrentLoginUser currentLoginUser = usernamePasswordAuthenticationToken2CurrentLoginUser((UsernamePasswordAuthenticationToken) principal.get());
                                        currentLoginUser.setClientId(s);

                                        String json = objectMapper.writeValueAsString(currentLoginUser);
                                        exchange.getRequest().mutate().header("CUSTOM-REQUEST-HEADER", URLEncoder.encode(json, "UTF-8")).build();
                                    } catch (JsonProcessingException e) {
                                        throw new RuntimeException("CurrentLoginUser transformToJson happen error!", e);
                                    } catch (UnsupportedEncodingException e) {
                                        throw new RuntimeException("CurrentLoginUser UnsupportedEncoding happen error!", e);
                                    }
                                }
                                return exchange;

                            });
                        }).flatMap(webExchange -> {
                    return webExchange.flatMap(chain::filter)
                            .doOnError(e -> {
                                log.error("序列化json出错", e);
                            });
                });
    }

    private CurrentLoginUser usernamePasswordAuthenticationToken2CurrentLoginUser(UsernamePasswordAuthenticationToken token) {
        CurrentLoginUser currentLoginUser = new CurrentLoginUser();
        AuthUserDetails user = (AuthUserDetails) token.getPrincipal();
        if (user != null) {
            currentLoginUser.setLoginOid(user.getOid());
            currentLoginUser.setUserOid(user.getUserOid());
            currentLoginUser.setDistrictOid(user.getDistrictOid());
            currentLoginUser.setOrganOid(user.getOrganOid());
            currentLoginUser.setUserCode(user.getUserCode());
            currentLoginUser.setAdviStatus(user.getAdviStatus());
            currentLoginUser.setDataAuthority(user.getDataAuthority());
            try {
                /*if(StrUtil.isNotEmpty(user.getUsername())){
                    currentLoginUser.setUserName(URLDecoder.decode(user.getUsername(), "UTF-8"));
                }
                if(StrUtil.isNotEmpty(user.getDistrictName())){
                    currentLoginUser.setDistrictName(URLDecoder.decode(user.getDistrictName(), "UTF-8"));
                }
                if(StrUtil.isNotEmpty(user.getOrganName())){
                    currentLoginUser.setOrganName(URLDecoder.decode(user.getOrganName(), "UTF-8"));
                }*/
                currentLoginUser.setUserName(user.getUsername());
                currentLoginUser.setDistrictName(user.getDistrictName());
                currentLoginUser.setOrganName(user.getOrganName());
            } catch (Exception e) {
                //编码异常暂不做处理
            }
            Collection<GrantedAuthority> grantedAuthorities = (Collection<GrantedAuthority>) user.getAuthorities();
            List<String> grantedAuthorityList = null;
            if (grantedAuthorities != null && grantedAuthorities.size() > 0) {
                grantedAuthorityList = grantedAuthorities.stream().map(item -> item.getAuthority()).collect(Collectors.toList());
            }

            currentLoginUser.setAuthorities(grantedAuthorityList);
        }
        return currentLoginUser;
    }

    //    @Bean
    public GlobalFilter customGlobalPostFilter() {
        return (exchange, chain) -> chain.filter(exchange)
                .then(Mono.just(exchange))
                .map(serverWebExchange -> {
                    //adds header to response
                    serverWebExchange.getResponse().getHeaders().set("CUSTOM-RESPONSE-HEADER",
                            HttpStatus.OK.equals(serverWebExchange.getResponse().getStatusCode()) ? "It worked" : "It did not work");
                    return serverWebExchange;
                })
                .then();
    }
}
