import request from '@/utils/request'

// 查询消息信息列表
export function page(query) {
  return request({
    url: '/platform/security/message/page',
    method: 'get',
    params: query
  })
}

// 初始化消息信息
export function init(id) {
  if(id == undefined){
    id ='';
  }
  return request({
    url: '/platform/security/message/init/'+id,
    method: 'get'
  })
}

// 发送消息
export function send(data) {
  return request({
    url: '/platform/security/message/send',
    method: 'post',
    data: data
  })
}

//删除消息管理
export function del(id) {
  return request({
    url: '/platform/security/message/delete/' + id,
    method: 'post'
  })
}

// 查看消息信息
export function read(id) {
  return request({
    url: '/platform/security/message/read/'+id,
    method: 'post'
  })
}

// 获取单个消息信息
export function getOne(id) {
  return request({
    url: '/platform/security/message/getOne/'+id,
    method: 'get'
  })
}


//已发消息列表
export function sendPage(query) {
  return request({
    url: '/platform/security/message/sendPage',
    method: 'get',
    params: query
  })
}
//删除已发消息管理
export function deleteSended(id) {
  return request({
    url: '/platform/security/message/deleteSended/' + id,
    method: 'post'
  })
}

// 查看已发消息信息
export function getOneSended(id) {
  return request({
    url: '/platform/security/message/getOneSended/'+id,
    method: 'get'
  })
}

//用户未读消息数量
export function getCountOfUnReadMessage() {
  return request({
    url: '/platform/security/message/getCountOfUnReadMessagebyUserOid',
    method: 'get'
  })
}
