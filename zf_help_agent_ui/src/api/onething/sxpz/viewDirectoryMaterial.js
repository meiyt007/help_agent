import request from '@/utils/request'

// 查询一件事目录事项列表
export function querySxServiceList(comboDireOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directoryMaterial/querySxServiceListByMaterOid/'+comboDireOid,
    method: 'get'
  })
}






