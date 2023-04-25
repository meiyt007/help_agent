import '@babel/polyfill'
import Vue from 'vue'

import Cookies from 'js-cookie'

import 'normalize.css/normalize.css' // a modern alternative to CSS resets

import Element from 'element-ui'
import './assets/styles/element-variables.scss'

import '@/assets/styles/index.scss' // global css
import '@/assets/styles/ruoyi.scss' // ruoyi css
import '@/assets/styles/iconfont.css' // zhuofan.css
import '@/assets/styles/moudle.scss' // moudle.css
import '@/assets/styles/ele-style-cover.scss'
import App from './App'
import store from './store'
import router from './router'
import permission from './directive/permission'
import './directive/dialog/dialogDrag'

import './assets/icons' // icon
import './permission' // permission control
import { parseTime, resetForm,selectMapLabel, download } from "@/utils/ruoyi";
import Pagination from "@/components/Pagination";
import CallActivityPropsReadonly from '@/views/workflowModeler/components/CallActivityPropsReadonly.vue';
import CallActivityPropsView from '@/views/workflowModeler/components/CallActivityPropsView.vue';
import { getToken } from '@/utils/auth'
import axios from "axios";
axios.interceptors.response.use(response => {
  return response.data
})
Vue.prototype.$axios = axios;

import Common from '@/common.js'
Vue.prototype.Common = Common

import * as filters from "./filters";
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key]);
})

//基础支撑
import { getDictList,queryDistrictSimpleTree,queryConfigTree,queryOrganTree,queryDistrictOrganUserTree } from "@/api/sys/common";

// 全局方法挂载

Vue.prototype.getDictList = getDictList
Vue.prototype.queryDistrictSimpleTree = queryDistrictSimpleTree
Vue.prototype.queryConfigTree = queryConfigTree
Vue.prototype.queryOrganTree = queryOrganTree
Vue.prototype.queryDistrictOrganUserTree = queryDistrictOrganUserTree

Vue.prototype.parseTime = parseTime
Vue.prototype.resetForm = resetForm
Vue.prototype.selectMapLabel = selectMapLabel
Vue.prototype.download = download
Vue.prototype.getToken = getToken

Vue.prototype.msgSuccess = function (msg) {
  this.$message({ showClose: true, message: msg, type: "success" });
}

Vue.prototype.msgError = function (msg) {
  this.$message({ showClose: true, message: msg, type: "error" });
}

Vue.prototype.msgWarning = function (msg) {
  this.$message({ showClose: true, message: msg, type: "warning" });
}

Vue.prototype.msgInfo = function (msg) {
  this.$message.info(msg);
}

// 全局组件挂载
Vue.component('Pagination', Pagination)
Vue.component('CallActivityPropsReadonly', CallActivityPropsReadonly)
Vue.component('CallActivityPropsView', CallActivityPropsView)

Vue.use(permission)
import FRender from "f-render";
Vue.component("f-render", FRender.FRender);
Vue.component("FormDesign", FRender.FormDesign);
Vue.component("FormReport", FRender.FormReport);
Vue.component("FormView", FRender.FormView);
Vue.component("FormTemplate", FRender.FormTemplate);
import 'f-render/src/assets/styles/fonts/iconfont-form.css' // zhuofan.css

/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online! ! !
 */

Vue.use(Element, {
  size: Cookies.get('size') || 'medium' // set element-ui default size
})

Vue.config.productionTip = false
//store.dispatch("LoadStaticRouters")
new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
