
const state = {
    currentPersonNum: '未开始叫号',
    startCall: false, // 是否开始叫号
    caseOid: '',
    currentVirtualBusinessRecordOid: '',
    saveCallRecordOid: '' //加号记录表主键
}

const mutations = {
  SET_SAVE_CALL_RECORD_OID: (state, payload) => {
    state.saveCallRecordOid = payload;
  },
    SET_CURRENT_PERSON_NUM: (state, payload) => {
        state.currentPersonNum = payload;
        state.startCall = true;
    },
  SET_CURRENT_PERSON_NUM_FALSE: (state, payload) => {
    state.currentPersonNum = payload;
    state.startCall = false;
  },
    SET_CASE_OID: (state, payload) => {
        state.caseOid = payload;
    },
    SET_VIRTUAL_BUSINESS_RECORD_OID: (state, payload) => {
      state.currentVirtualBusinessRecordOid = payload;
    }

}

const actions = {

}

export default {
    state,
    mutations,
    actions
}

