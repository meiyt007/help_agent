<template>
  <div class="business-handling-calling">
    <div class="calling-time white-box">
      <div class="left">
        <img
          src="@/assets/image/calling/timer.png"
          alt=""
          width="23"
          height="23"
        />
        <span class="title">办理时间：</span>
      </div>
      <span class="time right">{{ currentPerson.time }}</span>
    </div>
    <div class="current-caller white-box">
      <div class="left">
        <img
          src="@/assets/image/calling/current-num.png"
          alt=""
          width="28"
          height="25"
        />
        <span class="title">当前号：</span>
      </div>
      <span class="num right">{{ currentPerson.currentNum }}</span>
    </div>
    <div class="wating-person-number white-box">
      <div class="left">
        <img
          src="@/assets/image/calling/wating.png"
          alt=""
          width="27"
          height="31"
        />
        <span class="title">等待人数：</span>
      </div>
      <div class="right">
        <span class="person-num num">{{ watingPersonNum }}</span>
        <span class="person">人</span>
      </div>
    </div>
    <div class="work-step white-box">
      <div class="left">
        <el-button class="recall" @click="recall">
          <img src="@/assets/image/calling/recall.png" alt="" />
          重呼
        </el-button>
        <el-button class="work" @click="handleWorking">
          <img src="@/assets/image/calling/work-start.png" alt="" />
          办理
        </el-button>
        <el-button class="work-done" @click="getThingsDone">
          <img src="@/assets/image/calling/over.png" alt="" />
          完成办事
        </el-button>
        <el-button class="special-calling" @click="specialCalling">
          <img src="@/assets/image/calling/star.png" alt="" />
          特呼
        </el-button>
      </div>
      <el-button class="calling-next" @click="callingNextPerson">
        <img src="@/assets/image/calling/next.png" alt="" />
        <span>下一位</span>
      </el-button>
    </div>
  </div>
</template>

<script>
import { getloginUser } from "@/api/zc/businessManagement/windowAcceptance";
import { showCallMessage } from "@/api/zc/businessManagement/doubleScreenInteraction";
const callingList = [
  { currentNum: "未开始叫号", time: "00: 00: 00", id: 0 },
  { currentNum: "A005", time: "00: 00: 00", id: 1 },
  { currentNum: "A004", time: "00: 00: 00", id: 2 },
  { currentNum: "A003", time: "00: 00: 00", id: 3 },
  { currentNum: "A002", time: "00: 00: 00", id: 4 },
  { currentNum: "A001", time: "00: 00: 00", id: 5 }
];
export default {
  name: "BusinessHandlingCalling",
  data() {
    return {
      callingList: [...callingList],
      handledCallingList: [], // 已办
      idx: 0,
      timer: null,
      h: 0,
      s: 0,
      m: 0,
      isHandleWorking: false, // 是否正在办理
      //登录信息
      loginUser: {},
      waitNum: null
    };
  },
  created() {
    this.queryLoginInfo();
  },
  computed: {
    currentPerson() {
      return this.callingList[this.idx || 0];
    },

    watingPersonNum() {
      this.waitNum =
        this.callingList.length - 1 - this.handledCallingList.length;
      return this.callingList.length - 1 - this.handledCallingList.length;
    }
  },
  beforeDestroy() {
    this.timer && clearInterval(this.timer);
  },
  methods: {
    /** 登录信息 */
    queryLoginInfo() {
      let _that = this;
      getloginUser().then(response => {
        _that.loginUser = response.data;
      });
    },
    showCall() {
      let _that = this;
      let url = window.location.origin;
      let pushUrl = url + "/serviceHall/index.html";
      let pushForm = {};
      let data = {};
      data.waitNum = _that.waitNum;
      data.now_1 = _that.currentPerson.currentNum.substring(0, 1);
      data.now_2 = _that.currentPerson.currentNum.substring(1, 2);
      data.now_3 = _that.currentPerson.currentNum.substring(2, 3);
      data.now_4 = _that.currentPerson.currentNum.substring(3, 4);
      data.next_1 = _that.currentPerson.currentNum.substring(0, 1);
      data.next_2 = _that.currentPerson.currentNum.substring(1, 2);
      data.next_3 = _that.currentPerson.currentNum.substring(2, 3);
      data.next_4 = _that.currentPerson.currentNum.substring(3, 4) - 1;
      pushForm.pushUrl = pushUrl + "?jsonObject=" + JSON.stringify(data);
      window.open(pushUrl + "?jsonObject=" + JSON.stringify(data));
      /*       showCallMessage(JSON.stringify(pushForm)).then(response => {
               console.log(response.data);
             });*/
    },
    //超级综窗柜台双屏登录打开用户信息
    openWelcome() {
      let _that = this;
      let url = window.location.origin;
      let pushUrl = url + "/serviceHall/suspendedService.html";
      // let pushForm = {};
      //let data = {};
      // data.userName = _that.loginUser.userName;
      // data.organName = _that.loginUser.organName;
      //pushForm.pushUrl = pushUrl+"?jsonObject="+JSON.stringify(data);
      // window.open(pushUrl+"?jsonObject="+JSON.stringify(data))
      window.open(pushUrl);
      /*     showCallMessage(JSON.stringify(pushForm)).then(response => {
               console.log(response.data);
           });*/
    },
    // 呼叫下一位用户
    callingNextPerson() {
      if (this.idx !== 0) {
        this.handledCallingList.push(this.currentPerson);
      }
      this.resetTimer();
      if (this.idx === this.callingList.length - 1) {
        this.idx = 0;
        this.callingList = [...callingList];
        this.handledCallingList = [];
        return;
      }
      this.idx++;
      this.showCall();
    },

    // 重新呼叫
    recall() {
      if (this.idx === 0) return this.$message.warning("请先开始叫号");
      if (!this.isHandleWorking) return this.$message.warning("请先点击办理");
      this.currentPerson.time = "00: 00: 00";
      this.resetTimer();
      this.handleWorking();
      this.$message.success("已重新呼叫");
    },

    // 办理
    handleWorking() {
      if (this.isHandleWorking)
        return this.$message.warning("正在办理中, 请先结束当前办理");
      if (this.idx === 0) return this.$message.warning("请先开始叫号");
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
        this.currentPerson.time = `${this.h < 10 ? "0" + this.h : this.h}: ${
          this.s < 10 ? "0" + this.s : this.s
        }: ${this.m < 10 ? "0" + this.m : this.m}`;
      }, 1000);
    },

    // 完成办事
    getThingsDone() {
      if (this.idx === 0) return this.$message.warning("请先开始叫号");
      this.timer && clearInterval(this.timer);
      this.isHandleWorking && this.handledCallingList.push(this.currentPerson);
      this.isHandleWorking = false;
      // 重置
      //   this.resetTimer();
      //   this.callingList.splice(this.idx, 1);
      //   this.idx = 0;
      this.openWelcome();
    },

    // 特殊呼叫
    specialCalling() {
      this.$message.success("已为您特殊呼叫用户");
    },

    resetTimer() {
      this.timer && clearInterval(this.timer);
      this.h = 0;
      this.m = 0;
      this.s = 0;
      this.isHandleWorking = false;
    }
  }
};
</script>

