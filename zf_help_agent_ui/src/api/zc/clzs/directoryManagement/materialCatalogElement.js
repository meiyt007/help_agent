import request from '@/utils/request';

//查询材料目录元素列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/materialCatalogElement/queryMaterialCatalogElementWithPage',
    method: 'get',
    params: query
  })
}





// 获取一条材料目录和目录子项详情
export function getOne(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/materialCatalogElement/getone',
    params: {
      id: id
    },
    method: 'get',
  })
}


// 材料目录和目录子项新增或者修改
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/materialCatalogElement/saveOrUpdate',
    method: 'post',
    data: data,
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    }
  })
}


// 删除材料目录和目录子项信息
export function del(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/materialCatalogElement/delete',
    params: {
      id: id
    },
    method: 'get'
  })
}

//materialCatalogElement
export function checkIsRelation(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/materialCatalogElement/checkIsRelation?materialCatalogElementOid=' + id,
    method: 'get'
  })
}

//检查子级是否重复
export function checkMaterialCatalogChlidRepeat(materialParentOid,materialCatalogElementOid,  elementName) {
  if(materialCatalogElementOid =="" || materialCatalogElementOid ==undefined){
    materialCatalogElementOid="";
  }
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/materialCatalogElement/checkMaterialCatalogChlidRepeat?materialCatalogElementOid=' + materialCatalogElementOid+'&elementName='+elementName+'&materialParentOid='+materialParentOid,
    method: 'get'
  })
}

export function queryFaModelRuleValidationList(id) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/preTrial/queryFaModelRuleValidationList?templateMetadataOid=' + id,
    method: 'get'
  })
}

export function queryComboFaModelRuleValidationList(id) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboPreTrial/queryComboFaModelRuleValidationList?templateMetadataOid=' + id,
    method: 'get'
  })
}


//根据卡证目录查询卡证目录元素列表信息
export function getCardCatalogueElementList(cardCatalogueOid) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/cardCatalogueElement/getCardCatalogueElementList?cardCatalogueOid='+cardCatalogueOid,
    method: 'get'
  })
}
