import request from '@/utils/request';
import { WPS } from '@/utils/config';
import store from '@/store';
const URL_PRE = () => store.state.config.deviceMap?.[WPS];

//查询平板信息列表
export function page (query) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/manage/sxDocuTemplate/page',
    method: 'get',
    params: query
  })
}



// 获取一条平板信息详情
export function getOne (id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/manage/sxDocuTemplate/getOne/' + id + '?id=' + id,
    method: 'get',
  })
}


// 确认平板信息信息
export function del (id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/manage/sxDocuTemplate/delete/' + id,
    params: {
      id: id
    },
    method: 'post'
  })

}
// 启禁用平板信息信息
export function able (id) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/manage/sxDocuTemplate/able/' + id,
    params: {
      id: id
    },
    method: 'post'
  })
}
// 平板信息新增或者修改
export function save (data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/manage/sxDocuTemplate/save',
    method: 'post',
    headers: {
      "Content-Type": "application/json;charset=UTF-8",
    },
    data: data
  })
}

export function isOSWin () {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/common/isOSWin',
    method: 'get',
  })
}

export function uploadDocuTemplate (oid) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/yanshi/uploadDocuTemplate?oid=' + oid,
    method: 'get',
  })
}

export function downloadPrintFile (filePath, attaOid) {
  return request({
    url: URL_PRE() + '/Download?url=' + filePath + '&attaOid=' + attaOid,
    method: 'get',
  })
}
