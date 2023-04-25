/**
 * @Author: kkfan
 * @Date: 2020-10-28 15:47:06
 * @Last Modified by: kkfan
 * @Last Modified time: 2020-10-28 16:48:29
 * @Description: 知识库分类管理 api
 */
import request from '@/utils/request';

//查询列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/knowledgeBaseClassification/pageList',
    method: 'post',
    params: query
  })
}

// 新增或者修改
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/knowledgeBaseClassification/saveOrUpdate',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

// 验证是否重复
export function validateRepeat(zskDictOid, code, name, parentOid) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/knowledgeBaseClassification/validateRepeat?code=' + code +'&name=' + name+'&parentOid=' + parentOid +'&zskDictOid=' + zskDictOid,
    method: 'get',
  })
}
// 获取一条详情
export function getOne(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/knowledgeBaseClassification/getOne?id=' + id,
    method: 'post',
  })
}

// 删除操作 支持批量删除  eg:  ids = 123,123,123,123
export function deletes(ids) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/knowledgeBaseClassification/delete?ids=' + ids,
    method: 'post',
  })
}

// 获取知识库分类树
export function queryTree(parentOid) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/knowledgeBaseClassification/queryKnowledgeBaseClassificationTree?parentOid=' + (parentOid ? parentOid : ''),
    method: 'post',
  })
}

export function checkisExist(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/knowledgeBase/checkIsExist?zskDictOid=' + id,
    method: 'get'
  })
}

