<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-07-20 17:00:15
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-11-10 17:31:34
 * @FilePath: \hpNewHall\src\views\operationFlow\index.vue
 ** 服务主窗口
-->
<template>
  <div class="conponent-container" id="con_lf_top_div" ref="fullScreen" :class="fullscreen ? 'screenAll' : ''">
    <div class="header">
      <div class="left">
        <p class="serviceTime" v-if="baseUserInfo.serviceTime">
          <img :src="require('@/assets/images/pad/timeIcon.png')" alt="" />
          <span>{{ baseUserInfo.serviceTime }}</span>
        </p>
        <span>{{ baseUserInfo.name }}</span>
      </div>
      <div class="right">
        <el-dropdown @command="handleCommand" trigger="click">
          <span class="el-dropdown-link">
            <img :src="require('@/assets/images/pad/ellipsis.png')" alt="" />
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="getWindowNumber" class="centerY"><img src="@/assets/images/pad/getNumber.png"
                alt=""> 窗口取号</el-dropdown-item>
            <el-dropdown-item command="toServiceTurn" class="centerY"><img src="@/assets/images/pad/reassign.png"
                alt=""></img>服务转派</el-dropdown-item>
            <el-dropdown-item command="toVisitingRecords" class="centerY"><img src="@/assets/images/pad/record.png"
                alt=""></img>办事记录</el-dropdown-item>
            <el-dropdown-item command="toCall" v-if="baseUserInfo.takeNumberType != 5" class="centerY"><img :src="require('@/assets/video/9.png')" 
                alt=""></img>呼叫帮办</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>

        <img @click="closeService(baseUserInfo)" :src="require('@/assets/images/pad/colse.png')" alt="" />
      </div>
    </div>
    <!-- <div class="header">
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
    </div> -->
    <div class="content">
      <div class="containerHeade">
        <div class="left">
          <p class="tips" v-if="
  baseUserInfo.processStep === 'baseInfo' && staffInformation.length
">
            来访人员详情
          </p>
          <p class="tips" v-if="baseUserInfo.processStep === 'mattersHandling'">
            请选择需要办理的事项
          </p>
          <p class="tips" v-if="baseUserInfo.currentStepIndex && baseUserInfo.processStep != 'baseInfo'">
            正在办理：<span v-html="baseUserInfo.specificMatters.serviceName"></span>
          </p>
        </div>
        <div class="right none">
          <el-popover placement="bottom" trigger="click" v-show="
  baseUserInfo.processStep != 'baseInfo' &&
  baseUserInfo.processStep != 'mattersHandling'
">
            <div class="popover-content">
              <ul>
                <li class="steps" v-for="(item, index) in componentStepList" :key="index">
                  <div class="top">
                    <div :class="
  baseUserInfo.currentStepIndex >= item.step
    ? 'activeStep'
    : 'circularIcon'
">
                      <p>{{ item.step }}</p>
                    </div>
                    <span>{{ item.name }}</span>
                  </div>
                  <div class="blew" v-if="index != 3">
                    <img v-if="baseUserInfo.currentStepIndex >= item.step"
                      :src="require('@/assets/images/home/progressBar.png')" alt="" />
                    <p v-else></p>
                  </div>
                </li>
              </ul>
            </div>
            <div class="step" slot="reference" @click="showStpe" v-show="
  baseUserInfo.currentStepIndex && baseUserInfo.serviceType != '1'
">
              <p class="stepInfo">
                第 <span>{{ baseUserInfo.currentStepIndex }}</span> 步：<span>{{
    baseUserInfo.componentName
}}</span>
              </p>
              <img :src="
  stepShow
    ? require('@/assets/images/pad/pullDown.png')
    : require('@/assets/images/pad/pullUp.png')
