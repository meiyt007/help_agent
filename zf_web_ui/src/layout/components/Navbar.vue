<template>
  <div class="navbar">
    <div class="projectTitle">
      <img :src="require('@/assets/images/home/systemLogo.png')" alt="" />
    </div>
    <div class="rightBlock">
      <div class="tipsArea" v-show="haveNewMesses || haveTurnService">
        <img :src="require('@/assets/images/tips.png')" alt="" />
        <span>新消息：</span>
        <el-carousel direction="vertical" :interval="2000">
          <el-carousel-item v-for="item in messageList" :key="item.message">
            <el-tooltip
              :content="item.message"
              placement="top"
              effect="light"
              :disabled="tooltipDisable"
            >
              <p @mouseenter="isShowTooltip" class="textContent">
                {{ item.message }}
              </p>
            </el-tooltip>
            <span
              class="detail"
              v-show="item.type === 'turnService'"
              @click="toDeailTurnService(item)"
              >查看详情</span
            >
            <span class="detail" v-show="item.type === 'turnService'">。</span>
          </el-carousel-item>
        </el-carousel>
      </div>
      <!-- <div class="searchArea">
        <el-input placeholder="输入关键词搜索" prefix-icon="el-icon-search" v-model="keyWord">
        </el-input>
      </div> -->
      <!-- <div class="notice">
        <img :src="require('@/assets/images/home/notice.png')" alt="" />
        <span class="rollIcon"></span>
      </div>
      <div class="skinPeeler">
        <img :src="require('@/assets/images/home/skinPeeler.png')" alt="" />
      </div> -->

      <div class="outLink" @click="jumpOutLink('一业一证')">
        <img src="~@/assets/images/home/yiyeyizheng.png" alt="">
        <span>一业一证</span>
      </div>
      <div class="outLink" @click="jumpOutLink('一件事')">
        <img src="~@/assets/images/home/yijianshi.png" alt="">
        <span>一件事</span>
      </div>
      <div class="outLink" @click="jumpOutLink('政策')" >
        <img src="~@/assets/images/home/zhengce.png" alt="">
        <span>政策库</span>
      </div>
      <el-dropdown class="avatar-container" trigger="click">
        <div class="userArea">
          <el-image class="headPortrait" :src="basicUserInfo.image">
            <div slot="error" class="image-slot">
              <img
                class="headPortrait"
                :src="require('@/assets/images/headPortrait.png')"
                alt=""
              />
            </div>
          </el-image>

          <div class="center">
            <p>{{ basicUserInfo.name }}</p>
            <p>{{ basicUserInfo.account }}</p>
          </div>
          <div class="avatar-wrapper">
            <i class="el-icon-caret-bottom" />
          </div>
        </div>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item @click.native="openPersonalCenter"
            >个人中心</el-dropdown-item
          >
          <el-dropdown-item @click.native="openScan"
            >扫码办理</el-dropdown-item
          >
          <el-dropdown-item divided @click.native="logout">
            <span>退出登录</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <el-dialog
      title="服务转派"
      :visible.sync="serviceTransfer"
      width="80%"
      custom-class="serviceTransfer"
      v-dialogDrag
    >
      <transferService v-if="serviceTransfer" :turnRecordId="turnRecordId" />
      <!-- <div slot="footer" class="dialog-footer">
        <el-input
          placeholder="请填写拒绝转派原因"
          v-model="rollbackMemo"
          v-show="operateServiceType === '3'"
        />
        <p @click="operateService('3')">拒绝</p>
        <p @click="operateService('2')">接收</p>
      </div> -->
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
      title="政策库"
      :visible.sync="showPolicy"
      width="80%"
      top="4vh !important"
      v-dialogDrag
      append-to-body>
      <policyPc></policyPc>
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
  </div>
</template>

