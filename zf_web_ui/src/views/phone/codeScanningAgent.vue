<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-07-29 09:55:59
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-09-07 14:09:37
 * @FilePath: \zf_web_ui\src\views\phone\codeScanningAgent.vue
 * @Description: 扫码帮代办页面
-->
<template>
  <div class="container">
    <div class="header"></div>
    <div class="formArea">
      <el-form
        :model="codeScanningAgentForm"
        :rules="codeScanningAgentRules"
        ref="codeScanningAgentForm"
        class="codeScanningAgentForm"
      >
        <p class="title">请填写以下信息</p>
        <el-form-item label="姓名" prop="name">
          <el-input
            v-model="codeScanningAgentForm.name"
            placeholder="请输入姓名"
          ></el-input>
        </el-form-item>
        <el-form-item label="身份证号" prop="cardNo">
          <el-input
            v-model="codeScanningAgentForm.cardNo"
            placeholder="请输入身份证号"
          ></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input
            v-model="codeScanningAgentForm.phone"
            placeholder="请输入手机号码"
          ></el-input>
        </el-form-item>
        <el-form-item label="是否企业" prop="isComponey">
          <el-radio-group v-model="codeScanningAgentForm.isComponey">
            <el-radio :label="true">是</el-radio>
            <el-radio :label="false">否</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>

      <el-form
        :model="codeScanningAgentForm"
        v-if="codeScanningAgentForm.isComponey"
        :rules="componeneyRules"
        ref="codeScanningAgentForm"
        class="codeScanningAgentForm2"
      >
        <el-form-item label="公司名称" prop="companyName">
          <el-input
            v-model="codeScanningAgentForm.companyName"
            placeholder="请输入公司名称"
          ></el-input>
        </el-form-item>
        <el-form-item label="统一社会信用代码" prop="companyCode">
          <el-input
            v-model="codeScanningAgentForm.companyCode"
            placeholder="请输入统一社会信用代码"
          ></el-input>
        </el-form-item>
      </el-form>
    </div>
    <div class="footBtn">
      <p @click="submitForm('codeScanningAgentForm')">提交</p>
    </div>
    <el-dialog
      :visible.sync="showDialog"
      width="80%"
      height="40vh"
      center
      :show-close="false"
      append-to-body
      v-dialogDrag
    >
      <p>信息登记成功</p>
      <p>帮办信息已经登记成功，请您稍等。工作人员尽快给您安排帮办人员</p>
      <div slot="footer" class="dialog-footer">
        <p @click="showDialog = false">好的，知道了</p>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { helpInfo } from "@/api/modules/helpAgent.js";
