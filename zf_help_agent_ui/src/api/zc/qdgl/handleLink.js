import request from '@/utils/request';

//查询列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxServiceLink/getLinkList',
    method: 'post',
    params: query
  })
}

//增加/修改
  export function save(data) {
    return request({
      url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxServiceLink/saveOrUpdateSxServiceLink',
      method: 'post',
      data: data
    })
  }

//查询详细
  export function detail(id) {
    return request({
      url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxServiceLink/detail?id='+id,
      method: 'get',
      params: id
    })
  }

//删除
  export function del(id) {
    return request({
      url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxServiceLink/delete?id='+id,
      method: 'get',
      params: id
    })
}

// 查询用户信息列表
export function userPage (query) {
  return request({
    //url: '/platform/security/sysuser/page',
    url: '/platform/security/sysuser/queryWithPage',
    method: 'get',
    params: query
  })
}

// 获取单个用户信息
export function get (userOid) {
  return request({
    url: '/platform/security/sysuser/getSysUserByUserOid/' + userOid,
    method: 'get'
  })
}
