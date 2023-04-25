import request from '@/utils/request';

//查询卡证目录列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/electronicLicense/queryInfoWithPage',
    method: 'post',
    data: query
  })
}

// 获取一条卡证详情
export function getOne(oid) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/electronicLicense/getInfoByOid',
    params: {
      oid: oid
    },
    method: 'get',
  })
}

// 材料目录和目录子项新增或者修改
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/electronicLicense/saveOrUpdate',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

// 删除材料目录和目录子项信息
export function del(oid) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/electronicLicense/deleteByOid',
    params: {
      oid: oid
    },
    method: 'get'
  })
}

//添加修改时判断材料类别是否重复
export function checkHasRepeat(oid, code) {
  if(oid =="" || oid ==undefined){
    oid="";
  }
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/electronicLicense/checkHasRepeat?oid=' +oid+ '&code='+code,
    method: 'get'
  })
}



