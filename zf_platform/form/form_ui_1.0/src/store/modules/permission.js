import { constantRoutes } from '@/router'
import { getRouters } from '@/api/menu'
import Layout from '@/layout/index'
import ParentView from '@/components/ParentView';
const permission = {
  state: {
    appOid:'',
    routes: [],
    addRoutes: [],
    appList: []
  },
  mutations: {
    SET_ROUTES: (state, routes) => {
      state.addRoutes = routes
      state.routes = constantRoutes.concat(routes)
    },
    SET_APP_OID:(state,appOid)=>{
      state.appOid = appOid;
    },
    GET_APP_OID:(state)=>{
     return  state.appOid;
    },
    SET_APP_LIST:(state,appList)=>{
      state.appList = appList;
    }

  },

  actions: {
    // LoadStaticRouters({commit}){
    //   commit('SET_ROUTES', []);
    // },
    // 生成路由
    GenerateRoutes({commit,getters}) {
      return new Promise(resolve => {
        // 向后端请求路由数据
        const appOid = getters.app_oid;
        //设置本地浏览器缓存
        sessionStorage.setItem('currentAppOid',appOid);
        getRouters(appOid).then(res => {
          const accessedRoutes = filterAsyncRouter(res.data)
          accessedRoutes.push({ path: '*', redirect: '/404', hidden: true })
          commit('SET_ROUTES', accessedRoutes)
          resolve(accessedRoutes)
        })
      })
    },
    //设置选中的appOid
    setAppOid({ commit },appOid){
      commit('SET_APP_OID', appOid)
    },
    //所属的应用列表
    setAppList({ commit },appList){
      var result = [];
      for(var i=0,len=appList.length;i<len;i+=4){
        result.push(appList.slice(i,i+4));
      }
      commit('SET_APP_LIST', result)
    }
  }
}

// 遍历后台传来的路由字符串，转换为组件对象
function filterAsyncRouter(asyncRouterMap) {
  return asyncRouterMap.filter(route => {
    if (route.component) {
      // Layout组件特殊处理
      if (route.component === 'Layout') {
        route.component = Layout
      }else if (route.component === 'ParentView') {
        route.component = ParentView
      }  else {
        if(route.meta && route.meta.outLink) {
          if(!/^(?:http(s)?:\/\/)?[\w.-]+(?:\.[\w\.-]+)+[\w\-\._~:/?#[\]@!\$&'\*\+,;=.]+$/.test(route.meta.outLink)) {
            route.meta.outLink = process.env.VUE_APP_BASE_API + route.meta.outLink;
          }
        }
        route.component = loadView(route.component)
      }
    }
    if (route.children != null && route.children && route.children.length) {
      route.children = filterAsyncRouter(route.children)
    }
    return true
  })
}

export const loadView = (view) => { // 路由懒加载
  return (resolve) =>  require([`@/views/${view}`], resolve)
}

export default permission