<script>
import store from "../../store";
import { getLoginUrl } from "@/utils/auth";
import { online } from "@/api/login";
import {
  isHaveNewService,
  getMessesByTurn,
  serviceAccept,
} from "@/api/modules/helpAgent";
import transferService from "./transferService.vue";
import personalCenter from "./personalCenter.vue";
import PolicyPc from '@/views/pad/assistant/components/policyPc.vue';
import QRCode from "qrcode";
export default {
  components: {
    transferService,
    personalCenter,
    PolicyPc,
  },
  data() {
    return {
      initTheme: "#409EFF", // content of theme-chalk css
      projectName: "",
      messageList: [],
      tooltipDisable: false,
      keyWord: "",
      showTips: false,
      haveNewMesses: false, //是否有新的办事人等待服务，1-是，2-否
      haveTurnService: false, //是否有转派服务等待处理，1-是，2-否
      turnRecordId: "", //转派记录编号
      imgUrl: "",
      serviceTransfer: false,
      essentialinformation: {}, //转派服务详情
      operateServiceType: null,
      rollbackMemo: "",
      showPersonalCenter: false,
      showPolicy:false,
      eleShow:false,
    };
  },
  computed: {
    basicUserInfo() {
      return this.$store.state.user.basicUserInfo;
    },
  },
  created() {
    this.getOnline();
  },
  mounted() {
    if (this.basicUserInfo.userType === "2") {
      setInterval(() => {
        this.getMessage();
      }, 5000);
    }
  },
  methods: {
    closePersonalCenter() {
      this.showPersonalCenter = false;
    },
    jumpOutLink(name){
      if(name == '一业一证'){
        window.location.href ='https://zwdt.sh.gov.cn/govPortals/column/ot/yyyz.html'; 
        return
      }
      if(name == '一件事'){
        window.location.href ='https://zwdt.sh.gov.cn/qykj/guide_hp/yslb'; 
        return
      }
      if(name == '政策'){
        this.showPolicy = true;
        return
      }
    },

    //个人中心
    openPersonalCenter() {
      this.showPersonalCenter = true;
    },
    async openScan(){
      // this.visible = false;
      this.imgUrl = await QRCode.toDataURL('https://onlineserve.shhuangpu.gov.cn/bangban/getNumber?workUserIds=' +this.basicUserInfo.id + '&groupId=' + this.basicUserInfo.groupId);
      this.eleShow = true;
    },
    //服务接收
    operateService(type) {
      const that = this;
      that.operateServiceType = type;
      const data = {
        turnRecordId: that.turnRecordId,
        turnStatus: type,
        rollbackMemo: that.rollbackMemo,
      };
      if (type === "3" && data.rollbackMemo === "") {
        that.$message.warning("请填写拒绝原因");
        return;
      }
      serviceAccept(data).then((res) => {
        if (res.code === 200) {
          if (type === "2") {
            that.$store.commit("setAcceptService", true);
            that.$message.success("服务转派成功");
          } else {
            that.$message.success("你拒绝服务转派");
          }
          this.getMessage();
          that.serviceTransfer = false;
        }
      });
    },

    //帮代办人员登录后，每1分钟发送一次请求，记录当前人员的登录服务时长
    getOnline() {
      setInterval(() => {
        online()
          .then((res) => {})
          .catch((err) => {});
      }, 60000);
    },
    getMessage() {
      const that = this;
      isHaveNewService().then((res) => {
        if (res.code === 200) {
          if (res.data.messesService.haveNewMesses === "1") {
            that.messageList = [];
            that.haveNewMesses = true;
            const obj = {
              message: "您好，您新增一位待服务客户",
              type: "messesService",
            };
            that.messageList.push(obj);
            this.$store.commit("setServiceOperateStatus", true);
          } else {
            that.haveNewMesses = false;
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
    toDeailTurnService(item) {
      this.serviceTransfer = true;
      this.turnRecordId = item.turnRecordId;
      this.$store.commit("setAcceptService", false);
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

    goDetailPage(index) {
      this.projectName = this.mainList[index].name;
      let linkList = this.$store.state.home.linkList;
      let curItem = this.mainList[index];
      const findIndex = linkList.findIndex((item) => item.id === curItem.oid);
      // 当前标题不存在于侧边浮动栏
      if (findIndex < 0) {
        linkList.push({
          id: curItem.oid,
          title: curItem.name,
        });
      }
      // this.$store.dispatch('home/updateLinkId', index);
      // this.$store.dispatch('home/updateLinkList', linkList);
      let appOid = curItem.oid;
      let routeLink = curItem?.loginSuccessAddr;
      let layoutName = curItem?.name;
      this.changeApp(appOid, routeLink, layoutName);
    },
    changeApp(appOid, routeLink, layoutName) {
      this.$store.dispatch("setAppOid", appOid);
      store
        .dispatch("GenerateRoutes", { routeLink, layoutName })
        .then((accessRoutes) => {
          router.addRoutes(accessRoutes); // 动态添加可访问路由表
          //next({ ...to, replace: true }) // hack方法 确保addRoutes已完成
          this.$router.push({ path: `${routeLink}` });
        });
      //关闭所有的主页面
      this.$store.dispatch("tagsView/delAllViews").then(({ visitedViews }) => {
        this.$router.push("/welcome");
      });
    },
    async logout() {
      this.$confirm("确定注销并退出系统吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        this.$store.dispatch("LogOut").then(() => {
          //location.reload()
          location.href = getLoginUrl();
        });
      });
    },
  },
};
</script>

<style lang="scss">
.navbar {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  overflow: hidden;
  position: relative;
  //background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);

  height: 100%;
  background: #fff;
  border-bottom-left-radius: 0.3125rem;

  .projectTitle {
    width: 23%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: flex-start;
    padding-left: 1.5625rem;

    img {
      // height: 90%;
      margin-right: 0.3125rem;
    }
  }

  .rightBlock {
    height: 100%;
    width: 77%;
    padding-right: 0.9375rem;
    display: flex;
    align-items: center;
    justify-content: flex-end;
    
    .outLink{
      width:9rem ;
      margin-right: 10px;
      height: 36px;
      line-height: 36px;
      color: black;
      border-radius: 2.5rem;
      img{
        vertical-align: middle;
        margin-right: 5px;
      }
      span{
        vertical-align: middle;
      }
    }
    .tipsArea {
      padding: 0 0.9375rem 0 0.3125rem;
      margin-right: 1.5rem;
      height: 2.5rem;
      background-color: #edf1f2;
      border-radius: 1.0714rem;
      font-size: 1.4rem;
      display: flex;
      align-items: center;
      justify-content: flex-start;

      img {
        width: 1.5rem;
        margin-right: 0.7857rem;
      }

      .el-carousel {
        min-width: 16.375rem;
        height: 100%;

        .el-carousel__container {
          width: auto;
          min-width: 25rem;
          height: 100%;

          .el-carousel__item {
            width: auto;
            height: 100%;
            display: flex;
            align-items: center;
            justify-content: flex-start;

            .textContent {
              padding: 0;
              margin: 0;
              font-size: 1.4rem;
              // width: 12rem;
              // height: 100%;
              flex: 1;
              text-align: center;
              line-height: 2.5rem;
              overflow: hidden;
              text-overflow: ellipsis;
              -o-text-overflow: ellipsis;
              -webkit-text-overflow: ellipsis;
              -moz-text-overflow: ellipsis;
              white-space: nowrap;
            }

            .detail {
              font-size: 1.4rem;

              &:nth-child(2) {
                color: blue;
                text-decoration: underline;
                cursor: pointer;
              }
            }
          }
        }

        .el-carousel__indicators {
          display: none;
        }
      }
    }

    .searchArea {
      width: 15.625rem;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin: 0 5%;

      .el-input {
        width: 100%;
        height: 2.5rem !important;

        .el-input__inner {
          height: 2.5rem !important;
          line-height: 2.5rem !important;
          border-radius: 1.25rem;
          background-color: #edf1f2;
        }

        .el-input__prefix {
          .el-input__icon {
            line-height: 2.5rem !important;
          }
        }

        input {
          &::-webkit-input-placeholder {
            font-size: 0.75rem;
          }
        }
      }
    }

    .notice {
      width: 2.5rem;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      position: relative;

      img {
        width: 1.25rem;
      }

      .rollIcon {
        position: absolute;
        width: 1.1rem;
        height: 1.1rem;
        border-radius: 50%;
        background: #00c475;
        border: 2px solid #fff;
        right: 0.25rem;
        top: 1.6667rem;
      }
    }

    .skinPeeler {
      width: 3rem;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;

      img {
        width: 2.5rem;
      }
    }

    .userArea {
      padding-right: 0.9375rem;
      height: 36px;
      display: flex;
      align-items: center;
      justify-content: flex-start;
      border-radius: 1.125rem;
      background-color: #edf1f2;

      .headPortrait {
        width: 36px;
        height: 36px;
        border-radius: 50%;
      }

      .center {
        height: 100%;
        padding: 0 0.625rem;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: space-around;

        p {
          padding: 0;
          margin: 0;
          font-size: 0.75rem;

          &:nth-child(1) {
            font-size: 0.75rem;
            font-family: PingFang;
            font-weight: 800;
            color: #3a3856;
          }

          &:nth-child(2) {
            font-size: 0.75rem;
            font-family: PingFang;
            font-weight: 500;
            color: #3a3856;
            line-height: 0.9375rem;
          }
        }
      }

      .avatar-container {
        width: 1.25rem;
        height: 100%;

        .avatar-wrapper {
          width: 100%;
          height: 100%;
          display: flex;
          align-items: center;
          justify-content: center;
        }
      }
    }
  }

  .projectArea {
    width: 60%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: flex-end;
    padding-right: 8%;

    div {
      height: 100%;
      width: auto;
      display: flex;
      align-items: center;

      p {
        width: auto;
        height: 100%;
        padding: 0;
        margin: 0;
        padding: 0 1.25rem;
        display: flex;
        align-items: center;
      }
    }
  }
}

.carousel {
  flex-grow: 1;
  min-width: 31.25rem;
  height: 3.125rem;
  overflow-y: hidden;
  display: inline-block;
  vertical-align: top;
  margin-left: 1.25rem;
}

.carousel-item {
  width: 100px;
  display: inline-block;
  vertical-align: middle;
  text-align: center;
  margin: 2px 0 0 0;
  padding-bottom: 7px;
  box-sizing: border-box;
  cursor: pointer;
  font-size: 16px;
  font-family: Microsoft YaHei;
  font-weight: 400;
  color: #cbf7e0;
  opacity: 0.7;
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
.codeWrap{
  width: 100%;
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
  img{
    width: 300px;
  }
}

.carousel-item-active {
  // background: rgba(12, 52, 44, .8);
  font-size: 18px;
  font-family: Microsoft YaHei;
  font-weight: 700;
  color: #ffffff;
  border-bottom: 4px solid rgb(78, 220, 159);
  box-sizing: border-box;
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

.gjx-dropdown {
  vertical-align: top;
  cursor: pointer;
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

.dialog-footer {
  .el-input {
    width: 24.2857rem;
    height: 3rem;
    margin-right: 2rem;
  }

  p {
    &:nth-child(2) {
      background: #ffffff;
      border: 1px solid #4988f2;
      box-shadow: 0px 0px 1.8125rem 0px rgba(204, 177, 121, 0.31);
      color: #2473ff;
      margin-right: 2.1875rem;
      cursor: pointer;
    }

    &:nth-child(3) {
      background: linear-gradient(90deg, #2473ff 0%, #56b1fd 100%);
      box-shadow: 0px 0px 1.5rem 0px rgb(40 107 198 / 31%);
      color: #ffffff;
    }
  }
}
.serviceTransfer {
  height: 80vh;

  .el-dialog__body {
    height: calc(100% - 3.75rem);
    max-height: 100%;
  }
}
</style>
