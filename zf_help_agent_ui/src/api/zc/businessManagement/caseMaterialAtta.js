import request from '@/utils/request'
import axios from "axios";
axios.defaults.headers['Content-Type'] = 'multipart/form-data'

// 获取上传附件数据记录
export function getCaseMaterialAttaList(oid) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/windowAcceptance/getCaseMaterialAttaList?oid='+oid,
    method: 'get'
  })
}

// 附件上传
export function uploadCaseMaterialFile(data) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/qlcaseAtta/uploadCaseMaterialFile',
    method: 'POST',
    headers:{
      "Content-Type":"multipart/form-data",
    },
    data:data
  })
}

// 保存上传附件记录
export function saveUploadFileRecord(data) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/sysAttaService/saveSysAtta',
    method: 'POST',
    headers:{
      "Content-Type":"application/json;charset=utf-8",
    },
    data: data
  })
}

// 保存材料附件信息
export function saveQlCaseMaterialAtta(data) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/qlCaseMaterialAttaService/saveQlCaseMaterialAtta',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=utf-8",
    },
    data: data
  })
}

// 通过材料获取上传附件数据记录
export function getAttaListByMaterialOid(materialOid,caseOid) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/sysAttaService/querySysAttaListByMaterialOid?materialOid='+materialOid+"&caseOid="+caseOid,
    method: 'get'
  })
}

// 通过办件获取上传附件数据记录
export function getSxServiceMaterialAttaList(caseOid) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/windowAcceptance/queryMaterialAttaListByCaseOid?caseOid='+caseOid,
    method: 'get'
  })
}



// 预览上传附件
export function previewCaseAtta(attaOid) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/windowAcceptance/querySysAttaByOid?attaOid='+attaOid,
    method: 'get'
  })
}

