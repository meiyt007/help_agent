<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-11-10 09:53:02
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-11-10 17:25:03
 * @FilePath: \zf_web_ui\src\views\pad\assistant\components\process\processComponents\followServiceSuggestions.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <div class="content-body">
    <p class="dialogContentTitle">帮办服务完成</p>
    <div class="containerArea">
      <div class="chooseArea">
        <p class="title">下次帮办业务建议：</p>
        <el-radio-group
          v-model.trim="nextServiceAdvise"
          class="chooseOption"
          @change="changeOptions"
        >
          <el-radio
            v-for="item in options1"
            :key="item.value"
            :label="item.value"
            >{{ item.label }}</el-radio
          >
        </el-radio-group>
      </div>
      <div class="chooseArea" v-if="nextServiceAdvise === '1'">
        <p class="title">后续服务建议：</p>
        <el-radio-group v-model.trim="distributionAdvise" class="chooseOption">
          <el-radio
            v-for="item in options2"
            :key="item.value"
            :label="item.value"
            >{{ item.label }}</el-radio
          >
        </el-radio-group>
      </div>
      <div class="remarks">
        <p class="title">备注：</p>
        <el-input
          type="textarea"
          :autosize="{ minRows: 5, maxRows: 4 }"
          placeholder="请输入内容"
          v-model="adviseMemo"
        >
        </el-input>
      </div>
    </div>
    <div class="guidance-foot">
      <p @click="toLastStep"><span>取消</span></p>
      <p @click="submit">保存</p>
    </div>
  </div>
