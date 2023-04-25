/*
* @Author: liyanqing
* @Description: 失信记录
*/
import request from '@/utils/request';

//查询列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/dishonestRecord/pageList',
    method: 'post',
    params: query
  })
}

// 新增或者修改
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/dishonestRecord/saveOrUpdate',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

//删除操作
export function deletes(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/dishonestRecord/delete/' + id,
    method: 'post',
  });
}

//获取单条信息
export function getOne(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/dishonestRecord/getOne/' + id,
    method: 'get',
  });
}

