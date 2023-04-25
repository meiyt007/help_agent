<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-08-08 19:16:02
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-09-27 16:14:45
 * @FilePath: \zf_web_ui\src\views\pc\roundTableAssistant\serviceQueue.vue
 * @Description: 服务队列
-->
<template>
  <div class="body-content">
    <div class="tab1">
      <div
        class="item-block"
        v-for="(item, index) in userList"
        :key="index"
        @click="toDetailUser(item)"
      >
        <div class="logoArea">
          <img :src="require('@/assets/images/home/headPortrait.png')" alt="" />
        </div>
        <div class="right">
          <div class="top">
            <span style="display: flex;">
              <div style="font-size: 1.625rem">{{ item.name }}</div>
              <div v-if="item.agentTakeNumber"
                   style="margin-left: auto;font-size: 1.625rem">
                号票:{{ item.agentTakeNumber }}
              </div>
            </span>
            <p class="phone">
              <img :src="require('@/assets/images/home/phone.png')" alt="" />
              {{ item.phone }}
            </p>
          </div>
          <span>
            <img :src="require('@/assets/images/home/come.png')" alt="" />
            {{ item.createDate }}</span
          >
          <span v-if="item.serviceBeginTime">
            <img :src="require('@/assets/images/home/begin.png')" alt="" />
            {{ item.serviceBeginTime }}</span
          >
<!--          v-if="item.adviseMemo"-->
          <span  style="height:30%;max-width: 90%;overflow:hidden;text-overflow:ellipsis;    display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 2;"
                :title="item.adviseMemo">
           备注：{{item.adviseMemo}}</span
          >
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { queryWorkQueueList } from "@/api/modules/helpAgent";
export default {
  name: "ServiceQueue",
  props: {
    serviceStatus: {
      type: String,
      default: () => "2",
    },
  },
  data() {
    return {
      userList: [],
    };
  },
  mounted() {
    this.queryWorkQueueList();
  },
  methods: {
    queryWorkQueueList() {
      const data = {
        name: "",
        cardNo: "",
        companyName: "",
        queueStatus: "2",
        serviceStatus: this.serviceStatus,
      };
      queryWorkQueueList(data).then((res) => {
        if (res.code === 200) {
          this.userList = res.data;
        }
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.body-content {
  width: 100%;
  height: 100%;
  padding-top: 2rem;

  .tab1 {
    width: 100%;
    height: 100%;
    display: flex;
    flex-wrap: wrap;
    align-items: flex-start;
    justify-content: flex-start;
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
      //  height: 13.5714rem;
      height: 14.5714rem;
      background: #ffffff;
      border: 1px solid #f0e7d7;
      box-shadow: 0px 0px 1.5rem 0px rgba(77, 138, 242, 0.31);
      border-radius: 0.9375rem;
      padding: 1.5rem 0.75rem;
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

          span {
            font-size: 1.625rem;
            font-family: Source Han Sans CN;
            font-weight: 500;
            color: #373737;
          }

          .phone {
            padding: 0;
            margin: 0;
            width: 100%;
            height: 2.375rem;
            margin-top: 1rem;
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
          width: 100%;
          display: flex;
          align-items: center;
          justify-content: flex-start;
          flex-wrap: wrap;
          overflow: hidden;
          font-size: 1.25rem;
          font-family: Source Han Sans CN;
          font-weight: 400;
          color: #b4ac9d;

          img {
            width: 1.5rem;
            margin-right: 0.5rem;
          }
        }
      }
    }
  }
}
</style>
