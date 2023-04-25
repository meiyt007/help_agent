import request from '@/utils/request'
import { getToken, setToken, removeToken } from '@/utils/auth'

// 附件上传
export function uploadFile() {
  const baseURL = process.env.VUE_APP_BASE_API;
  return baseURL + "/platform/security/atta/uploadFile";
}

// 查询附件信息列表
export function pageFile(query) {
  return request({
    url: '/platform/security/atta/page',
    method: 'get',
    params: query
  })
}


//删除附件管理
export function delFile(oid) {
  return request({
    url: '/platform/security/atta/delete/' + oid,
    method: 'delete'
  })
}

// 获取单个附件信息
export function getOneFile(oid) {
  return request({
    url: '/platform/security/atta/getOne/'+oid,
    method: 'get'
  })
}

// 根据业务主键获取单个附件信息
export function getSysAttaByAttaOid(attaOid) {
  return request({
    url: '/platform/security/atta/getSysAttaByAttaOid/' + attaOid,
    method: 'get'
  })
}
export function uploadCompressImage() {
  const baseURL = process.env.VUE_APP_BASE_API;
  return baseURL + "/platform/security/atta/uploadCompressImage";
}


// 根据id 集合 获取 attaJSON信息
export function getAttaListByOids(attaOids) {
  return request({
    url:  '/platform/security/atta/getAttaListByOids?attaOids='+attaOids,
    method: 'get'
  })
}

export function getPdf(fileOid) {
  return request({
    url:  '/platform/security/atta/downloadFile/'+fileOid + '?access_token=' +  getToken(),
    method: 'get',
    headers: {
      'Content-Type': 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
    },
    responseType: 'blob'  //设置响应的数据类型为一个包含二进制数据的 Blob 对象，必须设置！！！
  })
}
