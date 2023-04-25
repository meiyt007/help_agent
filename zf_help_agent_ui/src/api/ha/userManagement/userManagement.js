import request from '@/utils/request';

//查询列表
export function onlinePage(query) {
  console.log(query);
    return request({
      url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/online/OnlineServicePage',
      method: 'post',
      params: query
    })
  }

// 获取用户管理分组树信息
export function queryWorkGroupTree(id) {
  if(id == undefined){
    id = '';
  }
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/work/group/queryHaWorkGroupTree?id='+id,
    method: 'get'
  })
}


// 获取用户管理分组树信息
export function queryWorkGroupSplitTree(groupId) {
  if(groupId === undefined){
    groupId = '';
  }
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/work/groupSplit/queryHaWorkGroupTree?groupId='+groupId,
    method: 'get'
  })
}
// 根据id User信息
export function initUser(id) {
  return request({
    url:  process.env.VUE_APP_BJFW_ROUTE_PATH + '/work/user/getHaUserById?id='+id,
    method: 'get'
  })
}
//上传头像
export function uploadCompressImage() {
  const baseURL = process.env.VUE_APP_BASE_API; //这个地址要写固定地址，因为action只能写固定地址，不然会出现跨域问题
  return process.env.VUE_APP_BJFW_ROUTE_PATH + '/work/user/uploadImage';
}

export function uploadImage(data) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/work/user/uploadImage',
    method: 'post',
    params: data
  })
}
//查询列表
export function page(query) {
  console.log(query);
    return request({
      url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/work/user/UserServicePage',
      method: 'post',
      params: query
    })
  }

// 新增或修改
export function saveUser(data) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH +'/work/user/save',
    method: 'post',
    data: data
  })
}

//单调删除
export function UserById(id){
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH +'/work/user/deleteUserOid?id='+id,
    method: 'get',
  })
}

//批量删除
export function batchRemoveUser(idList){
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH +'/work/user/deleteUserListid?ids='+idList,
    method: 'get',
  })
}
//重置密码
export function reserpassword(id){
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH +'/work/user/ResetPassword?id='+id,
    method: 'get',
  })
}
