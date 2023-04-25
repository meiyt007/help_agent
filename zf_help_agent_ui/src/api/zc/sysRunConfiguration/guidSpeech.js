
import request from '@/utils/request';

//查询列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/guidSpeech/page',
    method: 'get',
    params: query
  })
}

// 新增或者修改
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/guidSpeech/saveOrUpdate',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

// 获取一条详情
export function getOne(oid) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/guidSpeech/getOneGuidSpeech?oid=' + oid,
    method: 'get',
  })
}

// 删除操作
export function deletes(oid) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/guidSpeech/delete?oid=' + oid,
    method: 'get',
  })
}
