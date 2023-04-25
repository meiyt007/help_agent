/**
 * @Author: kkfan
 * @Date: 2020-10-29 15:50:10
 * @Last Modified by: kkfan
 * @Last Modified time: 2020-10-29 15:51:14
 * @Description: 知识库管理api
 */
import request from '@/utils/request';

//查询列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/knowledgeBase/pageList',
    method: 'post',
    params: query
  })
}

// 新增或者修改
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/knowledgeBase/saveOrUpdate',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

// 获取一条详情
export function getOne(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/knowledgeBase/getOne?id=' + id,
    method: 'post',
  })
}

// 删除操作 支持批量删除  eg:  ids = 123,123,123,123
export function deletes(ids) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/knowledgeBase/delete?ids=' + ids,
    method: 'post',
  })
}

