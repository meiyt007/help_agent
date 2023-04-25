import request from '@/utils/request'

// 附件上传
export function uploadFile() {
  const baseURL = process.env.VUE_APP_BASE_API;
  return baseURL + process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + "/comboCase/atta/uploadFile";
}

export function downloadFile(attaOid) {
  //const baseURL = process.env.VUE_APP_ZC_ONETHING_API_PAGE;
  const baseURL = process.env.VUE_APP_ONETHING_API;
  location.href = baseURL+'/comboCase/atta/downloadFile/'+attaOid;
}

// 查询附件信息列表
export function pageFile(query) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH +'/comboCase/atta/page',
    method: 'get',
    params: query
  })
}

//删除附件管理
export function delFile(caseMaterialOid,oid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH +'/comboCase/atta/deleteFile?caseMaterialOid='+caseMaterialOid+"&attaOid=" + oid,
    method: 'delete'
  })
}



// 根据业务主键获取单个附件信息
export function getComboCaseAttaByAttaOid(attaOid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH +'/comboCase/atta/getComboCaseAttaByOid/' + attaOid,
    method: 'get'
  })
}
export function uploadCompressImage() {
  const baseURL = process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH;
  return baseURL + "/comboCase/atta/uploadCompressImage";
}

export function getPdf(fileOid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH +'/comboCase/atta/downloadFile/'+fileOid,
    method: 'get',
    headers: {
      'Content-Type': 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
    },
    responseType: 'blob'  //设置响应的数据类型为一个包含二进制数据的 Blob 对象，必须设置！！！
  })
}


// 材料下载
export function fileDownLoad(fileOid) { 
  const baseURL = process.env.VUE_APP_BASE_API;  
  location.href = baseURL+process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH+'/comboCase/atta/downloadFile/'+fileOid;
}
