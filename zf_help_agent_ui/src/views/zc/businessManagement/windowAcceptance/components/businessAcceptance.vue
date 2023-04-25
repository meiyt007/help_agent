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
          <tr>
            <td><b>审批部门：</b></td>
            <td colspan="3">{{ serviceForm.organName }}</td>
          </tr>
          <tr>
            <td><b>是否受理：</b></td>
            <td colspan="3">
              <el-radio-group v-model.trim="ruleForm.acceptradio">
                <el-radio label="1">给予受理</el-radio>
                <el-radio label="2">不予受理</el-radio>
              </el-radio-group>
            </td>
          </tr>
          <tr>
            <td>
              <i class="require" v-if="ruleForm.acceptradio == 2">*</i>
              <b>意见说明：</b>
            </td>
            <td colspan="3">
              <el-form-item label="" prop="desc">
                <el-input
                  type="textarea"
                  v-model.trim="ruleForm.finalOpinionDesc"
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
      <el-button
        type="primary"
        v-if="!isRqslFlag && !informPromiseFlag && sjStatus==2"
        @click="pushPjCaseService"
      >
        受理
      </el-button>
      <el-button
        type="primary"
        v-if="!isRqslFlag && !informPromiseFlag && (sjStatus==1 || !sjStatus)"
        @click="pushPjCaseService"
      >
        收件
      </el-button>
      <el-button type="primary" v-if="isRqslFlag" @click="pushPjCaseService">
        容缺受理
      </el-button>
      <el-button
        type="primary"
        v-if="informPromiseFlag"
        @click="pushPjCaseService()"
      >
        告知承诺受理
      </el-button>
      <el-button
        v-if="flagCc"
        @click="qlCaseCharge"
        :disabled="true"
        type="info"
      >
        收费
      </el-button>
      <el-button type="primary" v-if="chakanCharge" @click="viewCharge">
        查看缴费
      </el-button>
    </div>

    <!-- 获取收费信息-->
    <el-dialog
      v-dialog-drag
      :visible.sync="viewchargeShow"
      v-if="viewchargeShow"
      title="查看收费信息"
      width="60%"
      append-to-body
    >
      <view-charge @case-close="closeView"></view-charge>
    </el-dialog>

    <!-- 获取收费信息-->
    <el-dialog
      v-dialog-drag
      :visible.sync="chargeShow"
      v-if="chargeShow"
      title="办件收费"
      width="60%"
      append-to-body
    >
      <case-charge @case-close="closeCaseCharge"></case-charge>
    </el-dialog>
  </div>
</template>

