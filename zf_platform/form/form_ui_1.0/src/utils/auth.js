import Cookies from 'js-cookie'

const TokenKey = 'CUSTOM-REQUEST-HEADER'

export function getToken() {
  let token = Cookies.get(TokenKey);
  // return (token === 'undefined') ? '' : token;
  return token;
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

export function getLoginUrl() {
  let loginType = process.env.VUE_APP_INDEX_LOGIN_TYPE,
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
