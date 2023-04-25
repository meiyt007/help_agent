import request from '@/utils/request'

$(document).ready(function(){
  var urlParam = decodeURI(window.location.href.split("?")[2]);
});

// 登录方法
export function login(username, password, captcha, uuid) {
  const data = {
    username,
    password,
    captcha,
    uuid
  }
  //const data="username="+username+"&password="+password+"&captcha="+captcha
  return request({
    url: '/auth/login',
    method: 'post',
    data: data,
    headers: {"Content-Type":"application/x-www-form-urlencoded"},
    type: "form"

  })
}

// 登录方法
export function loginByOauth2(username, password, captcha, uuid) {
  const data = {
    username,
    password,
    captcha,
    uuid
  }
  //const data="username="+username+"&password="+password+"&captcha="+captcha
  return request({
    url: '/platform-auth-server/oauth/token',
    method: 'post',
    data: data,
    headers: {"Content-Type":"application/x-www-form-urlencoded"},
    type: "form"

  })
}

// 获取用户详细信息
export function getInfo() {
  return request({
    url: '/platform/security/syslogin/getUserInfo',
    method: 'get'
  })
}

// 退出方法
export function logout() {
  return request({
    url: '/auth/logout',
    method: 'post'
  })
}

// 获取验证码
export function getCodeImg() {
  return request({
    url: '/captcha-image',
    method: 'get'
  })
}

//获取登录加密公钥
export function getPublicKey() {
  return request({
    url: '/rsa-key',
    method: 'get'
  })
}
//获取加密公钥
export function getKey() {
  return request({
    url: '/platform/security/syslogin/getPublicKey',
    method: 'get'
  })
}

//获取验证码是否标识
export function getLoginCodeFlag(){
  return request({
    url: '/getLoginCodeFlag',
    method: 'get'
  })
}


//找回密码的部分
//获取登录加密公钥
export function getFindPasswordPublicKey() {
  return request({
    url: '/platform/security/findPassword/getFindPasswordPublicKey',
    method: 'get'
  })
}


//验证找回密码的登录名和验证码
export function checkAccountAndCode(data) {
  return request({
    url: '/checkAccountAndCode',
    method: 'post',
    params: data
  })
}

// 验证找回密码的身份证和手机号
export function checkCardNoAndMobile(data) {
  return request({
    url: '/platform/security/findPassword/checkCardNoAndMobile',
    method: 'post',
    params: data
  })
}

// 验证找回密码的更新
export function updatePassword(data) {
  return request({
    url: '/platform/security/findPassword/updatePassword',
    method: 'post',
    data: data
  })
}
//根据当前登录主键获取所有的权限集合
export function getPermissionLinkByLoginOid(path) {
  if(undefined == path){
    path='';
  }
  return request({
    url: '/platform/security/getPermissionLinkByLoginOid?path='+path,
    method: 'get'
  })
}
