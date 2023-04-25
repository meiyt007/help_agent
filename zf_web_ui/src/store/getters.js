/*
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-07-18 10:51:54
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-08-11 09:53:33
 * @FilePath: \hpNewHall\src\store\getters.js
 * @Description:
 */
const getters = {
  token: (state) => state.user.token,
  avatar: (state) => state.user.avatar,
  name: (state) => state.user.name,
  introduction: (state) => state.user.introduction,
  basicUserInfo: (state) => state.user.basicUserInfo,
  baseUserInfo:(state) => state.pageData.baseUserInfo,
  permissions: (state) => state.user.permissions,
  login: (state) => state.user.login,
  userOid: (state) => state.user.userOid,
  setAcceptService: (state) => state.pageData.setAcceptService,
}
export default getters
