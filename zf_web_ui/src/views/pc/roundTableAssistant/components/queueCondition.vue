<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-08-08 17:48:32
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-10-18 17:19:29
 * @FilePath: \zf_web_ui\src\views\pc\roundTableAssistant\queueCondition.vue
 * @Description: 排队队列情况
-->
<template>
  <div class="intelligentFormFilling-container">
    <!-- <div class="tabHeader">
      <p class="tabLeft" @click="changeTab('1')" :class="tabType === '1' ? 'active' : ''">
        我的
      </p>
      <p class="tabRight" @click="changeTab('2')" :class="tabType === '2' ? 'active' : ''">
        全部
      </p>
    </div> -->
    <div class="body-content">
      <div class="tab1" v-if="userList.length">
        <div class="item-block" v-for="(item, index) in userList" :key="index">
          <div class="logoArea">
            <img
              :src="require('@/assets/images/home/headPortrait.png')"
              alt=""
            />
          </div>
          <div class="right">
            <div class="top">
              <p>
                <span>{{ item.name }}</span>
                <span class="turn" @click="turnService(item, index)">接待</span>
              </p>

              <p class="phone">
                <img :src="require('@/assets/images/home/phone.png')" alt="" />
                {{ item.phone }}
              </p>
              <p>
                <span>等待时长：{{ item.waitTime }}</span>
              </p>
            </div>
            <span>{{ item.createDate }}</span>
          </div>
        </div>
      </div>
      <div class="tab2" v-else>
        <p>暂无数据</p>

        <!-- <div class="header">
          <p>帮办人员</p>
          <p>所在组</p>
          <p>帮办类型</p>
          <p>当前状态</p>
          <p>正在服务人数</p>
          <p>等待人数</p>
        </div>
        <div class="content-block">
          <div class="item-block" v-for="(item, index) in userGroupList">
            <p>{{ item.name }}</p>
            <p>{{ item.groupName }}</p>
            <p>{{ item.haType === '1' ? '快捷帮办' : '圆桌帮办' }}</p>
            <p>{{ item.status === '1' ? '离线' : item.status === '2' ? '忙碌' : item.status === '3' ? '空闲' : '服务中' }}</p>
            <p>{{ item.inServiceNum }}</p>
            <p>{{ item.waitingNum }}</p>
          </div>
        </div> -->
      </div>
    </div>
  </div>
</template>
<script>
import {
  queryWorkQueueList,
  getAllUserService,
  workUserService,
} from "@/api/modules/helpAgent";
import { formatDate } from "@/utils/index";
import videoOperate from "@/utils/video";
export default {
  name: "QueueCondition",
  data() {
    return {
      tabType: "1",
      myservice: {},
      groupService: {},
      userList: [],
      userGroupList: [],
    };
  },
  mounted() {
    this.getQueryWorkQueueList();
  },
  mixins:[videoOperate],
  methods: {
    changeTab(type) {
      this.tabType = type;
    },
    getQueryWorkQueueList() {
      const data = {
        name: "",
        cardNo: "",
        companyName: "",
        queueStatus: "2",
        serviceStatus: "1",
      };
      queryWorkQueueList(data).then((res) => {
        if (res.code === 200) {
          this.userList = res.data;
        }
      });

      getAllUserService().then((res) => {
        if (res.code === 200) {
          this.userGroupList = res.data;
        }
      });
    },
    // getQueueNum() {
    //   getQueueNum().then(res => {
    //     if (res.code === 200) {
    //       this.myservice = res.data
    //     }
    //   })
    // }

    //接待用户
    turnService(data) {
      const params = {
        queueId: data.id,
      };
      workUserService(params).then((res) => {
        if (res.code === 200) {
          if (res.data) {
            console.log(res.data);
            if(res.data.takeNumberType == '5' && res.data.haVideoAccess.roomNumber){
              this.$emit("openVideo");
              this.startOpenVideo(res.data.haVideoAccess);  //开启视频咨询
            }
            if(res.data.takeNumberType == '4'){
              this.$emit('openshowAddDocument');
            }
            this.$store.commit("setServiceOperateStatus", true);
            if (!res.data.firstServiceBeginTime) {
              res.data.firstServiceBeginTime = formatDate(new Date());
              res.data.serviceBeginTime = res.data.firstServiceBeginTime
              // console.log(res.data.firstServiceBeginTime);
              // console.log(res.data.serviceBeginTime);
            }
            res.data.id = res.data.queueId;
            localStorage.setItem('baseUserInfo',res.data);
            this.$emit("closeQueueCondition");
            this.$store.commit("setBaseUserInfo", res.data);
          }
        }
      });
    },
  },
};
</script>
<style lang="scss" scoped>
p {
  padding: 0;
  margin: 0;
}

