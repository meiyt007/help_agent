import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: 路由配置项
 *
 * hidden: true                   // 当设置 true 的时候该路由不会再侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1
 * alwaysShow: true               // 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
 *                                // 只有一个时，会将那个子路由当做根路由显示在侧边栏--如引导页面
 *                                // 若你想不管路由下面的 children 声明的个数都显示你的根路由
 *                                // 你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，一直显示根路由
 * redirect: noRedirect           // 当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
 * name:'router-name'             // 设定路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题
 * meta : {
    roles: ['admin','editor']    // 设置该路由进入的权限，支持多个权限叠加
    title: 'title'               // 设置该路由在侧边栏和面包屑中展示的名字
    icon: 'svg-name'             // 设置该路由的图标，对应路径src/icons/svg
    breadcrumb: false            // 如果设置为false，则不会在breadcrumb面包屑中显示
  }
 */

// 公共路由
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: (resolve) => require(['@/views/redirect'], resolve)
      }
    ]
  },
  {
    path: '/login',
    component: (resolve) => require(['@/views/login'], resolve),
    hidden: true
  },
  {
    path: '/oauthLogin',
    component: (resolve) => require(['@/views/oauthLogin'], resolve),
    hidden: true
  },
  {
    path: '/404',
    component: (resolve) => require(['@/views/error/404'], resolve),
    hidden: true
  },
  {
    path: '/401',
    component: (resolve) => require(['@/views/error/401'], resolve),
    hidden: true
  },
  {
    path: '/initRegister',
    component: (resolve) => require(['@/views/sys/register/index'], resolve),
    hidden: true
  },
  {
    path: '/findPassword',
    component: (resolve) => require(['@/views/findPassword'], resolve),
    hidden: true
  },
  {
    path: '/flowView',
    component: (resolve) => require(['@/views/workflowModeler/FlowView'], resolve),
    hidden: true
  },
  {
    path: '/processView',
    component: (resolve) => require(['@/views/workflowModeler/ProcessView'], resolve),
    hidden: true
  },
  {
    path: '/processDesign',
    component: (resolve) => require(['@/views/workflowModeler/ProcessDesign'], resolve),
    hidden: true
  },
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: 'profile',
        component: (resolve) => require(['@/views/sys/user/profile/index'], resolve),
        name: 'Profile',
        meta: { title: '个人中心', icon: 'user' }
      }
    ]
  },
  // {
  //   path: '/',
  //   component: Layout,
  //   redirect: 'welcome',
  //   children: [
  //     {
  //       path: 'welcome',
  //       component: (resolve) => require(['@/views/welcome'], resolve),
  //       name: '欢迎页',
  //       meta: { title: '欢迎页', icon: 'colorIconfont icon-shouye-caise', noCache: true, affix: true }
  //     }
  //   ]
  // },
  {
    path: '/',
    component: Layout,
    redirect: 'home',
    children: [
      {
        path: 'home',
        component: (resolve) => require(['@/views/home/index'], resolve),
        name: '首页',
        meta: { title: '首页', icon: 'colorIconfont icon-shouye-caise', noCache: true, affix: true }
      }
    ]
  },
  {
    path: '/formDesign',/** 电子表单设计 */
    component: (resolve) => require(['@/views/zc/sxService/formConfig/formDesign.vue'], resolve),
    hidden: true
  },
  {
    path: '/comboFormDesign',/** 一件事电子表单设计 */
    component: (resolve) => require(['@/views/onething/sxpz/comboSituation/comboForm/comboFormDesign.vue'], resolve),
    hidden: true
  },
  // 下方为鄂尔多斯项目单独提取外链路由
  {
    path:'/temporaryAcceptance',
    name:'temporaryAcceptance',
    component: (resolve) => require(['@/views/zc/businessManagement/temporaryAcceptance/temporaryAcceptance.vue'], resolve),
  },
  {
    path:'/smartRegistration',
    name:'smartRegistration',
    component: (resolve) => require(['@/views/zc/businessManagement/windowAcceptance/smartRegistration.vue'], resolve),
  },
  {
    path:'/doneBusiness',
    name:'doneBusiness',
    component: (resolve) => require(['@/views/zc/businessManagement/doneBusiness/doneBusiness.vue'], resolve),
  },
  {
    path: '/userOidLogin',
    component: (resolve) => require(['@/views/userOidLogin/index'], resolve),
    hidden: true
  }
]

const createRouter = () =>
  new Router({
    mode: 'history', // 去掉url中的#
    scrollBehavior: () => ({ y: 0 }),
    routes: constantRoutes
  })

const router = createRouter();

export default router;

// 定义一个resetRouter 方法，在退出登录时，调用即可
export function resetRouter () {
  const newRouter = createRouter();
  router.matcher = newRouter.matcher;
}
