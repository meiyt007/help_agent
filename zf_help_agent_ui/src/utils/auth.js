/*
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-08-11 10:50:14
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-09-23 17:13:56
 * @FilePath: \zf_help_agent_ui\src\utils\auth.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import Cookies from 'js-cookie'

const TokenKey = 'CUSTOM-REQUEST-HEADER'

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

export function getLoginUrl() {
  let loginType = process.env.VUE_APP_INDEX_LOGIN_TYPE?process.env.VUE_APP_INDEX_LOGIN_TYPE:'form',
    loginTypeMap = {
      form: '/login',
      oauth: '/oauthLogin'
    },
    loginPageUrl = loginTypeMap[loginType] ? loginTypeMap[loginType] : '';
  if(!loginPageUrl) {
    Notification.error({
      title: "登陆方式未配置"
    })
    return;
  }
  return loginPageUrl;
}