.intelligentFormFilling-container {
  width: 100%;
  height: 100%;
  background: #ffffff;
  border-radius: 1.75rem 2.125rem 2.125rem 1.75rem;
  padding: 1.625rem 2.8125rem 0 0.8125rem;

  .tabHeader {
    width: 100%;
    height: 4.375rem;
    background: #f3f6f8;
    border-radius: 2.1875rem;
    display: flex;
    align-items: center;

    p {
      width: 50%;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 20px;
      font-family: Source Han Sans CN;
      font-weight: 500;
      color: #373737;
      cursor: pointer;
    }

    .active {
      height: 3.75rem;
      background: linear-gradient(90deg, #2473ff 0%, #56b1fd 100%);
      border-radius: 1.875rem;
      color: #ffffff;
    }
  }

  .body-content {
    width: 100%;
    height: calc(100% - 4.375rem);
    padding-top: 2rem;

    .tab1 {
      width: 100%;
      height: 100%;
      display: flex;
      align-items: flex-start;
      justify-content: flex-start;
      flex-wrap: wrap;
      overflow-y: auto;

      &::-webkit-scrollbar {
        width: 0.4375rem;
        background-color: #fff;
      }

      &::-webkit-scrollbar-thumb {
        width: 0.4375rem;
        height: 0.625rem !important;
        background: linear-gradient(270deg, #2473ff 0%, #56b1fd 100%);
        border-radius: 4px;
      }

      .item-block {
        min-width: 250px;
        width: 24%;
        height: 13.625rem;
        background: #ffffff;
        border: 1px solid #f0e7d7;
        box-shadow: 0px 0px 1.5rem 0px rgba(77, 138, 242, 0.31);
        border-radius: 0.9375rem;
        padding: 1.6875rem;
        display: flex;
        align-items: center;
        justify-content: flex-start;
        margin-right: 1%;
        margin-bottom: 2rem;

        .logoArea {
          width: 30%;
          height: 100%;
          background: rgba(148, 189, 253, 0.56);
          box-shadow: 0px 0.4375rem 0px 0px rgba(106, 159, 231, 0.08);
          border-radius: 0.5rem;
          padding-top: 1rem;

          img {
            width: 100%;
            height: 100%;
          }
        }

        .right {
          width: 70%;
          height: 100%;
          padding-left: 1.4375rem;
          display: flex;
          flex-direction: column;
          align-items: flex-start;
          justify-content: space-between;

          .top {
            width: 100%;
            height: auto;
            display: flex;
            flex-direction: column;
            align-items: flex-start;
            justify-content: flex-start;

            p {
              padding: 0;
              margin: 0;

              &:nth-child(1) {
                width: 100%;
                height: 2.5rem;
                display: flex;
                align-items: center;
                justify-content: space-between;

                span {
                  font-size: 1.625rem;
                  font-family: Source Han Sans CN;
                  font-weight: 500;
                  color: #373737;
                  white-space: nowrap;
                  text-overflow: ellipsis;
                  overflow: hidden;
                }

                .turn {
                  cursor: pointer;
                  height: 2.5rem;
                  width: auto;
                  padding: 0.8rem 1rem;
                  border-radius: 1.8rem;
                  display: flex;
                  align-items: center;
                  justify-content: center;
                  background: linear-gradient(90deg, #2473ff 0%, #56b1fd 100%);
                  font-family: MicrosoftYaHei;
                  font-size: 1.5rem;
                  font-weight: normal;
                  font-stretch: normal;
                  letter-spacing: 0px;
                  color: #ffffff;
                }
              }
            }

            .phone {
              padding: 0;
              margin: 0;
              width: 100%;
              height: 2.375rem;
              margin-top: 1.5rem;
              background: rgba(148, 189, 253, 0.15);
              border-radius: 1.1875rem;
              display: flex;
              align-items: center;
              justify-content: flex-start;
              padding-left: 1.0625rem;
              font-size: 1.375rem;
              font-family: Source Han Sans CN;
              font-weight: 400;
              color: #4c89f1;

              img {
                width: 1.875rem;
                margin-right: 0.6875rem;
              }
            }
          }

          span {
            font-size: 1.25rem;
            font-family: Source Han Sans CN;
            font-weight: 400;
            color: #b4ac9d;
          }
        }
      }
    }

    .tab2 {
      width: 100%;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      // .header {
      //   width: 100%;
      //   height: 3.7143rem;
      //   display: flex;
      //   border-radius: 1.7857rem;
      //   background: #f3f6f8;

      //   p {
      //     padding: 0;
      //     margin: 0;
      //     width: 25%;
      //     height: 100%;
      //     font-size: 16px;
      //     font-family: Source Han Sans CN;
      //     font-weight: 500;
      //     color: #373737;
      //     display: flex;
      //     align-items: center;
      //     justify-content: center;
      //   }
      // }

      // .content-block {
      //   width: 100%;
      //   background: linear-gradient(180deg, #E2EBFA 0%, #fff 30%);
      //   box-shadow: 0px 0.2143rem 1.7143rem 0px rgba(4, 11, 31, 0.0500);
      //   height: auto;
      //   border-radius: 0.7143rem;
      //   padding: 3.5rem 1.4286rem 2rem 1.4286rem;
      //   margin-top: 0.5rem;

      //   .item-block {
      //     width: 100%;
      //     height: 3.7143rem;
      //     margin-top: 1.5rem;
      //     display: flex;
      //     border-radius: 1.7857rem;
      //     background: #f3f6f8;

      //     p {
      //       padding: 0;
      //       margin: 0;
      //       width: 25%;
      //       height: 100%;
      //       font-size: 16px;
      //       font-family: Source Han Sans CN;
      //       font-weight: 500;
      //       color: #373737;
      //       display: flex;
      //       align-items: center;
      //       justify-content: center;
      //     }
      //   }
      // }
    }
  }
}
</style>
