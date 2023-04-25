//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.security.config.web.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.DelegatingReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManagerResolver;
import org.springframework.security.authorization.AuthenticatedReactiveAuthorizationManager;
import org.springframework.security.authorization.AuthorityReactiveAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthorizationCodeReactiveAuthenticationManager;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginReactiveAuthenticationManager;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.ReactiveOAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.WebClientReactiveAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.oidc.authentication.OidcAuthorizationCodeReactiveAuthenticationManager;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.web.server.AuthenticatedPrincipalServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.OAuth2AuthorizationCodeGrantWebFilter;
import org.springframework.security.oauth2.client.web.server.OAuth2AuthorizationRequestRedirectWebFilter;
import org.springframework.security.oauth2.client.web.server.ServerAuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationCodeAuthenticationTokenConverter;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.WebSessionOAuth2ServerAuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.server.authentication.OAuth2LoginAuthenticationWebFilter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoderFactory;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtReactiveAuthenticationManager;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenReactiveAuthenticationManager;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.oauth2.server.resource.introspection.NimbusReactiveOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.ReactiveOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.web.access.server.BearerTokenServerAccessDeniedHandler;
import org.springframework.security.oauth2.server.resource.web.server.BearerTokenServerAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.server.ServerBearerTokenAuthenticationConverter;
import org.springframework.security.web.PortMapper;
import org.springframework.security.web.authentication.preauth.x509.SubjectDnX509PrincipalExtractor;
import org.springframework.security.web.authentication.preauth.x509.X509PrincipalExtractor;
import org.springframework.security.web.server.DelegatingServerAuthenticationEntryPoint;
import org.springframework.security.web.server.MatcherSecurityWebFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.DelegatingServerAuthenticationEntryPoint.DelegateEntry;
import org.springframework.security.web.server.authentication.AnonymousAuthenticationWebFilter;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.HttpBasicServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.ReactivePreAuthenticatedAuthenticationManager;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.ServerFormLoginAuthenticationConverter;
import org.springframework.security.web.server.authentication.ServerHttpBasicAuthenticationConverter;
import org.springframework.security.web.server.authentication.ServerX509AuthenticationConverter;
import org.springframework.security.web.server.authentication.logout.DelegatingServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.LogoutWebFilter;
import org.springframework.security.web.server.authentication.logout.SecurityContextServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.security.web.server.authorization.AuthorizationWebFilter;
import org.springframework.security.web.server.authorization.DelegatingReactiveAuthorizationManager;
import org.springframework.security.web.server.authorization.ExceptionTranslationWebFilter;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.security.web.server.authorization.ServerWebExchangeDelegatingServerAccessDeniedHandler;
import org.springframework.security.web.server.authorization.DelegatingReactiveAuthorizationManager.Builder;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.context.ReactorContextWebFilter;
import org.springframework.security.web.server.context.SecurityContextServerWebExchangeWebFilter;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.security.web.server.csrf.CsrfServerLogoutHandler;
import org.springframework.security.web.server.csrf.CsrfWebFilter;
import org.springframework.security.web.server.csrf.ServerCsrfTokenRepository;
import org.springframework.security.web.server.csrf.WebSessionServerCsrfTokenRepository;
import org.springframework.security.web.server.header.CacheControlServerHttpHeadersWriter;
import org.springframework.security.web.server.header.CompositeServerHttpHeadersWriter;
import org.springframework.security.web.server.header.ContentSecurityPolicyServerHttpHeadersWriter;
import org.springframework.security.web.server.header.ContentTypeOptionsServerHttpHeadersWriter;
import org.springframework.security.web.server.header.FeaturePolicyServerHttpHeadersWriter;
import org.springframework.security.web.server.header.HttpHeaderWriterWebFilter;
import org.springframework.security.web.server.header.ReferrerPolicyServerHttpHeadersWriter;
import org.springframework.security.web.server.header.ServerHttpHeadersWriter;
import org.springframework.security.web.server.header.StrictTransportSecurityServerHttpHeadersWriter;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter;
import org.springframework.security.web.server.header.XXssProtectionServerHttpHeadersWriter;
import org.springframework.security.web.server.header.ReferrerPolicyServerHttpHeadersWriter.ReferrerPolicy;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter.Mode;
import org.springframework.security.web.server.savedrequest.NoOpServerRequestCache;
import org.springframework.security.web.server.savedrequest.ServerRequestCache;
import org.springframework.security.web.server.savedrequest.ServerRequestCacheWebFilter;
import org.springframework.security.web.server.savedrequest.WebSessionServerRequestCache;
import org.springframework.security.web.server.transport.HttpsRedirectWebFilter;
import org.springframework.security.web.server.ui.LoginPageGeneratingWebFilter;
import org.springframework.security.web.server.ui.LogoutPageGeneratingWebFilter;
import org.springframework.security.web.server.util.matcher.AndServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.MediaTypeServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.OrServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcherEntry;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsProcessor;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.DefaultCorsProcessor;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

public class ServerHttpSecurity {
    private ServerWebExchangeMatcher securityMatcher = ServerWebExchangeMatchers.anyExchange();
    private ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchange;
    private ServerHttpSecurity.HttpsRedirectSpec httpsRedirectSpec;
    private ServerHttpSecurity.HeaderSpec headers = new ServerHttpSecurity.HeaderSpec();
    private ServerHttpSecurity.CsrfSpec csrf = new ServerHttpSecurity.CsrfSpec();
    private ServerHttpSecurity.CorsSpec cors = new ServerHttpSecurity.CorsSpec();
    private ServerHttpSecurity.ExceptionHandlingSpec exceptionHandling = new ServerHttpSecurity.ExceptionHandlingSpec();
    private ServerHttpSecurity.HttpBasicSpec httpBasic;
    private ServerHttpSecurity.X509Spec x509;
    private final ServerHttpSecurity.RequestCacheSpec requestCache = new ServerHttpSecurity.RequestCacheSpec();
    private ServerHttpSecurity.FormLoginSpec formLogin;
    private ServerHttpSecurity.OAuth2LoginSpec oauth2Login;
    private ServerHttpSecurity.OAuth2ResourceServerSpec resourceServer;
    private ServerHttpSecurity.OAuth2ClientSpec client;
    private ServerHttpSecurity.LogoutSpec logout = new ServerHttpSecurity.LogoutSpec();
    private ServerHttpSecurity.LoginPageSpec loginPage = new ServerHttpSecurity.LoginPageSpec();
    private ReactiveAuthenticationManager authenticationManager;
    private ServerSecurityContextRepository securityContextRepository;
    private ServerAuthenticationEntryPoint authenticationEntryPoint;
    private List<DelegateEntry> defaultEntryPoints = new ArrayList();
    private ServerAccessDeniedHandler accessDeniedHandler;
    private List<org.springframework.security.web.server.authorization.ServerWebExchangeDelegatingServerAccessDeniedHandler.DelegateEntry> defaultAccessDeniedHandlers = new ArrayList();
    private List<WebFilter> webFilters = new ArrayList();
    private ApplicationContext context;
    private Throwable built;
    private ServerHttpSecurity.AnonymousSpec anonymous;

    public ServerHttpSecurity securityMatcher(ServerWebExchangeMatcher matcher) {
        Assert.notNull(matcher, "matcher cannot be null");
        this.securityMatcher = matcher;
        return this;
    }

    public ServerHttpSecurity addFilterAt(WebFilter webFilter, SecurityWebFiltersOrder order) {
        this.webFilters.add(new ServerHttpSecurity.OrderedWebFilter(webFilter, order.getOrder()));
        return this;
    }

    public ServerHttpSecurity addFilterBefore(WebFilter webFilter, SecurityWebFiltersOrder order) {
        this.webFilters.add(new ServerHttpSecurity.OrderedWebFilter(webFilter, order.getOrder() - 1));
        return this;
    }

    public ServerHttpSecurity addFilterAfter(WebFilter webFilter, SecurityWebFiltersOrder order) {
        this.webFilters.add(new ServerHttpSecurity.OrderedWebFilter(webFilter, order.getOrder() + 1));
        return this;
    }

    private ServerWebExchangeMatcher getSecurityMatcher() {
        return this.securityMatcher;
    }

    public ServerHttpSecurity securityContextRepository(ServerSecurityContextRepository securityContextRepository) {
        Assert.notNull(securityContextRepository, "securityContextRepository cannot be null");
        this.securityContextRepository = securityContextRepository;
        return this;
    }

    public ServerHttpSecurity.HttpsRedirectSpec redirectToHttps() {
        this.httpsRedirectSpec = new ServerHttpSecurity.HttpsRedirectSpec();
        return this.httpsRedirectSpec;
    }

    public ServerHttpSecurity redirectToHttps(Customizer<ServerHttpSecurity.HttpsRedirectSpec> httpsRedirectCustomizer) {
        this.httpsRedirectSpec = new ServerHttpSecurity.HttpsRedirectSpec();
        httpsRedirectCustomizer.customize(this.httpsRedirectSpec);
        return this;
    }

    public ServerHttpSecurity.CsrfSpec csrf() {
        if (this.csrf == null) {
            this.csrf = new ServerHttpSecurity.CsrfSpec();
        }

        return this.csrf;
    }

    public ServerHttpSecurity csrf(Customizer<ServerHttpSecurity.CsrfSpec> csrfCustomizer) {
        if (this.csrf == null) {
            this.csrf = new ServerHttpSecurity.CsrfSpec();
        }

        csrfCustomizer.customize(this.csrf);
        return this;
    }

    public ServerHttpSecurity.CorsSpec cors() {
        if (this.cors == null) {
            this.cors = new ServerHttpSecurity.CorsSpec();
        }

        return this.cors;
    }

    public ServerHttpSecurity cors(Customizer<ServerHttpSecurity.CorsSpec> corsCustomizer) {
        if (this.cors == null) {
            this.cors = new ServerHttpSecurity.CorsSpec();
        }

        corsCustomizer.customize(this.cors);
        return this;
    }

    public ServerHttpSecurity.AnonymousSpec anonymous() {
        if (this.anonymous == null) {
            this.anonymous = new ServerHttpSecurity.AnonymousSpec();
        }

        return this.anonymous;
    }

    public ServerHttpSecurity anonymous(Customizer<ServerHttpSecurity.AnonymousSpec> anonymousCustomizer) {
        if (this.anonymous == null) {
            this.anonymous = new ServerHttpSecurity.AnonymousSpec();
        }

        anonymousCustomizer.customize(this.anonymous);
        return this;
    }

    public ServerHttpSecurity.HttpBasicSpec httpBasic() {
        if (this.httpBasic == null) {
            this.httpBasic = new ServerHttpSecurity.HttpBasicSpec();
        }

        return this.httpBasic;
    }

    public ServerHttpSecurity httpBasic(Customizer<ServerHttpSecurity.HttpBasicSpec> httpBasicCustomizer) {
        if (this.httpBasic == null) {
            this.httpBasic = new ServerHttpSecurity.HttpBasicSpec();
        }

        httpBasicCustomizer.customize(this.httpBasic);
        return this;
    }

    public ServerHttpSecurity.FormLoginSpec formLogin() {
        if (this.formLogin == null) {
            this.formLogin = new ServerHttpSecurity.FormLoginSpec();
        }

        return this.formLogin;
    }

    public ServerHttpSecurity formLogin(Customizer<ServerHttpSecurity.FormLoginSpec> formLoginCustomizer) {
        if (this.formLogin == null) {
            this.formLogin = new ServerHttpSecurity.FormLoginSpec();
        }

        formLoginCustomizer.customize(this.formLogin);
        return this;
    }

    public ServerHttpSecurity.X509Spec x509() {
        if (this.x509 == null) {
            this.x509 = new ServerHttpSecurity.X509Spec();
        }

        return this.x509;
    }

    public ServerHttpSecurity x509(Customizer<ServerHttpSecurity.X509Spec> x509Customizer) {
        if (this.x509 == null) {
            this.x509 = new ServerHttpSecurity.X509Spec();
        }

        x509Customizer.customize(this.x509);
        return this;
    }

    public ServerHttpSecurity.OAuth2LoginSpec oauth2Login() {
        if (this.oauth2Login == null) {
            this.oauth2Login = new ServerHttpSecurity.OAuth2LoginSpec();
        }

        return this.oauth2Login;
    }

