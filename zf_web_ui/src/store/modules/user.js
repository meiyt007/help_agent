import { login, logout, getInfo } from "@/api/login";
import { getToken, setToken, removeToken } from "@/utils/auth";
import { encryptPwd } from "@/utils/jsencrypt";
import { MessageBox } from "element-ui";
import store from "@/store";

const user = {
  state: {
    token: getToken(),
    name: "",
    avatar: "",
    permissions: [],
    login: {},
    basicUserInfo: {},
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token;
    },
    SET_NAME: (state, name) => {
      state.name = name;
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar;
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions;
    },
    SET_LOGIN: (state, login) => {
      state.login = login;
    },
    setBasicUserInfo(state, view) {
      state.basicUserInfo = view;
    },
  },

  actions: {
    // 登录
    Login({ commit }, userInfo) {
      const account = userInfo.account.trim();
      const pcode = userInfo.pcode;
      const checkCode = userInfo.checkCode;
      const haType = userInfo.haType;
      return new Promise((resolve, reject) => {
        login(account, pcode, checkCode, haType)
          .then((res) => {
            if (res.code === 200) {
              // console.log(res.data)
              getInfo({ Token: res.data.token })
                .then((res) => {
                  const user = res.data;
                  // console.log(user);
                  commit("setBasicUserInfo", user);
                  commit("SET_NAME", user.name);
                })
                .catch((error) => {
                  reject(error);
                });
              setToken(res.data.token);
              commit("SET_LOGIN", res.data);
              commit("SET_TOKEN", res.data.token);
              resolve(res);
            }
            resolve(res);

            // let warnDay = null
            // let flag = null
            // if (warnDay != null && warnDay != undefined && warnDay != '') {
            //   setToken(res.token)
            //   commit('SET_LOGIN', res.result)
            //   commit('SET_TOKEN', res.token)
            //   resolve()
            // } else if (flag != null && flag != undefined && flag === 1) {
            //   setToken(res.token)
            //   localStorage.setItem('loginInfo', JSON.stringify(res.result))
            //   commit('SET_LOGIN', res.result)
            //   commit('SET_TOKEN', res.token)
            //   resolve()
            // } else {

            // }
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    // 获取用户信息
    GetInfo({ commit, state, getters }) {
      return new Promise((resolve, reject) => {
        getInfo({ Token: state.token })
          .then((res) => {
            const user = res.data;
            // console.log(user);
            commit("setBasicUserInfo", user);
            // let avatar = require('@/assets/image/profile.jpg')
            // if (user && user.headImageAttaOid) {
            //   avatar = process.env.VUE_APP_BASE_API + '/platform/security/atta/imageDisplay/' + user.headImageAttaOid + '?access_token=' + getToken()
            // }
            //user.avatar == "" ? require("@/assets/image/profile.jpg") : process.env.VUE_APP_BASE_API + user.avatar;

            commit("SET_NAME", user.name);
            // commit('SET_AVATAR', avatar)
            resolve(res);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },

    // 退出系统
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token)
          .then(() => {
            commit("SET_TOKEN", "");
            removeToken();
            resolve();
          })
          .catch((error) => {
            reject(error);
          });
      });
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise((resolve) => {
        commit("SET_TOKEN", "");
        removeToken();
        resolve();
      });
    },
  },
};

export default user;