" alt="" />
            </div>
          </el-popover>
        </div>
      </div>
      <div class="process-content">
        <!-- <iframe v-show="iframeState" id="show-iframe"  frameborder=0 name="showHere" scrolling=auto :src="aa"></iframe> -->
        <!-- <keep-alive> -->
        <template v-if="!staffInformation.length">
          <baseInfo @setStaffInformation="setStaffInformation" />
        </template>
        <template v-else>
          <template v-for="(item, index) in staffInformation">
            <component 
              :key="index" 
              v-if="currentServiceIndex === index" 
              :initialIndex="swiperIndex"
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
              @nextStep="nextStep"
              @setHaWorkServiceId="(data) => (item.haWorkServiceId = data)" 
              @setResetBaseUserInfo="setResetBaseUserInfo"
              @spliceStaffInformation="spliceStaffInformation" 
              @setServiceType="(data) => (item.serviceType = data)"
              @setMaterialList="(data) => (item.materialList = data)" 
              @setAssociatedAttachmentsList="(data) => (item.associatedAttachmentsList = data)" 
              @setSpecificMatters="setSpecificMatters" 
              @setBaseUserInfo="setBaseUserInfo"
              @setSkipSmartForms="(data) => (item.skipSmartForms = data)" 
              @lastStep="lastStep" 
              @setQlCaseTitleValList="(data) => (item.qlCaseTitleValList = data)" 
              @setData="setData" 
              @caseMaterialOids="(data) => (caseMaterialOids = data)"
              @setFillUserInfo="(data) => (item.fillUserInfo = data)"
              @setFormFillInfosList="(data) => (item.formFillInfosList = data)" 
              @setSxServiceMaterialList="(data) => (item.sxServiceMaterialList = data)" 
              @setSituationCheckList="setSituationCheckList" 
              @setValOids="(data) => (item.valOids = data)"
              @close="$emit('update:commmonVisible', false)" 
              @showDialog="showDialog">
            </component>
          </template>
        </template>
        <!-- </keep-alive> -->
      </div>
    </div>
    <!-- 服务转派窗口 -->
    <el-dialog title="服务转派" :visible.sync="showServiceTransfer" width="80%" custom-class="serviceTransfer serviceTransfer-coupon visitRecord"
      v-dialogDrag>
      <serviceTransfer :showServiceTransfer="showServiceTransfer" :baseUserInfo="baseUserInfo"
        :currentServiceIndex="currentServiceIndex" @spliceStaffInformation="spliceStaffInformation"
        @changeServiceTransfer="changeServiceTransfer" />
    </el-dialog>
    <!-- 窗口取号 -->
    <el-dialog title="窗口取号" :visible.sync="showWindowNumber" width="80%" :show-close="false"
      custom-class="serviceTransfer" class="window-number" v-dialogDrag>

      <windowNumber :showServiceTransfer="showServiceTransfer" :baseUserInfo="baseUserInfo"
        :currentServiceIndex="currentServiceIndex" @spliceStaffInformation="spliceStaffInformation"
        @closeWindow="closeWindow" />
    </el-dialog>
    <!-- 来访记录 -->
    <el-dialog :title="visitingRecordsTitle" :visible.sync="showVisitingRecords" width="90%"
      custom-class="serviceTransfer visitRecord" v-dialogDrag>
      <visitingRecords :baseUserInfo="baseUserInfo" v-if="showVisitingRecords"
        @setShowVisitingRecords="setShowVisitingRecords" />
    </el-dialog>
    <!-- 窗口取号页 -->
    <el-dialog v-dialog-drag title="窗口取号" :visible.sync="showSelectBusinessType" class="select-dialog" width="80%"
      append-to-body>
      <div class="select-Body">
        <p class="tips">请选择窗口取号方式</p>
        <div class="select-content">
          <div class="item" @click="getNumber('3')">
            <img :src="require('@/assets/images/pad/consultingService.png')" alt="" />
            <p>取标准号</p>
          </div>
          <div class="item" @click="getNumber()">
            <img :src="require('@/assets/images/pad/storesReserve.png')" alt="" />
            <p>取指定号</p>
          </div>
        </div>
      </div>
    </el-dialog>
    <v-touch @swiperight="swiperight" v-show="showTurntable && staffInformation.length" class="turntable animate"
      :style="{ 'animation-duration': 1000 + 'ms' }" :class="!showTurntable ? 'slideOutRight' : 'slideInRight'">
      <div class="inherit-turn">
        <!-- <p class="user">刘大</p> -->
        <div class="turnCenter"></div>
        <div class="specifiedArea">
          <span>当前服务</span>
        </div>
        <div ref="rotateTouch" class="userArea" @touchstart="touchstart" @touchend="touchend" @touchmove="on_touchmove"
          @touchleave="touchdown = false">
          <section class="container" :style="{
  transform: 'rotate(' + angle_data + 'deg)',
}">
            <div class="item-user" v-for="(item, index) in circleList" :key="index" :style="item.style">
              <p :class="item.className" v-if="index < staffInformation.length">
                {{ staffInformation[index].name }}
              </p>
            </div>
          </section>
        </div>

        <div class="userLogo" @touchstart="touchstart" @touchend="touchend" @touchmove="on_touchmove"
          @touchleave="touchdown = false">
          <section class="container" :style="{
  transform: 'rotate(' + angle_data + 'deg)',
}">
            <div class="item-user-log" v-for="(item, index) in circleList" :key="index" :style="item.style">
              <img v-if="index < staffInformation.length" :class="item.className"
                :src="staffInformation[index].headerLog" alt="" />
            </div>
          </section>
        </div>
      </div>
    </v-touch>
    <v-touch v-show="!showTurntable && staffInformation.length" @pan="toChooseUser" class="userIdentity animate"
      :style="{ 'animation-duration': 1000 + 'ms' }" :class="showTurntable ? 'slideOutRight' : 'slideInRight'">
      <p>
        <span>{{ baseUserInfo.name }}</span>
      </p>
    </v-touch>
  </div>
