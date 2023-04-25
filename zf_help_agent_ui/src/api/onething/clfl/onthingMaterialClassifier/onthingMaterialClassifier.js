import request from '@/utils/request'

// 查询一件事目录公共材料列表
export function queryDirectoryMaterialList(comboDireOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directoryMaterial/queryPubDirectoryMaterialByComboDireOid/'+comboDireOid,
    method: 'get'
  })
}



// 查询一件事目录关联的事项材料列表
export function queryComboDireSxMaterList(comboDireOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directoryMaterial/queryComboDireMaterRelList/'+comboDireOid,
    method: 'get'
  })
}


// 保存或者修改
export function saveOrUpdateMaterialClassifier(data) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/directoryMaterial/saveOrUpdateMaterialClassifier',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}




