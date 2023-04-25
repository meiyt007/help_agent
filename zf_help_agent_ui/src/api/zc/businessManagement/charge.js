import request from '@/utils/request';
import { PUSH } from '@/utils/config';
/** 高拍仪前缀地址 */
import store from '@/store';
const URL_PRE = () => store.state.config.deviceMap?.[PUSH];
export function openScanningGun () {
  return request({
    url: URL_PRE() + '/spru/Open',
    method: 'get',
  })
}

export function closeScanningGun () {
  return request({
    url: URL_PRE() + '/spru/Close',
    method: 'get',
  })
}

export function activeScanningGun () {
  return request({
    url: URL_PRE() + '/spru/Active',
    method: 'get',
  })
}

export function cancleScanningGun () {
  return request({
    url: URL_PRE() + '/spru/Cancle',
    method: 'get',
  })
}

export function pushActiveMessage (data) {
  return request({
    url: process.env.VUE_CASE_API + '/chargeService/pushActiveScanningMessage?resEntity=' + data,
    method: 'get',
  })
}

export function getActiveMessage () {
  return request({
    url: process.env.VUE_CASE_API + '/chargeService/getActiveScanningMessage',
    method: 'get',
  })
}


