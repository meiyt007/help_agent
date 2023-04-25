import request from '@/utils/request';

//查询卡证目录列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/cardCatalogue/queryCardCatalogueWithPage',
    method: 'get',
    params: query
  })
}

// 获取一条卡证详情
export function getOne(oid) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/cardCatalogue/getCardCatalogueByOid',
    params: {
      oid: oid
    },
    method: 'get',
  })
}

// 材料目录和目录子项新增或者修改
/*export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/cardCatalogue/saveOrUpdateCatalogue',
    method: 'get',
    params: data
  })
}*/

// 材料目录和目录子项新增或者修改
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/cardCatalogue/saveOrUpdateCatalogue',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}



// 删除材料目录和目录子项信息
export function del(oid) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/cardCatalogue/deleteByOid',
    params: {
      oid: oid
    },
    method: 'get'
  })
}



