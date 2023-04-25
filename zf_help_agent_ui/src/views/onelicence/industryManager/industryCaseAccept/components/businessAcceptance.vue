<!-- 业务受理 -->
<template>
  <div class="business-acceptance">
    <el-scrollbar class="common-dialog-content">
      <div class="common-dialog--title">受理信息</div>
      <el-form
        :model="ruleForm"
        :rules="rules"
        ref="ruleForm"
        label-width="0px"
        label-position="top"
      >
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="15%" />
            <col width="35%" />
            <col width="15%" />
            <col width="35%" />
          </colgroup>
          <!-- <tr>
            <td><b>审批部门：</b></td>
            <td colspan="3">{{ loginUser.organName }}</td>
          </tr> -->
          <tr>
            <td><b>是否受理：</b></td>
            <td colspan="3">
              <el-radio-group v-model="ruleForm.ifAccept">
                <el-radio :label="1">给予受理</el-radio>
                <el-radio :label="2">不予受理</el-radio>
              </el-radio-group>
            </td>
          </tr>
          <!-- <tr v-if="isRqslFlag == true">
            <td><i class="require">*</i><b>承诺容缺补正时间：</b></td>
            <td colspan="3">
              <el-date-picker
                v-model="ruleForm.rqbzDueDate"
                type="date"
                value-format="yyyy-MM-dd"
                :picker-options="optionDate"
                placeholder="选择承诺容缺补正时间"
              ></el-date-picker>
            </td>
          </tr> -->
          <tr>
            <td>
              <i class="require" v-if="ruleForm.ifAccept == 2">*</i>
              <b>意见说明：</b>
            </td>
            <td colspan="3">
              <el-form-item label="" prop="desc">
                <el-input
                  type="textarea"
                  v-model="ruleForm.acceptOpinionDesc"
                  maxlength="200"
                  show-word-limit
                >
                </el-input>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
    </el-scrollbar>
    <div class="common-dialog-footer">
      <el-button type="primary" @click="$emit('lastStep', 5)">上一步</el-button>
      <el-button type="primary" v-if="isRqslFlag" @click="pushPjCaseService">
        容缺受理
      </el-button>
      <el-button
        type="primary"
        v-else-if="informPromiseFlag"
        @click="pushPjCaseService"
      >
        告知承诺受理
      </el-button>
      <el-button type="primary" v-else @click="pushPjCaseService">
        受理
      </el-button>
      <!--      <el-button type="primary" v-if="dueDate" @click="printNotice()">
        告知承诺书
      </el-button>-->
    </div>
  </div>
</template>

<script>
import {
  showCallMessage,
  getPushStar, getSpeechConfigInfo
} from "@/api/zc/businessManagement/doubleScreenInteraction";
import { queryQlCaseApplayByCaseOid } from "@/api/zc/businessManagement/evaluate";
import { rules } from "./constant.js";
import {
  saveOrUpdateGzBqbz,
  getFileDownPath,
  downloadPrintFile,
  saveOut
} from "@/api/zc/businessManagement/windowAcceptance";
import { mapGetters } from "vuex";
import { saveManualEvaluation } from "@/api/zc/goodBadComment/virtualBusinessRecord";
import { debounce } from '@/utils/utils.js';
import {
  saveCaseAccpet,
  pushPbpjUser,
  pbpjSaveCaseInfo,
  getComboCaseByOid
} from "@/api/onething/comboManager/comboAccept/initComboCase";
import {saveIndustryCaseAccpet} from '@/api/onelicence/industryManager/industryCaseAccept/initIndustryCase'
import { getIndustryCaseByCaseOid} from '@/api/onelicence/industryManager/industryCaseAccept/initIndustryCase';

