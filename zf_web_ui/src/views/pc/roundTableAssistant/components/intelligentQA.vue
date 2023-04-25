<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-08-19 18:14:37
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-09-30 17:30:23
 * @FilePath: \zf_web_ui\src\views\pc\intelligentQA\index.vue
 * @Description: 智能问答
-->
<template>
  <div class="intelligentQA">
    <div class="chatInformation">
      <div class="chartArea">
        <div class="chartInfoList" v-for="(item, index) in dataList">
          <div class="user">
            <p>
              办事人
              <img :src="require('@/assets/images/home/user.png')" alt="" />
            </p>
            <p>{{ item.name }}</p>
          </div>
          <div class="robot">
            <p class="userInfo">
              <img :src="require('@/assets/images/home/robot.png')" alt="" />
              系统
            </p>
            <div class="detail">
              <p class="tips" v-show="!item.matterList.length">
                暂未查询到相关事项
              </p>
              <p class="title" v-show="item.matterList.length">您是要办理：</p>
              <div class="matter" v-for="(ite, idx) in item.matterList">
                <p v-html="ite.name"></p>
                <p @click="handleImmediately(item, ite)">立即办理</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="inputArea">
      <el-input
        placeholder="请输入您想要咨询的内容"
        v-model="keyword"
        @keyup.enter.native="searchKey"
      >
      </el-input>
      <p class="searchBtn" @click="searchKey">发送</p>
    </div>
  </div>
