import '@babel/polyfill'
import Vue from 'vue'
require("babel-polyfill");
import 'babel-polyfill'
require('es6-promise').polyfill();

import Cookies from 'js-cookie'

import 'normalize.css/normalize.css' // a modern alternative to CSS resets

import FRender from "f-render";
Vue.component("f-render", FRender.FRender);
Vue.component("FormDesign", FRender.FormDesign);
Vue.component("FormReport", FRender.FormReport);
Vue.component("FormView", FRender.FormView);
import 'f-render/src/assets/styles/fonts/iconfont-form.css'// zhuofan.css

import Element from 'element-ui'
import './assets/styles/element-variables.scss'

import '@/assets/styles/index.scss' // global css
import '@/assets/styles/ruoyi.scss' // ruoyi css
import '@/assets/styles/iconfont.css' // zhuofan.css
import '@/assets/styles/moudle.scss' // moudle.css
import '@/assets/styles/ele-style-cover.scss'
import '@/assets/styles/theme.scss' //组件主题定制
import '@/assets/styles/iconfontSelectColor.css'
import '@/assets/styles/zcFonts/iconfont.css' // zc css

/** 富文本编辑器 */
import "quill/dist/quill.core.css";
import "quill/dist/quill.snow.css";
import "quill/dist/quill.bubble.css";

import App from './App'
import store from './store'
import router from './router'
import permission from './directive/permission'
//import './directive/POBrowser/POBrowser'
import './directive/dialog/dialogDrag'

import './assets/icons' // icon
import './permission' // permission control
import { parseTime, resetForm, selectMapLabel, download } from "@/utils/ruoyi";
import Pagination from "@/components/Pagination";
import socketApi from "@/utils/socketStart";//找到封装的socket.js文件
import CallActivityPropsReadonly from '@/views/workflowModeler/components/CallActivityPropsReadonly.vue';
import CallActivityPropsView from '@/views/workflowModeler/components/CallActivityPropsView.vue';

import { getToken } from '@/utils/auth'

// 引入 vue prototype common functions
import '@/utils/proto.js';

// 引入array.at polyfill
import '@/utils/array.polyfill.js';

// 指令 v-animate
import '@/utils/v-animate.js'

import elTableInfiniteScroll from 'el-table-infinite-scroll';
Vue.use(elTableInfiniteScroll);

// 引入echarts
import echarts from 'echarts'
Vue.prototype.$echarts = echarts

import Common from '@/common.js'
Vue.prototype.Common = Common

import * as filters from "./filters";
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key]);
})

// 表单校验
//import 'default-passive-events'

//基础支撑
import { getDictList, queryDistrictSimpleTree, queryConfigTree, queryOrganTree, queryDistrictOrganUserTree } from "@/api/sys/common";
//添加打印插件
import Print from '@/utils/lib/print.umd';
Vue.use(Print)
Vue.prototype.$print = Print;
// 高德地图
import AMap from 'vue-amap';
Vue.use(AMap);
// 初始化vue-amap
AMap.initAMapApiLoader({
  // 高德key
  key: '87bd8b61d0bfd98f3049d13a09a026c7',
  // 插件集合 （插件按需引入）
  plugin: ['AMap.Autocomplete']
});
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

Vue.prototype.socketApi = socketApi;//$socketApi就在所有的 Vue 实例中可用了。

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

Vue.prototype.$keyBoard = function (vm, methodName, code) {
  document.onkeydown = function () {
    let key = window.event.keyCode;
    if (key == code) {
      vm[methodName](code); // 触发methodName事件
    }
  };

  vm.$once('hook:beforeDestroy', function () {
    document.onkeydown = null;
  })
}

// 全局组件挂载
Vue.component('Pagination', Pagination)
Vue.component('CallActivityPropsReadonly', CallActivityPropsReadonly)
Vue.component('CallActivityPropsView', CallActivityPropsView)

Vue.use(permission)

/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online! ! !
 */

Vue.use(Element, {
  size: Cookies.get('size') || 'medium', // set element-ui default size
})

/** 添加语音输入功能 */
import ElInput from '@/components/ElInput';
Vue.component('ElInput', ElInput);

/** 添加语音输入功能 */
import ElAutocomplete from '@/components/ElAutocomplete';
Vue.component('ElAutocomplete', ElAutocomplete);

/** dialog添加内置属性,修改内置属性初始值 */
import ElDialog from '@/components/ElDialog';
Vue.component('ElDialog', ElDialog);

/** stripe属性改为false */
import ElTable from '@/components/ElTable';
Vue.component('ElTable', ElTable);

const $bus = new Vue();
Vue.prototype.$bus = $bus;

Vue.config.productionTip = false
//store.dispatch("LoadStaticRouters")
new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
