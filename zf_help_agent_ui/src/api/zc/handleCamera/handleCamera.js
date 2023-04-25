import request from '@/utils/request';
import { PUSH } from '@/utils/config';
import store from '@/store';
const URL_PRE = () => store.state.config.deviceMap?.[PUSH];
export function getHandleCamera () {
  return request({
    url: URL_PRE() + '/camera/Open',
    method: 'post'
  })
}

export function getCameraImage () {
  return request({
    url: URL_PRE() + '/camera/GetImage',
    method: 'post'
  })
}

export function stopCameraImage () {
  return request({
    url: URL_PRE() + '/camera/Close',
    method: 'post'
  })
}

export function openAudio () {
  return request({
    url: URL_PRE() + '/audio/Start',
    method: 'get'
  })
}

export function closeAudio () {
  return request({
    url: URL_PRE() + '/audio/Stop',
    method: 'get'
  })
}

export function uploadAudio (virtualBusinessNum) {
  return request({
    url: URL_PRE() + '/audio/Upload?number=' + virtualBusinessNum,
    method: 'get'
  })
}

/**
 *  调用情绪评价接口
 *
 * @returns
 */
export function getEmotionJudgment (data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/interactionService/getEmotionJudgment ',
    method: 'post',
    data: data
  })
}

//新增虚拟业务
export function saveVirtualBusinessRecord (data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/virtualBusinessRecord/saveVirtualBusinessRecord',
    method: 'post',
    data: data
  })
}

export function getTotalWaitNum (data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/queueCall/getTotalWaitNum ',
    method: 'get',
    data: data
  })
}


export function getQhjhWaitNum (data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/queueCall/getQhjhWaitNum',
    method: 'post',
    data: data
  })
}

export function sendQhJhInfo (data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/queueCall/sendQhJhInfo',
    method: 'post',
    data: data
  })
}

//保存叫号记录接口
export function SaveCallRecordInterface (data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/callRecordService/saveCallRecord',
    method: 'post',
    data: data
  })
}

//完成推送好差评
export function getEvaluateAndPush(virtualBusinessNum) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/manualEvaluation/getEvaluateAndPush?virtualBusinessNum='+ virtualBusinessNum,
    method: 'get'
  })
}
