/*
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-07-18 10:05:28
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-08-19 11:32:34
 * @FilePath: \hpNewHall\src\permission.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import router from './router'
import store from './store'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { getToken, getLoginUrl } from '@/utils/auth'
NProgress.configure({ showSpinner: false })

let flag = navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i)
const whiteList = ['/pcLogin', '/padLogin', '/findPassword', '/codeScanningAgent', '/materialUpload', '/getNumber']
router.beforeEach((to, from, next) => {
  NProgress.start()
  if (getToken()) {
    /* has token*/
    console.log('路由拦截0000=====>', to.path, getLoginUrl())
    if (to.path === '/login') {
      if (flag) {
        next({ path: '/padAssistantHome' })
      } else {
        next({ path: '/roundTableAssistant' })
      }

      NProgress.done()
    } else {
      if (JSON.stringify(store.getters.basicUserInfo) === '{}') {
        // 判断当前用户是否已拉取完user_info信息
        store
          .dispatch('GetInfo')
          .then((res) => {
            let userType = res.data.userType
            if (to.path === '/') {
              if (userType === '1') {
                next({ path: '/guideClothing' })
              }
              if (userType === '2') {
                next({ path: '/roundTableAssistant' })
              }
            }
            //如果存在缓存，则从浏览器缓存中获取
            // let currentAppOid = sessionStorage.getItem('currentAppOid')
            // let flag = true
            // if ('' == currentAppOid || null == currentAppOid) {
            //   currentAppOid = res.data.fristAppOid
            //   flag = false
            // }
            const user = res.data
            // }
            // if (from.path === '/login') {
            //   next({ path: '/padAssistantHome' })
            // } else {
            next({ ...to, replace: true })

            // if (flag) {
            //   next({ path: '/padAssistantHome' })
            // } else {
            //   next({ path: '/roundTableAssistant' })
            // }
          })
          .catch((err) => {
            store.dispatch('FedLogOut').then(() => {
              // Message.error(err)
              if (flag) {
                next(`/padLogin?redirect=${to.path}`)
              } else {
                next(`/pcLogin?redirect=${to.path}`)
              }
            })
          })
      } else {
        if (to.path === '/') {
          if (flag) {
            next({ path: '/padAssistantHome' })
          } else {
            next({ path: '/roundTableAssistant' })
          }
        } else {
          next()
        }

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
