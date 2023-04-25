import request from '@/utils/request'

// 查询平板评价信息列表
export function page (query) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/evaluation/evaluationStandard/listEvaluationStandardPage',
    method: 'get',
    params: query
  })
}
// 权限信息的新增或者修改
export function save (data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/evaluation/evaluationStandard/saveEvaluationStandard',
    method: 'post',
    headers: {
      "Content-Type": "application/json;charset=UTF-8",
    },
    data: data
  })
}

// 权限信息的新增或者修改
export function saveOptions (data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/evaluation/evaluationOption/saveEvaluationOption',
    method: 'post',
    headers: {
      "Content-Type": "application/json;charset=UTF-8",
    },
    data: data
  })
}

// 查询评价选项
export function init (standardOid) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/evaluation/evaluationOption/queryEvaluationOptionList?standardOid=' + standardOid,
    method: 'get',
  })
}
// 删除评价
export function del (standardOid) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/evaluation/evaluationStandard/delEvaluation?standardOid=' + standardOid,
    method: 'get',
  })
}

// 查询评价选项
export function delEvaluationOption (id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/evaluation/evaluationOption/delEvaluationOption?id=' + id,
    method: 'get',
  })
}

