import request from '@/utils/request';
import {saveOrUpdate} from "@/api/zc/clzs/directoryManagement/materialCatalog";

//查询列表
export function presonalpage(query) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/afterAdvisory/pageList',
    method: 'post',
    params: query
  })
}

//设置设置当前登录人状态
export function setReplyState(advStatus) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/afterAdvisory/setReplyState',
    params: {
      advStatus: advStatus
    },
    method: 'post',
  })
}

export function saveAfterAdvisory(data) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/afterAdvisory/saveOrUpdate',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data,
  })
}

export function getFreeUserOrgan(organOid) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/afterAdvisory/getFreeUserTreeByOrgan?organOid='+organOid,
    method: 'get'

  })
}