    public ServerHttpSecurity oauth2Login(Customizer<ServerHttpSecurity.OAuth2LoginSpec> oauth2LoginCustomizer) {
        if (this.oauth2Login == null) {
            this.oauth2Login = new ServerHttpSecurity.OAuth2LoginSpec();
        }

        oauth2LoginCustomizer.customize(this.oauth2Login);
        return this;
    }

    public ServerHttpSecurity.OAuth2ClientSpec oauth2Client() {
        if (this.client == null) {
            this.client = new ServerHttpSecurity.OAuth2ClientSpec();
        }

        return this.client;
    }

    public ServerHttpSecurity oauth2Client(Customizer<ServerHttpSecurity.OAuth2ClientSpec> oauth2ClientCustomizer) {
        if (this.client == null) {
            this.client = new ServerHttpSecurity.OAuth2ClientSpec();
        }

        oauth2ClientCustomizer.customize(this.client);
        return this;
    }

    public ServerHttpSecurity.OAuth2ResourceServerSpec oauth2ResourceServer() {
        if (this.resourceServer == null) {
            this.resourceServer = new ServerHttpSecurity.OAuth2ResourceServerSpec();
        }

        return this.resourceServer;
    }

    public ServerHttpSecurity oauth2ResourceServer(Customizer<ServerHttpSecurity.OAuth2ResourceServerSpec> oauth2ResourceServerCustomizer) {
        if (this.resourceServer == null) {
            this.resourceServer = new ServerHttpSecurity.OAuth2ResourceServerSpec();
        }

        oauth2ResourceServerCustomizer.customize(this.resourceServer);
        return this;
    }

    public ServerHttpSecurity.HeaderSpec headers() {
        if (this.headers == null) {
            this.headers = new ServerHttpSecurity.HeaderSpec();
        }

        return this.headers;
    }

    public ServerHttpSecurity headers(Customizer<ServerHttpSecurity.HeaderSpec> headerCustomizer) {
        if (this.headers == null) {
            this.headers = new ServerHttpSecurity.HeaderSpec();
        }

        headerCustomizer.customize(this.headers);
        return this;
    }

    public ServerHttpSecurity.ExceptionHandlingSpec exceptionHandling() {
        if (this.exceptionHandling == null) {
            this.exceptionHandling = new ServerHttpSecurity.ExceptionHandlingSpec();
        }

        return this.exceptionHandling;
    }

    public ServerHttpSecurity exceptionHandling(Customizer<ServerHttpSecurity.ExceptionHandlingSpec> exceptionHandlingCustomizer) {
        if (this.exceptionHandling == null) {
            this.exceptionHandling = new ServerHttpSecurity.ExceptionHandlingSpec();
        }

        exceptionHandlingCustomizer.customize(this.exceptionHandling);
        return this;
    }

    public ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchange() {
        if (this.authorizeExchange == null) {
            this.authorizeExchange = new ServerHttpSecurity.AuthorizeExchangeSpec();
        }

        return this.authorizeExchange;
    }

    public ServerHttpSecurity authorizeExchange(Customizer<ServerHttpSecurity.AuthorizeExchangeSpec> authorizeExchangeCustomizer) {
        if (this.authorizeExchange == null) {
            this.authorizeExchange = new ServerHttpSecurity.AuthorizeExchangeSpec();
        }

        authorizeExchangeCustomizer.customize(this.authorizeExchange);
        return this;
    }

    public ServerHttpSecurity.LogoutSpec logout() {
        if (this.logout == null) {
            this.logout = new ServerHttpSecurity.LogoutSpec();
        }

        return this.logout;
    }

    public ServerHttpSecurity logout(Customizer<ServerHttpSecurity.LogoutSpec> logoutCustomizer) {
        if (this.logout == null) {
            this.logout = new ServerHttpSecurity.LogoutSpec();
        }

        logoutCustomizer.customize(this.logout);
        return this;
    }

    public ServerHttpSecurity.RequestCacheSpec requestCache() {
        return this.requestCache;
    }

    public ServerHttpSecurity requestCache(Customizer<ServerHttpSecurity.RequestCacheSpec> requestCacheCustomizer) {
        requestCacheCustomizer.customize(this.requestCache);
        return this;
    }

    public ServerHttpSecurity authenticationManager(ReactiveAuthenticationManager manager) {
        this.authenticationManager = manager;
        return this;
    }

    public SecurityWebFilterChain build() {
        if (this.built != null) {
            throw new IllegalStateException("This has already been built with the following stacktrace. " + this.buildToString());
        } else {
            this.built = (new RuntimeException("First Build Invocation")).fillInStackTrace();
            if (this.headers != null) {
                this.headers.configure(this);
            }

            WebFilter securityContextRepositoryWebFilter = this.securityContextRepositoryWebFilter();
            this.webFilters.add(securityContextRepositoryWebFilter);
            if (this.httpsRedirectSpec != null) {
                this.httpsRedirectSpec.configure(this);
            }

            if (this.x509 != null) {
                this.x509.configure(this);
            }

            if (this.csrf != null) {
                this.csrf.configure(this);
            }

            if (this.cors != null) {
                this.cors.configure(this);
            }

            if (this.httpBasic != null) {
                if (this.httpBasic.authenticationManager == null) {
                    this.httpBasic.authenticationManager(this.authenticationManager);
                }

                if (this.httpBasic.securityContextRepository != null) {
                    this.httpBasic.securityContextRepository(this.httpBasic.securityContextRepository);
                } else if (this.securityContextRepository != null) {
                    this.httpBasic.securityContextRepository(this.securityContextRepository);
                } else {
                    this.httpBasic.securityContextRepository(NoOpServerSecurityContextRepository.getInstance());
                }

                this.httpBasic.configure(this);
            }

            if (this.formLogin != null) {
                if (this.formLogin.authenticationManager == null) {
                    this.formLogin.authenticationManager(this.authenticationManager);
                }

                if (this.formLogin.securityContextRepository != null) {
                    this.formLogin.securityContextRepository(this.formLogin.securityContextRepository);
                } else if (this.securityContextRepository != null) {
                    this.formLogin.securityContextRepository(this.securityContextRepository);
                } else {
                    this.formLogin.securityContextRepository(new WebSessionServerSecurityContextRepository());
                }

                this.formLogin.configure(this);
            }

            if (this.oauth2Login != null) {
                if (this.oauth2Login.securityContextRepository != null) {
                    this.oauth2Login.securityContextRepository(this.oauth2Login.securityContextRepository);
                } else if (this.securityContextRepository != null) {
                    this.oauth2Login.securityContextRepository(this.securityContextRepository);
                } else {
                    this.oauth2Login.securityContextRepository(new WebSessionServerSecurityContextRepository());
                }

                this.oauth2Login.configure(this);
            }

            if (this.resourceServer != null) {
                this.resourceServer.configure(this);
            }

            if (this.client != null) {
                this.client.configure(this);
            }

            if (this.anonymous != null) {
                this.anonymous.configure(this);
            }

            this.loginPage.configure(this);
            if (this.logout != null) {
                this.logout.configure(this);
            }

            this.requestCache.configure(this);
            this.addFilterAt(new SecurityContextServerWebExchangeWebFilter(), SecurityWebFiltersOrder.SECURITY_CONTEXT_SERVER_WEB_EXCHANGE);
            if (this.authorizeExchange != null) {
                ServerAuthenticationEntryPoint authenticationEntryPoint = this.getAuthenticationEntryPoint();
                ExceptionTranslationWebFilter exceptionTranslationWebFilter = new ExceptionTranslationWebFilter();
                if (authenticationEntryPoint != null) {
                    exceptionTranslationWebFilter.setAuthenticationEntryPoint(authenticationEntryPoint);
                }

                ServerAccessDeniedHandler accessDeniedHandler = this.getAccessDeniedHandler();
                if (accessDeniedHandler != null) {
                    exceptionTranslationWebFilter.setAccessDeniedHandler(accessDeniedHandler);
                }

                this.addFilterAt(exceptionTranslationWebFilter, SecurityWebFiltersOrder.EXCEPTION_TRANSLATION);
                this.authorizeExchange.configure(this);
            }

            AnnotationAwareOrderComparator.sort(this.webFilters);
            List<WebFilter> sortedWebFilters = new ArrayList();
            this.webFilters.forEach((f) -> {
                if (f instanceof ServerHttpSecurity.OrderedWebFilter) {
                    f = ((ServerHttpSecurity.OrderedWebFilter)f).webFilter;
                }

                sortedWebFilters.add(f);
            });
            sortedWebFilters.add(0, new ServerHttpSecurity.ServerWebExchangeReactorContextWebFilter());
            return new MatcherSecurityWebFilterChain(this.getSecurityMatcher(), sortedWebFilters);
        }
    }

    private String buildToString() {
        try {
            StringWriter writer = new StringWriter();
            Throwable var2 = null;

            Object var5;
            try {
                PrintWriter printer = new PrintWriter(writer);
                Throwable var4 = null;

                try {
                    printer.println();
                    printer.println();
                    this.built.printStackTrace(printer);
                    printer.println();
                    printer.println();
                    var5 = writer.toString();
                } catch (Throwable var30) {
                    var5 = var30;
                    var4 = var30;
                    throw var30;
                } finally {
                    if (printer != null) {
                        if (var4 != null) {
                            try {
                                printer.close();
                            } catch (Throwable var29) {
                                var4.addSuppressed(var29);
                            }
                        } else {
                            printer.close();
                        }
                    }

                }
            } catch (Throwable var32) {
                var2 = var32;
                throw var32;
            } finally {
                if (writer != null) {
                    if (var2 != null) {
                        try {
                            writer.close();
                        } catch (Throwable var28) {
                            var2.addSuppressed(var28);
                        }
                    } else {
                        writer.close();
                    }
                }

            }

            return (String)var5;
        } catch (IOException var34) {
            throw new RuntimeException(var34);
        }
    }

    private ServerAuthenticationEntryPoint getAuthenticationEntryPoint() {
        if (this.authenticationEntryPoint == null && !this.defaultEntryPoints.isEmpty()) {
            if (this.defaultEntryPoints.size() == 1) {
                return ((DelegateEntry)this.defaultEntryPoints.get(0)).getEntryPoint();
            } else {
                DelegatingServerAuthenticationEntryPoint result = new DelegatingServerAuthenticationEntryPoint(this.defaultEntryPoints);
                result.setDefaultEntryPoint(((DelegateEntry)this.defaultEntryPoints.get(this.defaultEntryPoints.size() - 1)).getEntryPoint());
                return result;
            }
        } else {
            return this.authenticationEntryPoint;
        }
    }

    private ServerAccessDeniedHandler getAccessDeniedHandler() {
        if (this.accessDeniedHandler == null && !this.defaultAccessDeniedHandlers.isEmpty()) {
            if (this.defaultAccessDeniedHandlers.size() == 1) {
                return ((org.springframework.security.web.server.authorization.ServerWebExchangeDelegatingServerAccessDeniedHandler.DelegateEntry)this.defaultAccessDeniedHandlers.get(0)).getAccessDeniedHandler();
            } else {
                ServerWebExchangeDelegatingServerAccessDeniedHandler result = new ServerWebExchangeDelegatingServerAccessDeniedHandler(this.defaultAccessDeniedHandlers);
                result.setDefaultAccessDeniedHandler(((org.springframework.security.web.server.authorization.ServerWebExchangeDelegatingServerAccessDeniedHandler.DelegateEntry)this.defaultAccessDeniedHandlers.get(this.defaultAccessDeniedHandlers.size() - 1)).getAccessDeniedHandler());
                return result;
            }
        } else {
            return this.accessDeniedHandler;
        }
    }

    public static ServerHttpSecurity http() {
        return new ServerHttpSecurity();
    }

    private WebFilter securityContextRepositoryWebFilter() {
        ServerSecurityContextRepository repository = this.securityContextRepository == null ? new WebSessionServerSecurityContextRepository() : this.securityContextRepository;
        WebFilter result = new ReactorContextWebFilter((ServerSecurityContextRepository)repository);
        return new ServerHttpSecurity.OrderedWebFilter(result, SecurityWebFiltersOrder.REACTOR_CONTEXT.getOrder());
    }

