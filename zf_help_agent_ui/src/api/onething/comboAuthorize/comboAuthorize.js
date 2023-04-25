import request from "@/utils/request";


export function getOneForAuthorize(id) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/combo/comboAuthorize/getOneForAuthorize?oid=' + id,
    method: 'get',
  })
}

// 保存或者修改
export function saveOrUpdateForRegister(data) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/combo/comboAuthorize/saveOrUpdateForRegister',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

// 保存或者修改
export function saveOrUpdateForSite(data) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/combo/comboAuthorize/saveOrUpdateForSite',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

// 人员授权保存或者修改
export function saveOrUpdatePersonReg(data) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/combo/comboAuthorize/saveOrUpdatePersonReg',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

//辖区树
export function querySiteAuthorizeTree() {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/serviceSiteAuthorize/querySiteAuthorizeTree',
    method: 'post',
  })
}

// 获取详情
export function getSiteOidsByComboDirectoryOid(id) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/combo/comboAuthorize/getSiteOidsByComboDirectoryOid?id=' + id,
    method: 'get',
  })
}

//区划机构一件事树
export function queryComboTree() {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/combo/comboAuthorize/queryComboTree',
    method: 'post',
  })
}

// 查询一件事主题信息详情
export function getComboOne(comboDirectoryOid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/combo/comboAuthorize/getComboOne/'+comboDirectoryOid,
    method: 'get',
  })
}

// 查询当前人员下所有授权的一件事
export function getOneByUserOid(id) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/combo/comboAuthorize/getOneByUserOid?oid=' + id,
    method: 'get',
  })
}


//一件事取消授权
export function delComboDireReg(oid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH +'/combo/comboAuthorize/delComboDireReg/' + oid,
    method: 'delete'
  })
}

//人员取消授权
export function delUserComboDire(oid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH +'/combo/comboAuthorize/delUserComboDire/' + oid,
    method: 'delete'
  })
}

//辖区取消授权
export function delComboDireSite(oid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH +'/combo/comboAuthorize/delComboDireSite/' + oid,
    method: 'delete'
  })
}
