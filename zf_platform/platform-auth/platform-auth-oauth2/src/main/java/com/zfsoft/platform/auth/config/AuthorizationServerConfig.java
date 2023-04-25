package com.zfsoft.platform.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zfsoft.platform.auth.component.CommenceAuthExceptionEntryPoint;
import com.zfsoft.platform.auth.component.WebResponseExceptionTranslator;
import com.zfsoft.platform.auth.encoder.Md5PasswordEncoder;
import com.zfsoft.platform.auth.manager.MyTokenService;
import com.zfsoft.platform.auth.manager.SecurityUserDetailsManage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@AllArgsConstructor
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    public final Md5PasswordEncoder passwordEncoder;

    @Autowired
    public final SecurityUserDetailsManage securityUserDetailsManage;

    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    private final TokenEnhancer myTokenEnhancer;

    @Autowired
    private final TokenStore redisTokenStore;

    private final ObjectMapper objectMapper;

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        MyTokenService myTokenService = new MyTokenService();
        myTokenService.setTokenEnhancer(myTokenEnhancer);
        myTokenService.setTokenStore(redisTokenStore);
        //配置授权服务处理策略
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(securityUserDetailsManage)
                .tokenStore(redisTokenStore)
                .tokenServices(myTokenService)
                .tokenEnhancer(myTokenEnhancer)
                .reuseRefreshTokens(false)
                .exceptionTranslator(new WebResponseExceptionTranslator());
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //配置网关服务的用户名密码，仅网关服务可作为客户端可访问oauth服务
        clients.inMemory()
                .withClient("gateway-client")
                .secret(passwordEncoder.encode("123456"))
                .authorizedGrantTypes("password", "refresh_token")
                .accessTokenValiditySeconds(3600)
                .refreshTokenValiditySeconds(7200)
                .scopes("all");
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //允许客户端发送表单来进行权限认证来获取令牌
        security.tokenKeyAccess("isAuthenticated()")
                //只允许认证的客户端，比如网关服务才可以获取和校验token
//                .checkTokenAccess("isAuthenticated()")
                .checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients()
                .authenticationEntryPoint(new CommenceAuthExceptionEntryPoint(objectMapper))
                .passwordEncoder(passwordEncoder);
    }


}