export default {
  name: "BusinessAcceptance",
  inheritAttrs: false,
  props: [
    "sxServiceMaterialList",
    "loginUser",
    "systemType",
    "pCegisterType",
    "caseNumber",
    "ruleForm",
    "caseOid",
    "serviceOid",
    "serviceForm",
    "dueDate",
    "caseMaterialOids",
    "situation",
    "rqbzMaterialNames"
  ],
  data () {
    return {
      rules: { ...rules },
      flagCc: true,
      chakanCharge: false,
      bqbzCaseForm: {},
      allMaterialNames: "",
      evaluateContentList: [],
      screenUrl: "",
      star: "",
      maOid: "",
      contentCode: "",
      content: "",
      pushStar: "",
      caseUserName: "",
      phone: "",
      mustOutcaseMaterialOids: "", //需要出库的材料oids
      optionDate: {
        disabledDate (time) {
          return time.getTime() < Date.now(); // 选当前时间之后的时间
        }
      },
      appraiseFlag: "", //是否启用手动评价
      evaluateCreateDate: "",
      smallspeech: 'N',//是否打开引导语
      speechAddress: ''
    };
  },
  computed: {
    ...mapGetters(["currentVirtualBusinessRecordOid", "pjdjTime"]), ...mapGetters(["deviceMap"]),
    /** 是否选择了告知承诺 */
    informPromiseFlag ({ caseMaterialOids }) {
      return !!caseMaterialOids["7"];
    },

    /** 是否是容缺补正 */
    isRqslFlag () {
      return this.ruleForm.rqhbTime;
    }
  },
  activated () {
    this.getAllMaterials();
  },
  created () {
    this.getPushStar();
    this.getSpeechConfig();
    this.queryQlCaseApplayByCaseOidM();
    pushPbpjUser(this.loginUser.userOid).then(response => {
      if (response.data != "") {
        this.appraiseFlag = response.data.appraiseFlag;
      }
    });
  },
  methods: {
    queryQlCaseApplayByCaseOidM () {
      if (this.caseOid != null && this.caseOid != "") {
        getIndustryCaseByCaseOid(this.caseOid).then(res => {
          console.log(res);
          this.caseUserName = res.data.applyUserName;
          this.evaluateCreateDate = res.data.createDate;
          if (
            res.data.applyUserPhone != null &&
            res.data.applyUserPhone != ""
          ) {
            this.phone = res.data.applyUserPhone;
          } else if (
            res.data.applyUserTel != null &&
            res.data.applyUserTel != ""
          ) {
            this.phone = res.data.applyUserTel;
          }
        });
      }
    },
    // 获取所有材料的名称
    getAllMaterials () {
      this.allMaterialNames = this.sxServiceMaterialList
        .map(item => item.materialName)
        .join(";");
    },
    //办件平板评价
    pushPjCaseService: debounce(function () {
      // if (this.isRqslFlag && !this.ruleForm.rqbzDueDate) {
      //   this.$message.warning("承诺补正时间不能为空!");
      //   return false;
      // }
      //1  开启
      if (this.appraiseFlag == "1") {
        if (this.ruleForm.ifAccept == 2) {
          if (this.ruleForm.acceptOpinionDesc) {
            let manualEvaluation = {};
            manualEvaluation.virtualBusinessNum = this.currentVirtualBusinessRecordOid;
            manualEvaluation.caseNumber = this.caseNumber;
            manualEvaluation.caseUserName = this.caseUserName;
            manualEvaluation.phone = this.phone;
            manualEvaluation.evaluateSource = "2";
            manualEvaluation.caseOid = this.caseOid;
            saveManualEvaluation(manualEvaluation)
              .then(res => {
                this.star = res.data.star;
                this.maOid = res.data.oid;
                this.contentCode = res.data.contentCode;
                this.content = res.data.content;
              })
              .then(() => {
                this.pushInteractionPj();
              });
          } else {
            this.$message.error("意见说明不能为空！");
            return false;
          }
        } else {
          let manualEvaluation = {};
          manualEvaluation.virtualBusinessNum = this.currentVirtualBusinessRecordOid;
          manualEvaluation.caseNumber = this.caseNumber;
          manualEvaluation.caseUserName = this.caseUserName;
          manualEvaluation.phone = this.phone;
          manualEvaluation.evaluateSource = "2";
          manualEvaluation.caseOid = this.caseOid;
          saveManualEvaluation(manualEvaluation)
            .then(res => {
              this.star = res.data.star;
              this.maOid = res.data.oid;
              this.contentCode = res.data.contentCode;
              this.content = res.data.content;
            })
            .then(() => {
              this.pushInteractionPj();
            });
        }
      } else {
        this.pushInteractionPj();
      }
    }, 2000, true),
    printNotice () {
      this.handleGzcnPrint();
    },
    handleGzcnPrint () {
      let noticeMes = {};
      if (this.ruleForm.applyUserType == 0) {
        noticeMes.applyUserName = this.ruleForm.applyUserName;
        noticeMes.applyCompany = "";
      } else {
        noticeMes.applyCompany = this.ruleForm.applyUserName;
        noticeMes.applyUserName = "";
      }
      noticeMes.phone = this.ruleForm.applyUserPhone;
      noticeMes.serviceName = this.serviceForm.serviceName;
      noticeMes.zxPhone = this.serviceForm.zxDhText;
      noticeMes.caseNumber = this.caseNumber;
      noticeMes.bzDate = this.dueDate;
      noticeMes.fileName = "gzcns.doc";
      noticeMes.materials = this.allMaterialNames;
      this.handlePrintAll(noticeMes);
    },
    //调用c++服务打印
    handlePrintAll (obj) {
      getFileDownPath(obj).then(response => {
        if (response.data) {
          let url = response.data;
          downloadPrintFile(url).then(res => { });
          //window.location.href = process.env.VUE_APP_FILEDOWN_API_PAGE+'?url='+response.data;
        }
      });
    },
    viewCharge () {
      this.viewchargeShow = true;
    },
    qlCaseCharge () {
      this.chargeShow = true;
    },
    closeCaseCharge (obj) {
      if (obj.flag == "1" || obj.flag == "2") {
        this.chargeShow = false;
        this.chakanCharge = true;
        this.flagCc = false;
      }
      if (obj == "") {
        this.chargeShow = false;
      }
    },
    closeView () {
      this.viewchargeShow = false;
      this.chakanCharge = true;
      this.flagCc = false;
    },

    //超级综窗柜台双屏互动办件评价
    async pushInteractionPj () {
      let url = window.location.origin;
      let pushUrl = url + "/serviceHall/evaluation.html";
      let data = {};
      data.userName = this.loginUser.userName;
      data.organName = this.loginUser.organName;
      data.projectName = this.ruleForm.projectName;
      data.caseNumber = this.ruleForm.caseNumber;
      data.createDate = this.evaluateCreateDate;
      data.maOid = this.maOid;
      data.star = this.star;
      data.contentCode = this.contentCode;
      data.content = this.content;
      data.pushStar = this.pushStar;
      data.caseOid = this.caseOid;
      data.currentVirtualBusinessRecordOid = this.currentVirtualBusinessRecordOid;
      data.caseUserName = this.caseUserName;
      data.phone = this.phone;
      data.smallspeech = this.smallspeech;
      data.speechAddress = this.speechAddress;
      //add
      data.pjdjTime = this.pjdjTime;
      pushUrl = pushUrl + "?jsonObject=" + JSON.stringify(data);
      process.env.NODE_ENV === "development" && window.open(pushUrl);
      await this.caseAccpet();
      await pbpjSaveCaseInfo(this.caseOid).then(response => {
        if (response.code == 200) {
          if (this.appraiseFlag == "1") {
            showCallMessage(pushUrl).then(res => {
              if (res) {
                this.$message.warning("办件评价中...");
              }
            });
          }
        } else {
          this.$message.warning("平板评价保存办件失败！");
          return false;
        }
      });
    },
    /** 办件受理 */
    caseAccpet () {
      if (null != this.caseMaterialOids) {
        // 纸质
        let zzcaseMaterialOids = this.caseMaterialOids["1"];
        // 附件上传
        let fjsccaseMaterialOids = this.caseMaterialOids["2"];
        // 扫描
        let smcaseMaterialOids = this.caseMaterialOids["3"];
        // 容缺
        let rqcaseMaterialOids = this.caseMaterialOids["4"];
        // 证照
        let zzhaocaseMaterialOids = this.caseMaterialOids["5"];
        // 告知承诺
        let gzcncaseMaterialOids = this.caseMaterialOids["7"];
        if (zzcaseMaterialOids != undefined) {
          if (
            this.mustOutcaseMaterialOids != "" &&
            this.mustOutcaseMaterialOids != null
          ) {
            this.mustOutcaseMaterialOids =
              this.mustOutcaseMaterialOids + ";" + zzcaseMaterialOids;
          } else {
            this.mustOutcaseMaterialOids = zzcaseMaterialOids;
          }
        }
        if (smcaseMaterialOids != undefined) {
          if (
            this.mustOutcaseMaterialOids != "" &&
            this.mustOutcaseMaterialOids != null
          ) {
            this.mustOutcaseMaterialOids =
              this.mustOutcaseMaterialOids + ";" + smcaseMaterialOids;
          } else {
            this.mustOutcaseMaterialOids = smcaseMaterialOids;
          }
        }
        if (zzhaocaseMaterialOids != undefined) {
          if (
            this.mustOutcaseMaterialOids != "" &&
            this.mustOutcaseMaterialOids != null
          ) {
            this.mustOutcaseMaterialOids =
              this.mustOutcaseMaterialOids + ";" + zzhaocaseMaterialOids;
          } else {
            this.mustOutcaseMaterialOids = zzhaocaseMaterialOids;
          }
        }

        if (fjsccaseMaterialOids != undefined) {
          if (
            this.mustOutcaseMaterialOids != "" &&
            this.mustOutcaseMaterialOids != null
          ) {
            this.mustOutcaseMaterialOids =
              this.mustOutcaseMaterialOids + ";" + fjsccaseMaterialOids;
          } else {
            this.mustOutcaseMaterialOids = fjsccaseMaterialOids;
          }
        }
      }
      console.log(this.mustOutcaseMaterialOids);
      if (this.caseOid != "") {
        const params = {
          caseOid: this.caseOid,
          valList: this.situation?.valOids?.split?.(","),
          sqrName: this.ruleForm.applyUserName,
          rqhbTime: this.ruleForm.rqhbTime,
          ifAccept: this.ruleForm.ifAccept,
          projectName: this.ruleForm.projectName,
          caseNumber: this.caseNumber || this.ruleForm.caseNumber,
          acceptOpinionDesc: this.ruleForm.acceptOpinionDesc
        };
        if (this.ruleForm.ifAccept == 2) {
          if (!this.ruleForm.acceptOpinionDesc) {
            this.$message.error("意见说明不能为空！");
            return false;
          }
        }
        return saveIndustryCaseAccpet(params).then(response => {
          if (response.data != "") {
            //this.linkResultOid = response.data.linkResultOid;
            let noticeMes = {};
            if (this.pCegisterType == 0) {
              noticeMes.applyUserName = this.ruleForm.applyUserName;
              noticeMes.applyCompany = "";
            } else {
              noticeMes.applyCompany = this.ruleForm.applyUserName;
              noticeMes.applyUserName = "";
            }
            noticeMes.phone = this.ruleForm.applyUserPhone;
            noticeMes.serviceName = this.serviceForm.serviceName;
            noticeMes.zxPhone = this.serviceForm.zxDhText;
            noticeMes.caseNumber = this.ruleForm.caseNumber;
            noticeMes.slTime = response.data.acceptanceDate;
            noticeMes.jbrName = this.loginUser.userName;
            noticeMes.caseOid = this.caseOid;
            noticeMes.organName = this.serviceForm.organName;
            noticeMes.sjrName = this.loginUser.userName;
            noticeMes.delivery = this.ruleForm.resultDeliveryWay;
            noticeMes.pushUrl = window.location.origin + "/serviceHall/industyCaseInfo.html?caseOid=" + this.caseOid;
            if (noticeMes.delivery.indexOf("1") > -1) {
              noticeMes.jsrName = this.ruleForm.addresseeName;
              noticeMes.jsrPhone = this.ruleForm.addresseePhone;
              noticeMes.jsrAddress = this.ruleForm.addresseeDetailAddress;
            }

            if (this.ruleForm.ifAccept == 2) {
              noticeMes.resonDesc = this.ruleForm.acceptOpinionDesc;
              noticeMes.materials = this.allMaterialNames;
              noticeMes.isRqslFlag = "";
              noticeMes.fileName = "bysltzs.docx";
              noticeMes.isSl = "false";
              this.$message.success("办件不予受理保存成功！");
              this.$emit("case-close", noticeMes); //不予受理通知书
            } else {
              this.$message.success("办件进入受理成功！");
              //保存补齐补正信息
              // if (this.dueDate) {
              //   this.saveCaseCorrection();
              // }
              // 需要出库的
              // if (this.mustOutcaseMaterialOids && !this.dueDate) {
              //   //不是容缺的的材料进行出库信息保存
              //   this.saveMaterialOut(this.mustOutcaseMaterialOids);
              // }
              if (this.chargeFlag == "1") {
                this.chargeShow = true;
              }
              if (this.isRqslFlag) {
                noticeMes.rqbzDueDate = this.ruleForm.rqhbTime;
                //打印容缺承诺书
                noticeMes.bzMaterials = this.rqbzMaterialNames;
                noticeMes.fileName = "rqsltzs.docx"; //通知书名称必须
                noticeMes.isRqslFlag = "true";
                noticeMes.isGzSl = "";
                this.$emit("case-close", noticeMes);
              } else if (this.informPromiseFlag) {
                //打印告知承诺书
                noticeMes.fileName = "gzs.docx"; //通知书名称必须
                noticeMes.isRqslFlag = "";
                noticeMes.isSl = "";
                noticeMes.isGzSl = "true";

                this.$emit("case-close", noticeMes);
              } else {
                //受理通知书
                //noticeMes.materials = this.allMaterialNames;
                noticeMes.fileName = "sltzs.docx"; //通知书名称必须
                noticeMes.isRqslFlag = "";
                noticeMes.isSl = "true";
                noticeMes.isGzSl = "";
                this.$emit("case-close", {
                  ...noticeMes,
                  isAllowMaterialOut:
                    !this.informPromiseFlag && !!this.mustOutcaseMaterialOids
                });
              }
            }
          }
        });
      } else {
        this.msgError("办件信息未保存，请先保存！");
        return false;
      }
    },
    /** 综窗保存材料出库信息 */
    async saveMaterialOut (materialOids) {
      let outMaterial = {};
      outMaterial.regOid = this.caseOid;
      outMaterial.caseNumber = this.caseNumber;
      outMaterial.applyUserName = this.ruleForm.applyUserName;
      outMaterial.serviceTypeName = this.serviceForm.serviceTypeName;
      outMaterial.cardNumber = this.ruleForm.credentialNumber;
      outMaterial.serviceOid = this.serviceOid;
      outMaterial.materialOids = materialOids;
      await saveOut(outMaterial).then(response => {
        if (response.data == "") {
          this.msgError("材料出库信息保存失败！");
          return false;
        }
      });
    },
    //保存补齐补正信息
    async saveCaseCorrection () {
      this.bqbzCaseForm.caseOid = this.caseOid;
      this.bqbzCaseForm.caseNumber = this.caseNumber;
      this.bqbzCaseForm.applyUserName = this.ruleForm.applyUserName;
      this.bqbzCaseForm.applyProjectName = this.ruleForm.projectName;
      this.bqbzCaseForm.userPhone = this.ruleForm.applyUserPhone;
      this.bqbzCaseForm.bzType = "0";
      this.bqbzCaseForm.dueDate = this.dueDate;
      this.bqbzCaseForm.serviceOid = this.serviceOid;
      await saveOrUpdateGzBqbz(this.bqbzCaseForm).then(response => {
        if (response.code == 200) {
        }
      });
    },
    getPushStar () {
      getPushStar().then(res => {
        if (res.code == 200) {
          this.pushStar = res.data;
        }
      });
    },
    //查询引导语配置
    async getSpeechConfig () {
      await getSpeechConfigInfo("SMALL_SPEECH").then(response => {
        this.smallspeech = response.data.value;
        this.speechAddress = this.deviceMap?.["AUDIO_SPEECH"];
        console.log(this.deviceMap?.["AUDIO_SPEECH"])
      })
    },
  }
};
</script>
<style lang="scss" scoped>
::v-deep .el-input__icon.el-icon-date {
  line-height: 32px;
}
</style>
