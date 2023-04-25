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
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/optionResult/delete/' + oid+'?oid='+oid,
    method: 'delete'
  })
}

export function chooseOptionResult(comboDirectoryOid,resultOid,valOid,relOid){
  if(relOid == undefined){
    relOid = '';
  }
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/optionResult/saveOptionResult?comboDirectoryOid='+comboDirectoryOid+'&valOid='+valOid+'&resultOid='+resultOid+'&relOid='+relOid,
    method: 'get',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data:''
  })
}

export function queryOptionResult(comboDirectoryOid) {

  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/optionResult/queryOptionResultList?comboDirectoryOid='+comboDirectoryOid,
    method: 'get'
  })
}
