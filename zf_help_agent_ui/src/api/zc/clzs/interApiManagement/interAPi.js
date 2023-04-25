import request from '@/utils/request';

//查询卡证目录列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/interApi/queryInfoWithPage',
    method: 'post',
    data: query
  })
}

// 获取一条卡证详情
export function getOne(oid) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/interApi/getInfoByOid',
    params: {
      id: oid
    },
    method: 'get',
  })
}

// 材料目录和目录子项新增或者修改
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/interApi/saveOrUpdate',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

// 删除材料目录和目录子项信息
export function del(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/interApi/deleteByOid',
    params: {
      id: id
    },
    method: 'get'
  })
}

//添加修改时判断材料类别是否重复
export function checkHasRepeat(id, code) {
  if(id =="" || id ==undefined){
    id="";
  }
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/interApi/checkHasRepeat?id=' +id+ '&code='+code,
    method: 'get'
  })
}

/**获得服务列表信息 */
export function queryServerList() {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/interApi/queryServerList',
    params: {},
    method: 'get'
  })
}

/**获得服务api信息 */
export function queryServerInterfaceList(serverOid) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/interApi/queryServerInterfaceList',
    params: {
      serverOid:serverOid
    },
    method: 'get'
  })
}

