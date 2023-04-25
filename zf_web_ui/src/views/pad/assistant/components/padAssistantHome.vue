<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-07-22 15:25:53
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-11-10 17:13:27
 * @FilePath: \zf_web_ui\src\views\pad\assistant\components\padAssistantHome.vue
 * @Description: pad端首页
-->
<template>
  <div class="padHome-container">
    <div class="top" @click="toCondition">
      <p class="leftTop bounceInRight" :class="initialIndex ? 'animate' : ''" v-show="initialIndex"
        :style="initialIndex ? { 'animation-duration': 500 + 'ms' } : {}">
        今日个人服务总览
      </p>
      <p class="leftTop bounceInLeft" :class="!initialIndex ? 'animate' : ''" v-show="!initialIndex"
        :style="!initialIndex ? { 'animation-duration': 500 + 'ms' } : {}">
        今日大厅服务总览
      </p>
      <div class="righTop none">
        <img :src="require('@/assets/images/pad/messageTips.png')" alt="" />
      </div>
    </div>
    <div class="header">
      <div class="headerLeft none" @click="toIntelligentQA">
        <img :src="require('@/assets/images/pad/wave.png')" alt="" />
        <p>
          <span>你可以这样问我：</span>
          <span>小帮营业执照需要什么材料？</span>
        </p>
      </div>
      <div class="weather">
        <img :src="require('@/assets/images/pad/cloudy.png')" alt="" />
        <div class="right">
          <p>
            <span>上海市 黄浦区</span>
            <!-- <span>空气质量</span> <span>优</span> -->
          </p>
          <p>
            <span v-if="weather.text">{{weather.text + '  ' + weather.temp + '℃'}}</span>
            <span>{{ weather.wind_dir + '  ' + weather.wind_class }}</span>
          </p>
        </div>
      </div>
      <div class="date">
        <p>{{ dateInfo.time }}</p>
        <p>{{ dateInfo.date }} {{ dateInfo.day }}</p>
      </div>
    </div>
    <div class="main-carousel">
      <v-touch class="touch-content" @swipeleft="swipeleft" @swiperight="swiperight">
        <el-carousel ref="nop" :autoplay="false" indicator-position="outside" arrow="never" :loop="true">
          <el-carousel-item v-for="(item, index) in serViceList" :key="index">
            <v-touch class="carousel-item" v-for="(ite, idx) in item.serDetail"
              :style="{ backgroundImage: `url(${ite.backImg})` }" :key="`${index}+'_'${idx}`"
              @swipeup="swipeup(item, index, idx)">
              <div class="carousel-item-content" @click="showDetail(index,ite.label)">
                <p class="title">{{ ite.label }}</p>
                <div class="serviceNum">
                  <div class="num">
                    <numericScrolling :numValue="ite.value"></numericScrolling>
                  </div>
                  <!-- <span class="num">{{ ite.value }}</span> -->
                  <span>人</span>
                </div>
                <div class="foot" v-if="index != 1" :class="ite.swiperup ? 'swiper' : ''">
                  <img :src="require('@/assets/images/pad/swiperUp.png')" alt="" />
                  <div class="foot-content">
                    <div class="left foot-container">
                      <p>圆桌帮办</p>
                      <p>
                        <span>{{ ite.tableWaitNum }}</span>人
                      </p>
                    </div>
                    <div class="right foot-container">
                      <p>快捷帮办</p>
                      <p>
                        <span>{{ ite.quickWaitNum }}</span>人
                      </p>
                    </div>
                  </div>
                </div>
              </div>
            </v-touch>
          </el-carousel-item>
        </el-carousel>
      </v-touch>
    </div>
    <footer>
      <div class="foot-main">
        <div class="center-main" :class="initialIndex === 1 ? 'person-main' : ''">
          <span v-show="!initialIndex">今日来访人数</span>
          <p v-show="!initialIndex" class="comeNum">
            {{ groupServiceNum.visitors }}
          </p>
          <span v-show="initialIndex">平均服务时长</span>
          <p v-show="initialIndex" class="num">
            {{ serviceNum.avgServiceTime }} <span>min</span>
          </p>
        </div>
        <div class="foot-main-container" :class="initialIndex ? 'centerItem' : ''">
          <div class="btnArea" v-if="!initialIndex" @click="toOther('showOuter')">
            <div class="btnArea-content animate slideInDown" :style="{ 'animation-duration': 200 + 'ms' }">
              <img :src="require('@/assets/images/pad/electronicMaterial.png')" alt="" />
              <span>快捷服务</span>
            </div>
          </div>
          <!-- <div class="btnArea" @click="toOther('video','voice')">
            <img :src="require('@/assets/images/pad/voiceCall.png')" alt="" />
            <span>语音求助</span>
          </div> -->
          <div class="btnArea" @click="toOther('video','video')">
          <!-- <div class="btnArea" @click="toOther('videoConsultation')"> -->
            <img :src="require('@/assets/images/pad/videoConsultation.png')" alt="" />
            <span>视频服务</span>
          </div>
        </div>
        <div class="foot-main-container" :class="initialIndex ? 'centerItem' : ''">
          <div class="btnArea" v-if="initialIndex" @click="toOther('materialStore')">
            <div class="btnArea-content animate slideInUp" :style="{ 'animation-duration': 200 + 'ms' }">
              <img :src="require('@/assets/images/pad/myDatabase.png')" alt="" />
              <span>个人资料库</span>
            </div>
          </div>
          <!-- <div class="btnArea" @click="toOther('search')">
            <img :src="require('@/assets/images/pad/assistantKnowledge.png')" alt="" />
            <span>帮办知识库</span>
          </div> -->
          <div class="btnArea" v-if="!initialIndex" @click="jumpBackLink('oneThing')">
            <img :src="require('@/assets/images/pad/myDatabase.png')" alt="" />
            <span>一件事</span>
          </div>
          <div class="btnArea" v-if="!initialIndex" @click="jumpBackLink('oneProof')">
            <img :src="require('@/assets/images/pad/assistantKnowledge.png')" alt="" />
            <span>一业一证</span>
          </div>
        </div>
      </div>
    </footer>
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
        v-if="showQueueCondition"
        @closeQueueCondition="closeQueueCondition"
        @openshowAddDocument="openshowAddDocument"
        @changeToService="toService"
      />
    </el-dialog>
    <el-dialog
      title="快捷服务"
      :visible.sync="showOuter"
      width="80%"
      class="pageDialog"
      center
      append-to-body
      v-dialogDrag
    >
      <outer />
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
      title="选择部门"
      :visible.sync="videoShow"
      width="60%"
      class="window-number"
      custom-class="serviceTransfer"
      v-dialogDrag
    >
      <chooseDepart v-if="videoShow" :type="type" @closeDepart="closeDepart"/>
    </el-dialog>
    <el-dialog
        title="服务总览"
        :visible.sync="showWorkUserList"
        width="90%"
        custom-class="serviceTransfer"
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
    <!-- 窗口还是 -->
    <el-dialog v-dialog-drag title="部门或窗口选择" :visible.sync="showSelectBusinessType" class="select-dialog" width="80%"
      append-to-body>
      <div class="select-Body">
        <!-- <p class="tips">请选择窗口取号方式</p> -->
        <div class="select-content">
          <div class="item" @click="getWindow">
            <img :src="require('@/assets/images/pad/consultingService.png')" alt="" />
            <p>窗口</p>
          </div>
          <div class="item" @click="getWindow">
            <img :src="require('@/assets/images/pad/storesReserve.png')" alt="" />
            <p>部门</p>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import outer from "./test.vue";
