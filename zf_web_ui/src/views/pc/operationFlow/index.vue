<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-07-20 17:00:15
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-11-10 09:46:22
 * @FilePath: \hpNewHall\src\views\operationFlow\index.vue
 ** 服务主窗口
-->
<template>
  <div
    class="conponent-container"
    id="con_lf_top_div"
    ref="fullScreen"
    :class="fullscreen ? 'screenAll' : ''"
  >
    <div class="header">
      <div class="serviceUser" v-if="staffInformation.length">
        <p class="leftBtn" @click="lastUser">
          <img :src="require('@/assets/images/home/leftIcon.png')" alt="" />
        </p>
        <div class="userContent" ref="userContent">
          <template v-for="(item, index) in staffInformation">
            <div
              v-if="index >= nextIndex"
              :ref="
                currentServiceIndex === index
                  ? 'currentServices'
                  : 'serviceTabs'
              "
              :class="
                currentServiceIndex === index
                  ? 'currentServices'
                  : 'serviceTabs'
              "
              @click="checkUser(item, index)"
              :key="index"
            >
              <div class="left">
                <img
                  :src="
                    currentServiceIndex === index
                      ? require('@/assets/images/home/calling.png')
                      : require('@/assets/images/home/opcityCalling.png')
                  "
                  alt=""
                />
                <p>
                  <span v-show="currentServiceIndex === index">当前服务：</span>

                  <el-tooltip
                    :content="item.name"
                    placement="top"
                    effect="light"
                    :disabled="tooltipDisable"
                  >
                    <span @mouseenter="isShowTooltip" class="username">{{
                      item.name
                    }}</span>
                  </el-tooltip>
                </p>
              </div>
              <div
                class="right"
                v-show="currentServiceIndex === index"
                @click="closeService(item)"
              >
                <img
                  :src="require('@/assets/images/home/endService.png')"
                  alt=""
                />
                <span>结束服务</span>
              </div>
            </div>
          </template>
        </div>
        <p class="nextBtn" @click="nextUser">
          <img :src="require('@/assets/images/home/rightIcon.png')" alt="" />
        </p>
      </div>
      <div class="noDataServices" v-if="!staffInformation.length"></div>
      <div class="rightBtn">
        <div class="btn" @click="toCall" v-if="baseUserInfo.takeNumberType != 5">
          <img
            :src="require('@/assets/video/2.png')"
            alt=""
          />
          <span>呼叫帮办</span>
        </div>
        <div class="btn" @click="toContinue" v-if="baseUserInfo.processStep == 'baseInfo'">
          <img
            :src="require('@/assets/images/home/scan.png')"
            alt=""
          />
          <span>一码续办</span>
        </div>
        <div class="btn" @click="toTable" v-if="baseUserInfo.processStep == 'baseInfo' && isHelp == '2'">
          <img
            :src="require('@/assets/images/home/computer.png')"
            alt=""
          />
          <span>一桌联办</span>
        </div>
        <div class="btn" @click="toVisitingRecords">
          <img
            :src="require('@/assets/images/home/visitingRecords.png')"
            alt=""
          />
          <span>办事记录</span>
        </div>
        <div class="btn" @click="toServiceTurn">
          <img
            :src="require('@/assets/images/home/serviceTransfer.png')"
            alt=""
          />
          <span>服务转派</span>
        </div>
        <div class="btn" @click="getWindowNumber">
          <img
            :src="require('@/assets/images/home/windowNumbering.png')"
            alt=""
          />
          <span>窗口取号</span>
        </div>
        <div class="screen" @click="screen">
          <img :src="require('@/assets/images/home/screen.png')" alt="" />
        </div>
      </div>
    </div>
    <div class="content">
      <div class="containerHeade">
        <div class="left">
          <p
            class="tips"
            v-if="
              baseUserInfo.processStep === 'baseInfo' && staffInformation.length
            "
          >
            来访人员详情
          </p>
          <p class="tips" v-if="baseUserInfo.processStep === 'mattersHandling'">
            请选择需要办理的事项
          </p>
          <p class="tips" v-if="baseUserInfo.currentStepIndex">
            正在办理：<span v-html="baseUserInfo.specificMatters.serviceName">{{
            }}</span>
          </p>
        </div>
        <div class="right">
          <p class="rightSpan" @click="getAdviseMemo" v-if="baseUserInfo.serviceTime">
            <span>备注</span>
          </p>
          <p class="serviceTime" v-if="baseUserInfo.serviceTime">
            <img :src="require('@/assets/images/home/timeIcon.png')" alt="" />
            服务时间：
            <span>{{ baseUserInfo.serviceTime }}</span>
          </p>
          <el-popover
            placement="bottom"
            trigger="click"
            v-show="
              baseUserInfo.processStep != 'baseInfo' &&
              baseUserInfo.processStep != 'mattersHandling'
            "
          >
            <div class="popover-content">
              <ul>
                <li
                  class="steps"
                  v-for="(item, index) in componentStepList"
                  :key="index"
                >
                  <div class="top">
                    <div
                      :class="
                        baseUserInfo.currentStepIndex >= item.step
                          ? 'activeStep'
                          : 'circularIcon'
                      "
                    >
                      <p>{{ item.step }}</p>
                    </div>
                    <span>{{ item.name }}</span>
                  </div>
                  <div class="blew" v-if="index != 3">
                    <img
                      v-if="baseUserInfo.currentStepIndex >= item.step"
                      :src="require('@/assets/images/home/progressBar.png')"
                      alt=""
                    />
                    <p v-else></p>
                  </div>
                </li>
              </ul>
            </div>
            <div
              class="step"
              slot="reference"
              @click="showStpe"
              v-show="
                baseUserInfo.currentStepIndex && baseUserInfo.serviceType != '1'
              "
            >
              <p class="stepInfo">
                第 <span>{{ baseUserInfo.currentStepIndex }}</span> 步：<span>{{
                  baseUserInfo.componentName
                }}</span>
              </p>
              <img
                :src="
                  stepShow
                    ? require('@/assets/images/pad/pullDown.png')
                    : require('@/assets/images/pad/pullUp.png')
                "
                alt=""
              />
            </div>
          </el-popover>
        </div>
      </div>
      <div class="process-content">
        <!-- <iframe v-show="iframeState" id="show-iframe"  frameborder=0 name="showHere" scrolling=auto :src="aa"></iframe> -->
        <!-- <keep-alive> -->
        <template v-if="!staffInformation.length">
          <baseInfo ref="baseInfo" @setStaffInformation="setStaffInformation" />
        </template>
        <template v-else>
          <template v-for="(item, index) in staffInformation">
            <component
              :key="index"
              v-if="currentServiceIndex === index"
              :staffInformation="staffInformation"
              :currentServiceIndex="currentServiceIndex"
              :is="item.processStep"
              :baseUserInfo="item"
              :basicUserInfo="basicUserInfo"
              :serviceType="item.serviceType"
              :specificMatters="item.specificMatters"
              :serviceRoot="serviceRoot"
              :sxServiceMaterialList="item.sxServiceMaterialList"
              :situationCheckList="item.situationCheckList"
              :valOids="item.valOids"
              :qlCaseTitleValList="item.qlCaseTitleValList"
              :caseOid="item.caseOid"
              :fillUserInfo="item.fillUserInfo"
              :formFillInfosList="item.formFillInfosList"
              :caseNumber="item.caseNumber"
              :caseMaterialOids="caseMaterialOids"
              :sxServiceMaterialAttaList="item.sxServiceMaterialAttaList"
              :materialList="item.materialList"
              :situationAnswerList="situationAnswerList"
              :associatedAttachmentsList="item.associatedAttachmentsList"
              :comeFormArtific="comeFormArtific"
              :skipSmartForms="item.skipSmartForms"
              :companyHistoryInfo="companyHistoryInfo"
              :companyHistoryStopList="companyHistoryStopList"
              @nextStep="nextStep"
              @setHaWorkServiceId="(data) => (item.haWorkServiceId = data)"
              @setResetBaseUserInfo="setResetBaseUserInfo"
              @spliceStaffInformation="spliceStaffInformation"
              @setServiceType="(data) => (item.serviceType = data)"
              @setMaterialList="(data) => (item.materialList = data)"
              @setAssociatedAttachmentsList="
                (data) => (item.associatedAttachmentsList = data)
              "
              @setSpecificMatters="setSpecificMatters"
              @setBaseUserInfo="setBaseUserInfo"
              @setSkipSmartForms="(data) => (item.skipSmartForms = data)"
              @lastStep="lastStep"
              @setQlCaseTitleValList="
                (data) => (item.qlCaseTitleValList = data)
              "
              @setData="setData"
              @caseMaterialOids="(data) => (caseMaterialOids = data)"
              @setFillUserInfo="(data) => (item.fillUserInfo = data)"
              @setFormFillInfosList="(data) => (item.formFillInfosList = data)"
              @setSxServiceMaterialList="
                (data) => (item.sxServiceMaterialList = data)
              "
              @setSituationCheckList="setSituationCheckList"
              @setValOids="(data) => (item.valOids = data)"
              @close="$emit('update:commmonVisible', false)"
              @showDialog="showDialog"
            >
            </component>
          </template>
        </template>
        <!-- </keep-alive> -->
      </div>
    </div>
    <!-- 一码续办窗口 -->
    <el-dialog
      title="一码续办"
      :visible.sync="showContinue"
      width="760px"
      custom-class="serviceContinue"
      v-dialogDrag
    > 
      <div slot="title">
        <div class="continue-title centerXY">
          <img src="@/assets/images/home/scan.png" alt="">
          <span>一码续办</span>
        </div>
      </div>
      <serviceContinue @toBasic="toBasic" />
    </el-dialog>
    <!-- 服务转派窗口 -->
    <el-dialog
      title="服务转派"
      :visible.sync="showServiceTransfer"
      width="80%"
      class="serviceTransferCoupon"
      :show-close="false"
      custom-class="serviceTransfer"
      v-dialogDrag
    >
      
      <serviceTransfer
        :showServiceTransfer="showServiceTransfer"
        :baseUserInfo="baseUserInfo"
        :currentServiceIndex="currentServiceIndex"
        @spliceStaffInformation="spliceStaffInformation"
        @changeServiceTransfer="changeServiceTransfer"
        @transferData="transferData"
      />
    </el-dialog>
    <!-- 窗口取号 -->
    <el-dialog
      title="窗口取号"
      :visible.sync="showWindowNumber"
      width="80%"
      :show-close="false"
      custom-class="serviceTransfer"
      v-dialogDrag
    >
      
      <windowsNumberNew
        :showServiceTransfer="showServiceTransfer"
        v-if="showWindowNumber"
        :baseUserInfo="baseUserInfo"
        :currentServiceIndex="currentServiceIndex"
        @spliceStaffInformation="spliceStaffInformation"
        @closeWindow="closeWindow"
      />
    </el-dialog>

    <el-dialog
        title="备注"
        :visible.sync="showAdviseMemo"
        width="40%"
        :show-close="false"
        v-dialogDrag
    >
      <div class="el-dialog-div">
