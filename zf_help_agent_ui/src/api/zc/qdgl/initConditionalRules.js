import request from '@/utils/request';

/** 内部接口的数据 */
export function queryAllInterApi() {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/interApi/queryAllInterApi',
    method: 'post'
  })
}

/** 通过id获取内部接口的返回值 列表 */
export function queryInterApiValById(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/interApi/queryInterApiValById?id=' + id,
    method: 'post'
  })
}

/** 预检规则返回值的主键获取实体 */
export function getInterApiReo(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/interApi/getInterApiReponse?id=' + id,
    method: 'post'
  })
}

/** 获取该事项下保存的条件预检配置的数据 */
export function queryAllConditionalRulesList(data) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/rules/queryConditionalRulesList',
    method: 'post',
    headers: {
      "Content-Type": "application/json;charset=UTF-8",
    },
    data: data
  })
}

/** 保存条件预检 */
export function saveOrUpdate (data) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/rules/saveOrUpdateSxConditionalRules',
    method: 'post',
    headers: {
      "Content-Type": "application/json;charset=UTF-8",
    },
    data: data
  })
}

/** 删除条件配置信息 */
export function delConditionalRulesById (id) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/rules/delete?id=' + id,
    method: 'get',
    params: id
  })
}




