import request from '@/utils/request';

// 获取满意度
export function getSatisfactionList () {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/virtualBusinessRecord/satisfactionList',
    method: 'get'
  })
}

//查询列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/manualEvaluation/manualEvaluationList',
    method: 'post',
    params: query
  })
}

//查询手动评价信息
export function getManualEvaluation(virtualBusinessNum) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/manualEvaluation/getManualEvaluation?virtualBusinessNum='+ virtualBusinessNum,
    method: 'get'
  })
}

//修改手动评价信息
export function updateManualEvaluation(query) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/manualEvaluation/updateManualEvaluation',
    method: 'post',
    params: query
  })
}

//保存手动评价信息
export function saveManualEvaluation(query) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/manualEvaluation/saveManualEvaluation',
    method: 'post',
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
    data: query
  })
}

//修改手动评价信息
export function pushManualEvaluation(virtualBusinessNum) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/manualEvaluation/pushManualEvaluation/' + virtualBusinessNum,
    method: 'post'
  })
}

//查询列表
export function getEmotionList(query) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/interactionService/getEmotionList',
    method: 'post',
    params: query
  })
}

//查询手动评价信息
export function getEmotionRecognitionRecordById(id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/interactionService/getEmotionRecognitionRecordById?id='+ id,
    method: 'get'
  })
}

// 获取满意度
export function getVirtualBusinessRecordByOid (virtualBusinessNum) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/virtualBusinessRecord/getVirtualBusinessRecordByOid?virtualBusinessNum='+ virtualBusinessNum,
    method: 'get'
  })
}

//查询情绪折线数据集
export function getEmotionListById(query) {
  return request({
    //url: process.env.VUE_APP_ZC_ROUTE_PATH + '/interactionService/getEmotionListById?virtualBusinessOid='+ virtualBusinessOid,
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/interactionService/getEmotionListById',
    method: 'get',
    params: query
  })
}

// 办件信息查询
export function getOneCase (caseNumber) {
  return request({
    url: process.env.VUE_APP_BJFW_ROUTE_PATH + '/qlCaseService/queryQlCaseByCaseNumber?caseNumber=' + caseNumber,
    method: 'get'
  })
}
// 办件信息查询
export function getThingCase (caseNumber) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/combo/case/getComboCaseByCaseNumber?caseNumber=' + caseNumber,
    method: 'get'
  })
}