<!--        <p class="title">备注：</p>-->
        <el-input
            type="textarea"
            :autosize="{ minRows: 5, maxRows: 4 }"
            placeholder="请输入内容"
            v-model="baseUserInfo.adviseMemo"
        >
        </el-input>
      </div>
      <div class="dialog-footer center">
        <p @click="closeAdviseMemo">取消</p>
        <p @click="saveAdviseMemo">确定</p>
      </div>
    </el-dialog>
    <!-- 来访记录 -->
    <el-dialog
      :title="visitingRecordsTitle"
      :visible.sync="showVisitingRecords"
      width="90%"
      custom-class="serviceTransfer"
      v-dialogDrag
    >
      <visitingRecords
        :baseUserInfo="baseUserInfo"
        v-if="showVisitingRecords"
        @setShowVisitingRecords="setShowVisitingRecords"
      />
    </el-dialog>
    <!-- 联办部门 -->
    <el-dialog
      title="一桌联办"
      :visible.sync="departShow"
      width="90%"
      custom-class="serviceTransfer"
      v-dialogDrag
    >
      <tableList :isHelp="isHelp"
      />
    </el-dialog>
    
  </div>
</template>
<script>
import windowsNumberNew from "./components/windowsNumberNew";  //窗口取号
import serviceContinue from "./components/serviceContinue";
import baseInfo from "./components/baseInfo.vue";
import mattersHandling from "./components/mattersHandling.vue";
import situationGuidance from "./components/situationGuidance.vue";
import essentialInformation from "./components/essentialInformation.vue";
import intelligentFormFilling from "./components/intelligentFormFilling.vue";
import materialCompletion from "./components/materialCompletion.vue";
import materialUpload from "./components/materialUpload.vue";
import goodBadComment from "./components/goodBadComment.vue";
import serviceTransfer from "./components/serviceTransfer.vue";
import visitingRecords from "./components/visitingRecords.vue";
import tableList from "./components/tableList.vue";
import followServiceSuggestions from "./components/followServiceSuggestions";
import { windowCall,getQueueById,saveQueueAdviseMemo, queryWorkQueueList,getGroupById } from "@/api/modules/helpAgent.js";
import { second, deepClone } from "@/utils/index";
import {
  queryQlCaseByCaseOid,
  listSxServicePage,
  getCaseTitleValueList,
} from "@/api/modules/business";
import {
  queryCompanyHistoryList,
} from "@/api/modules/workingGroup";
export default {
  name: "OperationFlow",
  components: {
    baseInfo,
    mattersHandling,
    situationGuidance,
    essentialInformation,
    intelligentFormFilling,
    materialCompletion,
    materialUpload,
    goodBadComment,
    serviceTransfer,
    followServiceSuggestions,
    visitingRecords,
    serviceContinue,
    windowsNumberNew, //窗口取号
    tableList,
  },
  props: {
    commmonVisible: {
      type: Boolean,
      default: false,
    },
    situationAnswerList: {
      type: Array,
      default: () => [],
    },
    artificialService: Boolean,
    isNotFullScreen: Boolean,
    serviceRoot: {
      type: Array,
      default: () => [true, false],
    },
    comeFormArtific: Boolean,
    handleType: String,
    serviceUserInfo: {
      type: Object,
      default: () => {},
    },
    serviceOid: {
      type: String,
      default: () => "",
    },
    serviceType: {
      type: String,
      default: () => "",
    },
    specificMatters: {
      type: Object,
      default: () => {},
    },
  },
  computed: {
    acceptService() {
      return this.$store.state.pageData.acceptService;
    },
    basicUserInfo() {
      return this.$store.state.user.basicUserInfo;
    },
    getBaseUserInfo() {
      return this.$store.state.pageData.baseUserInfo;
    },
  },
  data() {
    return {
      isHelp:'',
      departShow:false,
      showWindowNumber:false, //窗口取号
      showAdviseMemo:false,//备注
      pageNum: 1,
      pageSize: 100,
      companyHistoryInfo:[],
      companyHistoryStopList:[],
      stepShow: false,
      showContinue:false,
      showServiceTransfer: false,
      showVisitingRecords: false,
      tooltipDisable: false,
      visitingRecordsTitle: "",
      nextIndex: 0,
      timer: "",
      componentStepList: [
        { name: "情形引导", step: 1 },
        { name: "基本信息", step: 2 },
        { name: "智能表单", step: 3 },
        { name: "材料准备", step: 4 },
      ],
      componentList: [
        { name: "情形引导", component: "situationGuidance", step: 1 },
        { name: "基本信息", component: "essentialInformation", step: 2 },
        { name: "智能表单", component: "intelligentFormFilling", step: 3 },
        { name: "材料准备", component: "materialCompletion", step: 4 },
        { name: "材料准备", component: "materialUpload", step: 4 },
      ],
      componentName: "情形引导",
      fullscreen: false,
      currentServiceIndex: 0,
      staffInformation: [], //服务用户列表
      caseOid: "",
      serviceTypeName: "",
      caseMaterialOids: "",
      createDate: "",
      serviceName: "",
      baseUserInfo: {},
      processData: {
        processStep: "baseInfo",
        currentStepIndex: 0,
        serviceType: "",
        times: 0,
        serviceTime: "",
        nextServiceAdvise: "",
        adviseMemo: "",
        distributionAdvise: "",
        haWorkServiceId: "",
        specificMatters: {},
        situationCheckList: [],
        valOids: "",
        materialList: [],
        qlCaseTitleValList: [],
        sxServiceMaterialAttaList: [],
        sxServiceMaterialList: [],
        associatedAttachmentsList: [], //上传材料附件
        skipSmartForms: false,
        fillUserInfo: {
          applyUserType: "2",
          applyUserName: "",
          credentialType: "",
          credentialNumber: "",
          applyUserPhone: "",
          applyUserTel: "",
          applyUserAddress: "",
          legalPersonName: "",
          projectName: "",
          applyPostCode: "",
          bussVenueDistrict: "黄浦区",
          bussVenueDistrictOid: ["740560774493309106"],
          specificLocation: "",
          investProjecName: "",
          investProjectCode: "",
          projectAbstract: "",
          proxyFlag: "0",
          contactUserName: "",
          contactCredentialNumber: "",
          contactEmail: "",
          contactUserPhone: "",
          contactUserTel: "",
          contactRemark: "",
          resultDeliveryWay: "1",
          addresseeName: "",
          addresseePostCode: "",
          addresseePhone: "",
          addresseeTel: "",
          addresseeAddress: "",
          addresseeDetailAddress: "",
          chooseAddress: [],
          certWay: "",
          expressCompany: "",
        },
        formFillInfosList: [],
      },
      noNextOperate:localStorage.getItem('noNextOperate'),
      basicUserId:this.$store.state.user.basicUserInfo.id,
      
    };
  },
  mounted() {
    let that = this;
    console.log(this.$store.state.user.basicUserInfo);
    //判断是否为帮办老师  1是  2否
    getGroupById({id:this.$store.state.user.basicUserInfo.groupId}).then(res=>{
      if(res.code == 200 && res.data.deskStatus == '2' ){
        that.isHelp = '2';
      }
      
    })
  },
  methods: {
    // 打开一桌联办
    toTable(){
      this.departShow = true;
    },
    transferData(status){
      this.$emit('isVerify',status);
    },
    //关闭窗口取号
    closeWindow(){
      this.showWindowNumber = false;
    },//关闭窗口取号
    closeAdviseMemo(){
      this.showAdviseMemo = false;
    },
    saveAdviseMemo(){
      let _this = this;
      const data = {
        queueId: this.baseUserInfo.id,
        adviseMemo: this.baseUserInfo.adviseMemo
      };
      saveQueueAdviseMemo(data).then((res) => {
        if (res.code === 200) {
          this.$message.success("保存备注成功");
          _this.showAdviseMemo = false;
        }else{
          _this.$message.warning("保存失败！");
        }
      });
    },
    toBasic(val){
      this.baseUserInfo.processStep = 'baseInfo';
      // val['applyUserName'] = '';
      val['serviceStatus'] = '';
      val['pageNum'] = this.pageNum;
      val['pageSize'] = this.pageSize;
      queryCompanyHistoryList(val).then((res) => {
        if (res.code === 200) {
          this.companyHistoryInfo = res.data.data;
          if(res.data.data.length){
            this.showContinue = false;
            this.companyHistoryStopList = res.data.data.filter((item) => {
              return item.serviceStatus === '2'
            });
            // console.log(this.companyHistoryInfo.length+":"+this.companyHistoryStopList.length)
          }else{
            this.$message.warning("没有查到相关信息！");
          }
          
        }
      });
    },
    setSpecificMatters(data) {
      this.$set(
        this.staffInformation[this.currentServiceIndex],
        "specificMatters",
        data
      );
      this.baseUserInfo = this.staffInformation[this.currentServiceIndex];
      // this.staffInformation[this.currentServiceIndex].specificMatters = data
    },
    //切换用户
    lastUser() {
      if (this.nextIndex) {
        this.nextIndex -= 1;
        this.currentServiceIndex = this.nextIndex;
        this.baseUserInfo = this.staffInformation[this.currentServiceIndex];
        localStorage.setItem('baseUserInfo',this.baseUserInfo);
      }
    },
    nextUser() {
      if (
        (this.staffInformation.length - this.nextIndex - 1) *
          (this.$refs["serviceTabs"][0].clientWidth + 13) +
          this.$refs["currentServices"][0].clientWidth >
        this.$refs.userContent.clientWidth
      ) {
        this.nextIndex += 1;
        this.currentServiceIndex = this.nextIndex;
        this.baseUserInfo = this.staffInformation[this.currentServiceIndex];
        localStorage.setItem('baseUserInfo',this.baseUserInfo);
      } else {
        this.$message.warning("已经到最后了");
      }
    },
    //来访记录操作
    setShowVisitingRecords(data) {
      this.showVisitingRecords = data;
    },
    //自动计时服务时间
    getServiceTime() {
      this.timer = setInterval(() => {
        if (!this.staffInformation.length) {
          clearInterval(this.timer);
        }
        let beginTime = new Date(
          this.staffInformation[this.currentServiceIndex].firstServiceBeginTime
        ).getTime();
        const endTime = new Date().getTime();
        const time = second((endTime - beginTime) / 1000);
        this.$set(
          this.staffInformation[this.currentServiceIndex],
          "serviceTime",
          time
        );
        this.$nextTick(() => {
          this.staffInformation = [...this.staffInformation];
        });
      }, 1000);
    },
    showDialog() {
      setTimeout(() => {
        if (document.querySelector(".el-dialog__wrapper ")) {
          this.$refs.fullScreen.appendChild(
            document.querySelector(".el-dialog__wrapper ")
          );
        }
      }, 100);
    },
    setSituationCheckList(data) {
      this.staffInformation[this.currentServiceIndex].situationCheckList = data;
    },
    //关闭服务转派窗口
    changeServiceTransfer(data) {
      this.showServiceTransfer = data;
    },
    //关闭服务
    closeService(item) {
      this.$confirm("确定已完成该服务,并且结束服务?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          
          this.baseUserInfo.processStep = "followServiceSuggestions";
          this.$set(
            this.staffInformation[this.currentServiceIndex],
            "processStep",
            "followServiceSuggestions"
          );
          this.baseUserInfo.currentStepIndex = 0;
          this.$set(
            this.staffInformation[this.currentServiceIndex],
            "currentStepIndex",
            0
          );
          // this.$message({
          //   type: 'success',
          //   message: '删除成功!'
          // });
        })
        .catch((err) => {
          console.log(err);
        });
    },
    setResetBaseUserInfo(index) {
      const data = {
        name: "",
        cardNo: "",
        companyName: "",
        queueStatus: "2",
        serviceStatus: "2",
      };
      queryWorkQueueList(data).then((res) => {
        if (res.code === 200) {
          if (res.data.length) {
            // this.processData.processStep = "mattersHandling";
            this.$set(
              this.staffInformation,
              index,
              Object.assign(res.data[index], deepClone(this.processData))
            );
            this.staffInformation[index].processStep = "mattersHandling";
          }
        }
      });
    },
    //获取您当前服务用户信息
    setBaseUserInfo(data) {
      this.baseUserInfo = data;
      Object.assign(this.baseUserInfo, deepClone(this.processData));
      this.staffInformation.push(this.baseUserInfo);
      this.currentServiceIndex = this.staffInformation.length - 1;
      this.$store.commit("setCurrentServiceIndex", this.currentServiceIndex);
      this.$nextTick(() => {
        this.staffInformation = [...this.staffInformation];
      });
    },
    // //获取用户服务列表
    setStaffInformation(data) {
      data.forEach((item) => {
        Object.assign(item, deepClone(this.processData));
        this.staffInformation.push(item);
      });
      this.baseUserInfo = this.staffInformation[0];
      this.$store.commit("setStaffInformation", this.staffInformation);
      if (this.staffInformation.length) {
        this.getServiceTime();
      }
    },
    //将当前用户从服务列表中删除
    spliceStaffInformation(index) {
      this.staffInformation.splice(index, 1);
      this.currentServiceIndex = 0;
      // this.nextIndex = 0;
      if (this.staffInformation.length) {
        this.baseUserInfo = this.staffInformation[this.currentServiceIndex];
      } else {
        this.baseUserInfo = {};
      }
    },

    isShowTooltip(e) {
      let clientWidth = e.target.clientWidth,
        scrollWidth = e.target.scrollWidth;
      if (scrollWidth > clientWidth) {
        this.tooltipDisable = false;
      } else {
        this.tooltipDisable = true;
      }
    },

    //窗口叫号
    getWindowNumber() {
      if (!this.staffInformation.length) {
        this.$message.warning("请先选择接待用户");
        return;
      }
      this.showWindowNumber = true;
      // const data = {
      //   queueId: this.baseUserInfo.id,
      //   windowsNumber: "",
      // };
      // windowCall(data).then((res) => {
      //   if (res.code === 200) {
      //     this.$message.success("叫号成功");
      //   }
      // });
    },
    //窗口叫号
    getAdviseMemo() {
      this.showAdviseMemo = true;
      let _this = this;
      const data = {
        queueId: this.baseUserInfo.id,
      };
      getQueueById(data).then((res) => {
        if (res.code === 200) {
          _this.baseUserInfo.adviseMemo = res.data.adviseMemo;
        }
      });
    },

    //打开服务转派窗口
    toServiceTurn() {
      if (!this.staffInformation.length) {
        this.$message.warning("请先选择接待用户");
        return;
      }
      this.showServiceTransfer = true;
    },
    //呼叫帮办
    toCall(){
      this.$emit('openBusinessType',this.baseUserInfo);
    },
    //一码续办
    toContinue(){
      this.showContinue = true;
    },
    //来访记录
    toVisitingRecords() {
      if (!this.staffInformation.length) {
        this.$message.warning("请先选择接待用户");
        return;
      }
      this.visitingRecordsTitle =
        this.staffInformation[this.currentServiceIndex].name +
        `   (${this.staffInformation[this.currentServiceIndex].phone}，${
          this.staffInformation[this.currentServiceIndex].cardNo
        })  来访记录`;
      this.showVisitingRecords = true;
    },

    checkUser(data, index) {
      this.currentServiceIndex = index;
      this.staffInformation[this.currentServiceIndex] = data;
      // this.$store.commit('setCurrentServiceIndex', index)
      this.baseUserInfo = data;
      localStorage.setItem('baseUserInfo',JSON.stringify(data));
    },
    showStpe() {
      this.stepShow = !this.stepShow;
    },
    lastStep() {},
    //跳转下一步
    nextStep(component, step) {
      let that = this;
      setTimeout(function(){
        that.$emit("getAppointNum");   //更新联办列表数量
        that.$emit("getStatServiceNum");  //更新左边数据
      },500)
      this.baseUserInfo.processStep = component;
      this.$set(
        this.staffInformation[this.currentServiceIndex],
        "processStep",
        component
      );
      if (step) {
        this.baseUserInfo.currentStepIndex = step;
        this.$set(
          this.staffInformation[this.currentServiceIndex],
          "currentStepIndex",
          step
        );
        const currentComponent = this.componentList.filter(
          (item) => item.component === component
        );
        this.baseUserInfo.componentName = currentComponent[0].name;
        this.$set(
          this.staffInformation[this.currentServiceIndex],
          "componentName",
          currentComponent[0].name
        );
      } else {
        this.baseUserInfo.currentStepIndex = 0;
        this.$set(
          this.staffInformation[this.currentServiceIndex],
          "currentStepIndex",
          0
        );
      }
      this.$nextTick(() => {
        this.staffInformation = [...this.staffInformation];
      });
    },
    screen() {
      // const element = element || document.documentElement
      // let element = document.documentElement;//设置后就是我们平时的整个页面全屏效果
      let element =
        document.getElementById("con_lf_top_div") || document.documentElement; //设置后就是   id==con_lf_top_div 的容器全屏
      if (this.fullscreen) {
        if (document.exitFullscreen) {
          document.exitFullscreen();
        } else if (document.webkitCancelFullScreen) {
          document.webkitCancelFullScreen();
        } else if (document.mozCancelFullScreen) {
          document.mozCancelFullScreen();
        } else if (document.msExitFullscreen) {
          document.msExitFullscreen();
        }
      } else {
        if (element.requestFullscreen) {
          element.requestFullscreen();
        } else if (element.webkitRequestFullScreen) {
          element.webkitRequestFullScreen();
        } else if (element.mozRequestFullScreen) {
          element.mozRequestFullScreen();
        } else if (element.msRequestFullscreen) {
          // IE11
          element.msRequestFullscreen();
        }
      }
      this.fullscreen = !this.fullscreen;
    },
    /** 业务代码 */
    setData(data) {
      Object.assign(this.staffInformation[this.currentServiceIndex], data);
    },
    // 暂存继续办件
    getProceedData(data) {
      if (this.staffInformation.length) {
        this.staffInformation.forEach((item, index) => {
          if (
            item.name === data.name &&
            item.phone === data.phone &&
            item.companyCode === data.companyCode
          ) {
            this.currentServiceIndex = index;
            const obj = {
              caseOid: data.qlCaseId,
              caseNumber: data.caseNumber,
              haWorkServiceId: data.id,
            };
            Object.assign(data, obj);
            delete data.id;
            Object.assign(
              this.staffInformation[this.currentServiceIndex],
              data
            );
            this.getQueryQlCaseApplayByCaseOid(data.qlCaseId);
            this.getCaseTitleValueList(data.qlCaseId);
            this.getDataList(data);
          }
        });
      }
    },
    //获取办件申请信息
    getQueryQlCaseApplayByCaseOid(caseOid) {
      queryQlCaseByCaseOid({ caseOid: caseOid }).then((res) => {
        if (res.code === 200) {
          res.data.applay.certWay = res.data.certWay;
          res.data.applay.expressCompany = res.data.expressCompany;
          if (res.data.applay.chooseAddress) {
            res.data.applay.chooseAddress = JSON.parse(
              res.data.applay.chooseAddress
            );
          } else {
            res.data.applay.chooseAddress = [];
          }

          Object.assign(
            this.staffInformation[this.currentServiceIndex].fillUserInfo,
            res.data.applay
          );
        }
      });
    },
    //获取事项列表
    getDataList(data) {
      const params = {
        serviceName: data.serviceMemo,
        // districtOid: '',
        districtOid: "4028545d665734290166b02711c20073",
        organOid: "",
        handleType: "",
        pageNum: "",
        pageSize: "",
        serviceType: data.serviceType,
      };
      listSxServicePage(params)
        .then((res) => {
          if (res.code === 200) {
            if (res.data.data.length) {
              res.data.data.forEach((item) => {
                if (item.serviceOid === data.sxServiceId) {
                  Object.assign(
                    this.staffInformation[this.currentServiceIndex]
                      .specificMatters,
                    item
                  );
                  this.nextStep("essentialInformation", "2");
                }
              });
            }
          }
        })
        .catch((err) => {
          this.loadingService = false;
          console.log(err);
        });
    },
    //根据办件编号获取情形选项信息
    getCaseTitleValueList(caseOid) {
      getCaseTitleValueList({ caseOid: caseOid }).then((res) => {
        if (res.code === 200) {
          const arr = [];
          res.data.forEach((item) => {
            const obj = {
              titleOid: item.titleOid,
              valueOid: item.valueOid,
            };
            arr.push(obj);
          });
          Object.assign(
            this.staffInformation[this.currentServiceIndex].qlCaseTitleValList,
            arr
          );
        }
      });
    },
  },
  watch: {
    getBaseUserInfo: {
      handler(val) {
        if (val) {
          this.baseUserInfo = val;
          localStorage.setItem('baseUserInfo',JSON.stringify(val));
          Object.assign(this.baseUserInfo, deepClone(this.processData));
          if (!this.staffInformation.find((item) => item.name === val.name)) {
            val.processStep = "baseInfo";
            val.currentStepIndex = 0;
            this.staffInformation.unshift(val);
            this.currentServiceIndex = 0;
            this.nextIndex = 0;
          } else {
            this.staffInformation.forEach((item, index) => {
              if (JSON.stringify(item) === JSON.stringify(val)) {
                this.currentServiceIndex = index;
              }
            });
          }
          this.$store.commit(
            "setCurrentServiceIndex",
            this.currentServiceIndex
          );
          this.$store.commit("setStaffInformation", this.staffInformation);
          if (this.staffInformation.length) {
            this.getServiceTime();
          }
        }
      },
      deep: true,
    },
    serviceUserInfo: {
      handler(val) {
        if (val) {
          this.baseUserInfo = val;
          localStorage.setItem('baseUserInfo',JSON.stringify(val));
          Object.assign(this.baseUserInfo, deepClone(this.processData));
          this.staffInformation.unshift(this.baseUserInfo);
          this.currentServiceIndex = 0;
          this.$message.success("成功接待下一位用户");
          this.nextIndex = 0;
          this.$store.commit(
            "setCurrentServiceIndex",
            this.currentServiceIndex
          );
          this.$store.commit("setStaffInformation", this.staffInformation);
          if (this.staffInformation.length) {
            this.getServiceTime();
          }
        }
      },
    },
    specificMatters: {
      handler(val) {
        if (val) {
          this.baseUserInfo.processStep = "situationGuidance";
          this.$set(
            this.staffInformation[this.currentServiceIndex],
            "processStep",
            "situationGuidance"
          );
          this.baseUserInfo.currentStepIndex = "1";
          this.baseUserInfo.specificMatters = val;
          this.$set(
            this.staffInformation[this.currentServiceIndex],
            "currentStepIndex",
            "1"
          );
          this.baseUserInfo.componentName = "情形引导";
          this.$set(
            this.staffInformation[this.currentServiceIndex],
            "componentName",
            "情形引导"
          );
          this.staffInformation = [...this.staffInformation];
        }
      },
      deep: true,
    },
    serviceType: {
      handler(val) {
        if (val) {
          this.baseUserInfo.serviceType = val;
        }
      },
    },
  },
};
</script>
<style lang="scss" scoped>
  @import './index.scss';
  .el-dialog-div{
    height: auto;//如果高度过高，可用max-height
    overflow: auto;
  }
  .screenAll {
  background: url("@/assets/images/home/homeBack.png") no-repeat;
  background-size: 100% 100%;
  }

