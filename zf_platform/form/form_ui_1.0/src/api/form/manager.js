import request from '@/utils/request'

var applicationName = process.env.VUE_APP_FORM_API_ROUTE_PATH + '/security/manager';

// 获取表单树信息
export function queryAuthorizeTree(authorizeKey) {
  if(authorizeKey == undefined){
    authorizeKey = '';
  }
  return request({
    url: applicationName + '/queryAuthorizeTree?authorizeKey='+authorizeKey,
    method: 'get'
  })
}
