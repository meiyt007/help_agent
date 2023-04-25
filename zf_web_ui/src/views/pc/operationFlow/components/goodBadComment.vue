<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-08-04 16:40:14
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-09-07 13:29:34
 * @FilePath: \zf_web_ui\src\views\pc\operationFlow\components\goodBadComment.vue
 * @Description: pc好差评页面
-->
<template>
  <div
    :class="
      routerName === 'goodBadComment'
        ? 'goodBadComment-container'
        : 'goodBadComment'
    "
  >
    <div class="content">
      <div class="topArea">
        <div class="userPhoto">
          <img :src="basicUserInfo.image" alt="" />
        </div>
        <div class="evaInformation">
          <p class="title">{{ baseUserInfo.specificMatters.serviceName }}</p>
          <p class="matter">
            办事编号：
            <span>{{ basicUserInfo.id }}</span>
            到场次数：
            <span>1次</span>
          </p>
          <div class="overallEvaluation">
            <p>“帮办”满意度评价</p>
            <el-rate
              v-model="evalScore"
              disabled
              show-score
              text-color="#ff9900"
              score-template="{ value }"
            >
            </el-rate>
            <!-- <el-rate v-model="evalScore" disabled score-template="{evalScore}" :colors="colors"> -->
            <!-- </el-rate> -->
          </div>
        </div>
      </div>
      <div class="evaContent">
        <p class="tips">
          感谢您的评价，您可以在下列预设的理由中进行评分也可以输入其他理由，以帮助相关部门及其工作人员持续改进服务质量，提高服务水平。
        </p>
        <div class="evaArea" v-for="(item, index) in evaluateList">
          <div class="left">
            <p>{{ item.name }}</p>
            <p>{{ item.memo }}</p>
          </div>
          <el-rate v-model="item.fraction" :colors="colors" ></el-rate>
        </div>
        <div class="opinion">
          <p class="title">内容评价</p>
          <div class="fillOption">
            <el-input
              type="textarea"
              :autosize="{ minRows: 5, maxRows: 4 }"
              placeholder="请输入内容"
              v-model="evaluateContainer"
            >
            </el-input>
            <p @click="submit">提交评价</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import {
  evalGetItem,
  saveEvalResult,
  getWorkUserEvalScore,
} from "@/api/modules/evaluate.js";
import { formatDate } from "@/utils/index";
export default {
  name: "GoodBadComment",
  props: {
    staffInformation: {
      type: Array,
      default: () => [],
    },
    currentServiceIndex: {
      type: Number,
      default: () => 0,
    },
    baseUserInfo: {
      type: Object,
      default: () => {},
    },
  },

  data() {
    return {
      endTime: "",
      evaluateList: [],
      evalScore: 0,
      evaluateContainer: "",
      routerName: "goodBadComment",
      colors: ["#99A9BF", "#F7BA2A", "#FF9900"],
      texts: ["极差", "失望", "一般", "满意", "非常满意"],
    };
  },
  computed: {
    basicUserInfo() {
      return this.$store.state.user.basicUserInfo;
    },
  },
  created() {
    this.routerName = this.$router.currentRoute.name;
  },
  mounted() {
    this.getItem();
    this.getWorkUserEvalScore();
  },
  methods: {
    submit() {
      const params = {
        evalContent: this.evaluateContainer,
        queueId: this.baseUserInfo.id,
        result: [],
      };
      this.evaluateList.forEach((item) => {
        const obj = {
          evalItemId: item.id,
          evalItemScore: item.fraction,
          evalContent: item.memo,
        };
        params.result.push(obj);
      });
      saveEvalResult(params).then((res) => {
        if (res.code === 200) {
          this.$message.success("评价成功");
          this.$emit("spliceStaffInformation", this.currentServiceIndex);
        }
      });
    },
    getNowTime() {},
    getItem() {
      evalGetItem().then((res) => {
        if (res.code === 200) {
          this.evaluateList = [];
          if (res.data.length) {
            res.data.forEach((item) => {
              item.fraction = "5";
              this.evaluateList.push(item);
            });
            this.$nextTick(() => {
              this.evaluateList = [...this.evaluateList];
            });
          }
        }
      });
    },

    getWorkUserEvalScore() {
      getWorkUserEvalScore().then((res) => {
        if (res.code === 200) {
          this.evalScore = res.data.evalScore;
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

.goodBadComment-container {
  width: 100%;
  height: 100%;
  padding: 2.625rem 1.75rem 2.875rem 2rem;
  box-sizing: border-box;
}

.goodBadComment {
  width: 100%;
  height: 100%;
  box-sizing: border-box;
}

.content {
  width: 100%;
  height: 100%;
  background-color: #fff;
  border-radius: 1.75rem 2.125rem 2.125rem 1.75rem;
  padding: 3.0625rem 13.25rem 6.25rem 10.3125rem;

  .topArea {
    width: 100%;
    height: 10.625rem;
    display: flex;
    align-items: center;
    justify-content: flex-start;

    .userPhoto {
      width: 8.8125rem;
      height: 10.5625rem;
      background: #e7e5ed;
      border: 0.3125rem solid #6f7cc7;
      border-radius: 0.5rem;

      img {
        width: 100%;
        height: 100%;
      }
    }

    .evaInformation {
      flex: 1;
      height: 100%;
      padding-left: 1.6875rem;
      padding-top: 0.4125rem;

      .title {
        font-size: 1.625rem;
        font-family: Microsoft YaHei;
        font-weight: 400;
        color: #333333;
        margin-bottom: 0.5rem;
      }

      .matter {
        font-size: 1rem;
        font-family: Microsoft YaHei;
        font-weight: 400;
        color: #999999;
        margin-bottom: 0.5rem;
      }

      .overallEvaluation {
        width: 100%;
        height: 5.0625rem;
        background: linear-gradient(90deg, #d5e2ff 0%, #e0ecff 100%);
        border-radius: 0.1875rem;
        display: flex;
        align-items: center;
        justify-content: flex-start;

        p {
          width: 21.875rem;
          font-size: 1.75rem;
          font-family: HYZongYiJ;
          font-weight: 600;
          color: #3f59ef;
        }

        .el-rate {
          height: 100%;
          flex: 1;
          display: flex;
          align-items: center;

          ::v-deep .el-rate__item {
            font-size: 3.125rem;

            .el-rate__icon {
              font-size: 3.125rem;
            }
          }

          ::v-deep .el-rate__text {
            // display: inline-block;
            // width: 7.8125rem;
            // height: 2.8125rem;
            // background: #0252ff;
            // border-radius: 0.1875rem;
            text-align: center;
            line-height: 2.8125rem;
            font-size: 1.25rem;
            font-family: PingFang SC;
            font-weight: 400;
            // color: #ffffff !important;
          }
        }
      }
    }
  }

  .evaContent {
    width: 100%;
    padding-top: 4.625rem;

    .tips {
      font-size: 1.0625rem;
      font-family: Microsoft YaHei;
      font-weight: 400;
      color: #553a05;
      text-align: left;
      margin-bottom: 1.125rem;
    }

    .evaArea {
      width: 100%;
      height: 6.125rem;
      background: #eeebe4;
      opacity: 0.5;
      border-radius: 0.25rem;
      display: flex;
      align-items: center;
      justify-content: flex-start;
      margin-bottom: 1.5625rem;

      .left {
        height: 100%;
        width: 35%;
        padding: 1.3125rem 1.6875rem 1rem 1.6875rem;
        display: flex;
        align-items: flex-start;
        flex-direction: column;

        p {
          &:nth-child(1) {
            font-size: 1.625rem;
            font-family: Microsoft YaHei;
            font-weight: 400;
            color: #333333;
          }

          &:nth-child(2) {
            font-size: 1.125rem;
            font-family: Microsoft YaHei;
            font-weight: 400;
            color: #3d4048;
          }
        }
      }

      .el-rate {
        height: 100%;
        flex: 1;
        display: flex;
        align-items: center;

        ::v-deep .el-rate__item {
          font-size: 3.125rem;

          .el-rate__icon {
            font-size: 3.125rem;
          }
        }
      }
    }

    .opinion {
      .title {
        font-size: 1.375rem;
        font-family: Microsoft YaHei;
        font-weight: 400;
        color: #3b36a3;
        text-align: left;
      }

      .fillOption {
        width: 100%;
        height: 7.375rem;
        display: flex;
        align-items: center;
        justify-content: space-between;

        .el-textarea {
          width: calc(100% - 14rem);
          height: 7.375rem;

          ::v-deep .el-textarea__inner {
            height: 100%;
          }
        }

        P {
          width: 12.125rem;
          height: 7.375rem;
          background: linear-gradient(270deg, #2473ff 0%, #56b1fd 100%);
          background-size: 100%;
          text-align: center;
          line-height: 7.375rem;
          font-size: 2.0833rem;
          font-family: Microsoft YaHei;
          font-weight: 400;
          color: #ffffff;
          border-radius: 0.5rem;
        }
      }
    }
  }
}
</style>
