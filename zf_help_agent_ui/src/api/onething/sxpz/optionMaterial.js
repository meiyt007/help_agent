import request from '@/utils/request'

// 获取选项值列表信息
export function queryComOptionVal(titleOid,comboDirectoryOid,relOid) {
  if(titleOid == undefined){
    titleOid = '';
  }
  if(relOid == undefined){
    relOid = '';
  }
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/OptionVal/queryComboOptionValByComboDireOidList?titleOid='+titleOid+'&comboDireOid='+comboDirectoryOid+"&relOid="+relOid,
    method: 'get'
  })
}
//删除选项关系
export function del(oid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/optionMaterial/delete/' + oid+'?oid='+oid,
    method: 'delete'
  })
}

export function chooseOptionMaterial(comboDirectoryOid,materialOid,valOid,relOid){
  if(relOid == undefined){
    relOid = '';
  }
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/optionMaterial/saveOptionMaterial?comboDirectoryOid='+comboDirectoryOid+'&valOid='+valOid+'&materialOid='+materialOid+'&relOid='+relOid,
    method: 'get',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data:''
  })
}

export function queryOptionMaterial(comboDirectoryOid) {

  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/optionMaterial/queryOptionMaterialList?comboDirectoryOid='+comboDirectoryOid,
    method: 'get'
  })
}
