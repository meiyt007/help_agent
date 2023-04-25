/**
 * 业态管理
 * @author: wangwg
 * @date: 2020-02-01
 */
import request from '@/utils/request';

// 查询业态信息列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directory/realPage',
    method: 'get',
    params: query
  })
}

// 初始化一件事主题信息
export function init(oid) {
  if(oid == undefined){
    oid ='';
  }
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directory/init?oid='+oid,
    method: 'get'
  })
}

// 保存业态信息
export function saveIndustry(data) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directory/saveIndustryDirectory',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=utf-8",
    },
    data: data
  })
}

// 保存业态信息
export function queryChekedList(industryType) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directory/queryChekedDirectoryList?industryType='+industryType,
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

//区划机构事项树
export function queryServiceTree() {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/directory/queryServiceTree',
    method: 'post',
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

// 查询附件信息列表
export function pageFile(query) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/atta/page',
    method: 'get',
    params: query
  })
}

// 附件上传
export function uploadFile() {
  const baseURL = process.env.VUE_APP_BASE_API;
  return baseURL + process.env.VUE_APP_THING_ROUTE_PATH + "/combo/atta/uploadFile";
}

//获取上级区划主键
export function getParentOidByDistrictOid(districtOid, chooseType) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH +'/common/getParentOidByDistrictOid?districtOid='+districtOid+"&chooseType="+chooseType,
    method: 'get'
  })
}

export function downloadFile(attaOid) {
  const baseURL = process.env.VUE_APP_ZC_ROUTE_PATH;
  window.location.href = process.env.VUE_APP_BASE_API + baseURL+'/combo/atta/downloadFile/'+attaOid;
}


