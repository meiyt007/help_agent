package com.zfsoft.microservice.workflow.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * @Description: 工作流用户权限
 * @Author wuxx
 * @Date 2021/1/14
 **/
public class WorkflowAuthUserDetails implements UserDetails {

    private String username;
    private String password;
    private String account;
    private String userOid;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //增加activiti7 的用户授权role
        //return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ACTIVITI_USER");
        String roleStr = "ROLE_ACTIVITI_USER,activitiTeam";
        return Arrays.stream(roleStr.split(",")).map(e->new SimpleGrantedAuthority(e)).collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userOid;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUserOid() {
        return userOid;
    }

    public void setUserOid(String userOid) {
        this.userOid = userOid;
    }
}