export default {
  name: "CodeScanningAgent",
  data() {
    return {
      showDialog: false,
      codeScanningAgentForm: {
        name: "",
        cardNo: "",
        phone: "",
        isComponey: false,
        companyName: "",
        companyCode: "",
      },
      codeScanningAgentRules: {
        name: [{ required: true, message: "请输入姓名", trigger: "blur" }],
        cardNo: [
          { required: true, message: "请输入身份证号码", trigger: "blur" },
          {
            pattern:
              /(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}$)/,
            message: "请输入合法身份证号",
            trigger: "blur",
          },
        ],
        phone: [
          { required: true, message: "请输入手机号码", trigger: "blur" },
          {
            pattern: /^((0\d{2,3}-\d{7,8})|(1[3584]\d{9}))$/,
            message: "请输入正确手机号码",
            trigger: "blur",
          },
        ],
      },
      componeneyRules: {
        companyName: [
          { required: true, message: "请输入公司名称", trigger: "blur" },
        ],
        companyCode: [
          {
            required: true,
            message: "请输入统一社会信用代码",
            trigger: "blur",
          },
          {
            pattern: /[^_IOZSVa-z\W]{2}\d {6}[^_IOZSVa-z\W]{10}$/g,
            message: "请输入正确统一社会信用代码",
            trigger: "blur",
          },
        ],
      },
    };
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          helpInfo(this.codeScanningAgentForm).then((res) => {
            if (res.code === 200) {
              this.showDialog = true;
            }
          });
        } else {
          return false;
        }
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.container {
  width: 100%;
  height: 100%;

  .header {
    width: 100%;
    height: 35vh;
    background: url("@/assets/images/phone/codeScanningAgentBg.png") no-repeat;
    background-size: 100% 100%;
  }

  .formArea {
    width: 100%;
    height: auto;
    margin-top: -5vh;
    padding: 0 1rem;

    .el-form {
      width: 100%;
      height: auto;
      overflow-y: auto;
      background: #fff;
      border-radius: 0.625rem;
      padding: 0 1rem;

      .title {
        font-size: 1.8rem;
        font-weight: bold;
        text-align: left;
        width: 100%;
      }

      .el-form-item {
        width: 100%;
        height: 4rem;

        margin-bottom: 1.5rem;
        border-bottom: 1px solid #e5e4e4;
        display: flex;
        align-items: center;
        justify-content: flex-start;

        ::v-deep .el-form-item__label {
          width: 17rem;
          font-size: 1.7rem;
          font-weight: 500;
          font-family: PingFang SC;
          color: #706d70;
          text-align: left;
        }

        ::v-deep .el-form-item__content {
          width: calc(100% - 17rem);
          height: 95%;

          .el-input {
            width: 100%;
            height: 100%;
            display: flex;
            align-items: flex-start;

            .el-input__inner {
              width: 100%;
              height: 100%;
              font-size: 1.5rem;
              border: none;
              color: #3f3f3f;
              padding: 0 !important;

              &::placeholder {
                color: #cfcfcf;
                font-size: 1.5rem;
              }
            }
          }

          .el-form-item__error {
            font-size: 1.25rem;
          }

          .el-radio-group {
            width: 100%;
            height: 100%;
            display: flex;
            align-items: center;
            justify-content: flex-start;

            .el-radio {
              .el-radio__input {
                .el-radio__inner {
                  width: 1.5rem;
                  height: 1.5rem;
                }
              }

              .is-checked {
                .el-radio__inner {
                  border-color: #409eff;
                  background: #409eff;
                }
              }

              .el-radio__label {
                font-size: 1.5rem !important;
              }
            }
          }
        }

        &:nth-last-child(1) {
          border: none;
          display: flex;
          align-items: flex-end;
          justify-content: center;
          margin-bottom: 1.5rem;

          ::v-deep .el-form-item__content {
            height: 100%;
            display: flex;
            align-items: center;
            justify-content: center;

            .el-button--primary {
              padding: 0.75rem 2rem;
              border-radius: 1.25rem;
              background: #00a2ff !important;

              span {
                font-size: 1.5rem;
              }
            }
          }
        }
      }
    }

    .codeScanningAgentForm2 {
      margin-top: 2rem;
    }
  }

  .footBtn {
    width: 100%;
    height: 3.75rem;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-top: 1.25rem;

    p {
      font-size: 1.8rem;
      width: 60%;
      height: 100%;
      background: #3f7bd4;
      padding: 0;
      margin: 0;
      border-radius: 1.875rem;
      color: #fff;
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }
}

::v-deep .el-dialog {
  width: 40rem;
  height: auto;
  margin-top: calc(50vh - 12.5625rem) !important;

  .el-dialog__header {
    display: none;
  }

  .el-dialog__body {
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 0 !important;

    p {
      width: 100%;
      font-size: 1.5rem;
      font-family: Source Han Sans CN;
      font-weight: 500;
      color: #373737;
      padding: 0;
      margin: 0;

      &:nth-child(1) {
        height: 3.5714rem;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 1.8571rem;
        font-family: Source Han Sans CN;
        font-weight: 500;
        color: #373737;
      }

      &:nth-child(2) {
        padding: 0 2rem;
        margin-top: 2rem;
        text-indent: 2;
      }
    }
  }

  .el-dialog__footer {
    height: 4.6875rem;
    margin-top: 5.9375rem;
    background: #00a2ff;

    .dialog-footer {
      width: 100%;
      height: 100%;
      display: flex;
      align-items: center;

      p {
        color: #fff;
        width: 100%;
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 1.5rem;
        font-family: Source Han Sans CN;
        font-weight: 500;
        padding: 0;
        margin: 0;
      }
    }
  }
}
</style>