<style scoped lang="scss">
.business-handling-calling {
  width: 100%;
  height: 86px;
  background: #ebeef2;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 15px;

  .left,
  .right {
    display: flex;
    align-items: center;
  }

  .right {
    flex: 1;
    justify-content: center;
  }

  .title {
    font-size: 15px;
    font-family: Microsoft YaHei;
    font-weight: bold;
    color: #333333;
  }

  img {
    margin-right: 10px;
  }

  .calling-time {
    flex: 1;
    .time {
      font-size: 14px;
      font-weight: bold;
      color: #ff7c07;
    }
  }

  .current-caller {
    flex: 1;
  }

  .wating-person-number {
    flex: 1;
    .person {
      font-size: 15px;
      font-weight: bold;
      color: #333333;
    }
  }

  .work-step {
    flex: 2;
    justify-content: space-between;
    margin-right: unset !important;
    ::v-deep.el-button {
      height: 30px;
      background: linear-gradient(270deg, #1248ef 0%, #03afff 100%);
      border-radius: 5px;
      display: flex;
      align-items: center;
      padding: 0 15px;
      cursor: pointer;
      span {
        display: flex;
        align-items: center;
        font-size: 15px;
        font-weight: bold;
        color: #ffffff;
      }

      img {
        width: 16px;
        height: 16px;
      }
    }
    .left {
      flex: 1;
      justify-content: space-around;

      .special-calling {
        background: #295e87;
      }
    }
    .calling-next {
      display: flex;
      justify-content: center;
      align-items: center;
      cursor: pointer;
      width: 154px;
      height: 100%;
      background: linear-gradient(-90deg, #fc7b1c 0%, #ffb503 100%);
      border-radius: 0px 5px 5px 0px;
      span {
        font-size: 20px;
        font-weight: bold;
        color: #ffffff;
      }

      img {
        width: 26px;
        height: 24px;
      }
    }
  }

  .num {
    font-size: 14px;
    font-family: DIN Black;
    font-weight: bold;
    color: #0470bc;
  }

  .white-box {
    height: 65px;
    background: #ffffff;
    border-radius: 10px;
    display: flex;
    align-items: center;
    padding-left: 20px;
    margin-right: 15px;
  }
}
</style>
