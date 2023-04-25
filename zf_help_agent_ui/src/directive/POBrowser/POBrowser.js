import Vue from 'vue';
Vue.directive('poBrowser', {
  bind (el, binding, vnode, oldVnode) {
    var oS = document.createElement('script')
    oS.src = "http://10.22.19.40:9090/pageoffice.js";
    // TODO: 质控要求先注释 2021/9/7
     document.getElementsByTagName('head')[0].appendChild(oS)
  }
})
