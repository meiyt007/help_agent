import request from '@/utils/request'

// 查询用户问题信息列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/userManagement/question/page',
    method: 'post',
    params: query
  })
}

// 新增或者修改
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/userManagement/question/save',
    method: 'post',
    data: data
  })
}

// 获取单个信息
export function getOne(id) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/userManagement/question/getOne/' + id,
    method: 'get'
  })
}

//删除管理
export function del(id) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/userManagement/question/delete/' + id,
    method: 'post',
  })
}
//获取工作人员信息
export function getWorkPersonInfo() {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/work/user/UserServicePage',
    method: 'post'
  })

}

// 初始化信息
/*export function init(oid) {
  if(oid == undefined){
    oid ='';
  }
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH +'/dataChange/fieldType/init/'+oid,
    method: 'get'
  })
}*/


// 启禁用信息
/*export function able(oid) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH +'/dataChange/fieldType/able/'+oid,
    method: 'post'
  })
}*/




