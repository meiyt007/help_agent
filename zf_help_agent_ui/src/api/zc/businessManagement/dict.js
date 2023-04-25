import request from '@/utils/request';

//事项查询接口
export function querDictApi(params) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxService/getSxServiceListBySxServiceName',
    method: 'get',
    params
  })
}

//获取所有事项接口
export function getDictApi() {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxService/getSxServiceListBySxServiceName',
    method: 'get'
  })
}

//获取事项细化材料接口
export function getDictListApi(params) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/serviceMaterial/getRefinedMaterialByServiceId',
    method: 'get',
    params
  })
}

//上传图片接口
export function uploadImage(data) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/pic/uploadImage',
    method: 'post',
    headers:{
      "Content-Type":"multipart/form-data",
    },
    data
  })
}
//删除上传图片
export function deleteImageItem(params) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/pic/deleteByPicRecordId',
    method: 'get',
    params
  })
}
//是否为空表设置
export function blankTableItem(params) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/pic/updateByPicRecordId',
    method: 'get',
    params
  })
}

//根据细化材料id查询此细化材料所有的图片
export function allMaterialImage(params) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/pic/queryTDataSetMaterialImgRecsByRefinedMaterialId',
    method: 'get',
    params
  })
}

//压缩图片接口
export function commpressPicToZip(data) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/pic/commpressPicToZip',
    method: 'post',
    data
  })
}
//查询打包列表数据
export function queryTDataSetZipRecS(data) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/pic/queryTDataSetZipRecS',
    method: 'post',
    data
  })
}
//下载压缩文件
export function downloadZip(params) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/pic/download',
    method: 'get',
    params
  })
}

//删除列表
export function deleteZipList(params) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/pic/deleteTDataSetZipRecById',
    method: 'get',
    params
  })
}
//发布操作
export function updateIsPublish(params) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/pic/updateIsPublish',
    method: 'get',
    params
  })
}