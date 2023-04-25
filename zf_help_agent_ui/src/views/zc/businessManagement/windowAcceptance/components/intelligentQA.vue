<!-- 智能问答 -->
<template>
  <div class="intelligent-qa">
    <div class="intelligent-qa-service">
      【 服务对象：{{
      cegisterType ? (cegisterType === "1" ? "个人" :
      cegisterType === "2" ? "企业法人" :
      cegisterType === "3" ? "事业法人" :
      cegisterType === "4" ? "社会组织法人" :
      cegisterType === "5" ? "非法人企业" :
      cegisterType === "6" ? "行政机关" : "其他组织"
      ) : "#" + cegisterType + "#"
      }}
      】
    </div>
    <el-scrollbar class="common-dialog-content">
      <div class="situation-box">
        <div class="common-dialog--title">情形选择</div>
        <div class="select-list-content">
          <div v-for="(item, index) in selectData" :key="item.index" :class="{ current: index == currentSituationNum }"
            class="select-list-item" @click="handleSituationChange(item, index)">
            <img :src="
                index == currentSituationNum
                  ? require('@/assets/image/intelligent/checked.png')
                  : require('@/assets/image/intelligent/unchecked.png')
              " alt width="16" height="16" />
            <span style="margin-left: 10px">{{ item.title }}</span>
          </div>
        </div>
      </div>
      <div class="common-dialog--title">情形选项信息</div>
      <div class="situation-box-check" v-loading="loading">
        <div class="situation-box-check--title">选项名称：</div>
        <div class="situation-box-check--content">
          <template v-if="situationCheckList.length > 0">
            <div v-for="(data, index) in situationCheckList" :key="index" class="situation-box-check--content-item">
              <span class="situation-box-check--content-item__title">{{
                data.name
                }}</span>
              <!-- 单选 -->
              <template v-if="data.moreStatus === 0">
                <el-radio-group v-model.trim="data.isSelected">
                  <el-radio v-for="item in data.answers" :key="item.oid" :disabled="item.isDisabled" :label="item.oid"
                    @change="handleQA(item.oid, item.titleOid)">{{ item.name }}</el-radio>
                </el-radio-group>
              </template>
              <!-- 多选 -->
              <template v-if="data.moreStatus === 1">
                <el-checkbox-group v-model.trim="data.isSelected">
                  <el-checkbox v-for="item in data.answers" :disabled="item.isDisabled" :key="item.oid" :id="item.oid"
                    :label="item.oid" @change="handleQA(item.oid, item.titleOid)">{{ item.name }}</el-checkbox>
                </el-checkbox-group>
              </template>
            </div>
          </template>
          <div v-else style="
              width: 100%;
              height: 100%;
              display: flex;
              align-items: center;
              justify-content: center;
            ">
            暂无数据
          </div>
        </div>
      </div>
      <div class="situation-box">
        <div class="common-dialog--title">所需材料</div>
        <div class="tableArea" v-loading="tableLoading">
          <el-table border :data="materialTableData" style="width:100%" :show-header="false">
            <el-table-column type="index" label="序号" width="50"></el-table-column>
            <template v-for="(item, index) in materialColumn">
              <el-table-column :label="item.label" :prop="item.prop"></el-table-column>
            </template>
            <el-table-column label="材料要求" prop="materialRequirements" align="center">
              <template slot-scope="scope">
                <div class="content">
                  <div class="contentData">
                    <div class="leftBtn requirementsContent" v-show="scope.row.reviewPointsFlag === 1"
                      @click="previewDetail(scope.row)">
                      <img :src="
                          require('@/assets/image/intelligent/QA/reviewPoints.png')
                        " alt="" />
                      <p>审查要点</p>
                    </div>
                    <div class="centerBtn requirementsContent" v-show="scope.row.materialSampleAddr"
                       @click="viewMaterialAddr(
                          scope.row.materialSampleAddr
                        )
                      ">
                      <img :src="
                          require('@/assets/image/intelligent/QA/preview.png')
                        " alt="" />
                      <p>样表预览</p>
                    </div>
                    <div class="rightBtn requirementsContent" v-show="scope.row.materialEmptyAddr"
                      @click="downLoadMaterialAddr(scope.row.materialEmptyAddr)">
                      <img :src="
                          require('@/assets/image/intelligent/QA/download.png')
                        " alt="" />
                      <p>样表下载</p>
                    </div>
                  </div>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </el-scrollbar>
    <div class="common-dialog-footer">
      <el-button @click="cancelStep" style="background: #E7EDF3!important;color: #525960;">取消</el-button>
      <!-- <el-button @click="oneTimeNotice" style="background: #4B7AC8!important;color: #FFFFFF;" size="mini">一次性告知单</el-button> -->
      <el-button type="primary" @click="beforeNextStep">下一步</el-button>
    </div>
    <!-- 智能问答审查要点 -->
    <IntelligentAuditDetail v-if="visibleDetail" :visibleDetail.sync="visibleDetail"
      :intelligentDetailList="intelligentDetailList" :total="total" :isManualVerification="isManualVerification"
      :serviceName="serviceName" :caseOid="caseOid" :serviceOid="serviceOid" :qlCaseMaterial="qlCaseMaterial"
      :onlyShow="onlyShow" @setAll="setAll" @setNoPass="setNoPass" @refreshIntelligentList="
        () => {
          visibleDetail = false;
          getMaterialPrePrialNew();
        }
      " />
    <!-- 服务对象弹框组件 -->
    <ServiceObjectDialog v-if="visibleDialog" :visibleDialog.sync="visibleDialog" :serviceObject="cegisterType"
      @setServiceObject="
        data => {
          $emit('setServiceObject', data);
        }
      " />
    <el-dialog v-dialog-drag title="文件预览" :visible.sync="view.show" v-for="view in viewDialogOptions"
      :key="view.attaOid" @close="closeFileViewNew" custom-class="preview-dialog" width="1158px" height="800px"
      :scrollbar="view.ext !== 'pdf'" append-to-body>
      <sx-file-view :attaOid="view.attaOid" @father-click="closeFileViewNew" />
    </el-dialog>
  </div>
