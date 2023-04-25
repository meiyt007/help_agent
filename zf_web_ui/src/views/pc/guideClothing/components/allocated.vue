<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-08-05 11:53:45
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-09-07 13:28:53
 * @FilePath: \zf_web_ui\src\views\pc\guideClothing\components\allocated.vue
 * @Description: 待分配人员列表
-->
<template>
  <div class="main-content" v-loading="loading">
    <div class="content" v-if="userList.length">
      <div class="item-block" v-for="(item, index) in userList" :key="index">
        <div class="logoArea">
          <img :src="require('@/assets/images/home/headPortrait.png')" alt="" />
        </div>
        <div class="right">
          <div class="top">
            <p>
              <span>{{ item.name }}</span>
              <span class="turn" @click="toDetailUser(item, index)">分配</span>
            </p>

            <p class="phone">
              <img :src="require('@/assets/images/home/phone.png')" alt="" />
              {{ item.phone }}
            </p>
          </div>
          <span>{{ item.createDate }}</span>
        </div>
      </div>
    </div>
    <div class="content" v-else>
      <p class="noData">暂无数据</p>
    </div>
  </div>
</template>
<script>
import { queryWorkQueueList } from "@/api/modules/guideService";
export default {
  name: "Allocated",
  data() {
    return {
      loading: false,
      userList: [],
    };
  },
  mounted() {
    this.getQueryWorkQueueList();
  },
  methods: {
    getQueryWorkQueueList() {
      const data = {
        name: "",
        cardNo: "",
        companyName: "",
        queueStatus: "2",
        serviceStatus: "2",
      };
      this.loading = true;
      queryWorkQueueList(data)
        .then((res) => {
          if (res.code === 200) {
            this.loading = false;
            this.userList = res.data;
          }
        })
        .catch((err) => {
          console.log(err);
          this.loading = false;
        });
    },
    toDetailUser(item) {
      this.$emit("userDetail", "baseInfo", item);
    },
  },
};
</script>
<style lang="scss" scoped>
.main-content {
  width: 100%;
  height: 100%;
  padding: 3.1875rem 3rem;
  background-color: #fff;

  .content {
    width: 100%;
    height: 100%;

    border-radius: 1.75rem 2.125rem 2.125rem 1.75rem;

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
      // background: linear-gradient(270deg, #bf9e63 0%, #dfca98 100%);
      background: linear-gradient(270deg, #2473ff 0%, #56b1fd 100%);
      border-radius: 4px;
    }

    .noData {
      width: 100%;
      display: flex;
      justify-content: center;
    }

    .item-block {
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
      margin-bottom: 2vh;

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
          font-size: 1.25rem;
          font-family: Source Han Sans CN;
          font-weight: 400;
          color: #b4ac9d;
        }
      }
    }
  }
}
</style>