import queueCondition from "./queueCondition.vue";
import serviceQueue from "./serviceQueue.vue";
import chooseDepart from "./chooseDepart.vue";
import { setFormatDate } from "@/utils/index";
import { getQueueNum, getQueueAllNum, getAllUserService,getWeather } from "@/api/modules/helpAgent";
import numericScrolling from "./process/assembly/numericScrolling.vue";
import workUserCondition from "./workUserCondition.vue";
export default {
  name: "padAssistantHome",
  components: {
    numericScrolling,
    chooseDepart,
    workUserCondition,
    queueCondition,
    serviceQueue,
    outer
  },
  data() {
    return {
      showQueueCondition:false,
      showServiceQueue:false,
      serviceStatus: "2",
      weather:{
        temp:'',
        wind_class:'',
        text:'',
        wind_dir:''
      },
      groupServiceNum: {},
      serviceNum: {},
      serViceList: [
        {
          serDetail: [
            {
              backImg: require("@/assets/images/pad/allWaitServe.png"),
              label: "等待服务",
              value: null,
              swiperup: false,
              tableWaitNum: null,
              quickWaitNum: null,
            },
            {
              backImg: require("@/assets/images/pad/allService.png"),
              label: "正在服务",
              value: null,
              swiperup: false,
              tableWaitNum: null,
              quickWaitNum: null,
            },
            {
              backImg: require("@/assets/images/pad/allCumulativeService.png"),
              label: "累计服务",
              value: null,
              swiperup: false,
              tableWaitNum: null,
              quickWaitNum: null,
            },
          ],
        },
        {
          serDetail: [
            {
              backImg: require("@/assets/images/pad/waitServe.png"),
              label: "等待服务",
              value: null,
              swiperup: false,
            },
            {
              backImg: require("@/assets/images/pad/isService.png"),
              label: "正在服务",
              value: null,
              swiperup: false,
            },
            {
              backImg: require("@/assets/images/pad/cumulativeService.png"),
              label: "累计服务",
              value: null,
              swiperup: false,
            },
          ],
        },
      ],
      initialIndex: 0,
      dateInfo: {
        time: "",
        date: "",
        day: "",
      },
      videoShow:false,
      type:'',
      showWorkUserList:false,
      showSelectBusinessType:false,
      showOuter:false
    };
  },
  computed: {
    basicUserInfo() {
      return this.$store.state.user.basicUserInfo;
    },
    serviceOperateStatus() {
      return this.$store.state.pageData.serviceOperateStatus;
    },
  },
  props:{
    stepSwitch: {
      type: Number,
      default: () => "",
    },
  },
  mounted() {
    this.getNowDate();
    this.getQueueNum();
    this.getAllUserService();
    getWeather().then(res=>{
      this.weather = res.data.result.realtime;
    })
  },
  methods: {
    openshowAddDocument(){
      this.showQueueCondition = false;
      this.$emit('openDocution');
    },  
    //跳转到服务页面
    toService(type){
      this.$emit("changeCatalogue",type)
    },
    closeQueueCondition() {
      this.showQueueCondition = false;
    },
    //打开弹窗
    showDetail(index,label){
      // console.log(index,label);
      
      if(index == 1 && label == '等待服务'){
        this.showQueueCondition = true;
      }

      if(index == 1 && label == '正在服务'){
        this.showServiceQueue = true;
        this.serviceStatus = '2';
      }
      if(index == 1 && label == '累计服务'){
        this.showServiceQueue = true;
        this.serviceStatus = '3';
      }
    },
    toCondition(){
      this.showWorkUserList = true;
    },  
    closeWorkUserCondition() {
      this.showWorkUserList = false;
    },
    closeDepart(){
      this.videoShow = false;
    },
    jumpBackLink(name){
      if (name == 'oneThing') {
        android.setWebUrl('https://zwdt.sh.gov.cn/qykj/guide_hp/yslb');
        return;
      }
      if(name == 'oneProof'){
        android.setWebUrl('http://180.169.19.202:8085/index');
        return;
      }

    },
    toOther(name,type) {
      if (name == 'search') {
        this.$emit('changeCatalogue', 'search');
        return;
      }
      if(name == 'showOuter'){
        this.showOuter = true;
        return;
      }
      // 视频咨询
      if(name == 'video'){
        if(localStorage.getItem('baseUserInfo')){
          this.$emit('openBusinessType');
        }else{
          this.$message.warning("请先选择接待用户");
        }
        // this.showSelectBusinessType = true;
        // this.type = type;
        return;
      }
      if(name == 'todo'){
        this.$message.warning("正在建设中");
        return;
      }
      this.$router.push({
        name
      })
    },
    getWindow(){
      this.showSelectBusinessType = false;
      this.videoShow = true;
    },  
    //
    toIntelligentQA() {
      this.$emit("changeCatalogue", "intelligentQA");
    },

    //获取当前时间
    getNowDate() {
      let weeks = new Array(
        "星期日",
        "星期一",
        "星期二",
        "星期三",
        "星期四",
        "星期五",
        "星期六"
      );

      setInterval(() => {
        const date = new Date();
        const obj = {
          day: weeks[date.getDay()],
          time: setFormatDate(date, "hh:mm"),
          date:
            setFormatDate(date, "MM") + "月" + setFormatDate(date, "dd") + "日",
        };
        this.dateInfo = obj;
      }, 1000);
    },
    swipeleft() {
      this.prev();
      console.log("左");
    },
    swiperight() {
      console.log("右");
      this.next();
      this.initialIndex = this.$refs.nop.activeIndex;
    },
    prev() {
      this.$refs.nop.prev();
      this.initialIndex = this.$refs.nop.activeIndex;
    },
    next() {
      this.$refs.nop.next();
    },
    swipeup(data, index, idx) {
      this.$set(this.serViceList[index].serDetail[idx], "swiperup", true);
      this.$nextTick(() => {
        this.serViceList = [...this.serViceList];
      });
      setTimeout(() => {
        this.$set(this.serViceList[index].serDetail[idx], "swiperup", false);
        this.$nextTick(() => {
          this.serViceList = [...this.serViceList];
        });
      }, 3000);
    },

    //统计大厅服务总览
    getQueueNum() {
      getQueueAllNum().then((res) => {
        if (res.code === 200) {
          this.$store.commit("setServiceOperateStatus", false);
          this.groupServiceNum = res.data;
          this.groupServiceNum.visitors =
            res.data.inServiceAllNum +
            res.data.waitIngAllNum +
            res.data.todayServiceAllNum;
          this.$set( this.serViceList[0].serDetail[0],"value",this.groupServiceNum.waitIngAllNum ?? 0);
          this.$set( this.serViceList[0].serDetail[0],"tableWaitNum",this.groupServiceNum.waitIngYZAllNum ?? 0);
          this.$set( this.serViceList[0].serDetail[0],"quickWaitNum",this.groupServiceNum.waitIngKJAllNum ?? 0);
          this.$set( this.serViceList[0].serDetail[1],"value",this.groupServiceNum.inServiceAllNum ?? 0);
          this.$set( this.serViceList[0].serDetail[1],"tableWaitNum",this.groupServiceNum.inServiceYZAllNum ?? 0);
          this.$set( this.serViceList[0].serDetail[1],"quickWaitNum",this.groupServiceNum.inServiceKJAllNum ?? 0);
          this.$set( this.serViceList[0].serDetail[2],"value",this.groupServiceNum.todayServiceAllNum ?? 0);
          this.$set( this.serViceList[0].serDetail[2],"tableWaitNum",this.groupServiceNum.todayServiceYZAllNum ?? 0);
          this.$set( this.serViceList[0].serDetail[2],"quickWaitNum",this.groupServiceNum.todayServiceKJAllNum ?? 0);
          // this.$set(
          //   this.serViceList[0].serDetail[1],
          //   "value",
          //   this.groupServiceNum.inServiceGroupNum ?? 0
          // );
          // this.$set(
          //   this.serViceList[0].serDetail[2],
          //   "value",
          //   this.groupServiceNum.todayServiceGroupNum ?? 0
          // );
          // this.$set(
          //   this.serViceList[0].serDetail[0],
          //   "quickWaitNum",
          //   res.data.quickWaitNum
          // );
          // this.$set(
          //   this.serViceList[0].serDetail[0],
          //   "tableWaitNum",
          //   res.data.tableWaitNum
          // );
        }
      });
    },
    getAllUserService() {
      const params = {
        groupId: this.basicUserInfo.groupId,
        name: this.basicUserInfo.name,
      };
      getAllUserService(params).then((res) => {
        this.$store.commit("setServiceOperateStatus", false);
        if (res.code === 200) {
          this.serviceNum = res.data[0];
          this.$set(
            this.serViceList[1].serDetail[0],
            "value",
            this.serviceNum.waitingNum ?? 0
          );
          this.$set(
            this.serViceList[1].serDetail[1],
            "value",
            this.serviceNum.inServiceNum ?? 0
          );
          this.$set(
            this.serViceList[1].serDetail[2],
            "value",
            this.serviceNum.todayServiceNum ?? 0
          );
        }
      });
    },
    // getUserGroupService() {
    //   getUserGroupService().then((res) => {
    //     if (res.code === 200) {
    //       this.groupwaitingNum = res.data.waitingNum ?? 0;
    //       this.groupInServiceNum = res.data.inServiceNum ?? 0;
    //       this.groupTodayServiceNum = res.data.todayServiceNum ?? 0;
    //       this.groupVisitorsNum =
    //         this.groupwaitingNum +
    //         this.groupInServiceNum +
    //         this.groupTodayServiceNum;
    //     }
    //   });
    // },
  },
  watch: {
    stepSwitch(){
      this.getNowDate();
      this.getQueueNum();
      this.getAllUserService();
    },
    serviceOperateStatus: {
      handler(val) {
        if (val) {
          this.getQueueNum();
          this.getAllUserService();
        }
      },
    },
  },
};
</script>
<style lang="less" scoped>
.bounceInRight:visited,.bounceInLeft:visited{
  opacity: 0.5;
}
.bounceInRight:active,.bounceInLeft:active{
  opacity: 0.5;
}
.padHome-container {
  width: 100%;
  height: 100%;
  position: relative;

  // border-radius: 2.1176rem 0px 0px 2.1176rem;
  .top {
    width: 100%;
    height: 2.3529rem;
    display: flex;
    align-items: center;
    justify-content: space-between;

    .leftTop {
      padding: 0;
      margin: 0;
      width: 15.1176rem;
      height: 3.0588rem;
      text-align: center;
      line-height: 3.0588rem;
      background: rgba(193, 214, 246, 0.6);
      border-radius: 2.1176rem 0px 2.1176rem 0px;
      font-size: 1.2941rem;
      font-family: PingFang SC;
      font-weight: bold;
      color: #4f5c6b;
    }

    .righTop {
      height: 100%;
      width: calc(100% - 21.1765rem);
      padding-left: calc(50% - 21.1765rem);
      display: flex;
      align-items: center;
      justify-content: flex-start;
      background: linear-gradient(90deg,
          rgba(201, 222, 245, 0) 0%,
          #a4c8f0 100%);
      opacity: 0.42;

      img {
        width: 2.3529rem;
      }
    }
  }

  .header {
    width: 100%;
    margin-top: 3.7647rem;
    height: 9.375rem;
    padding: 0 5.1176rem;
    box-sizing: border-box;
    display: flex;
    align-items: center;
    justify-content: space-around;

    .headerLeft {
      width: 32rem;
      height: 8.4706rem;
      border-image: linear-gradient(123deg, #ffffff) 10 10;
      background: linear-gradient(-41deg, #d5e8fc 0%, #a8d5f7 100%);
      box-shadow: 0px 0px 0.4117rem 0px rgba(181, 205, 229, 0.31);
      opacity: 0.8;
      border-radius: 3.5294rem 1.1765rem 1.1765rem 1.1765rem;
      padding: 1.7647rem 1.353rem 1.2353rem 2.5883rem;
      box-sizing: border-box;
      display: flex;
      align-items: center;
      justify-content: flex-start;

      img {
        width: 5.4706rem;
      }

      p {
        width: auto;
        height: 5.4706rem;
        padding: 0;
        margin: 0;
        margin-left: 1.7059rem;
        display: flex;
        flex-direction: column;
        align-items: flex-start;
        justify-content: center;

        span {
          &:nth-child(1) {
            font-size: 1.1765rem;
            font-family: PingFang SC;
            font-weight: bold;
            color: #808ba1;
          }

          &:nth-child(2) {
            margin-top: 1rem;
            font-size: 1.4118rem;
            font-family: PingFang SC;
            font-weight: bold;
            color: #171d26;
          }
        }
      }
    }

    .weather {
      width: 37.5294rem;
      height: 8.4706rem;
      background: linear-gradient(-41deg,
          rgba(208, 240, 255, 0) 0%,
          #fdecd8 100%);
      box-shadow: 0px 0px 0.4118rem 0px rgba(181, 205, 229, 0.71);
      border-radius: 3.5294rem 1.1765rem 1.1765rem 1.1765rem;
      padding: 1.7059rem 0.7059rem 1.5294rem 1.7647rem;
      box-sizing: border-box;
      display: flex;
      align-items: center;
      justify-content: flex-start;

      img {
        height: 5.2471rem;
      }

      .right {
        width: auto;
        height: 5.2471rem;
        padding: 0;
        margin: 0;
        margin-left: 2.4706rem;
        display: flex;
        flex-direction: column;
        align-items: flex-start;
        justify-content: center;

        p {
          padding: 0;
          margin: 0;
          width: auto;

          &:nth-child(1) {
            span {

              &:nth-child(1),
              &:nth-child(2) {
                font-size: 1.4118rem;
                font-family: PingFang SC;
                font-weight: bold;
                color: #171d26;
              }

              &:nth-child(1) {
                margin-right: 3.5294rem;
                display: inline-block;
                min-width: 9.4118rem;
                text-align: left;
              }

              &:nth-child(3) {
                margin-left: 1rem;
                font-size: 1.4118rem;
                font-family: PingFang SC;
                font-weight: bold;
                color: #32c29e;
              }
            }
          }

          &:nth-child(2) {
            margin-top: 1.3529rem;

            span {
              font-size: 1.2353rem;
              font-family: PingFang SC;
              font-weight: bold;
              color: #808ba1;

              &:nth-child(1) {
                margin-right: 3.5294rem;
                display: inline-block;
                width: 9.4118rem;
                text-align: left;
              }
            }
          }
        }
      }
    }

    .date {
      width: 13.5294rem;
      height: 8.4706rem;
      margin-left: 2rem;
      display: flex;
      flex-direction: column;
      align-items: flex-start;
      justify-content: center;

      p {
        padding: 0;
        margin: 0;

        &:nth-child(1) {
          font-size: 4rem;
          font-family: DIN;
          font-weight: 500;
          color: #171d26;
        }

        &:nth-child(2) {
          margin-top: 0.8824rem;
          font-size: 1.1765rem;
          font-family: PingFang SC;
          font-weight: bold;
          color: #606a7e;
        }
      }
    }
  }

  .main-carousel {
    width: 100%;
    height: 31.7647rem;
    margin-top: 5.1176rem;
    height: calc(100% - 35rem);
    .touch-content {
      width: 100%;
      height: 100%;
      position: relative;

      /deep/ .el-carousel {
        width: 100%;
        height: 100% !important;
        position: absolute;
        left: 0;
        top: 0;
        z-index: 0;

        .el-carousel__container {
          width: 100%;
          height: 100% !important;

          .el-carousel__item {
            width: 100%;
            height: 100%;
            padding: 0 5rem;
            display: flex;
            align-items: center;
            padding-top: 1.4118rem;
            justify-content: space-between;

            .carousel-item {
              width: 26.4941rem;
              height: 25.6471rem;
              background-repeat: no-repeat !important;
              background-size: 100% 100% !important;
              border-radius: 3.5294rem 1.1765rem 1.1765rem 1.1765rem;
              position: relative;
              box-shadow: 0rem 1.1176rem 2.3529rem 0rem rgba(159, 186, 215, 0.4);
              position: relative;
              overflow: hidden;

              &:nth-child(1) {
                margin-left: 0;
              }

              .carousel-item-content {
                width: 100%;
                height: 100%;
                padding: 2.2941rem 0 0 2.4118rem;
                position: absolute;
                left: 0;
                top: 0;
                z-index: 1000;
              }

              .title {
                padding: 0;
                margin: 0;
                font-size: 1.4118rem;
                font-family: PingFang SC;
                font-weight: bold;
                color: #171d26;
                text-align: left;
              }

              .serviceNum {
                width: 100%;
                height: 6.7rem;
                margin-top: 1.8235rem;
                display: flex;
                align-items: flex-end;
                justify-content: flex-start;

                .num {
                  margin-right: 1.3571rem;
                  width: auto;
                  min-width: 3rem;
                  height: 100%;
                  display: flex;
                  align-items: flex-end;
                  justify-content: flex-end;
                }

                span {
                  font-size: 1.4286rem;
                  font-family: PingFang SC;
                  font-weight: bold;
                  color: #171d26;
                  margin-bottom: 1rem;
                }
              }

              .foot {
                width: 100%;
                height: 10.7059rem;
                position: absolute;
                bottom: 0;
                left: 0;
                z-index: 100;
                display: none;

                img {
                  width: 3.4118rem;
                }

                .foot-content {
                  width: 100%;
                  height: 9rem;
                  background: rgba(255, 255, 255, 0.72);
                  border-radius: 1.1765rem;
                  position: relative;
                  padding: 2.2941rem 2.9412rem 1.4118rem 2.2353rem;
                  box-sizing: border-box;
                  display: flex;
                  justify-content: space-between;

                  .foot-container {
                    width: 50%;
                    height: 100%;

                    &:nth-child(1) {
                      display: flex;
                      flex-direction: column;
                      align-items: flex-start;
                    }

                    &:nth-child(2) {
                      display: flex;
                      flex-direction: column;
                      align-items: flex-end;
                    }

                    p {
                      padding: 0;
                      margin: 0;

                      &:nth-child(1) {
                        font-size: 1.4118rem;
                        font-family: PingFang SC;
                        font-weight: bold;
                        color: #171d26;
                      }

                      &:nth-child(2) {
                        font-size: 1.1765rem;
                        font-family: PingFang SC;
                        font-weight: bold;
                        color: #171d26;

                        span {
                          font-size: 3.5294rem;
                          font-family: DIN;
                          font-weight: bold;
                          color: #323f53;
                        }
                      }
                    }

                    &:nth-child(2) {
                      text-align: left;
                    }

                    &:nth-child(3) {
                      text-align: right;
                    }
                  }
                }
              }

              .swiper {
                width: 100%;
                height: 10.7059rem;
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: center;
                position: absolute;
                left: 0;
                bottom: -10.7059rem;
                animation-name: swiperup;
                animation-duration: 3s;
                animation-direction: alternate;
              }

              @keyframes swiperup {
                0% {
                  bottom: -10.7059rem;
                }

                20% {
                  bottom: 0;
                }

                70% {
                  bottom: 0;
                }

                100% {
                  bottom: -10.7059rem;
                }
              }
            }
          }
        }

        .el-carousel__indicators {
          position: absolute;
          width: 2.3529rem;
          left: calc(50% - 1.1765rem);
          bottom: 0.5rem;
          display: flex;
          align-items: center;
          justify-content: center;

          li {
            width: 0.8824rem;
            height: 0.8824rem;
            margin-right: 0.9412rem;

            .el-carousel__button {
              width: 0.8824rem;
              height: 0.8824rem;
              border-radius: 50%;
              background-color: #425067 !important;
            }
          }
        }
      }
    }
  }

  footer {
    width: 100%;
    height: 7.2471rem;
    padding: 0 5.1176rem;
    margin-top: 3.7rem;

    .foot-main {
      width: 100%;
      height: 100%;
      background: url("@/assets/images/pad/footBack.png") no-repeat;
      background-size: 100% 100%;
      position: relative;
      display: flex;
      align-items: center;
      justify-content: space-between;

      .center-main {
        width: 21.5882rem;
        height: 9.7059rem;
        background: url("@/assets/images/pad/centerMain.png") no-repeat;
        background-size: 100% 100%;
        // box-shadow: 0px 0px 1.4118rem 0px #9fbad7;
        position: absolute;
        bottom: 0.7059rem;
        left: calc(50% - 10.7941rem);
        display: flex;
        flex-direction: column;
        justify-content: flex-start;
        align-items: center;
        padding-top: 1.6706rem;

        span {
          font-size: 1.1765rem;
          font-family: PingFang SC;
          font-weight: bold;
          color: #edf4fc;
        }

        p {
          font-size: 4rem;
          font-family: DIN;
          font-weight: bold;
          color: #323f53;
          padding: 0;
          margin: 0;
          margin-top: 0.66rem;

          span {
            font-size: 1.1765rem;
            font-family: PingFang SC;
            font-weight: bold;
            color: #4f5c6b;
            margin-left: 0.8824rem;
          }
        }

        .num {
          margin-left: 2rem;
          display: flex;
          align-items: center;
        }
      }

      .person-main {
        background: url("@/assets/images/pad/personMain.png") no-repeat;
        background-size: 100% 100%;
      }
      .centerItem{
        justify-content: space-around !important;
      }
      .foot-main-container {
        width: 40%;
        height: 100%;
        display: flex;
        align-items: center;

        &:nth-child(2) {
          padding-left: 3.4118rem;
          justify-content: flex-start;

          .btnArea {
            &:nth-child(1) {
              margin-right: 1.7647rem;
            }
          }
        }

        &:nth-child(3) {
          padding-right: 3.2941rem;
          justify-content: flex-end;

          .btnArea {
            margin-right: 1.7647rem;

            &:nth-child(1),
            &:nth-child(2) {
              position: relative;

              .btnArea-content {
                width: 100%;
                height: 100%;
                display: flex;
                align-items: center;
                justify-content: flex-start;
              }
            }

            &:nth-last-child(1) {
              margin-right: 0;
            }
          }
        }

        .btnArea {
          width: auto;
          height: auto;
          padding: 1.2353rem 1.8235rem 1.1765rem 1.7059rem;
          border-radius: 2.2353rem;
          background: url("@/assets/images/pad/duan.png") no-repeat;
          background-size: 100% 100%;
          display: flex;
          align-items: center;
          justify-content: flex-start;

          img {
            width: 2.0588rem;
          }

          span {
            font-size: 1.4118rem;
            font-family: PingFang SC;
            font-weight: bold;
            color: #4f5c6b;
            margin-left: 1.2353rem;
          }
        }
      }
    }
  }
}
  
</style>
<style lang="scss">
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
  .window-number {
    ::v-deep .el-dialog__body {
        max-height: 100% !important;
    }
    
  }
  .pageDialog {
     .el-dialog .el-dialog__body {
        height: calc(100% - 3.75rem) !important;
        max-height: 100%;
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
</style>