import request from '@/utils/request';

//查询列表
export function page(query) {
/*  alert(JSON.stringify(query))*/
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxService/sxServicePage',
    method: 'post',
    params: query
  })
}
//获取事项详情
export function getSxServiceOne(id) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxService/viewSxService?serviceOid='+id,
    method: 'get'
  })
}


// 保存或者修改
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/serviceMaterial/updateByMaterialOid',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}


// 保存或者修改



//查询一级材料目录列表
export function pageList() {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/materialCatalog/getMaterialCatalogList',
    method: 'post'
  })
}

//二级事项列表
export function querySubitemMaterialCatalogList() {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/materialCatalog/querySubitemMaterialCatalogList',
    method: 'get'
  })
}
//目录树
export function queryCatalogTree() {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/common/queryCatalogTree',
    method: 'get'
  })
}

// 查询当前人员下所有授权的事项
export function getOneByUserOid(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/sxServiceRegistrar/getOneByUserOid?id=' + id,
    method: 'post',
  })
}
//区划机构事项树
export function queryServiceTree() {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/sxServiceRegistrar/queryServiceTree',
    method: 'post',
  })
}

// 人员授权保存或者修改
export function saveOrUpdatePersonReg(data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/sxServiceRegistrar/saveOrUpdatePersonReg',
    method: 'post',
    data: data,
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    }
  })
}


//修改保存事项材料
export function saveOrUpdateSxServiceMaterial(data) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxService/saveOrUpdateSxServiceMateria',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}



//根据事项oid获取颗粒化材料列表
export function getServiceMaterialsByServiceOid(serviceOid) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/serviceMaterial/getServiceMaterialsByServiceOid?serviceOid='+serviceOid,
    method: 'get'
  })
}

export function saveOrUpdateServiceMaterial(data) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxService/saveOrUpdateServiceMaterial',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

//根据oid查询事项和材料
export function geSxServiceAndMaterial(id) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxService/geSxServiceAndMaterial?serviceOid='+id,
    method: 'get'
  })
}

//修改保存精细化材料
export function updateRefinedMaterialList(data) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/refindeMaterial/updateRefinedMaterialList',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}
