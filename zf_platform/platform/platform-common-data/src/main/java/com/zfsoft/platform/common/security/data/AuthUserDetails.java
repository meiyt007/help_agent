package com.zfsoft.platform.common.security.data;

/**
 * @Auther: lijun@sstcsoft.com
 * @Date: 2019/9/20 15:18
 */
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

public class AuthUserDetails implements UserDetails {

    private static final long serialVersionUID =-6521686858115103786L;

    private String username;
    private String account;
    @JsonIgnore
    private String password;
    private Collection<String> roles;
    private String token;
    /**
     * 登录oid
     **/
    private String oid;
    private String userOid;
    /**
     * 区划oid
     **/
    private String districtOid;
    /**
     * 区划oid
     **/
    private String districtName;
    /**
     * 组织机构oid
     **/
    private String organOid;
    /**
     * 组织机构名称
     **/
    private String organName;

    /**
     * 启禁用
     */
    private Integer isAble;

    /**
     * 锁定状态
     */
    private Integer lockStatus;

    /**
     * 用户类型对应数据字典code
     **/
    private String userCode;

    /**
     * 后缓咨询状态
     */
    private Integer adviStatus;
    /**
     * 登录时间
     */
    private Date loginDate = new Date();

    /**
     * 长时间未修改密码标志
     */
    private int psFlag;

    /**
     *  注册授权文件预警剩余天数
     */
    private String warnDay;

    /**
     * 数据权限（1全部 2本人 3本部门 4本区划）
     */
    private Integer dataAuthority;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(Collection<String> roles) {
        this.roles = roles;
    }

    public Collection<String> getRoles() {
        return roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return BaseStaticParameter.N == this.lockStatus;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return BaseStaticParameter.Y == this.isAble;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }



    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }


    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getOrganOid() {
        return organOid;
    }

    public void setOrganOid(String organOid) {
        this.organOid = organOid;
    }

    public String getDistrictOid() {
        return districtOid;
    }

    public void setDistrictOid(String districtOid) {
        this.districtOid = districtOid;
    }

    public String getUserOid() {
        return userOid;
    }

    public void setUserOid(String userOid) {
        this.userOid = userOid;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public void setLockStatus(Integer lockStatus) {
        this.lockStatus = lockStatus;
    }

    public Integer getAdviStatus() {
        return adviStatus;
    }

    public void setAdviStatus(Integer adviStatus) {
        this.adviStatus = adviStatus;
    }

    public void setIsAble(Integer isAble) {
        this.isAble = isAble;
    }

    public int getPsFlag() {
        return psFlag;
    }

    public void setPsFlag(int psFlag) {
        this.psFlag = psFlag;
    }

    public String getWarnDay() {
        return warnDay;
    }

    public void setWarnDay(String warnDay) {
        this.warnDay = warnDay;
    }

    public Integer getDataAuthority() {
        return dataAuthority;
    }

    public void setDataAuthority(Integer dataAuthority) {
        this.dataAuthority = dataAuthority;
    }
}
