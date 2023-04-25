import request from "@/utils/request";

// 查询一件事目录信息详情
export function getComboOne(comboDirectoryOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/directory/getOne/'+comboDirectoryOid,
    method: 'get',
  })
}

// 查询一件事登记授权信息详情
export function pageOfCombo(query) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/directory/page/',
    method: 'get',
    params: query
  })
}
