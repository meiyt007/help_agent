import request from "@/utils/request";
import { encode, filterSpecialCharacters } from "@/utils/index";
// const baseUrl = '/m1/1286952-0-default'
const baseUrl = process.env.VUE_APP_CASE_API;
// 登录方法
export function login(account, pcode, checkCode, haType) {
  pcode = encode(filterSpecialCharacters(pcode));
  account = filterSpecialCharacters(account);
  if (checkCode) {
    checkCode = encode(filterSpecialCharacters(checkCode));
  }
  const data = {
    account,
    pcode,
    checkCode,
    haType,
  };
  return request({
    url: baseUrl + "/ha/login/loginByAccount",
    method: "post",
    params: data,
  });
}

// export function login(username, password, captcha, uuid) {
//   const data = {
//     username,
//     password,
//     captcha,
//     uuid,
//   }
//   //const data="username="+username+"&password="+password+"&captcha="+captcha
//   return request({
//     url: baseUrl + '/auth/login',
//     method: 'post',
//     data: data,
//     headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
//     type: 'form',
//   })
// }

// 获取用户详细信息
export function getInfo() {
  return request({
    url: baseUrl + "/ha/getUserInfo",
    method: "get",
  });
}

// 获取帮办人员登录地点列表
export function queryLoginLocation() {
  return request({
    url: baseUrl + "/ha/outer/queryLoginLocation",
    method: "get",
  });
}

// 更新登录地名称
export function saveLoingLocationName(data) {
  return request({
    url: baseUrl + "/ha/outer/saveLoingLocationName",
    method: "get",
    params: data,
  });
}

// 退出方法
export function logout() {
  return request({
    url: baseUrl + "/ha/logout",
    method: "get",
  });
}

//登录是否需要验证码

export function checkCodeFlag() {
  return request({
    url: baseUrl + "/ha/login/checkCodeFlag",
    method: "get",
  });
}

// 获取验证码
export function getCodeImg() {
  return request({
    url: baseUrl + "/ha/login/getLoginCheckCode",
    method: "get",
  });
}

//获取用户登录状态
export function online() {
  return request({
    url: baseUrl + "/ha/login/online",
    method: "get",
  });
}

//获取登录加密公钥
export function getPublicKey() {
  return request({
    url: "/rsa-key",
    method: "get",
  });
}
//获取加密公钥
export function getKey() {
  return request({
    url: "/platform/security/syslogin/getPublicKey",
    method: "get",
  });
}

//获取验证码是否标识
export function getLoginCodeFlag() {
  return request({
    url: "/getLoginCodeFlag",
    method: "get",
  });
}

//找回密码的部分
//获取登录加密公钥
export function getFindPasswordPublicKey() {
  return request({
    url: "/platform/security/findPassword/getFindPasswordPublicKey",
    method: "get",
  });
}

//验证找回密码的登录名和验证码
export function checkAccountAndCode(data) {
  return request({
    url: "/checkAccountAndCode",
    method: "post",
    params: data,
  });
}

// 验证找回密码的身份证和手机号
export function checkCardNoAndMobile(data) {
  return request({
    url: "/platform/security/findPassword/checkCardNoAndMobile",
    method: "post",
    params: data,
  });
}

// 验证找回密码的更新
export function updatePassword(data) {
  return request({
    url: "/platform/security/findPassword/updatePassword",
    method: "post",
    data: data,
  });
}
//根据当前登录主键获取所有的权限集合
export function getPermissionLinkByLoginOid(path) {
  if (undefined == path) {
    path = "";
  }
  return request({
    url: "/platform/security/getPermissionLinkByLoginOid?path=" + path,
    method: "get",
  });
}
