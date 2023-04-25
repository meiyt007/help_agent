import request from '@/utils/request'

// 根据业务主键获取单个附件信息
export function getCaseAttaByAttaOid(attaOid) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH +'/sysAttaService/querySysAttaByOid?attaOid=' + attaOid,
    method: 'get'
  })
}
export function uploadCompressImage() {
  const baseURL = process.env.VUE_APP_ZC_ROUTE_PATH;
  return baseURL + "/comboCase/atta/uploadCompressImage";
}

export function getPdf(fileOid) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH +'/sysAttaService/download?attaOid='+fileOid,
    method: 'get',
    headers: {
      'Content-Type': 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
    },
    responseType: 'blob'  //设置响应的数据类型为一个包含二进制数据的 Blob 对象，必须设置！！！
  })
}

// 根据业务主键获取单个附件信息
export function getMaterialAttaByAttaOid(attaOid) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/sxSys/file/downloadFile/'+attaOid,
    method: 'get'
  })
}

export function getMaterialPdf(fileOid) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH +'/sxSys/file/downloadFile/'+fileOid,
    method: 'get',
    headers: {
      'Content-Type': 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
    },
    responseType: 'blob'  //设置响应的数据类型为一个包含二进制数据的 Blob 对象，必须设置！！！
  })
}
