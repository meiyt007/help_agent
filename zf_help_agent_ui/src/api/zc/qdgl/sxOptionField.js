import request from '@/utils/request';

// 查询表单字段列表
export function sxFillFieldList (serviceOid, typeOid) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/matter/fillField/queryFieldList?serviceOid=' + serviceOid
      + "&fieldTypeOid=" + typeOid,
    method: 'get'
  })
}

// 保存
export function saveOrUpdateOptionField(data) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/matter/fillField/saveOrUpdateOptionField',
    method: 'POST',
    data: data
  })
}

// 关系列表
export function valOptionFieldList (valOid) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/matter/fillField/queryOptionFieldListByValOid?valOid=' + valOid,
    method: 'get'
  })
}

/*=======*/
// 查询填充值列表
export function getFieldFillValList (serviceOid) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/matter/sxOptionFieldVal/getFieldFillValList?serviceOid=' + serviceOid,
    method: 'get'
  })
}

// 初始化字段填充
export function initOptionFieldValInfo (serviceOid, oid) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/matter/sxOptionFieldVal/initOptionFieldValInfo?serviceOid=' + serviceOid
      + "&oid=" + oid,
    method: 'get'
  })
}

// 查询标签下的所有字段
export function querySxFieldList (serviceOid, fieldTypeOid, labelOid) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/matter/fillField/queryFieldListInfo',
    method: 'get',
    params: { serviceOid, fieldTypeOid, labelOid }
  })
}

// 查询字段下选项值
export function querySxFieldValList(code, formCode) {
  return request({
    url: process.env.VUE_APP_FORM_API_ROUTE_PATH +'/manager/getDesignInfoByCode?code=' + code + '&formCode=' + formCode,
    method: 'get'
  })
}

// 获取表单信息
export function getSxFormInfo(serviceOid, typeOid) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH +'/matter/formInfo/getDesignFormListByServiceOidAndTypeOid',
    method: 'get',
    params:{serviceOid:serviceOid,typeOid:typeOid}
  })
}

//保存
export function saveSxOptionFieldValInfo(data){
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH +'/matter/sxOptionFieldVal/saveSxOptionFieldValInfo',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data:data
  })
}

//删除
export function delSxOptionFieldVal(oid) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH +'/matter/sxOptionFieldVal/delSxOptionFieldVal?oid=' + oid,
    method: 'get'
  })
}
