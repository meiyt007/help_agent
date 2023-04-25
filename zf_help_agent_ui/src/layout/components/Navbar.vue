<template>
  <div class="navbar1">
    <!-- <hamburger id="hamburger-container" :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" /> -->
    <!-- <search id="header-search" class="right-menu-item show" /> -->
    <!-- 办理信息 -->
    <div class="handle-content">
      <div class="handle-item">
        <img src="../../assets/image/blsj-icon.png" />办理时间:
        <!--<span class="orange">{{ currentPerson.time }}</span>-->
        <span class="orange">{{ currentPersonTime }}</span>
      </div>
      <div class="handle-item">
        <img src="../../assets/image/gh-icon.png" />当前号:
        <!--<span class="grey">{{ currentPerson.currentNum }}</span>-->
        <span class="grey">{{ currentPersonNum }}</span>
      </div>
      <div class="handle-item">
        <img src="../../assets/image/wait-icon.png" />等待人数:
        <span class="grey">{{ result }}</span>
      </div>
    </div>
    <!-- 按钮组 -->
    <div class="handle-btn-group">
      <el-button type="primary" :disabled="this.isCanClick" icon="iconfont zfsoft-zhonghu" @click="recall">重呼</el-button>
      <el-button type="primary" :disabled="this.isCanClick" @click="callingNextPerson" class="next-btn">
        <span class="next-btn--content">
          <i class="iconfont zfsoft-xiayiwei" />
          下一位
        </span>
      </el-button>
      <el-button type="primary" icon="iconfont zfsoft-banli" @click="handleWorking" v-if="flag">办理</el-button>
      <el-button type="warning" icon="iconfont zfsoft-xuanzhong" @click="finishHandle" class="finish-btn" v-else>完成</el-button>
      <el-button type="primary" :disabled="this.isCanClick" icon="iconfont zfsoft-zanting1" @click="pauseCalling">暂停</el-button>
      <!--      <el-dropdown trigger="click">
        <el-button
          type="primary"
          :disabled="this.isCanClick"
          icon="iconfont zfsoft-tehu"
          @click="specialCalling"
        >
          特呼
          <i class="el-icon-arrow-down el-icon&#45;&#45;right" />
        </el-button>
        <el-dropdown-menu slot="dropdown">
          <template v-if="thoptions.length > 0">
            <el-dropdown-item
              v-for="(item, index) in thoptions"
              :key="index"
              @click.native="handleCommand(item)"
            >
              {{ item.name }}
            </el-dropdown-item>
          </template>
          <el-dropdown-item v-else> 暂无号码 </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>-->
      <el-button type="primary" :disabled="this.isCanClick" icon="iconfont zfsoft-tehu" @click="specialCalling">特呼</el-button>
    </div>

    <div class="right-menu-basic">
      <template v-if="device !== 'mobile'">
        <el-tooltip content="消息" effect="dark" placement="bottom" style="width: 50px">
          <zfsoft-message id="read-message" class="right-menu-item hover-effect" />
        </el-tooltip>
        <el-tooltip content="全屏" effect="dark" placement="bottom">
          <screenfull id="screenfull" class="right-menu-item hover-effect" />
        </el-tooltip>
        <ThemePicker :initTheme="initTheme"></ThemePicker>
      </template>

      <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
        <div class="avatar-wrapper">
          <img :src="avatar" class="user-avatar" />
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown">
          <!-- 模块菜单暂时放置 -->
          <div v-for="apps in appList" :key="apps.name">
            <div
              :title="app.name"
              :key="app.oid"
              v-for="app in apps"
              :class="[
                { 'moudle-item': app_oid != app.oid },
                { 'moudle-item moudle-item-active': app_oid == app.oid },
              ]"
              @click="changeApp(app.oid)"
            >{{ app.name }}</div>
          </div>
          <!-- 模块菜单暂时放置 -->
          <el-dropdown-item divided>
            <router-link to="/user/profile">个人中心</router-link>
          </el-dropdown-item>
          <el-dropdown-item @click.native="setting = true">
            <span>布局设置</span>
          </el-dropdown-item>
          <el-dropdown-item divided @click.native="logout">
            <span>退出登录</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>

    <el-dialog
      title="请输入完整的排队号码"
      v-dialog-drag
      :visible.sync="isSpecialCalling"
      v-if="isSpecialCalling"
      width="350px"
      height="200px"
      backgroud-color="white"
      append-to-body
    >
      <template>
        <div style="margin-top: 10px;height: 38px;outline: none;">
          <el-input v-model="specialCallingNo"></el-input>
        </div>
        <div style="text-align: center;margin-top: 35px">
          <el-button @click="StartSpecialCalling()" type="primary">确 定</el-button>
          <el-button @click="isSpecialCalling = false">关 闭</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog
      title="暂停服务"
      v-dialog-drag
      :visible.sync="isStopService"
      v-if="isStopService"
      width="550px"
      height="400px"
      backgroud-color="white"
      :before-close="closeStopService"
      append-to-body
    >
      <template>
        <div style="text-align:center">
          <img src="@/assets/image/stopService.png" />
          <span style="text-align: center;display:block;margin-top:20px">暂停服务中</span>
        </div>
      </template>
      <div style="text-align: center;margin-top: 35px">
        <el-button @click="closeStopService()">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters, mapMutations } from "vuex";
