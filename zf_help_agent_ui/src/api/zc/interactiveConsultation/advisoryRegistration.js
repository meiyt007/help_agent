import request from '@/utils/request';

//查询咨询记录列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/advisoryRegistration/queryAdvisoryRegistrationWithPage',
    method: 'post',
    params: query
  })
}

// 获取一条咨询详情
export function getOne(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/advisoryRegistration/getAdvisoryRegistrationById',
    params: {
      id: id
    },
    method: 'post',
  })
}
// 咨询新增或者修改
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/advisoryRegistration/saveAdvisoryRegistration',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}


// 删除咨询信息
export function del(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/advisoryRegistration/deleteAdvisoryRegistrationById',
    params: {
      id: id
    },
    method: 'post'
  })
}


