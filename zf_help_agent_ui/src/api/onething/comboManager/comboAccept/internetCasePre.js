import request from '@/utils/request';

// 分页查询列表操作
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/combo/case/queryWithPageInternet',
    method: 'get',
    params: query
  })
}
//保存或者修改
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/internetCasePre/saveOrUpdateCombo',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

//保存或者修改办件信息
export function saveCaseAccpet(data) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/combo/case/saveCaseAccpet',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