import Breadcrumb from "@/components/Breadcrumb";
import Hamburger from "@/components/Hamburger";
import Screenfull from "@/components/Screenfull";
import SizeSelect from "@/components/SizeSelect";
import Search from "@/components/HeaderSearch";
import ZfsoftDoc from "@/components/zfsoft/doc";
import ZfsoftMessage from "@/components/zfsoft/message";
import store from "../../store";
import router from "../../router";
import ThemePicker from "@/components/ThemePicker/MainIndex";
import { getLoginUrl } from "@/utils/auth";
import { handleCallNumber, completeCallNumber, skipCallNumber, selectCallNums } from "@/api/zc/qhjh/qhjh";
import {
  getloginUser,
  queryQlCaseByCaseOid,
  queryQlCaseApplayByCaseOid
} from "@/api/zc/businessManagement/windowAcceptance";
import { showCallMessage } from "@/api/zc/businessManagement/doubleScreenInteraction";
import Utils from "@/assets/js/util.js";
import { getConfigByCode } from "@/api/sys/config";
import {
  getHandleCamera,
  getCameraImage,
  getEmotionJudgment,
  // stopCameraImage,
  getTotalWaitNum,
  getQhjhWaitNum,
  sendQhJhInfo,
  saveVirtualBusinessRecord,
  SaveCallRecordInterface,
  getEvaluateAndPush,
  //openAudio,
  // closeAudio,
  //uploadAudio
} from "@/api/zc/handleCamera/handleCamera";
import { addAveragePrescription } from "@/api/home/common";
import { resetRouter } from '@/router';
const callingList = [
  { currentNum: "未开始叫号", time: "00: 00: 00", id: 0 },
  { currentNum: "A001", time: "00: 00: 00", id: 1 },
  { currentNum: "A002", time: "00: 00: 00", id: 2 },
  { currentNum: "A003", time: "00: 00: 00", id: 3 },
  { currentNum: "A004", time: "00: 00: 00", id: 4 },
  { currentNum: "A005", time: "00: 00: 00", id: 5 }
];
export default {
  components: {
    Breadcrumb,
    Hamburger,
    Screenfull,
    SizeSelect,
    Search,
    ZfsoftDoc,
    ZfsoftMessage,
    ThemePicker
  },
  data () {
    return {
      //是否暂停服务
      isStopService: false,
      //排队号
      specialCallingNo: '',
      // 是否显示特护
      isSpecialCalling: false,
      //是否打开视像头
      isOpenCamera: false,
      thoptions: [],
      thvalue: '',
      initTheme: "#409EFF", // content of theme-chalk css
      //共享记事本列表
      openSendView: false,
      //后援记录列表
      openHyjlView: false,
      callingList: [...callingList],
      handledCallingList: [], // 已办
      idx: 0,
      timer: null,
      timers: null,
      h: 0,
      s: 0,
      m: 0,
      isHandleWorking: false, // 是否正在办理
      //登录信息
      loginUser: {},
      waitNum: "",
      //照片拍摄数组集合
      cameraList: [],
      flag: true,
      //等待人数
      result: "",
      //当前叫号
      // currentPersonNum: "未开始叫号",
      //下一个叫号
      nextPersonNum: "",
      //当前时间
      currentPersonTime: "00: 00: 00",
      //保存叫号表oid
      //saveCallRecordOid: "",
      //取号表主键
      numberOid: "",
      //是否点击了下一位
      isNext: false,
      //窗口信息
      WindowName: '',
      //是否特护
      isSep: false,
      //点击办理按钮后，重呼、下一位以及特呼按钮，不可以点击。
      isCanClick: false
    };
  },
  computed: {
    ...mapGetters([
      "sidebar",
      "avatar",
      "device",
      "appList",
      "app_oid",
      "currentPersonNum",
      "caseOid",
      "currentVirtualBusinessRecordOid",
      "wgpjFlag",
      "znzxFlag",
      "saveCallRecordOid"
    ]),
    setting: {
      get () {
        return this.$store.state.settings.showSettings;
      },
      set (val) {
        this.$store.dispatch("settings/changeSetting", {
          key: "showSettings",
          value: val
        });
      }
    },

    currentPerson () {
      return this.callingList[this.idx || 0];
    },

    isWindowAcceptance ({ $route: { path } }) {
      return path.includes("windowAcceptance") || path.includes("comboWindowAccept");
    },

    /** 当前路由表 */
    currentRouterEnv ({ appList, app_oid }) {
      return appList.flat().find(item => item.oid === app_oid);
    },

    /** 是否是一件事 */
    isOnethingRouter ({ currentRouterEnv }) {
      return currentRouterEnv?.name?.includes?.('一件事');
    },

    /** 是否是单办事项 */
    isOneCaseRouter ({ currentRouterEnv }) {
      return currentRouterEnv?.name?.includes?.('单办事项');
    },

    /** 是否可以使用情绪识别 */
    isEmotionRecognition ({ $route: { path } }) {
      return path.includes('smartRegistration') || path.includes('comboAccept') || path.includes('windowAcceptance') || path.includes('comboWindowAccept');
    }
  },
  created () {
    this.changeClassName();
    this.queryLoginInfo();
    this.watingPersonNum();
    //刷新页面，关闭摄像头，录音
    /*closeAudio().then(res => {
      console.log("刷新页面，关闭录音");
      console.log(res);
    }).catch(err => {
      console.log(err);
    });
    clearInterval(this.timers);
    stopCameraImage().then(res => {
      console.log("刷新页面，关闭摄像头");
      console.log(res);
    });*/
  },
  beforeDestroy () {
    this.timer && clearInterval(this.timer);
  },
  mounted () {
    /* Utils.$on("stopCamera", msg => {
       this.stopInterval();
       this.$message.success(msg);
     });*/
  },
  methods: {
    closeStopService () {
      this.isStopService = false;
      /*let data = {};
      let url = window.location.origin;
      data.userName = this.loginUser.userName;
      data.organName = this.loginUser.organName;
      data.isWorking = 1;
      let pushUrl =
        url +
        "/serviceHall/suspendedService.html" +
        "?jsonObject=" +
        JSON.stringify(data);
      //process.env.NODE_ENV === 'development' && window.open(pushUrl);
      showCallMessage(pushUrl).then(response => {
        console.log(response);
      });*/
    },
    handleCommand (item) {
      this.$store.commit("SET_CURRENT_PERSON_NUM", item.name);
      this.WindowName = item.windowName;
      this.resetTimer();
      this.isNext = true;
      this.isSep = true;
      var save = {
        callInfo: this.WindowName,
        callNum: this.currentPersonNum,
        caseUserName: "沈博文",
        cardNo: "310112199305245610"
      }
      SaveCallRecordInterface(save).then(re => {
        if (re.code == 200) {
          //this.saveCallRecordOid = re.data;
          this.$store.commit("SET_SAVE_CALL_RECORD_OID", re.data);
          this.showCall();
        }
      });
    },
    /** 登录信息 */
    queryLoginInfo () {
      getloginUser().then(response => {
        this.loginUser = response.data;
        //this.openWelcome();
      });
    },
    watingPersonNum () {
      var data = {
        userOid: "111"
      };
      getTotalWaitNum(data).then(response => {
        //this.result = response.data;
        if (response.data.success) {
          this.result = response.data.waitNum;
        } else {
          this.result = 0;
          this.$message.warning(response.data.msg);
        }
      });
    },
    toggleSideBar () {
      this.$store.dispatch("app/toggleSideBar");
    },
    changeClassName () {
      if (
        null != store.getters.skinClassname &&
        "" != store.getters.skinClassname
      ) {
        let val = store.getters.skinClassname;
        this.initTheme = val;
      }
    },
    changeApp (appOid) {
      this.$store.dispatch("setAppOid", appOid);
      //this.$store.dispatch("GenerateRoutes")
      const roles = [];
      store.dispatch("GenerateRoutes").then(accessRoutes => {
        resetRouter();
        router.addRoutes(accessRoutes); // 动态添加可访问路由表
        //next({ ...to, replace: true }) // hack方法 确保addRoutes已完成
      });
      //关闭所有的主页面
      this.$store.dispatch("tagsView/delAllViews").then(({ visitedViews }) => {
        this.$router.push("/");
      });
    },
    async logout () {
      this.$confirm("确定注销并退出系统吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.$store.dispatch("LogOut").then(() => {
          location.href = getLoginUrl();
        });
      });
    },
    //查看共享记事本列表
    handleSendPage () {
      this.openSendView = true;
    },
    //查看后援记录
    handhyjl () {
      this.openHyjlView = true;
    },
    /** -------------------------------------------------- 软呼 ------------------------------------------------- */
    /*showCall () {
      let url = window.location.origin;
      let pushUrl = url + "/serviceHall/index.html";
      let data = {};
      data.waitNum = this.result;
      data.currentPersonNum = this.currentPersonNum;
      data.winndowInfo = this.WindowName;
      pushUrl = pushUrl + "?jsonObject=" + JSON.stringify(data);
      process.env.NODE_ENV === 'development' && window.open(pushUrl);
      setTimeout(() => {
        showCallMessage(pushUrl).then(response => {
          console.log(response);
        });
      }, 500);
    },
    //超级综窗柜台双屏登录打开用户信息
    openWelcome () {
      let url = window.location.origin;
      let data = {};
      data.userName = this.loginUser.userName;
      data.organName = this.loginUser.organName;
      data.isWorking = 1;
      let pushUrl =
        url +
        "/serviceHall/suspendedService.html?jsonObject=" +
        JSON.stringify(data);
      showCallMessage(pushUrl).then(response => {
        console.log(response);
      });
    },*/

    // 呼叫下一位用户
    ...mapMutations(["CHANGE_OPEN"]),
    async callingNextPerson () {
      /** 如果不是在单办事项或者一件事系统中 */
      if (!(this.isOneCaseRouter || this.isOnethingRouter)) return;

      this.znzxFlag && this.CHANGE_OPEN(true);

      // 刷新智能登记列表
      this.$bus.$emit('ON_ACCEPTANCE_LIST_REFRESH');

      this.handledCallingList.push(this.currentPerson);
      if (!this.flag) {
        this.finishHandle();
      }
      //没完成，没办理，不是特护，跳过
      if (this.isNext && !this.isHandleWorking && !this.isSep) {
        //上一次，获取到了号
        if (this.numberOid != null && this.numberOid != '') {
          var skipCall = {};
          skipCall.nOid = this.numberOid;
          await skipCallNumber(skipCall).then(res => {
            console.log(res);
            if (res.data.isSuccess == 1) {
              this.$message.warning(res.data.msg);
              return;
            }
          });
        }
      }
      this.isNext = true;
      this.resetTimer();
      var data = {};
      getQhjhWaitNum(data).then(response => {
        //this.result =   response.data;
        //console.log("test ----- " + response.data);
        var isCallNum = false;
        if (response.data.success) {
          /** 如果是单办 */
          if (this.isOneCaseRouter) {
            this.$router.push({ path: '/businessManagement/smartRegistration' });
          } else if (this.isOnethingRouter) { /** 一件事 */
            this.$router.push({ path: '/comboManager/comboAccept' });
          }
          this.result = response.data.smallScreenWaitNum;
          // this.currentPersonNum = response.data.callNum;
          this.$store.commit("SET_CURRENT_PERSON_NUM", response.data.callNum);
          isCallNum = true;
          this.numberOid = response.data.numberOid;
          //this.nextPersonNum = response.data.callNum;
          this.WindowName = response.data.WindowName;
          this.showCall();
        } else {
          if (response.data.isNoWaitt == '0') {
            this.result = 0;
            this.numberOid = '';
            //startCall: false, // 是否开始叫号，设置为 false
            //this.$store.commit("SET_CURRENT_PERSON_NUM", response.data.data);
            this.$store.commit("SET_CURRENT_PERSON_NUM_FALSE", response.data.data);
          }
        }
        if (isCallNum) {
          var save = {
            callInfo: this.WindowName,
            callNum: this.currentPersonNum,
            caseUserName: "沈博文",
            cardNo: "310112199305245610"
          }
          SaveCallRecordInterface(save).then(re => {
            if (re.code == 200) {
              //this.saveCallRecordOid = re.data;
              this.$store.commit("SET_SAVE_CALL_RECORD_OID", re.data);
            }
          });
        }
      });
      var sendQhJhInfoData = {};
      sendQhJhInfo(sendQhJhInfoData).then(response => {
        //this.result =   response.data;
        console.log("sendQhJhInfo ----- " + response.data.msg);
        //alert(response.data.msg);
      });

      //this.showCall();
    },

    // 重新呼叫
    recall () {
      /* if (this.idx === 0) return this.$message.warning("请先开始叫号");
       if (!this.isHandleWorking) return this.$message.warning("请先点击办理");
       this.currentPersonTime = "00: 00: 00";
       this.resetTimer();
       this.handleWorking();
       this.$message.success("已重新呼叫");*/
      if (!this.isNext) {
        return this.$message.warning("请先叫号");
      }
      if (!this.isSep && (this.numberOid == null || this.numberOid == '')) {
        return this.$message.warning(this.currentPersonNum);
      }
      this.showCall();
    },
    // 暂停
    pauseCalling () {
      this.isStopService = true;
      /*let data = {};
      let url = window.location.origin;
      console.log("pauseCalling");
      console.log(this.loginUser);
      data.userName = this.loginUser.userName;
      data.organName = this.loginUser.organName;
      data.isWorking = 0;
      let pushUrl =
        url +
        "/serviceHall/suspendedService.html" +
        "?jsonObject=" +
        JSON.stringify(data);
      //process.env.NODE_ENV === 'development' && window.open(pushUrl);
      showCallMessage(pushUrl).then(response => {
        console.log(response);
      });*/
    },

    // 办理
    ...mapMutations(["CHANGE_SWITCHONOFF"]),
    async handleWorking () {
      if (!this.isNext) {
        this.$message.warning("请先叫号");
        return;
      }
      if (this.isHandleWorking)
        return this.$message.warning("正在办理中, 请先结束当前办理");
      if (!this.isSep && (this.numberOid == null || this.numberOid == '')) {
        return this.$message.warning(this.currentPersonNum);
      }

      this.flag = false;
      this.isNext = false;
      this.isCanClick = true;
      //this.isSep = false;
      if (!this.isWindowAcceptance) {
        this.CHANGE_OPEN(true);
        this.CHANGE_SWITCHONOFF(true);
      }
      //清空数据，主键
      this.$store.commit("SET_VIRTUAL_BUSINESS_RECORD_OID", '');
      //this.saveCallRecordOid = "";
      //handleOid  办件信息id,可以在修改的时候新增。
      //办理接口
      var handleCall = {
      };
      console.log("handleCall");
      console.log(this.numberOid);
      if (!this.isSep) {
        handleCall.nOid = this.numberOid;
        handleCallNumber(handleCall).then(res => {
          if (res.data.isSuccess == 0) {
            //调用接口成功再去进行办理逻辑

          } else {

          }
        });
      }
      var saveRecord = {
        windowInfo: "测试数据"
      };
      saveVirtualBusinessRecord(saveRecord).then(res => {
        if (res.code == 200) {
          this.$store.commit("SET_VIRTUAL_BUSINESS_RECORD_OID", res.data);
          var save = {
            virtualBusinessOid: this.currentVirtualBusinessRecordOid,
            oid: this.saveCallRecordOid
          }
          SaveCallRecordInterface(save).then(re => {
            if (re.code == 200) {

            }
          });
        }
      });
      /*openAudio().then(res => {
        if (res.StateCode == 0) {
          this.$message.success("录音打开成功！");
        } else if (res.StateCode == -1) {
          this.$message.success("录音已打开！");
        } else if (res.StateCode == -2) {
          this.$message.error("录音打开失败");
        }
      }).catch(err => {
        console.log(err);
      });*/
      var isWgpj = false;
      await getConfigByCode("WGPJ")
        .then(res => {
          if (res.code == 200 && res.data.isAble == 1 && res.data.value == "0") {
            isWgpj = true;
          }
        });
      if (isWgpj == true && this.isEmotionRecognition) {
        //每个5s调用一次拍照接口
        /*await getHandleCamera()
          .then(res => {
/!*            if (res.state === "sucess" || res.state === "faile") {
              if (res.tips != "") {
                this.$message.success(res.tips);
              }
              //每个5s调用一次拍照接口
              this.timers = setInterval(() => {
                this.timeFunction(this.timers);
              }, 5000);
            }*!/
            if (res.StateCode === 0 || res.StateCode === -1) {
              this.isOpenCamera  = true;
              if(res.StateCode === 0){
                this.$message.success("摄像头设备已开启");
              }else if(res.StateCode === -1){
                this.$message.success("摄像头设备已开启");
              }
              //每个5s调用一次拍照接口
              this.timers = setInterval(() => {
                this.timeFunction(this.timers);
              }, 5000);
            }else if(res.StateCode === -2){
              this.$message.success("摄像头设备打开失败");
            }
          })
          .catch(err => {
            console.log(err);
          });*/
      }
      // if (this.idx === 0) return this.$message.warning("请先开始叫号");
      this.isHandleWorking = true;
      // 开始计时
      this.timer = setInterval(() => {
        this.m++;
        if (this.m > 59) {
          this.m = 0;
          this.s++;
        }
        if (this.s > 59) {
          this.s = 0;
          this.h++;
        }
        this.currentPersonTime = `${this.h < 10 ? "0" + this.h : this.h}: ${this.s < 10 ? "0" + this.s : this.s
          }: ${this.m < 10 ? "0" + this.m : this.m}`;
      }, 1000);
    },
    /*timeFunction (timers) {
      setTimeout(() => {
        // 如需要停止定时器，需加入以下：
        getCameraImage()
          .then(res => {
            if (res.state === "sucess") {
              //this.$message.success('每隔5s拍照一次开始拍照');
              let cameraImg = res.Image;
              let images = {};
              images.base64Img = encodeURIComponent(cameraImg);
              //images.currentNum = this.currentPerson.currentNum;
              images.currentNum = this.currentPersonNum;
              images.virtualBusinessOid = this.currentVirtualBusinessRecordOid;
              if (
                (cameraImg != null || cameraImg != "") &&
                this.currentVirtualBusinessRecordOid != ""
              ) {
                getEmotionJudgment(images).then(response => {
                  console.log(response.data);
                });
              } else {
                console.log("拍照失败...");
              }
            }
          })
          .catch(err => {
            console.log(err);
          });
        // clearInterval(timers);
      }, 0);
    },*/
    //停止定时器
    /*stopInterval () {
      clearInterval(this.timers);
      stopCameraImage().then(res => {
/!*        if (res.state == "sucess") {
          if (res.tips != "") {
            this.$message.warning(res.tips);
          }
        }*!/
        if(res.StateCode === 0){
          this.$message.warning("摄像头设备已关闭");
        }else if(res.StateCode === -1){
          this.$message.warning("摄像头设备未打开");
        }
        console.log("关闭摄像头。。。");
      });
    },*/
    // 完成办事
    getThingsDone () {
      if (this.idx === 0)
        return thwatingPerscurrentPerson.timeonNumis.$message.warning(
          "请先开始叫号"
        );
      this.timer && clearInterval(this.timer);
      this.isHandleWorking && this.handledCallingList.push(this.currentPerson);
      this.isHandleWorking = false;
      // 重置watingPersonNum
      //   this.resetTimer();
      //   this.callingList.splice(this.idx, 1);
      //   this.idx = 0;
      this.openWelcome();
    },

    // 特殊呼叫
    specialCalling () {
      //this.$message.success("已为您特殊呼叫用户");
      //this.thoptions =
      selectCallNums().then(res => {
        console.log(res);
        this.thoptions = res.data;
        this.isSpecialCalling = true;
        this.specialCallingNo = "";
      });
    },

    StartSpecialCalling () {
      if (this.thoptions.length == 0) {
        this.$message.warning("暂无号码");
        return;
      }
      //alert(this.specialCallingNo);
      if (this.specialCallingNo == '') {
        this.$message.warning("请输入排队号");
        return;
      }
      for (let i = 0; i < this.thoptions.length; i++) {
        if (this.specialCallingNo == this.thoptions[i].name) {
          //特护
          this.$message.success("特呼成功");
          this.isSpecialCalling = false;
          this.handleCommand(this.thoptions[i]);
          return;
        }
      }
      this.$message.warning("暂无此排队号");
    },

    resetTimer () {
      //alert(this.timer);
      this.timer && clearInterval(this.timer);
      this.h = 0;
      this.m = 0;
      this.s = 0;
      /*      this.currentPerson.time = `${this.h < 10 ? "0" + this.h : this.h}: ${
              this.s < 10 ? "0" + this.s : this.s
            }: ${this.m < 10 ? "0" + this.m : this.m}`;*/
      //this.currentPerson.time = "00: 00: 00";
      this.currentPersonTime = "00: 00: 00";
      this.isHandleWorking = false;
      //alert("resetTimer")
    },
    //完成
    async finishHandle () {
      this.flag = true;
      this.CHANGE_OPEN(false);
      this.CHANGE_SWITCHONOFF(false);
      clearInterval(this.timers);
      clearInterval(this.timer);
      console.log("caseOid");
      console.log(this.caseOid);
      //var  completeCall = false;
      //点击完成的时候，已经办理了
      if (this.isHandleWorking && !this.isSep) {
        var completeCall = {};
        completeCall.nOid = this.numberOid;
        await completeCallNumber(completeCall).then(res => {
          console.log(res);
          if (res.data.isSuccess == 1) {
            this.$message.warning(res.data.msg);
            return;
          }
        });
      }
      this.isNext = false;
      this.isSep = false;
      this.isCanClick = false;
      await closeAudio().then(res => {
        if (res.StateCode == 0) {
          this.$message.warning("录音设备关闭成功");
        } else if (res.StateCode == -1) {
          this.$message.error("录音设备没有打开");
        }
      }).then(() => {
        uploadAudio(this.currentVirtualBusinessRecordOid).then(response => {
          console.log(response)
        }).catch(error => {
          console.log(error)
        })
      }).catch(err => {
        console.log(err);
      });
      // getEvaluateAndPush(this.currentVirtualBusinessRecordOid).then(res => {
      //   console.log(res);
      // })
      this.$store.commit('SET_CASE_OID', "");
      this.$store.commit("SET_VIRTUAL_BUSINESS_RECORD_OID", '');
      //修改保存记录表记录R
      var saveR = {
        oid: this.saveCallRecordOid,
        timeLengthString: this.currentPersonTime
      }
      SaveCallRecordInterface(saveR).then(re => {
        if (re.code == 200) {
          this.$store.commit("SET_SAVE_CALL_RECORD_OID", "");
        }
      });
      // 受理时间
      addAveragePrescription({
        processingTime: this.currentPersonTime.replace(/\s+/g, "")
      });
      this.currentPersonTime = "00: 00: 00";
      this.$store.commit("SET_CURRENT_PERSON_NUM_FALSE", "未开始叫号");
      // 关闭摄像头
      if (this.isOpenCamera == true) {
        //this.stopInterval();
        this.isOpenCamera = false;
      }
    }
  }
};
</script>

