/*
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-07-22 15:25:53
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-09-27 16:30:53
 * @FilePath: \zf_web_ui\src\utils\auth.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import Cookies from "js-cookie";

const TokenKey = "CUSTOM-REQUEST-HEADER-WEB";

export function getToken() {
  return Cookies.get(TokenKey);
}

export function setToken(token) {
  return Cookies.set(TokenKey, token);
}

export function removeToken() {
  return Cookies.remove(TokenKey);
}

export function getLoginUrl() {
  // let loginType = process.env.VUE_APP_INDEX_LOGIN_TYPE ? process.env.VUE_APP_INDEX_LOGIN_TYPE : 'form',
  let flag = navigator.userAgent.match(
      /(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i
    )
      ? "pad"
      : "pc",
    loginTypeMap = {
      pad: "padLogin",
      pc: "pcLogin",
    },
    loginPageUrl = loginTypeMap[flag];
  if (!loginPageUrl) {
    Notification.error({
      title: "登陆方式未配置",
    });
    return;
  }
  return loginPageUrl;
}
