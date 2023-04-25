<!-- 一次告知 -->
<template>
  <div class="infoKnow-for-once" v-loading="loading">
    <el-scrollbar class="common-dialog-content">
      <div class="data-box">
        <div class="common-dialog--title">事项基本信息</div>
        <table class="zf-zc-table">
          <colgroup>
            <col width="15%" />
            <col width="35%" />
            <col width="15%" />
            <col width="35%" />
          </colgroup>
          <tbody>
          <tr>
            <td><b>事项名称：</b></td>
            <td>{{ serviceForm.serviceName }}</td>
            <td><b>事项类型：</b></td>
            <td>{{ serviceForm.serviceTypeName }}</td>
          </tr>
          <tr>
            <td><b>实施机构：</b></td>
            <td>{{ serviceForm.organName }}</td>
            <td><b>是否收费：</b></td>
            <td>{{ sxService.chargeFlagName }}</td>
          </tr>
          <tr v-if="sxService.chargeFlag == 1">
            <td><b>收费依据：</b></td>
            <td>{{ serviceForm.sxServiceExtend.chargeAccord }}</td>
            <td><b>收费标准：</b></td>
            <td>{{ serviceForm.sxServiceExtend.chargeStandard }}</td>
          </tr>
          <tr>
            <td><b>办理方式：</b></td>
            <td>{{ sxService.handleFormName }}</td>
            <td><b>领取方式：</b></td>
            <td>{{ sxService.resultDeliveryWayName }}</td>
          </tr>
          <tr>
            <td><b>承诺时限：</b></td>
            <td>
              <template>
                {{ serviceForm.sxServiceExtend.promiseLimit }}
                <span
                  v-if="serviceForm.sxServiceExtend.promiseLimitType == 'W'"
                >
                    (工作日)</span
                >
                <span
                  v-if="serviceForm.sxServiceExtend.promiseLimitType == 'N'"
                >
                    (自然日)</span
                >
                <span
                  v-if="serviceForm.sxServiceExtend.promiseLimitType == 'H'"
                >
                    (小时)</span
                >
              </template>
            </td>
            <td><b>法定时限：</b></td>
            <td>
              <template>
                {{ serviceForm.sxServiceExtend.legalLimit }}
                <span
                  v-if="serviceForm.sxServiceExtend.legalLimitType == 'W'"
                >
                    (工作日)</span
                >
                <span
                  v-if="serviceForm.sxServiceExtend.legalLimitType == 'N'"
                >
                    (自然日)</span
                >
                <span
                  v-if="serviceForm.sxServiceExtend.legalLimitType == 'H'"
                >
                    (小时)</span
                >
              </template>
            </td>
          </tr>
          <!--            <tr>
            <td><b>办理时间：</b></td>
            <td colspan="3">
              <div
                v-for="(data, index) in serviceForm.sxServiceLocations"
                :label="data.acceptDate"
                :key="index"
              >
                {{ data.acceptDate }}
              </div>
            </td>