<style lang="scss">
.navbar1 {
  display: flex;
  flex-direction: row;
  height: 52px;
  justify-content: space-between;
  overflow: hidden;
  position: relative;
  background: #fff;
  // box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  background: linear-gradient(0deg, #f1f3f7 0%, #e2e7ec 15%, #ffffff 100%);
  border-bottom: 1px solid #c5ccda;

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background 0.3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, 0.025);
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu-basic {
    position: relative;
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 15px;
      height: 100%;
      font-size: 18px;
      color: #99a8b4;
      vertical-align: text-bottom;
      position: relative;
      background: none;
      &:first-child:before {
        position: absolute;
        left: 0;
        top: 0px;
        content: "";
        width: 1px;
        height: 51px;
        background-color: #d8dce5;
      }
      &:last-child:after {
        width: 0;
      }

      &.hover-effect {
        cursor: pointer;
        transition: background 0.3s;

        &:hover {
          // background: rgba(0, 0, 0, 0.025);
          color: #3a9ffd;
        }
      }
    }

    .avatar-container {
      margin-right: 10px;
      padding: 0px 20px 0px 15px;
      .avatar-wrapper {
        margin-top: 10px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 30px;
          height: 30px;
          border-radius: 10px;
          border-radius: 100%;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 10px;
          font-size: 12px;
        }
      }
    }
  }
}
.carousel {
  flex-grow: 1;
  min-width: 500px;
  height: 50px;
  overflow-y: hidden;
  display: inline-block;
  vertical-align: top;
  margin-left: 20px;
}
.carousel-item {
  width: 100px;
  display: inline-block;
  vertical-align: middle;
  text-align: center;
  font-size: 14px;
  color: #838c98;
  margin: 2px 0 0 0;
  padding-bottom: 7px;
  box-sizing: border-box;
  cursor: pointer;
}
.carousel-item:first-child {
  margin-left: 65px;
}
.carousel-item p {
  margin: 5px 0 0 0;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  font-size: 14px;
}
.carousel-item-active {
  border-bottom: 1px solid #304156;
}
.carousel >>> .el-carousel__arrow {
  width: 20px;
  height: 40px;
  border-radius: 5px;
  background-color: #f1f4f8;
  color: #304156;
  opacity: 0.8;
}
.carousel >>> .el-carousel__arrow:hover {
  opacity: 1;
}

