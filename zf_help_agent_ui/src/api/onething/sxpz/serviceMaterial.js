import request from '@/utils/request'

// 查询一件事目录事项列表
export function querySxServiceList(comboDireOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/service/querySxServiceListByComboDireOid/'+comboDireOid,
    method: 'get'
  })
}

// 一件事目录公共材料的新增或者修改
export function saveMaterial(data) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directoryMaterial/saveOrupdateMaterial',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

// 获取目录整合材料oid集合
export function getSxMatersByDireMaterOid(oid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directoryMaterial/init?oid='+oid,
    method: 'get'
  })
}

//根据公共材料和单事项材料删除
export function delMaterialRel(pubMaterialOid,sxMaterialOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directoryMaterial/delRelByPubMaterialOidAndMaterialOid?pubMaterialOid='+pubMaterialOid+'&sxMaterialOid='+sxMaterialOid,
    method: 'get'
  })
}



