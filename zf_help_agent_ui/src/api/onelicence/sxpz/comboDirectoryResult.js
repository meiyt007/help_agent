import request from '@/utils/request'

// 查询一件事目录统一结果列表
export function queryDirectoryResultList(comboDireOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directoryResult/queryComboDirectoryResultByComboDireOid/'+comboDireOid,
    method: 'get'
  })
}

// 获取目录整合结果oid集合
export function getSxResultsByDireResultOid(oid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directoryResult/querySxServiceResultListByResultOid/'+oid,
    method: 'get'
  })
}

// 查询一件事目录被选择事项结果列表
export function querySxServiceResultList(comboDireOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/service/querySxServiceResultList/'+comboDireOid,
    method: 'get'
  })
}
//确认一件事目录统一结果
export function setRtStatus(oid){
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directoryResult/setResultStatus/' + oid,
    method: 'get'
  })
}

//删除一件事目录统一结果
export function del(oid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directoryResult/delete/' + oid,
    method: 'delete'
  })
}

//删除事项结果与一件事目录关联
export function delSxResult(id) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directoryResult/deleteSxResult/' + id,
    method: 'delete'
  })
}


