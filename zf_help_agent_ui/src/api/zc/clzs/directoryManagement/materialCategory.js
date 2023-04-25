import request from '@/utils/request';

//查询材料类型列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/materialCategory/queryMaterialCategoryWithPage',
    method: 'post',
    params: query
  })
}


// 获取一条材料类型详情
export function getOne(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/materialCategory/getOne',
    params: {
      id: id
    },
    method: 'post',
  })
}
// 材料类型新增或者修改
export function saveOrUpdate(data) {
 /* alert(JSON.stringify(data))*/
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/materialCategory/saveOrUpdate',
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
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/materialCategory/delete',
    params: {
      id: id
    },
    method: 'post'
  })
}


export function checkisExist(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/materialCategory/checkIsExist?materialCategoryOid=' + id,
    method: 'get'
  })
}
//添加修改时判断材料类别是否重复
export function checkHasRepeat(materialCategoryOid,  categoryName,  categoryCode) {
  if(materialCategoryOid =="" || materialCategoryOid ==undefined){
    materialCategoryOid="";
  }
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/materialCategory/checkHasRepeat?materialCategoryOid=' + materialCategoryOid+'&categoryName='+categoryName+'&categoryCode='+categoryCode,
    method: 'get'
  })
}

