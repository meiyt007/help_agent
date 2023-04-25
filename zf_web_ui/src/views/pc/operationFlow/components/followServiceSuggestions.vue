<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-08-12 16:42:18
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-09-07 13:14:56
 * @FilePath: \zf_web_ui\src\views\pc\operationFlow\components\followServiceSuggestions.vue
 * @Description: 
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
      <div class="chooseArea" v-if="nextServiceAdvise == '4'">
        <p class="title">申请单位名称：</p>
        <el-input class="chooseOption" v-model="query.companyName" placeholder="请输入单位名称"></el-input>
      </div>
      <div class="chooseArea" v-if="nextServiceAdvise == '4'">
        <p class="title">统一社会信用代码：</p>
        <el-input class="chooseOption" v-model="query.companyCode" placeholder="请输入统一社会信用代码"></el-input>
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
      <p @click="toLastStep">取消</p>
      <p @click="submit">保存</p>
    </div>
  </div>
</template>
<script>
import { endService,saveDesk } from "@/api/modules/helpAgent";
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
        { label: "一桌联办", value: "4" },
      ],
      options2: [
        { label: "随机分配帮办人员", value: "1" },
        { label: "指定帮办人员", value: "2" },
      ],
      query:{
        companyName:'',
        companyCode:'',
        queueId:this.baseUserInfo.id,
        deskStatus:0,
        appDate:'',
        workUserId:this.$store.state.user.basicUserInfo.id,
      }
    };
  },
  computed: {},
  methods: {
    changeOptions() {
      console.log(this.baseUserInfo);
      // console.log(this.$store.state.user.basicUserInfo)
    },
    toLastStep() {
      this.$emit("nextStep", "baseInfo");
    },
    submit() {
      if(this.nextServiceAdvise == '4'){
        if(!this.query.companyCode || !this.query.companyName){
          this.$message.warning("信息填写不完整");
          return;
        }else{
          let obj = this.query;
          obj['desc'] = this.adviseMemo;
          obj['workUserName'] = this.$store.state.user.basicUserInfo.name;
          saveDesk(obj).then(res=>{
          })
        }
      }
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
  background-color: #fff;
  padding-top: 2rem;
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
      font-size: 18px;
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

        ::v-deep .el-radio {
          .el-radio__label {
            font-size: 18px;
          }
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
      cursor: pointer;
      padding: 0 3.75rem;
      width: auto;
      height: 5.1667rem;
      background: #ffffff;
      border: 1px solid #4988f2;
      box-shadow: 0px 0px 1.5rem 0px rgba(40, 107, 198, 0.31);
      border-radius: 2.5833rem;
      font-size: 1.8333rem;
      font-family: Microsoft YaHei;
      font-weight: 400;
      color: #2473ff;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 2.1875rem;

      &:nth-child(2) {
        color: #ffffff;
        background: linear-gradient(90deg, #2473ff 0%, #56b1fd 100%);
        box-shadow: 0px 0px 1.5rem 0px rgba(40, 107, 198, 0.31);
      }
    }
  }
}
</style>
