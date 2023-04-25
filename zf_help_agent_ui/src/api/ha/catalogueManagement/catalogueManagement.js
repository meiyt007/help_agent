import request from '@/utils/request';
//查询列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/catalogue/config/queryCataloguePage',
    method: 'post',
    params: query
  })
}
// 新增或者修改
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/catalogue/config/saveAndUpload',
    method: 'post',
    data: data
  })
}

// 根据idcatalogue信息
export function init(id) {
  return request({
    url:  process.env.VUE_APP_BJFW_ROUTE_PATH + '/catalogue/config/getById?id='+id,
    method: 'get'
  })
}
// 根据idcatalogue信息
export function getAndLoadWandaCatalogue() {
  return request({
    url:  process.env.VUE_APP_BJFW_ROUTE_PATH + '/catalogue/config/getAndLoadWandaCatalogue',
    method: 'post'
  })
}

// 根据id删除banner信息
export function deleteById(id) {
  return request({
    url:  process.env.VUE_APP_BJFW_ROUTE_PATH + '/catalogue/config/deleteById?id='+id,
    method: 'get'
  })
}

