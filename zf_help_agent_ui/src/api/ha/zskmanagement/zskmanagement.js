import request from '@/utils/request';

// 获取用户管理分组树信息
export function queryServiceList(id) {
  if(id == undefined){
    id = '';
  }
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/work/knowledge/queryServiceList?id='+id,
    method: 'get'
  })
}
// 附件上传
export function uploadFile(data) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + "/work/knowledge/uploadFile",
    method: "POST",
    headers: {
      "Content-Type": "multipart/form-data"
    },
    data: data
  });
}
//附件下载
export function downLoadFile(attaOid) {
  window.location.href =
    process.env.VUE_APP_SERVICE_API +
    "/sxSys/atta/downloadFile?attaOid=" +
    attaOid;
}
//查询列表
export function page(query) {
    return request({
      url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/work/knowledge/KnowledgeServicePage',
      method: 'post',
      params: query
    })
  }

  // 根据id zsk信息
export function initZsk(id) {
  return request({
    url:  process.env.VUE_APP_BJFW_ROUTE_PATH + '/work/knowledge/getZskBaseById?id='+id,
    method: 'get'
  })
}
  // 根据id删除ZSK信息
  export function deleteZSKById(id) {
    return request({
      url:  process.env.VUE_APP_BJFW_ROUTE_PATH + '/work/knowledge/deleteKnowledgeId?id='+id,
      method: 'get'
    })
  }

  // 新增或者修改
export function saveOrUpdateZsk(data) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/work/knowledge/saveOrUpdata',
    method: 'post',
    data: data
  })
}
