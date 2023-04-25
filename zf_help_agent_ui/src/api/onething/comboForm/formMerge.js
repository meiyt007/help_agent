import request from '@/utils/request'
// 获取字段标签列表
export function getLabelList(thingOid,fieldTypeOid,comboFormOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/label/queryComboFillLabelAndFieldList',
    method: 'get',
    params:{thingOid:thingOid,fieldTypeOid:fieldTypeOid,formOid:comboFormOid}
  })
}


export function queryCouldMergeLabelList(thingOid,fieldTypeOid,comboFormOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/label/queryCouldMergeLabelList',
    method: 'get',
    params:{thingOid:thingOid,fieldTypeOid:fieldTypeOid,formOid:comboFormOid}
  })
}

export function resetLabel(thingOid,fieldTypeOid,comboFormOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/fillForm/reset',
    method: 'post',
    params:{thingOid:thingOid,fieldTypeOid:fieldTypeOid,formOid:comboFormOid}
  })
}

export function mergeLabel(labelOidList,name,thingOid,fieldTypeOid,formOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/label/mergeLabel',
    method: 'post',
    params:{labelOidList:labelOidList,name:name,fieldTypeOid:fieldTypeOid,thingOid:thingOid,formOid:formOid}
  })
}


export function moveField(labelOid,fieldOids) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/fillField/moveField',
    method: 'post',
    params:{labelOid:labelOid,fieldOids:fieldOids}
  })
}



export function getMergeFieldInfo(mergeFieldOids) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/fillField/getMergeFieldInfo',
    method: 'get',
    params:{mergeFieldOids:mergeFieldOids}
  })
}


export function mergeField(data) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/fillField/mergeField',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}
