<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-07-21 13:09:27
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-11-11 09:21:19
 * @FilePath: \hpNewHall\src\views\pad\home\index.vue
 * @Description: pad系统显示首页
-->
<template>
  <div class="container">
    <div class="left-content">
      <el-popover placement="right" trigger="click" v-model="visible">
        <div class="popover-content">
          <div class="header">
            <img :src="basicUserInfo.image" alt="" />
            <div class="info">
              <p>{{ basicUserInfo.name }}</p>
              <el-tooltip
                :content="message"
                placement="top"
                effect="light"
                :disabled="tooltipDisable"
              >
                <p @mouseenter="isShowTooltip" class="textContent">
                  <span>部门：</span>{{ basicUserInfo.groupName }}
                </p>
              </el-tooltip>
            </div>
          </div>
          <div class="addState none">
            <img :src="require('@/assets/images/pad/message.png')" alt="" />
            <p>添加工作状态</p>
          </div>
          <ul>
            <li class="items none">
              <div class="left">
                <img :src="require('@/assets/images/pad/tips.png')" alt="" />
                <span>消息</span>
              </div>
              <p>29</p>
            </li>
            <li class="items" @click="toGoodBadComment">
              <div class="left">
                <img :src="require('@/assets/images/pad/praise.png')" alt="" />
                <span>好差评</span>
              </div>
              <p>{{ evalScore }}</p>
            </li>
            <li class="items" @click="openPersonalCenter">
              <div class="left">
                <img :src="require('@/assets/images/pad/setting.png')" alt="" />
                <span>个人设置</span>
              </div>
            </li>
            <li class="items" @click="openScan">
              <div class="left">
                <img :src="require('@/assets/images/pad/setting.png')" alt="" />
                <span>扫码办理</span>
              </div>
            </li>
            <li class="items" @click="openScanCode">
              <div class="left">
                <img :src="require('@/assets/images/pad/setting.png')" alt="" />
                <span>扫描</span>
              </div>
            </li>
            <li class="items" @click="logout">
              <div class="left">
                <img :src="require('@/assets/images/pad/logout.png')" alt="" />
                <span>退出账号</span>
              </div>
            </li>
          </ul>
        </div>
        <div class="user" slot="reference">
          <img :src="basicUserInfo.image" alt="" />
          <p>{{ basicUserInfo.name }}</p>
        </div>
      </el-popover>

      <div class="catalogue">
        <div
          class="item-catalogue"
          @click="changeCatalogue('padAssistantHome')"
          :class="
            activeCatalogue === 'padAssistantHome' ? 'active-catalogue' : ''
          "
        >
          <img
            :src="
              activeCatalogue === 'padAssistantHome'? require('@/assets/images/pad/homeActive.png'):require('@/assets/images/pad/home.png')"
            alt=""
          />
          <p>首页</p>
        </div>
        <div
          class="item-catalogue"
          @click="changeCatalogue('processService')"
          :class="
            activeCatalogue === 'processService' ? 'active-catalogue' : ''
          "
        >
          <img
            :src=" activeCatalogue === 'processService'? require('@/assets/images/pad/serviceActive.png'):require('@/assets/images/pad/service.png')"
            alt=""
          />
          <p>快捷帮办</p>
        </div>
        <div
          class="item-catalogue"
          @click="changeCatalogue('search')"
          :class="activeCatalogue === 'search' ? 'active-catalogue' : ''"
        >
          <img
            :src="activeCatalogue === 'search' ? require('@/assets/images/pad/searchActive.png'):require('@/assets/images/pad/search.png')"
            alt=""
          />
          <p>知识库</p>
        </div>
        <!-- <div
          class="item-catalogue"
          @click="changeCatalogue('intelligentQA')"
          :class="activeCatalogue === 'intelligentQA' ? 'active-catalogue' : ''"
        >
          <img
            :src="
              activeCatalogue === 'intelligentQA'? require('@/assets/images/pad/qaActive.png'):require('@/assets/images/pad/qa.png')"
            alt=""
          />
          <p>问答</p>
        </div> -->
        <div
          class="item-catalogue"
          @click="changeCatalogue('policy')"
          :class="activeCatalogue === 'policy' ? 'active-catalogue' : ''"
        >
          <img
            :src="
              activeCatalogue === 'policy'? require('@/assets/images/pad/qaActive.png'):require('@/assets/images/pad/qa.png')"
            alt=""
          />
          <p>政策库</p>
        </div>
        <!-- <div
          class="item-catalogue"
          @click="changeCatalogue('test')"
          :class="activeCatalogue === 'test' ? 'active-catalogue' : ''"
        >
          <img
            :src="
              activeCatalogue === 'test'? require('@/assets/images/pad/qaActive.png'):require('@/assets/images/pad/qa.png')"
            alt=""
          />
          <p>外链</p>
        </div> -->
      </div>
      <!-- <el-popover placement="right" trigger="click">
        <div class="popover-content">
          <div class="seekContent">
            <div class="seekOperate">
              <img :src="require('@/assets/images/pad/voiceHelp.png')" alt="" />
              <p>语音求助</p>
            </div>
            <div class="seekOperate">
              <img :src="require('@/assets/images/pad/videoHelp.png')" alt="" />
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
    <div
      class="right-content"
      :class="rightFullScreen ? 'right-fullScreen' : 'right-nofullScreen'"
    >
      <div class="right-container">
        <div class="righTop" :class="{'rightMessage':haveTurnService}" @click="toDeailTurnService">
          <img :src="require('@/assets/images/pad/messageTips.png')" alt="" />
        </div>
        <div class="leftBtn" @click="swiperleft">
          <img :src="require('@/assets/images/pad/leftBtnIcon.png')" alt="" />
        </div>
        <div class="body-content">
          <transition name="fade-transform" mode="out-in">
            <keep-alive>
              <component
                ref="operationFlow"
                :key="new Date().getTime()"
                :serviceUserInfo="serviceUserInfo"
                :is="activeCatalogue"
                @changeCatalogue="changeCatalogue"
                @setComponentName="setComponentName"
                :specificMatters="specificMatters"
                :stepSwitch="stepSwitch"
                @openDocution="openDocution"
                @openBusinessType="openBusinessType"
              >
              </component>
            </keep-alive>
          </transition>
        </div>
        <div class="right-banner">
          <v-touch
            class="userLogo animate"
            v-if="newService"
            @pan="toAcceptUser"
            :class="newService ? 'slideInRight' : 'slideOutRight'"
            :style="{ 'animation-duration': 1000 + 'ms' }"
          >
            <img
              :src="require('@/assets/images/pad/receptionUsers1.png')"
              alt=""
            />
          </v-touch>
          <div
            class="acceptUser animate"
            :class="toAccept ? 'slideInRight' : 'slideOutRight'"
            :style="{ 'animation-duration': 1000 + 'ms' }"
            v-if="toAccept"
          >
            <div class="accept" @click="acceptService"></div>
            <div class="refuse" @click="refuseService"></div>
          </div>
        </div>
      </div>
    </div>
    <div class="video-coupon video-pad" v-if="isVideo">
          <meeting @openRoom="openRoom" @openBusinessType="openBusinessType" @finishVideo="finishVideo"></meeting>
    </div>
    <!--组长或委办局老师查询是否有等待的视频咨询 -->
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
    <el-dialog
      title="服务转派审核"
      :visible.sync="serviceTransfer"
      width="80%"
      custom-class="serviceTransfer"
      v-dialogDrag
    >
      <transferService v-if="serviceTransfer" :turnRecordId="turnRecordId" />
    </el-dialog>
    <el-dialog
      title="个人中心"
      :visible.sync="showPersonalCenter"
      width="80%"
      v-dialogDrag
      append-to-body
    >
      <personalCenter
        v-if="showPersonalCenter"
        @closePersonalCenter="closePersonalCenter"
      />
    </el-dialog>
    <el-dialog
      v-dialog-drag
      title="扫码"
      :visible.sync="eleShow"
      custom-class="preview-dialog"
      width="600px"
      height="400px"
      append-to-body
    >
      <div class="codeWrap">
        <img :src="imgUrl" alt="二维码" />
      </div>
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
    <!-- 选择部门 -->
    <el-dialog
      :title=" groupType == '2'?'中心':'委办局' "
      :visible.sync="videoShow"
      width="60%"
      class="window-number"
      custom-class="serviceTransfer"
      v-dialogDrag
    >
      <chooseDepart :isNeedOpenVideo="isNeedOpenVideo"  @openVideo="openVideo" v-if="videoShow" :groupType="groupType" :type="type" @closeDepart="closeDepart" />
    </el-dialog>
    <!-- 选择组长或委办局 -->
    <el-dialog
      v-dialog-drag
      title="选择中心或委办局"
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
import serviceNewGuide from "./components/process/processComponents/serviceNewGuide.vue";
import padAssistantHome from "./components/padAssistantHome.vue";
import processService from "./components/process/index.vue";
import search from "./components/search/index.vue";
import policy from "./components/policy.vue";
import intelligentQA from "./components/intelligentQA.vue";
import transferService from "./components/transferService.vue";
import personalCenter from "./components/personalCenter.vue";
import { isHaveNewService, nextService,saveNameAndCard,isHaveNewCall,
  joinRoom,takeNumHelpInfo } from "@/api/modules/helpAgent";
