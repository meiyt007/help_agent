import request from '@/utils/request'
import { getToken } from '@/utils/auth'

var applicationName = process.env.VUE_APP_FORM_API_ROUTE_PATH + '/manager/security/formAtta';
const baseURL = process.env.VUE_APP_BASE_API;

// 附件上传
export function uploadFormFile() {
  return baseURL + "/form/manager/uploadFormFile";
}

//上传图片
export function uploadImage() {
  return baseURL + "/form/manager/security/formAtta/uploadImage";
}

//文件下载
export function download(attaOid) {
  window.location.href = baseURL + "/form/manager/downloadFile/" +attaOid + '?access_token=' +  getToken();
}

//获取pdf文件
export function getPdf(fileOid) {
  return request({
    url:  baseURL + '/form/manager/downloadFile/'+fileOid + '?access_token=' +  getToken(),
    method: 'get',
    headers: {
      'Content-Type': 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
    },
    responseType: 'blob'  //设置响应的数据类型为一个包含二进制数据的 Blob 对象，必须设置！！！
  })
}

//删除附件管理
export function delFile(oid) {
  return request({
    url: applicationName  + '/delete/' + oid,
    method: 'delete'
  })
}

// 获取单个附件信息
export function getOneFile(oid) {
  return request({
    url: applicationName  + '/getOne/'+oid,
    method: 'get'
  })
}

// 根据业务主键获取单个附件信息
export function getSysAttaByAttaOid(attaOid) {
  return request({
    url: applicationName  + '/getSysAttaByAttaOid/' + attaOid,
    method: 'get'
  })
}

// 根据id 集合 获取 attaJSON信息
export function getAttaListByOids(attaOids) {
  return request({
    url:  applicationName  + '/getAttaListByOids?attaOids='+attaOids,
    method: 'get'
  })
}
