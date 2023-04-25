/*
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-07-21 09:35:19
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-09-30 16:20:28
 * @FilePath: \hpNewHall\src\api\modules\helpAgent.js
 * 帮代办人员接口
 */
import request from "@/utils/request";
import { toDataURL } from "qrcode";
const baseUrl = process.env.VUE_APP_CASE_API;

//扫码帮代办接口
export function helpInfo(params) {
  return request({
    url: baseUrl + "/ha/scan/helpInfo",
    method: "post",
    params: params,
  });
}

//取号并分配帮办人员
export function takeNumHelpInfo(params) {
  return request({
    url: baseUrl + "/ha/takeNum/helpInfo",
    method: "post",
    params: params,
  });
}

//保存办事人基本信息
export function saveNameAndCard(params) {
  return request({
    url: baseUrl + "/ha/df/saveNameAndCard",
    method: "post",
    params: params,
  });
}

//是否有新服务等待处理
export function isHaveNewService() {
  return request({
    url: baseUrl + "/ha/workUser/isHaveNewService",
    method: "get",
  });
}

//获取等待人数
export function getQueueNum() {
  return request({
    url: baseUrl + "/ha/workUser/getQueueNum",
    method: "get",
  });
}

//获取等待人数（当日大厅所有人数）
export function getQueueAllNum() {
  return request({
    url: baseUrl + "/ha/workUser/getQueueAllNum",
    method: "get",
  });
}

//查询办事队列
export function queryWorkQueueList(data) {
  return request({
    url: baseUrl + "/ha/workUser/queryWorkQueueList",
    method: "get",
    params: data,
  });
}

//查看所有用户的服务信息
export function getAllUserService(data) {
  return request({
    url: baseUrl + "/ha/workUser/getAllUserService",
    method: "get",
    params: data,
  });
}

//查看所有用户的服务信息
export function getAllUserServiceByGroupSplit(data) {
  return request({
    url: baseUrl + "/ha/workUser/getAllUserServiceByGroupSplit",
    method: "get",
    params: data,
  });
}

//获取帮代办人员列表
export function getWorkUserList(data) {
  return request({
    url: baseUrl + "/ha/df/getWorkUserList",
    method: "post",
    params: data,
  });
}

//查看用户组的服务信息
export function getUserGroupService() {
  return request({
    url: baseUrl + "/ha/workUser/getUserGroupService",
    method: "get",
  });
}
//查询用户组的办事队列
export function queryWorkQueueGroup(data) {
  return request({
    url: baseUrl + "/ha/workUser/queryWorkQueueGroup",
    method: "post",
    params: data,
  });
}

//查询文件知识库分页信息列表
export function queryMaterialList(data) {
  return request({
    url: baseUrl + "/ha/knowledge/queryMaterialList",
    method: "get",
    params: data,
  });
}

//获取转派服务记录
export function getTurnService(data) {
  return request({
    url: baseUrl + "/ha/workUser/getTurnService",
    method: "get",
    params: data,
  });
}
//服务转派

export function serviceTurn(data) {
  return request({
    url: baseUrl + "/ha/workUser/serviceTurn",
    method: "post",
    params: data,
  });
}

//转派服务是否接收
export function turnIsVerify(data) {
  return request({
    url: baseUrl + "/ha/workUser/turnIsVerify",
    method: "post",
    params: data,
  });
}

//根据转派编号获取办事队列信息
export function getMessesByTurn(data) {
  return request({
    url: baseUrl + "/ha/workUser/getMessesByTurn",
    method: "get",
    params: data,
  });
}

//获取转派服务待审核列表
export function getServiceTurnByNoVerify(data) {
  return request({
    url: baseUrl + "/ha/workUser/getServiceTurnByNoVerify",
    method: "get",
    params: data,
  });
}
//服务接收
export function serviceAccept(data) {
  return request({
    url: baseUrl + "/ha/workUser/serviceAccept",
    method: "post",
    params: data,
  });
}

//选择办事人进行接待服务
export function workUserService(data) {
  return request({
    url: baseUrl + "/ha/workUser/service",
    method: "get",
    params: data,
  });
}

//接待下一位
export function nextService(data) {
  return request({
    url: baseUrl + "/ha/workUser/nextService",
    method: "get",
  });
}

//完成服务
export function completeService(data) {
  return request({
    url: baseUrl + "/ha/workUser/completeService",
    method: "post",
    params: data,
  });
}

//结束本次服务
export function endService(data) {
  return request({
    url: baseUrl + "/ha/workUser/endService",
    method: "post",
    params: data,
  });
}

//服务结束，保存一桌联办记录
export function saveDesk(data) {
  return request({
    url: baseUrl + "/ha/desk/saveDesk",
    method: "post",
    data: data,
  });
}

