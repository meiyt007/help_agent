import { login, loginByOauth2, logout, getInfo } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'
import { encryptPwd } from '@/utils/jsencrypt'
import {MessageBox} from "element-ui";
import store from "@/store";

const user = {
  state: {
    token: getToken(),
    name: '',
    avatar: '',
    roles: [],
    permissions: [],
    login:{},
    skinClassname:'',
    userOid:''
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions
    },
    SET_LOGIN: (state, login) => {
      state.login = login
    },
    SET_SKINCLASSNAME: (state, skinClassname) => {
      state.skinClassname = skinClassname
    },
    SET_USER_OID: (state, userOid) => {
      state.userOid = userOid
    }
  },

  actions: {
    // 登录
    Login({ commit }, userInfo) {
      const username = userInfo.username.trim()
      let password = userInfo.password
      if(userInfo.config==="1"){
        password = encryptPwd(userInfo.publicKey,userInfo.password)
      }
      const captcha = userInfo.captcha
      const uuid = userInfo.uuid
      return new Promise((resolve, reject) => {
        login(username, password, captcha, uuid).then(res => {
          let warnDay = res.result.warnDay;
          let flag = res.result.psFlag;
          if (warnDay != null && warnDay != undefined && warnDay != ''){
            MessageBox.alert(
              '当前系统注册授权剩余'+warnDay+'天,请及时申请新的授权文件！',
              '温馨提示',
              {
                type: 'warning'
              }
            ).then(() => {
              setToken(res.token)
              commit('SET_LOGIN', res.result)
              commit('SET_TOKEN', res.token)
              resolve()
            })
          }else if (flag != null && flag != undefined && flag === 1){
            MessageBox.alert(
              '您已长时间未修改密码，请及时修改密码',
              '温馨提示',
              {
                type: 'warning'
              }
            ).then(() => {
              setToken(res.token)
              commit('SET_LOGIN', res.result)
              commit('SET_TOKEN', res.token)
              resolve()
            })
          }else {
            setToken(res.token)
            commit('SET_LOGIN', res.result)
            commit('SET_TOKEN', res.token)
            resolve()
          }


        }).catch(error => {
          reject(error)
        })
      })
    },
    // 登录
    LoginByOauth2({ commit }, userInfo) {
      const username = userInfo.username.trim()
      let password = userInfo.password
      if(userInfo.config==="1"){
        password = encryptPwd(userInfo.publicKey,userInfo.password)
      }
      const captcha = userInfo.captcha
      const uuid = userInfo.uuid
      return new Promise((resolve, reject) => {
        loginByOauth2(username, password, captcha, uuid).then(res => {
          let warnDay = res.user_info.warnDay,
            flag = res.user_info.psFlag;
          if (warnDay != null && warnDay != undefined && warnDay != ''){
            MessageBox.alert(
              '当前系统注册授权剩余'+warnDay+'天,请及时申请新的授权文件！',
              '温馨提示',
              {
                type: 'warning'
              }
            ).then(() => {
              setToken(res.access_token)
              commit('SET_LOGIN', res.user_info)
              commit('SET_TOKEN', res.access_token)
              resolve()
            })
          }else if (flag != null && flag != undefined && flag === 1){
            MessageBox.alert(
              '您已长时间未修改密码，请及时修改密码',
              '温馨提示',
              {
                type: 'warning'
              }
            ).then(() => {
              setToken(res.access_token)
              commit('SET_LOGIN', res.user_info)
              commit('SET_TOKEN', res.access_token)
              resolve()
            })
          }else {
            setToken(res.access_token)
            commit('SET_LOGIN', res.user_info)
            commit('SET_TOKEN', res.access_token)
            resolve()
          }


        }).catch(error => {
          reject(error)
        })
      })
    },
    // 获取用户信息
    GetInfo({ commit, state ,getters}) {
      return new Promise((resolve, reject) => {
        getInfo(state.token).then(res => {
          const user = res.data.user
          const roles=res.data.roles;
          const permission=res.data.permissions;
          let avatar = require("@/assets/image/profile.jpg");
          if( user && user.headImageAttaOid){
            avatar = process.env.VUE_APP_BASE_API + "/platform/security/atta/imageDisplay/"+user.headImageAttaOid + '?access_token=' +  getToken();
          }
          //user.avatar == "" ? require("@/assets/image/profile.jpg") : process.env.VUE_APP_BASE_API + user.avatar;
          if (roles && roles.length > 0) { // 验证返回的roles是否是一个非空数组
            commit('SET_ROLES', roles)
            commit('SET_PERMISSIONS', permission)
          } else {
            commit('SET_ROLES', ['ROLE_DEFAULT'])
          }
          commit('SET_NAME', user.name)
          commit('SET_USER_OID', user.userOid)
          commit('SET_SKINCLASSNAME', user.skinClassname)
          commit('SET_AVATAR', avatar)
          resolve(res)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 退出系统
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          commit('SET_TOKEN', '')
          commit('SET_ROLES', [])
          commit('SET_PERMISSIONS', [])
          removeToken()
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        removeToken()
        resolve()
      })
    }
  }
}

export default user