import { getWorkUserEvalScore,scanCertQrCode } from "@/api/modules/evaluate.js";
import { formatDate } from "@/utils/index";
import QRCode from "qrcode";
import chooseDepart from "../../meeting/chooseDepart.vue";
import meeting from '../../meeting/Meeting.vue';
import videoOperate from "@/utils/video";
export default {
  name: "assistant",
  components: { policy,search,padAssistantHome, processService, intelligentQA,personalCenter,serviceNewGuide,transferService ,chooseDepart,meeting},
  data() {
    return {
      tooltipDisable: false,
      toAccept: false,
      message: "省委常委",
      currentUser: "张磊",
      userList: [
        { name: "张磊" },
        { name: "王青" },
        { name: "刘涛" },
        { name: "张云雷" },
      ],
      activeCatalogue: "padAssistantHome",
      specificMatters: {},
      rightFullScreen: false,
      newService: false,
      messageList: [],
      serviceUserInfo: {},
      showPersonalCenter:false,
      toProcess:'',
      haveTurnService: false, //是否有转派服务等待处理，1-是，2-否
      essentialinformation: {}, //转派服务详情
      turnRecordId: "", //转派记录编号
      serviceTransfer: false,
      evalScore:5,
      visible:false,
      stepSwitch:0,
      eleShow:false,
      imgUrl:'',
      showAddDocument:false,
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
      showSelectBusinessType:false,
      isNeedOpenVideo:true,  //是否需要打开视频 
      roomOpen:false,  //视频房间是否打开
      departOuter:false,  //是否中心外人员页面显示设置 
    };
  },
  mixins:[videoOperate],
  computed: {
    key() {
      return this.$route.path;
    },
    outLink() {
      if (this.$route.meta.outLink) {
        return this.$route.meta.outLink;
      }
      return "";
    },
    basicUserInfo() {
      return this.$store.state.user.basicUserInfo;
    },
    baseUserInfo(){
      return this.$store.state.pageData.baseUserInfo;
    }
  },
  mounted() {
    console.log(this.baseUserInfo);
    if (this.basicUserInfo.userType === "2") {
      setInterval(() => {
        this.getMessage();
      }, 5000);
    }

    // 好差评分
    getWorkUserEvalScore().then(res=>{
      if(res.data.evalScore){
        this.evalScore = res.data.evalScore;
      }
    })
    let that = this;
    // 中心或委办局老师查询是否有等待的视频咨询
    let hasVideo = setInterval(function(){
      that.toVideoList();
    },5000)
  },
  methods: {
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
          that.clearVideoData(); //清空视频数据
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
      // if(baseUserInfo){
      //   this.baseUserInfo = baseUserInfo;
      // }
      if(needOpenVideo){
        this.isNeedOpenVideo = false;  //在视频打开下，呼叫组长或委办局不需要再次打开视频
      }else{
        this.isNeedOpenVideo = true;
      }
    },
    //打开 2组长 3委办局
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
        // that.roomOpen = false;
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
      this.groupType = type; //2中心   3委办局
      // this.baseUserInfo =this.baseUserInfo;
    },
    closeDepart(){
      this.videoShow = false;
    },
    //打开视频咨询
    openVideo(){
      this.isVideo = true;
      // this.roomOpen = false;
    },
    // 打开房间 视觉
    openRoom(){
      // this.roomOpen = true;
    },
    //关闭视频咨询
    finishVideo(){
      this.isVideo = false;
    },
    openDocution(){
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
    // 智能问答获取办理事项
    setComponentName(componentName, data) {
      // this.showIntelligentQA = false;
      this.serviceType = "1";
      this.activeCatalogue = componentName;
      const obj = {
        serviceOid: data.serviceOid,
        serviceName: data.name,
      };
      this.specificMatters = obj;
    },
    closePersonalCenter() {
      this.showPersonalCenter = false;
    },
    //个人中心
    openPersonalCenter() {
      this.showPersonalCenter = true;
      this.visible = false;
    },
    async openScan(){
      this.visible = false;
      this.imgUrl = await QRCode.toDataURL('https://onlineserve.shhuangpu.gov.cn/bangban/getNumber?workUserIds=' +this.basicUserInfo.id + '&groupId=' + this.basicUserInfo.groupId);
      this.eleShow = true;
    },
    openScanCode(){
      let _this = this;
      android.scanCode();
      let timer = setInterval(function(){
        if(resultQrcode){
          clearInterval(timer);
          timer = null;
          _this.getUserData(resultQrcode);
          // alert(resultQrcode)
        }
      },3000)
    },
      //调用万达方法 通过二维码获取用户信息
    getUserData: function (resultQrcode) {
      const urlPro = 'http://172.21.178.38:8088/case-service';
      let _this = this;
      let param = {
        pos: "黄浦区行政服务中心",
        use: "自助打印机",
        qrcode: resultQrcode
      }
      // let url = interUrlPro + '/zzk/inter/scanCertQrCode.do';
      let url = urlPro + '/ha/outer/scanCertQrCode';
      // alert(JSON.stringify(param));
      $.ajax({
        type: 'post',
        dataType: 'json',
        url: url,
        data: qs.stringify(param),
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',  //解决跨域问题
        },
        success: function (res) {
         
          // console.log(res);
          if (res.code === 200) {
            // alert("调用万达方法 --------------" + JSON.stringify(res));
            //扫码取号
            let userInfo = {
              name:res.data.xm,
              phone:res.data.mobile,
              cardType: "1",
              cardNo:res.data.zjhm,
              companyName:'',
              companyCode:'',
              workUserIds:_this.basicUserInfo.id,
              takeNumberType:'1',
              groupId:_this.basicUserInfo.groupId,
              haType:'1'
            };
            takeNumHelpInfo(userInfo).then(res=>{
                if(res.code == 200){
                  _this.$message.success("取号成功！");
                  _this.visible = false;
                }
            })
          //  alert("随申办信息"+JSON.stringify(res.data));

          } else {
            alert(res.message);
            // _this.errorMessage = "扫描随身办获取用户信息错误:" + res.message;
          }
        },
        error: function (error) {
          // alert(JSON.stringify(error));
          alert(error);
          // this.showScanQRCode = false;
          // _this.showError = true;
          // _this.errorMessage = "扫描随身办获取用户信息错误:" + JSON.stringify(error);
          // // alert(res.message)
        }
      })
    },
    toAcceptUser() {
      this.toAccept = true;
      this.newService = false;
    },
    acceptService() {
      nextService().then((res) => {
        this.toAccept = false;
        this.$store.commit("setServiceOperateStatus", true);
        if (res.code === 200) {
          if (res.data) {
            this.newService = false;
            res.data.id = res.data.queueId;
            this.documentInfo.queueId = res.data.queueId;
            if (!res.data.firstServiceBeginTime) {
              res.data.firstServiceBeginTime = formatDate(new Date());
            }

            if(res.data.takeNumberType == '4'){
              this.showAddDocument = true;
            }
            if(res.data.haVideoAccess){
              localStorage.setItem("accessId",res.data.haVideoAccess.id);
            }
            this.$store.commit("setBaseUserInfo", res.data);
            localStorage.setItem('baseUserInfo',JSON.stringify(res.data));
            this.serviceUserInfo = res.data;
            this.$store.commit("setServiceOperateStatus", true);
          } else {
            this.$message.warning("暂无可接待人员");
          }
        }
      });
      this.setComponentProcess("processService");
    },
    refuseService() {
      this.toAccept = false;
      this.newService = true;
    },

    getMessage() {
      const that = this;
      isHaveNewService().then((res) => {
        if (res.code === 200) {
          if (res.data.messesService.haveNewMesses === "1" && !this.toAccept) {
            that.newService = true;
            this.$store.commit("setServiceOperateStatus", true);
          } else {
            that.newService = false;
          }
          if (res.data.turnService.haveTurnService === "1") {
            that.haveTurnService = true;
            that.turnRecordId = res.data.turnService.turnRecordId;
            that.getMessesByTurn();
          } else {
            that.haveTurnService = false;
          }
        }
      });
    },
    //获取转派服务信息
    getMessesByTurn() {
      getMessesByTurn({ turnRecordId: this.turnRecordId }).then((res) => {
        if (res.code === 200) {
          this.messageList = [];
          this.essentialinformation = res.data;
          const obj = {
            message: `${this.essentialinformation.oldServiceWorkUserName}转派一个帮办服务给你`,
            type: "turnService",
            turnRecordId: this.essentialinformation.turnRecordId,
          };
          this.messageList.push(obj);
          this.$nextTick(() => {
            this.messageList = [...this.messageList];
          });
        }
      });
    },
    toDeailTurnService() {
      this.serviceTransfer = true;
      this.turnRecordId = this.turnRecordId;
      this.$store.commit("setAcceptService", false);
    },
    swiperleft() {
      this.rightFullScreen = !this.rightFullScreen;
    },
    isShowTooltip(e) {
      let clientWidth = e.target.clientWidth,
        scrollWidth = e.target.scrollWidth;
      if (scrollWidth >= clientWidth) {
        this.tooltipDisable = false;
      } else {
        this.tooltipDisable = true;
      }
    },
    changeCatalogue(type) {
      this.activeCatalogue = type;
      if(type == 'processService'){
        this.stepSwitch = Math.random();
      }
    },
    setComponentProcess(data) {
      this.activeCatalogue = data;
    },
    changeUser(data) {
      this.currentUser = data.name;
    },
    async logout() {
      this.$confirm("确定注销并退出系统吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        this.$store.dispatch("LogOut").then(() => {
          let flag = navigator.userAgent.match(
            /(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i
          );
          location.href = "padLogin";
        });
      });
    },
    toGoodBadComment() {
      this.$router.push({
        name: "goodBadComment",
      });
    },
  },
  watch:{
    baseUserInfo:{
      handler(val){
        localStorage.setItem('baseUserInfo', JSON.stringify(val));
      }
    }
  }
};

