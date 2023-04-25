import request from '@/utils/request';
// 分页查询列表操作
export function pageUser(query) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/help/userManager/userResource/queryHaUserResourceByNameAndWorkUserName',
    method: 'get',
    params: query
  })
}
// 获取资源树信息
export function queryResourcrSimpleTree() {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/help/userManager/userResource/queryAllResourceSimpleTree',
    method: 'get'
  })
}
// 用户资源新增或者修改
export function saveUser(data) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH +'/help/userManager/userResource/save',
    method: 'post',
    data: data
  })
}
// 初始化用户列表
export function initUserList() {
  return request({
    url:  process.env.VUE_APP_BJFW_ROUTE_PATH + '/work/user/UserServicePage',
    method: 'post'
  })
}

// 初始化用户列表
export function initResourceFolder() {
  return request({
    url:  process.env.VUE_APP_BJFW_ROUTE_PATH + '/help/userManager/userResource/queryHaUserResourceList',
    method: 'get'
  })
}
// 根据id获取办件
export function querySysAttaByOid(oid) {
  return request({
    url:  process.env.VUE_APP_BJFW_ROUTE_PATH + '/sysAttaService/querySysAttaByOid?attaOid='+oid,
    method: 'get'
  })
}

// 根据id获取用户资源
export function initUser(id) {
  return request({
    url:  process.env.VUE_APP_BJFW_ROUTE_PATH + '/help/userManager/userResource/getHaUserResourceById?id='+id,
    method: 'get'
  })
}

// 附件上传
export function uploadsxFile(data) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/help/userManager/userResource/uploadFile',
    method: 'POST',
    headers:{
      "Content-Type":"multipart/form-data",
    },
    data:data
  })
}
//删除用户
export function del(id) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH +'/help/userManager/userResource/deleteById?id='+id,
    method: 'get',
  })
}

/**
 * 根据id下载材料
 * @param attaOid
 * @returns {AxiosPromise}
 */
export function downloadSysAtta(attaOid) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH  +'/help/userManager/userResource/downloadAtta/',
    params: {
      attaOid: attaOid
    },
    method: 'get'
  })
}

export function queryHaUserResourceDataByIdAndType(id,type) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH  +'/help/userManager/userResource/queryHaUserResourceDataByIdAndType/',
    params: {
      id: id,
      type: type
    },
    method: 'get'
  })
}


