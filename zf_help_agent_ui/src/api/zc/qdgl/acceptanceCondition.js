import request from '@/utils/request';

//查询列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxAcceptCondition/getAcceptConditionList',
    method: 'post',
    params: query
  })
}

//增加/修改
export function save(data) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxAcceptCondition/saveOrUpdateSxAcceptCondition',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}


//查询详细
export function detail(id) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxAcceptCondition/detail?id='+id,
    method: 'get',
    params: id
  })
}

//删除
export function del(id) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxAcceptCondition/delete?id='+id,
    method: 'get',
    params: id
  })
}
