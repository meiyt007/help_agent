/** 智审的混入 用于智能登记 窗口办理 已办业务 */

import {
  getComboCaseMaterialListAndAuditResult,
  viewDetailResult,
} from "@/api/onething/comboManager/comboAccept/materialCategory.js";
import ZfImageLoading from "@/components/ZfImageLoading";

import IntelligentAuditDetail from '@/views/onething/comboManager/comboAccept/components/intelligentAuditDetail';

// 审核状态枚举
const RESULT_STATUS_ENMU = {
  /**  审核结果标识 0:通过 */
  RESULT_STATUS_PASS: "0",
  /**  审核结果标识  1:不通过 */
  RESULT_STATUS_NOT_PASS: "1",
  /**  审核结果标识  2:无需智审*/
  RESULT_STATUS_NOT_AUDIT: "2",
  /**  审核结果标识  3:人工审核*/
  RESULT_STATUS_ARTIFICIAL_AUDIT: "3",
  /**  审核结果标识  4:审核通过但部分需要人工审核*/
  RESULT_STATUS_PASS_AND_MUST_ARTIFICIAL: "4",
  /**  审核结果标识  6:附件不符合审核模板*/
  RESULT_STATUS_NO_TEMPLATE: "6",
};

export default {
  name: "IntelligentAudit",
  inheritAttrs: false,
  props: ["caseOid", "comboDirectoryOid", "ruleForm", "comboDirectoryName", "isRqslFlag", "isTempComponent","sxServiceMaterialList", "loginUser",],
  components: { ZfImageLoading, IntelligentAuditDetail },
  data () {
    return {
      ...RESULT_STATUS_ENMU,
      intelligentList: [],
      loading: null,
      intelligentDetailList: [], // 智审详情列表
      intelligentDetailListCache: [], // 智审详情列表副本
      visibleDetail: false, // 智审详情页面
      isManualVerification: false, // 是否是人工核验
      qlCaseMaterial: {},
      onlyShow: false,
    };
  },
  computed: {
    total () {
      return this.intelligentDetailListCache.length;
    },
  },
  mounted () {
    this.isTempComponent && this.getMaterialPrePrialNew();
  },
  methods: {
    setLoading (text = "正在查询智审详情信息") {
      this.loading = this.$loading({
        lock: true,
        text: text,
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });
    },

    previewDetail (data) {
      this.setLoading();
      viewDetailResult({
        caseOid: data.comboCaseOid,
        comboDireOid: this.comboDirectoryOid,
        materialOid: data.materialOid,
        caseMaterialOid: data.caseMaterialOid || "",
      })
        .then(({ code, message, data: _data }) => {
          this.loading.close();
          if (code !== 200) return this.$message.warning(message || _data.message || "未查询到智审详情信息");
          this.intelligentDetailList = _data?.preDetailTrialResultVoList.map(item => {
            return {
              ...item,
              notPassList: item.notPassList.map(child => ({ ...child, reviewPoints: child.reviewPointsName })),
              passList: item.passList.map(child => ({ ...child, reviewPoints: child.reviewPointsName })),
              manualAuditList: item.manualAuditList.map(child => ({ ...child, reviewPoints: child.reviewPointsName, isManual: true })),
            }
          });
          this.intelligentDetailListCache = [...this.intelligentDetailList];
          // 是否是人工核验
          this.isManualVerification = data.resultStatus === this.RESULT_STATUS_ARTIFICIAL_AUDIT;
          // 是否只是展示
          this.onlyShow = data.resultStatus === this.RESULT_STATUS_PASS;
          // 智审失败的数据
          this.qlCaseMaterial = _data.qlCaseMaterial;
          // 默认展示智审失败的
          this.setNoPass();

          if (this.intelligentDetailList.length === 0) {
            this.setAll();
          }

          this.$nextTick(() => {
            this.visibleDetail = true;
          })
        })
        .catch(() => this.loading.close());
    },

    getMaterialPrePrialNew () {
      this.setLoading("正在获取材料智审列表信息");
      getComboCaseMaterialListAndAuditResult({ caseOid: this.caseOid, comboDireOid: this.comboDirectoryOid })
        // getComboCaseMaterialListAndAuditResult({ caseOid: '9ef8754efede4a51924c711cd4678c2a' })
        .then(({ code, data, message }) => {
          this.loading.close();
          if (code !== 200)
            return this.$message.warning(message || "获取材料智审列表失败");
          if (data.success === false)
            return this.$message.warning(
              data.message || "获取材料智审列表失败"
            );
          this.intelligentList = data.comboCaseMaterialList.map((item) => {
            return { ...item };
          });
        })
        .catch(() => this.loading.close());
    },

    setAll () {
      this.intelligentDetailList = [...this.intelligentDetailListCache];
    },

    setNoPass () {
      // 个人工核验 需关注的显示有审核要点的
      this.intelligentDetailList = this.isManualVerification
        ? this.intelligentDetailListCache.filter(item => item.reviewPointsList.length > 0)
        : this.intelligentDetailListCache.filter(item => item.notPassList?.length > 0 || item.manualAuditList?.length > 0);
    }
  },
}
