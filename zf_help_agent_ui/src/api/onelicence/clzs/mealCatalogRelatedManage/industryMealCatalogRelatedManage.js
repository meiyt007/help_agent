/**
 * 一件事目录关联管理
 * @author: chenjm
 * @date: 2020-11-06
 */
import request from '@/utils/request';

// 查询一件事主题信息列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directory/page',
    method: 'get',
    params: query
  })
}
//获取一件事详情
export function getMealOne(id) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directory/getOne/'+id,
    method: 'get'
  })
}
// 保存或者修改
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/serviceMaterial/updateByMaterialOid',
    method: 'post',
    data: data,
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    }
  })
}

//查询一级材料目录列表
export function pageList() {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/materialCatalog/getMaterialCatalogList',
    method: 'post'
  })
}

//获取事项详情
export function getSxServiceOne(id) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxService/viewSxService?serviceOid='+id,
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


//根据一件事查询关联事项主键
export function pageServiceOidList(comboDirectoryOid) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryMealCatalogRelated/queryListByComboDirectoryOid?comboDirectoryOid='+comboDirectoryOid,
    method: 'post'
  })
}




// 查询一件事目录公共材料列表
export function queryDirectoryMaterialList(comboDireOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directoryMaterial/queryComboDirectoryMaterialByComboDireOid/'+comboDireOid,
    method: 'get'
  })
}


//更新材料
export function saveOrUpdateComboDirectoryMaterial(data) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/comboMealCatalogRelated/saveOrUpdateComboDirectoryMaterial',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

