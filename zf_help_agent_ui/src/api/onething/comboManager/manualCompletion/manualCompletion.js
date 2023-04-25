// author:ningzz
import request from '@/utils/request';

export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/manualCompletion/page',
    method: 'get',
    params: query
  })
}

export function get(id) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/manualCompletion/get/'+id,
    method: 'get'
  })
}

export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/manualCompletion/saveOrUpdate',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

//获取登录机构主键
export function checkedCase(caseOid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/manualCompletion/checkedCase/'+caseOid,
    method: 'get'
  })
}

//获取登录机构主键
export function checkedHavingResultElec(combodirOid,serviceOid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/manualCompletion/checkedIsHavingElecConfig?comboDirOid='+combodirOid+'&serviceOid='+serviceOid,
    method: 'get'
  })
}
