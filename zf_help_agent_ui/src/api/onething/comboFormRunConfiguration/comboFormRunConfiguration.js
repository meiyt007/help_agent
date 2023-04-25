import request from '@/utils/request';

//查询列表
export function sxServicePage(query) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxService/sxServicePage',
    method: 'post',
    params: query
  })
}

//获取事项详情
export function getSxServiceOne(id) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxService/viewSxService?serviceOid='+id,
    method: 'get'
  })
}

// 事项表单关联保存或者修改
export function save(data) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/onethingFormRunConfiguration/save',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

//查询列表 - serviceOid
export function getSerFormList(serviceOid,pageNumber,pageSize) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/onethingFormRunConfiguration/getComboSerFormsByServiceOid/'+ serviceOid+'/'+pageNumber+'/'+ pageSize,
    method: 'get',
  })
}

//查询列表 - comboDirectoryOid
export function getComboFormList(comboDirectoryOid,pageNumber,pageSize) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/onethingFormRunConfiguration/getComboSerFormsByComboDirectoryOid/'+ comboDirectoryOid+'/'+pageNumber+'/'+ pageSize,
    method: 'get',

  })
}

// 获取事项表单详情
export function getSxSerFormByOid(sxSerFormOid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/onethingFormRunConfiguration/getComboSxSerFormByOid?comboSxSerFormOid=' + sxSerFormOid,
    method: 'get',
  })
}

// 启禁用表单信息
export function sxSerFormAble(oid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/onethingFormRunConfiguration/comboSxSerFormAble',
    params: {
      oid: oid
    },
    method: 'post'
  })
}

//获取一件事下所有和一件事关联的表单
export function getSerFormsOfCombo(comboDirectoryOid,queryIneffectiveFlag,pageNumber,pageSize) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/onethingFormRunConfiguration/getSerFormsOfCombo/'+ comboDirectoryOid+'/'+pageNumber+'/'+ pageSize+'/'+ queryIneffectiveFlag,
    method: 'get',
  })
}

//批量更新事项表单，
export function saveSxSerFormByList(data) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/onethingFormRunConfiguration/saveComboSxSerFormByList',
    data: data,
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    method: 'post'
  })
}

//单条更新
export function updateSxSerForm(oid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/onethingFormRunConfiguration/updateComboSxSerForm',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    params: {
      oid: oid
    },
  })
}



// 删除表单信息
export function comboSxSerFormDel(oid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/onethingFormRunConfiguration/comboSxSerFormDel',
    params: {
      oid: oid
    },
    method: 'post'
  })
}
