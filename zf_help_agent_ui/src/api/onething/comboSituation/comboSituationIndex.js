import request from '@/utils/request'

/*
 * @Description:查询一件事情形配置分页列表
 **/
export function page(query) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directory/queryPubshDirectoryList',
    method: 'get',
    params: query
  })
}

// 查询一件事服务对象
export function getComboServiceObject() {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directory/getComboServiceObject',
    method: 'get'
  })
}


/*
 * @Description:获取一件事主题选项列表
 **/
export function queryComboOptionTitle(comboDirectoryOid) {
  if(comboDirectoryOid == undefined){
    comboDirectoryOid = '';
  }
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/OptionTitle/queryComboOptionTitleListByDireOid?comboDirectoryOid='+comboDirectoryOid,
    method: 'get'
  })
}


export function queryComOptiontitleStu(comboDirectoryOid, situationOid,optionType) {
  if(comboDirectoryOid == undefined){
    comboDirectoryOid = '';
  }
  if(situationOid == undefined){
    situationOid = '';
  }

  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/OptionTitle/queryComOptiontitleStu?comboDirectoryOid='+comboDirectoryOid+'&situationOid='+situationOid+'&optionType='+optionType,
    method: 'get'
  })
}

export function queryComOptiontitleByRel(comboDirectoryOid, relOid) {
  if(comboDirectoryOid == undefined){
    comboDirectoryOid = '';
  }
  if(relOid == undefined){
    relOid = '';
  }

  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/OptionTitle/queryComboOptionTitleListByRel?comboDirectoryOid='+comboDirectoryOid+'&relOid='+relOid,
    method: 'get'
  })
}
export function queryComOptiontitleByRelForResult(comboDirectoryOid, relOid) {
  if(comboDirectoryOid == undefined){
    comboDirectoryOid = '';
  }
  if(relOid == undefined){
    relOid = '';
  }

  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/OptionTitle/queryComboOptionTitleListByRelForResult?comboDirectoryOid='+comboDirectoryOid+'&relOid='+relOid,
    method: 'get'
  })
}
//配置选项值列表页面
export function openTitleView(oid) {
  if(oid == undefined){
    oid ='';
  }
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/OptionTitle/getOne/'+ oid+'?oid='+oid,
    method: 'get'
  })
}



// 获取选项值列表信息
export function queryOptionVal(titleOid) {
  if(titleOid == undefined){
    titleOid = '';
  }
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/OptionVal/queryComboOptionValList?titleOid='+titleOid,
    method: 'get'
  })
}

// 情形列表
export function situationPage(query) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/situation/page',
    method: 'get',
    params: query
  })
}

// 保存情形
export function saveSituation(data) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/situation/saveSituation',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

// 初始化情形
export function initSituation(oid) {
  if(oid == undefined){
    oid = '';
  }
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/situation/initSituation?oid='+oid,
    method: 'get'
  })
}

// 删除情形
export function delSituation(oid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/situation/delComboSituationById/'+oid,
    method: 'delete'
  })
}
