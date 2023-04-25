/**
 * 超级综窗柜台api
 * @author: wangwg
 * @date: 2021-06-15
 */
import request from "@/utils/request";
import axios from 'axios';
import { PUSH } from '@/utils/config';
import store from '@/store';
const URL_PRE = () => store.state.config.deviceMap?.[PUSH]

// const pbpjUrl = `http://172.168.253.96:9094`;
// const pbpjUrl = `http://172.168.253.49:9094`;
 const pbpjUrl = `http://172.168.252.71:9094`;
const req = axios.create({
  // axios中请求配置有baseURL选项，表示请求URL公共部分
  baseURL: pbpjUrl,
  // 超时
  timeout: 1000
});

// 获取评价设备服务系统
export function getPjServiceSystem () {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/interactionService/getPjServiceSystem',
    method: 'get'
  })
}

export function showCallMessage (data) {
  return req.post('/Brower/GetData?url='+data);
}

export function getCallBackInfo (params) {
  return request.get(process.env.VUE_APP_DSXBL_ROUTE_PATH + '/smallScreen/getCallBackInfo', { params })
}

//根据虚拟ID查询评价星级
export function getStarAndScore (virtualBusinessOid) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/starService/getStarAndScore?virtualBusinessOid=' + virtualBusinessOid,
    method: 'get'
  })
}

//根据虚拟ID查询评价星级
export function getPushStar () {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/starService/getPushStar',
    method: 'get'
  })
}


//根据code获取配置信息
export function getSpeechConfigInfo (code) {
  return request({
    url: '/settings/security/config/getSysConfigByCode?code=' + code,
    method: 'get',
    isConfig: true,
  })
}

//调取语音播报信息
export function getAudioInfo(query) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH +'/guidSpeech/selectByGuidSpeechCode',
    method: 'get',
    params: query
  })
}

export function aotuAudioSpeech(speechAddress,data) {
  return request({
    url: speechAddress +'/audio/Play',
    method: 'get',
    params:  { 'PlayUrl': data }
  })
}
