/*
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-07-22 15:25:52
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-10-25 10:47:57
 * @FilePath: \zf_web_ui\src\main.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import * as api from "./api/index.js";
import outside from './directives/outside'

import * as BRTC from './lib/BRTC'
import * as BRTM from './lib/BRTM'
Vue.directive('outside', outside)



import Cookies from "js-cookie";
import FRender from "f-render";
// import FRender from 'f-render'
Vue.component("f-render", FRender.FRender);
Vue.component("FormDesign", FRender.FormDesign);
Vue.component("FormReport", FRender.FormReport);
Vue.component("FormView", FRender.FormView);
import "f-render/src/assets/styles/fonts/iconfont-form.css"; // zhuofan.css

Vue.config.productionTip = false;
import Print from "@/libs/vue-print-nb";
import "./permission";
import "./directive/dialog/dialogDrag";
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
import VueTouch from "vue-touch";

import "@/assets/styles/ruoyi.scss"; // ruoyi css
import "@/assets/styles/fonts/iconfont.css"; // zhuofan.css
import "@/assets/styles/moudle.scss"; // moudle.css
import "@/assets/styles/ele-style-cover.scss";
import * as animated from "animate.css";
require("!style-loader!css-loader!less-loader!@/assets/styles/color.less");
Vue.prototype.$ = $;
window.$ = $;
Vue.use(Print);
Vue.use(animated);
Vue.use(ElementUI);
Vue.use(VueTouch, {
  name: "v-touch",
});
VueTouch.config.swipe = {
  threshold: 2, //手指左右滑动触发事件距离
};
Vue.prototype.$api = api;

window.BRTC = BRTC
window.BRTM = BRTM

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount("#app");
