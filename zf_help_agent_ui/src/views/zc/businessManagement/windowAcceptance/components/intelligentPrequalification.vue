<template>
  <div class="dialogContent" v-loading="dataLoading">
    <div class="container">
      <p v-if="!auditSuccessful" class="title">
        申请的【道路货物匀速经营许可新办】系统智能审核不通过！
      </p>
      <p v-if="auditSuccessful" class="title passTitle">
        智能预审通过，可以在线提交申请
      </p>
      <p class="reason" v-if="!auditSuccessful">
        <span>不通过原因：</span> <span>{{ message }}</span>
      </p>
      <div class="imageArea">
        <img
          v-if="auditSuccessful"
          :src="require('@/assets/image/intelligent/approved.png')"
          alt=""
        />
        <img
          v-if="!auditSuccessful"
          :src="require('@/assets/image/intelligent/failPass.png')"
          alt=""
        />
      </div>
    </div>
    <div class="footBtn">
      <el-button type="primary" @click="lastStep">上一步</el-button>
      <el-button type="primary" @click="nextStep"
        >下一步</el-button
      >
      <!-- <el-button v-if="!auditSuccessful" type="primary" @click="close"
        >好的，知道了</el-button
      > -->
    </div>
  </div>
</template>

<script>
import {
  listSxConditionalRules,
  executeInterApiCode
} from "@/api/zc/businessManagement/windowAcceptance";
export default {
  name: "intelligentPrequalification",
  props: {
    ruleForm: {
      type: Object,
      default: {}
    },
    cegisterType: {
      type: String,
      default: "0"
    },
    serviceOid: {
      type: String,
      default: ""
    }
  },

  data() {
    return {
      dataLoading: false,
      auditSuccessful: false,
      message: ""
    };
  },
  mounted() {
    this.getList();
  },
  activated() {
    this.getList();
  },
  methods: {
    getList() {
      this.dataLoading = true;
      let formData = new FormData();
      formData.append("serviceOid", this.serviceOid);
      listSxConditionalRules(formData)
        .then(res => {
          this.dataLoading = false;
          if (res.code === 200) {
            if (res.data.length) {
              res.data.forEach(item => {
                executeInterApiCode(item.interApiCode, {
                  sxServiceOid: item.serviceOid,
                  type: this.cegisterType,
                  uniqueCode: this.ruleForm.credentialNumber
                }).then(res => {
                  if (res.code === 200) {
                    if (res.data.data) {
                      this.auditSuccessful = true;
                    } else {
                      this.auditSuccessful = false;
                      this.message = res.data.message;
                    }
                  } else {
                    this.auditSuccessful = false;
                    this.message = res.data.message;
                  }
                });
              });
            }
          }
        })
        .catch(err => {
          this.dataLoading = false;
        });
    },
    lastStep() {
      this.$emit("lastStep", 4);
    },
    nextStep() {
      this.$emit("nextStep", 6);
    },
    close() {
      this.$emit("close");
    }
  }
};
</script>

<style lang="scss" scoped>
.dialogContent {
  padding: 35px 35px 0 35px;
  .container {
    width: 100%;
    height: 458px;
    background: #f7f9fc;
    padding-top: 85px;
    display: flex;
    flex-direction: column;
    align-items: center;
    // justify-content: center;
    .title {
      padding: 0;
      margin: 0;
      font-size: 30px;
      font-family: Microsoft YaHei;
      font-weight: bold;
      color: #3d7de6;
    }
    .passTitle {
      margin-bottom: 65px;
    }
    .reason {
      font-size: 18px;
      padding: 0;
      margin: 0;
      margin-top: 21px;
      margin-bottom: 39px;
      font-family: Microsoft YaHei;
      font-weight: 400;
      color: #333333;
      span {
        &:nth-child(1) {
          color: #333;
        }
        &:nth-child(2) {
          color: #ff8213;
        }
      }
    }
    .imageArea {
      width: 321px;
      border: 1px dashed;
      img {
        width: 100%;
      }
    }
  }
  .footBtn {
    width: 100%;
    height: 50px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-top: 80px;
  }
}
</style>
