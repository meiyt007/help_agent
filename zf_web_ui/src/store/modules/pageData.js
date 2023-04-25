/*
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-08-11 09:39:49
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-10-10 15:15:17
 * @FilePath: \zf_web_ui\src\store\modules\pageData.js
 * @Description: 一些页面交互数据
 */
import store from "@/store";

const pageData = {
  state: {
    acceptService: false,
    serviceType: "",
    staffInformation: [], //服务用户列表
    baseUserInfo: "", //办件用户基本信息
    caseOid: "",
    valOids: "",
    cegisterType: "1", //用户类型 1个人 2企业
    situationCheckList: [], //情形选择内容
    currentServiceIndex: 0, //当前服务用户index
    serviceOperateStatus: false,
    reassignmentCurrentServiceIndex: null,
  },
  mutations: {
    setAcceptService: (state, view) => {
      state.acceptService = view;
    },
    setServiceType: (state, view) => {
      state.serviceType = view;
    },
    setBaseUserInfo: (state, view) => {
      state.baseUserInfo = view;
    },
    setCaseOid: (state, view) => {
      state.caseOid = view;
    },
    setValOids: (state, view) => {
      state.valOids = view;
    },
    setCegisterType: (state, view) => {
      state.cegisterType = view;
    },
    setSituationCheckList: (state, view) => {
      state.situationCheckList = view;
    },
    setStaffInformation: (state, view) => {
      state.staffInformation = view;
    },
    setCurrentServiceIndex: (state, view) => {
      state.currentServiceIndex = view;
    },
    setServiceOperateStatus: (state, view) => {
      state.serviceOperateStatus = view;
    },
    setReassignmentCurrentServiceIndex: (state, view) => {
      state.reassignmentCurrentServiceIndex = view;
    },
  },
  actions: {},
};
export default pageData;
