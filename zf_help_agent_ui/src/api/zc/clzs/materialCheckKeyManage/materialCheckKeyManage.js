/**
 * 材料审查要点配置管理
 * @author: chenjm
 * @date: 2020-11-10
 */
import request from '@/utils/request';

//查询事项列表
export function page(query) {

  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxService/sxServicePage',
    method: 'post',
    params: query
  })
}
//查询一件事目录列表
export function getTaocanList(query) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directory/page',
    method: 'get',
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

//获取事项详情
export function serviceListHasOrNot(serviceOids) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxService/viewSxService?serviceOids='+serviceOids,
    method: 'get'
  })
}

//保存/更新样表信息
export function saveOrUpdateAhsSamplePicInfo(data) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/examinePointCarding/saveOrUpdateAhsSamplePicInfo',
    method: 'post',
    data: data,
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    }
  })
}
//根据样表信息查询列表
export function getAhsSamplePicInfoList(data) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/examinePointCarding/getAhsSamplePicInfoList',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    method: 'post',
    data: data
  })
}

// 删除样表信息
export function deleteAhsSamplePicInfo(id) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/examinePointCarding/deleteAhsSamplePicInfo',
    params: {
      id: id
    },
    method: 'post'
  })
}

//根据材料和办件查询事项办件附件
export function querySysAttaListByMaterialOid(materialOid,caseOid) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH +'/examinePointCarding/querySysAttaListByMaterialOid/',
    params: {
      materialOid: materialOid,
      caseOid:caseOid,
    },
    method: 'post'
  })
}




export function downloadSysAtta(attaOid) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH  +'/sysAttaService/download/',
    params: {
      attaOid: attaOid,
    },
    method: 'get'
  })
}


export function queryComboAttaListByMaterialOid(materialOid,caseOid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH +'/comboExaminePointCarding/queryComboAttaListByMaterialOid/',
    params: {
      materialOid: materialOid,
      caseOid:caseOid,
    },
    method: 'post'
  })
}
