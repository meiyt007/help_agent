import request from '@/utils/request';

// 分页查询列表操作
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/caseLicenseManageCombo/pageList',
    method: 'post',
    params: query
  })
}

//保存或者修改
export function saveOrUpdateSign(data) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/caseLicenseManageCombo/saveOrUpdateSign',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

//查询一件事目录证照配置信息
export function queryComboDirectoryResult(comboCaseOid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/caseLicenseManageCombo/queryComboDirectoryResult?comboCaseOid='+comboCaseOid,
    method: 'get'
  })
}

//通知发送短信
export function noticeSendMessage(comboCaseOid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/caseLicenseManageCombo/noticeSendMessage?comboCaseOid='+comboCaseOid,
    method: 'get'
  })
}

//查询证照签收记录
export function queryComboSignByMaps(data) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/caseLicenseManageCombo/queryComboSignByMaps',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

export function queryComboCaseSignByCaseOid(comboCaseOid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/caseLicenseManageCombo/queryComboCaseSignByCaseOid?comboCaseOid='+comboCaseOid,
    method: 'get'
  })
}

export function queryComboCaseSignByOid(oid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/caseLicenseManageCombo/queryComboCaseSignByOid?oid='+oid,
    method: 'get'
  })
}



