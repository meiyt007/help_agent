import request from '@/utils/request'

// 查询一件事目录公共材料列表
export function queryDirectoryMaterialList(comboDireOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directoryMaterial/queryPubDirectoryMaterialByComboDireOid/'+comboDireOid,
    method: 'get'
  })
}

// 获取目录整合材料oid集合
export function getSxMatersByDireMaterOid(oid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directoryMaterial/querySxServiceMaterialListByMaterOid/'+oid,
    method: 'get'
  })
}
//删除一件事目录公共材料
export function del(oid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directoryMaterial/delete/' + oid,
    method: 'delete'
  })
}


// 查询一件事目录关联的事项材料列表
export function queryComboDireSxMaterList(comboDireOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directoryMaterial/queryComboDireSxMaterList/'+comboDireOid,
    method: 'get'
  })
}

// 查询一件事目录未整合事项材料列表
export function querySxServiceMaterList(comboDireOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/service/querySxServiceMaterList/'+comboDireOid,
    method: 'get'
  })
}
//确认一件事目录公共材料
export function setMaterialStatus(oid){
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directoryMaterial/setMaterialStatus/' + oid,
    method: 'get'
  })
}


//删除事项材料与一件事目录关联
export function delSxMater(comboDireOid,materialOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directoryMaterial/deleteSxMater?comboDireOid='+comboDireOid+'&materialOid=' + materialOid,
    method: 'delete'
  })
}


