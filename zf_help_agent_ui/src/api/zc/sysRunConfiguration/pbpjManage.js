import request from '@/utils/request';

//查询平板设备列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/manage/pbpj/page',
    method: 'get',
    params: query
  })
}

// 平板设备新增或者修改
export function save(data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/manage/pbpj/save',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

// 获取一条平板设备详情
export function getOne(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/manage/pbpj/getOne/'+id+'?id=' + id,
    method: 'get',
  })
}

// 获取一条平板设备详情
export function del(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/manage/pbpj/delete/'+id+'?id=' + id,
    method: 'post',
  })
}
// 启禁用平板设备信息
export function able(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/manage/pbpj/able/'+id,
    params: {
      id: id
    },
    method: 'post'
  })
}