.conponent-container {
  width: 100%;
  height: 100%;
  background-size: 100% 100%;

  .header {
    width: 100%;
    height: 40px;
    display: flex;
    justify-content: space-between;
    .serviceUser {
      min-width: calc(100% - 60rem);
      width: auto;
      padding-right: 1rem;
      height: 100%;
      position: relative;
      overflow: hidden;
      &:hover {
        .leftBtn {
          display: flex;
        }
        .nextBtn {
          display: flex;
        }
      }
      .leftBtn {
        width: 15px;
        height: 30px;
        cursor: pointer;
        display: none;
        align-items: center;
        justify-content: center;
        background-color: rgba(31, 45, 61, 0.11);
        position: absolute;
        top: 5px;
        left: 0;
        z-index: 1000;
        img {
          height: 15px;
        }
      }
      .nextBtn {
        width: 15px;
        height: 30px;
        cursor: pointer;
        background-color: rgba(31, 45, 61, 0.11);
        position: absolute;
        display: none;
        align-items: center;
        justify-content: center;
        top: 5px;
        right: 0;
        z-index: 1000;
        img {
          height: 15px;
        }
      }
      .userContent {
        width: 100%;
        height: 100%;
        position: absolute;
        left: 0;
        top: 0;
        display: flex;
        align-items: center;
        justify-content: flex-start;
      }
      .currentServices {
        width: auto;
        min-width: 26rem;
        height: 100%;
        display: flex;
        justify-content: space-between;
        background-color: #f5f5f5;
        border-top-left-radius: 0.375rem;
        border-radius: 0.8333rem 0.8333rem 0px 0px;
        margin-right: 1.4167rem;
        cursor: pointer;
        img {
          width: 1.5rem;
          margin-right: 0.6667rem;
        }
        .left {
          width: auto;
          height: 100%;
          display: flex;
          align-items: center;
          padding: 0 0.5rem 0 0.875rem;

          p {
            padding: 0;
            margin: 0;
            font-stretch: normal;
            letter-spacing: 0px;
            font-size: 1.5rem;
            font-family: Microsoft YaHei;
            font-weight: 400;
            color: #3f3f3f;
            display: flex;
            align-items: center;
            justify-content: flex-start;
            .username {
              display: inline-block;
              width: 4.5rem;
              overflow: hidden;
              white-space: nowrap;
              text-overflow: ellipsis;
            }
            span {
              font-size: 1.5rem;
              font-family: Microsoft YaHei;
              font-weight: 400;
              color: #222222;
              font-stretch: normal;
              letter-spacing: 0px;
            }
          }
        }

        .right {
          width: auto;
          height: 100%;
          background: linear-gradient(
            90deg,
            #fad3af 0%,
            rgba(249, 218, 190, 0.45) 100%
          );
          border-radius: 1.5833rem 0.5rem 0px 1.5833rem;
          display: flex;
          align-items: center;
          padding: 0 0.8125rem;
          cursor: pointer;

          img {
            width: 1.3333rem;
          }

          span {
            font-family: MicrosoftYaHei;
            font-stretch: normal;
            letter-spacing: 0px;
            font-size: 1.5rem;
            font-weight: 400;
            color: #ff5926;
          }
        }
      }

      .serviceTabs {
        height: 100%;
        width: auto;
        display: flex;
        align-items: center;
        justify-content: flex-start;
        margin-right: 1.4167rem;
        cursor: pointer;

        .left {
          width: 8.6667rem;
          height: 100%;
          background: rgba(245, 245, 245, 0.27);
          border: 1px solid #efefef;
          box-shadow: 0px 0px 0.6667rem 0px rgba(68, 120, 214, 0.16);
          border-radius: 0.8333rem 0.8333rem 0px 0px;
          padding-left: 0.9167rem;
          display: flex;
          align-items: center;
          justify-content: flex-start;

          img {
            width: 1.5rem;
            margin-right: 0.6667rem;
          }
          .username {
            display: inline-block;
            width: 4.5rem;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
          }
          span {
            font-stretch: normal;
            letter-spacing: 0px;
            font-size: 1.5rem;
            font-family: Microsoft YaHei;
            font-weight: 400;
            color: #6c7997;
          }
        }
      }
    }
    .noDataServices {
      width: 23.9286rem;
      height: 100%;
      background-color: #f5f5f5;
      border-top-left-radius: 0.375rem;
      border-radius: 0.8333rem 0.8333rem 0px 0px;
    }
    .rightBtn {
      // width: 41rem;
      height: 100%;
      padding-bottom: 0.4167rem;
      display: flex;
      align-items: flex-start;
      justify-content: flex-end;

      .btn {
        width: auto;
        height: 25px;
        padding: 0 0.5rem;
        border-radius: 1.2857rem;
        margin-top: 5px;
        background: linear-gradient(90deg, #2473ff 0%, #56b1fd 100%);
        box-shadow: 0px 0px 2.0714rem 0px rgba(204, 177, 121, 0.31);
        padding-left: 0.75rem;
        display: flex;
        align-items: center;
        margin-left: 1.0833rem;
        cursor: pointer;

        &:nth-child(1) {
          background: linear-gradient(270deg, #ffc95b 0%, #ffa53b 100%);
        }

        img {
          width: 1.125rem;
          margin-right: 0.5833rem;
        }

        span {
          font-family: MicrosoftYaHei;
          font-size: 12px;
          font-weight: normal;
          font-stretch: normal;
          letter-spacing: 0px;
          color: #ffffff;
          white-space: nowrap;
        }
      }

      .screen {
        margin-top: 5px;
        width: 2.9167rem;
        height: 2.9167rem;
        background: rgba(0, 0, 0, 0.44);
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-left: 1.5rem;

        img {
          width: 1.5rem;
        }
      }
    }
  }

  .content {
    width: 100%;
    height: calc(100% - 40px);
    border-top-left-radius: none;
    background-color: #f5f5f5;
    display: flex;
    flex-direction: column;

    .containerHeade {
      width: 100%;
      height: 5.1667rem;
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding-left: 3rem;
      font-size: 1.5rem;
      font-family: Microsoft YaHei;
      font-weight: 400;
      color: #4f4f4f;

      .tips {
        padding: 0;
        margin: 0;
        font-size: 1.5rem;
        font-family: Microsoft YaHei;
        font-weight: 400;
        color: #4f4f4f;
      }

      .right {
        width: auto;
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: flex-end;
        padding-right: 0.5rem;
        .rightSpan {
          width: auto;
          height: 60%;
          padding: 0 1.5rem;
          border-radius: 1.2857rem;
          //margin-top: 5px;
          background: linear-gradient(90deg, #2473ff 0%, #56b1fd 100%);
          box-shadow: 0px 0px 2.0714rem 0px rgba(204, 177, 121, 0.31);
          //padding-left: 0.75rem;
          display: flex;
          align-items: center;
          margin-right: 1.0833rem;
          cursor: pointer;
          span {
            font-family: MicrosoftYaHei;
            font-size: 12px;
            font-weight: normal;
            font-stretch: normal;
            letter-spacing: 0px;
            color: #ffffff;
            white-space: nowrap;
          }
        }
        .serviceTime {
          width: auto;
          height: 100%;
          display: flex;
          align-items: center;
          justify-content: flex-start;
          font-size: 1.5rem;
          font-family: Source Han Sans CN;
          font-weight: 400;
          color: #393939;

          img {
            width: 1.5rem;
            margin-right: 0.5rem;
          }

          span {
            font-size: 1.6667rem;
            font-family: DIN;
            font-weight: bold;
            color: #393939;
            margin-left: 0.5rem;
          }
        }

        .step {
          width: 18.6667rem;
          height: 3.5rem;
          background: #6eb2ff;
          border-radius: 1.75rem;
          display: flex;
          align-items: center;
          justify-content: flex-start;
          padding-left: 2.0833rem;
          margin-left: 1.8333rem;

          .stepInfo {
            font-size: 1.5rem;
            font-family: Source Han Sans CN;
            font-weight: 500;
            color: #ffffff;
            text-shadow: 0px 0px 5px rgba(0, 56, 156, 0.63);
          }

          img {
            width: 1.6667rem;
            margin-left: 1.5rem;
          }
        }
      }
    }

    .process-content {
      width: 100%;
      height: calc(100% - 5.1667rem);
    }
  }
}

::v-deep .el-dialog {
  // height: 75vh;
}
</style>
<style lang="scss">
p {
  padding: 0;
  margin: 0;
}

.el-popover {
  .popover-content {
    ul {
      width: 100%;
      padding: 0;
      margin: 0;

      .steps {
        list-style: none;
        padding: 0;
        margin: 0;
        width: 100%;
        height: 8rem;

        .top {
          width: 100%;
          height: 4rem;
          display: flex;
          align-items: center;
          justify-content: flex-start;

          .activeStep {
            width: 4rem;
            height: 4rem;
            border-radius: 50%;
            background-color: rgba(201, 171, 111, 0.2);
            display: flex;
            align-items: center;
            justify-content: center;
            margin-right: 0.5rem;

            p {
              padding: 0;
              margin: 0;
              width: 3.1875rem;
              height: 3.1875rem;
              background: linear-gradient(90deg, #2473ff 0%, #56b1fd 100%);
              border-radius: 50%;
              font-size: 1.5625rem;
              font-family: DIN;
              font-weight: bold;
              color: #ffffff;
              display: flex;
              align-items: center;
              justify-content: center;
            }
          }

          .circularIcon {
            width: 3.1875rem;
            height: 3.1875rem;
            background: rgba(209, 204, 195, 0.73);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-right: 0.9375rem;
            margin-left: 0.4063rem;

            p {
              font-size: 1.5625rem;
              font-family: DIN;
              font-weight: bold;
              color: #ffffff;
            }
          }

          span {
            font-size: 1.125rem;
            font-family: Source Han Sans CN;
            font-weight: 500;
            color: #9e7d3c;
          }
        }

        .blew {
          width: 100%;
          height: 4rem;
          margin-top: 0.5625rem;
          padding-left: 2rem;

          img {
            width: 0.5rem;
            height: 3.25rem;
          }

          p {
            padding: 0;
            margin: 0;
            width: 0.1875rem;
            height: 3.25rem;
            background: rgba(218, 184, 114, 0.3);
          }
        }
      }
    }
  }
}
.serviceTransferCoupon{
  z-index: 9999999 !important;
}
.serviceTransfer {
  height: 80vh;
  z-index: 9999999 !important;
  .el-dialog__body {
    height: calc(100% - 3.75rem);
    max-height: 100%;
  }
}

</style>
<style lang="scss" scoped>
  
  .dialog-footer {
    padding-top: 10px;
    justify-content: center !important;
    p {
      &:nth-child(1) {
        cursor: pointer;
        padding: 0 3.75rem;
        width: 30%;
        //height: 5.1667rem;
        background: #ffffff;
        border: 1px solid #4988f2;
        box-shadow: 0px 0px 1.5rem 0px rgb(40 107 198 / 31%);
        border-radius: 2.5833rem;
        font-size: 1.8333rem;
        font-family: Microsoft YaHei;
        font-weight: 400;
        color: #2473ff;
        display: inline-block;
        align-items: center;
        justify-content: center;
        margin-right: 2.1875rem;
      }

      &:nth-child(2) {
        cursor: pointer;
        padding: 0 3.75rem;
        width: 30%;
        //height: 5.1667rem;
        background: #ffffff;
        border: 1px solid #4988f2;
        box-shadow: 0px 0px 1.5rem 0px rgb(40 107 198 / 31%);
        border-radius: 2.5833rem;
        font-size: 1.8333rem;
        font-family: Microsoft YaHei;
        font-weight: 400;
        color: #2473ff;
        display: inline-block;
        align-items: center;
        justify-content: center;
        margin-right: 2.1875rem;
        color: #ffffff !important;
        background: linear-gradient(90deg, #2473ff 0%, #56b1fd 100%) !important;
        box-shadow: 0px 0px 1.5rem 0px rgb(40 107 198 / 31%) !important;
      }
    }
  }
</style>
