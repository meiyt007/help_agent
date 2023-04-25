import request from '@/utils/request';

//查询材料目录列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/materialCatalog/queryMaterialCatalogWithPage',
    method: 'get',
    params: query
  })
}

//查询材料分类列表
export function getMaterialCategoryList() {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/materialCategory/queryMaterialCategoryList',
    method: 'post'
  })
}



// 获取一条材料目录和目录子项详情
export function getOne(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/materialCatalog/getMaterialCatalogAndSubitem',
    params: {
      id: id
    },
    method: 'get',
  })
}

//初始化数据
export function initAll() {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/materialCatalog/initAll',
    method: 'post'
  })
}
// 材料目录和目录子项新增或者修改
export function saveOrUpdate(data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/materialCatalog/saveOrUpdateMaterialCatalogAndSubitem',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}


// 删除材料目录和目录子项信息
export function del(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/materialCatalog/del',
    params: {
      id: id
    },
    method: 'post'
  })
}

//判断是否关联事项
export function checkIsReationSx(id) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/serviceMaterial/getSxServiceMaterialListByMaterialCatalogOid?materialCatalogOid=' + id,
    method: 'get'
  })
}

//判断是否关联一件事事项
export function checkIsReationYjssx(id) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/directoryMaterial/queryComboDirectoryMaterialBymaterialCatalogOid/' + id,
    method: 'get'
  })
}
//检查父级是否重复
export function checkMaterialCatalogRepeat(materialCatalogOid,  catalogName) {
  if(materialCatalogOid =="" || materialCatalogOid ==undefined){
    materialCatalogOid="";
  }
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/materialCatalog/checkMaterialCatalogRepeat?materialCatalogOid=' + materialCatalogOid+'&catalogName='+catalogName,
    method: 'get'
  })
}



export function getAttaBase64ByUrl() {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/preTrial/getAttaBase64ByUrl',
    method: 'get'
  })
}

//获取卡证目录列表
export function getAllCardCatalogueList() {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/cardCatalogue/getAllCardCatalogueList',
    method: 'get'
  })
}
