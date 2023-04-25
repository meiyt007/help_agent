import request from '@/utils/request';

//查询列表
export function page(query) {
  console.log(query);
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/work/perPhone/queryPerPhoneWithPage',
    method: 'post',
    params: query
  })
}

// 新增或者修改
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/work/perPhone/saveOrUpdatePerPhone',
    method: 'post',
    data: data
  })
}

// 根据id banner信息
export function init(id) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/work/perPhone/getPerPhoneById?id=' + id,
    method: 'get'
  })
}

// 根据id删除banner信息
export function deleteById(id) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/work/perPhone/deletePerPhoneById?id=' + id,
    method: 'get'
  })
}

// 导入电话绩效excel
export function ImportPhoneExcel(data) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/work/perPhone/ImportPhoneExcel' ,
    method: "POST",
    headers: {
      "Content-Type": "multipart/form-data"
    },
    data: data
  })
}
