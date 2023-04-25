import request from '@/utils/request'

var applicationName = '/workflow';

// 查询流程信息列表
export function page(params) {
  return request({
    url: applicationName + '/security/workflow/workflowHistory/page',
    method: 'get',
    params: params
  })
}

//删除历史流程
export function del(modelId) {
  return request({
    url: applicationName + '/security/workflow/workflowHistory/delete/' + modelId,
    method: 'delete'
  })
}
