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

export function chooseOptionrel(comboDirectoryOid,titleOid,valOid,relOid,relType){
  if(relOid == undefined){
    relOid = '';
  }
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/optionRel/saveComboOptionRel?comboDirectoryOid='+comboDirectoryOid+'&valOid='+valOid+'&titleOid='+titleOid+'&relOid='+relOid+'&relType='+relType,
    method: 'get',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data:''
  })
}

export function queryComOptionRel(titleOid,comboDirectoryOid,relType) {
  if(titleOid == undefined){
    titleOid = '';
  }
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/optionRel/queryComboOptionRelList?titleOid='+titleOid+'&comboDirectoryOid='+comboDirectoryOid+'&relType='+relType,
    method: 'get'
  })
}