</template>
<script>
import { knowledgeQuery } from "@/api/modules/knowledgeBase";
import { completeService } from "@/api/modules/helpAgent";
import { encode } from "@/utils/index";
export default {
  name: "IntelligentQA",
  data() {
    return {
      keyword: "",
      tooltipDisable: false,
      dataList: [],
      serviceMemo: [],
    };
  },
  mounted() {},
  computed: {
    staffInformation() {
      return this.$store.state.pageData.staffInformation;
    },
    currentServiceIndex() {
      return this.$store.state.pageData.currentServiceIndex;
    },
  },
  methods: {
    //立即办理
    handleImmediately(data, data2) {
      if (!this.staffInformation.length) {
        this.$message.warning("当前无服务用户，无法办理");
        return;
      }
      this.completeService(data, data2);
      this.$emit("setComponentName", "operationFlow", data2);
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
    searchKey() {
      const data = {
        keyword: this.keyword,
      };
      knowledgeQuery(data).then((res) => {
        if (res.code === 200) {
          const obj = {
            name: this.keyword,
            matterList: res.data ?? [],
          };
          this.dataList.push(obj);
          this.keyword = "";
        }
      });
    },
    //完成服务
    //服务完成
    completeService(params, data2) {
      const that = this;
      const obj = { question: `${params.name}`, answer: `${data2.name}` };
      this.serviceMemo.push(obj);
      let data = {
        workQueueId: that.staffInformation[that.currentServiceIndex].id,
        serviceType: "1",
        pushType: "",
        serviceMemo: encode(JSON.stringify(this.serviceMemo)),
        sxServiceId: params.serviceOid,
        qlCaseId: that.staffInformation[that.currentServiceIndex].caseOid,
        pushMemo: "",
        caseNumber:
          that.staffInformation[that.currentServiceIndex].caseNumber ?? "",
      };
      completeService(data).then((res) => {
        if (res.code === 200) {
          // this.$message.success('已完成服务')
        }
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.intelligentQA {
  width: 100%;
  height: 100%;
  background: #f3f5f8;
  padding: 1.4375rem 0 1.5625rem 0rem;

  .chatInformation {
    width: 100%;
    height: calc(100% - 7rem);
    padding: 0 2.875rem 0 2.5625rem;
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

    .chartArea {
      width: 100%;
      height: 100%;

      .chartInfoList {
        width: 100%;
        height: auto;

        .user {
          width: 100%;
          height: auto;
          display: flex;
          flex-direction: column;
          align-items: flex-end;

          p {
            padding: 0;
            margin: 0;

            &:nth-child(1) {
              width: 100%;
              height: auto;
              display: flex;
              align-items: center;
              justify-content: flex-end;
              font-size: 1.3125rem;
              font-family: Microsoft YaHei;
              font-weight: 400;
              color: #5e5e5e;

              img {
                width: 3.375rem;
                margin-left: 1.1875rem;
              }
            }

            &:nth-child(2) {
              display: flex;
              align-items: center;
              justify-content: center;
              width: auto;
              height: 4.375rem;
              padding: 0 1.875rem;
              margin-right: 4.5625rem;
              background: linear-gradient(90deg, #2473ff 0%, #56b1fd 100%);
              border: 1px solid #ffffff;
              box-shadow: 0px 0.1875rem 1.5rem 0px rgba(4, 11, 31, 0.05);
              border-radius: 0.625rem 0px 0.625rem 0.625rem;
              font-size: 1.375rem;
              font-family: Microsoft YaHei;
              font-weight: 400;
              color: #ffffff;
            }
          }
        }

        .robot {
          width: 100%;
          height: auto;

          .userInfo {
            width: 100%;
            height: auto;
            display: flex;
            align-items: center;
            font-size: 1.3125rem;
            font-family: Microsoft YaHei;
            font-weight: 400;
            color: #2c2c2c;

            img {
              width: 3.4375rem;
              margin-right: 1.1875rem;
            }
          }

          .detail {
            width: 90%;
            height: auto;
            background: #ffffff;
            box-shadow: 0px 0.1875rem 1.5rem 0px rgba(4, 11, 31, 0.05);
            border-radius: 0px 0.625rem 0.625rem 0.625rem;
            margin-left: 4.3125rem;
            padding: 1.375rem 1.875rem 1.4375rem 1.75rem;

            .tips {
              width: 100%;
              font-size: 1.4375rem;
              font-family: Microsoft YaHei;
              font-weight: 400;
              color: #4f4f4f;
              text-align: left;
              margin-bottom: 1.75rem;
            }

            .title {
              font-size: 1.4375rem;
              font-family: Microsoft YaHei;
              font-weight: 400;
              color: #4f4f4f;
              text-align: left;
              margin-bottom: 1.75rem;
            }

            .matter {
              width: 100%;
              height: auto;
              margin-bottom: 1.4375rem;
              background: rgba(255, 255, 255, 0.81);
              border: 1px solid #2473ff;
              box-shadow: 0px 0px 1.8125rem 0px rgba(204, 177, 121, 0.31);
              border-radius: 1.8125rem;
              display: flex;
              justify-content: flex-start;
              font-size: 1.7143rem;

              p {
                &:nth-child(1) {
                  margin: 0;
                  width: calc(100% - 120px);
                  height: auto;
                  display: flex;
                  align-items: center;
                  justify-content: flex-start;
                  flex-wrap: wrap;
                  margin-left: 12px;
                  padding: 5px 0;
                }

                &:nth-child(2) {
                  flex: 1;
                  display: flex;
                  align-items: center;
                  justify-content: center;
                  background: linear-gradient(90deg, #2473ff 0%, #56b1fd 100%);
                  border-radius: 0px 1.8125rem 1.8125rem 0px;
                  color: #fff;
                }
              }
            }
          }
        }
      }
    }
  }

  .inputArea {
    margin-top: 2.25rem;
    width: 100%;
    height: 4.75rem;
    padding: 0 4.375rem;
    display: flex;
    align-items: center;

    .el-input {
      width: calc(100% - 7.875rem);
      height: 100%;
      border: none;
      background: #ffffff;
      // box-shadow: 0px 0px 1.8125rem 0px rgba(204, 177, 121, 0.31);
      border-radius: 2.375rem;

      ::v-deep .el-input__inner {
        height: 100%;
        border-radius:2.375rem 0 0 2.375rem;
        // border-top-left-radius: 2.375rem;
        // border-bottom-left-radius: 2.375rem;

        &:focus {
          border: none;
        }
      }
    }

    .searchBtn {
      padding: 0;
      margin: 0;
      width: 7.875rem;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      background: linear-gradient(90deg, #2473ff 0%, #56b1fd 100%);
      box-shadow: 0px 0px 29px 0px rgba(204, 177, 121, 0.31);
      border-top-right-radius: 2.375rem;
      border-bottom-right-radius: 2.375rem;
      font-size: 1.75rem;
      font-family: Microsoft YaHei;
      font-weight: 400;
      color: #ffffff;
      text-shadow: 0px 0px 1.8125rem rgba(204, 177, 121, 0.31);
    }
  }
}
</style>
