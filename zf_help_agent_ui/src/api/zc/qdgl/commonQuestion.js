import request from '@/utils/request';

//查询列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxServiceQuestion/getQuestionList',
    method: 'post',
    params: query
  })
}

// 启禁用常见问题
export function able(questionOid) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxServiceQuestion/able?questionOid='+questionOid,
    method: 'get',
    params: questionOid
  })
}

//增加/修改
export function save(data) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxServiceQuestion/saveOrUpdateSxServiceQuestion',
    method: 'post',
    data: data
  })
}

//查询详细
export function detail(id) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxServiceQuestion/detail?id='+id,
    method: 'get',
    params: id
  })
}

//删除
export function del(id) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxServiceQuestion/delete?id='+id,
    method: 'get',
    params: id
  })
}
