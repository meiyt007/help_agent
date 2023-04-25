import request from '@/utils/request';

//查询识别模板-材料目录列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/faModelTemplate/queryFaModelMaterialCatalogWithPage',
    method: 'GET',
    params: query
  })
}

