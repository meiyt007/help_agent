/*
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-07-18 10:05:28
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-11-10 09:59:08
 * @FilePath: \hpNewHall\src\router\index.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import Vue from "vue";
import VueRouter from "vue-router";

import Index from '../views/meeting/Index.vue'
import Join from '../views/meeting/Join.vue'
import Meeting from '../views/meeting/Meeting.vue'
import NetError from '../views/meeting/NetError.vue'
import Unsupport from '../views/meeting/Unsupport.vue'

Vue.use(VueRouter);
// 解决报错
const originalPush = VueRouter.prototype.push;
const originalReplace = VueRouter.prototype.replace;
// push
VueRouter.prototype.push = function push(location, onResolve, onReject) {
  if (onResolve || onReject)
    return originalPush.call(this, location, onResolve, onReject);
  return originalPush.call(this, location).catch((err) => err);
};
// replace
VueRouter.prototype.replace = function push(location, onResolve, onReject) {
  if (onResolve || onReject)
    return originalReplace.call(this, location, onResolve, onReject);
  return originalReplace.call(this, location).catch((err) => err);
};

import Layout from "@/layout";
import assistant from "@/views/pad/assistant/index.vue";
const routes = [
  {
    path: "/redirect",
    component: Layout,
    hidden: true,
    children: [
      {
        path: "/redirect/:path(.*)",
        component: (resolve) => require(["@/views/redirect"], resolve),
      },
    ],
  },
  //pc登入界面
  {
    path: "/pcLogin",
    component: (resolve) => require(["@/views/pc/login/login"], resolve),
    hidden: true,
  },
  //pad登入界面
  {
    path: "/padLogin",
    component: (resolve) => require(["@/views/pad/login/login"], resolve),
    hidden: true,
  },

  // {
  //   path: '/404',
  //   component: (resolve) => require(['@/views/error/404'], resolve),
  //   hidden: true,
  // },
  // {
  //   path: '/401',
  //   component: (resolve) => require(['@/views/error/401'], resolve),
  //   hidden: true,
  // },
  // 找回密码
  {
    path: "/findPassword",
    component: (resolve) => require(["@/views/findPassword"], resolve),
    hidden: true,
  },
  {
    path: "/user",
    component: Layout,
    hidden: true,
    redirect: "noredirect",
    children: [
      {
        path: "profile",
        component: (resolve) =>
          require(["@/views/user/profile/index"], resolve),
        name: "Profile",
        meta: { title: "个人中心", icon: "user" },
      },
    ],
  },
  {
    path: "/guideClothing",
    component: Layout,
    hidden: true,
    redirect: "guideClothing",
    children: [
      {
        path: "/guideClothing",
        component: (resolve) =>
          require(["@/views/pc/guideClothing/index"], resolve),
        name: "导服首页",
        meta: { title: "导服首页", icon: "" },
      },
    ],
  },
  {
    path: "/quickAssistant",
    component: Layout,
    hidden: true,
    redirect: "quickAssistant",
    children: [
      {
        path: "/quickAssistant",
        component: (resolve) =>
          require(["@/views/pc/quickAssistant/index"], resolve),
        name: "快捷帮办首页",
        meta: { title: "快捷帮办首页", icon: "" },
      },
    ],
  },
  {
    path: "/roundTableAssistant",
    component: Layout,
    hidden: true,
    redirect: "roundTableAssistant",
    children: [
      {
        path: "/roundTableAssistant",
        component: (resolve) =>
          require(["@/views/pc/roundTableAssistant/index"], resolve),
        name: "圆桌帮办首页",
        meta: { title: "圆桌帮办首页", icon: "" },
      },
    ],
  },
  {
    path: "/assistant",
    component: (resolve) => require(["@/views/pad/assistant/index"], resolve),
    hidden: true,
  },
  // {
  //   path: '/',
  //   component: (resolve) => require(['@/views/pad/assistant/index'], resolve),
  //   hidden: true,
  // },
  //快捷帮办首页
  {
    path: "/padAssistantHome",
    component: assistant,
    hidden: true,
    redirect: "padAssistantHome",
    children: [
      {
        path: "/padAssistantHome",
        component: (resolve) =>
          require([
            "@/views/pad/assistant/components/padAssistantHome.vue",
          ], resolve),
        name: "padAssistantHome",
        meta: { title: "首页", icon: "" },
      },
    ],
  },
  //材料库
  // {
  //   path: "/materialStore",
  //   component: (resolve) => require(["@/views/pad/materialStore"], resolve),
  //   hidden: true,
  // },
  {
    path: '/materialStore',
    hidden: true,
    component: (resolve) => require(["@/views/pad/materialStore"], resolve),
    // redirect: "materialStore",
    children: [
      {
        path: '/materialStore',
        component: (resolve) =>
        require([
          "@/views/pad/materialStore",
        ], resolve),
        // component: (resolve) => require(["@/views/pad/materialStore"], resolve),
        name: 'materialStore',
        meta: { title: '我的资源库', icon: "" }
      },
    ]
  },
  {
    path: '/detail',
    hidden: true,
    component: (resolve) => require(["@/views/pad/materialStore/detail"], resolve),
    // redirect: "materialStore",
    children: [
      {
        path: '/detail',
        component: (resolve) =>
        require([
          "@/views/pad/materialStore/detail",
        ], resolve),
        // component: (resolve) => require(["@/views/pad/materialStore"], resolve),
        name: 'detail',
        meta: { title: '详情', icon: "" }
      },
    ]
  },
  {
    path: '/videoConsultation',
    hidden: true,
    component: (resolve) => require(["@/views/pad/videoConsultation"], resolve),
    // redirect: "materialStore",
    children: [
      {
        path: '/videoConsultation',
        component: (resolve) =>
        require([
          "@/views/pad/videoConsultation",
        ], resolve),
        name: 'videoConsultation',
        meta: { title: '视频咨询', icon: "" }
      },
    ]
  },
  // {
  //   path: "/goodBadComment",
  //   component: assistant,
  //   hidden: true,
  //   redirect: "goodBadComment",
  //   children: [
  //     {
  //       path: "/goodBadComment",
  //       component: (resolve) =>
  //         require([
  //           "@/views/pad/assistant/components/process/goodBadComment.vue",
  //         ], resolve),
  //       name: "goodBadComment",
  //       meta: { title: "好差评", icon: "" },
  //     },
  //   ],
  // },
  {
    path: "/processService",
    component: assistant,
    hidden: true,
    redirect: "processService",
    children: [
      {
        path: "/processService",
        component: (resolve) =>
          require([
            "@/views/pad/assistant/components/process/index.vue",
          ], resolve),
        name: "processService",
        meta: { title: "服务", icon: "" },
      },
    ],
  },
  //扫码帮代办
  {
    path: "/codeScanningAgent",
    component: (resolve) =>
      require(["@/views/phone/codeScanningAgent"], resolve),
    hidden: true,
    meta: { title: "扫码帮代办", icon: "" },
  },
  //扫码上传
  {
    path: "/materialUpload",
    component: (resolve) =>
      require(["@/views/phone/material-upload/index"], resolve),
    hidden: true,
    meta: { title: "扫码上传", icon: "" },
  },
  //绩效列表
  {
    path: "/achievement",
    component: (resolve) =>
      require(["@/views/phone/achievements/index.vue"], resolve),
    hidden: true,
    meta: { title: "绩效列表", icon: "" },
  },
  //取号信息
  {
    path: "/getNumber",
    component: (resolve) =>
      require(["@/views/phone/getNumber/index.vue"], resolve),
    hidden: true,
    meta: { title: "取号信息", icon: "" },
  },
  {
    path: '/index',
    name: 'index',
    component: Index
  },
  {
    path: '/join',
    name: 'Join',
    component: Join
  },
  {
    path: '/meeting',
    name: 'Meeting',
    component: Meeting
  },
  {
    path: '/neterr',
    name: 'NetError',
    component: NetError
  },
  {
    path: '/unsupport',
    name: 'Unsupport',
    component: Unsupport
  }
];

const router = new VueRouter({
  mode: "history",
  base: 'bangban',
  routes,
});

export default router;
