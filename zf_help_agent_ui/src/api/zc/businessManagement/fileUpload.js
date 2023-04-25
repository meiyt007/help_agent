import request from '@/utils/request'


// 附件上传--根据路径
export function uploadFileByPaths(data) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/common/uploadFileByPaths',
    method: 'POST',
    data:data
  })
}

// 附件上传--根据路径
export function downFileByoid(oid) {
  return request({
    url:  process.env.VUE_APP_BJFW_ROUTE_PATH + "/sysAttaService/preview?attaOid="+ oid,
    method: 'GET'
  })
}

//根据oid查询附件信息
export function querySysAttaByOid(oid) {
  return request({
    url:  process.env.VUE_APP_BJFW_ROUTE_PATH + "/sysAttaService/querySysAttaByOid?attaOid="+ oid,
    method: 'GET'
  })
}



