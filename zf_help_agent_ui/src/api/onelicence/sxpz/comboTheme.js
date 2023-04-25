import request from '@/utils/request'

// 查询一件事主题信息列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/theme/page',
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
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/common/queryComboThemeSimpleTree?themeOid='+themeOid,
    method: 'get'
  })
}

// 初始化一件事主题信息
export function init(oid,parentOid) {
  if(oid == undefined){
    oid ='';
  }
  if(parentOid == undefined){
    parentOid ='';
  }
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/theme/init?oid='+oid+'&themeParentOid='+parentOid,
    method: 'get'
  })
}

// 一件事主题的新增或者修改
export function save(data) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/theme/save',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

//删除一件事主题管理
export function del(oid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/theme/delete/' + oid,
    method: 'delete'
  })
}

/*// 启禁用区划信息
export function able(oid) {
  return request({
    url: '/platform/security/district/able/'+oid,
    method: 'post'
  })
}*/


// 获取单个一件事主题信息
export function getOne(oid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/theme/getOne/'+oid,
    method: 'get'
  })
}
//根据分类OID查询是否有一件事目录引用
export  function getCountByThemeOid(oid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/theme/getComboDireCountByThemeOid/'+oid,
    method: 'get'
  })
}

//根据分类OID查询是否有一件事目录引用
export  function getCountByParentThemeOid(oid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/theme/getComboDireCountByThemeOid/'+oid,
    method: 'get'
  })
}

export  function getComboDireCountByParentThemeOid(oid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/theme/getComboDireCountByParentThemeOid/'+oid,
    method: 'get'
  })
}
/*// 导出区划信息
export function listExp(query) {
  const  baseURL = process.env.VUE_APP_BASE_API;
  window.location.href = baseURL + '/platform/security/district/listExp?name='+encodeURI(query.name) +
    '&code='+query.code +'&parentOid='+query.parentOid+'&pageNum='+query.pageNum + '&pageSize='+query.pageSize;
}*/
