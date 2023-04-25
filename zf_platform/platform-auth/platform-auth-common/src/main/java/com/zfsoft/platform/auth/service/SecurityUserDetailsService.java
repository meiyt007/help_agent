package com.zfsoft.platform.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


/**
 * 用户登录验证逻辑相关操作
 * @author kkfan
 * @date 2020-02-07
 */
public interface SecurityUserDetailsService extends UserDetailsService {

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

}
