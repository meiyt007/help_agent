/*
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-08-11 10:50:14
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-09-22 14:17:40
 * @FilePath: \zf_help_agent_ui\src\permission.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { getToken, getLoginUrl } from '@/utils/auth'
import { Notification } from 'element-ui'

NProgress.configure({ showSpinner: false })

const whiteList = ['/login', '/oauthLogin', '/auth-redirect', '/bind', '/register', '/initRegister', '/findPassword','/userOidLogin']

router.beforeEach((to, from, next) => {
  NProgress.start()
  if (getToken()) {
    /* has token*/
    if (to.path === getLoginUrl()) {
      next({ path: '/' })
      NProgress.done()
    } else {
      if (store.getters.roles.length === 0) {
        // 判断当前用户是否已拉取完user_info信息
        store.dispatch('GetInfo').then(res => {
          //如果存在缓存，则从浏览器缓存中获取
          let currentAppOid = sessionStorage.getItem('currentAppOid');
          let flag = true;
          if ('' == currentAppOid || null == currentAppOid) {
            currentAppOid = res.data.fristAppOid;
            flag = false;
          }
          //设置第一个默认的应用oid
          store.dispatch('setAppOid', currentAppOid);
          //所属的应用list
          store.dispatch('setAppList', res.data.appList);
          // 拉取user_info
          const roles = res.data.roles;
          store.dispatch('GenerateRoutes', { roles }).then(accessRoutes => {
            // 根据roles权限生成可访问的路由表
            router.addRoutes(accessRoutes) // 动态添加可访问路由表
            if (flag) {
              next({ ...to, replace: true }) // hack方法 确保addRoutes已完成
            } else {
              next({ path: '/home' })
            }
          })
        })
          .catch(err => {
            store.dispatch('FedLogOut').then(() => {
              // FIX: 提示去除
              // Message.error(err)
              next({ path: '/' })
            })
          })
      } else {
        next()
        // 没有动态改变权限的需求可直接next() 删除下方权限判断 ↓
        // if (hasPermission(store.getters.roles, to.meta.roles)) {
        //   next()
        // } else {
        //   next({ path: '/401', replace: true, query: { noGoBack: true }})
        // }
        // 可删 ↑
      }
    }
  } else {
    // 没有token
    if (whiteList.indexOf(to.path) !== -1) {
      // 在免登录白名单，直接进入
      next()
    } else {
      next(getLoginUrl() + `?redirect=${to.fullPath}`) // 否则全部重定向到登录页
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})