</template>

<script>
import {
  save,
  getSituationOpinionList,
  getSxServiceHotSituations,
  getSxServiceTitlesNoRelation,
  getSxServiceTitlesByRelationOids,
  getSituationMaterialListByOids
} from "@/api/zc/businessManagement/windowAcceptance";

import {
  formatSituationCheckList,
  getAllSelectedOids,
  getAllSelectedOidsList,
  checkAllSituationsSelected,
  getQlTitleValList,
  removeDuplicate
} from "./util.js";

// import { getIntelligentAnswer } from "@/api/zc/intelligentAq/intelligentAq";
import sxFileView from "@/views/zc/qdgl/sxFileView";
import ServiceObjectDialog from "./serviceObjectDialog.vue";
import { viewDetailRefinedMaterial } from "@/api/materialCategory.js";
import Verfication from "./verfication.vue";
import { downloadMaterial } from "@/api/zc/qdgl/materialDetails.js";
import { querySxServicePrecheckList } from "@/api/zc/qdgl/sxService";
import IntelligentAuditDetail from "@/views/zc/businessManagement/windowAcceptance/components/intelligentAuditDetail";
import { mapGetters } from "vuex";
export default {
  name: "IntelligentQA",
  inheritAttrs: false,
  props: {
    serviceOid: {
      type: String,
      default: ""
    },
    sxServiceMaterialList: {
      type: Array,
      delete: []
    },
    // 智能问答 通过语音 带过来的 oid
    situationAnswerList: {
      type: Array,
      default: () => []
    },
    cegisterType: String,
    serviceRoot: Array,
    comeFormArtific: Boolean,
    serviceName: {
      type: String,
      default: ""
    }
  },
  components: {
    ServiceObjectDialog,
    Verfication,
    IntelligentAuditDetail,
    sxFileView
  },
  data() {
    return {
      selectData: [
        {
          index: 0,
          title: "默认自定情形"
        }
      ],
      //办件与情形标题选项关系
      qlCaseTitleValList: [],
      currentSituationNum: 0,
      //当前选中的热门情形名称
      currentHotSituationName: "",
      // 情景选项信息
      situationCheckList: [],
      // 缓存办件与情形标题选项关系, 用于对比是否发生过情形修改
      qlCaseTitleValCacheList: [],
      enterNum: 0, // 进入该组件的次数
      visibleDialog: false,

      loading: false,
      tableData: [],
      checkChangeTypeList: [],
      changeTypeList: [
        { label: "企业名称", value: "enterpriseName" },
        { label: "企业住所", value: "enterpriseDomicile" },
        { label: "注册资本", value: "registeredCapital" },
        { label: "股东名称", value: "nameOfShareholder" },
        { label: "股东/股权", value: "shareholdersEquity" },
        { label: "法定代表人", value: "legalRepresentative" },
        { label: "经营范围", value: "natureOfBusiness" },
        { label: "经营期限", value: "operatingPeriod" },
        { label: "董事", value: "director" },
        { label: "章程", value: "constitution" },
        { label: "监事", value: "supervisor" },
        { label: "经理", value: "manager" }
      ],
      materialTableData: [],
      chooseHandlingMethod: "entrust",
      materialColumn: [{ label: "材料名称", prop: "materialName" }],
      visibleDetail: false,
      intelligentDetailList: [], // 要点查看详情列表
      intelligentDetailListCache: [], // 要点查看详情列表副本
      isManualVerification: false, // 是否是人工核验
      onlyShow: true,
      qlCaseMaterial: {},
      viewDialogOptions: [],
      fileAttaOid: "",
      caseOid: "",
      //serviceName: "",
      tableLoading: false,
      isQlCaseChanged: false,
      saveStepInfo:false
      // situationAnswerList: ["2c287b8b79f39489017a2227abad1a66", "2c287b8b79f39489017a1d1793491756"]
    };
  },
  computed: {
    total() {
      return this.intelligentDetailListCache.length;
    },
    saveComponentIndex() {
      return this.$store.state.config.saveComponentIndex;
    },
    ...mapGetters(["currentPersonNum", "startCall"])
  },
  mounted() {
    // getIntelligentAnswer("/r").then(() => {
    //   getIntelligentAnswer('个体新办道路货物运输经营许可').then(({ data }) => {
    //     this.situationAnswerList = Object.keys(JSON.parse(data)?.solutionDict);
    //   })
    // })
    // 热门情形
    this.getSxServiceHotSituations();
    this.$getResponse(
      querySxServicePrecheckList({ serviceOid: this.serviceOid }),
      (error, res) => {
        if (error || res.code !== 200) return;
        this.tableData = res.data
          .sort((a, b) => a.sort - b.sort)
          .map(item => ({ ...item, switch: false }));
      }
    );
    if (this.comeFormArtific) {
      this.openServiceObject(false);
    }
  },
  activated() {
    this.enterNum++;
  },
  deactivated() {
    this.qlCaseTitleValList = getQlTitleValList(
      this.situationCheckList,
      this.selectData[this.currentSituationNum].oid
    );
    if (this.enterNum !== 1) {
      // 如果没有选中情形 第一次为true 第二次为false
      if (
        this.qlCaseTitleValList.length === 0 &&
        this.qlCaseTitleValCacheList.length === 0 &&
        this.enterNum > 1
      ) {
        if (this.isQlCaseChanged) {
          this.$emit("setIsQlCaseChanged", true);
        } else {
          this.$emit("setIsQlCaseChanged", false);
        }
      } else {
        // 在选中情形的情况下 如果相等 说明没有改变
        const isQlCaseChanged = !(
          JSON.stringify(this.qlCaseTitleValList) ===
          JSON.stringify(this.qlCaseTitleValCacheList)
        );
        if (isQlCaseChanged && !this.isQlCaseChanged) {
          this.$emit("setIsQlCaseChanged", false);
        } else {
          this.$emit("setIsQlCaseChanged", true);
        }
      }
    }
    this.qlCaseTitleValCacheList = [...this.qlCaseTitleValList];
    this.$emit("setQlCaseTitleValList", this.qlCaseTitleValList);
    this.$emit("setSituationCheckList", this.situationCheckList);
    this.$emit("setHotSituationName", this.currentHotSituationName);
  },
  watch: {
    saveComponentIndex: {
      handler(val) {
        if (val === 1) {
          this.initCase();
        }
      }
    },
    cegisterType: {
      handler(val) {
        this.isQlCaseChanged = true;
      }
    }
  },
  methods: {
    // 热门情形
    async getSxServiceHotSituations() {
      this.loading = true;
      const { code, data, message } = await getSxServiceHotSituations({
        serviceOid: this.serviceOid
      });
      if (code !== 200) {
        this.loading = false;
        return this.$message.warning(message || "热门情形查询失败");
      }

      data.forEach(item => {
        this.selectData.push({
          ...item,
          title: item.situationName
        });
      });

      // 初始默认选中默认情形
      this.getSxServiceTitlesNoRelation();
    },

    // 点击热门情形选择
    async handleSituationChange(item, idx) {
      this.loading = true;
      this.materialTableData = [];
      this.currentSituationNum = idx;
      this.situationCheckList = [];
      this.currentHotSituationName = item.situationName;

      // 热门情形
      if (item.oid) {
        const { data, code, message } = await getSituationOpinionList(item.oid);

        if (code !== 200) {
          this.loading = false;
          return message && this.$message.warning(message);
        }
        this.loading = false;
        this.situationCheckList = formatSituationCheckList(
          data?.sxServiceSituationOption?.sxServiceOptionTitles ?? [],
          true
        );
        await this.getMaterialList();
        return;
      }
      // 默认情形
      this.getSxServiceTitlesNoRelation();
    },

    // 获取默认情形下 选项信息
    async getSxServiceTitlesNoRelation() {
      this.loading = true;
      this.materialTableData = [];
      const { data, code, message } = await getSxServiceTitlesNoRelation({
        serviceOid: this.serviceOid
      });
      if (code !== 200) {
        this.loading = false;
        return message && this.$message.warning(message);
      }
      this.loading = false;
      this.situationCheckList = formatSituationCheckList(data);
      await this.getMaterialList();
      await this.handleIntelligentQaWithSituation();
    },

    // 处理智能咨询和情形交互问题
    async handleIntelligentQaWithSituation() {
      // 初始判断是否有智能问答关联的情形信息
      if (this.situationAnswerList.length > 0) {
        // check question's answers has been exit in situationAnswerList
        let target = null;
        for (const item of this.situationCheckList) {
          target = item.answers.find(child => {
            if (this.situationAnswerList.includes(child.oid)) {
              this.situationAnswerList = this.situationAnswerList.filter(
                i => i !== child.oid
              );
              return true;
            }
            return false;
          });

          if (target) {
            break;
          }
        }
        if (target) {
          const t = this.situationCheckList.find(
            item => item.oid === target.titleOid
          );
          if (t) {
            if (typeof t.isSelected === "string") {
              t.isSelected = target.oid;
            } else {
              t.isSelected.push(target.oid);
            }
          }
          return await this.handleQA(target.oid, target.titleOid);
        }
      }
    },

    // 查询出默认情形相关联的标题信息
    async getSxServiceTitlesByRelationOids(oid, titleOid) {
      const oids = getAllSelectedOids(this.situationCheckList);
      const { data, code, message } = await getSxServiceTitlesByRelationOids({
        serviceOid: this.serviceOid,
        valOids: oids,
        currentOid: oid,
        currentTitleOid: titleOid,
        rootTitleOid: this.situationCheckList[0].oid
      });
      if (code !== 200) return message && this.$message.warning(message);
      // this.situationCheckList = formatSituationCheckList(data);
      this.situationCheckList = removeDuplicate(formatSituationCheckList(data));
      await this.getMaterialList();
      await this.handleIntelligentQaWithSituation();
    },

    /**
     * 点击单选事件 处理问答事件
     * @param {string} oid
     * @param {string} titleOid
     */
    async handleQA(oid, titleOid) {
      return await this.getSxServiceTitlesByRelationOids(oid, titleOid);
    },

    async beforeNextStep() {
      if (this.currentSituationNum === 0) {
        const isAllFill = checkAllSituationsSelected(this.situationCheckList);

        if (isAllFill.name) {
          return this.$message.warning(`${isAllFill.name}为必选项!`);
        }
      }
      this.saveStepInfo = true
      this.initCase()
      this.$emit("nextStep", 2);
    },

    cancelStep() {
      this.$emit("close");
    },

    /** 点击选项值 选项有精细化材料展示精细化材料 */
    async getMaterialList() {
      this.tableLoading = true;
      const allCheckItem = getAllSelectedOidsList(this.situationCheckList);
      //根据被选中的选项获取关联材料
      const { data, code, message } = await getSituationMaterialListByOids(
        this.serviceOid,
        allCheckItem
      );
      if (code !== 200) {
        this.tableLoading = false;
        return message && this.$message.warning(message);
      }
      this.tableLoading = false;
      this.materialTableData = data;
      this.$emit("setSxServiceMaterialList", data || []);
    },

    openServiceObject(isTip = true) {
      const [isPerson, isLegal] = this.serviceRoot;
      if (isPerson && isLegal) {
        this.visibleDialog = true;
      } else {
        isTip &&
          this.$message.warning(
            `该事项仅支持${isPerson ? "个人" : "法人"}办理`
          );
      }
    },
    setLoading(text = "正在查询审查要点信息") {
      this.loading = this.$loading({
        lock: true,
        text: text,
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
    },
    //智能问答材料审查要点
    previewDetail(data) {
      // this.serviceName = data.serviceName;
      viewDetailRefinedMaterial({
        materialOid: data.materialOid
      })
        .then(({ code, message, data: _data }) => {
          if (code !== 200)
            return this.$message.warning(
              message || _data.message || "未查询到审查要点信息"
            );
          this.intelligentDetailList = _data?.preDetailTrialResultVoList.map(
            item => {
              return {
                ...item,
                notPassList: item.notPassList.map(child => ({
                  ...child,
                  reviewPoints: child.reviewPointsName
                })),
                passList: item.passList.map(child => ({
                  ...child,
                  reviewPoints: child.reviewPointsName
                })),
                manualAuditList: item.manualAuditList.map(child => ({
                  ...child,
                  reviewPoints: child.reviewPointsName,
                  isManual: true
                }))
              };
            }
          );
          this.intelligentDetailListCache = [...this.intelligentDetailList];
          // 是否是人工核验
          this.isManualVerification =
            data.resultStatus === this.RESULT_STATUS_ARTIFICIAL_AUDIT;
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
          });
        })
        .catch(err => {
          console.log(err);
        });
    },
    setAll() {
      this.intelligentDetailList = [...this.intelligentDetailListCache];
    },
    setNoPass() {
      // 个人工核验 需关注的显示有审核要点的
      this.intelligentDetailList = this.isManualVerification
        ? this.intelligentDetailListCache.filter(
            item => item.reviewPointsList.length > 0
          )
        : this.intelligentDetailListCache.filter(
            item =>
              item.notPassList?.length > 0 || item.manualAuditList?.length > 0
          );
    },
    //样表预览
    viewMaterialAddr(attaOid, addr) {
      if (attaOid) {
        let item = {
          show: true,
          attaOid: attaOid,
          ext: addr?.split?.(".")?.splice?.(-1)?.[0]
        };
        this.viewDialogOptions.push(item);
      } else {
        this.$message.warning("暂无材料查看！");
      }
    },
    //样表下载
    downLoadMaterialAddr(attaOid) {
      if (attaOid) {
        downloadMaterial(attaOid);
      } else {
        this.$message.warning("暂无材料下载！");
      }
    },

    //预览附件
    viewFile(attaOid) {
      this.fileAttaOid = attaOid;
      this.showFile = true;
    },
    //关闭预览附件
    closeFileViewNew() {
      this.viewDialogOptions.pop();
    },
    //智能问答暂存
    initCase() {
      // console.log('%c [qlCaseTitleValList]:', 'color:red;font-weight:700;', this.$attrs.qlCaseTitleValList);
      const loading = this.$loading({
        lock: true,
        text: "正在保存信息",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
      return save({
        id: "",
        caseOid: "",
        applyOid: "",
        extOid: "",
        cegisterType: this.$attrs.cegisterType,
        serviceOid: this.serviceOid,
        caseStatus:'0',
        sourceApp:1,
        projectName:this.serviceName,
        caseNumber: "",
        sxServiceMaterialList: this.sxServiceMaterialList,
        qlCaseTitleValList: this.$attrs.qlCaseTitleValList,
        currentPersonNum: this.startCall ? this.currentPersonNum : ""
      })
        .then(response => {
          loading.close();
          if (response.code === 200 && response.data) {
            // this.$message.success("保存办件成功！");
            this.$emit("setData", {
              caseOid: response.data.caseOid,
              caseNumber: response.data.caseNumber,
              serviceTypeName: response.data.serviceTypeName,
              caseMaterialOids: response.data.caseMaterialOids,
              createDate: response.data.createDate
            });

            this.$store.commit("SET_CASE_OID", response.data.caseOid);
            this.$store.commit("setSaveComponentIndex", 0);
            if(!this.saveStepInfo){
           this.$emit("close");
            }
           this.saveStepInfo = false
          } else {
            this.$store.commit("setSaveComponentIndex", 0);
            this.$message.warning("保存办件失败！");
            return false;
          }
        })
        .catch(() => {
          this.$store.commit("setSaveComponentIndex", 0);
          loading.close();
        });
    }
  }
};
</script>

<style lang="scss" scoped>
.intelligent-qa {
  position: relative;
  .intelligent-qa-service {
    position: absolute;
    right: 0;
    top: 0;
    width: auto;
    height: 48px;
    padding: 0 20px;
    background: #e1ebff;
    border-radius: 3px 3px 3px 24px;
    font-size: 14px;
    font-family: PingFang SC;
    font-weight: 500;
    color: #35528a;
    text-align: center;
    line-height: 48px;
    z-index: 1;
  }
  .situation-box {
    margin-bottom: 35px;
    &-check {
      display: flex;
      min-height: 100px;
      margin-bottom: 35px;
      padding-left: 10px;
      &--title {
        width: 168px;
        background: #edf0f5;
        border: 1px solid #e0e6f0;
        display: flex;
        align-items: center;
        justify-content: flex-end;
      }

      &--content {
        flex: 1;
        border: 1px solid #e0e6f0;
        border-left: unset;
        display: flex;
        align-items: center;
        flex-direction: column;

        &-item {
          display: flex;
          flex-direction: column;
          padding: 25px 0 20px 35px;
          width: 100%;
          border-bottom: 1px solid #e0e6f0;

          &:last-child {
            border-bottom: unset;
          }

          &__title {
            margin-bottom: 20px;
            font-size: 14px;
            font-weight: bold;
            color: #121b2f;
          }
        }
      }
    }
    .select-content {
      width: 100%;
      height: 216px;
      padding-left: 10px;
      display: flex;
      align-items: center;
      justify-content: flex-start;
      .left {
        width: 168px;
        height: 100%;
        background: #edf0f5;
        border: 1px solid #e0e6f0;
        display: flex;
        align-items: center;
        justify-content: center;
        p {
          padding: 0;
          margin: 0;
          font-size: 14px;
          font-family: Microsoft YaHei;
          font-weight: 400;
          color: #2a344c;
        }
      }
      .right {
        width: calc(100% - 168px);
        height: 100%;
        box-sizing: border-box;
        border-top: 1px solid #e0e6f0;
        border-right: 1px solid #e0e6f0;
        border-bottom: 1px solid #e0e6f0;
        .question {
          margin: 0;
          padding: 0;
          margin-bottom: 22px;
          font-size: 14px;
          font-family: Microsoft YaHei;
          font-weight: bold;
          color: #121b2f;
        }
        .right-top {
          width: 100%;
          height: 123px;
          box-sizing: border-box;
          border-bottom: 1px solid #e0e6f0;
          padding: 21px 91px 0 52px;
          .el-checkbox-group {
            ::v-deep .el-checkbox {
              margin-bottom: 18px !important;
            }
          }
        }
        .right-below {
          width: 100%;
          height: 93px;
          padding: 21px 91px 0 52px;
          box-sizing: border-box;
        }
      }
    }
    ::v-deep .el-table {
      border-radius: 0!important;
      .el-table__header-wrapper {
        .el-table__header {
          thead {
            tr {
              .el-table__cell {
                background-color: #edf0f5 !important;
                border-color: #e0e6f0 !important;
              }
            }
          }
        }
      }
      .el-table__body-wrapper {
        .el-table__body {
          tbody {
            .el-table__row {
              .el-table__cell {
                background-color: #fff !important;
                // border-color: #e0e6f0 !important;
                border: none;
                border-bottom: 1px solid  #e0e6f0;
              }
            }
          }
        }
      }
    }

    .content {
      width: 100%;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      .contentData {
        width: 80%;
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: flex-end;
      }
      .requirementsContent {
        cursor: pointer;
        width: 105px;
        height: 32px;
        padding: 9px 12px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        border-radius: 5px;
        p {
          padding: 0;
          margin: 0;
          font-size: 14px;
          font-family: Microsoft YaHei;
          font-weight: 400;
        }
      }
      .leftBtn {
        margin-right: 20px;
        border: 1px solid #2e7dff;
        img {
          width: 19px;
          height: 18px;
        }
        p {
          color: #4933d0;
        }
      }
      .centerBtn {
        margin-right: 20px;
        border: 1px solid #664dff;
        img {
          width: 20px;
          height: 16px;
        }
        p {
          color: #4933d0;
        }
      }
      .rightBtn {
        border: 1px solid #1497af;
        img {
          width: 22px;
          height: 16px;
        }
        p {
          color: #0a93ac;
        }
      }
    }
    .tableArea {
      width: 100%;
      height: 100%;
      padding-left: 10px;
      box-sizing: border-box;
    }
  }

  .select-list-content {
    display: flex;
    flex-wrap: wrap;
    padding-left: 10px;
    .select-list-item {
      margin-right: 15px;
      background: rgba(237, 240, 244, 0.55);
      border: 1px solid #c8cdd3;
      border-radius: 5px;
      display: flex;
      justify-content: center;
      align-items: center;
      font-size: 12px;
      font-weight: 400;
      color: #2a344c;
      cursor: pointer;
      padding: 10px 20px;

      &.current {
        height: 37px;
        background: #2e7dff;
        color: #ffffff;
      }
    }
  }
}
</style>
