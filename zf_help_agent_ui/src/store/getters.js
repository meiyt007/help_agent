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
  app_oid: state => state.permission.appOid,
  appList: state => state.permission.appList,
  login: state => state.user.login,
  skinClassname: state => state.user.skinClassname,
  userOid: state => state.user.userOid,
  open: state => state.open.open,
  switchOnOff: state => state.open.switchOnOff,
  code: state => state.open.code,
  currentPersonNum: state => state.navbar.currentPersonNum,
  startCall: state => state.navbar.startCall,
  caseOid: state => state.navbar.caseOid,
  currentVirtualBusinessRecordOid: state =>
    state.navbar.currentVirtualBusinessRecordOid,
  saveCallRecordOid: state => state.navbar.saveCallRecordOid,
  /** 系统配置信息 */
  deviceMap: state => state.config.deviceMap /** 硬件参数 */,
  znzxFlag: state => state.config.znzxFlag /** 智能咨询 */,
  wgpjFlag: state => state.config.wgpjFlag /** 无感评价 */,
  xxqrTime: state => state.config.xxqrTime /** 信息确认时间 */,
  pjdjTime: state => state.config.pjdjTime /** 评价倒计时时间 */,
  wgtpFlag: state => state.config.wgtpFlag /** 无感图片 */,
  clzsFlag: state => state.config.clzsFlag /** 材料智审 */,
  clflFlag: state => state.config.clflFlag /** 材料分类 */,
  saveComponentIndex: state => state.config.saveComponentIndex
};
export default getters;
