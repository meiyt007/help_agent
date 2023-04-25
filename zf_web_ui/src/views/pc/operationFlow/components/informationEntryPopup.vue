<template>
  <div class="content">
    <div class="dialog--title">用户申请信息</div>
    <div class="container">
      <el-form
        :model="baseFillInfo"
        class="baseFillInfo"
        :rules="baseFillInfoRules"
        ref="baseFillInfo"
        label-width="20rem"
      >
        <el-form-item label="用户申请类型：" prop="applyUserType">
          <el-radio-group
            style="width: 100%"
            v-model="baseFillInfo.applyUserType"
            placeholder="请选择"
            @change="changeCegisterType"
          >
            <el-radio
              v-for="item in cegisterTypeList"
              :key="item.value"
              :label="item.value"
            >
              {{ item.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="证件类型：" prop="credentialType">
          <el-select
            style="width: 100%"
            v-model="baseFillInfo.credentialType"
            placeholder="请选择"
          >
            <el-option
              v-for="item in certificateTypeList"
              :key="item.dictOid"
              :label="item.name"
              :value="item.dictOid"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          :label="
            baseFillInfo.applyUserType === '1'
              ? '申请人姓名：'
              : '申请单位名称：'
          "
          prop="applyUserName"
        >
          <el-input v-model="baseFillInfo.applyUserName"></el-input>
        </el-form-item>
        <el-form-item label="证件号码：" prop="credentialNumber">
          <el-input v-model="baseFillInfo.credentialNumber"></el-input>
        </el-form-item>
      </el-form>
    </div>
    <div class="dialog-footer">
      <p @click="cancel">取消</p>
      <p @click="nextStep">确定</p>
    </div>
  </div>
</template>
<script>
import { getSelectCertificateType } from "@/api/modules/business";
export default {
  name: "InformationEntryPopup",
  props: {
    baseUserInfo: {
      type: Object,
      default: () => {},
    },
    situationCheckList: {
      type: Array,
      default: () => [],
    },
    specificMatters: {
      type: Object,
      default: () => {},
    },
  },
  data() {
    return {
      baseFillInfo: this.baseUserInfo.fillUserInfo,
      cegisterTypeList: [
        { label: "企业", value: "2" },
        { label: "个人", value: "1" },
      ],
      certificateTypeList: [],
      credentialType: [],
      caseOid: "",
      baseFillInfoRules: {
        applyUserType: [
          {
            required: true,
            message: "必选项",
            trigger: "change",
          },
        ],
        credentialType: [
          {
            required: true,
            message: "必选项",
            trigger: "change",
          },
        ],
        applyUserName: [
          {
            required: true,
            message: "必填项",
            trigger: "blur",
          },
        ],
        credentialNumber: [
          {
            required: true,
            message: "必填项",
            trigger: "blur",
          },
        ],
      },
    };
  },
  mounted() {
    if (this.baseUserInfo.fillUserInfo.applyUserType === "2") {
      this.baseFillInfo.applyUserName = this.baseUserInfo.companyName;
      this.baseFillInfo.credentialNumber = this.baseUserInfo.companyCode;  
    } else {
      this.baseFillInfo.applyUserName = this.baseUserInfo.name;
      this.baseFillInfo.credentialNumber = this.baseUserInfo.cardNo;
    }
    //新增两个字段，方便万达数据推送
    this.baseFillInfo.GrName = this.baseUserInfo.name;
    this.baseFillInfo.GrCardNo = this.baseUserInfo.cardNo;
    this.baseFillInfo.GrPhone = this.baseUserInfo.phone;

    this.getCertificateTypeList();
  },
  methods: {
    cancel() {
      this.$emit("closeInformationEntryPopup", this.baseUserInfo, "close");
    },
    nextStep() {
      this.$refs.baseFillInfo.validate((valid) => {
        if (valid) {
          localStorage.setItem("fileName",this.baseFillInfo.applyUserName)
          this.$emit("setFillUserInfo", this.baseFillInfo);
          this.$emit("closeInformationEntryPopup", this.baseUserInfo, "next");
        }
      });
    },

    changeCegisterType(val) {
      this.$store.commit("setCegisterType", val);
      if (this.baseUserInfo.fillUserInfo.applyUserType === "2") {
        this.baseFillInfo.applyUserName = this.baseUserInfo.companyName;
        this.baseFillInfo.credentialNumber = this.baseUserInfo.companyCode;
      } else {
        this.baseFillInfo.applyUserName = this.baseUserInfo.name;
        this.baseFillInfo.credentialNumber = this.baseUserInfo.cardNo;
      }

      //新增两个字段，方便万达数据推送
      this.baseFillInfo.GrName = this.baseUserInfo.name;
      this.baseFillInfo.GrCardNo = this.baseUserInfo.cardNo;
      this.baseFillInfo.GrPhone = this.baseUserInfo.phone;
      
      this.getCertificateTypeList();
    },
    getCertificateTypeList() {
      getSelectCertificateType({ type: this.baseFillInfo.applyUserType }).then(
        (res) => {
          if (res.code === 200) {
            this.certificateTypeList = res.data;
            this.baseFillInfo.credentialType =
              this.certificateTypeList[0].dictOid;
          }
        }
      );
    },
  },
  watch: {
    "baseUserInfo.fillUserInfo": {
      handler(val) {
        if (val) {
          this.baseFillInfo = val;
        }
      },
      deep: true,
    },
  },
};
</script>
<style lang="scss" scoped>
.content {
  width: 100%;
  height: 100%;

  .dialog--title {
    font-size: 14px;
    font-weight: bold;
    color: #2a344c;
    position: relative;
    padding-left: 10px;
    margin-bottom: 18px;
    text-align: left;

    &::before {
      content: "";
      position: absolute;
      left: 0;
      top: 50%;
      transform: translateY(-50%);
      width: 3px;
      height: 16px;
      background: #2e7dff;
      border-radius: 2px;
    }
  }

  .container {
    width: 100%;
    height: auto;
    border: 1px solid #e0e6f0;

    // .name {
    //   width: 153px;
    //   height: 5.7143rem;
    //   background: #edf0f5;
    //   display: flex;
    //   align-items: center;
    //   justify-content: flex-end;
    //   padding-right: 19px;

    //   &:before {
    //     content: '*';
    //     color: red;
    //   }
    // }

    // .body {
    //   width: calc(100% - 153px);
    //   height: 100%;
    //   display: flex;
    //   align-items: center;
    //   justify-content: flex-start;
    //   padding: 0 20px;
    // }

    // .blew {
    //   height: 5.7143rem;
    //   width: 100%;
    //   display: flex;

    //   &:nth-child(1) {
    //     border-bottom: 1px solid #e0e6f0;
    //   }

    //   .left {
    //     width: 50%;
    //     height: 100%;
    //     display: flex;

    //     .name {
    //       border-right: 1px solid #e0e6f0;
    //     }
    //   }

    //   .right {
    //     width: 50%;
    //     height: 100%;
    //     display: flex;

    //     .name {
    //       border-right: 1px solid #e0e6f0;
    //       border-left: 1px solid #E0E6F0;
    //     }
    //   }
    // }
    .baseFillInfo {
      width: 100%;
      height: auto;
      display: flex;
      flex-wrap: wrap;
      align-items: flex-start;
      justify-content: flex-start;

      .el-form-item {
        width: 50%;
        height: 5.7143rem;
        margin-bottom: 0;
        border-bottom: 1px solid #e0e6f0;

        &:nth-child(3),
        &:nth-child(4) {
          border: none;
        }

        ::v-deep .el-form-item__label {
          height: 100%;
          display: flex;
          align-items: center;
          justify-content: center;
          background: #edf0f5;
        }

        ::v-deep .el-form-item__content {
          flex: 1;

          .el-radio-group {
            width: 100%;
            height: 5.7143rem;
            display: flex;
            align-items: center;
            justify-content: flex-start;
            padding-left: 15px;
          }

          .el-select {
            width: 100%;
            height: 5.5rem;

            .el-input {
              width: 100%;
              height: 100%;

              .el-input__inner {
                border: none;
              }
            }
          }

          .el-input {
            width: 100%;
            height: 5.7143rem;

            .el-input__inner {
              width: 100%;
              height: 100%;
              border: none;
            }
          }
        }
      }
    }
  }

  .dialog-footer {
    width: 100%;
    height: 6.75rem;
    padding: 0;
    margin-top: 2rem;
    display: flex;
    align-items: center;
    justify-content: center;

    p {
      padding: 1.3125rem 2.875rem 1.4375rem 2.9375rem;
      box-shadow: 0px 0px 1.8125rem 0px rgba(204, 177, 121, 0.31);
      border-radius: 2.1875rem;
      font-size: 1.625rem;
      font-family: Microsoft YaHei;
      font-weight: 400;

      &:nth-child(1) {
        background: #ffffff;
        border: 1px solid #4988f2;
        box-shadow: 0px 0px 1.8125rem 0px rgba(204, 177, 121, 0.31);
        color: #2473ff;
        margin-right: 2.1875rem;
        cursor: pointer;
      }

      &:nth-child(2) {
        background: linear-gradient(90deg, #2473ff 0%, #56b1fd 100%);
        box-shadow: 0px 0px 1.5rem 0px rgb(40 107 198 / 31%);
        color: #ffffff;
      }
    }
  }
}
</style>
