import request from '@/utils/request';

// 辖区授权保存或者修改
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/serviceSiteAuthorize/saveOrUpdate',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

// 获取详情
export function getSiteOidsByServiceOid(id) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/serviceSiteAuthorize/getSiteOidsByServiceOid?id=' + id,
    method: 'post',
  })
}

//辖区树
export function querySiteAuthorizeTree() {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/serviceSiteAuthorize/querySiteAuthorizeTree',
    method: 'post',
  })
}


//取消授权
export function del(oid) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH +'/serviceSiteAuthorize/deleteServiceSiteAuth?serviceOid=' + oid,
    method: 'delete'
  })
}

