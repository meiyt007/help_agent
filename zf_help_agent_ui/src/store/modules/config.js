import { getConfigListByParentCode, getConfigByCode } from "@/api/sys/config";

const CODE = {
  /** 硬件参数 */
  device: "DEVICE",
  /** 智能咨询 */
  znzx: "ZNZX",
  /** 无感评价 */
  wgpj: "WGPJ",
  /** 信息确认时间 */
  xxqr: "XXQR",
  /** 评价倒计时时间 */
  pjdj: "PJDJ",
  /** 无感评价图片 */
  wgtp: "WGTP",
  /** 材料智审 */
  clzs: "CLZS",
  /** 材料分类 */
  /** 材料分类 */
  clfl: "CLFL"
};

/** 开关编码 */
const SWITCH_CODE_TRUE = "0";
const SWITCH_CODE_FALSE = "1";

const formatMap = data => {
  return data.reduce((prev, cur) => {
    prev[cur.code] = cur.value;
    return prev;
  }, {});
};

/**
 * isAble: 0 是禁用 1 是开启
 */

export default {
  state: {
    /** 硬件参数 */
    deviceMap: {},
    /** 智能咨询 */
    znzxFlag: false,
    /** 无感评价 */
    wgpjFlag: false,
    /** 信息确认时间 默认30s */
    xxqrTime: 30,
    /** 评价倒计时时间 默认30s */
    pjdjTime: 60,
    /** 无感图片 */
    wgtpFlag: false,
    /** 材料智审 */
    clzsFlag: false,
    /** 材料分类 */
    clflFlag: true,
    // 窗口暂存
    saveComponentIndex: 0
  },
  mutations: {
    /** 硬件参数 */
    SET_DEVICE_MAP(state, payload) {
      // localStorage.setItem("__DEVICE_MAP__", JSON.stringify(payload));
      state.deviceMap = payload;
    },
    /** 智能咨询 */
    SET_ZNZX_MAP(state, payload) {
      state.znzxFlag = payload === SWITCH_CODE_TRUE;
    },
    /** 无感评价 */
    SET_WGPJ_MAP(state, payload) {
      state.wgpjFlag = payload === SWITCH_CODE_TRUE;
    },
    /** 信息确认时间 默认30s */
    SET_XXQR_MAP(state, payload) {
      state.xxqrTime = payload;
    },
    /** 评价倒计时时间 默认60s */
    SET_PJDJ_MAP(state, payload) {
      state.pjdjTime = payload;
    },
    /** 无感图片*/
    SET_WGTP_MAP(state, payload) {
      state.wgtpFlag = payload === SWITCH_CODE_TRUE;
    },
    /** 材料智审 */
    SET_CLZS_MAP(state, payload) {
      state.clzsFlag = payload === SWITCH_CODE_TRUE;
    },
    /** 材料分类*/
    SET_CLFL_MAP(state, payload) {
      state.clflFlag = payload === SWITCH_CODE_TRUE;
    },
    setSaveComponentIndex(state, preview) {
      state.saveComponentIndex = preview;
    }
  },
  actions: {
    /** 统一调用 */
    getAllConfig({ dispatch }) {
      return Promise.all();
    },
  }
};
