import request from '@/utils/request';

//查询平板人员列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/pbpj/user/page',
    method: 'get',
    params: query
  })
}



// 获取一条平板人员详情
export function getOne(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/pbpj/user/getOne/'+id+'?id=' + id,
    method: 'get',
  })
}


// 确认平板人员信息
export function del(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/pbpj/user/confirm/'+id,
    params: {
      id: id
    },
    method: 'post'
  })

}
// 启禁用平板人员信息
export function able(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/pbpj/user/appraise/'+id,
    params: {
      id: id
    },
    method: 'post'
  })
}
