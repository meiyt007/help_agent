<!-- 材料电子化 -->
<template>
  <div class="electronization-of-materials" v-loading="loading">
    <el-scrollbar class="common-dialog-content" ref="scrollbar">
      <div
        class="common-dialog--header"
        style="
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 20px;
        "
      >
        <div class="common-dialog--title" style="margin: unset">申请材料信息</div>
        <el-button type="primary" size="small" plain :disabled="isMatchAllElec" @click="hanldeMatchAllElec">智能匹配证照库</el-button>
      </div>
      <table class="zf-zc-table--common zf-zc-table--td-center">
        <colgroup>
          <col width="5%" />
          <col width="20%" />
          <col width="10%" />
          <col width="15%" />
          <col width="50%" />
        </colgroup>
        <tr>
          <th>序号</th>
          <th>材料名称</th>
          <th>材料类型(份数)</th>
          <th>材料样本</th>
          <th>提交方式</th>
        </tr>
        <template v-if="sxServiceMaterialAttaList.length > 0">
          <tbody v-for="(item, index) in sxServiceMaterialAttaList" :key="index">
            <tr v-if="!item.isElecBill">
              <td>{{ item.index }}</td>
              <td style="text-align: left !important">{{ item.materialName }}</td>
              <td>
                <template>
                  <span v-if="item.materialType == '0'">原件</span>
                  <span v-if="item.materialType == '1'">复印件</span>
                  <span v-if="item.materialType == '2'">原件和复印件</span>
                  <span v-if="item.materialType == '3'">原件或复印件</span>
                  ({{ item.paperNumber }})
                </template>
              </td>
              <td>
                <div class="handle-btn">
                  <el-button type="text" size="small" @click="downLoadMaterialAddr(item.materialSampleAddr)">
                    <img src="@/assets/image/intelligent/download.png" alt width="18" height="16" />
                    下载
                  </el-button>
                  <el-button
                    type="text"
                    size="small"
                    @click="
                      viewMaterialAddr(
                        item.materialSampleAddr,
                        item.materialSampleAddrYl
                      )
                    "
                  >
                    <img src="@/assets/image/intelligent/view.png" alt width="18" height="16" />
                    查看
                  </el-button>
                </div>
              </td>
              <td>
                <el-radio-group
                  v-model="item.collectionType"
                  @change="
                    () => {
                      handleCollectionTypeChange(item);
                    }
                  "
                >
                  <el-radio :label="'1'">纸质收取</el-radio>
                  <el-radio
                    :disabled="
                      item.sxMaterials.materialFormat != '7' &&
                      item.sxMaterials.mustFlag != '3'
                    "
                    :label="'7'"
                  >告知承诺</el-radio>
                  <el-radio :disabled="item.rqbzFlag != '1'" :label="'4'">容缺</el-radio>
                  <el-radio :label="'3'">扫描</el-radio>
                  <el-radio :disabled="!item.elecBillOid || !item.directoryObj" :label="'5'">证照库</el-radio>
                  <el-radio :label="'2'">附件上传</el-radio>
                </el-radio-group>
              </td>
            </tr>
            <tr v-else>
              <td colspan="5" class="is-elec-bill">
                <div class="is-elec-bill--content">
                  <div v-if="!item.elecLicenName"></div>
                  <div v-if="item.elecLicenName && !item.elemLicenseOid" style="color: #c56412">未匹配到相应证照</div>
                  <div
                    v-if="item.elecLicenName && item.elemLicenseOid"
                    class="is-elec-bill-content-link"
                    @click="viewElemsInfo(item.elemLicenseOid)"
                  >
                    <span>{{ item.elecLicenName }}-{{ item.elecLicenNumber }}</span>
                  </div>
                  <div class="is-elec-bill--content-btn">
                    <el-button type="primary" size="mini" plain @click="viewElemsInfo(item.elemLicenseOid)">预览</el-button>

                    <el-button
                      type="primary"
                      size="mini"
                      plain
                      @click="getElecLicen(item)"
                    >{{ item.elemLicenseOid ? '重新获取' : '获取证照' }}</el-button>
                  </div>
                </div>
              </td>
            </tr>
            <tr v-if="item.collectionType == 2">
              <td colspan="5" style="background-color: #fafbfc">
                <div class="handle-data">
                  <div class="handle-data-list">
                    <div class="handle-data-list-item" v-for="(dataAtta, idx) in item.attaList" :key="idx">
                      <div class="grid-content qdcg-text">
                        <div
                          @click="viewFile(dataAtta.attaOid)"
                          class="handle-data-list-item--link"
                        >材料名称 - {{ dataAtta.originName }}</div>
                      </div>
                      <div>
                        <el-button
                          type="text"
                          icon="el-icon-view"
                          v-if="item.collectionType == 2"
                          @click="viewFile(dataAtta.attaOid)"
                        >查看</el-button>
                        <el-button
                          type="text"
                          icon="el-icon-delete"
                          v-if="item.collectionType == 2"
                          @click="deleteMaterialAtta(idx, index)"
                        >删除</el-button>
                      </div>
                    </div>
                  </div>
                  <el-upload
                    class="upload-demo"
                    action
                    :http-request="uploadFiles"
                    :before-upload="beforeUpload"
                    :on-error="uploadError"
                    :file-list="fileList"
                    :show-file-list="showFileList"
                    accept=".pdf, .PDF"
                  >
                    <el-button
                      v-if="item.collectionType == 2"
                      size="mini"
                      type="primary"
                      icon="el-icon-upload"
                      @click="pushMaterialOid(item.caseMaterialOid, index)"
                    >点击上传</el-button>
                  </el-upload>
                </div>
              </td>
            </tr>
          </tbody>
        </template>
        <tr v-else>
          <td colspan="5">暂无数据</td>
        </tr>
      </table>

      <!-- 容缺补正 -->
      <table v-if="isExitRqbzTime" cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
        <colgroup>
          <col width="15%" />
          <col width="35%" />
          <col width="15%" />
          <col width="35%" />
        </colgroup>
        <tr>
          <td>
            <i class="require">*</i>
            <b>容缺补正时间：</b>
          </td>
          <td colspan="3">
            <el-date-picker
              v-model.trim="rqbzDueDate"
              type="date"
              value-format="yyyy-MM-dd"
              :picker-options="optionDate"
              placeholder="选择容缺补正时间"
              @change="handleDueDateChange"
            />
          </td>
        </tr>
      </table>
    </el-scrollbar>
    <div class="common-dialog-footer">
      <el-button type="primary" @click="$emit('lastStep', 2)">上一步</el-button>
      <el-button type="primary" :disabled="loading" @click="nextStep">下一步</el-button>
    </div>
    <GpyUplaodDialog
      v-if="dialogVisible"
      v-bind="$attrs"
      :caseOid="caseOid"
      :comboDirectoryOid="comboDirectoryOid"
      :comboDirectoryName="comboDirectoryName"
      :dialogVisible.sync="dialogVisible"
      :materialAttaList="materialAttaList"
      @nextStep="
        id => {
          $emit('nextStep', id);
        }
      "
    />

    <!--引入文件的预览弹出层-->
    <el-dialog
      v-dialog-drag
      title="文件预览"
      :visible.sync="view.show"
      v-for="view in viewDialogOptions"
      :key="view.attaOid"
      @close="closeFileViewNew"
      custom-class="preview-dialog"
      width="1158px"
      height="800px"
      :scrollbar="view.ext !== 'pdf'"
      append-to-body
    >
      <sx-file-view :attaOid="view.attaOid" @father-click="closeFileViewNew" />
    </el-dialog>

    <!-- 电子证照 -->
    <el-dialog
      v-dialog-drag
      title="引用电子证照库"
      :visible.sync="elecVisible"
      width="500px"
      append-to-body
      center
      @close="handleElecClose"
    >
      <el-form ref="elecForm" label-width="100px" :model="elecForm" :rules="rules">
        <el-divider v-if="clickHandleMatchAllElec && isPersonElecShow">个人证照信息录入</el-divider>
        <template v-if="isPersonElecShow">
          <el-form-item label="姓名：" prop="userName">
            <el-input v-model.trim="elecForm.userName" placeholder="请输入姓名"></el-input>
          </el-form-item>
          <el-form-item label="证件号：" prop="idCard">
            <el-input v-model.trim="elecForm.idCard" placeholder="请输入身份证号码"></el-input>
          </el-form-item>
        </template>
        <el-divider v-if="clickHandleMatchAllElec && isLegalElecShow">法人证照信息录入</el-divider>
        <template v-if="isLegalElecShow">
          <el-form-item label="单位名称：" prop="legalUserName">
            <el-input v-model.trim="elecForm.legalUserName" placeholder="请输入持证单位名称"></el-input>
          </el-form-item>
          <el-form-item label="证件号：" prop="legalIdCard">
            <el-input v-model.trim="elecForm.legalIdCard" placeholder="请输入企业信用代码或者组织机构代码"></el-input>
          </el-form-item>
        </template>
      </el-form>
      <div slot="footer" style="text-align: center">
        <el-button v-if="isPersonElecShow" type="primary" @click="scanCard">扫描身份证</el-button>
        <el-button type="primary" @click="handelElecFormSubmit">提交</el-button>
      </div>
    </el-dialog>

    <!--引入文件的预览弹出层-->
    <el-dialog
      v-dialog-drag
      title="文件预览"
      :visible.sync="showFile"
      v-if="showFile"
      @close="closeFileView"
      width="1100px"
      scrollbar
      height="700px"
      append-to-body
    >
      <case-file-view-industry :attaOid="fileAttaOid" @father-click="closeFileView"></case-file-view-industry>
      <div slot="footer" class="zf-text-center">
        <el-button @click="showFile = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import GpyUplaodDialog from "../dialogs/gpy-upload-dialog.vue";
