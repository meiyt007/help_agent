import request from '@/utils/request';
// 添加选项标题
export function initOptionTitle(titleOid) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxServiceOptionTitle/initSxServiceOptionTitle/'+titleOid,
    method: 'get'
  })
}

export function saveServiceOption (data) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxServiceOptionTitle/saveSxServiceOptionTitle',
    method: 'POST',
    data:data
  })
}

//查看
export function getSxServiceOptionTitleByOid(query) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxServiceOptionTitle/getSxServiceOptionTitleByOid/'+query,
    method: 'get'
  })
}

//删除
export function delSxServiceOptionTitleByOid(id) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxServiceOptionTitle/delSxServiceOptionTitleByOid/'+id,
    method: 'get'
  })
}
