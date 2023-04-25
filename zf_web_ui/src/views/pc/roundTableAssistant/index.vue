<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-07-19 09:39:28
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-10-28 13:12:20
 * @FilePath: \hpNewHall\src\views\roundTableAssistant\index.vue
 * @Description: 圆桌帮办首页
-->
<template>
  <div class="container">
    <div class="container-header" :class="{'departOuter':departOuter}">
      <div class="windowInformation" v-if="!departOuter">
        <div class="left">
          <p>
            <img
              :src="require('@/assets/images/home/queueCondition.png')"
              alt=""
            />
            当前服务队列情况:
          </p>
          <div class="queueQueue">
            <p @click="workUserCondition">查看大厅帮办队列情况>></p>
          </div>
        </div>
        <div class="center">
          <div class="item" @click="queueCondition">
            <img
              :src="require('@/assets/images/home/serviceWaiting.png')"
              alt=""
            />
            <p>
              <span v-if="showText"> 等待中 </span>{{ waitIngNum }}
              <span v-if="showText">人</span>
            </p>
          </div>
          <div class="item" @click="toServiceQueue('2')">
            <img
              :src="require('@/assets/images/home/quickAssistant.png')"
              alt=""
            />
            <p>
              <span v-if="showText">服务中</span>{{ inServiceNum }}
              <span v-if="showText">人</span>
            </p>
          </div>
          <div class="item" @click="toServiceQueue('3')">
            <img
              :src="require('@/assets/images/home/roundTableAssistant.png')"
              alt=""
            />
            <p>
              <span v-if="showText">今日已服务</span>{{ todayServiceNum }}
              <span v-if="showText">人</span>
            </p>
          </div>
          <div class="item"  @click="allUserDataCondition">
            <img
              :src="require('@/assets/images/home/serviceQueue.png')"
              alt=""
            />
            <p>
              <span v-if="showText">今日组内服务</span
              >{{ todayServiceGroupNum }} <span v-if="showText">人</span>
            </p>
          </div>
        </div>
      </div>
      <div class="rightBg" v-if="!departOuter">
        <p @click="toNextService">接待下一位</p>
      </div>
      <!-- <img class="rightBg"  :src="require('')" alt="" /> -->
    </div>
    <div class="content">
      <div class="left" :class="{'departOuter':departOuter}">
        <ul v-if="!departOuter">
          <li>
            <div class="item" @click="toTableQueue">
              <p class="item-name">一桌联办服务列表</p>
              <div class="item-num">
                <img
                  :src="require('@/assets/images/home/serviceList.png')"
                  alt=""
                />
                <span>{{ appointNum }}</span>
                <span>条</span>
              </div>
            </div>
            <img
              class="more"
              :src="require('@/assets/images/home/angleBrackets.png')"
              alt=""
            />
          </li>
          <li>
            <div class="item" @click="toStagingServiceList">
              <p class="item-name">我的暂存服务列表</p>
              <div class="item-num">
                <img :src="require('@/assets/images/home/save.png')" alt="" />
                <span>{{ tempServiceNum }}</span>
                <span>条</span>
              </div>
            </div>
            <img
              class="more"
              :src="require('@/assets/images/home/angleBrackets.png')"
              alt=""
            />
          </li>
          <li>
            <div class="item" @click="toCompleteServiceList">
              <p class="item-name">已完成服务列表</p>
              <div class="item-num">
                <img
                  :src="require('@/assets/images/home/complite.png')"
                  alt=""
                />
                <span>{{ finishServiceNum }}</span>
                <span>人</span>
              </div>
            </div>
            <img
              class="more"
              :src="require('@/assets/images/home/angleBrackets.png')"
              alt=""
            />
          </li>
          <li>
            <div class="item" @click="toMyServiceTransferList">
              <p class="item-name" :class="{'item-toVerify':!isVerifyComplete}">我的服务转派列表</p>
              <div class="item-num">
                <img
                  :src="require('@/assets/images/home/transfer.png')"
                  alt=""
                />
                <span>{{ turnServiceNum }}</span>
                <span>人</span>
              </div>
            </div>
            <img
              class="more"
              :src="require('@/assets/images/home/angleBrackets.png')"
              alt=""
            />
          </li>
        </ul>
        <div v-if="!departOuter" class="operateArea">
          <p  @click="toSearch" class="operate">知识库</p>
          <p v-if="$store.state.user.basicUserInfo.resourceStatus === '1'" @click="toDatabase" class="operate">我的资料库</p>

          <p v-if="$store.state.user.basicUserInfo.questionStatus === '1'" @click="toCommonProblem" class="operate">常见问题</p>
          <p v-if="$store.state.user.basicUserInfo.qaStatus === '1'" @click="toIntelligentQA" class="operate">智能问答</p>
          <p v-if="" @click="toAssistantServiceRecord" class="operate">帮办历史</p>
          <p v-if="$store.state.user.basicUserInfo.evalStatus === '1'" @click="toAchievements" class="operate">绩效录入</p>
          <el-popover placement="right" trigger="hover">
            <div class="popover-content">
              <p @click="toDetailWeb('1')" class="website">
                国家企业信用信息公示系统
              </p>
              <p @click="toDetailWeb('2')" class="website">
                经营范围规范表述查询系统
              </p>
              <p @click="toDetailWeb('3')" class="website">企查查</p>
              <p @click="toDetailWeb('4')" class="website">
                上海市市场监督管理局
              </p>
              <p @click="toDetailWeb('5')" class="website">一网通办</p>
              <p @click="toDetailWeb('6')" class="website">网约车驾驶员从业资格证新增</p>
              <p @click="toDetailWeb('7')" class="website">网约车运输证新增、更新核发</p>
              <p @click="toDetailWeb('8')" class="website">市监局档案查询</p>
              <p @click="toDetailWeb('9')" class="website">黄浦区政务公开</p>
              <p @click="toGuideWeb('10')" class="website" style="color:green">全程网办系统使用指南</p>
              <p @click="toGuideWeb('11')" class="website" style="color:green">如何查询企业登记档案</p>
              <p @click="toGuideWeb('12')" class="website" style="color:green">如何获取电子营业执照</p>
            </div>
            <p class="operate" slot="reference">常用网站</p>
          </el-popover>
          
          <p></p>
        </div>

        <!-- <el-popover v-if="!departOuter" placement="right" trigger="click">
          <div class="popover-content">
            <div class="seekContent">
              <div class="seekOperate">
                <img
                  :src="require('@/assets/images/pad/voiceHelp.png')"
                  alt=""
                />
                <p>语音求助</p>
              </div>
              <div class="seekOperate" @click="videoHelp">
                <img
                  :src="require('@/assets/images/pad/videoHelp.png')"
                  alt=""
                />
                <p>视频求助</p>
              </div>
            </div>
          </div>
          <div class="seekHelp" slot="reference">
            <img :src="require('@/assets/images/pad/seekHelp.png')" alt="" />
            <p>求助</p>
          </div>
        </el-popover> -->
      </div>
      <div class="right">
        <keep-alive v-if="!departOuter">
          <component
            ref="operationFlow"
            :is="componentName"
            :serviceUserInfo="serviceUserInfo"
            :specificMatters="specificMatters"
            :serviceType="serviceType"
            @isVerify="isVerify"
            @getAppointNum="getAppointNum"
            @getStatServiceNum="getStatServiceNum"
            @openDepart="openDepart"
            @openBusinessType="openBusinessType"
          >
          </component>
        </keep-alive>
        <div class="video-coupon" v-if="isVideo">
          <meeting @openRoom="openRoom" @openBusinessType="openBusinessType" @finishVideo="finishVideo"></meeting>
        </div>
      </div>
    </div>
    <!-- ***中心或委办局老师查询是否有等待的视频咨询 -->
    <div class="toVideo" v-if="isHasVideo">
      <div class="wait centerXY flexY"  v-if="!chooseVideo" @click="receiveVideo">
        <img src="@/assets/video/9.png" alt="">
        <div>等待接听</div>
      </div>
      <template v-if="chooseVideo">
        <div class="videoList">
          <i class="el-icon-close" @click="chooseVideo = false"></i>
          <div class="choose-video centerXY" :key="item.id" v-for="item in accessIdList">
            <div class="choose-detail centerXY">
              <div class="head">
                <img src="@/assets/video/people_default.png">
              </div>
              <div class="name">
                <div>{{item.callUserName}}</div>
                <div>邀请你视频咨询</div>
              </div>
            </div>
            <div class="choose-btn centerY">
              <img class="reject" @click="rejectVideo(item)" src="@/assets/video/7.png" alt="">
              <img class="receive" @click="toReceiveVideo(item)" src="@/assets/video/11.png" alt="">
            </div>
          </div>
        </div>
      </template>
      
    </div>
    <!-- 扫码 -->
    <el-dialog
      v-dialog-drag
      title="扫码"
      :visible.sync="eleShow"
      custom-class="preview-dialog"
      width="600px"
      height="400px"
      append-to-body>
      <div  
          style="
          width: 100%;
          display: flex;
          justify-content: center;
          margin-bottom: 20px;">
          <td v-if="index==10">
            <img src="@/assets/images/guide.png" alt="二维码" />
          </td>
          <td v-if="index==11">
            <img src="@/assets/images/archives.png" alt="二维码" />
          </td>
          <td v-if="index==12">
            <img src="@/assets/images/lcicense.png" alt="二维码" />
          </td>
          
      </div>
      <div style="text-align: center">
        <strong>请用手机浏览器扫描上方二维码。</strong>
      </div>
    
    </el-dialog>
    <el-dialog
      title="等待队列"
      :visible.sync="showQueueCondition"
      width="80%"
      class="pageDialog"
      center
      append-to-body
      v-dialogDrag
    >
      <queueCondition
        ref="queueCondition"
        v-if="showQueueCondition"
        @openshowAddDocument="openshowAddDocument"
        @openVideo="openVideo"
        @closeQueueCondition="closeQueueCondition"
      />
    </el-dialog>
    <el-dialog
      title="服务队列"
      :visible.sync="showServiceQueue"
      width="80%"
      class="pageDialog"
      center
      append-to-body
      v-dialogDrag
    >
      <serviceQueue v-if="showServiceQueue" :serviceStatus="serviceStatus" />
    </el-dialog>
    <el-dialog
      title="智能问答"
      :visible.sync="showIntelligentQA"
      width="80%"
      center
      class="pageDialog"
      append-to-body
      v-dialogDrag
    >
      <intelligentQA
        v-if="showIntelligentQA"
        @setComponentName="setComponentName"
      />
    </el-dialog>
    <el-dialog
      title="我的资料库"
      :visible.sync="showDatabase"
      width="80%"
      center
      class="pageDialog"
      append-to-body
      v-dialogDrag
    >
      <database v-if="showDatabase" />
    </el-dialog>
    <el-dialog
      title="常见问题"
      :visible.sync="showCommonProblem"
      width="80%"
      center
      class="pageDialog"
      append-to-body
      v-dialogDrag
    >
      <commonProblem v-if="showCommonProblem" />
    </el-dialog>
    <!-- 联办预约信息确认 -->
    <el-dialog
      title="联办预约信息确认"
      :visible.sync="appointShow"
      width="80%"
      center
      class="pageDialog"
      append-to-body
      v-dialogDrag
    >
      <appointInfo v-if="appointShow" :dataObj="dataObj"  @closeAppoint="closeAppoint"/>
    </el-dialog>
    <el-dialog
      title="帮办历史"
      :visible.sync="showVisitingRecords"
      width="80%"
      center
      class="pageDialog"
      append-to-body
      v-dialogDrag
    >
      <visitingRecords
        v-if="showVisitingRecords"
        @setShowVisitingRecords="setShowVisitingRecords"
      />
    </el-dialog>
    <!-- 我的服务转派列表 -->
    <el-dialog
      title="我转派的服务"
      :visible.sync="showMyServiceTransferList"
      width="80%"
      center
      class="pageDialog"
      append-to-body
      v-dialogDrag
    >
      <myServiceTransferList v-if="showMyServiceTransferList" />
    </el-dialog>
    <!-- 一桌联办服务列表 -->
    <el-dialog
      title="一桌联办服务"
      :visible.sync="tableServiceList"
      width="80%"
      center
      class="pageDialog"
      append-to-body
      v-dialogDrag
    >
      <tableList
        v-if="tableServiceList"
        :isHelp="isHelp"
        @setProceedStepData="setProceedStepData"
      />
    </el-dialog>
    <!-- 暂存服务列表 -->
    <el-dialog
      title="我暂存的服务"
      :visible.sync="showStagingServiceList"
      width="80%"
      center
      class="pageDialog"
      append-to-body
      v-dialogDrag
    >
      <stagingServiceList
        v-if="showStagingServiceList"
        @setProceedStepData="setProceedStepData"
      />
    </el-dialog>
    <!-- 绩效列表 -->
    <el-dialog
      title="绩效列表"
      :visible.sync="showAchievemenetsList"
      width="90%"
      center
      class="pageDialog"
      append-to-body
      v-dialogDrag
    >
      <achievements
        v-if="showAchievemenetsList"
      />
    </el-dialog>
    <el-dialog
      title="已完成的服务"
      :visible.sync="showCompleteServiceList"
      width="80%"
      center
      class="pageDialog"
      append-to-body
      v-dialogDrag
    >
      <completeServiceList v-if="showCompleteServiceList" />
    </el-dialog>
    <el-dialog
        title="查看大厅帮办队列情况"
        :visible.sync="showWorkUserList"
        width="80%"
        class="pageDialog"
        center
        append-to-body
        v-dialogDrag
    >
      <workUserCondition
          v-if="showWorkUserList"
          @closeWorkUserCondition="closeWorkUserCondition"
      />
    </el-dialog>

    <el-dialog
        title="今日组内服务情况"
        :visible.sync="showAllUserDataList"
        width="80%"
        class="pageDialog"
        center
        append-to-body
        v-dialogDrag
    >
      <allUserDataCondition
          v-if="showAllUserDataList"
          @closeWorkUserCondition="closeAllUserDataCondition"
      />
    </el-dialog>
    <!-- 补录信息 -->
    <el-dialog
      title="补录信息"
      :visible.sync="showAddDocument"
      width="80%"
      center
      class="addDocumentDialog"
      append-to-body
      v-dialogDrag
    >
      <el-form
        v-if="showAddDocument"
        :model="documentInfo"
        :rules="documentRules"
        ref="document"
        label-width="10rem"
      >
        <el-form-item label="姓名" prop="name">
          <el-input
            v-model.trim="documentInfo.name"
            placeholder="请输入姓名"
          ></el-input>
        </el-form-item>
        <el-form-item label="证件类型" prop="cardType">
          <el-select
            v-model="documentInfo.cardType"
            placeholder="请选择证件类型"
          >
            <el-option label="身份证" value="1"></el-option>
            <el-option label="港澳通行证" value="2"></el-option>
            <el-option label="护照" value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="证件号码" prop="cardNo">
          <el-input
            v-model.trim="documentInfo.cardNo"
            placeholder="请输入证件号码"
          ></el-input>
        </el-form-item>
        <!-- <el-form-item label="备注" prop="resourceInfo">
          <el-input type="textarea" v-model="documentInfo.resourceInfo"></el-input>
        </el-form-item> -->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <p @click="showAddDocument = false">取消</p>
        <p @click="compliteNext">确定</p>
      </div>
    </el-dialog>
    <!-- 知识库 -->
    <el-dialog
      title="知识库"
      :visible.sync="searchShow"
      width="80%"
      class="window-number"
      custom-class="serviceTransfer"
      v-dialogDrag
    >
      <search  />
    </el-dialog>
    <!-- 选择部门 -->
    <el-dialog
      title="选择工作人员"
      :visible.sync="videoShow"
      width="60%"
      class="window-number"
      custom-class="serviceTransfer"
      v-dialogDrag
    >
      <chooseDepart :isNeedOpenVideo="isNeedOpenVideo"  @openVideo="openVideo" v-if="videoShow" :groupType="groupType" :type="type" @closeDepart="closeDepart" />
    </el-dialog>
    <!-- 选择中心或委办局 -->
    <el-dialog
      v-dialog-drag
      title="选择工作人员"
      :visible.sync="showSelectBusinessType"
      class="select-dialog"
      width="80%"
      append-to-body
    >
      <div class="select-Body">
        <div class="select-content">
          <div class="item" @click="selectTarget('2')">
            <img
              :src="require('@/assets/images/pad/consultingService.png')"
              alt=""
            />
            <p>中心</p>
          </div>
          <div
            class="item"
            @click="selectTarget('3')"
          >
            <img
              :src="require('@/assets/images/pad/storesReserve.png')"
              alt=""
            />
            <p>委办局</p>
          </div>
          <div
            class="item"
            @click="selectTarget('1')"
          >
            <img
              :src="require('@/assets/images/pad/storesReserve.png')"
              alt=""
            />
            <p>街道</p>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {
  nextService,
  getQueueNum,
  statServiceNum, getAllUserService,
  isDeskAppointment,
  getGroupById,
  getListWithPage,
  saveNameAndCard,
  isHaveNewCall,
  joinRoom,
  workUserService
} from "@/api/modules/helpAgent";
import meeting from '../../meeting/Meeting.vue';
import operationFlow from "@/views/pc/operationFlow/index.vue";
import intelligentQA from "./components/intelligentQA.vue";
import database from "./components/database.vue";
import commonProblem from "./components/commonProblem.vue";
import appointInfo from "./components/appointInfo.vue";
import queueCondition from "./components/queueCondition.vue";
import workUserCondition from "./components/workUserCondition.vue";
import allUserDataCondition from "./components/allUserDataCondition.vue";
import serviceQueue from "./components/serviceQueue.vue";
import assistantServiceRecord from "./components/assistantServiceRecord.vue";
import visitingRecords from "../operationFlow/components/visitingRecords.vue";
import myServiceTransferList from "./components/myServiceTransferList.vue";
import stagingServiceList from "./components/stagingServiceList.vue";
import achievements from "./components/achievements.vue";
import tableList from "./components/tableList.vue";
import completeServiceList from "./components/completeServiceList.vue";
import { formatDate } from "@/utils/index";
import chooseDepart from "../../meeting/chooseDepart.vue";
import search from "./search/index.vue";
import videoOperate from "@/utils/video";
export default {
  
  name: "GuideClothing",
  components: {
    search,
    operationFlow,
    queueCondition,
    workUserCondition,
    allUserDataCondition,
    serviceQueue,
    intelligentQA,
    database,
    commonProblem,
    assistantServiceRecord,
    visitingRecords,
    myServiceTransferList,
    stagingServiceList,
    completeServiceList,
    achievements,
    tableList,
    appointInfo,
    meeting,
    chooseDepart
  },
  data() {
    return {
      searchShow:false,
      showAddDocument:false,
      appointShow:false,
      tableServiceList:false,
      showQueueCondition: false,
      showWorkUserList: false,
      showAllUserDataList: false,
      showServiceQueue: false,
      showIntelligentQA: false,
      showCommonProblem: false,
      showDatabase: false,
      showVisitingRecords: false,
      showMyServiceTransferList: false,
      showStagingServiceList: false,
      showCompleteServiceList: false,
      showAchievemenetsList:false,
      serviceStatus: "2",
      quickWaitNum: 0,
      tableWaitNum: 0,
      windowWaitNum: 0,
      totalServiceNum: 0,
      waitIngNum: 0,
      todayServiceGroupNum: 0,
      inServiceNum: 0,
      todayServiceNum: 0,
      serviceUserInfo: {},
      componentName: "operationFlow",
      specificMatters: {},
      serviceType: "",
      screenWidth: null,
      showText: true,
      serviceingNum: 0,
      tempServiceNum: 0,
      finishServiceNum: 0,
      turnServiceNum: 0,
      webSite: "",
      //扫码标识
      eleShow: false,
      //1-全程网办系统使用指南 2-如何查询企业登记档案 3-如何获取电子营业执照
      index: null,
      isVerifyComplete:true,  ////转派是否审核,
      appointTime:'',
      isHelp:'',  //是否部门老师  1是 2否
      appointNum:'', //联办数量
      dataObj:{},
      documentInfo: {
        queueId:'',
        name: "",
        cardType: "",
        cardNo:''
      },
      documentRules: {
        name: [{ required: true, message: "请输入姓名", trigger: "blur" }],
        cardNo: [{ required: true, message: "请输入证件号码", trigger: "blur" }],
        cardType: [
          { required: true, message: "请选择证件类型", trigger: "change" },
        ],
      },
      isVideo:false,  //是否开启视频咨询
      videoShow:false,
      type:'',
      chooseVideo:false,  //选择接听与否
      isHasVideo:false,  //是否有视频咨询
      accessIdList:[],  //待接视频咨询列表
      groupType:'', //1中心   2委办局
      baseUserInfo:{},
      showSelectBusinessType:false,
      isNeedOpenVideo:true,  //是否需要打开视频 
      roomOpen:false,  //视频房间是否打开
      departOuter:false,  //是否中心外人员页面显示设置 
    };
  },
  mixins:[videoOperate],
  computed: {
    basicUserInfo() {
      console.log(this.$store.state.user.basicUserInfo);
      return this.$store.state.user.basicUserInfo;

    },
    serviceOperateStatus() {
      return this.$store.state.pageData.serviceOperateStatus;
    },
  },
  mounted() {
    const that = this;
    this.getQueueNum();
    this.getStatServiceNum();
    this.showScreenText();
    window.onresize = () => {
      return (() => {
        that.screenWidth = document.documentElement.clientWidth;
      })();
    };

    //判断是否为帮办老师  1是  2否
    getGroupById({id:this.$store.state.user.basicUserInfo.groupId}).then(res=>{
      if(res.code == 200 && res.data.deskStatus == '1' ){
        that.isHelp = '1';
        that.$forceUpdate();
        that.getAppoint();
        that.appointTime = setInterval(function(){
          that.getAppoint();
        },30000)
      }
      
    })
    
    this.getAppointNum();


    // 中心或委办局老师查询是否有等待的视频咨询
    let hasVideo = setInterval(function(){
      that.toVideoList();
    },5000)

  },
  methods: {
    toSearch(){
      this.searchShow = true;
    },
    //获取待接收视频列表
    toVideoList(){
      let that = this;
      isHaveNewCall().then(res=>{
        //待接听
        if(res.data.accessCallList.length > 0){
          that.isHasVideo = true;
          that.accessIdList = res.data.accessCallList;
        }else{
          that.isHasVideo = false;
        }  
        // 已接通
        let accessTouchList = res.data.accessTouchList;
        if(accessTouchList.length > 0){
          let length = accessTouchList.length;
          that.openVideo();
          that.startOpenVideo(accessTouchList[length-1]);  //开启视频咨询
        }      
      })
    },
    //打开中心或委办局
    openBusinessType(baseUserInfo,needOpenVideo){
      this.showSelectBusinessType = true;
      if(baseUserInfo){
        this.baseUserInfo = baseUserInfo;
      }
      if(needOpenVideo){
        this.isNeedOpenVideo = false;  //在视频打开下，呼叫组长或委办局不需要再次打开视频
      }else{
        this.isNeedOpenVideo = true;
      }
    },
    //打开 2中心 3委办局 1街道
    selectTarget(type){
      this.showSelectBusinessType = false;
      this.openDepart(type);
    },
    // 等待接听
    receiveVideo(){
      this.chooseVideo = true;
    },
    //拒接视频
    rejectVideo(data){
      let that = this;
      that.$confirm("确定拒绝接收视频吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(()=>{
        let parems = {
          accessId:data.id,
          existStatus:3,  //2-接通、3-拒绝
        };
        joinRoom(parems).then(res=>{
          that.toVideoList();  //更新待接列表
        })
      }).catch(()=>{

      })
    },
    //接通视频
    toReceiveVideo(data){
      let that = this;
      let parems = {
        accessId:data.id,
        existStatus:2,  //2-接通、3-拒绝
      };
      joinRoom(parems).then(res=>{
        that.isVideo = true;
        that.roomOpen = false;
        that.chooseVideo = false;
        that.startOpenVideo(data);  //开启视频咨询
        that.toVideoList();  //更新待接列表
        // const params = {
        //   queueId: data.queueId,
        // };
        // workUserService(params).then((res) => {
        //   if (res.code === 200) {
        //     if (res.data) {
        //       console.log(res.data);
        //       this.$store.commit("setServiceOperateStatus", true);
        //       if (!res.data.firstServiceBeginTime) {
        //         res.data.firstServiceBeginTime = formatDate(new Date());
        //         res.data.serviceBeginTime = res.data.firstServiceBeginTime
        //       }
        //       res.data.id = res.data.queueId;
        //       localStorage.setItem('baseUserInfo',res.data);
        //       this.$store.commit("setBaseUserInfo", res.data);
        //     }
        //   }
        // });
        
      })
    },
    openDepart(type){
      this.videoShow = true;
      this.groupType = type; //2组长   3委办局  1街道
      this.baseUserInfo =this.baseUserInfo;
    },
    closeDepart(){
      this.videoShow = false;
    },
    //打开视频咨询
    openVideo(){
      this.isVideo = true;
      this.roomOpen = false;
    },
    // 打开房间 视觉
    openRoom(){
      this.roomOpen = true;
    },
    //关闭视频咨询
    finishVideo(){
      this.isVideo = false;
    },
    openshowAddDocument(){
      this.showQueueCondition = false;
      this.showAddDocument = true;
    },
    //补录信息  确定
    compliteNext(){
      let that = this;
      this.$refs["document"].validate((valid) => {
        if (valid) {
          saveNameAndCard(that.documentInfo).then(res=>{
            if(res.code == 200){
              this.$message("保存成功！");
              this.showAddDocument = false;
            }
          })
        }
      })
    },
    //获取联办数量
    getAppointNum(){
      let that = this;
      const params = {
            name: '',
            phone: '',
            companyName: '',
            companyCode:'',
            deskStatus: "",
            beginTime: '',
            endTime:  "",
            pageNum: 1,
            pageSize: 10,
          };
      getListWithPage(params)
        .then((res) => {
          if (res.code === 200) {
            that.appointNum = res.data.total;
          }
      })
    .catch((err) => {
    });
    },
    closeAppoint(){
      this.appointShow = false;
      this.getAppointNum();
    },
    getAppoint(){
      let that = this;
      isDeskAppointment().then(res=>{
        if(res.code != 200){
          clearInterval(that.appointTime)
        }else if(res.data.deskNum > 0){
          that.dataObj =  JSON.stringify(res.data);
          that.appointShow = true;
        }
      }).catch((err) => {
        clearInterval(that.appointTime)
      });
    },
    //打开一桌联办
    toTableQueue(){
      this.tableServiceList = true;
    },
    //转派是否审核
    isVerify(status){
      console.log(status)
      this.isVerifyComplete = status;
    },
    toGuideWeb(id){
      this.eleShow = true;
      this.index = id;
    },

    toDetailWeb(type) {
      switch (type) {
        case "1":
          window.open("https://www.gsxt.gov.cn/index.html");
          break;
        case "2":
          window.open("https://jyfwyun.com");
          break;
        case "3":
          window.open("https://www.qcc.com");
          break;
        case "4":
          window.open("http://scjgj.sh.gov.cn/");
          break;
        case "5":
          window.open("https://zwdt.sh.gov.cn/govPortals/index.do");
          break;
        case "6":
          window.open("https://zwdt.sh.gov.cn/govPortals/bsfw/item/4df7e909-5471-49cf-adf6-3cfc7f4604bd");
          break;
        case "7":
          window.open("https://zwdt.sh.gov.cn/govPortals/bsfw/item/29b5d21a-b059-44fa-bd55-ae53e0137960");
          break;
        case "8":
          window.open("http://fw.scjgj.sh.gov.cn/achieve_outer/apply/mainEtps");
          break;
        case "9":
          window.open("https://www.shhuangpu.gov.cn/zw/ysqxxgksl.html");
          break;
      }
    },
    setProceedStepData(data) {
      this.$refs.operationFlow.getProceedData(data);
      this.showStagingServiceList = false;
    },
    //我的服务转派列表
    toMyServiceTransferList() {
      this.showMyServiceTransferList = true;
      this.isVerifyComplete = true;  //关闭转派红点提示
    },
    //我的暂存服务列表
    toStagingServiceList() {
      this.showStagingServiceList = true;
    },
    //绩效列表
    toAchievements(){
      this.showAchievemenetsList = true;
    },
    //已完成服务列表
    toCompleteServiceList() {
      this.showCompleteServiceList = true;
    },
    //视频求助
    videoHelp() {
      window.open("https://www.miaohuida.com:3004/#/web/room");
    },
    //来访记录操作
    setShowVisitingRecords(data) {
      this.showVisitingRecords = data;
    },

    //是否展示文字
    showScreenText() {
      this.screenWidth = document.documentElement.clientWidth;
      if (this.screenWidth < 1260) {
        this.showText = false;
      } else {
        this.showText = true;
      }
    },
    // 智能问答获取办理事项
    setComponentName(componentName, data) {
      this.showIntelligentQA = false;
      this.serviceType = "2";
      this.componentName = componentName;
      const obj = {
        serviceOid: data.serviceOid,
        serviceName: data.name,
      };
      this.specificMatters = obj;
    },

    //打开智能问答弹窗
    toIntelligentQA() {
      this.showIntelligentQA = true;
    },
    //打开资料库弹窗
    toDatabase() {
      this.showDatabase = true;
    },
    //打开常见问题列表
    toCommonProblem() {
      this.showCommonProblem = true;
    },
    //帮办历史
    toAssistantServiceRecord() {
      this.showVisitingRecords = true;
    },
    getQueueNum() {
      getQueueNum().then((res) => {
        if (res.code === 200) {
          this.waitIngNum = res.data.waitIngNum ?? "0";
          this.inServiceNum = res.data.inServiceNum ?? "0";
          this.todayServiceNum = res.data.todayServiceNum ?? "0";
          this.todayServiceGroupNum = res.data.todayServiceGroupNum ?? "0";
          this.$store.commit("setServiceOperateStatus", false);
        }
      });
    },
    //统计服务数量
    getStatServiceNum() {
      statServiceNum().then((res) => {
        if (res.code === 200) {
          this.serviceingNum = res.data.serviceingNum ?? 0;
          this.tempServiceNum = res.data.tempServiceNum ?? 0;
          this.finishServiceNum = res.data.finishServiceNum ?? 0;
          this.turnServiceNum = res.data.turnServiceNum ?? 0;
        }
      });
    },
    toNextService() {
      nextService().then((res) => {
        if (res.code === 200) {
          if (res.data) {
            res.data.id = res.data.queueId;
            localStorage.setItem('baseUserInfo',res.data);
            this.documentInfo.queueId = res.data.queueId;
            if (!res.data.firstServiceBeginTime) {
              res.data.firstServiceBeginTime = formatDate(new Date());
            }
            this.serviceUserInfo = res.data;
            if(res.data.takeNumberType == '4'){
              this.showAddDocument = true;
            }
            if(res.data.takeNumberType == '5' && res.data.haVideoAccess.roomNumber){
              this.isVideo = true;
              this.roomOpen = false;
              this.startOpenVideo(res.data.haVideoAccess)
            }
            this.getQueueNum();
            this.getStatServiceNum();
          } else {
            this.$message.warning("暂无可接待人员");
          }
        }
      });
    },
    queueCondition() {
      this.showQueueCondition = true;
    },
    closeQueueCondition() {
      this.showQueueCondition = false;
    },
    /**
     * 全部帮办人员的帮办
     */
    workUserCondition() {
      this.showWorkUserList = true;
    },
    closeWorkUserCondition() {
      this.showWorkUserList = false;
    },
    /**
     * 组内所有帮办的人的服务星系
     */
    allUserDataCondition() {
      this.showAllUserDataList = true;
    },
    closeAllUserDataCondition() {
      this.showAllUserDataList = false;
    },
    toServiceQueue(serviceStatus) {
      this.showServiceQueue = true;
      this.serviceStatus = serviceStatus;
    },
  },
  watch: {
    screenWidth: {
      handler(val) {
        if (val) {
          if (val < 1260) {
            this.showText = false;
          } else {
            this.showText = true;
          }
        }
      },
    },
    serviceOperateStatus: {
      handler(val) {
        if (val) {
          this.getQueueNum();
          this.getStatServiceNum();
        }
      },
    },
  },
};
</script>
<style lang="scss" scoped>
  .departOuter{
    background: unset !important;
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
          &:nth-child(1) ,&:nth-child(2) {
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
    .dialog-footer {
      p {
        &:nth-child(1) {
          background: #ffffff;
          border: 1px solid #4988f2;
          box-shadow: 0px 0px 1.8125rem 0px rgba(204, 177, 121, 0.31);
          color: #2473ff;
          margin-right: 2.1875rem;
          cursor: pointer;
        }

        &:nth-child(2) {
          background: linear-gradient(90deg, #2473ff 0%, #56b1fd 100%);
          box-shadow: 0px 0px 1.5rem 0px rgb(40 107 198 / 31%);
          color: #ffffff;
        }
      }
    }
  .toVideo{
        position: fixed;
        z-index: 2;
        top: 6px;
        right: 30%;
        display: flex;
        flex-direction: column;
        align-items: end;
	      border-radius: 8px;
        
        
        .wait{
            box-shadow: -5px 3px 26px 3px rgba(0, 48, 83, 0.15);
            background: #fff;
            width: 55px;
            height: 55px;
            font-size: 10px;
            color: #6c7588;
            border-radius: 8px;
            cursor: pointer;
            img{
                width: 20px;
                margin-bottom: 2px;
            }
        }
        .choose-video{
            border-top: 1px solid #e0e5eb;
            // box-shadow: -5px 3px 26px 3px rgba(0, 48, 83, 0.15);
            background: #fff;
            // margin-top: 15px;
            padding: 20px 10px 10px;
            // border-radius: 8px;
            color: #696c76;
            position: relative;
            img{
                cursor: pointer;
                &:hover{
                  opacity: 0.8;
                }
            }  
        }
        .videoList{
          position: relative;
          border-radius: 8px;
          height: 300px;
          overflow-y: auto;
          .el-icon-close{
              font-size: 15px;
              position: absolute;
              top: 2px;
              right: 3px;
              cursor: pointer;
              z-index: 1;
              &:hover{
                opacity: 0.8;
              }
          }
          &>div:nth-child(2){
            border-top:none;
          }
        }
        .name{
            font-size: 12px;
            color: #313233;
            line-height: 16px;
            margin-left: 10px;
            width: 90px;
            text-align: left;
            margin-right: 25px;
        }
        .reject{
            margin-right: 5px;
        }
    }
  .container {
    width: 100%;
    height: 100%;
    padding: 0.8333rem 1.25rem 1.25rem 1.25rem;

    .container-header {
      width: 100%;
      height: 7.55rem;
      padding-left: 0.875rem;
      display: flex;
      align-items: center;
      justify-content: space-between;

      .windowInformation {
        width: calc(100% - 17.5rem);
        height: calc(100% - 1.275rem);
        display: flex;
        align-items: center;
        justify-content: flex-start;
        padding: 0.75rem 1.6875rem;

        .left {
          height: 100%;
          width: 18.3333rem;
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: space-between;
          cursor: pointer;

          p {
            width: 100%;
            height: 1.375rem;
            padding: 0;
            margin: 0;
            display: flex;
            align-items: center;
            justify-content: flex-start;
            font-size: 1.5rem;
            font-family: PingFang SC;
            font-weight: bold;
            color: #333333;

            img {
              width: 1.375rem;
              margin-right: 0.6875rem;
              margin-top: 0.1rem;
            }
          }

          .queueQueue {
            width: auto;
            height: auto;
            border-radius: 1.46rem;
            padding: 0.25rem 1.5rem;

            p {
              font-size: 1.33rem;
              font-family: Source Han Sans CN;
              font-weight: 400;
              color: #516cb8;
            }
          }
        }

        .center {
          width: calc(100% - 18.3333rem);
          height: 100%;
          padding: 0 3.5rem;
          display: flex;
          align-items: center;
          justify-content: space-between;

          .item {
            width: 30%;
            height: 100%;
            display: flex;
            align-items: center;
            justify-content: flex-start;

            img {
              width: 2.8125rem;
              margin-right: 0.3125rem;
            }

            p {
              height: 100%;
              display: flex;
              align-items: center;
              justify-content: center;
              font-size: 3rem;
              font-family: DIN;
              font-weight: bold;
              color: #343434;
              margin: 0 1.0833rem;

              span {
                font-size: 1.6rem;
                font-family: Microsoft YaHei;
                font-weight: 400;
                color: #383f58;

                &:nth-child(1) {
                  margin-right: 5px;
                }

                &:nth-child(2) {
                  margin-left: 5px;
                }
              }
            }
          }
        }

        .right {
          width: 15%;
          height: 100%;
          display: flex;
          flex-direction: column;
          align-items: flex-end;
          justify-content: flex-end;

          p {
            padding: 0 0.625rem;
            margin: 0;
            height: 1.875rem;
            background-color: #6895f7;
            border-radius: 0.9375rem;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #fff;
          }
        }
      }

      .rightBg {
        // width: 197px;
        width: 17.5rem;
        height: 7.55rem;
        background: url("@/assets/images/home/nextIcon.png") no-repeat;
        background-size: 100% 100%;
        display: flex;
        align-items: center;
        justify-content: center;

        p {
          margin: 0;
          padding: 0.5rem 0.8rem;
          cursor: pointer;
          // background-color: ;
          background: linear-gradient(90deg, #6d93e8 0%, #77b0fe 100%);
          box-shadow: 0px 0.5rem 0px 0px rgba(106, 159, 231, 0.31);
          border-radius: 2.5rem;
          font-size: 1.8571rem;
          font-family: Microsoft YaHei;
          font-weight: 400;
          color: #ffffff;
        }
      }
    }

    .content {
      width: 100%;
      height: calc(100% - 7.55rem);
      padding-top: 1.0833rem;
      display: flex;
      align-items: center;
      justify-content: space-between;

      .left {
        width: 18.5833rem;
        height: 100%;
        background: #f5f5f5;
        border-radius: 0.375rem;
        padding: 0.875rem;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: space-between;
        padding-bottom: 2.5rem;

        ul {
          padding: 0;
          margin: 0;
          height: auto;
          width: 100%;
          overflow-y: auto;

          &::-webkit-scrollbar {
            width: 0.4375rem;
            background-color: #fff;
          }

          &::-webkit-scrollbar-thumb {
            width: 0.4375rem;
            height: 0.625rem !important;
            background: rgba(103, 112, 119, 0.26);
            border-radius: 0.25rem;
          }

          li {
            margin: 0;
            list-style: none;
            width: 100%;
            height: 8.5833rem;
            background: #fdfeff;
            border: 1px solid #e9eff5;
            border-radius: 0.3333rem;
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 0.9375rem 1.4375rem 1rem 1.6875rem;

            .item {
              width: 80%;
              height: 100%;
              cursor: pointer;
              display: flex;
              flex-direction: column;
              align-items: center;
              justify-content: space-between;

              .item-name {
                padding: 0;
                margin: 0;
                width: 100%;
                text-align: left;
                font-size: 1.3333rem;
                font-family: Microsoft YaHei;
                font-weight: 400;
                color: #333333;
                &.item-toVerify{
                  position: relative;
                  &::after{
                    content: '';
                    position: absolute;
                    top: -3px;
                    right: -16px;
                    width: 15px;
                    height: 15px;
                    background-image: url('@/assets/images/dot.gif');
                    background-size: 100% 100%;
                    background-repeat: no-repeat;
                    border-radius: 50%;
                  }
                }

              }

              .item-num {
                width: 100%;
                height: 3.3333rem;
                display: flex;
                align-items: center;

                img {
                  width: 3.3333rem;
                }

                span {
                  padding: 0;
                  margin: 0;

                  &:nth-child(2) {
                    font-size: 2rem;
                    font-family: DIN;
                    font-weight: 500;
                    color: #333333;
                    margin: 0 1rem;
                  }

                  &:nth-child(3) {
                    font-size: 1.3333rem;
                    font-family: Microsoft YaHei;
                    font-weight: 400;
                    color: #626a7f;
                  }
                }
              }
            }

            .more {
              width: 1.5rem;
            }
          }
        }

        .operateArea {
          width: 100%;
          margin-top: 5%;
          display: flex;
          align-items: flex-start;
          justify-content: space-between;
          flex-wrap: wrap;
          .operate{
            background: linear-gradient(90deg, #2473FF 0%, #56B1FD 100%);
          }

          p {
            padding: 0;
            margin: 0;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.3rem;
            font-family: Microsoft YaHei;
            font-weight: 400;
            color: #ffffff;
            text-shadow: 0px 0px 0.4167rem rgba(0, 56, 156, 0.63);
          }

          .operate {
            width: 8.3333rem;
            height: 4rem;
            border-radius: 0.25rem;
            margin-bottom: 0.875rem;
            box-shadow: 0px 0px 2.4167rem 0px rgba(204, 177, 121, 0.31);
            cursor: pointer;
          }
        }

        .seekHelp {
          width: 16.8333rem;
          height: 4rem;
          border: 1px solid #4988f2;
          // background: linear-gradient(90deg, #2473FF 0%, #56B1FD 100%);
          border-radius: 2rem;
          display: flex;
          align-items: center;
          justify-content: center;

          img {
            width: 1.9167rem;
            margin-right: 0.9167rem;
          }

          p {
            padding: 0;
            margin: 0;
            font-size: 1.5rem;
            font-family: Microsoft YaHei;
            font-weight: 400;
            color: #464646;
          }
        }
      }

      .right {
        width: calc(100% - 19.4167rem);
        height: 100%;
        // background-color: #fff;
        border-radius: 0.375rem;
      }
    }
  }

  ::v-deep .el-dialog  {
    // height: 80vh !important;
    // margin-top: 10vh !important;
  }
</style>
<style lang="scss">
  .el-popover {
    .popover-content {
      .seekContent {
        width: auto;
        height: 7.5rem;
        padding: 0.875rem 2rem 1.25rem 2.1rem;
        display: flex;
        align-items: center;
        justify-content: flex-start;

        .seekOperate {
          width: auto;
          height: 100%;
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: space-around;
          cursor: pointer;

          &:nth-child(1) {
            margin-right: 5.125rem;
          }

          img {
            width: 2.3125rem;
          }

          p {
            padding: 0;
            margin: 0;
            font-size: 1.625rem;
            font-family: Source Han Sans CN;
            font-weight: 500;
            color: #2f2f2f;
          }
        }
      }
      .website {
        padding: 1.2rem 0;
        cursor: pointer;
        color: #00a2ff;
        text-decoration: underline;
      }
    }
  }

  .pageDialog {
    height: 100vh;

    .el-dialog {
      height: 80vh;

      .el-dialog__body {
        height: calc(100% - 3.75rem) !important;
        max-height: 100%;
      }
    }
  }
</style>
<style lang="scss" scoped>
  @import './index.scss';
  .roomFail{
    position: absolute;
    z-index: -1;
  }
</style>