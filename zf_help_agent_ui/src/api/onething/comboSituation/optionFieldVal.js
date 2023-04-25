import request from '@/utils/request'

let applicationName = 'form';

//保存
export function saveOptionFieldValRel(data){
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/optionFieldValRel/saveOptionFieldValRel',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data:data
  })
}

//分页查询
export function page(query) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/optionFieldValRel/page',
    method: 'get',
    params: query
  })
}

//初始化
export function initFieldValRel(comboDireOid, relOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/optionFieldValRel/initOptionFieldValRel?comboDirectoryOid='+comboDireOid+'&relOid='+relOid,
    method: 'get'
  })
}

//删除
export function delOptionFieldValRel(oid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/optionFieldValRel/delOptionFieldValRel/' + oid,
    method: 'delete'
  })
}

//查询标签下字段
export function queryComboFieldList(thingOid, typeOid, labelOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/fillField/queryComboFillFieldList?comboDirectoryOid='
      + thingOid + '&typeOid=' + typeOid + '&labelOid=' + labelOid,
    method: 'get'
  })
}

// 获取表单信息
export function getFormInfo(thingOid, typeOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/fillForm/getFormInfoByThingOidAndTypeOid',
    method: 'get',
    params:{thingOid:thingOid,typeOid:typeOid}
  })
}

// 查询字段下选项值
export function queryComboFieldValList(code, formCode) {
  return request({
    url: process.env.VUE_APP_FORM_API_ROUTE_PATH +'/manager/getDesignInfoByCode?code=' + code + '&formCode=' + formCode,
    method: 'get'
  })
}