<script>
import viewCharge from "@/views/zc/businessManagement/charge/viewCharge";
import caseCharge from "@/views/zc/businessManagement/charge/caseCharge";
import {
  showCallMessage,
  getStarAndScore,
  getPushStar, getSpeechConfigInfo
} from "@/api/zc/businessManagement/doubleScreenInteraction";
import { pushPbpjUser,pushPbpjCheckLogin,pbpjSaveCaseInfo,pushPbpjInfo } from "@/api/zc/businessManagement/pbpjManage";
import {
  contentList,
  saveEvaluateCaseInfo,
  queryQlCaseApplayByCaseOid
} from "@/api/zc/businessManagement/evaluate";
import { rules } from "./constant.js";
import {
  saveCaseAccpet,
  saveOrUpdateGzBqbz,
  getFileDownPath,
  downloadPrintFile,
  saveOut
} from "@/api/zc/businessManagement/windowAcceptance";
import { mapGetters } from "vuex";
import { saveManualEvaluation } from "@/api/zc/goodBadComment/virtualBusinessRecord";
import { debounce } from '@/utils/utils.js';
import {getPjDeviceType, grantWindowUser, savePjMark} from "@/api/zc/businessManagement/smartEvaManage";
export default {
  name: "BusinessAcceptance",
  inheritAttrs: false,
  props: [
    "sxServiceMaterialList",
    "loginUser",
    "systemType",
    "cegisterType",
    "caseNumber",
    "ruleForm",
    "caseOid",
    "serviceOid",
    "serviceForm",
    // "dueDate",
    "caseMaterialOids",
    "rqbzMaterialNames"
  ],
  components: { viewCharge, caseCharge },
  data () {
    return {
      rules: { ...rules },
      viewchargeShow: false,
      flagCc: true,
      chakanCharge: false,
      bqbzCaseForm: {},
      chargeShow: false,
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
      appraiseFlag: "1",//是否启用手动评价,
      smallspeech: 'N',//是否打开引导语
      speechAddress: '',
      pjType:'1',//评价类型默认平板
      sjStatus:this.serviceForm.sjStatus
    };
  },
  computed: {
    ...mapGetters(["currentVirtualBusinessRecordOid"]),
    /** 是否选择了告知承诺 */
    informPromiseFlag ({ caseMaterialOids }) {
      return !!caseMaterialOids["7"];
    },

    /** 是否是容缺补正 */
    isRqslFlag () {
      return this.ruleForm.rqbzDueDate;
    },
    ...mapGetters(["currentVirtualBusinessRecordOid", "pjdjTime"]),
    ...mapGetters(["deviceMap"])
  },
  activated () {
    this.getAllMaterials();
  },
  created () {
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
        queryQlCaseApplayByCaseOid(this.caseOid).then(res => {
          this.caseUserName = res.data.applyUserName;
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
      // this.allMaterialOids = this.sxServiceMaterialList.filter(item.)
    },
    //办件平板评价
    // pushPjCaseService: debounce(function () {
    //   let _that = this;
    //   let userOid = _that.loginUser.userOid;
    //   pushPbpjUser(userOid).then(response => {
    //     //推送办件评价
    //     if (response.data != "") {
    //       if (response.data.appraiseFlag == "1") {
    //         if (_that.pjType == "1") {
    //           //推送平板评价
    //           _that.pushPbpj();
    //         } else {
    //           //推送智能评价
    //           _that.pushSmartpj();
    //         }
    //       } else { //没用启用评价 直接受理
    //         _that.caseAccpet();
    //       }
    //     }
    //   });
    // }, 500, true),

     //办件平板评价
    pushPjCaseService: debounce(function () {
      if (this.systemType == "1") {
        //1  开启评价
        if (this.appraiseFlag == "1") {
          if (this.ruleForm.acceptradio == 2) {
            if (this.ruleForm.finalOpinionDesc) {
              let manualEvaluation = {};
              manualEvaluation.virtualBusinessNum = this.currentVirtualBusinessRecordOid;
              manualEvaluation.caseNumber = this.caseNumber;
              manualEvaluation.caseUserName = this.caseUserName;
              manualEvaluation.phone = this.phone;
              manualEvaluation.evaluateSource = "1";
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
            manualEvaluation.evaluateSource = "1";
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
      } else {
        this.caseAccpet();
      }
    }, 500, true),

    //平板评价
    pushPbpj() {
      let _that = this;
      pushPbpjCheckLogin().then(response => {
        if (response.data) {
          //检查平板评价登录
          _that.$confirm('你确定要进行办件评价吗？', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(function() {
            pbpjSaveCaseInfo(_that.caseOid).then(response => {
              if (response.data != "") {
                let json = JSON.parse(response.data);
                if (json.state == "0") {
                  pushPbpjInfo(_that.caseNumber).then(response => {
                    if (response.data != null) {
                      _that.caseAccpet();
                    }
                  });
                }
              } else {
                _that.msgError("平板评价保存办件失败！");
                return false;
              }
            });
          }).catch(function(){
            _that.caseAccpet();
          });
        } else {
          _that.msgWarning("平板评价未登录，请登录平板评价器！");
          return false;
        }
      });
    },
    //智能评价
    pushSmartpj() {
      let _that = this;
      _that.$confirm('你确定要进行办件评价吗？', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        savePjMark(_that.caseOid).then(response => {
          let mess = JSON.parse(response.data);
          if (mess.code == 0) {
            _that.caseAccpet();
          } else {
            _that.msgError("平板评价保存办件失败！");
            return false;
          }
        });
      });
    },
    /** 初始化评价类型 */
    queryPjStartType() {
      let _that = this;
      getPjDeviceType().then(response => {
        _that.pjType = response.data;
      });
    },


    printNotice () {
      /*this.$confirm('是否打印告知承诺书?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.handleGzcnPrint();
      }).catch(() => { });*/
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
      // noticeMes.bzDate = this.dueDate;
      noticeMes.fileName = "gzs.doc";
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
      /*  if(this.chargeFlag =="0"){

          } */
      this.viewchargeShow = true;
    },
    qlCaseCharge () {
      // console.log(this.chargeFlag);
      /*  if(this.chargeFlag =="0"){

          } */
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
      data.createDate = this.ruleForm.createDate;
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
      this.$confirm("你确定要进行办件评价吗？", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        distinguishCancelAndClose: true,
        type: "warning"
      })
        .then(() => {
      this.caseAccpet();
      saveEvaluateCaseInfo(this.caseOid).then(response => {
        if (response.data != "") {
          let json = JSON.parse(response.data);
          if (json.state == "0") {
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
        } else {
          // this.msgError("平板评价保存办件失败！");
          this.$message.warning("平板评价保存办件失败！");
          return false;
        }
      });
      })
      .catch(action => {
        if (action === "cancel") {
          this.caseAccpet();
        }
      });
    },
    /** 办件受理 */
    caseAccpet () {
      if (Object.keys(this.caseMaterialOids).length > 0) {
        // 纸质
        let zzcaseMaterialOids = this.caseMaterialOids["1"];
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
      }
      console.log(this.mustOutcaseMaterialOids);
      if (this.caseOid != "") {
        let formData = new FormData();
        if (this.ruleForm.rqbzDueDate) {
          formData.append("rqbzDueDate", this.ruleForm.rqbzDueDate);
        }
        formData.append("caseOid", this.caseOid);
        formData.append("userOid", this.loginUser.userOid);
        formData.append("userName", this.loginUser.userName);
        formData.append("finalOpinion", this.ruleForm.acceptradio);
        formData.append("finalOpinionDesc", this.ruleForm.finalOpinionDesc);
        formData.append("sjStatus", this.serviceForm.sjStatus!=2?1:2);
        if (this.ruleForm.acceptradio == 2) {
          if (!this.ruleForm.finalOpinionDesc) {
            this.$message.error("意见说明不能为空！");
            return false;
          }
        }
        saveCaseAccpet(formData).then(response => {
          if (response.data != "") {
            this.linkResultOid = response.data.linkResultOid;
            let noticeMes = {};
            if (this.cegisterType == 0) {
              noticeMes.applyUserName = this.ruleForm.applyUserName;
              noticeMes.applyCompany = "";
            } else {
              noticeMes.applyCompany = this.ruleForm.applyUserName;
              noticeMes.applyUserName = "";
            }
            noticeMes.organName = this.serviceForm.organName;
            noticeMes.phone = this.ruleForm.applyUserPhone;
            noticeMes.serviceName = this.serviceForm.serviceName;
            noticeMes.zxPhone = this.serviceForm.zxDhText;
            noticeMes.caseNumber = this.caseNumber;
            noticeMes.slTime = response.data.accpectTime;
            noticeMes.jbrName = response.data.personName;
            noticeMes.caseOid = this.caseOid;
            noticeMes.sjrName = this.loginUser.userName;
            noticeMes.delivery = this.ruleForm.resultDeliveryWay;
            noticeMes.pushUrl = window.location.origin + "/serviceHall/caseInfo.html?caseOid=" + this.caseOid;
            noticeMes.sjStatus=this.sjStatus;
            if (noticeMes.delivery.indexOf("1") > -1) {
              noticeMes.jsrName = this.ruleForm.addresseeName;
              noticeMes.jsrPhone = this.ruleForm.addresseePhone;
              noticeMes.jsrAddress = this.ruleForm.addresseeDetailAddress;
            }

            if (this.ruleForm.acceptradio == 2) {
              noticeMes.resonDesc = this.ruleForm.finalOpinionDesc;
              noticeMes.materials = this.allMaterialNames;
              noticeMes.isRqslFlag = "";
              noticeMes.fileName = "bysltzs.docx";
              noticeMes.isSl = "false";
              this.$message.success("办件不予受理保存成功！");
              this.$emit("case-close", noticeMes); //不予受理通知书
            } else {
              if(this.serviceForm.sjStatus && this.serviceForm.sjStatus==2){
                this.$message.success("办件进入受理成功！");
              }else{
                this.$message.success("办件收件成功！");
              }

              //保存补齐补正信息
              // if (this.dueDate) {
              //   this.saveCaseCorrection();
              // }
              // 需要出库的
              if (this.mustOutcaseMaterialOids && !this.dueDate) {
                //不是容缺的的材料进行出库信息保存
                this.saveMaterialOut(this.mustOutcaseMaterialOids);
              }
              if (this.ruleForm.rqbzDueDate) {
                noticeMes.rqbzDueDate = this.ruleForm.rqbzDueDate;
                //打印容缺受理通知书
                noticeMes.bzMaterials = this.rqbzMaterialNames;
                noticeMes.fileName = "rqsltzs.docx"; //通知书名称必须
                noticeMes.isRqslFlag = "true";
                this.$emit("case-close", noticeMes);
              } else if (this.informPromiseFlag) {
                //告知承诺
                // noticeMes.materials = this.allMaterialNames;
                noticeMes.fileName = "gzs.docx"; //通知书名称必须
                noticeMes.isRqslFlag = "";
                noticeMes.isGzSl = "true";
                noticeMes.isSl = "";
                this.$emit("case-close", noticeMes);
              } else {
                noticeMes.fileName = "sjtzs.docx"; //通知书名称必须
                if(this.serviceForm.sjStatus && this.serviceForm.sjStatus==2){//收件及受理
                  noticeMes.fileName = "sltzs.docx"; //通知书名称必须
                }
                //受理通知书
                //noticeMes.materials = this.allMaterialNames;
                noticeMes.isRqslFlag = "";
                noticeMes.isGzSl = "";
                noticeMes.isSl = "true";
                this.$emit("case-close", {
                  ...noticeMes,
                  // 不是补齐补正的
                  // isAllowMaterialOut:
                  //   !this.dueDate && !!this.mustOutcaseMaterialOids
                  isAllowMaterialOut:
                    !this.informPromiseFlag && !!this.mustOutcaseMaterialOids
                });
              }
            }
          }
        });
      } else {
        this.$message.warning("办件信息未保存，请先保存！");
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

<style scoped lang="scss"></style>
