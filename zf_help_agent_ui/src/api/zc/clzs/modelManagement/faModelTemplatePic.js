import request from '@/utils/request';

//查询底图列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/faModelTemplatePic/queryFaModelTemplatePic',
    method: 'post',
    params: query
  })
}

// 底图新增或者修改
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/faModelTemplatePic/saveOrUpdate',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
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