</template>
<script>
import windowNumber from "./processComponents/windowNumber";  //窗口取号
import baseInfo from "./processComponents/baseInfo.vue";
import mattersHandling from "./processComponents/mattersHandling.vue";
import situationNewGuidance from "./processComponents/situationNewGuidance.vue";
import situationGuidance from "./processComponents/situationGuidance.vue";
import essentialInformation from "./processComponents/essentialInformation.vue";
import intelligentFormFilling from "./processComponents/intelligentFormFilling.vue";
import materialCompletion from "./processComponents/materialCompletion.vue";
import materialUpload from "./processComponents/materialUpload.vue";
import goodBadComment from "./processComponents/goodBadComment.vue";
import serviceTransfer from "./processComponents/serviceTransfer.vue";
import visitingRecords from "./processComponents/visitingRecords.vue";
import serviceNewGuide from "./processComponents/serviceNewGuide.vue";
import followServiceSuggestions from "./processComponents/followServiceSuggestions.vue";
import { windowCall, queryWorkQueueList } from "@/api/modules/helpAgent.js";
import { second, deepClone } from "@/utils/index";
import videoOperate from "@/utils/video";
import {
  queryQlCaseByCaseOid,
  listSxServicePage,
  getCaseTitleValueList,
} from "@/api/modules/business";
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
    situationNewGuidance,
    serviceNewGuide,
    windowNumber
  },
  mixins:[videoOperate],
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
      default: () => { },
    },
    serviceOid: {
      type: String,
      default: () => "",
    },
    serviceType: {
      type: String,
      default: () => "",
    },
    stepSwitch: {
      type: Number,
      default: () => "",
    },
    specificMatters: {
      type: Object,
      default: () => { },
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
      showSelectBusinessType: false,
      showWindowNumber: false, //窗口取号
      stepShow: false,
      showServiceTransfer: false,
      showVisitingRecords: false,
      tooltipDisable: false,
      showTurntable: false,
      touchdown: false,
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
        headerLog: require("@/assets/images/pad/receptionUsers.png"),
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
      circleList: [], //用户旋转样式列表
      angle_data: 281.25,
      currentAngle: 281.25,
      startAngle: 0,
      swiperIndex: 0, //情形选择默认index
    };
  },
  created() {
    this.formateUserListStyle();
  },
  mounted() {
  },
  methods: {
    //呼叫帮办
    toCall(){
      this.$emit('openBusinessType');
    },
    //窗口取号页
    getNumber(id) {
      if(id){
        this.$message.warning("正在开发中...");
        return;
      }
      this.getWindowNumber();
    },
    //关闭窗口取号
    closeWindow() {
      this.showWindowNumber = false;
    },
    handleCommand(command) {
      if (command == 'toVisitingRecords') {
        this.toVisitingRecords();
      }
      if (command == 'toServiceTurn') {
        this.toServiceTurn();
      }
      if (command == 'toCall') {
        this.toCall();
      }
      if (command == 'getWindowNumber') {
        this.showSelectBusinessType = true;
      }
    },
    //隐藏用户选择
    swiperight() {
      this.showTurntable = false;
    },
    //展开用户选择界面
    toChooseUser() {
      this.showTurntable = true;
    },
    formateUserListStyle() {
      const average = 22.5;
      const half = average / 2;
      for (var i = 0; i < 16; i++) {
        const angle = -(i * average + half);
        const obj = {
          style: `-webkit-transform: rotate(${angle}deg);
                  transform: rotate(${angle}deg);`,
        };
        if (i == 0) {
          obj.className = "centerSelected";
        } else {
          obj.className = "unchecked";
        }
        this.circleList.push(obj);
      }
    },
    //用户选择转盘手指摁下
    touchstart(event) {
      this.touchdown = true;
      const rotateTouch = this.$refs.rotateTouch;
      let centerX = -~(rotateTouch.offsetHeight || rotateTouch.height) / 2;
      let centerY = -~(rotateTouch.offsetWidth || rotateTouch.width) / 2;

      let imgTop = this.$refs["rotateTouch"].getBoundingClientRect().top;
      let imgLeft = this.$refs["rotateTouch"].getBoundingClientRect().left;
      let offsetX = event.changedTouches[0].pageX - imgLeft;
      let offsetY = event.changedTouches[0].pageY - imgTop;

      let angle = this.calculate_degree(offsetX, offsetY, centerX, centerY);
      this.startAngle = -~angle;
    },
    //用户选择转盘手指移动
    on_touchmove(event) {
      if (this.touchdown) {
        //表示是按下移动的
        const rotateTouch = this.$refs.rotateTouch;
        let centerX = -~(rotateTouch.offsetHeight || rotateTouch.height) / 2;
        let centerY = -~(rotateTouch.offsetWidth || rotateTouch.width) / 2;

        let imgTop = this.$refs["rotateTouch"].getBoundingClientRect().top;
        let imgLeft = this.$refs["rotateTouch"].getBoundingClientRect().left;
        let offsetX = event.changedTouches[0].pageX - imgLeft;
        let offsetY = event.changedTouches[0].pageY - imgTop;

        let angle = this.calculate_degree(offsetX, offsetY, centerX, centerY);
        const rotateAngle = -~angle - this.startAngle;
        let angle_data = this.currentAngle + -~rotateAngle;
        if (angle_data > 360) {
          if (angle_data - 360 > 281.25 + 22.5 * this.staffInformation.length) {
            return;
          }
        } else {
          if (angle_data < 281.25) {
            // return;
          }
        }
        this.angle_data = angle_data;
        if (this.angle_data > 360) {
          this.angle_data = this.angle_data - 360;
        }
        // if(rotateAngle){}
        if (this.angle_data > 270) {
          const index = Math.floor((this.angle_data - 270) / 22.5);
          this.circleList.forEach((item) => {
            item.className = "unchecked";
          });
          if (Math.abs(this.angle_data - (281.25 + 22.5 * index)) < 5) {
            this.$set(this.circleList[index], "className", "centerSelected");
            this.currentServiceIndex = index;
            this.baseUserInfo = this.staffInformation[this.currentServiceIndex];
          } else {
            this.$set(this.circleList[index], "className", "checked");
            this.currentServiceIndex = index;
            this.baseUserInfo = this.staffInformation[this.currentServiceIndex];
          }
        } else {
          let index = 4 + Math.floor(this.angle_data / 22.5);
          // 为负，取正
          if(index < 0){
            index = index+16*Math.ceil(Math.abs(index/16));
          }
          this.circleList.forEach((item) => {
            item.className = "unchecked";
          });
          if (Math.abs(this.angle_data - (11.25 + 22.5 * (index - 4))) < 5) {
            this.$set(this.circleList[index], "className", "centerSelected");
          } else {
            this.$set(this.circleList[index], "className", "checked");
          }
          this.currentServiceIndex = index;
          if(index < this.staffInformation.length){
            this.baseUserInfo = this.staffInformation[this.currentServiceIndex];
          }
          
        }
      }
    },
    touchend() {
      this.touchdown = false;
      this.currentAngle = this.angle_data;
    },
    //根据当前坐标和中心坐标计算角度
    calculate_degree(x, y, centerX, centerY) {
      const radians = Math.atan2(x - centerX, y - centerY);
      return radians * (180 / Math.PI) * -1 + 180;
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
    setSituationCheckList(data, index) {
      this.staffInformation[this.currentServiceIndex].situationCheckList = data;
      this.swiperIndex = index;  //选择情形index
    },
    //关闭服务转派窗口
    changeServiceTransfer(data) {
      this.showServiceTransfer = data;
    },
    //关闭服务
    closeService() {
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
          this.clearVideoData();  //清空视频数据
          localStorage.removeItem('baseUserInfo');
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
      localStorage.setItem('baseUserInfo',JSON.stringify(data));
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
    //打开服务转派窗口
    toServiceTurn() {
      if (!this.staffInformation.length) {
        this.$message.warning("请先选择接待用户");
        return;
      }
      this.showServiceTransfer = true;
    },
    //来访记录
    toVisitingRecords() {
      if (!this.staffInformation.length) {
        this.$message.warning("请先选择接待用户");
        return;
      }
      this.visitingRecordsTitle =
        this.staffInformation[this.currentServiceIndex].name +
        `   (${this.staffInformation[this.currentServiceIndex].phone}，${this.staffInformation[this.currentServiceIndex].cardNo
        })  来访记录`;
      this.showVisitingRecords = true;
    },

    checkUser(data, index) {
      this.currentServiceIndex = index;
      this.staffInformation[this.currentServiceIndex] = data;
      // this.$store.commit('setCurrentServiceIndex', index)
      this.baseUserInfo = data;
    },
    showStpe() {
      this.stepShow = !this.stepShow;
    },
    lastStep() { },
    //跳转下一步
    nextStep(component, step) {
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
    baseUserInfo:{
      handler(val){
        localStorage.setItem('baseUserInfo',JSON.stringify(val));
      }
    },  
    stepSwitch(){
      this.baseUserInfo.processStep = 'baseInfo';
    },
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
          localStorage.setItem('baseUserInfo',val);
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
 .window-number{
  ::v-deep  .el-dialog__body{
    max-height: calc(100% - 3.75rem) !important; 
    .body-content{
      overflow-y: auto;
    }
  }
}
.select-dialog{
  ::v-deep  .el-dialog{
    top: 50% !important;
    margin-top: -150px !important;
  }
}
.select-Body {
  width: 100%;
  padding: 0.7059rem 0;

  .tips {
    font-size: 1.5714rem;
    font-family: Source Han Sans CN;
    font-weight: 400;
    color: #568fff;
  }

  .select-content {
    width: 100%;
    height: auto;
    padding: 2.4286rem 0;
    display: flex;
    align-items: center;
    justify-content: center;

    .item {
      width: 22.1429rem;
      // height: 17.3571rem;
      padding: 1.3571rem 0;
      background: #ffffff;
      border: 1px solid #94bdfd;
      box-shadow: 0px 0.4118rem 0px 0px rgba(213, 238, 251, 0.31);
      border-radius: 0.2941rem;
      background: url("@/assets/images/pad/itemSelect.png") no-repeat;
      background-size: 100% 100%;
      display: flex;
      flex-direction: column;
      align-items: center;

      &:nth-child(1) {
        margin-right: 2.5rem;
      }

      img {
        height: 11.6429rem;
      }

      p {
        width: 11.5714rem;
        height: 3.2857rem;
        background: rgba(148, 189, 253, 0.24);
        border-radius: 1.6429rem;
        font-size: 2rem;
        font-family: Source Han Sans CN;
        font-weight: 400;
        color: #4a7ae6;
        text-align: center;
        line-height: 3.2857rem;
      }
    }
  }
}
.el-dropdown-menu {
  .el-dropdown-menu__item {
    color: #5093e4;
  }

  img {
    width: 2.8235rem;
    margin-right: 5px;
  }
}

.screenAll {
  background: url("@/assets/images/home/homeBack.png") no-repeat;
  background-size: 100% 100%;
}

.conponent-container {
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg,
      RGBA(217, 228, 240, 1) 0%,
      RGBA(235, 246, 249, 1) 100%);
  position: relative;
  overflow: hidden;

  .header {
    width: 100%;
    height: 4.4444rem;
    background: linear-gradient(90deg,
        rgba(43, 114, 241, 0.9) 0%,
        rgba(101, 159, 239, 0.9) 100%);
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 2.9444rem 0 2.0556rem;
    border-top-right-radius: 1.6667rem;

    .left {
      width: 14rem;
      height: 2.7778rem;
      background: #3377ee;
      box-shadow: 0px 0.1667rem 0.4444rem 0px rgba(36, 89, 178, 0.19);
      border-radius: 1.3889rem;
      padding: 0 1.2222rem 0 0.8333rem;
      display: flex;
      align-items: center;
      justify-content: space-between;
      min-width: 150px;

      span {
        font-size: 1.3333rem;
        font-family: Adobe Heiti Std;
        font-weight: normal;
        color: #ffffff;
      }

      p {
        padding: 0;
        margin: 0;
        width: auto;
        height: 100%;
        display: flex;
        align-items: center;

        span {
          font-size: 1.3333rem;
          font-family: DIN;
          font-weight: bold;
          color: #ffffff;
        }

        img {
          width: 1.2778rem;
          margin-right: 0.6111rem;
        }
      }
    }

    .right {
      width: 10rem;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: space-between;

      img {
        height: 2.7778rem;
      }
    }
  }

  .content {
    width: 100%;
    height: calc(100% - 4.4444rem);
    border-top-left-radius: none;
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
          // width: 18.6667rem;
          height: 3.5rem;
          background: #6eb2ff;
          border-radius: 1.75rem;
          display: flex;
          align-items: center;
          justify-content: flex-start;
          padding: 0 2.0588rem;
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

  .turntable {
    width: 47.6471rem;
    height: 47.6471rem;
    border-radius: 50%;
    background: url("@/assets/images/pad/turnBack.png") no-repeat;
    background-size: 100% 100%;
    position: absolute;
    right: -25rem;
    bottom: 1rem;
    z-index: 2;
    .inherit-turn {
      width: 100%;
      height: 100%;
      border-radius: 50%;
      position: relative;
      display: flex;
      align-items: center;
      justify-content: center;

      .userArea {
        width: 100%;
        height: 100%;
        position: absolute;
        left: 0;
        top: 0;
        box-sizing: border-box;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        // position: relative;
        cursor: pointer;

        section {
          width: 100%;
          height: 100%;
          position: relative;

          .item-user {
            width: 9.2955rem;
            height: 24.1176rem;
            position: absolute;
            top: 0;
            left: 50%;
            margin-left: -4.648rem;
            transform-origin: 50% 100%;
            padding-top: 2.5rem;

            p {
              padding: 0;
              margin: 0;
              transform: rotate(90deg);
              font-size: 1.8235rem;
              font-family: Source Han Sans CN;
              font-weight: 400;
              color: #000000;
            }

            .unchecked {
              opacity: 0.5;
            }

            .checked {
              opacity: 0.6;
            }

            .centerSelected {
              opacity: 1;
            }
          }
        }
      }

      .userLogo {
        position: absolute;
        left: calc(50% - 11.5882rem);
        width: 23.1765rem;
        height: 23.1765rem;
        border-radius: 50%;
        background: linear-gradient(183deg, #ffffff 38%, #e9eef5 100%);
        background-size: 100% 100%;
        display: flex;
        align-items: center;
        justify-content: center;

        section {
          width: 100%;
          height: 100%;
          position: relative;

          .item-user-log {
            width: 4.5215rem;
            height: 11.5882rem;
            position: absolute;
            top: 0;
            left: 50%;
            margin-left: -2.2607rem;
            transform-origin: 50% 100%;
            padding-top: 1rem;

            .unchecked {
              width: 2.5rem;
            }

            .checked {
              width: 4rem;
            }

            .centerSelected {
              width: 4rem;
            }

            // p {
            //   padding: 0;
            //   margin: 0;
            //   transform: rotate(90deg);
            //   font-size: 1.8235rem;
            //   font-family: Source Han Sans CN;
            //   font-weight: 400;
            //   color: #5d6987;
            // }
            .unchecked {
              opacity: 0.4;
            }

            .checked {
              opacity: 0.6;
            }

            .centerSelected {
              opacity: 1;
            }
          }
        }
      }

      .turnCenter {
        width: 29.4118rem;
        height: 29.4118rem;
        border: 1px solid #ffffff;
        border-radius: 50%;
        background: url("@/assets/images/pad/turnCenter.png") no-repeat;
        background-size: 100% 100%;
      }

      .specifiedArea {
        width: 12.4706rem;
        height: 9.2353rem;
        background: url("@/assets/images/pad/specifiedArea.png") no-repeat;
        background-size: 100% 100%;
        position: absolute;
        left: 0;
        top: calc(50% - 4.6176rem);
        padding-top: 6rem;

        span {
          font-size: 1.0588rem;
          font-family: Source Han Sans CN;
          font-weight: 400;
          color: #9ab0ca;
        }
      }
    }
  }

  .userIdentity {
    position: absolute;
    width: 10rem;
    height: 6.4444rem;
    right: -10rem;
    bottom: 6rem;
    z-index: 10;
    p {
      padding: 0;
      margin: 0;
      width: 16rem;
      height: 5.7778rem;
      padding-left: 1.1111rem;
      background: linear-gradient(0deg, #d6dfec 0%, #ffffff 100%);
      // border-radius: 50%;
      border-top-left-radius: 2.8889rem;
      border-bottom-left-radius: 2.8889rem;
      font-size: 2rem;
      font-family: Source Han Sans CN;
      font-weight: 400;
      color: #7681a3;
      display: flex;
      align-items: center;
      justify-content: flex-start;
      flex-wrap: nowrap;
      position: absolute;
      bottom: 6.3333rem;
      right: 0rem;

      span {
        letter-spacing: 0.1111rem;
      }
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

.visitRecord {
  height: 80vh;

  .el-dialog__body {
    max-height: calc(100% - 3.75rem) !important;
    ;
    height: calc(100% - 3.75rem) !important;
  }

  .el-date-editor .el-range-separator {
    min-width: 25px;
  }
}

.serviceTransfer-coupon .serviceTransfer{
  height: calc(80vh + 60px);
}

</style>
