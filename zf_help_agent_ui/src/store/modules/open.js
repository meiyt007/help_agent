import store from "@/store";
import { Message } from "element-ui";
const state = {
  open: null,
  switchOnOff: null,
  code: null
}

const mutations = {
  CHANGE_OPEN: (state, open) => {
    if (!store.state.config.znzxFlag) return;
    // if (!store.state.config.znzxFlag) return Message({ type: 'warning', message: '未启用智能咨询配置!' });
    state.open = open
  },
  CHANGE_SWITCHONOFF: (state, switchOnOff) => {
    if (!store.state.config.znzxFlag) return;
    // if (!store.state.config.znzxFlag) return Message({ type: 'warning', message: '未启用智能咨询配置!' });
    state.switchOnOff = switchOnOff
  },
  CHANGE_CODE: (state, code) => {
    state.code = code
  }
}

const actions = {

}

export default {
  state,
  mutations,
  actions
}