//窗口叫号
export function windowCall(data) {
  return request({
    url: baseUrl + "/ha/workUser/windowCall",
    method: "post",
    params: data,
  });
}

export function getQueueById(data) {
  return request({
    url: baseUrl + "/ha/df/getQueueById",
    method: "post",
    params: data,
  });
}

export function saveQueueAdviseMemo(data) {
  return request({
    url: baseUrl + "/ha/df/saveQueueAdviceMemo",
    method: "post",
    params: data,
  });
}

//获取指定窗口取号目录列表
export function getAppointCatalogue(data) {
  return request({
    url: baseUrl + "/ha/takeCatalogue/getAppointCatalogue",
    method: "post",
    params: data,
  });
}

//窗口取号
export function takePriorityNumber(data) {
  return request({
    url: baseUrl + "/ha/takeCatalogue/takePriorityNumber",
    method: "post",
    data: data,
  });
}

//号票推送
export function winNumbPush(data) {
  return request({
    url: baseUrl + "/ha/web/winNumbPush",
    method: "post",
    data: data,
  });
}


//帮代办预约
export function appointmentNext(data) {
  return request({
    url: baseUrl + "/ha/workUser/appointmentNext",
    method: "post",
    params: data,
  });
}

//统计服务数据
export function statServiceNum() {
  return request({
    url: baseUrl + "/ha/workUser/statServiceNum",
    method: "get",
  });
}

// 获取全部的办事队列
export function queryAllWorkQueueList(data) {
  return request({
    url: baseUrl + "/ha/workUser/queryAllWorkQueueList",
    method: "get",
    params: data,
  });
}

// 获取当前帮办人全部的办事队列
export function queryAllWorkQueueListBylogin(data) {
  return request({
    url: baseUrl + "/ha/workUser/queryAllWorkQueueListBylogin",
    method: "get",
    params: data,
  });
}

//获取队列服务信息
export function queueServiceList(data) {
  return request({
    url: baseUrl + "/ha/workUser/queueServiceList",
    method: "get",
    params: data,
  });
}

//更新帮办服务
export function updateService(data) {
  return request({
    url: baseUrl + "/ha/workUser/updateService",
    method: "post",
    params: data,
  });
}

//获取分组列表
export function getGroupList() {
  return request({
    url: baseUrl + "/ha/workUser/getGroupList",
    method: "get",
  });
}

//获取分组列表
export function getGroupLeaderList() {
  return request({
    url: baseUrl + "/ha/workUser/getGroupLeaderList",
    method: "get",
  });
}

//获取街道分组信息及人员信息
export function getStreetist() {
  return request({
    url: baseUrl + "/ha/workUser/getStreetist",
    method: "get",
  });
}

//转派服务审核
export function turnServiceVerify(data) {
  return request({
    url: baseUrl + "/ha/workUser/turnServiceVerify",
    method: "post",
    params: data,
  });
}

//获取暂存的办事队列
export function getTempServiceList(data) {
  return request({
    url: baseUrl + "/ha/workUser/getTempServiceList",
    method: "get",
    params: data,
  });
}

//查询当前登录的帮办人员所属组长集合
export function queryGroupLeaderInfoList(data) {
  return request({
    url: baseUrl + "/ha/performancePlusTimeRecord/queryGroupLeaderInfoList",
    method: "post",
    params: data,
  });
}

//获取全部的加分项目列表
export function queryHaPlusProjectList(data) {
  return request({
    url: baseUrl + "/ha/performancePlusTimeRecord/queryHaPlusProjectList",
    method: "get",
    params: data,
  });
}

//分页查询个人加分时长绩效列表
export function queryHaPerformancePlustimeRecordPageResult(data) {
  return request({
    url: baseUrl + "/ha/performancePlusTimeRecord/queryHaPerformancePlustimeRecordPageResult",
    method: "post",
    params: data,
  });
}

//保存绩效加分时长记录信息
export function saveHaPerformancePlustimeRecord(data) {
  return request({
    url: baseUrl + "/ha/performancePlusTimeRecord/saveHaPerformancePlustimeRecord",
    method: "post",
    params: data,
  });
}

//根据主键查询绩效加分时长记录信息
export function getHaPerformancePlustimeRecordById(data) {
  return request({
    url: baseUrl + "/ha/performancePlusTimeRecord/getHaPerformancePlustimeRecordById",
    method: "get",
    params: data,
  });
}


//根据id删除加分记录信息
export function deleteHaPerformancePlustimeRecordById(data) {
  return request({
    url: baseUrl + "/ha/performancePlusTimeRecord/deleteHaPerformancePlustimeRecordById",
    method: "post",
    params: data,
  });
}

