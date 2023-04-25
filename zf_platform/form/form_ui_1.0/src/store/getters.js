const getters = {
  sidebar: state => state.app.sidebar,
  size: state => state.app.size,
  device: state => state.app.device,
  visitedViews: state => state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  introduction: state => state.user.introduction,
  roles: state => state.user.roles,
  permissions: state => state.user.permissions,
  permission_routes: state => state.permission.routes,
  app_oid:state => state.permission.appOid,
  appList:state => state.permission.appList,
  login: state => state.user.login,
  skinClassname: state => state.user.skinClassname,
  userOid: state => state.user.userOid
}
export default getters
