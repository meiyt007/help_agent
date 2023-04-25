import request from '@/utils/request'

/*// 查询一件事目录事项列表
export function querySxServiceList(comboDireOid) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH +'/combo/service/querySxServiceListForResult/'+comboDireOid,
    method: 'get'
  })
}*/

// 一件事目录公共材料的新增或者修改
export function saveOrupdateResult(data) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directoryResult/saveOrupdateResult',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

// 根据主健获取统一结果信息
export function getDireResultsByOid(oid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directoryResult/init?oid='+oid,
    method: 'get'
  })
}



