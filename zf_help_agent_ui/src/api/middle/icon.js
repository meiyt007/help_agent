import request from '@/utils/request'

//查询服务图标数据
export function page(query) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/icon/list',
    method: 'get',
    params: query
  })
}

//修改图标信息获取其数据
export function editIcon(id) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/icon/init',
    params: {
      oid: id
    },
    method: 'post'
  })
}

//保存图标信息
export function saveIcon(data) {
  console.log(data)
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH +'/middleManager/middle/icon/save',
    method: 'post',
    data: data
  })
}

//删除图标
export function del(oid) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH +'/middleManager/middle/icon/delete',
    method: 'get',
    params: {
      oid: oid
    }
  })
}

//查看图标信息
export function getOne(id) {
  return request({
    url: process.env.VUE_APP_MIDDLE_ROUTE_PATH + '/middleManager/middle/icon/view',
    params: {
      oid: id
    },
    method: 'post'
  })
}