</template>
<script>
import { endService } from "@/api/modules/helpAgent";
import { encode } from "@/utils/index";
export default {
  name: "FollowServiceSuggestions",
  props: {
    baseUserInfo: {
      type: Object,
      default: () => {},
    },
  },
  data() {
    return {
      nextServiceAdvise: this.baseUserInfo.nextServiceAdvise,
      distributionAdvise: this.baseUserInfo.distributionAdvise,
      adviseMemo: this.baseUserInfo.adviseMemo,
      options1: [
        { label: "待服务", value: "1" },
        { label: "窗口取号", value: "2" },
        { label: "完结", value: "3" },
      ],
      options2: [
        { label: "随机分配帮办人员", value: "1" },
        { label: "指定帮办人员", value: "2" },
      ],
    };
  },
  computed: {},
  methods: {
    changeOptions() {
      console.log(this.baseUserInfo.nextServiceAdvise);
    },
    toLastStep() {
      this.$emit("nextStep", "baseInfo");
    },
    submit() {
      this.$emit("setData", {
        nextServiceAdvise: this.nextServiceAdvise,
        distributionAdvise: this.distributionAdvise,
        adviseMemo: this.adviseMemo,
      });
      const data = {
        workQueueId: this.baseUserInfo.id,
        serviceType: this.baseUserInfo.serviceType,
        serviceMemo: this.baseUserInfo.specificMatters.serviceName
          ? encode(this.baseUserInfo.specificMatters.serviceName)
          : "",
        sxServiceId: this.baseUserInfo.specificMatters.serviceOid,
        qlCaseId: this.baseUserInfo.caseOid,
        pushMemo: "",
        nextServiceAdvise: this.baseUserInfo.nextServiceAdvise,
        distributionAdvise: this.baseUserInfo.distributionAdvise,
        adviseMemo: this.baseUserInfo.adviseMemo,
      };
      endService(data)
        .then((res) => {
          if (res.code === 200) {
            this.$emit("nextStep", "goodBadComment");
            this.$store.commit("setServiceOperateStatus", true);
          }
        })
        .catch((err) => {
          console.log(err);
          this.$emit("nextStep", "goodBadComment");
        });
    },
  },
};
</script>
<style lang="scss" scoped>
.content-body {
  width: 100%;
  height: 100%;
  overflow-y: auto;

  .dialogContentTitle {
    height: 2rem;
    line-height: 2rem;
    text-align: left;
    padding-left: 1.375rem;
    margin-left: 3rem;
    font-size: 1.625rem;
    font-family: Source Han Sans CN;
    font-weight: 500;
    color: #2a344c;
    position: relative;

    &::before {
      content: "";
      position: absolute;
      left: 0;
      top: 50%;
      transform: translateY(-50%);
      width: 0.5625rem;
      height: 1.375rem;
      background: linear-gradient(270deg, #2473ff 0%, #56b1fd 100%);
      border-radius: 0.3125rem;
    }
  }

  .containerArea {
    margin-top: 2rem;
    width: 100%;
    height: auto;
    padding-left: 10%;

    .title {
      width: 18rem;
      text-align: right;
      margin-right: 1rem;
      font-size: 1.4444rem;
      font-family: Source Han Sans CN;
      font-weight: 500;
      color: #2a344c;
    }

    .chooseArea {
      width: 90%;
      height: auto;
      margin-bottom: 2rem;
      display: flex;
      flex-wrap: wrap;

      .chooseOption {
        flex: 1;
        height: auto;
        display: flex;
        flex-wrap: wrap;
        align-items: center;
        justify-content: flex-start;

        .el-radio {
          min-width: 13.875rem;
          height: 3.9375rem;
          background: #edf0f4;
          border: 1px solid #c8cdd3;
          box-shadow: 0px 0.5rem 0px 0px rgba(106, 159, 231, 0.31);
          border-radius: 2.2143rem;
          display: flex;
          align-items: center;
          justify-content: flex-start;
          padding: 0 3rem 0 1.3125rem;
          font-size: 1.5rem;
          font-family: Source Han Sans CN;
          font-weight: 500;
          color: #3f3f3f;
          margin-right: 2.875rem;
          margin-bottom: 1.5rem;

          ::v-deep .el-radio__inner {
            display: inline-block;
            width: 1.625rem;
            height: 1.625rem;
            background: url("@/assets/images/pad/choose.png") no-repeat;
            background-size: 100% 100%;
          }

          // img {
          //   width: 1.625rem;
          //   margin-right: 0.6875rem;
          // }

          span {
            color: #3f3f3f;
          }
        }

        .is-checked {
          color: #ffffff;
          background: linear-gradient(
            90deg,
            #6d93e8 0%,
            #77b0fe 100%
          ) !important;
          // box-shadow: 0px 0.5rem 0px 0px rgba(106, 159, 231, 0.31);
          box-shadow: 0px 0.4375rem 0px 0px rgb(106 159 231 / 31%),
            inset 0 0.4375rem 0.4375rem 0 rgb(188 212 251),
            inset -0.4375rem 0 0 0 rgb(114 173 249),
            inset 7px 0 0 0 rgb(107 146 230) !important;
          border: none !important;

          ::v-deep .el-radio__label {
            color: #fff;
          }

          ::v-deep .el-checkbox__label {
            color: #fff;
          }

          ::v-deep .el-checkbox__inner {
            background: #fff;

            &:after {
              border-color: #6090e3;
            }
          }

          ::v-deep .el-radio__inner {
            background: url("@/assets/images/pad/chooseActivate.png") no-repeat;
            background-size: 100% 100%;

            &:after {
              display: none;
            }
          }
        }

        .el-checkbox {
          min-width: 13.875rem;
          height: 3.9375rem;
          background: #edf0f4;
          border: 1px solid #c8cdd3;
          box-shadow: 0px 0.5rem 0px 0px rgba(106, 159, 231, 0.31);
          border-radius: 2.2143rem;
          display: flex;
          align-items: center;
          justify-content: flex-start;
          padding: 0 3rem 0 1.3125rem;
          font-size: 1.5rem;
          font-family: Source Han Sans CN;
          font-weight: 500;
          margin-right: 2.875rem;
          margin-bottom: 1.5rem;
        }

        p {
          min-width: 13.875rem;
          height: 3.9375rem;
          background: rgba(237, 240, 244, 0.55);
          border: 1px solid #c8cdd3;
          border-radius: 1.9375rem;
          display: flex;
          align-items: center;
          justify-content: flex-start;
          padding: 0 3rem 0 1.3125rem;
          font-size: 18px;
          font-family: Source Han Sans CN;
          font-weight: 500;
          color: #3f3f3f;
          margin-right: 2.875rem;
          margin-bottom: 2rem;

          img {
            width: 1.625rem;
            margin-right: 0.6875rem;
          }
        }

        .chooseActivate {
          background: linear-gradient(90deg, #2473ff 0%, #56b1fd 100%);
          box-shadow: 0px 0px 1.5rem 0px rgba(40, 107, 198, 0.31);
          border: none;
          color: #fff;
        }
      }
    }

    .remarks {
      width: 90%;
      height: auto;
      margin-top: 2.5rem;
      display: flex;
      flex-wrap: wrap;
      align-items: flex-start;
      justify-content: space-between;

      .el-textarea {
        flex: 1;
      }
    }
  }

  .guidance-foot {
    margin-top: 1.5rem;
    width: 100%;
    height: 6.375rem;
    display: flex;
    align-items: center;
    justify-content: center;

    p {
      padding: 1px !important;
      background: linear-gradient(#e1eeff, #0977ff) !important;
      box-shadow: 0px 0.5rem 0px 0px rgba(106, 159, 231, 0.08) !important;
      border-radius: 2.1875rem !important;
      font-size: 1.8571rem;
      font-family: Microsoft YaHei;
      font-weight: 400;
      color: #6890e7;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 2.1875rem;
      span {
        padding: 1.375rem 3.75rem;
        display: block;
        width: 100%;
        height: 100%;
        border-radius: 2.1875rem;
        background-color: #fff;
      }

      &:nth-child(2) {
        padding: 1.375rem 3.75rem !important;
        border: none;
        background: linear-gradient(90deg, #6d93e8 0%, #77b0fe 100%) !important;
        box-shadow: 0px 0.4375rem 0px 0px rgb(106 159 231 / 31%),
          inset 0 0.4375rem 0.4375rem 0 rgb(188 212 251),
          inset -0.4375rem 0 0 0 rgb(114 173 249),
          inset 7px 0 0 0 rgb(107 146 230) !important;
        color: #ffffff;
      }
    }
  }
}
</style>
