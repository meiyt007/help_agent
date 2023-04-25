import request from '@/utils/request'

/*
 * @Description:查询情形配置-配置选项-关联结果信息列表
 * @Author: wangxl
 * @Date: 2021/4/22
 **/
export function page(query) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/optionResult/page',
    method: 'get',
    params: query
  })
}

/*
 * @Description:获取单个情形配置-配置选项-关联结果信息
 * @Author: wangxl
 * @Date: 2021/4/22
 **/
export function getOne(oid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/optionResult/getOne/' + oid,
    method: 'get'
  })
}

/*
 * @Description:根据主题OID与选项值OID获取配置对象
 * @Author: wangxl
 * @Date: 2021/4/20
 **/
export function getOptionResultByDirOidValOid(comboDireOid,valOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/optionResult/getOptionResultByDirOidValOid?comboDirectoryOid='+comboDireOid+'&valOid='+valOid,
    method: 'get'
  })
}

/*
 * @Description:查询一件主题已确认公共结果列表
 * @Author: wangxl
 * @Date: 2021/4/21
 **/
export function queryDirectoryResultList(comboDireOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directoryResult/queryDirectoryResultByDireOidStatus?status=1&comboDireOid='+comboDireOid,
    method: 'get'
  })
}

/*
 * @Description:保存选项值与结果
 * @Author: wangxl
 * @Date: 2021/4/21
 **/
export function saveOptionResult(data){
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/optionResult/saveOptionResultInfo',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data:data
  })
}

//事项结果配置
export function optionResultVoPage(query) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/optionResult/optionResultPage',
    method: 'get',
    params: query
  })
}

//初始化事项结果配置
export function initResultRel(comboDireOid, relOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/optionResult/initOptionResult?comboDirectoryOid='+comboDireOid+'&relOid='+relOid,
    method: 'get'
  })
}

//删除选项关系
export function delResultRel(oid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/optionResult/delResultRel/' + oid,
    method: 'delete'
  })
}
//查询一件事下事项列表
export function queryAllServiceList(comboDireOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/service/queryAllServiceByComboDirectoryOid?comboDirectoryOid='+comboDireOid,
    method: 'get'
  })
}