import materialFileView from "@/views/zc/businessManagement/windowAcceptance/materialFileView";
import sxFileView from "@/views/onething/sxpz/comboDirectory/comboDireFileView";
import fileView from "@/views/common/fileView";
import {getIndustryCaseMaterialList,saveIndustryCaseMaterialAttaList,updateIndustryCaseMaterialList} from '@/api/onelicence/industryManager/industryCaseAccept/materialClassifiler'
import {queryindustryElecLicenseByDirCode} from '@/api/onelicence/industryManager/industryCaseAccept/elemLice.js'
import {
  getElecLicenUrl,
  queryElecLicenseType
} from "@/api/zc/businessManagement/elemLice";
import { getIdCardInfo, openIdCard, findIdCard } from "@/api/sys/hardwareScan";
import { downloadMaterial } from "@/api/zc/qdgl/materialDetails.js";
import { uploadCaseMaterialFile } from "@/api/onelicence/industryManager/industryCaseAccept/initIndustryCase";
import caseFileViewIndustry from "@/views/onelicence/industryManager/industryCaseAccept/industryCaseFileView.vue";

import { querySaveCallRecordByOid } from "@/api/zc/qhjh/qhjh";
import { mapGetters } from "vuex";

import moment from "moment";

import { awaitPromise } from "@/utils/utils";
import GPYDrive from "@/api/handwareDrive.js";
import DEVEICETYPE, {
  ID_CARD_V1,
  ID_CARD_V2,
  ID_CARD_V3
} from '@/components/HiSpeedCamera/config.js'
import {
  openIdcardv3,
  closeIdcardv3,
  getdataIdcardv3
} from "@/api/handwarev3.js";

