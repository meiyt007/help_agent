import request from '@/utils/request'

//保存
export function saveOptionFieldRel(data){
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/optionFieldRel/saveOptionFieldRel',
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
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/optionFieldRel/page',
    method: 'get',
    params: query
  })
}

//初始化阻塞配置
export function initFieldRel(comboDireOid, relOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/optionFieldRel/initOptionFieldRel?comboDirectoryOid='+comboDireOid+'&relOid='+relOid,
    method: 'get'
  })
}

//删除阻塞配置
export function delOptionFieldRel(oid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/optionFieldRel/delOptionFieldRel/' + oid,
    method: 'delete'
  })
}

//查询标签树
export function getComboTypeAndLabelTree(oid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/label/queryComboFieldTypeAndLabelTree?thingOid=' + oid,
    method: 'get'
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