&lt;!&ndash;              <td><b>办理地点：</b></td>
            <td>
              <div
                v-for="(data, index) in serviceForm.sxServiceLocations"
                :label="data.locationName"
                :key="index"
              >
                {{ data.locationName }}
              </div>
            </td>&ndash;&gt;
          </tr>-->
          <tr>
            <td><b>监督电话：</b></td>
            <td>{{ serviceForm.tsDhText }}</td>
            <td><b>咨询电话：</b></td>
            <td>{{ serviceForm.zxDhText }}</td>
          </tr>
          <tr>
            <td><b>结果名称：</b></td>
            <td>{{ serviceForm.sxServiceExtend.resultName }}</td>
            <td><b>结果送达方式：</b></td>
            <td>{{ serviceForm.resultDeliveryWayName }}</td>
          </tr>
          <tr v-if="serviceForm.unionOrganFlag == 1">
            <td><b>是否联合办理：</b></td>
            <td>{{ serviceForm.unionOrganFlagName }}</td>
            <td><b>联合办理机构：</b></td>
            <td>{{ serviceForm.unionOrgan }}</td>
          </tr>
          <tr>
            <td><b>是否有特殊程序：</b></td>
            <td>{{ serviceForm.isSpecialName }}</td>
            <td><b>结果样本类型：</b></td>
            <td>
                <span v-if="serviceForm.sxServiceExtend.resultSampleType == 0">
                  证照
                </span>
              <span v-if="serviceForm.sxServiceExtend.resultSampleType == 1">
                  批文
                </span>
              <span v-if="serviceForm.sxServiceExtend.resultSampleType == 2">
                  其他
                </span>
            </td>
          </tr>
          <tr>
            <td><b>申请受理条件：</b></td>
            <td colspan="3">
              <div
                v-for="(data, index) in serviceForm.sxAcceptConditions"
                :label="data.conditionText"
                :key="index"
              >
                <p v-if="serviceForm.sxAcceptConditions.length > 1">
                  {{ index + 1 }}、 {{ data.conditionText }}
                </p>
                <p v-else>
                  {{ data.conditionText }}
                </p>
              </div>
            </td>
          </tr>
          <tr>
            <td><b>设定依据：</b></td>
            <td colspan="3">
              {{ serviceForm.sxServiceExtend.setAccord }}
            </td>
          </tr>
          <tr>
            <td><b>办事流程：</b></td>
            <td colspan="3">
              <div
                v-for="(data, index) in serviceForm.sxServiceLinks"
                :label="data.linkName"
                :key="index"
              >
                <p v-if="serviceForm.sxServiceLinks.length > 1">
                  {{ index + 1 }}、{{ data.linkName }}
                </p>
                <p v-else>
                  {{ data.linkName }}
                </p>
              </div>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
      <div class="data-box">
        <div class="data-box--title">
          <div class="common-dialog--title">材料列表</div>
          <div class="data-box--tip">
            <div class="data-box--tip-item">
              <img
                :src="require('@/assets/image/intelligent/gzcn.png')"
                width="10px"
                alt=""
              />
              <span>告知承诺</span>
            </div>
            <div class="data-box--tip-item">
              <img
                :src="require('@/assets/image/intelligent/rqsl.png')"
                width="10px"
                alt=""
              />
              <span>容缺受理</span>
            </div>
          </div>
        </div>
        <table border="0" class="zf-zc-table--common zf-zc-table--td-center">
          <colgroup>
            <col width="10%" />
            <col width="30%" />
            <col width="15%" />
            <col width="15%" />
            <col width="15%" />
          </colgroup>
          <tr>
            <th>序号</th>
            <th>材料名称</th>
            <th>材料类型</th>
            <th>材料形式</th>
            <th>份数</th>
            <th>必要性</th>
          </tr>
          <tbody
            v-if="sxServiceMaterialList.length > 0"
            style="background: #fff"
          >
          <tr
            v-for="(data, index) in sxServiceMaterialList"
            :key="index"
            empty-text="暂无我的待办"
          >
            <td>{{ index + 1 }}</td>
            <td
              style="text-align: left !important"
              :class="[
                  data.materialFormat == '4' ? 'rqbz--tip' : '',
                  data.materialFormat == '7' ? 'inform-promise--tip' : ''
                ]"
            >
              {{ data.materialName }}
            </td>
            <td>
              <template>
                <span v-if="data.materialType == '0'">原件</span>
                <span v-if="data.materialType == '1'">复印件</span>
                <span v-if="data.materialType == '2'"> 原件和复印件 </span>
              </template>
            </td>
            <td>
              <template>
                <span v-if="data.materialFormat == '1'">纸质</span>
                <span v-if="data.materialFormat == '2'">电子版</span>
                <span v-if="data.materialFormat == '3'">证照</span>
                <span v-if="data.materialFormat == '4'"> 容缺补正 </span>
                <span v-if="data.materialFormat == '7'"> 告知承诺 </span>
              </template>
            </td>
            <td>{{ data.paperNumber }}</td>
            <td>
              <template>
                  <span
                    class="bage-necessery"
                    v-if="data.materialMustFlag == '0'"
                  >
                    必要
                  </span>
                <span v-if="data.materialMustFlag == '1'">不必要</span>
                <span v-if="data.materialMustFlag == '2'">容缺后补</span>
              </template>
            </td>
          </tr>
          </tbody>
          <tbody v-else>
          <tr>
            <td colspan="6" style="text-align: center; background: #fff">
              暂无数据
            </td>
          </tr>
          </tbody>
        </table>
      </div>
      <!-- <div style="height: 5px"></div>
      <div
        style="
          text-align: center;
          position: absolute;
          bottom: 10px;
          width: 100%;
        "
      >
        <el-checkbox checked v-model.trim="radio" label="1">
          已核验上述材料并确定齐全
        </el-checkbox>
      </div> -->
    </el-scrollbar>
    <div class="common-dialog-footer">
      <el-button
        style="background: #3d5fb5; color: #fff"
        @click="openNoticeForm"
      >
        打印告知单
      </el-button>
      <el-button type="primary" @click="$emit('lastStep', 1)">上一步</el-button>
      <el-button
        v-if="serviceForm.showRzhs == '1'"
        style="background: #3d5fb5; color: #fff"
        @click="beforeGetImageCamera"
      >
        人证对比
      </el-button>
      <el-button type="primary" @click="nextStep">下一步并确认齐全</el-button>
      <div v-if="dialogVisible">
        <el-dialog
          v-dialog-drag
          :visible.sync="dialogVisible"
          width="1158px"
          height="800px"
          scrollbar
          :before-close="handleClose"
          :close-on-click-modal="false"
          :close-on-press-escape="false"
          append-to-body
          custom-class="common-dialog once-in-form"
          title="一次性告知单"
        >
          <OnceInform
            ref="onceNoticeForm"
            :serviceOid="serviceOid"
            :sxServiceMaterialList="sxServiceMaterialList"
            :situationCheckList="situationCheckList"
            :currentHotSituationName="currentHotSituationName"
          />
          <div style="text-align: center" slot="footer">
            <el-button type="primary" id="trueBtn" v-print="printObj">
              打印
            </el-button>
            <el-button @click="handleClose">关闭</el-button>
          </div>
        </el-dialog>
      </div>
    </div>

    <!-- 人证比对 -->
    <div v-if="hardWareVisible">
      <el-dialog
        v-dialog-drag
        :visible.sync="hardWareVisible"
        width="1158px"
        top="10vh"
        style="padding: 21px 16px 16px 19px;"
        @close="closeHard"
        :close-on-click-modal="false"
        :close-on-press-escape="false"
        append-to-body
        custom-class="hardware-scan"
        title="人证比对"
      >
        <HardWareScan
          ref="scanForm"
          :caseOid="caseOid"
          :loginUser="loginUser"
          @close="closeHard"
        />
      </el-dialog>
    </div>
    <el-dialog
      class="userInfoDialoag"
      title="录入申请人/申请单位基本信息"
      :visible.sync="userInfoVisible"
      :fullscreen="showFull"
      width="900px"
      height="467px"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      append-to-body
      custom-class="userInfo-scan"
      @close="closeUserInfoDialog"
    >
      <template slot="title">
        <div class="userInfoDialoagTitle">
          <span>录入申请人/申请单位基本信息</span>
          <img
            @click="flexDialoag"
            :src="require('@/assets/image/intelligent/flexDialoag.png')"
            alt=""
          />
        </div>
      </template>
      <informationEntryPopup
        :cegisterType="cegisterType"
        :caseOid="caseOid"
        :serviceOid="serviceOid"
        :checkUserInfo="checkUserInfo"
        @checkUserData="checkUserData"
      ></informationEntryPopup>
      <template slot="footer">
        <div class="footBtn">
          <el-button style="background: #E7EDF3;" @click="closeUserInfoDialog"
          >取消</el-button
          >
          <el-button type="primary" @click="ConfirmInformation">确定</el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog
      v-dialog-drag
      title="条件预检"
      :visible.sync="inspectionCondition"
      append-to-body
      footerCenter
      width="1200px"
      height="563px"
      scrollbar
    >
      <Verfication ref="verfication" @checkVerFication="checkVerFication" :serviceOid="serviceOid" :fillUserInfo="fillUserInfo" :inspectionCondition="inspectionCondition" :cegisterType="cegisterType" />
      <span slot="footer">
        <el-button type="primary" @click="nextStepPage">下一步</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getSxService,
  save
} from "@/api/zc/businessManagement/windowAcceptance";
import { listSxConditionalRules } from "@/api/zc/qdgl/sxService";
//import { showCallMessage } from "@/api/zc/businessManagement/doubleScreenInteraction";
import OnceInform from "@/views/zc/businessManagement/windowAcceptance/components/onceInform";
import { mapGetters } from "vuex";
import GPYDrive from "@/api/handwareDrive.js";
import informationEntryPopup from "./informationEntryPopup.vue";
// 人证比对
import HardWare from "@/mixins/hardware";
//条件预检
import Verfication from "./verfication.vue";
export default {
  name: "InfoKnowForOnce",
  inheritAttrs: false,
  mixins: [HardWare],
  props: {
    loginUser: {
      type: Object,
      default: () => ({})
    },
    serviceOid: {
      type: String,
      default: ""
    },
    currentHotSituationName: {
      type: String,
      default: ""
    },
    sxServiceMaterialList: {
      type: Array,
      default: () => []
    },
    situationCheckList: {
      type: Array,
      default: () => []
    },
    isQlCaseChanged: Boolean,
    caseOid: String,
    serviceName:String
  },
  components: { OnceInform, informationEntryPopup, Verfication },
  data() {
    return {
      printObj: {
        id: "print",
        popTitle: "一次性告知单",
        extraHead:
          '<meta http-equiv="Content-Language"content="zh-cn"/>,<style> #print { height: auto !important; } <style>'
      },
      serviceForm: {
        serviceName: "",
        serviceTypeName: "",
        organName: "",
        sxServiceLocations: {},
        zxDhText: "",
        sjStatus: 1,
        sxServiceExtend: {},
        sxAcceptConditions: {},
        sxServiceLinks: {},
        sxServiceQuestions: {}
      },
      //事项
      sxService: {},
      radio: "1",
      firstEnter: true,
      dialogVisible: false,
      userInfoVisible: true,
      showFull: false,
      cegisterType: "0",
      inspectionCondition: false,
      checkUserInfo: false,
      fillUserInfo: {},
      baseUserInfo: {},
      checkVerFicationData:[],
      noVerificationData:false
    };
  },
  computed: {
    saveComponentIndex() {
      return this.$store.state.config.saveComponentIndex;
    },
    ...mapGetters(["currentPersonNum", "startCall"])
  },
  created() {
    this.getServiceDetail();
    this.cegisterType = this.$attrs.cegisterType;
  },
  activated() {
    this.cegisterType = this.$attrs.cegisterType;
    if (!this.firstEnter) {
      this.showGuidence();
    }
    this.userInfoVisible = true;
  },
  watch: {
    saveComponentIndex: {
      handler(val) {
        if (val === 2) {
          this.initCase();
        }
      }
    }
  },
  methods: {
    /** 获取事项详细信息 */
    getServiceDetail() {
      this.loading = true;
      getSxService(this.serviceOid)
        .then(({ code, data, message }) => {
          if (code !== 200) {
            this.loading = false;
            return this.$message.warning(message || "接口请求失败");
          }
          this.loading = false;
          this.sxService = data.sxService;
          this.serviceForm = {
            ...this.serviceForm,
            serviceName: data.sxService.serviceName,
            serviceTypeName: data.sxService.serviceTypeName,
            organName: data.sxService.organName,
            sxServiceLocations: data.sxServiceLocations,
            zxDhText: data.sxService.zxDhText,
            tsDhText: data.sxService.tsDhText,
            sxServiceExtend: data?.sxServiceExtend ?? {},
            sxAcceptConditions: data.sxAcceptConditions,
            sxServiceLinks: data.sxServiceLinks,
            sxServiceQuestions: data.sxServiceQuestions,
            showRzhs: data.sxService.showRzhs,
            resultDeliveryWayName: data.sxService.resultDeliveryWayName,
            isSpecialName: data.sxService.isSpecialName,
            sjStatus: data.sxService.sjStatus
          };
          this.$emit("setServiceFormData", {
            serviceName: this.serviceForm.serviceName,
            zxDhText: this.serviceForm.zxDhText,
            serviceTypeName: this.serviceForm.serviceTypeName,
            showRzhs: this.serviceForm.showRzhs,
            organName: this.serviceForm.organName,
            sjStatus: this.serviceForm.sjStatus
          });
          this.$emit("setHotSituationName", this.currentHotSituationName);
          this.showGuidence();
          this.firstEnter = false;
        })
        .catch(() => {
          this.loading = false;
        });
    },

    //超级综窗柜台双屏显示办事指南信息
    /* async showGuidence () {
      let url = window.location.origin;
      let pushUrl = url + "/serviceHall/guidence.html";
      let data = {};
      let sxService = {};
      let materialList = [];
      if (this.sxServiceMaterialList.length > 0) {
        this.sxServiceMaterialList.forEach(material => {
          let ma = {};
          ma.materialName = material.materialName;
          ma.paperNumber = material.paperNumber;
          materialList.push(ma);
        });
      }
      data.userName = this.loginUser.userName;
      data.organName = this.loginUser.organName;
      data.sxServiceMaterialList = materialList;
      sxService.serviceName = this.sxService.serviceName;
      sxService.chargeFlagName = this.sxService.chargeFlagName;
      sxService.chargeFlag = this.sxService.chargeFlag;
      sxService.zxDhText = this.sxService.zxDhText;
      sxService.tsDhText = this.sxService.tsDhText;
      sxService.promiseLimit = this.serviceForm?.sxServiceExtend?.promiseLimit;
      sxService.legalLimit = this.serviceForm?.sxServiceExtend?.legalLimit;
      sxService.handleFormName = this.sxService.handleFormName;
      data.sxService = sxService;
      pushUrl = pushUrl + "?jsonObject=" + JSON.stringify(data);
      // process.env.NODE_ENV === 'development' && window.open(pushUrl);
      await showCallMessage(pushUrl).then(response => {
        console.log(response);
      });
    },*/

    nextStep() {
      if (this?.serviceForm?.showRzhs == "1") {
        if (!this.rzbdResult) return this.$message.warning("请先完成人证比对!");
        if (this.rzbdResult == "2")
          return this.$message.warning("人证比对不通过, 请重新比对");
      }
      this.checkBaseInfo(this.fillUserInfo);

//有条件预检数据
if(!this.noVerificationData){
 const validate = this.checkVerFicationData.every(item=>{
        item.status === 1
      })
      //条件预检不通过
      if(!validate){
        //  this.inspectionCondition = true;
        //   return
      }
}
if(this.userInfoVisible){
  return
}
      this.getCheckbox();
  
    },
    openNoticeForm() {
      this.dialogVisible = true;
    },
    handleClose() {
      this.dialogVisible = false;
    },

    /** 核验材料勾选 */
    getCheckbox() {
      // if (this.radio) {
      if (this.isQlCaseChanged) {
        this.initCase();
      } else {
        this.$emit("nextStep", 3);
      }
      // } else {
      //   this.$message.warning("请勾选我已核验上述材料并确定齐全！");
      //   return false;
      // }
    },

    /** 办件信息下一步保存 */
    initCase(isRzhy = false) {
      // console.log('%c [qlCaseTitleValList]:', 'color:red;font-weight:700;', this.$attrs.qlCaseTitleValList);
      const loading = this.$loading({
        lock: true,
        text: "正在保存表单信息",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
      return save({
        id: "",
        caseOid: this.caseOid,
        applyOid: "",
        extOid: "",
        cegisterType: this.$attrs.cegisterType,
        serviceOid: this.serviceOid,
        caseNumber: "",
        sxServiceMaterialList: this.sxServiceMaterialList,
        qlCaseTitleValList: this.$attrs.qlCaseTitleValList,
        currentPersonNum: this.startCall ? this.currentPersonNum : "",
        fillUserInfo: this.fillUserInfo,
        credentialNumber:this.fillUserInfo.credentialNumber,
        applyUserName:this.fillUserInfo.applyUserName,
        credentialType:this.fillUserInfo.credentialType,
        caseStatus:'0',
        sourceApp:1,
        projectName:this.serviceName
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
              createDate: response.data.createDate,
              fillUserInfo: this.fillUserInfo,
            });

            this.$store.commit("SET_CASE_OID", response.data.caseOid);

            if (isRzhy) return;

            //加载材料附件
            if (this.saveComponentIndex === 2) {
              this.$store.commit("setSaveComponentIndex", 0);
              this.$emit("close");
            } else {
              this.$emit("nextStep", 3);
            }
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
    },

    beforeGetImageCamera() {
      if (this.caseOid) {
        return this.getImageCamera();
      }
      this.initCase(true).then(() => {
        this.$nextTick(() => {
          this.getImageCamera();
        });
      });
    },
    //是否将用户信息录入弹窗展开全屏
    flexDialoag() {
      this.showFull = !this.showFull;
    },
    //关闭用户信息录入弹窗
    closeUserInfoDialog() {
      this.userInfoVisible = false;
    },
    //确认用户信息
    ConfirmInformation() {
      listSxConditionalRules({ serviceOid: this.serviceOid,ruleType:1 }).then(res=>{
        if (res.code == 200){
           this.checkUserInfo = true;
        this.userInfoVisible = false
          if(!res.data.length){
              this.noVerificationData = true
          }
        }
      })
     
    },
    checkUserData(data) {
      this.checkBaseInfo(data);
      if (!this.userInfoVisible && !this.noVerificationData) {
        this.inspectionCondition = true;
      }
    },
    checkBaseInfo(data) {
      if (!data.applyUserName) {
        this.userInfoVisible = true;
        this.checkUserInfo = false;
        if(this.cegisterType==='1'){
           this.$message.warning("请填写申请人姓名")
           return
        }else{
           this.$message.warning("请填写申请单位信息")
          return
        }
      }
      if (!data.credentialNumber) {
        this.userInfoVisible = true;
        this.checkUserInfo = false;
         this.$message.warning("请填写证件号码")
         return
      }
      this.checkUserInfo = false;
      this.fillUserInfo = data;
      this.userInfoVisible = false;
    },
    checkVerFication(data){
      this.checkVerFicationData = data
    },
    //条件预检下一步
    nextStepPage() {
    const validate = this.checkVerFicationData.every(item=>{
        item.status === 1
      })
      if(!validate){
        try{
      this.checkVerFicationData.forEach(item=>{
          if(item.status!=1){
            throw( this.$message.warning(
          `核验名称【${item.rulesName}】未通过核验`
        ))
          }
         })
        }catch(e){
        }
      }
      this.inspectionCondition = false;
    }
  }
};
</script>

<style scoped lang="scss">
.bage-necessery {
  width: 75px;
  height: 28px;
  background: #fbe1cb;
  border-radius: 14px;
  display: inline-block;
  line-height: 28px;
  color: rgba(255, 60, 0, 0.8);
}

>>> .common-dialog.once-in-form {
  .el-dialog__body,
  .el-dialog__footer {
    background-color: #fff;
  }
}

.data-box {
  .data-box--title {
    display: flex;
    align-items: center;
    justify-content: space-between;

    .data-box--tip {
      display: flex;
      align-items: center;
      color: #cbcccf;

      .data-box--tip-item:nth-child(1) {
        margin-right: 20px;
      }
    }
  }
}

.rqbz--tip {
  position: relative;

  &::before {
    content: "";
    position: absolute;
    right: 0px;
    top: 0px;
    background: url(~@/assets/image/intelligent/rqsl.png) center no-repeat;
    width: 10px;
    height: 10px;
    background-size: 100%;
  }
}

.inform-promise--tip {
  position: relative;

  &::before {
    content: "";
    position: absolute;
    right: 0px;
    top: 0px;
    background: url(~@/assets/image/intelligent/gzcn.png) center no-repeat;
    width: 10px;
    height: 10px;
    background-size: 100%;
  }
}
.userInfoDialoag {
  display: flex;
  align-items: center;
  justify-content: center;
}
::v-deep .userInfo-scan {
  left: 0 !important;
  top: 0 !important;
  .userInfoDialoagTitle {
    width: calc(100% - 70px);
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding-right: 10px;
    span {
      font-size: 18px;
      font-family: PingFang SC;
      font-weight: 500;
      color: #ffffff;
    }
    img {
      width: 17px;
      height: 17px;
      cursor: pointer;
    }
  }
  .el-dialog__body {
    padding: 21px 16px 16px 19px !important;
  }
}
</style>
<style>
.el-message{
  z-index: 10000!important;
}
</style>