// rules
const personRules = {
  userName: [{ required: true, message: "姓名不能为空", trigger: "change" }],
  idCard: [{ required: true, message: "证件号不能为空", trigger: "change" }]
};

const legalRules = {
  legalUserName: [
    { required: true, message: "单位名称不能为空", trigger: "change" }
  ],
  legalIdCard: [
    { required: true, message: "证件号不能为空", trigger: "change" }
  ]
};
export default {
  inheritAttrs: false,
  name: "ElectronizationOfMaterials",
  props: [
    "caseOid",
    "comboDirectoryOid",
    "comboDirectoryName",
    "isQlCaseChanged",
    "isTempComponent",
    "ruleForm"
  ],
  components: {
    GpyUplaodDialog,
    materialFileView,
    fileView,
    sxFileView,
    caseFileViewIndustry
  },
  data () {
    return {
      dialogVisible: false,
      sxServiceMaterialAttaList: [],
      fileSxAttaOid: "",
      viewDialogOptions: [],
      loading: true,

      elecForm: {
        userName: "",
        idCard: "",
        legalUserName: "",
        legalIdCard: ""
      },

      elecVisible: false,

      currentElecForm: {},

      clickHandleMatchAllElec: false,

      rqbzDueDate: "",

      optionDate: {
        disabledDate (time) {
          return time.getTime() < Date.now(); // 选当前时间之后的时间
        }
      },

      isReadedRqbzNotice: false, // 是否阅读了容缺补正通知书

      isReadedInformNotice: false, // 是否阅读了告知承诺通知书
      showFileList: false,
      fileList: [],
      materialOid: null,
      attaIndex: null,
      fileAttaOid: "",
      showFile: false,
      materialAttaList: [], // 附件保存数据
    };
  },
  computed: {
    ...mapGetters(["saveCallRecordOid"]),
    /** 是否可以点击批量匹配电子证照 */
    isMatchAllElec ({ sxServiceMaterialAttaList }) {
      return (
        sxServiceMaterialAttaList.filter(
          item => !!item.elecBillOid && !!item.directoryObj
        ).length === 0
      );
    },

    /** 是否存在容缺补正 */
    isExitRqbzTime ({ sxServiceMaterialAttaList }) {
      return (
        sxServiceMaterialAttaList.filter(item => item.collectionType === "4")
          .length > 0
      );
    },

    /** 是否存在告知承诺 */
    isExitInformPromise ({ sxServiceMaterialAttaList }) {
      return (
        sxServiceMaterialAttaList.filter(item => item.collectionType === "7")
          .length > 0
      );
    },

    /** 判断是否有个人证照 */
    personElecList ({ sxServiceMaterialAttaList }) {
      return sxServiceMaterialAttaList.filter(
        item =>
          !item.isElecBill &&
          item.elecBillOid &&
          item.directoryObj &&
          item.directoryObj !== "1"
      );
    },

    /** 判断是否有法人的 */
    legalELecList ({ sxServiceMaterialAttaList }) {
      return sxServiceMaterialAttaList.filter(
        item =>
          !item.isElecBill &&
          item.elecBillOid &&
          item.directoryObj &&
          item.directoryObj === "1"
      );
    },

    /** 判断个人证照是否显示 */
    isPersonElecShow ({
      currentElecForm,
      clickHandleMatchAllElec,
      personElecList
    }) {
      return (
        (currentElecForm.directoryObj &&
          currentElecForm.directoryObj !== "1") ||
        (clickHandleMatchAllElec && personElecList.length > 0)
      );
    },

    /** 判断法人证照是否显示 */
    isLegalElecShow ({
      currentElecForm,
      clickHandleMatchAllElec,
      legalELecList
    }) {
      return (
        (currentElecForm.directoryObj &&
          currentElecForm.directoryObj === "1") ||
        (clickHandleMatchAllElec && legalELecList.length > 0)
      );
    },

    rules ({
      isPersonElecShow,
      isLegalElecShow,
      clickHandleMatchAllElec,
      personElecList,
      legalELecList
    }) {
      let rules = {};
      if (clickHandleMatchAllElec) {
        if (legalELecList.length > 0) {
          rules = { ...rules, ...legalRules };
        }

        if (personElecList.length > 0) {
          rules = { ...rules, ...personRules };
        }
      } else {
        if (isPersonElecShow) {
          rules = { ...personRules };
        }

        if (isLegalElecShow) {
          rules = { ...legalRules };
        }
      }

      return rules;
    }
  },

  watch: {
    isExitRqbzTime: {
      immediate: true,
      handler (val) {
        if (!val) {
          this.rqbzDueDate = "";
          this.$emit("setRqbzTime", "");
          if (this.isTempComponent) {
            this.ruleForm.rqhbTime = "";
          }
        } else {
          if (this.isTempComponent) {
            this.rqbzDueDate = this.ruleForm._rqhbTime;
            this.ruleForm.rqhbTime = this.rqbzDueDate;
          }
        }
      }
    }
  },

  mounted () {
    // 如果是暂存受理页面
    if (this.isTempComponent) {
      this.loading = true;
      this.handleIdCardInfo();
      this.getAttaList();
    }
  },
  async activated () {
    // 非暂存受理
    if (!this.isTempComponent) {
      // 如果存在叫号id
      if (this.saveCallRecordOid) {
        try {
          const { code, data } = await querySaveCallRecordByOid({
            oid: this.saveCallRecordOid
          });
          if (code === 200) {
            this.elecForm.userName = data.caseUserName;
            this.elecForm.idCard = data.cardNo;
          }
          this.handleIdCardInfo();
          this.getInit();
        } catch (error) {
          this.handleIdCardInfo();
          this.getInit();
        }
      } else {
        this.handleIdCardInfo();
        this.getInit();
      }
    } else {
      this.handleIdCardInfo();
    }
  },
  deactivated () {
    this.$emit("setIsRqbzFlag", this.isExitRqbzTime);

    const { userName, idCard, legalUserName, legalIdCard } = this.elecForm;

    if (
      this.isTempComponent &&
      (!userName || !idCard || !legalIdCard || !legalUserName)
    )
      return;

    if (!userName && !idCard && !legalUserName && !legalIdCard) return;

    // 判断是否缓存过身份信息
    const idCardInfo = localStorage?.idCardInfo
      ? JSON.parse(localStorage.idCardInfo)
      : false;

    if (idCardInfo) {
      localStorage.setItem(
        "idCardInfo",
        JSON.stringify({
          ...idCardInfo,
          CNName: this.elecForm.userName,
          carID: this.elecForm.idCard,
          legalUserName: this.elecForm.legalUserName,
          legalIdCard: this.elecForm.legalIdCard
        })
      );
    } else {
      localStorage.setItem(
        "idCardInfo",
        JSON.stringify({
          CNName: this.elecForm.userName,
          carID: this.elecForm.idCard,
          legalUserName: this.elecForm.legalUserName,
          legalIdCard: this.elecForm.legalIdCard
        })
      );
    }
  },
  methods: {
    handleIdCardInfo () {
      // 判断是否缓存过身份信息
      const idCardInfo = localStorage?.idCardInfo
        ? JSON.parse(localStorage.idCardInfo)
        : false;
      if (idCardInfo) {
        this.elecForm.userName = idCardInfo.CNName || this.elecForm.userName;
        this.elecForm.idCard = idCardInfo.carID || this.elecForm.idCard;
        this.elecForm.legalUserName =
          idCardInfo.legalUserName || this.elecForm.legalUserName;
        this.elecForm.legalIdCard =
          idCardInfo.legalIdCard || this.elecForm.legalIdCard;
      }
    },

    async nextStep () {
      // FIX: 饿了么button会自动聚焦
      if (this.dialogVisible) return;
      // 容缺补正和告知承诺只能选择其中一种
      if (this.isExitInformPromise && this.isExitRqbzTime) {
        return this.$message.warning(
          "【容缺补正】和【告知承诺】提交方式只能选择一种"
        );
      }

      // 容缺补正时间必填
      if (this.isExitRqbzTime && !this.rqbzDueDate) {
        // 自动滚动到底部
        this.$nextTick(() => {
          this.$refs.scrollbar.$refs.wrap.scrollTop = this.$refs.scrollbar.$refs.wrap.scrollHeight;
        });
        return this.$message.warning("请输入容缺补正时间!");
      } else if (
        this.isExitRqbzTime &&
        this.rqbzDueDate &&
        !this.isReadedRqbzNotice
      ) {
        const [_, action] = await awaitPromise(
          this.$alert(
            `告知申请人在<strong style="color:#fb783f">${moment(
              this.rqbzDueDate
            ).format(
              "YYYY[年]MM[月]DD[日]"
            )}</strong>补齐缺少的材料，否则材料将退回并结束该办件`,
            "容缺补正须知",
            {
              confirmButtonText: "已告知",
              dangerouslyUseHTMLString: true
            }
          )
        );
        if (action !== "confirm") return;
        this.isReadedRqbzNotice = true;
        this.$emit("setRqbzTime", this.rqbzDueDate);
      }

      // 如果选择了承诺告知
      if (this.isExitInformPromise && !this.isReadedInformNotice) {
        const materialNames = this.sxServiceMaterialAttaList
          .filter(item => item.collectionType === "7")
          .map(item => item.materialName);
        const content = (
          <div>
            <p>请告知申请人，以下材料需申请人以书面形式承诺符合办理条件：</p>
            {materialNames.map((ele, idx) => (
              <p>
                <span>{idx + 1}、</span>
                <span>{ele}</span>
              </p>
            ))}
          </div>
        );
        const [_, action] = await awaitPromise(
          this.$msgbox({
            title: "告知承诺须知",
            message: content,
            showCancelButton: false,
            confirmButtonText: "已告知",
            customClass: "inform-promise--msgbox"
          })
        );
        if (action !== "confirm") return;
        this.isReadedInformNotice = true;
      }

      // 判断是否选择了电子证照的却没有设置电子证照信息的
      const hasELec = this.sxServiceMaterialAttaList
        ?.filter(item => item.collectionType === "5" && !item.isElecBill)
        ?.find(item => !item.elecLicenNumber);
      if (hasELec)
        return this.$message.warning(
          `请获取【${hasELec.materialName}】的电子证照信息`
        );

      // 是否有扫描的方式
      const hasScanning = this.sxServiceMaterialAttaList.some(
        item => item.collectionType === "3"
      );

      //判断是否选择了附件上传方式
      const fileUpLoadList = this.sxServiceMaterialAttaList?.filter(
        item => item.collectionType === "2"
      );
      if (fileUpLoadList.length !== 0) {
        // 判端是否都有文件上传
        const isAllUpload = fileUpLoadList.find(
          item => item.attaList.length === 0
        );
        if (isAllUpload) {
          return this.$message.warning(
            `请上传【${isAllUpload.materialName}】的附件信息`
          );
        }
        this.materialAttaList = fileUpLoadList.map(item => {
          return {
            caseMaterialOid: item.caseMaterialOid,
            collectionType: item.collectionType,
            industryCaseMaterialAttaList: item.attaList.map(i => ({
              attaOid: i.attaOid,
              caseMaterialOid: item.caseMaterialOid,
              src: i.fastdfsNginxUrl
            }))
          }
        })
        // 如果没有选择扫描上传方式 上传附件保存附件的接口
        if (!hasScanning) {
          saveIndustryCaseMaterialAttaList(this.materialAttaList).then(res => {
            if (res.code !== 200 || !res.data.success) {
              return this.$message.warning("上传附件保存材料失败");
            }
          });
        }
      } else {
        // 删除所有附件
        this.sxServiceMaterialAttaList.forEach(
          item => (item.attaList.length = 0)
        );
      }

      // 选择上传方式
      const loading = this.$loading({
        lock: true,
        text: "正在加载中",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
      try {
        const { code, messge } = await updateIndustryCaseMaterialList(
          this.sxServiceMaterialAttaList.filter(item => !item.isElecBill)
        );
        if (code !== 200) {
          loading.close();
          return this.$message.warning(messge || "保存材料附件失败");
        }
        loading.close();
        // 缓存所有的caseMaterialOid
        const caseMaterialOids = this.sxServiceMaterialAttaList?.reduce(
          (prev, cur) => {
            if (prev[cur.collectionType]) {
              prev[cur.collectionType] = `${prev[cur.collectionType]};${cur.caseMaterialOid
                }`;
            } else {
              prev[cur.collectionType] = cur.caseMaterialOid;
            }
            return prev;
          },
          {}
        );
        this.$emit("caseMaterialOids", caseMaterialOids);

        // 获取容缺补正材料名称
        const rqbzMaterialNames = this.sxServiceMaterialAttaList
          .filter(item => item.collectionType == "4")
          .map(item => item.materialName)
          .join(",");

        this.$emit("setRqbzMaterialNames", rqbzMaterialNames);

        if (hasScanning) {
          this.dialogVisible = true;
          return;
        }

        this.$emit("nextStep", 4);
      } catch (error) {
        loading.close();
      }
    },

    handleDueDateChange () {
      this.isReadedRqbzNotice = false;
    },

    getInit () {
      if (this.isQlCaseChanged) {
        this.loading = true;
        this.getAttaList();
        this.$emit("setIsQlCaseChanged", false);
      }
    },

    //材料附件
    getAttaList () {
      this.sxServiceMaterialAttaList = [];
      getIndustryCaseMaterialList({
        caseOid: this.caseOid,
        userName: this.elecForm.userName,
        idCard: this.elecForm.idCard
      })
        .then(response => {
          this.loading = false;
          if (response.code !== 200)
            return this.$message.warning("查询材料附件失败");

          this.sxServiceMaterialAttaList =
            response.data?.reduce?.((prev, item, idx) => {
              let collectionType = item.collectionType;

              if (!this.isTempComponent) {
                collectionType =
                  !!item.directoryObj &&
                    !!item.elecBillOid &&
                    !!item.elemNumber &&
                    !!item.elemLicenseOid
                    ? "5"
                    : item.collectionType || "1";
              }

              prev.push({
                ...item,
                collectionType,
                index: idx + 1,
                sxMaterials: {}
              });

              if (
                collectionType === "5" &&
                !!item.elecBillOid &&
                !!item.elemNumber &&
                !!item.elemLicenseOid
              ) {
                // 新增一行
                const row = {
                  isElecBill: true,
                  ...item,
                  originElecInfo: {
                    originName: item.elemNumber,
                    attaOid: item.elecLicenOid,
                    elemLicenseOid: item.elecLicenOid
                  }
                };

                prev = [...prev, row];
              }

              return prev;
            }, []) || [];

          this.$attrs.sxServiceMaterialList.forEach(item => {
            this.sxServiceMaterialAttaList.forEach(ite => {
              if (ite.isElecBill) return;
              if (item.materialOid == ite.materialOid) {
                ite.sxMaterials = item;
                ite.collectionNumber = item.paperNumber;

                if (this.isTempComponent || ite.collectionType == "5") return;
                ite.collectionType = item.materialFormat
                  ? (item.materialFormat == "1"
                    ? item.materialFormat
                    : item.materialFormat == "2"
                      ? "3"
                      : ite.collectionType
                  ).toString()
                  : ite.collectionType;
              }
            });
          });

          // 如果是暂存受理
          if (this.isTempComponent) {
            if (this.isExitRqbzTime) {
              this.isReadedRqbzNotice = true;
            }

            if (this.isExitInformPromise) {
              this.isReadedInformNotice = true;
            }

            this.rqbzDueDate = this.ruleForm.rqhbTime;
          }
        })
        .catch(() => {
          this.loading = false;
        });
    },

    downLoadMaterialAddr (attaOid) {
      if (attaOid) {
        downloadMaterial(attaOid);
      } else {
        this.$message.warning("暂无材料下载！");
      }
    },

    //材料查看
    viewMaterialAddr (attaOid, addr) {
      debugger
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
    //关闭预览附件
    closeFileViewNew () {
      this.viewDialogOptions.pop();
    },

    handleCollectionTypeChange (value) { 
      const index = this.sxServiceMaterialAttaList.findIndex(
        item => item.id === value.id
      );
      if(value.collectionType === "5"||value.collectionType === "2"){ 
          if (index >= 0) {
            this.sxServiceMaterialAttaList[index].collectionFlag = 1; 
          }
      }
      if (value.collectionType === "5") {
        if (index >= 0) {
          this.sxServiceMaterialAttaList.splice(index + 1, 0, {
            ...this.sxServiceMaterialAttaList[index],
            ...(this.sxServiceMaterialAttaList[index]?.originElecInfo ?? {}),
            isElecBill: true
          }); 
          // 设置回缓存的值
          if (
            this.sxServiceMaterialAttaList[index]?.originElecInfo
              ?.elemLicenseOid
          ) {
            this.$set(this.sxServiceMaterialAttaList, index, {
              ...this.sxServiceMaterialAttaList[index],
              ...this.sxServiceMaterialAttaList[index].originElecInfo
            });

            return;
          }

          this.getElecLicen(this.sxServiceMaterialAttaList[index + 1]);
        }
      } else {
        if (this.sxServiceMaterialAttaList[index + 1]?.isElecBill) {
          this.sxServiceMaterialAttaList.splice(index + 1, 1);
        }
        this.$set(this.sxServiceMaterialAttaList, index, {
          ...this.sxServiceMaterialAttaList[index],
          originName: undefined,
          attaOid: undefined,
          elemLicenseOid: undefined,
          elecLicenName: undefined,
          elecLicenNumber: undefined
        });
      }
    },

    // 打开证照弹框 获取证照信息
    getElecLicen (elecForm) {
      if (elecForm.directoryObj) {
        this.currentElecForm = { ...elecForm };
        this.elecVisible = true;
      } else {
        queryElecLicenseType(elecForm.materialOid, elecForm.elecBillOid).then(
          ({ code, data }) => {
            if (code !== 200 || !data) return;
            const { directoryObj } = data;
            elecForm.directoryObj = directoryObj;
            this.currentElecForm = { ...elecForm };
            this.elecVisible = true;
          }
        );
      }
    },

    handelElecFormSubmit () {
      this.$refs.elecForm.validate(async valid => {
        if (valid) {
          if (this.clickHandleMatchAllElec) {
            this.elecVisible = false;
            if (this.personElecList.length > 0) {
              this.queryAllEelc(
                this.personElecList,
                this.elecForm.userName,
                this.elecForm.idCard
              );
            }
            if (this.legalELecList.length > 0) {
              this.queryAllEelc(
                this.legalELecList,
                this.elecForm.legalUserName,
                this.elecForm.legalIdCard
              );
            }
            return;
          }
          const { materialOid, elecBillOid } = this.currentElecForm;
          const {
            userName,
            idCard,
            legalUserName,
            legalIdCard
          } = this.elecForm;
          const index = this.sxServiceMaterialAttaList.findIndex(
            item => item.materialOid === materialOid
          );
          this.elecVisible = false;
          if (this.isPersonElecShow) {
            this.getElecLicenInfo(
              materialOid,
              userName,
              idCard,
              elecBillOid,
              index
            );
          } else {
            this.getElecLicenInfo(
              materialOid,
              legalUserName,
              legalIdCard,
              elecBillOid,
              index
            );
          }
        }
      });
    },

    getElecLicenInfo (materialOid, userName, idCard, elecBillOid, index) {
      return queryindustryElecLicenseByDirCode(
        materialOid,
        userName,
        idCard,
        elecBillOid,
        this.caseOid
      ).then(({ data, code, message }) => {
        if (code !== 200) {
          this.currentElecForm = {};
          return this.$message.warning(
            message || "暂无证照,请检查证照相关配置！"
          );
        }
        const { licenseNumber, elecLicenOid, elecLicenNumber, elecLicenName } =
          data || {};
        this.$set(this.sxServiceMaterialAttaList, index, {
          ...this.sxServiceMaterialAttaList[index],
          originName: licenseNumber,
          attaOid: elecLicenOid,
          elemLicenseOid: elecLicenOid,
          elemNumber: licenseNumber,
          elecLicenName: elecLicenName || "未匹配到相应证照",
          elecLicenNumber,
          originElecInfo: {
            // 设置缓存
            originName: licenseNumber,
            attaOid: elecLicenOid,
            elemLicenseOid: elecLicenOid,
            elecLicenName: elecLicenName || "未匹配到相应证照",
            elecLicenNumber
          }
        });

        this.$set(this.sxServiceMaterialAttaList, index + 1, {
          ...this.currentElecForm,
          ...data,
          elemLicenseOid: elecLicenOid,
          elecLicenName: elecLicenName || "未匹配到相应证照"
        });

        this.currentElecForm = {};

        // this.$message.success("电子证照引用成功");
      });
    },
    //电子证照预览
    viewElemsInfo (eleLincenseOid) {
      if (!eleLincenseOid) return this.$message.warning("请先获取证照信息");

      getElecLicenUrl(eleLincenseOid).then(response => {
        let urlElem = [];
        if (response.data) {
          urlElem = response.data;
          if (urlElem[0].viewOfdUrl) {
            window.open(urlElem[0].viewOfdUrl, "width=1200px;height=800px;");
          } else {
            this.$message.error("暂时无法查看证照信息！");
          }
        } else {
          this.$message.error("未查到相关证照信息！");
        }
      });
    },

    // 批量匹配电子证照
    hanldeMatchAllElec () {
      this.clickHandleMatchAllElec = true;
      this.sxServiceMaterialAttaList = this.sxServiceMaterialAttaList.reduce(
        (prev, cur) => {
          const arr = [];
          if (cur.elecBillOid && !cur.isElecBill && !!cur.directoryObj) {
            cur.collectionType = "5";
            arr.push({
              ...cur,
              isElecBill: true
            });
          }
          if (cur.isElecBill) {
            return prev;
          }
          prev.push(cur);
          prev = [...prev, ...arr];
          return prev;
        },
        []
      );

      this.elecVisible = true;
    },

    queryAllEelc (list = [], userName = "", idCard = "") {
      const promiseAllList = list.map(item =>
        queryindustryElecLicenseByDirCode(
          item.materialOid,
          userName,
          idCard,
          item.elecBillOid,
          this.caseOid
        )
      );
      this.clickHandleMatchAllElec = false;
      return Promise.all(promiseAllList).then(ret => {
        ret.forEach(({ code, data }, idx) => {
          if (code !== 200)
            return console.warn(`${list[idx].materialName}获取证照信息失败`);
          const index = this.sxServiceMaterialAttaList.findIndex(
            item => item.id === list[idx].id && !item.isElecBill
          );
          if (index >= 0) {
            const {
              licenseNumber,
              elecLicenOid,
              elecLicenName,
              elecLicenNumber
            } = data || {};

            this.$set(this.sxServiceMaterialAttaList, index, {
              ...this.sxServiceMaterialAttaList[index],
              originName: licenseNumber,
              attaOid: elecLicenOid,
              elemLicenseOid: elecLicenOid,
              elemNumber: licenseNumber,
              elecLicenName: elecLicenName || "未匹配到相应证照",
              elecLicenNumber,
              collectionType: "5",
              originElecInfo: {
                // 设置缓存
                originName: licenseNumber,
                attaOid: elecLicenOid,
                elemLicenseOid: elecLicenOid,
                elecLicenName: elecLicenName || "未匹配到相应证照",
                elecLicenNumber
              }
            });

            if (this.sxServiceMaterialAttaList[index + 1]?.isElecBill) {
              this.$set(this.sxServiceMaterialAttaList, index + 1, {
                ...this.sxServiceMaterialAttaList[index + 1],
                ...data,
                elemLicenseOid: elecLicenOid,
                elecLicenName: elecLicenName || "未匹配到相应证照"
              });
            } else {
              this.sxServiceMaterialAttaList.splice(index + 1, 0, {
                ...this.sxServiceMaterialAttaList[index],
                ...(this.sxServiceMaterialAttaList[index]?.originElecInfo ??
                  {}),
                isElecBill: true
              });
            }
          } else {
            return console.warn(`${list[idx].materialName}匹配证照信息失败`);
          }
        });
      });
    },

    handleElecClose () {
      this.elecVisible = false;
      this.clickHandleMatchAllElec = false;
      this.$refs?.elecForm?.clearValidate?.();
    },

    scanCard () {
      if (DEVEICETYPE.IDCARD_CONFIG === ID_CARD_V1) {
        this.getIdcardDatav1();
      }
      if (DEVEICETYPE.IDCARD_CONFIG === ID_CARD_V2) {
        this.getIdcardDatav2();
      }
      if (DEVEICETYPE.IDCARD_CONFIG === ID_CARD_V3) {
        this.getIdcardDatav3();
      }
    },
    async getIdcardDatav3 () {
      openIdcardv3().then(res => {
        if (res.StateCode == 0 || res.StateCode == -1) {
          return this.getIdcardDataByv3(); //获取身份证信息
        } else {
          return this.$message.warning("请确认设备连接是否正常");
        }
      });
    },
    async getIdcardDataByv3 () {
      getdataIdcardv3().then(resData => {
        if (resData.state == "sucess" && resData.StateCode == 0) {
          let resInfo = JSON.parse(resData.data);
          let res = {
            CNName: resInfo.name,
            sex: resInfo.sex,
            carID: resInfo.number,
            nation: resInfo.nation,
            address: resInfo.department,
            bron: resInfo.birthday,
            imgData: "data:image/jpeg;base64," + resInfo.Image
          };
          this.elecForm.userName = res.CNName.trim();
          this.elecForm.idCard = res.carID;
          // 缓存身份证信息 方便后面 人证对比 使用
          localStorage.setItem("idCardInfo", JSON.stringify(res));
        } else if (resData.StateCode == -1) {
          //0:成功-1:设备没有打开-2:寻卡失败-3选卡失败-4读卡失败
          //判断有没有设备
          //打开身份证
          openIdcardv3().then(res => {
            if (res.StateCode == 0 || res.StateCode == -1) {
              this.getIdcardDataByv3(); //重新获取身份证信息
            }
          });
        } else {
          this.$message.error(resData.tips);
          return false;
        }
      });
    },
    async getIdcardDatav1 () {
      openIdCard().then(response => {
        //打开设备
        if (response.state == "sucess") {
          this.getIdcardDataByv1(); //重新获取身份证信息
        } else {
          //查找设备
          findIdCard().then(resFind => {
            if ((resFind.state = "sucess" && resFind.StateCode == 0)) {
              this.getIdcardDataByv1();
            } else {
              this.$message.error("无法找到设备");
              return false;
            }
          });
        }
      });
    },
    async getIdcardDataByv1 () {
      getIdCardInfo().then(response => {
        let res = response;
        if (!res) {
          this.$message.error("请检查设备或连接是否正常!");
          return false;
        }
        if (res.state == "sucess") {
          this.elecForm.userName = res.CNName.trim();
          this.elecForm.idCard = res.carID;
          // 缓存身份证信息 方便后面 人证对比 使用
          localStorage.setItem("idCardInfo", JSON.stringify(res));
        } else if (res.StateCode == -1) {
          //0:成功-1:设备没有打开-2:寻卡失败-3选卡失败-4读卡失败
          //判断有没有设备
          //打开身份证
          openIdCard().then(response => {
            if ((response.state = "sucess")) {
              this.getIdcardDataByv1(); //重新获取身份证信息
            }
          });
        } else {
          this.$message.error(res.tips);
          return false;
        }
      });
    },
    async getIdcardDatav2 () {
      const resData = await GPYDrive.readCardInfo();
      if (!resData || resData.code !== 200) {
        this.$message.error("读取身份证信息失败");
        return false;
      }
      let res = {
        CNName: resData.data.name,
        sex: resData.data.sex,
        carID: resData.data.number,
        nation: resData.data.nation,
        address: resData.data.address,
        bron: resData.data.birthday,
        imgData: "data:image/jpeg;base64," + resData.data.image
      };
      this.elecForm.userName = res.CNName.trim();
      this.elecForm.idCard = res.carID;
      // 缓存身份证信息 方便后面 人证对比 使用
      localStorage.setItem("idCardInfo", JSON.stringify(res));
    },
    pushMaterialOid (materialOid, index) {
      this.materialOid = materialOid;
      this.attaIndex = index; //标识材料索引
    },
    //上传之前
    beforeUpload (file) {
      if (
        file.name.indexOf("%00") > -1 ||
        file.name.indexOf("./") > -1 ||
        file.name.indexOf(".\\") > -1
      ) {
        this.msgError("上传文件名称非法！");
        return false;
      }
      const fileSize = file.size;
      if (0 == fileSize) {
        this.msgError("上传文件不能为空！");
        return false;
      }
      const isLt2M = file.size / 1024 / 1024 < 500;
      if (!isLt2M) {
        this.msgError("上传文件大小不能超过 500MB！");
      }
      return isLt2M;
    },
    /** 上传附件 */
    uploadFiles (file) {
      let formData = new FormData();
      formData.append("files", file.file);
      uploadCaseMaterialFile(formData).then(response => {
        const { code, data } = response;
        if (code !== 200) return;
        const target = this.sxServiceMaterialAttaList[this.attaIndex];
        target.attaList = target.attaList.concat(data);
      });
    },
    //失败后返回
    uploadError (resp) {
      this.msgError("文件上传失败");
    },
    //预览附件
    viewFile (attaOid) {
      this.fileAttaOid = attaOid;
      this.showFile = true;
    },
    // //关闭预览附件
    closeFileView () {
      this.showFile = false;
    },
    //删除附件信息
    deleteMaterialAtta (idx, index) {
      this.sxServiceMaterialAttaList[index].attaList.splice(idx, 1);
    }
  }
};
</script>

<style lang="scss">
.el-message-box.inform-promise--msgbox {
  width: 450px;
}

.handle-btn {
  .el-button {
    span {
      display: flex;
      align-items: center;
      img {
        margin-right: 4px;
      }
    }
  }
}

.is-elec-bill {
  background-color: #fafbfc !important;
  height: 40px;
  padding: unset !important;

  .is-elec-bill--content {
    display: flex;
    justify-content: space-between;
    padding: 0 80px;
    align-items: center;

    .is-elec-bill-content-link {
      text-decoration: underline;
      cursor: pointer;

      &:hover {
        color: #409eff;
      }
    }
  }
}
</style>
<style lang="scss" scoped>
.handle-data {
  display: flex;
  justify-content: flex-end;
  padding: 0 20px;

  &-list {
    flex: 1;
    display: flex;
    flex-direction: column;

    &-item {
      flex: 1;
      display: flex;
      padding: 0 20px;
      justify-content: space-between;
      align-items: center;

      &--link {
        text-decoration: underline;
        cursor: pointer;

        &:hover {
          color: #409eff;
        }
      }
    }
  }
}
::v-deep .el-radio {
  margin-right: 15px;
}

::v-deep.el-input--medium .el-input__icon {
  line-height: 32px;
}
</style>
