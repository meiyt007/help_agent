import request from '@/utils/request';

//查询识别模板列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/faModelTemplate/queryFaModelTemplateWithPage',
    method: 'get',
    params: query
  })
}


// 获取一条材料类型详情
export function getOne(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/materialCategory/getOne',
    params: {
      id: id
    },
    method: 'get',
  })
}
// 材料类型新增或者修改
export function saveOrUpdate(data) {
  /* alert(JSON.stringify(data))*/
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/materialCategory/saveOrUpdate',
    method: 'post',
    data: data,
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    }
  })
}


// 删除材料类型信息
export function del(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/materialCategory/delete',
    params: {
      id: id
    },
    method: 'post'
  })
}

// 删除材料类型信息
export function delTemplate(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/faModelTemplate/del?id='+id,
    method: 'get'
  })
}


export function checkisExist(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/materialCategory/checkIsExist?materialCategoryOid=' + id,
    method: 'get'
  })
}

export function able(id,materialCatalogOid,ableFlag) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/faModelTemplate/able',
    params: {
      id: id,
      materialCatalogOid:materialCatalogOid,
      ableFlag:ableFlag,
    },
    method: 'get'
  })
}