    protected ServerHttpSecurity() {
    }

    private <T> T getBean(Class<T> beanClass) {
        return this.context == null ? null : this.context.getBean(beanClass);
    }

    private <T> T getBeanOrNull(Class<T> beanClass) {
        return this.getBeanOrNull(ResolvableType.forClass(beanClass));
    }

    private <T> T getBeanOrNull(ResolvableType type) {
        if (this.context == null) {
            return null;
        } else {
            String[] names = this.context.getBeanNamesForType(type);
            return names.length == 1 ? (T)this.context.getBean(names[0]) : null;
        }
    }

    protected void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    public final class AnonymousSpec {
        private String key;
        private AnonymousAuthenticationWebFilter authenticationFilter;
        private Object principal;
        private List<GrantedAuthority> authorities;

        public ServerHttpSecurity.AnonymousSpec key(String key) {
            this.key = key;
            return this;
        }

        public ServerHttpSecurity.AnonymousSpec principal(Object principal) {
            this.principal = principal;
            return this;
        }

        public ServerHttpSecurity.AnonymousSpec authorities(List<GrantedAuthority> authorities) {
            this.authorities = authorities;
            return this;
        }

        public ServerHttpSecurity.AnonymousSpec authorities(String... authorities) {
            return this.authorities(AuthorityUtils.createAuthorityList(authorities));
        }

        public ServerHttpSecurity.AnonymousSpec authenticationFilter(AnonymousAuthenticationWebFilter authenticationFilter) {
            this.authenticationFilter = authenticationFilter;
            return this;
        }

        public ServerHttpSecurity and() {
            return ServerHttpSecurity.this;
        }

        public ServerHttpSecurity disable() {
            ServerHttpSecurity.this.anonymous = null;
            return ServerHttpSecurity.this;
        }

        protected void configure(ServerHttpSecurity http) {
            if (this.authenticationFilter == null) {
                this.authenticationFilter = new AnonymousAuthenticationWebFilter(this.getKey(), this.principal, this.authorities);
            }

            http.addFilterAt(this.authenticationFilter, SecurityWebFiltersOrder.ANONYMOUS_AUTHENTICATION);
        }

        private String getKey() {
            if (this.key == null) {
                this.key = UUID.randomUUID().toString();
            }

            return this.key;
        }

        private AnonymousSpec() {
            this.principal = "anonymousUser";
            this.authorities = AuthorityUtils.createAuthorityList(new String[]{"ROLE_ANONYMOUS"});
        }
    }

    static class ServerWebExchangeReactorContextWebFilter implements WebFilter {
        ServerWebExchangeReactorContextWebFilter() {
        }

