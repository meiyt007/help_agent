import request from '@/utils/request';

//查询材料类型列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/basicFormField/queryInfoWithPage',
    method: 'post',
    params: query
  })
}


// 获取一条材料类型详情
export function getOne(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/basicFormField/getOne',
    params: {
      id: id
    },
    method: 'post',
  })
}
// 材料类型新增或者修改
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/basicFormField/saveOrUpdate',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}


// 删除材料类型信息
export function del(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/basicFormField/delete',
    params: {
      id: id
    },
    method: 'post'
  })
}




//验证是否存在单办规则关联数据
export function getFaModelRuleValidationList(query) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/faModelRuleValidation/getFaModelRuleValidationList',
    method: 'post',
    params: query
  })
}



//验证是否存在一件事关联规则数据
export function getComboFaModelRuleValidationListByYZ(query) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboFaModelRuleValidation/getComboFaModelRuleValidationListByYZ',
    method: 'post',
    params: query
  })
}

//判断被填充的分类字段是否已添加
export function checkHasRepeat(fieldType, fieldKey, oid) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/basicFormField/checkHasRepeat?fieldType='+fieldType+ '&fieldKey='+fieldKey+ '&oid='+oid,
    method: 'get'
  })
}

//检查字段是否被关联，不能被删除
export function checkIsHasRel(query) {
  var fieldType = query.fieldType;
  var url = '';
  if (fieldType === '单办') {  //单办
    url = process.env.VUE_APP_DSXBL_ROUTE_PATH + '/formFieldRelConfig/checkIsHasRel';
  } else {   //一件事
    url = process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboFieldRelConfig/checkIsHasRel';
  }
  return request({
    url: url,
    method: 'post',
    params: query
  })
}


