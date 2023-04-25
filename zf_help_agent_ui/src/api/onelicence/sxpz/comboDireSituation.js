import request from '@/utils/request'



// 初始化情形信息
export function initSituation(oid,comboDirectoryOid) {
  if(oid == undefined){
    oid ='';
  }
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/situation/init?oid='+oid+'&comboDirectoryOid='+comboDirectoryOid,
    method: 'get'
  })
}

// 一件事情形的保存
export function saveSituation(data) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/situation/save',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}


export function chooseOptionsitution(comboDirectoryOid,titleOid,situationOid){

  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/optionSituation/saveComboOptionSituRel?comboDirectoryOid='+comboDirectoryOid+'&situationOid='+situationOid+'&titleOid='+titleOid,
    method: 'get',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data:''
  })
}

//删除情形管理
export function delSituation(oid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/situation/delete/' + oid+'?oid='+oid,
    method: 'delete'
  })
}



/*// 导出区划信息
export function listExp(query) {
  const  baseURL = process.env.VUE_APP_BASE_API;
  window.location.href = baseURL + '/platform/security/district/listExp?name='+encodeURI(query.name) +
    '&code='+query.code +'&parentOid='+query.parentOid+'&pageNum='+query.pageNum + '&pageSize='+query.pageSize;
}*/
// 获取选项标题信息
export function queryComboSituation(comboDirectoryOid,situationType) {
  if(comboDirectoryOid == undefined){
    comboDirectoryOid = '';
  }
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/situation/queryComboSituationList?comboDirectoryOid='+comboDirectoryOid+'&situationType='+situationType,
    method: 'get'
  })
}