.navbar .right-menu-item {
  display: inline-block;
  padding: 0 10px;
  height: 100%;
  font-size: 18px;
  color: #5a5e66;
  vertical-align: text-bottom;
  position: relative;
  line-height: 48px;
}
#screenfull {
  vertical-align: top;
  cursor: pointer;
  position: relative;
  margin-left: -15px;
  &:after {
    position: absolute;
    right: 0px;
    top: 0px;
    content: "";
    width: 1px;
    height: 51px;
    background-color: #d8dce5;
  }
}
.gjx-dropdown .el-dropdown-menu--medium .el-dropdown-menu__item {
  text-align: center;
  font-size: 12px;
  color: #333;
  padding: 0;
  display: inline-block;
  vertical-align: middle;
  text-align: center;
}
.el-dropdown-menu .app-container {
  padding: 0;
}
.el-dropdown-menu .right-menu-item {
  display: inline-block;
  vertical-align: middle;
}
.suitcase-size {
  font-size: 18px;
  padding: 0px 10px;
}

//演示新增样式
.handle-content {
  display: flex;
  height: 51px;
  line-height: 51px;
  .handle-item {
    padding: 0px 15px;
    color: #404c70;
    font-size: 13px;
    border-right: 1px solid #c5ccda;
    img {
      display: inline-block;
      vertical-align: middle;
      margin-right: 5px;
      width: 24px;
      height: 24px;
    }
    .orange {
      color: #ff7007;
      font-weight: bold;
      font-family: dFont;
    }
    .grey {
      color: #5b6b95;
      font-weight: bold;
      font-family: dFont;
    }
  }
}
.handle-btn-group {
  padding-top: 8px;
  .el-button {
    padding: 8px 15px;
    &.next-btn {
      padding: 0;
      height: 32px;
      span {
        padding: 0;
      }
      .next-btn--content {
        padding: 8px 15px;
      }
    }
    span {
      padding-left: 5px;
    }
    &:nth-of-type(1) {
      background: #3d5fb5 !important;
      border: #3d5fb5 !important;
      box-shadow: 0px 3px 7px 0px rgba(61, 95, 181, 0.35);
    }
    &:nth-of-type(2) {
      background: #007ee8 !important;
      border: #007ee8 !important;
      box-shadow: 0px 3px 7px 0px rgba(16, 140, 238, 0.35);
    }
    &:nth-of-type(3) {
      background: #3d5fb5 !important;
      border: #3d5fb5 !important;
      box-shadow: 0px 3px 7px 0px rgba(116, 120, 196, 0.35);
    }
    &:nth-of-type(4) {
      background: #3d5fb5 !important;
      border-color: #3d5fb5 !important;
      box-shadow: 0px 3px 7px 0px rgba(116, 120, 196, 0.35);
    }
    &:nth-of-type(5) {
      background: #7478c4 !important;
      border-color: #7478c4 !important;
      box-shadow: 0px 3px 7px 0px rgba(116, 120, 196, 0.35);
    }
    &.finish-btn {
      background: #e6a23ced !important;
      border-color: #e6a23c !important;
      box-shadow: 0px 3px 7px 0px rgba(230, 162, 60, 0.35);
    }
    &:hover {
      opacity: 0.9;
    }
  }

  .el-dropdown {
    margin-left: 10px;
  }
}

.moudle-item {
  text-align: left;
  cursor: pointer;
  line-height: 30px;
  padding: 0 17px;
  font-size: 14px;
  color: #606266;
  &:hover {
    background-color: #ecf5ff;
    color: #66b1ff;
  }
}
.moudle-item-active {
  background-color: #ecf5ff;
  color: #66b1ff;
}

.zfsoft-xuanzhong:before {
  font-weight: bold;
}

//顶部导航1280*1024媒体查询
@media only screen and (max-width: 1280px) {
  .handle-content {
    .handle-item {
      padding: 0px 6px !important;
    }
  }
  .handle-btn-group {
    .el-button {
      margin: unset;
      padding: 8px 12px;
    }

    .el-dropdown {
      margin-left: unset;
    }

    .el-button + .el-button {
      margin: unset !important;
    }
  }
}
</style>


<style lang="scss">
.el-dropdown-menu.el-popper {
  min-width: 108px;
  max-height: 500px;
  background: #ffffff;
  border: 1px solid #e0e5f3;
  box-shadow: 0px 0px 6px 0px rgba(152, 179, 213, 0.72);
  border-radius: 3px;
  overflow-y: auto;
}
</style>
