import request from '@/utils/request'

/*
 * @Description:根据一件事主题OID获取配置关联关系列表
 * @Author: wangxl
 * @Date: 2021/4/15
 **/
export function queryComOptionRelByDirOid(comboDirectoryOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/optionRel/queryComOptionRelByDirOid?comboDirectoryOid='+comboDirectoryOid,
    method: 'get'
  })
}

/*
 * @Description:初始化配置页-获取选项关联配置列表
 * @Author: wangxl
 * @Date: 2021/4/15
 **/
export function queryOptionTitelListByDireOidAndRelOid(comboDirectoryOid, relOid) {
  if(comboDirectoryOid == undefined){
    comboDirectoryOid = '';
  }
  if(relOid == undefined){
    relOid = '';
  }

  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/OptionTitle/queryOptionTitelListByDireOidAndRelOid?comboDirectoryOid='+comboDirectoryOid+'&relOid='+relOid,
    method: 'get'
  })
}

/*
 * @Description:保存选项关联配置
 * @Author: wangxl
 * @Date: 2021/4/15
 **/
export function saveOptionRel(comboDirectoryOid,titleOid,valOid,relOid){
  if(relOid == undefined){
    relOid = '';
  }
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/optionRel/saveComboOptionRel?comboDirectoryOid='+comboDirectoryOid+'&valOid='+valOid+'&titleOid='+titleOid+'&relOid='+relOid,
    method: 'get',
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
//删除选项关系
export function delOptionRel(id) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/optionRel/delOptionRel/' + id,
    method: 'delete'
  })
}

// export function queryComOptiontitle(comboDirectoryOid, relOid,optionType) {
//   if(comboDirectoryOid == undefined){
//     comboDirectoryOid = '';
//   }
//   if(relOid == undefined){
//     relOid = '';
//   }
//
//   return request({
//     url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/OptionTitle/queryComboOptionTitleListByCombriOid?comboDirectoryOid='+comboDirectoryOid+'&relOid='+relOid+'&optionType='+optionType,
//     method: 'get'
//   })
// }

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

// 情形关系配置列表
export function situationOptionRelPage(query) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/optionRel/page',
    method: 'get',
    params: query
  })
}

// 情形关系配置列表
export function initComboOptionRel(comboDirectoryOid, relOid) {
  if(comboDirectoryOid == undefined){
    comboDirectoryOid = '';
  }
  if(relOid == undefined){
    relOid = '';
  }
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/optionRel/initComboOptionRel?comboDirectoryOid='+comboDirectoryOid+'&relOid='+relOid,
    method: 'get'
  })
}

// 情形关系配置列表
export function querySituationPictureList(comboDirectoryOid) {
  if(comboDirectoryOid == undefined){
    comboDirectoryOid = '';
  }
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/optionRel/querySituationPictureList?comboDirectoryOid='+comboDirectoryOid,
    method: 'get'
  })
}