//审核组长审核加分时长记录保存接口
export function groupLeaderAuditHaPerformancePlustimeRecord(data) {
  return request({
    url: baseUrl + "/ha/performancePlusTimeRecord/groupLeaderAuditHaPerformancePlustimeRecord",
    method: "post",
    params: data,
  });
}

//获取一桌联办记录列表(分页)
export function getListWithPage(data) {
  return request({
    url: baseUrl + "/ha/desk/getListWithPage",
    method: "get",
    params: data,
  });
}

//获取一桌联办所有部门
export function getDeskGroup(data) {
  return request({
    url: baseUrl + "/ha/desk/getDeskGroup",
    method: "get",
    params: data,
  });
}

//一桌联办发起预约（保存联办部门表信息）
export function appDepartment(data) {
  return request({
    url: baseUrl + "/ha/desk/appDepartment",
    method: "get",
    params: data,
  });
}

//查看各部门预约进度
export function getAppCondition(data) {
  return request({
    url: baseUrl + "/ha/desk/getAppCondition",
    method: "get",
    params: data,
  });
}

//是否有一桌联办预约确认信息
export function isDeskAppointment(data) {
  return request({
    url: baseUrl + "/ha/desk/isDeskAppointment",
    method: "get",
    params: data,
  });
}

//确认一桌联办预约
export function conAppointment(data) {
  return request({
    url: baseUrl + "/ha/desk/conAppointment",
    method: "get",
    params: data,
  });
}

//获取当前天气
export function getWeather() {
  return request({
    url: baseUrl + "/ha/web/getWeather",
    method: "get",
  });
}

//发起一桌联办
export function startDesk(data) {
  return request({
    url: baseUrl + "/ha/desk/startDesk",
    method: "get",
    params: data,
  });
}

//根据组别id获取组别信息
export function getGroupById(data) {
  return request({
    url: baseUrl + "/ha/desk/getGroupById",
    method: "get",
    params: data,
  });
}

// 获取组织机构树信息
export function querySysOrganWithPage(data) {
  // if(districtOid == undefined){
  //   districtOid = '';
  // }else{
  //   districtOid=districtOid.replace('DISTRICT-','');
  // }
  return request({
    url: baseUrl+ '/ha/policy/querySysOrganWithPage',
    method: 'get',
    params:data
  })
}

//政策库查询
export function queryPolicyBaseWithPage(data) {
  return request({
    url: baseUrl + "/ha/policy/queryPolicyBaseWithPage",
    method: "post",
    params: data,
  });
}

//组长或委办局老师查询是否有等待的视频咨询
export function isHaveNewCall() {
  return request({
    url: baseUrl + "/ha/video/isHaveNewCall",
    method: "get",
  });
}

//***获取当前用户组长信息列表
export function getCurrentGroupLeaderList() {
  return request({
    url: baseUrl + "/ha/workUser/getCurrentGroupLeaderList",
    method: "get",
  });
}

//***获取委办局分组信息及人员信息
export function getBureauGroupList() {
  return request({
    url: baseUrl + "/ha/workUser/getBureauGroupList",
    method: "get",
  });
}

//***帮办老师发起帮办
export function startHelp(data) {
  return request({
    url: baseUrl + "/ha/video/startHelp",
    method: "get",
    params: data,
  });
}

//消息发送
export function sendMessage(data) {
  return request({
    url: baseUrl + "/ha/video/sendMessage",
    method: "post",
    data: data,
  });
}

//接收消息 是否有新的消息
export function receiveMessage(data) {
  return request({
    url: baseUrl + "/ha/video/receiveMessage",
    method: "get",
    params: data,
  });
}

//材料上传
export function uploadCaseMaterialFile(data) {
  return request({
    url: baseUrl + "/ha/outer/uploadCaseMaterialFile",
    method: "post",
    data: data,
  });
}

//呼叫组长或委办局
export function callHelp(data) {
  return request({
    url: baseUrl + "/ha/video/callHelp",
    method: "get",
    params: data,
  });
}

//呼叫组长或委办局
export function joinRoom(data) {
  return request({
    url: baseUrl + "/ha/video/joinRoom",
    method: "get",
    params: data,
  });
}


//结束视频帮办
export function endVideoHelp(data) {
  return request({
    url: baseUrl + "/ha/video/endVideoHelp",
    method: "get",
    params: data,
  });
}


//退出房间
export function exitRoom(data) {
  return request({
    url: baseUrl + "/ha/video/exitRoom",
    method: "get",
    params: data,
  });
}

//查询组长或委办局老师接通状态
export function callWait(data) {
  return request({
    url: baseUrl + "/ha/video/callWait",
    method: "get",
    params: data,
  });
}

