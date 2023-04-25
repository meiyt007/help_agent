import request from '@/utils/request'

// 获取路由
export const getRouters = (appOid) => {
  if(undefined == appOid){
    appOid='';
  }
  return request({
    url: '/platform/security/syslogin/getRouters?appOid='+appOid,
    method: 'get'
  })
}