function onScanCodeResult(qrCode){
	alert(qrCode)
	console.log("扫码结果：" + qrCode)
}
</script>
<style lang="scss" scoped>
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
          &:nth-child(1),&:nth-child(2)  {
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
.codeWrap{
    width: 100%;
    display: flex;
    justify-content: center;
    margin-bottom: 20px;
    img{
      width: 300px;
    }
  }
.container {
  width: 100%;
  height: 100%;
  background: linear-gradient(
    90deg,
    RGBA(217, 228, 240, 1) 0%,
    RGBA(235, 246, 249, 1) 100%
  );
  position: relative;
  overflow: hidden;
  .righTop{
    position: fixed;
    z-index: 9999;
    left:50%;
    img{
      width: 2.3529rem;
    }
  }
  .rightMessage{
    top: 15px;
    animation: toShow 1s  infinite;
  }
  @keyframes toShow{
    0%   {transform: scale(1);}
    50%  {transform: scale(2.5);}
    100% {transform: scale(1);}
  }
  ul {
    padding: 0;
    margin: 0;

    li {
      list-style: none;
      padding: 0;
      margin: 0;
    }
  }

  .left-content {
    position: absolute;
    left: 0;
    top: 0;
    width: 8.25rem;
    padding-right: 2rem;
    height: 100%;
    background: #fff;
    box-shadow: 0px 0px 1.25rem 0px rgba(84, 85, 85, 0.1);
    padding-top: 3.5625rem;
    display: flex;
    flex-direction: column;
    align-items: center;

    .user {
      width: 100%;
      text-align: center;

      img {
        width: 5.2687rem;
        border-radius: 50%;
      }

      p {
        font-size: 1.375rem;
        font-family: Source Han Sans CN;
        font-weight: 400;
        color: #021a2f;
        margin-top: 0.4375rem;
      }
    }

    .catalogue {
      width: 100%;
      height: auto;
      padding: 5rem 0;

      .item-catalogue {
        width: 100%;
        height: 7.6875rem;
        text-align: center;
        padding-top: 0.5625rem;
        margin-bottom: 2.375rem;

        img {
          width: 3.6875rem;
        }

        p {
          margin-top: 0.625rem;
          font-size: 1.2rem;
          font-family: Source Han Sans CN;
          font-weight: 400;
          color: #61666b;
        }
      }

      .active-catalogue {
        // background: linear-gradient(90deg, #fbe8c2 0%, #fff 100%);
        background: rgba(255, 255, 255, 0.61);
        box-shadow: -1px 0px 10px 0px rgba(104, 144, 231, 0.66);

        p {
          color: #5093e4;
        }
      }
    }

    .seekHelp {
      width: 100%;
      height: 7.0625rem;
      text-align: center;
      padding-top: 0.5625rem;
      margin-bottom: 2.375rem;

      img {
        width: 3.6875rem;
      }

      p {
        font-size: 1.25rem;
        font-family: Source Han Sans CN;
        font-weight: 400;
        color: #021a2f;
      }
    }
  }

  .right-content {
    width: 100%;
    height: 100%;
    .right-container {
      width: 100%;
      height: 100%;
      position: relative;
      .leftBtn {
        width: 1.8824rem;
        height: 29.4118rem;
        position: absolute;
        top: calc(50% - 14.7059rem);
        left: 0;
        z-index: 99999;
        display: flex;
        align-items: center;
        justify-content: flex-start;
        img {
          width: 1.7059rem;
          height: 3.4118rem;
        }
      }
      .body-content {
        width: 100%;
        height: 100%;
        position: absolute;
        left: 0;
        top: 0;
        // z-index: 1000;
      }
      .right-banner {
        position: absolute;
        bottom: 43rem;
        right: -1px;
        z-index: 9999;
        height: 5.2353rem;
        width: auto;
        .userLogo {
          width: 7.8824rem;
          padding-left: 1rem;
          height: 5.2353rem;
          background: linear-gradient(0deg, #d6dfec 0%, #ffffff 100%);
          box-shadow: 0px 0.2941rem 0.3529rem 0px rgba(10, 86, 157, 0.22);
          border-radius: 3.1176rem 0 0 3.1176rem;
          display: flex;
          align-items: center;
          justify-content: flex-start;
          img {
            width: 4.2353rem;
          }
        }
        .acceptUser {
          width: 21.4706rem;
          height: 4.9412rem;
          background: url("@/assets/images/pad/acceptNext.png") no-repeat;
          background-size: 100% 100%;
          position: relative;
          .accept {
            position: absolute;
            right: 0;
            top: 0;
            width: 9rem;
            height: 4.2353rem;
            background: url("@/assets/images/pad/accept.png") no-repeat;
            background-size: 100% 100%;
          }
          .refuse {
            position: absolute;
            right: 0;
            top: 0;
            width: 4.1765rem;
            height: 4.2353rem;
            background: url("@/assets/images/pad/×.png") no-repeat;
            background-size: 100% 100%;
          }
        }
      }
    }
  }
  .right-nofullScreen {
    width: calc(100% - 6.25rem);
    height: 100%;
    position: absolute;
    left: 6.25rem;
    top: 0;
    // z-index: 1000;
    border-radius: 2.1176rem 0px 0px 2.1176rem;
    border: 0.1765rem solid;
    border-image: linear-gradient(-90deg, #bedaf8, #ffffff) 10 10;
    background: linear-gradient(-39deg, #bdd9f8 0%, #cde0f4 100%);
    animation-name: nofullScreen;
    animation-duration: 1s;
  }
  .right-fullScreen {
    width: 100%;
    height: 100%;
    position: absolute;
    left: 0;
    top: 0;
    // z-index: 1000;
    border: 0.1765rem solid;
    border-radius: 0;
    border-image: linear-gradient(-90deg, #bedaf8, #ffffff) 10 10;
    background: linear-gradient(-39deg, #bdd9f8 0%, #cde0f4 100%);
    animation-name: fullScreen;
    animation-duration: 1s;
  }
  @keyframes fullScreen {
    0% {
      left: 6.25rem;
      width: calc(100% - 6.25rem);
      border-radius: 2.1176rem 0px 0px 2.1176rem;
    }
    100% {
      left: 0;
      width: 100%;
      border-radius: 0;
    }
  }
  @keyframes nofullScreen {
    0% {
      left: 0;
      width: 100%;
      border-radius: 0;
    }
    100% {
      left: 6.25rem;
      width: calc(100% - 6.25rem);
      border-radius: 2.1176rem 0px 0px 2.1176rem;
    }
  }
}

p {
  padding: 0;
  margin: 0;
}
</style>
<style lang="scss">
.serviceTransfer {
  height: 80vh;
  z-index: 9999999 !important;
  .el-dialog__body {
    height: calc(100% - 3.75rem);
    max-height: 100% !important;
  }
}
.el-popover {
  .popover-content {
    .header {
      width: 23.75rem;
      height: 5.3125rem;
      display: flex;
      align-items: center;
      justify-content: flex-start;

      img {
        width: 5.3125rem;
        border-radius: 50%;
      }

      .info {
        flex: 1;

        p {
          &:nth-child(1) {
            font-size: 1.625rem;
            font-family: Source Han Sans CN;
            font-weight: 500;
            color: #2f2f2f;
          }

          &:nth-child(2) {
            font-size: 1.25rem;
            font-family: Source Han Sans CN;
            font-weight: 400;
            color: #727274;
            max-width: 14.375rem;
            overflow: hidden;
            text-overflow: ellipsis;
            -o-text-overflow: ellipsis;
            -webkit-text-overflow: ellipsis;
            -moz-text-overflow: ellipsis;
            white-space: nowrap;
          }
        }
      }
    }

    .addState {
      width: 23.75rem;
      height: 3.75rem;
      display: flex;
      align-items: center;
      justify-content: flex-start;

      img {
        width: 2.5rem;
        margin-right: 0.9375rem;
      }

      p {
        font-size: 1.375rem;
        font-family: Source Han Sans CN;
        font-weight: 400;
        color: #727274;
      }
    }

    ul {
      width: 100%;
      padding: 0;
      margin: 0;

      .items {
        list-style: none;
        padding: 0;
        margin: 0;
        width: 100%;
        height: 5.625rem;
        display: flex;
        align-items: center;
        justify-content: space-between;
        box-sizing: content-box;
        border-bottom: 1px solid #efefef;

        .left {
          width: 70%;
          height: 100%;
          display: flex;
          align-items: center;
          justify-content: flex-start;

          img {
            width: 2.1875rem;
            margin-right: 1.9375rem;
          }

          span {
            font-size: 1.625rem;
            font-family: Source Han Sans CN;
            font-weight: 500;
            color: #2f2f2f;
          }
        }

        p {
          width: 4.8125rem;
          height: 2.625rem;
          background: rgba(160, 160, 160, 0.12);
          border-radius: 1.3125rem;
          font-size: 1.75rem;
          font-family: DIN;
          font-weight: 500;
          text-align: center;
          line-height: 2.625rem;

          &:nth-child(1) {
            color: #ff3f2b;
          }

          &:nth-child(2) {
            color: #ff8400;
          }
        }
      }
    }

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
        justify-content: center;

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
  }

  .moreDetail {
    width: 10rem;

    p {
      height: 3.75rem;
      width: 10rem;
      display: flex;
      align-items: center;
      justify-content: flex-start;
      font-size: 1.625rem;
      font-family: Source Han Sans CN;
      font-weight: 500;
      color: #2f2f2f;
      cursor: pointer;

      img {
        width: 2.1875rem;
        margin-right: 1.125rem;
      }

      &:nth-child(2) {
        margin: 1.25rem 0;
      }
    }
  }
}
</style>
<style lang="scss">
  .video-pad{
    .chat-box{
      height: 300px;
    }
    .chat-box .header > div > span[data-v-58c1610a]:nth-child(1){
      width: 60px;
    }
    .chat-box .header > div > span[data-v-58c1610a]:nth-child(2){
      width: calc(100% - 65px);
    }
    .white-footer{
      display:block;
      text-align:right;
      &>div:nth-child(1){
        display: none;
      }
      &>div{
        display: inline-block;
      }
    }
    .router-meeting .video-container .document-manage{
      right:400px
    }
    .video-board{
      width: auto;
      left: 60px;
    }
    .document-manage .button{
      min-width: auto;
    }
    .maxVideo{
      top: 40px !important;
      left: 60px !important;
      width: calc(100% - 360px) !important;
      height: calc(100% - 50px) !important;
      z-index: 2 !important;
    }
    .sub-video .amplify,.main-video .amplify{
      width:10px;
    }
    .document-manage,.painter-tools-container{
      transform: scale(0.7);
    }
  }
</style>
