import request from '@/utils/request'

// 查询信息列表
export function page(query) {
  return request({
    url: '/platform/security/holiday/page',
    method: 'get',
    params: query
  })
}

// 初始化信息
export function init(oid) {
  if(oid == undefined){
    oid ='';
  }
  return request({
    url: '/platform/security/holiday/init/'+oid,
    method: 'get'
  })
}

// 应用的新增或者修改
export function save(data) {
  return request({
    url: '/platform/security/holiday/save',
    method: 'post',
    data: data
  })
}

//删除管理
export function del(oid) {
  return request({
    url: '/platform/security/holiday/delete/' + oid,
    method: 'post'
  })
}

// 获取单个信息
export function getOne(oid) {
  return request({
    url: '/platform/security/holiday/getOne/'+oid,
    method: 'get'
  })
}

