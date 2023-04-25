<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-08-05 10:23:02
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-09-07 13:28:58
 * @FilePath: \zf_web_ui\src\views\pc\guideClothing\components\guideClothingHome.vue
 * @Description: 导服首页
-->
<template>
  <div class="main-content">
    <div class="area">
      <p>
        <img :src="require('@/assets/images/home/tv.png')" alt="" /> 来访人员:
      </p>
      <div class="main">
        <div class="main-block">
          <p>来访人员{{ queueNum.totalServiceNum }}人</p>
        </div>
      </div>
    </div>
    <div class="area">
      <p>
        <img :src="require('@/assets/images/home/tv.png')" alt="" />
        当前服务排队情况:
      </p>
      <div class="main">
        <ul class="queuingSituation">
          <li>
            <p class="leftBg">
              <img
                :src="require('@/assets/images/pad/waitingForAllocation.png')"
                alt=""
              />
            </p>
            <div>
              <p>
                <span>{{ queueNum.windowWaitNum }}</span
                >人
              </p>
              <p>等待分配</p>
            </div>
          </li>
          <li>
            <p class="leftBg">
              <img
                :src="require('@/assets/images/pad/quickAssistant.png')"
                alt=""
              />
            </p>
            <div>
              <p>
                <span>{{ queueNum.quickWaitNum }}</span
                >人
              </p>
              <p>快捷帮办</p>
            </div>
          </li>
          <li>
            <p class="leftBg">
              <img
                :src="require('@/assets/images/pad/roundTableAssistant.png')"
                alt=""
              />
            </p>
            <div>
              <p>
                <span>{{ queueNum.tableWaitNum }}</span
                >人
              </p>
              <p>圆桌帮办</p>
            </div>
          </li>
          <li>
            <p class="leftBg">
              <img
                :src="require('@/assets/images/pad/serviceQueue.png')"
                alt=""
              />
            </p>
            <div>
              <p>
                <span>{{ queueNum.windowWaitNum }}</span
                >人
              </p>
              <p>窗口</p>
            </div>
          </li>
        </ul>
      </div>
    </div>
    <div class="area">
      <p>
        <img :src="require('@/assets/images/home/tv.png')" alt="" />
        截止目前服务完成情况:
      </p>
      <div class="main">
        <div class="completion">
          <div class="item">
            <p class="title">快捷帮办</p>
            <div>
              <p class="imgArea">
                <img :src="require('@/assets/images/pad/edit.png')" alt="" />
              </p>
              <span>3</span>条
            </div>
          </div>
          <div class="item">
            <p class="title">圆桌帮办</p>
            <div>
              <p class="imgArea">
                <img
                  :src="require('@/assets/images/pad/serviceCompleted.png')"
                  alt=""
                />
              </p>
              <span>5</span>条
            </div>
          </div>
          <div class="item">
            <p class="title">窗口</p>
            <div>
              <p class="imgArea">
                <img :src="require('@/assets/images/pad/save.png')" alt="" />
              </p>
              <span>58</span>条
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { getQueueNum } from "@/api/modules/guideService";
export default {
  name: "GuideClothingHome",
  data() {
    return {
      queueNum: {},
    };
  },
  mounted() {
    this.getQueueNum();
  },
  methods: {
    getQueueNum() {
      getQueueNum().then((res) => {
        if (res.code === 200) {
          this.queueNum = res.data;
        }
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.main-content {
  width: 100%;
  height: 100%;
  background-color: #fff;
  border-radius: 1.75rem 2.125rem 2.125rem 1.75rem;
  padding: 1.5625rem 2.5rem 3.375rem 1.5rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;

  .area {
    width: 100%;
    height: 30%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-between;

    p {
      width: 100%;
      height: 1.5rem;
      display: flex;
      align-items: center;
      justify-content: flex-start;
      font-size: 1.375rem;
      font-family: PingFang SC;
      font-weight: bold;
      color: #333333;
      margin: 0;

      img {
        width: 2.1875rem;
        margin-right: 0.8125rem;
      }
    }

    .main {
      width: 100%;
      padding: 0 2.5rem 0 2.3125rem;
      height: calc(100% - 3.5rem);

      .main-block {
        width: 100%;
        height: 100%;
        background: url("@/assets/images/home/visitorsBg.png") no-repeat;
        background-size: 100% 100%;
        display: flex;
        align-items: center;
        padding-left: 30%;

        p {
          padding: 0;
          margin: 0;
          color: #316ed5;
          font-size: 3.5rem;
        }
      }

      .queuingSituation {
        display: flex;
        align-items: center;
        justify-content: flex-start;
        width: 100%;
        height: 100%;
        background: linear-gradient(
          180deg,
          RGBA(225, 236, 252, 1) 0%,
          #fff 30%
        );
        border-radius: 0.5rem;
        box-shadow: 0px 0.4375rem 0px 0px rgba(106, 159, 231, 0.08);
        padding: 0;

        li {
          padding: 0;
          margin: 0;
          list-style: none;
          width: 25%;
          height: 100%;
          display: flex;
          align-items: center;
          justify-content: flex-start;
          padding-left: 3.4375rem;

          .leftBg {
            width: 5rem;
            height: 5rem;
            padding: 0;
            margin: 0;
            background: url("../../../../assets/images/pad/polygon.png")
              no-repeat;
            background-size: 100% 100%;
            margin-right: 1.1875rem;
            display: flex;
            align-items: center;
            justify-content: center;

            img {
              width: 1.9375rem;
              margin: 0;
              padding: 0;
            }
          }

          div {
            height: 100%;
            width: auto;
            display: flex;
            align-items: center;
            justify-content: center;
            flex-direction: column;
            padding: 5vh 0;

            p {
              font-size: 1.6875rem;
              font-family: Microsoft YaHei;
              font-weight: 400;
              color: #383f58;

              &:nth-child(1) {
                margin-bottom: 3rem;
              }

              span {
                font-size: 3.3125rem;
                font-family: DIN;
                font-weight: bold;
                color: #4c89f1;
                margin-right: 0.8125rem;
              }
            }
          }
        }
      }

      .completion {
        width: 100%;
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: space-between;

        .item {
          width: 32%;
          height: 100%;
          background: #fdfeff;
          border: 1px solid #e9eff5;
          box-shadow: 0px 0.4375rem 0px 0px rgba(106, 159, 231, 0.08);
          border-radius: 0.625rem;
          display: flex;
          flex-direction: column;
          align-items: flex-start;
          justify-content: center;
          padding-left: 5%;

          p {
            font-size: 1.8125rem;
            font-family: Microsoft YaHei;
            font-weight: 400;
            color: #333333;
            display: flex;
            justify-content: space-between;
            height: 4.5625rem;
            width: 100%;
            align-items: flex-start;
          }

          div {
            width: 100%;
            height: 4.4375rem;
            display: flex;
            align-items: center;
            justify-content: flex-start;

            .imgArea {
              width: 4.4375rem;
              height: 4.4375rem;
              background: rgba(213, 236, 255, 0.51);
              box-shadow: inset 0px -7px 0px 0px rgba(106, 159, 231, 0.08);
              border-radius: 50%;
              display: flex;
              align-items: center;
              justify-content: center;
              margin-right: 1.75rem;

              img {
                width: 2.125rem;
              }
            }

            span {
              font-size: 2.6875rem;
              font-family: DIN;
              font-weight: 500;
              color: #333333;
              margin-right: 3.125rem;
            }
          }
        }
      }
    }
  }
}
</style>
