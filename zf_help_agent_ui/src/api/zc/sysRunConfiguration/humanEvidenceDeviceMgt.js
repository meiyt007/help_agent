/**
 * @Author: kkfan
 * @Date: 2020-10-28 15:48:33
 * @Last Modified by: kkfan
 * @Last Modified time: 2020-10-28 15:49:35
 * @Description: 人证核验管理 api
 */
import request from '@/utils/request'

//分页查询列表操作
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/humanEvidenceDeviceMgt/pageList',
    method: 'post',
    params: query
  })
}

// 新增或者修改操作
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/humanEvidenceDeviceMgt/saveOrUpdate',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

// 获取详情操作
export function getOne(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/humanEvidenceDeviceMgt/getOne?id=' + id,
    method: 'post',
  })
}

// 删除操作 支持批量删除  eg:  ids = 123,123,123,123
export function deletes(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/humanEvidenceDeviceMgt/delete?ids=' + id,
    method: 'post',
  })
}
