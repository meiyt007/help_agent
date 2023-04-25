import request from '@/utils/request';

//查询列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxService/sxServicePage',
    method: 'post',
    params: query
  })
}
// 获取事项材料列表
export function pageMaterial(id) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/serviceMaterial/getSxServiceMaterialListByServiceOid/'+id+'?serviceOid='+id,
    method: 'get',

  })
}

// 初始化材料与证照目录配置页面
export function handleInitMater(id,serviceOid) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH +'/combo/SxMaterialElmsConfig/init?materialOid='+id+'&serviceOid='+serviceOid,
    method: 'get'
  })
}
// 初始化结果与证照目录配置页面
export function handleInitResultMater(id) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/sxResultElmsConfig/init?resultOid='+id,
    method: 'get'
  })
}
// 保存或者修改
export function saveOrUpdateResult(data) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/sxResultElmsConfig/save',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

//获取证照目录详情
export function getSxMaterialBillOne(id) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/combo/sxMaterialBill/getOne/'+id+'?oid='+id,
    method: 'get'
  })
}

//获取事项详情
export function getSxServiceOne(id) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxService/viewSxService?serviceOid='+id,
    method: 'get'
  })
}

// 保存或者修改
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/combo/SxMaterialElmsConfig/save',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

export function queryMaterBill() {

  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH +'/combo/sxMaterialBill/querySxMaterialBill',
    method: 'get'
  })
}

