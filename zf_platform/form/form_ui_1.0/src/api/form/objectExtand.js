import request from '@/utils/request'

var applicationName = process.env.VUE_APP_FORM_API_ROUTE_PATH + '/manager/security/objectExtand';

// 查询存储对象扩展信息列表
export function queryFormObjectExtandList(objectOid) {
  return request({
    url: applicationName + '/queryFormObjectExtandList/'+objectOid,
    method: 'get'
  })
}

