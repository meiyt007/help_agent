import request from '@/utils/request'

// 查询一件事主题信息列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directory/industryPage',
    method: 'get',
    params: query
  })
}

// 获取一件事主题树信息
export function queryComboThemeSimpleTree(themeOid) {
  if(themeOid == undefined){
    themeOid = '';
  }
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH +'/common/queryComboThemeSimpleTree?themeOid='+themeOid,
    method: 'get'
  })
}

// 查询一件事服务对象
export function getComboServiceObject() {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directory/getComboServiceObject',
    method: 'get'
  })
}

// 初始化选项标题信息
export function init(oid,comboDirectoryOid) {
  if(oid == undefined){
    oid ='';
  }
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/OptionTitle/init?oid='+oid+'&comboDirectoryOid='+comboDirectoryOid,
    method: 'get'
  })
}
//配置选项
export function handleOption(oid) {
  if(oid == undefined){
    oid ='';
  }
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directory/init?oid='+oid,
    method: 'get'
  })
}

// 一件事选项的保存
export function save(data) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/OptionTitle/save',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

//删除一件事选项管理
export function del(oid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/OptionTitle/delete/' + oid+'?oid='+oid,
    method: 'delete'
  })
}

//删除一件事选项值
export function delVal(oid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/OptionVal/delete/' + oid+'?oid='+oid,
    method: 'delete'
  })
}

//删除选项关系
export function delOptionRel(oid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/optionRel/delete/' + oid+'?oid='+oid,
    method: 'delete'
  })
}

// 获取单个一件事选项信息
export function getOne(oid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directory/getOne/'+oid,
    method: 'get'
  })
}

/*// 导出区划信息
export function listExp(query) {
  const  baseURL = process.env.VUE_APP_BASE_API;
  window.location.href = baseURL + '/platform/security/district/listExp?name='+encodeURI(query.name) +
    '&code='+query.code +'&parentOid='+query.parentOid+'&pageNum='+query.pageNum + '&pageSize='+query.pageSize;
}*/
// 获取选项标题信息
export function queryCombo(comboDirectoryOid, situationOid,optionType) {
  if(comboDirectoryOid == undefined){
    comboDirectoryOid = '';
  }
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/OptionTitle/queryComboOptionTitleList?comboDirectoryOid='+comboDirectoryOid+'&situationOid='+situationOid+'&optionType='+optionType,
    method: 'get'
  })
}

export function queryComOptiontitle(comboDirectoryOid, relOid,optionType) {
  if(comboDirectoryOid == undefined){
    comboDirectoryOid = '';
  }
  if(relOid == undefined){
    relOid = '';
  }

  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/OptionTitle/queryComboOptionTitleListByCombriOid?comboDirectoryOid='+comboDirectoryOid+'&relOid='+relOid+'&optionType='+optionType,
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
export function handleInitVal(oid,titleOid) {
  if(oid == undefined){
    oid ='';
  }
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/OptionVal/init?oid='+oid+'&titleOid='+titleOid,
    method: 'get'
  })
}


// 一件事选项值的保存
export function saveVal(data,comboDireOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/OptionVal/save?comboDireOid='+comboDireOid,
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
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