        public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
            return chain.filter(exchange).subscriberContext(Context.of(ServerWebExchange.class, exchange));
        }
    }

    private static class OrderedWebFilter implements WebFilter, Ordered {
        private final WebFilter webFilter;
        private final int order;

        OrderedWebFilter(WebFilter webFilter, int order) {
            this.webFilter = webFilter;
            this.order = order;
        }

        public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
            return this.webFilter.filter(exchange, chain);
        }

        public int getOrder() {
            return this.order;
        }

        public String toString() {
            return "OrderedWebFilter{webFilter=" + this.webFilter + ", order=" + this.order + '}';
        }
    }

    public final class LogoutSpec {
        private LogoutWebFilter logoutWebFilter;
        private final SecurityContextServerLogoutHandler DEFAULT_LOGOUT_HANDLER;
        private List<ServerLogoutHandler> logoutHandlers;

        public ServerHttpSecurity.LogoutSpec logoutHandler(ServerLogoutHandler logoutHandler) {
            Assert.notNull(logoutHandler, "logoutHandler cannot be null");
            this.logoutHandlers.clear();
            return this.addLogoutHandler(logoutHandler);
        }

        private ServerHttpSecurity.LogoutSpec addLogoutHandler(ServerLogoutHandler logoutHandler) {
            Assert.notNull(logoutHandler, "logoutHandler cannot be null");
            this.logoutHandlers.add(logoutHandler);
            return this;
        }

        public ServerHttpSecurity.LogoutSpec logoutUrl(String logoutUrl) {
            Assert.notNull(logoutUrl, "logoutUrl must not be null");
            ServerWebExchangeMatcher requiresLogout = ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, new String[]{logoutUrl});
            return this.requiresLogout(requiresLogout);
        }

        public ServerHttpSecurity.LogoutSpec requiresLogout(ServerWebExchangeMatcher requiresLogout) {
            this.logoutWebFilter.setRequiresLogoutMatcher(requiresLogout);
            return this;
        }

        public ServerHttpSecurity.LogoutSpec logoutSuccessHandler(ServerLogoutSuccessHandler handler) {
            this.logoutWebFilter.setLogoutSuccessHandler(handler);
            return this;
        }

        public ServerHttpSecurity and() {
            return ServerHttpSecurity.this;
        }

        public ServerHttpSecurity disable() {
            ServerHttpSecurity.this.logout = null;
            return this.and();
        }

        private ServerLogoutHandler createLogoutHandler() {
            ServerSecurityContextRepository securityContextRepository = ServerHttpSecurity.this.securityContextRepository;
            if (securityContextRepository != null) {
                this.DEFAULT_LOGOUT_HANDLER.setSecurityContextRepository(securityContextRepository);
            }

            if (this.logoutHandlers.isEmpty()) {
                return null;
            } else {
                return (ServerLogoutHandler)(this.logoutHandlers.size() == 1 ? (ServerLogoutHandler)this.logoutHandlers.get(0) : new DelegatingServerLogoutHandler(this.logoutHandlers));
            }
        }

        protected void configure(ServerHttpSecurity http) {
            ServerLogoutHandler logoutHandler = this.createLogoutHandler();
            if (logoutHandler != null) {
                this.logoutWebFilter.setLogoutHandler(logoutHandler);
            }

            http.addFilterAt(this.logoutWebFilter, SecurityWebFiltersOrder.LOGOUT);
        }

        private LogoutSpec() {
            this.logoutWebFilter = new LogoutWebFilter();
            this.DEFAULT_LOGOUT_HANDLER = new SecurityContextServerLogoutHandler();
            this.logoutHandlers = new ArrayList(Arrays.asList(this.DEFAULT_LOGOUT_HANDLER));
        }
    }

    public class HeaderSpec {
        private final List<ServerHttpHeadersWriter> writers;
        private CacheControlServerHttpHeadersWriter cacheControl;
        private ContentTypeOptionsServerHttpHeadersWriter contentTypeOptions;
        private StrictTransportSecurityServerHttpHeadersWriter hsts;
        private XFrameOptionsServerHttpHeadersWriter frameOptions;
        private XXssProtectionServerHttpHeadersWriter xss;
        private FeaturePolicyServerHttpHeadersWriter featurePolicy;
        private ContentSecurityPolicyServerHttpHeadersWriter contentSecurityPolicy;
        private ReferrerPolicyServerHttpHeadersWriter referrerPolicy;

        public ServerHttpSecurity and() {
            return ServerHttpSecurity.this;
        }

        public ServerHttpSecurity disable() {
            ServerHttpSecurity.this.headers = null;
            return ServerHttpSecurity.this;
        }

        public ServerHttpSecurity.HeaderSpec.CacheSpec cache() {
            return new ServerHttpSecurity.HeaderSpec.CacheSpec();
        }

        public ServerHttpSecurity.HeaderSpec cache(Customizer<ServerHttpSecurity.HeaderSpec.CacheSpec> cacheCustomizer) {
            cacheCustomizer.customize(new ServerHttpSecurity.HeaderSpec.CacheSpec());
            return this;
        }

        public ServerHttpSecurity.HeaderSpec.ContentTypeOptionsSpec contentTypeOptions() {
            return new ServerHttpSecurity.HeaderSpec.ContentTypeOptionsSpec();
        }

        public ServerHttpSecurity.HeaderSpec contentTypeOptions(Customizer<ServerHttpSecurity.HeaderSpec.ContentTypeOptionsSpec> contentTypeOptionsCustomizer) {
            contentTypeOptionsCustomizer.customize(new ServerHttpSecurity.HeaderSpec.ContentTypeOptionsSpec());
            return this;
        }

        public ServerHttpSecurity.HeaderSpec.FrameOptionsSpec frameOptions() {
            return new ServerHttpSecurity.HeaderSpec.FrameOptionsSpec();
        }

        public ServerHttpSecurity.HeaderSpec frameOptions(Customizer<ServerHttpSecurity.HeaderSpec.FrameOptionsSpec> frameOptionsCustomizer) {
            frameOptionsCustomizer.customize(new ServerHttpSecurity.HeaderSpec.FrameOptionsSpec());
            return this;
        }

        public ServerHttpSecurity.HeaderSpec.HstsSpec hsts() {
            return new ServerHttpSecurity.HeaderSpec.HstsSpec();
        }

        public ServerHttpSecurity.HeaderSpec hsts(Customizer<ServerHttpSecurity.HeaderSpec.HstsSpec> hstsCustomizer) {
            hstsCustomizer.customize(new ServerHttpSecurity.HeaderSpec.HstsSpec());
            return this;
        }

        protected void configure(ServerHttpSecurity http) {
            ServerHttpHeadersWriter writer = new CompositeServerHttpHeadersWriter(this.writers);
            HttpHeaderWriterWebFilter result = new HttpHeaderWriterWebFilter(writer);
            http.addFilterAt(result, SecurityWebFiltersOrder.HTTP_HEADERS_WRITER);
        }

        public ServerHttpSecurity.HeaderSpec.XssProtectionSpec xssProtection() {
            return new ServerHttpSecurity.HeaderSpec.XssProtectionSpec();
        }

        public ServerHttpSecurity.HeaderSpec xssProtection(Customizer<ServerHttpSecurity.HeaderSpec.XssProtectionSpec> xssProtectionCustomizer) {
            xssProtectionCustomizer.customize(new ServerHttpSecurity.HeaderSpec.XssProtectionSpec());
            return this;
        }

        public ServerHttpSecurity.HeaderSpec.ContentSecurityPolicySpec contentSecurityPolicy(String policyDirectives) {
            return new ServerHttpSecurity.HeaderSpec.ContentSecurityPolicySpec(policyDirectives);
        }

        public ServerHttpSecurity.HeaderSpec contentSecurityPolicy(Customizer<ServerHttpSecurity.HeaderSpec.ContentSecurityPolicySpec> contentSecurityPolicyCustomizer) {
            contentSecurityPolicyCustomizer.customize(new ServerHttpSecurity.HeaderSpec.ContentSecurityPolicySpec());
            return this;
        }

        public ServerHttpSecurity.HeaderSpec.FeaturePolicySpec featurePolicy(String policyDirectives) {
            return new ServerHttpSecurity.HeaderSpec.FeaturePolicySpec(policyDirectives);
        }

        public ServerHttpSecurity.HeaderSpec.ReferrerPolicySpec referrerPolicy(ReferrerPolicy referrerPolicy) {
            return new ServerHttpSecurity.HeaderSpec.ReferrerPolicySpec(referrerPolicy);
        }

        public ServerHttpSecurity.HeaderSpec.ReferrerPolicySpec referrerPolicy() {
            return new ServerHttpSecurity.HeaderSpec.ReferrerPolicySpec();
        }

        public ServerHttpSecurity.HeaderSpec referrerPolicy(Customizer<ServerHttpSecurity.HeaderSpec.ReferrerPolicySpec> referrerPolicyCustomizer) {
            referrerPolicyCustomizer.customize(new ServerHttpSecurity.HeaderSpec.ReferrerPolicySpec());
            return this;
        }

        private HeaderSpec() {
            this.cacheControl = new CacheControlServerHttpHeadersWriter();
            this.contentTypeOptions = new ContentTypeOptionsServerHttpHeadersWriter();
            this.hsts = new StrictTransportSecurityServerHttpHeadersWriter();
            this.frameOptions = new XFrameOptionsServerHttpHeadersWriter();
            this.xss = new XXssProtectionServerHttpHeadersWriter();
            this.featurePolicy = new FeaturePolicyServerHttpHeadersWriter();
            this.contentSecurityPolicy = new ContentSecurityPolicyServerHttpHeadersWriter();
            this.referrerPolicy = new ReferrerPolicyServerHttpHeadersWriter();
            this.writers = new ArrayList(Arrays.asList(this.cacheControl, this.contentTypeOptions, this.hsts, this.frameOptions, this.xss, this.featurePolicy, this.contentSecurityPolicy, this.referrerPolicy));
        }

        public class ReferrerPolicySpec {
            public ServerHttpSecurity.HeaderSpec.ReferrerPolicySpec policy(ReferrerPolicy referrerPolicy) {
                HeaderSpec.this.referrerPolicy.setPolicy(referrerPolicy);
                return this;
            }

            public ServerHttpSecurity.HeaderSpec and() {
                return HeaderSpec.this;
            }

            private ReferrerPolicySpec() {
            }

            private ReferrerPolicySpec(ReferrerPolicy referrerPolicy) {
                HeaderSpec.this.referrerPolicy.setPolicy(referrerPolicy);
            }
        }

        public class FeaturePolicySpec {
            public ServerHttpSecurity.HeaderSpec and() {
                return HeaderSpec.this;
            }

            private FeaturePolicySpec(String policyDirectives) {
                HeaderSpec.this.featurePolicy.setPolicyDirectives(policyDirectives);
            }
        }

        public class ContentSecurityPolicySpec {
            private static final String DEFAULT_SRC_SELF_POLICY = "default-src 'self'";

            public ServerHttpSecurity.HeaderSpec reportOnly(boolean reportOnly) {
                HeaderSpec.this.contentSecurityPolicy.setReportOnly(reportOnly);
                return HeaderSpec.this;
            }

            public ServerHttpSecurity.HeaderSpec policyDirectives(String policyDirectives) {
                HeaderSpec.this.contentSecurityPolicy.setPolicyDirectives(policyDirectives);
                return HeaderSpec.this;
            }

            public ServerHttpSecurity.HeaderSpec and() {
                return HeaderSpec.this;
            }

            private ContentSecurityPolicySpec(String policyDirectives) {
                HeaderSpec.this.contentSecurityPolicy.setPolicyDirectives(policyDirectives);
            }

            private ContentSecurityPolicySpec() {
                HeaderSpec.this.contentSecurityPolicy.setPolicyDirectives("default-src 'self'");
            }
        }

        public class XssProtectionSpec {
            public ServerHttpSecurity.HeaderSpec disable() {
                HeaderSpec.this.writers.remove(HeaderSpec.this.xss);
                return HeaderSpec.this;
            }

            private XssProtectionSpec() {
            }
        }

        public class HstsSpec {
            public ServerHttpSecurity.HeaderSpec.HstsSpec maxAge(Duration maxAge) {
                HeaderSpec.this.hsts.setMaxAge(maxAge);
                return this;
            }

            public ServerHttpSecurity.HeaderSpec.HstsSpec includeSubdomains(boolean includeSubDomains) {
                HeaderSpec.this.hsts.setIncludeSubDomains(includeSubDomains);
                return this;
            }

            public ServerHttpSecurity.HeaderSpec.HstsSpec preload(boolean preload) {
                HeaderSpec.this.hsts.setPreload(preload);
                return this;
            }

            public ServerHttpSecurity.HeaderSpec and() {
                return HeaderSpec.this;
            }

            public ServerHttpSecurity.HeaderSpec disable() {
                HeaderSpec.this.writers.remove(HeaderSpec.this.hsts);
                return HeaderSpec.this;
            }

            private HstsSpec() {
            }
        }

        public class FrameOptionsSpec {
            public ServerHttpSecurity.HeaderSpec mode(Mode mode) {
                HeaderSpec.this.frameOptions.setMode(mode);
                return this.and();
            }

            private ServerHttpSecurity.HeaderSpec and() {
                return HeaderSpec.this;
            }

            public ServerHttpSecurity.HeaderSpec disable() {
                HeaderSpec.this.writers.remove(HeaderSpec.this.frameOptions);
                return this.and();
            }

            private FrameOptionsSpec() {
            }
        }

        public class ContentTypeOptionsSpec {
            public ServerHttpSecurity.HeaderSpec disable() {
                HeaderSpec.this.writers.remove(HeaderSpec.this.contentTypeOptions);
                return HeaderSpec.this;
            }

            private ContentTypeOptionsSpec() {
            }
        }

        public class CacheSpec {
            public ServerHttpSecurity.HeaderSpec disable() {
                HeaderSpec.this.writers.remove(HeaderSpec.this.cacheControl);
                return HeaderSpec.this;
            }

            private CacheSpec() {
            }
        }
    }

    private class LoginPageSpec {
        protected void configure(ServerHttpSecurity http) {
            if (http.authenticationEntryPoint == null) {
                if (http.formLogin == null || !http.formLogin.isEntryPointExplicit) {
                    LoginPageGeneratingWebFilter loginPage = null;
                    if (http.formLogin != null && !http.formLogin.isEntryPointExplicit) {
                        loginPage = new LoginPageGeneratingWebFilter();
                        loginPage.setFormLoginEnabled(true);
                    }

                    if (http.oauth2Login != null) {
                        Map<String, String> urlToText = http.oauth2Login.getLinks();
                        if (loginPage == null) {
                            loginPage = new LoginPageGeneratingWebFilter();
                        }

                        loginPage.setOauth2AuthenticationUrlToClientName(urlToText);
                    }

                    if (loginPage != null) {
                        http.addFilterAt(loginPage, SecurityWebFiltersOrder.LOGIN_PAGE_GENERATING);
                        http.addFilterAt(new LogoutPageGeneratingWebFilter(), SecurityWebFiltersOrder.LOGOUT_PAGE_GENERATING);
                    }

                }
            }
        }

        private LoginPageSpec() {
        }
    }

    public class FormLoginSpec {
        private final RedirectServerAuthenticationSuccessHandler defaultSuccessHandler;
        private RedirectServerAuthenticationEntryPoint defaultEntryPoint;
        private ReactiveAuthenticationManager authenticationManager;
        private ServerSecurityContextRepository securityContextRepository;
        private ServerAuthenticationEntryPoint authenticationEntryPoint;
        private boolean isEntryPointExplicit;
        private ServerWebExchangeMatcher requiresAuthenticationMatcher;
        private ServerAuthenticationFailureHandler authenticationFailureHandler;
        private ServerAuthenticationSuccessHandler authenticationSuccessHandler;
        private ServerAuthenticationConverter authenticationConverter;

        public ServerHttpSecurity.FormLoginSpec authenticationManager(ReactiveAuthenticationManager authenticationManager) {
            this.authenticationManager = authenticationManager;
            return this;
        }

        public ServerHttpSecurity.FormLoginSpec authenticationSuccessHandler(ServerAuthenticationSuccessHandler authenticationSuccessHandler) {
            Assert.notNull(authenticationSuccessHandler, "authenticationSuccessHandler cannot be null");
            this.authenticationSuccessHandler = authenticationSuccessHandler;
            return this;
        }

        public ServerHttpSecurity.FormLoginSpec authenticationConverter(ServerAuthenticationConverter authenticationConverter){
            Assert.notNull(authenticationConverter, "authenticationConverter cannot be null");
            this.authenticationConverter = authenticationConverter;
            return this;
        }

        public ServerHttpSecurity.FormLoginSpec loginPage(String loginPage) {
            this.defaultEntryPoint = new RedirectServerAuthenticationEntryPoint(loginPage);
            this.authenticationEntryPoint = this.defaultEntryPoint;
            if (this.requiresAuthenticationMatcher == null) {
                this.requiresAuthenticationMatcher = ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, new String[]{loginPage});
            }

            if (this.authenticationFailureHandler == null) {
                this.authenticationFailureHandler = new RedirectServerAuthenticationFailureHandler(loginPage + "?error");
            }

            return this;
        }

        public ServerHttpSecurity.FormLoginSpec authenticationEntryPoint(ServerAuthenticationEntryPoint authenticationEntryPoint) {
            this.authenticationEntryPoint = authenticationEntryPoint;
            return this;
        }

        public ServerHttpSecurity.FormLoginSpec requiresAuthenticationMatcher(ServerWebExchangeMatcher requiresAuthenticationMatcher) {
            this.requiresAuthenticationMatcher = requiresAuthenticationMatcher;
            return this;
        }

        public ServerHttpSecurity.FormLoginSpec authenticationFailureHandler(ServerAuthenticationFailureHandler authenticationFailureHandler) {
            this.authenticationFailureHandler = authenticationFailureHandler;
            return this;
        }

        public ServerHttpSecurity.FormLoginSpec securityContextRepository(ServerSecurityContextRepository securityContextRepository) {
            this.securityContextRepository = securityContextRepository;
            return this;
        }

        public ServerHttpSecurity and() {
            return ServerHttpSecurity.this;
        }

        public ServerHttpSecurity disable() {
            ServerHttpSecurity.this.formLogin = null;
            return ServerHttpSecurity.this;
        }

        protected void configure(ServerHttpSecurity http) {
            if (this.authenticationEntryPoint == null) {
                this.isEntryPointExplicit = false;
                this.loginPage("/login");
            } else {
                this.isEntryPointExplicit = true;
            }

            if (http.requestCache != null) {
                ServerRequestCache requestCache = http.requestCache.requestCache;
                this.defaultSuccessHandler.setRequestCache(requestCache);
                if (this.defaultEntryPoint != null) {
                    this.defaultEntryPoint.setRequestCache(requestCache);
                }
            }

            MediaTypeServerWebExchangeMatcher htmlMatcher = new MediaTypeServerWebExchangeMatcher(new MediaType[]{MediaType.TEXT_HTML});
            htmlMatcher.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));
            ServerHttpSecurity.this.defaultEntryPoints.add(0, new DelegateEntry(htmlMatcher, this.authenticationEntryPoint));
            AuthenticationWebFilter authenticationFilter = new AuthenticationWebFilter(this.authenticationManager);
            authenticationFilter.setRequiresAuthenticationMatcher(this.requiresAuthenticationMatcher);
            authenticationFilter.setAuthenticationFailureHandler(this.authenticationFailureHandler);
            authenticationFilter.setServerAuthenticationConverter(this.authenticationConverter);
            authenticationFilter.setAuthenticationSuccessHandler(this.authenticationSuccessHandler);
            authenticationFilter.setSecurityContextRepository(this.securityContextRepository);
            http.addFilterAt(authenticationFilter, SecurityWebFiltersOrder.FORM_LOGIN);
        }

        public FormLoginSpec() {
            this.defaultSuccessHandler = new RedirectServerAuthenticationSuccessHandler("/");
            this.authenticationSuccessHandler = this.defaultSuccessHandler;
        }
    }

    public class HttpBasicSpec {
        private ReactiveAuthenticationManager authenticationManager;
        private ServerSecurityContextRepository securityContextRepository;
        private ServerAuthenticationEntryPoint entryPoint;

        public ServerHttpSecurity.HttpBasicSpec authenticationManager(ReactiveAuthenticationManager authenticationManager) {
            this.authenticationManager = authenticationManager;
            return this;
        }

        public ServerHttpSecurity.HttpBasicSpec securityContextRepository(ServerSecurityContextRepository securityContextRepository) {
            this.securityContextRepository = securityContextRepository;
            return this;
        }

        public ServerHttpSecurity.HttpBasicSpec authenticationEntryPoint(ServerAuthenticationEntryPoint authenticationEntryPoint) {
            Assert.notNull(authenticationEntryPoint, "authenticationEntryPoint cannot be null");
            this.entryPoint = authenticationEntryPoint;
            return this;
        }

        public ServerHttpSecurity and() {
            return ServerHttpSecurity.this;
        }

        public ServerHttpSecurity disable() {
            ServerHttpSecurity.this.httpBasic = null;
            return ServerHttpSecurity.this;
        }

        protected void configure(ServerHttpSecurity http) {
            MediaTypeServerWebExchangeMatcher restMatcher = new MediaTypeServerWebExchangeMatcher(new MediaType[]{MediaType.APPLICATION_ATOM_XML, MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_XML, MediaType.MULTIPART_FORM_DATA, MediaType.TEXT_XML});
            restMatcher.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));
            ServerHttpSecurity.this.defaultEntryPoints.add(new DelegateEntry(restMatcher, this.entryPoint));
            AuthenticationWebFilter authenticationFilter = new AuthenticationWebFilter(this.authenticationManager);
            authenticationFilter.setAuthenticationFailureHandler(new ServerAuthenticationEntryPointFailureHandler(this.entryPoint));
            authenticationFilter.setAuthenticationConverter(new ServerHttpBasicAuthenticationConverter());
            authenticationFilter.setSecurityContextRepository(this.securityContextRepository);
            http.addFilterAt(authenticationFilter, SecurityWebFiltersOrder.HTTP_BASIC);
        }

        private HttpBasicSpec() {
            this.entryPoint = new HttpBasicServerAuthenticationEntryPoint();
        }
    }

    public class RequestCacheSpec {
        private ServerRequestCache requestCache;

        public ServerHttpSecurity.RequestCacheSpec requestCache(ServerRequestCache requestCache) {
            Assert.notNull(requestCache, "requestCache cannot be null");
            this.requestCache = requestCache;
            return this;
        }

        protected void configure(ServerHttpSecurity http) {
            ServerRequestCacheWebFilter filter = new ServerRequestCacheWebFilter();
            filter.setRequestCache(this.requestCache);
            http.addFilterAt(filter, SecurityWebFiltersOrder.SERVER_REQUEST_CACHE);
        }

        public ServerHttpSecurity and() {
            return ServerHttpSecurity.this;
        }

        public ServerHttpSecurity disable() {
            this.requestCache = NoOpServerRequestCache.getInstance();
            return this.and();
        }

        private RequestCacheSpec() {
            this.requestCache = new WebSessionServerRequestCache();
        }
    }

    public class ExceptionHandlingSpec {
        public ServerHttpSecurity.ExceptionHandlingSpec authenticationEntryPoint(ServerAuthenticationEntryPoint authenticationEntryPoint) {
            ServerHttpSecurity.this.authenticationEntryPoint = authenticationEntryPoint;
            return this;
        }

        public ServerHttpSecurity.ExceptionHandlingSpec accessDeniedHandler(ServerAccessDeniedHandler accessDeniedHandler) {
            ServerHttpSecurity.this.accessDeniedHandler = accessDeniedHandler;
            return this;
        }

        public ServerHttpSecurity and() {
            return ServerHttpSecurity.this;
        }

        private ExceptionHandlingSpec() {
        }
    }

    public class CsrfSpec {
        private CsrfWebFilter filter;
        private ServerCsrfTokenRepository csrfTokenRepository;
        private boolean specifiedRequireCsrfProtectionMatcher;

        public ServerHttpSecurity.CsrfSpec accessDeniedHandler(ServerAccessDeniedHandler accessDeniedHandler) {
            this.filter.setAccessDeniedHandler(accessDeniedHandler);
            return this;
        }

        public ServerHttpSecurity.CsrfSpec csrfTokenRepository(ServerCsrfTokenRepository csrfTokenRepository) {
            this.csrfTokenRepository = csrfTokenRepository;
            return this;
        }

        public ServerHttpSecurity.CsrfSpec requireCsrfProtectionMatcher(ServerWebExchangeMatcher requireCsrfProtectionMatcher) {
            this.filter.setRequireCsrfProtectionMatcher(requireCsrfProtectionMatcher);
            this.specifiedRequireCsrfProtectionMatcher = true;
            return this;
        }

        public ServerHttpSecurity.CsrfSpec tokenFromMultipartDataEnabled(boolean enabled) {
            this.filter.setTokenFromMultipartDataEnabled(enabled);
            return this;
        }

        public ServerHttpSecurity and() {
            return ServerHttpSecurity.this;
        }

        public ServerHttpSecurity disable() {
            ServerHttpSecurity.this.csrf = null;
            return ServerHttpSecurity.this;
        }

        protected void configure(ServerHttpSecurity http) {
            if (this.csrfTokenRepository != null) {
                this.filter.setCsrfTokenRepository(this.csrfTokenRepository);
                if (ServerHttpSecurity.this.logout != null) {
                    ServerHttpSecurity.this.logout.addLogoutHandler(new CsrfServerLogoutHandler(this.csrfTokenRepository));
                }
            }

            http.addFilterAt(this.filter, SecurityWebFiltersOrder.CSRF);
        }

        private CsrfSpec() {
            this.filter = new CsrfWebFilter();
            this.csrfTokenRepository = new WebSessionServerCsrfTokenRepository();
        }
    }

    public class HttpsRedirectSpec {
        private ServerWebExchangeMatcher serverWebExchangeMatcher;
        private PortMapper portMapper;

        public HttpsRedirectSpec() {
        }

        public ServerHttpSecurity.HttpsRedirectSpec httpsRedirectWhen(ServerWebExchangeMatcher... matchers) {
            this.serverWebExchangeMatcher = new OrServerWebExchangeMatcher(matchers);
            return this;
        }

        public ServerHttpSecurity.HttpsRedirectSpec httpsRedirectWhen(Function<ServerWebExchange, Boolean> when) {
            ServerWebExchangeMatcher matcher = (e) -> {
                return (Boolean)when.apply(e) ? MatchResult.match() : MatchResult.notMatch();
            };
            return this.httpsRedirectWhen(matcher);
        }

        public ServerHttpSecurity.HttpsRedirectSpec portMapper(PortMapper portMapper) {
            this.portMapper = portMapper;
            return this;
        }

        protected void configure(ServerHttpSecurity http) {
            HttpsRedirectWebFilter httpsRedirectWebFilter = new HttpsRedirectWebFilter();
            if (this.serverWebExchangeMatcher != null) {
                httpsRedirectWebFilter.setRequiresHttpsRedirectMatcher(this.serverWebExchangeMatcher);
            }

            if (this.portMapper != null) {
                httpsRedirectWebFilter.setPortMapper(this.portMapper);
            }

            http.addFilterAt(httpsRedirectWebFilter, SecurityWebFiltersOrder.HTTPS_REDIRECT);
        }

        public ServerHttpSecurity and() {
            return ServerHttpSecurity.this;
        }
    }

    public class AuthorizeExchangeSpec extends AbstractServerWebExchangeMatcherRegistry<ServerHttpSecurity.AuthorizeExchangeSpec.Access> {
        private Builder managerBldr = DelegatingReactiveAuthorizationManager.builder();
        private ServerWebExchangeMatcher matcher;
        private boolean anyExchangeRegistered;

        public AuthorizeExchangeSpec() {
        }

        public ServerHttpSecurity and() {
            return ServerHttpSecurity.this;
        }

        public ServerHttpSecurity.AuthorizeExchangeSpec.Access anyExchange() {
            ServerHttpSecurity.AuthorizeExchangeSpec.Access result = (ServerHttpSecurity.AuthorizeExchangeSpec.Access)super.anyExchange();
            this.anyExchangeRegistered = true;
            return result;
        }

        protected ServerHttpSecurity.AuthorizeExchangeSpec.Access registerMatcher(ServerWebExchangeMatcher matcher) {
            if (this.anyExchangeRegistered) {
                throw new IllegalStateException("Cannot register " + matcher + " which would be unreachable because anyExchange() has already been registered.");
            } else if (this.matcher != null) {
                throw new IllegalStateException("The matcher " + matcher + " does not have an access rule defined");
            } else {
                this.matcher = matcher;
                return new ServerHttpSecurity.AuthorizeExchangeSpec.Access();
            }
        }

        protected void configure(ServerHttpSecurity http) {
            if (this.matcher != null) {
                throw new IllegalStateException("The matcher " + this.matcher + " does not have an access rule defined");
            } else {
                AuthorizationWebFilter result = new AuthorizationWebFilter(this.managerBldr.build());
                http.addFilterAt(result, SecurityWebFiltersOrder.AUTHORIZATION);
            }
        }

        public final class Access {
            public Access() {
            }

            public ServerHttpSecurity.AuthorizeExchangeSpec permitAll() {
                return this.access((a, e) -> {
                    return Mono.just(new AuthorizationDecision(true));
                });
            }

            public ServerHttpSecurity.AuthorizeExchangeSpec denyAll() {
                return this.access((a, e) -> {
                    return Mono.just(new AuthorizationDecision(false));
                });
            }

            public ServerHttpSecurity.AuthorizeExchangeSpec hasRole(String role) {
                return this.access(AuthorityReactiveAuthorizationManager.hasRole(role));
            }

            public ServerHttpSecurity.AuthorizeExchangeSpec hasAnyRole(String... roles) {
                return this.access(AuthorityReactiveAuthorizationManager.hasAnyRole(roles));
            }

            public ServerHttpSecurity.AuthorizeExchangeSpec hasAuthority(String authority) {
                return this.access(AuthorityReactiveAuthorizationManager.hasAuthority(authority));
            }

            public ServerHttpSecurity.AuthorizeExchangeSpec hasAnyAuthority(String... authorities) {
                return this.access(AuthorityReactiveAuthorizationManager.hasAnyAuthority(authorities));
            }

            public ServerHttpSecurity.AuthorizeExchangeSpec authenticated() {
                return this.access(AuthenticatedReactiveAuthorizationManager.authenticated());
            }

            public ServerHttpSecurity.AuthorizeExchangeSpec access(ReactiveAuthorizationManager<AuthorizationContext> manager) {
                AuthorizeExchangeSpec.this.managerBldr.add(new ServerWebExchangeMatcherEntry(AuthorizeExchangeSpec.this.matcher, manager));
                AuthorizeExchangeSpec.this.matcher = null;
                return AuthorizeExchangeSpec.this;
            }
        }
    }

    public class OAuth2ResourceServerSpec {
        private ServerAuthenticationEntryPoint entryPoint = new BearerTokenServerAuthenticationEntryPoint();
        private ServerAccessDeniedHandler accessDeniedHandler = new BearerTokenServerAccessDeniedHandler();
        private ServerAuthenticationConverter bearerTokenConverter = new ServerBearerTokenAuthenticationConverter();
        private ServerHttpSecurity.OAuth2ResourceServerSpec.BearerTokenServerWebExchangeMatcher bearerTokenServerWebExchangeMatcher = new ServerHttpSecurity.OAuth2ResourceServerSpec.BearerTokenServerWebExchangeMatcher();
        private ServerHttpSecurity.OAuth2ResourceServerSpec.JwtSpec jwt;
        private ServerHttpSecurity.OAuth2ResourceServerSpec.OpaqueTokenSpec opaqueToken;
        private ReactiveAuthenticationManagerResolver<ServerHttpRequest> authenticationManagerResolver;

        public OAuth2ResourceServerSpec() {
        }

        public ServerHttpSecurity.OAuth2ResourceServerSpec accessDeniedHandler(ServerAccessDeniedHandler accessDeniedHandler) {
            Assert.notNull(accessDeniedHandler, "accessDeniedHandler cannot be null");
            this.accessDeniedHandler = accessDeniedHandler;
            return this;
        }

        public ServerHttpSecurity.OAuth2ResourceServerSpec authenticationEntryPoint(ServerAuthenticationEntryPoint entryPoint) {
            Assert.notNull(entryPoint, "entryPoint cannot be null");
            this.entryPoint = entryPoint;
            return this;
        }

        public ServerHttpSecurity.OAuth2ResourceServerSpec bearerTokenConverter(ServerAuthenticationConverter bearerTokenConverter) {
            Assert.notNull(bearerTokenConverter, "bearerTokenConverter cannot be null");
            this.bearerTokenConverter = bearerTokenConverter;
            return this;
        }

        public ServerHttpSecurity.OAuth2ResourceServerSpec authenticationManagerResolver(ReactiveAuthenticationManagerResolver<ServerHttpRequest> authenticationManagerResolver) {
            Assert.notNull(authenticationManagerResolver, "authenticationManagerResolver cannot be null");
            this.authenticationManagerResolver = authenticationManagerResolver;
            return this;
        }

        public ServerHttpSecurity.OAuth2ResourceServerSpec.JwtSpec jwt() {
            if (this.jwt == null) {
                this.jwt = new ServerHttpSecurity.OAuth2ResourceServerSpec.JwtSpec();
            }

            return this.jwt;
        }

        public ServerHttpSecurity.OAuth2ResourceServerSpec jwt(Customizer<ServerHttpSecurity.OAuth2ResourceServerSpec.JwtSpec> jwtCustomizer) {
            if (this.jwt == null) {
                this.jwt = new ServerHttpSecurity.OAuth2ResourceServerSpec.JwtSpec();
            }

            jwtCustomizer.customize(this.jwt);
            return this;
        }

        public ServerHttpSecurity.OAuth2ResourceServerSpec.OpaqueTokenSpec opaqueToken() {
            if (this.opaqueToken == null) {
                this.opaqueToken = new ServerHttpSecurity.OAuth2ResourceServerSpec.OpaqueTokenSpec();
            }

            return this.opaqueToken;
        }

        public ServerHttpSecurity.OAuth2ResourceServerSpec opaqueToken(Customizer<ServerHttpSecurity.OAuth2ResourceServerSpec.OpaqueTokenSpec> opaqueTokenCustomizer) {
            if (this.opaqueToken == null) {
                this.opaqueToken = new ServerHttpSecurity.OAuth2ResourceServerSpec.OpaqueTokenSpec();
            }

            opaqueTokenCustomizer.customize(this.opaqueToken);
            return this;
        }

        protected void configure(ServerHttpSecurity http) {
            this.bearerTokenServerWebExchangeMatcher.setBearerTokenConverter(this.bearerTokenConverter);
            this.registerDefaultAccessDeniedHandler(http);
            this.registerDefaultAuthenticationEntryPoint(http);
            this.registerDefaultCsrfOverride(http);
            this.validateConfiguration();
            if (this.authenticationManagerResolver != null) {
                AuthenticationWebFilter oauth2 = new AuthenticationWebFilter(this.authenticationManagerResolver);
                oauth2.setServerAuthenticationConverter(this.bearerTokenConverter);
                oauth2.setAuthenticationFailureHandler(new ServerAuthenticationEntryPointFailureHandler(this.entryPoint));
                http.addFilterAt(oauth2, SecurityWebFiltersOrder.AUTHENTICATION);
            } else if (this.jwt != null) {
                this.jwt.configure(http);
            } else if (this.opaqueToken != null) {
                this.opaqueToken.configure(http);
            }

        }

        private void validateConfiguration() {
            if (this.authenticationManagerResolver == null) {
                if (this.jwt == null && this.opaqueToken == null) {
                    throw new IllegalStateException("Jwt and Opaque Token are the only supported formats for bearer tokens in Spring Security and neither was found. Make sure to configure JWT via http.oauth2ResourceServer().jwt() or Opaque Tokens via http.oauth2ResourceServer().opaqueToken().");
                }

                if (this.jwt != null && this.opaqueToken != null) {
                    throw new IllegalStateException("Spring Security only supports JWTs or Opaque Tokens, not both at the same time.");
                }
            } else if (this.jwt != null || this.opaqueToken != null) {
                throw new IllegalStateException("If an authenticationManagerResolver() is configured, then it takes precedence over any jwt() or opaqueToken() configuration.");
            }

        }

        private void registerDefaultAccessDeniedHandler(ServerHttpSecurity http) {
            if (http.exceptionHandling != null) {
                http.defaultAccessDeniedHandlers.add(new org.springframework.security.web.server.authorization.ServerWebExchangeDelegatingServerAccessDeniedHandler.DelegateEntry(this.bearerTokenServerWebExchangeMatcher, this.accessDeniedHandler));
            }

        }

        private void registerDefaultAuthenticationEntryPoint(ServerHttpSecurity http) {
            if (http.exceptionHandling != null) {
                http.defaultEntryPoints.add(new DelegateEntry(this.bearerTokenServerWebExchangeMatcher, this.entryPoint));
            }

        }

        private void registerDefaultCsrfOverride(ServerHttpSecurity http) {
            if (http.csrf != null && !http.csrf.specifiedRequireCsrfProtectionMatcher) {
                http.csrf().requireCsrfProtectionMatcher(new AndServerWebExchangeMatcher(new ServerWebExchangeMatcher[]{CsrfWebFilter.DEFAULT_CSRF_MATCHER, new NegatedServerWebExchangeMatcher(this.bearerTokenServerWebExchangeMatcher)}));
            }

        }

        public ServerHttpSecurity and() {
            return ServerHttpSecurity.this;
        }

        public class OpaqueTokenSpec {
            private String introspectionUri;
            private String clientId;
            private String clientSecret;
            private Supplier<ReactiveOpaqueTokenIntrospector> introspector;

            public ServerHttpSecurity.OAuth2ResourceServerSpec.OpaqueTokenSpec introspectionUri(String introspectionUri) {
                Assert.hasText(introspectionUri, "introspectionUri cannot be empty");
                this.introspectionUri = introspectionUri;
                this.introspector = () -> {
                    return new NimbusReactiveOpaqueTokenIntrospector(this.introspectionUri, this.clientId, this.clientSecret);
                };
                return this;
            }

            public ServerHttpSecurity.OAuth2ResourceServerSpec.OpaqueTokenSpec introspectionClientCredentials(String clientId, String clientSecret) {
                Assert.hasText(clientId, "clientId cannot be empty");
                Assert.notNull(clientSecret, "clientSecret cannot be null");
                this.clientId = clientId;
                this.clientSecret = clientSecret;
                this.introspector = () -> {
                    return new NimbusReactiveOpaqueTokenIntrospector(this.introspectionUri, this.clientId, this.clientSecret);
                };
                return this;
            }

            public ServerHttpSecurity.OAuth2ResourceServerSpec.OpaqueTokenSpec introspector(ReactiveOpaqueTokenIntrospector introspector) {
                Assert.notNull(introspector, "introspector cannot be null");
                this.introspector = () -> {
                    return introspector;
                };
                return this;
            }

            public ServerHttpSecurity.OAuth2ResourceServerSpec and() {
                return OAuth2ResourceServerSpec.this;
            }

            protected ReactiveAuthenticationManager getAuthenticationManager() {
                return new OpaqueTokenReactiveAuthenticationManager(this.getIntrospector());
            }

            protected ReactiveOpaqueTokenIntrospector getIntrospector() {
                return this.introspector != null ? (ReactiveOpaqueTokenIntrospector)this.introspector.get() : (ReactiveOpaqueTokenIntrospector)ServerHttpSecurity.this.getBean(ReactiveOpaqueTokenIntrospector.class);
            }

            protected void configure(ServerHttpSecurity http) {
                ReactiveAuthenticationManager authenticationManager = this.getAuthenticationManager();
                AuthenticationWebFilter oauth2 = OAuth2ResourceServerSpec.this.new BearerTokenAuthenticationWebFilter(authenticationManager);
                oauth2.setServerAuthenticationConverter(OAuth2ResourceServerSpec.this.bearerTokenConverter);
                oauth2.setAuthenticationFailureHandler(new ServerAuthenticationEntryPointFailureHandler(OAuth2ResourceServerSpec.this.entryPoint));
                http.addFilterAt(oauth2, SecurityWebFiltersOrder.AUTHENTICATION);
            }

            private OpaqueTokenSpec() {
            }
        }

        public class JwtSpec {
            private ReactiveAuthenticationManager authenticationManager;
            private ReactiveJwtDecoder jwtDecoder;
            private Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter = new ReactiveJwtAuthenticationConverterAdapter(new JwtAuthenticationConverter());

            public JwtSpec() {
            }

            public ServerHttpSecurity.OAuth2ResourceServerSpec.JwtSpec authenticationManager(ReactiveAuthenticationManager authenticationManager) {
                Assert.notNull(authenticationManager, "authenticationManager cannot be null");
                this.authenticationManager = authenticationManager;
                return this;
            }

            public ServerHttpSecurity.OAuth2ResourceServerSpec.JwtSpec jwtAuthenticationConverter(Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter) {
                Assert.notNull(jwtAuthenticationConverter, "jwtAuthenticationConverter cannot be null");
                this.jwtAuthenticationConverter = jwtAuthenticationConverter;
                return this;
            }

            public ServerHttpSecurity.OAuth2ResourceServerSpec.JwtSpec jwtDecoder(ReactiveJwtDecoder jwtDecoder) {
                this.jwtDecoder = jwtDecoder;
                return this;
            }

            public ServerHttpSecurity.OAuth2ResourceServerSpec.JwtSpec publicKey(RSAPublicKey publicKey) {
                this.jwtDecoder = new NimbusReactiveJwtDecoder(publicKey);
                return this;
            }

            public ServerHttpSecurity.OAuth2ResourceServerSpec.JwtSpec jwkSetUri(String jwkSetUri) {
                this.jwtDecoder = new NimbusReactiveJwtDecoder(jwkSetUri);
                return this;
            }

            public ServerHttpSecurity.OAuth2ResourceServerSpec and() {
                return OAuth2ResourceServerSpec.this;
            }

            protected void configure(ServerHttpSecurity http) {
                ReactiveAuthenticationManager authenticationManager = this.getAuthenticationManager();
                AuthenticationWebFilter oauth2 = OAuth2ResourceServerSpec.this.new BearerTokenAuthenticationWebFilter(authenticationManager);
                oauth2.setServerAuthenticationConverter(OAuth2ResourceServerSpec.this.bearerTokenConverter);
                oauth2.setAuthenticationFailureHandler(new ServerAuthenticationEntryPointFailureHandler(OAuth2ResourceServerSpec.this.entryPoint));
                http.addFilterAt(oauth2, SecurityWebFiltersOrder.AUTHENTICATION);
            }

            protected ReactiveJwtDecoder getJwtDecoder() {
                return this.jwtDecoder == null ? (ReactiveJwtDecoder)ServerHttpSecurity.this.getBean(ReactiveJwtDecoder.class) : this.jwtDecoder;
            }

            protected Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> getJwtAuthenticationConverter() {
                return this.jwtAuthenticationConverter;
            }

            private ReactiveAuthenticationManager getAuthenticationManager() {
                if (this.authenticationManager != null) {
                    return this.authenticationManager;
                } else {
                    ReactiveJwtDecoder jwtDecoder = this.getJwtDecoder();
                    Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter = this.getJwtAuthenticationConverter();
                    JwtReactiveAuthenticationManager authenticationManager = new JwtReactiveAuthenticationManager(jwtDecoder);
                    authenticationManager.setJwtAuthenticationConverter(jwtAuthenticationConverter);
                    return authenticationManager;
                }
            }
        }

        private class BearerTokenAuthenticationWebFilter extends AuthenticationWebFilter {
            private ServerAuthenticationFailureHandler authenticationFailureHandler;

            BearerTokenAuthenticationWebFilter(ReactiveAuthenticationManager authenticationManager) {
                super(authenticationManager);
            }

            public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
                WebFilterExchange webFilterExchange = new WebFilterExchange(exchange, chain);
                return super.filter(exchange, chain).onErrorResume(AuthenticationException.class, (e) -> {
                    return this.authenticationFailureHandler.onAuthenticationFailure(webFilterExchange, e);
                });
            }

            public void setAuthenticationFailureHandler(ServerAuthenticationFailureHandler authenticationFailureHandler) {
                super.setAuthenticationFailureHandler(authenticationFailureHandler);
                this.authenticationFailureHandler = authenticationFailureHandler;
            }
        }

        private class BearerTokenServerWebExchangeMatcher implements ServerWebExchangeMatcher {
            ServerAuthenticationConverter bearerTokenConverter;

            private BearerTokenServerWebExchangeMatcher() {
            }

            public Mono<MatchResult> matches(ServerWebExchange exchange) {
                return this.bearerTokenConverter.convert(exchange).flatMap(this::nullAuthentication).onErrorResume((e) -> {
                    return MatchResult.notMatch();
                });
            }

            public void setBearerTokenConverter(ServerAuthenticationConverter bearerTokenConverter) {
                Assert.notNull(bearerTokenConverter, "bearerTokenConverter cannot be null");
                this.bearerTokenConverter = bearerTokenConverter;
            }

            private Mono<MatchResult> nullAuthentication(Authentication authentication) {
                return authentication == null ? MatchResult.notMatch() : MatchResult.match();
            }
        }
    }

    public class OAuth2ClientSpec {
        private ReactiveClientRegistrationRepository clientRegistrationRepository;
        private ServerAuthenticationConverter authenticationConverter;
        private ServerOAuth2AuthorizedClientRepository authorizedClientRepository;
        private ReactiveAuthenticationManager authenticationManager;
        private ServerAuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository;

        public ServerHttpSecurity.OAuth2ClientSpec authenticationConverter(ServerAuthenticationConverter authenticationConverter) {
            this.authenticationConverter = authenticationConverter;
            return this;
        }

        private ServerAuthenticationConverter getAuthenticationConverter() {
            if (this.authenticationConverter == null) {
                ServerOAuth2AuthorizationCodeAuthenticationTokenConverter authenticationConverter = new ServerOAuth2AuthorizationCodeAuthenticationTokenConverter(this.getClientRegistrationRepository());
                authenticationConverter.setAuthorizationRequestRepository(this.getAuthorizationRequestRepository());
                this.authenticationConverter = authenticationConverter;
            }

            return this.authenticationConverter;
        }

        public ServerHttpSecurity.OAuth2ClientSpec authenticationManager(ReactiveAuthenticationManager authenticationManager) {
            this.authenticationManager = authenticationManager;
            return this;
        }

        private ReactiveAuthenticationManager getAuthenticationManager() {
            if (this.authenticationManager == null) {
                this.authenticationManager = new OAuth2AuthorizationCodeReactiveAuthenticationManager(new WebClientReactiveAuthorizationCodeTokenResponseClient());
            }

            return this.authenticationManager;
        }

        public ServerHttpSecurity.OAuth2ClientSpec clientRegistrationRepository(ReactiveClientRegistrationRepository clientRegistrationRepository) {
            this.clientRegistrationRepository = clientRegistrationRepository;
            return this;
        }

        public ServerHttpSecurity.OAuth2ClientSpec authorizedClientRepository(ServerOAuth2AuthorizedClientRepository authorizedClientRepository) {
            this.authorizedClientRepository = authorizedClientRepository;
            return this;
        }

        public ServerHttpSecurity.OAuth2ClientSpec authorizationRequestRepository(ServerAuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository) {
            this.authorizationRequestRepository = authorizationRequestRepository;
            return this;
        }

        private ServerAuthorizationRequestRepository<OAuth2AuthorizationRequest> getAuthorizationRequestRepository() {
            if (this.authorizationRequestRepository == null) {
                this.authorizationRequestRepository = new WebSessionOAuth2ServerAuthorizationRequestRepository();
            }

            return this.authorizationRequestRepository;
        }

        public ServerHttpSecurity and() {
            return ServerHttpSecurity.this;
        }

        protected void configure(ServerHttpSecurity http) {
            ReactiveClientRegistrationRepository clientRegistrationRepository = this.getClientRegistrationRepository();
            ServerOAuth2AuthorizedClientRepository authorizedClientRepository = this.getAuthorizedClientRepository();
            ServerAuthenticationConverter authenticationConverter = this.getAuthenticationConverter();
            ReactiveAuthenticationManager authenticationManager = this.getAuthenticationManager();
            OAuth2AuthorizationCodeGrantWebFilter codeGrantWebFilter = new OAuth2AuthorizationCodeGrantWebFilter(authenticationManager, authenticationConverter, authorizedClientRepository);
            codeGrantWebFilter.setAuthorizationRequestRepository(this.getAuthorizationRequestRepository());
            OAuth2AuthorizationRequestRedirectWebFilter oauthRedirectFilter = new OAuth2AuthorizationRequestRedirectWebFilter(clientRegistrationRepository);
            oauthRedirectFilter.setAuthorizationRequestRepository(this.getAuthorizationRequestRepository());
            http.addFilterAt(codeGrantWebFilter, SecurityWebFiltersOrder.OAUTH2_AUTHORIZATION_CODE);
            http.addFilterAt(oauthRedirectFilter, SecurityWebFiltersOrder.HTTP_BASIC);
        }

        private ReactiveClientRegistrationRepository getClientRegistrationRepository() {
            return this.clientRegistrationRepository != null ? this.clientRegistrationRepository : (ReactiveClientRegistrationRepository)ServerHttpSecurity.this.getBeanOrNull(ReactiveClientRegistrationRepository.class);
        }

        private ServerOAuth2AuthorizedClientRepository getAuthorizedClientRepository() {
            if (this.authorizedClientRepository != null) {
                return this.authorizedClientRepository;
            } else {
                ServerOAuth2AuthorizedClientRepository result = (ServerOAuth2AuthorizedClientRepository)ServerHttpSecurity.this.getBeanOrNull(ServerOAuth2AuthorizedClientRepository.class);
                if (result == null) {
                    ReactiveOAuth2AuthorizedClientService authorizedClientService = this.getAuthorizedClientService();
                    if (authorizedClientService != null) {
                        result = new AuthenticatedPrincipalServerOAuth2AuthorizedClientRepository(authorizedClientService);
                    }
                }

                return (ServerOAuth2AuthorizedClientRepository)result;
            }
        }

        private ReactiveOAuth2AuthorizedClientService getAuthorizedClientService() {
            ReactiveOAuth2AuthorizedClientService service = (ReactiveOAuth2AuthorizedClientService)ServerHttpSecurity.this.getBeanOrNull(ReactiveOAuth2AuthorizedClientService.class);
            if (service == null) {
                service = new InMemoryReactiveOAuth2AuthorizedClientService(this.getClientRegistrationRepository());
            }

            return (ReactiveOAuth2AuthorizedClientService)service;
        }

        private OAuth2ClientSpec() {
        }
    }

    public class OAuth2LoginSpec {
        private ReactiveClientRegistrationRepository clientRegistrationRepository;
        private ServerOAuth2AuthorizedClientRepository authorizedClientRepository;
        private ServerAuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository;
        private ReactiveAuthenticationManager authenticationManager;
        private ServerSecurityContextRepository securityContextRepository;
        private ServerAuthenticationConverter authenticationConverter;
        private ServerOAuth2AuthorizationRequestResolver authorizationRequestResolver;
        private ServerWebExchangeMatcher authenticationMatcher;
        private ServerAuthenticationSuccessHandler authenticationSuccessHandler;
        private ServerAuthenticationFailureHandler authenticationFailureHandler;

        public ServerHttpSecurity.OAuth2LoginSpec authenticationManager(ReactiveAuthenticationManager authenticationManager) {
            this.authenticationManager = authenticationManager;
            return this;
        }

        public ServerHttpSecurity.OAuth2LoginSpec securityContextRepository(ServerSecurityContextRepository securityContextRepository) {
            this.securityContextRepository = securityContextRepository;
            return this;
        }

        public ServerHttpSecurity.OAuth2LoginSpec authenticationSuccessHandler(ServerAuthenticationSuccessHandler authenticationSuccessHandler) {
            Assert.notNull(authenticationSuccessHandler, "authenticationSuccessHandler cannot be null");
            this.authenticationSuccessHandler = authenticationSuccessHandler;
            return this;
        }

        public ServerHttpSecurity.OAuth2LoginSpec authenticationFailureHandler(ServerAuthenticationFailureHandler authenticationFailureHandler) {
            Assert.notNull(authenticationFailureHandler, "authenticationFailureHandler cannot be null");
            this.authenticationFailureHandler = authenticationFailureHandler;
            return this;
        }

        private ReactiveAuthenticationManager getAuthenticationManager() {
            if (this.authenticationManager == null) {
                this.authenticationManager = this.createDefault();
            }

            return this.authenticationManager;
        }

        private ReactiveAuthenticationManager createDefault() {
            ReactiveOAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> client = this.getAccessTokenResponseClient();
            ReactiveAuthenticationManager result = new OAuth2LoginReactiveAuthenticationManager(client, this.getOauth2UserService());
            boolean oidcAuthenticationProviderEnabled = ClassUtils.isPresent("org.springframework.security.oauth2.jwt.JwtDecoder", this.getClass().getClassLoader());
            if (oidcAuthenticationProviderEnabled) {
                OidcAuthorizationCodeReactiveAuthenticationManager oidc = new OidcAuthorizationCodeReactiveAuthenticationManager(client, this.getOidcUserService());
                ResolvableType type = ResolvableType.forClassWithGenerics(ReactiveJwtDecoderFactory.class, new Class[]{ClientRegistration.class});
                ReactiveJwtDecoderFactory<ClientRegistration> jwtDecoderFactory = (ReactiveJwtDecoderFactory)ServerHttpSecurity.this.getBeanOrNull(type);
                if (jwtDecoderFactory != null) {
                    oidc.setJwtDecoderFactory(jwtDecoderFactory);
                }

                result = new DelegatingReactiveAuthenticationManager(new ReactiveAuthenticationManager[]{oidc, (ReactiveAuthenticationManager)result});
            }

            return (ReactiveAuthenticationManager)result;
        }

        public ServerHttpSecurity.OAuth2LoginSpec authenticationConverter(ServerAuthenticationConverter authenticationConverter) {
            this.authenticationConverter = authenticationConverter;
            return this;
        }

        private ServerAuthenticationConverter getAuthenticationConverter(ReactiveClientRegistrationRepository clientRegistrationRepository) {
            if (this.authenticationConverter == null) {
                ServerOAuth2AuthorizationCodeAuthenticationTokenConverter authenticationConverter = new ServerOAuth2AuthorizationCodeAuthenticationTokenConverter(clientRegistrationRepository);
                authenticationConverter.setAuthorizationRequestRepository(this.getAuthorizationRequestRepository());
                this.authenticationConverter = authenticationConverter;
            }

            return this.authenticationConverter;
        }

        public ServerHttpSecurity.OAuth2LoginSpec clientRegistrationRepository(ReactiveClientRegistrationRepository clientRegistrationRepository) {
            this.clientRegistrationRepository = clientRegistrationRepository;
            return this;
        }

        public ServerHttpSecurity.OAuth2LoginSpec authorizedClientService(ReactiveOAuth2AuthorizedClientService authorizedClientService) {
            this.authorizedClientRepository = new AuthenticatedPrincipalServerOAuth2AuthorizedClientRepository(authorizedClientService);
            return this;
        }

        public ServerHttpSecurity.OAuth2LoginSpec authorizedClientRepository(ServerOAuth2AuthorizedClientRepository authorizedClientRepository) {
            this.authorizedClientRepository = authorizedClientRepository;
            return this;
        }

        public ServerHttpSecurity.OAuth2LoginSpec authorizationRequestRepository(ServerAuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository) {
            this.authorizationRequestRepository = authorizationRequestRepository;
            return this;
        }

        public ServerHttpSecurity.OAuth2LoginSpec authorizationRequestResolver(ServerOAuth2AuthorizationRequestResolver authorizationRequestResolver) {
            this.authorizationRequestResolver = authorizationRequestResolver;
            return this;
        }

        public ServerHttpSecurity.OAuth2LoginSpec authenticationMatcher(ServerWebExchangeMatcher authenticationMatcher) {
            this.authenticationMatcher = authenticationMatcher;
            return this;
        }

        private ServerWebExchangeMatcher getAuthenticationMatcher() {
            if (this.authenticationMatcher == null) {
                this.authenticationMatcher = this.createAttemptAuthenticationRequestMatcher();
            }

            return this.authenticationMatcher;
        }

        public ServerHttpSecurity and() {
            return ServerHttpSecurity.this;
        }

        protected void configure(ServerHttpSecurity http) {
            ReactiveClientRegistrationRepository clientRegistrationRepository = this.getClientRegistrationRepository();
            ServerOAuth2AuthorizedClientRepository authorizedClientRepository = this.getAuthorizedClientRepository();
            OAuth2AuthorizationRequestRedirectWebFilter oauthRedirectFilter = this.getRedirectWebFilter();
            ServerAuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository = this.getAuthorizationRequestRepository();
            oauthRedirectFilter.setAuthorizationRequestRepository(authorizationRequestRepository);
            oauthRedirectFilter.setRequestCache(http.requestCache.requestCache);
            ReactiveAuthenticationManager manager = this.getAuthenticationManager();
            AuthenticationWebFilter authenticationFilter = new OAuth2LoginAuthenticationWebFilter(manager, authorizedClientRepository);
            authenticationFilter.setRequiresAuthenticationMatcher(this.getAuthenticationMatcher());
            authenticationFilter.setServerAuthenticationConverter(this.getAuthenticationConverter(clientRegistrationRepository));
            authenticationFilter.setAuthenticationSuccessHandler(this.getAuthenticationSuccessHandler(http));
            authenticationFilter.setAuthenticationFailureHandler(this.getAuthenticationFailureHandler());
            authenticationFilter.setSecurityContextRepository(this.securityContextRepository);
            this.setDefaultEntryPoints(http);
            http.addFilterAt(oauthRedirectFilter, SecurityWebFiltersOrder.HTTP_BASIC);
            http.addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION);
        }

        private void setDefaultEntryPoints(ServerHttpSecurity http) {
            String defaultLoginPage = "/login";
            Map<String, String> urlToText = http.oauth2Login.getLinks();
            String providerLoginPage = null;
            if (urlToText.size() == 1) {
                providerLoginPage = (String)urlToText.keySet().iterator().next();
            }

            MediaTypeServerWebExchangeMatcher htmlMatcher = new MediaTypeServerWebExchangeMatcher(new MediaType[]{MediaType.APPLICATION_XHTML_XML, new MediaType("image", "*"), MediaType.TEXT_HTML, MediaType.TEXT_PLAIN});
            htmlMatcher.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));
            ServerWebExchangeMatcher xhrMatcher = (exchange) -> {
                return exchange.getRequest().getHeaders().getOrEmpty("X-Requested-With").contains("XMLHttpRequest") ? MatchResult.match() : MatchResult.notMatch();
            };
            ServerWebExchangeMatcher notXhrMatcher = new NegatedServerWebExchangeMatcher(xhrMatcher);
            ServerWebExchangeMatcher defaultEntryPointMatcher = new AndServerWebExchangeMatcher(new ServerWebExchangeMatcher[]{notXhrMatcher, htmlMatcher});
            if (providerLoginPage != null) {
                ServerWebExchangeMatcher loginPageMatcher = new PathPatternParserServerWebExchangeMatcher(defaultLoginPage);
                ServerWebExchangeMatcher faviconMatcher = new PathPatternParserServerWebExchangeMatcher("/favicon.ico");
                ServerWebExchangeMatcher defaultLoginPageMatcher = new AndServerWebExchangeMatcher(new ServerWebExchangeMatcher[]{new OrServerWebExchangeMatcher(new ServerWebExchangeMatcher[]{loginPageMatcher, faviconMatcher}), defaultEntryPointMatcher});
                ServerWebExchangeMatcher matcher = new AndServerWebExchangeMatcher(new ServerWebExchangeMatcher[]{notXhrMatcher, new NegatedServerWebExchangeMatcher(defaultLoginPageMatcher)});
                RedirectServerAuthenticationEntryPoint entryPoint = new RedirectServerAuthenticationEntryPoint(providerLoginPage);
                entryPoint.setRequestCache(http.requestCache.requestCache);
                http.defaultEntryPoints.add(new DelegateEntry(matcher, entryPoint));
            }

            RedirectServerAuthenticationEntryPoint defaultEntryPoint = new RedirectServerAuthenticationEntryPoint(defaultLoginPage);
            defaultEntryPoint.setRequestCache(http.requestCache.requestCache);
            http.defaultEntryPoints.add(new DelegateEntry(defaultEntryPointMatcher, defaultEntryPoint));
        }

        private ServerAuthenticationSuccessHandler getAuthenticationSuccessHandler(ServerHttpSecurity http) {
            if (this.authenticationSuccessHandler == null) {
                RedirectServerAuthenticationSuccessHandler handler = new RedirectServerAuthenticationSuccessHandler();
                handler.setRequestCache(http.requestCache.requestCache);
                this.authenticationSuccessHandler = handler;
            }

            return this.authenticationSuccessHandler;
        }

        private ServerAuthenticationFailureHandler getAuthenticationFailureHandler() {
            if (this.authenticationFailureHandler == null) {
                this.authenticationFailureHandler = new RedirectServerAuthenticationFailureHandler("/login?error");
            }

            return this.authenticationFailureHandler;
        }

        private ServerWebExchangeMatcher createAttemptAuthenticationRequestMatcher() {
            return new PathPatternParserServerWebExchangeMatcher("/login/oauth2/code/{registrationId}");
        }

        private ReactiveOAuth2UserService<OidcUserRequest, OidcUser> getOidcUserService() {
            ResolvableType type = ResolvableType.forClassWithGenerics(ReactiveOAuth2UserService.class, new Class[]{OidcUserRequest.class, OidcUser.class});
            ReactiveOAuth2UserService<OidcUserRequest, OidcUser> bean = (ReactiveOAuth2UserService)ServerHttpSecurity.this.getBeanOrNull(type);
            return (ReactiveOAuth2UserService)(bean == null ? new OidcReactiveOAuth2UserService() : bean);
        }

        private ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> getOauth2UserService() {
            ResolvableType type = ResolvableType.forClassWithGenerics(ReactiveOAuth2UserService.class, new Class[]{OAuth2UserRequest.class, OAuth2User.class});
            ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> bean = (ReactiveOAuth2UserService)ServerHttpSecurity.this.getBeanOrNull(type);
            return (ReactiveOAuth2UserService)(bean == null ? new DefaultReactiveOAuth2UserService() : bean);
        }

        private Map<String, String> getLinks() {
            Iterable<ClientRegistration> registrations = (Iterable)ServerHttpSecurity.this.getBeanOrNull(ResolvableType.forClassWithGenerics(Iterable.class, new Class[]{ClientRegistration.class}));
            if (registrations == null) {
                return Collections.emptyMap();
            } else {
                Map<String, String> result = new HashMap();
                registrations.iterator().forEachRemaining((r) -> {
                    String var10000 = (String)result.put("/oauth2/authorization/" + r.getRegistrationId(), r.getClientName());
                });
                return result;
            }
        }

        private ReactiveOAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> getAccessTokenResponseClient() {
            ResolvableType type = ResolvableType.forClassWithGenerics(ReactiveOAuth2AccessTokenResponseClient.class, new Class[]{OAuth2AuthorizationCodeGrantRequest.class});
            ReactiveOAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> bean = (ReactiveOAuth2AccessTokenResponseClient)ServerHttpSecurity.this.getBeanOrNull(type);
            return (ReactiveOAuth2AccessTokenResponseClient)(bean == null ? new WebClientReactiveAuthorizationCodeTokenResponseClient() : bean);
        }

        private ReactiveClientRegistrationRepository getClientRegistrationRepository() {
            if (this.clientRegistrationRepository == null) {
                this.clientRegistrationRepository = (ReactiveClientRegistrationRepository)ServerHttpSecurity.this.getBeanOrNull(ReactiveClientRegistrationRepository.class);
            }

            return this.clientRegistrationRepository;
        }

        private OAuth2AuthorizationRequestRedirectWebFilter getRedirectWebFilter() {
            OAuth2AuthorizationRequestRedirectWebFilter oauthRedirectFilter;
            if (this.authorizationRequestResolver == null) {
                oauthRedirectFilter = new OAuth2AuthorizationRequestRedirectWebFilter(this.getClientRegistrationRepository());
            } else {
                oauthRedirectFilter = new OAuth2AuthorizationRequestRedirectWebFilter(this.authorizationRequestResolver);
            }

            return oauthRedirectFilter;
        }

        private ServerOAuth2AuthorizedClientRepository getAuthorizedClientRepository() {
            ServerOAuth2AuthorizedClientRepository result = this.authorizedClientRepository;
            if (result == null) {
                result = (ServerOAuth2AuthorizedClientRepository)ServerHttpSecurity.this.getBeanOrNull(ServerOAuth2AuthorizedClientRepository.class);
            }

            if (result == null) {
                ReactiveOAuth2AuthorizedClientService authorizedClientService = this.getAuthorizedClientService();
                if (authorizedClientService != null) {
                    result = new AuthenticatedPrincipalServerOAuth2AuthorizedClientRepository(authorizedClientService);
                }
            }

            return (ServerOAuth2AuthorizedClientRepository)result;
        }

        private ServerAuthorizationRequestRepository<OAuth2AuthorizationRequest> getAuthorizationRequestRepository() {
            if (this.authorizationRequestRepository == null) {
                this.authorizationRequestRepository = new WebSessionOAuth2ServerAuthorizationRequestRepository();
            }

            return this.authorizationRequestRepository;
        }

        private ReactiveOAuth2AuthorizedClientService getAuthorizedClientService() {
            ReactiveOAuth2AuthorizedClientService service = (ReactiveOAuth2AuthorizedClientService)ServerHttpSecurity.this.getBeanOrNull(ReactiveOAuth2AuthorizedClientService.class);
            if (service == null) {
                service = new InMemoryReactiveOAuth2AuthorizedClientService(this.getClientRegistrationRepository());
            }

            return (ReactiveOAuth2AuthorizedClientService)service;
        }

        private OAuth2LoginSpec() {
        }
    }

    public class X509Spec {
        private X509PrincipalExtractor principalExtractor;
        private ReactiveAuthenticationManager authenticationManager;

        public ServerHttpSecurity.X509Spec principalExtractor(X509PrincipalExtractor principalExtractor) {
            this.principalExtractor = principalExtractor;
            return this;
        }

        public ServerHttpSecurity.X509Spec authenticationManager(ReactiveAuthenticationManager authenticationManager) {
            this.authenticationManager = authenticationManager;
            return this;
        }

        public ServerHttpSecurity and() {
            return ServerHttpSecurity.this;
        }

        protected void configure(ServerHttpSecurity http) {
            ReactiveAuthenticationManager authenticationManager = this.getAuthenticationManager();
            X509PrincipalExtractor principalExtractor = this.getPrincipalExtractor();
            AuthenticationWebFilter filter = new AuthenticationWebFilter(authenticationManager);
            filter.setServerAuthenticationConverter(new ServerX509AuthenticationConverter(principalExtractor));
            http.addFilterAt(filter, SecurityWebFiltersOrder.AUTHENTICATION);
        }

        private X509PrincipalExtractor getPrincipalExtractor() {
            return (X509PrincipalExtractor)(this.principalExtractor != null ? this.principalExtractor : new SubjectDnX509PrincipalExtractor());
        }

        private ReactiveAuthenticationManager getAuthenticationManager() {
            if (this.authenticationManager != null) {
                return this.authenticationManager;
            } else {
                ReactiveUserDetailsService userDetailsService = (ReactiveUserDetailsService)ServerHttpSecurity.this.getBean(ReactiveUserDetailsService.class);
                ReactivePreAuthenticatedAuthenticationManager authenticationManager = new ReactivePreAuthenticatedAuthenticationManager(userDetailsService);
                return authenticationManager;
            }
        }

        private X509Spec() {
        }
    }

    public class CorsSpec {
        private CorsWebFilter corsFilter;

        public ServerHttpSecurity.CorsSpec configurationSource(CorsConfigurationSource source) {
            this.corsFilter = new CorsWebFilter(source);
            return this;
        }

        public ServerHttpSecurity disable() {
            ServerHttpSecurity.this.cors = null;
            return ServerHttpSecurity.this;
        }

        public ServerHttpSecurity and() {
            return ServerHttpSecurity.this;
        }

        protected void configure(ServerHttpSecurity http) {
            CorsWebFilter corsFilter = this.getCorsFilter();
            if (corsFilter != null) {
                http.addFilterAt(this.corsFilter, SecurityWebFiltersOrder.CORS);
            }

        }

        private CorsWebFilter getCorsFilter() {
            if (this.corsFilter != null) {
                return this.corsFilter;
            } else {
                CorsConfigurationSource source = (CorsConfigurationSource)ServerHttpSecurity.this.getBeanOrNull(CorsConfigurationSource.class);
                if (source == null) {
                    return null;
                } else {
                    CorsProcessor processor = (CorsProcessor)ServerHttpSecurity.this.getBeanOrNull(CorsProcessor.class);
                    if (processor == null) {
                        processor = new DefaultCorsProcessor();
                    }

                    this.corsFilter = new CorsWebFilter(source, (CorsProcessor)processor);
                    return this.corsFilter;
                }
            }
        }

        private CorsSpec() {
        }
    }
}
