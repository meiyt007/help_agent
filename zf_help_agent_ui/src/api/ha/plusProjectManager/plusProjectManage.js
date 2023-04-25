import request from '@/utils/request';
//查询列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/plusProjectManager/plusProjectServicePage',
    method: 'post',
    params: query
  })
}

// 新增或者修改
export function saveOrUpdatePlusProject(data) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/plusProjectManager/savePlusProject',
    method: 'post',
    data: data
  })
}

// 根据id banner信息
export function initPlusProject(id) {
  return request({
    url:  process.env.VUE_APP_BJFW_ROUTE_PATH + '/plusProjectManager/getPlusProjectById?id='+id,
    method: 'get'
  })
}

// 根据id删除banner信息
export function deletePlusProjectById(id) {
  return request({
    url:  process.env.VUE_APP_BJFW_ROUTE_PATH + '/plusProjectManager/deletePlusProjectById?id='+id,
    method: 'get'
  })
}
//批量删除
export function batchRemovePlusProject(idList){
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH +'/plusProjectManager/deletePlusProjectListid?ids='+idList,
    method: 'get',
  })
}
