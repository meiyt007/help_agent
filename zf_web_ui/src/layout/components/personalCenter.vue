<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-08-24 14:40:40
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-09-07 16:10:52
 * @FilePath: \zf_web_ui\src\layout\components\personalCenter.vue
 * @Description: 个人中心
-->
<template>
  <div class="content-body">
    <div class="tabHeader">
      <p
        class="tabLeft"
        @click="changeTab('1')"
        :class="tabType === '1' ? 'active' : ''"
      >
        基本信息
      </p>
      <p
        class="tabRight"
        @click="changeTab('2')"
        :class="tabType === '2' ? 'active' : ''"
      >
        修改密码
      </p>
    </div>
    <div class="userInfoList" v-show="tabType === '1'">
      <div class="essentialinformationForm">
        <el-form
          :model="userInfo"
          class="baseFillInfo"
          :rules="userInfoRules"
          ref="baseFillInfo"
          label-width="16.85rem"
        >
          <el-form-item label="姓名：" prop="name">
            <el-input v-model="userInfo.name"></el-input>
          </el-form-item>
          <el-form-item label="手机号：" prop="phone">
            <el-input v-model="userInfo.phone"></el-input>
          </el-form-item>
          <el-form-item label="工号：" prop="workNumber">
            <el-input v-model="userInfo.workNumber" readonly></el-input>
          </el-form-item>
          <el-form-item label="电子邮箱：" prop="email">
            <el-input v-model="userInfo.email" ></el-input>
          </el-form-item>
          <el-form-item label="平均服务时长(分钟)：" prop="avgServiceTime">
            <el-input v-model="userInfo.avgServiceTime" readonly></el-input>
          </el-form-item>
          <el-form-item label="最大服务人数：" prop="maxServiceNum">
            <el-input v-model="userInfo.maxServiceNum" readonly></el-input>
          </el-form-item>
          <el-form-item label="办公地点：" prop="servicePostion">
            <el-input v-model="userInfo.servicePostion" readonly></el-input>
          </el-form-item>
          <el-form-item label="备注：" prop="memo">
            <el-input v-model="userInfo.memo"></el-input>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <div class="modifyPass" v-show="tabType === '2'">
      <el-form
        :model="passwordData"
        :rules="passwordRules"
        ref="password"
        label-width="10rem"
      >
        <el-form-item label="旧密码" prop="oldPass">
          <el-input
            v-model="passwordData.oldPass"
            placeholder="请输入旧密码"
            type="password"
            show-password
          ></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPass">
          <el-input
            v-model="passwordData.newPass"
            placeholder="请输入新密码"
            type="password"
            show-password
          ></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="newPassConfirm">
          <el-input
            v-model="passwordData.newPassConfirm"
            placeholder="请确认密码"
            type="password"
            show-password
          ></el-input>
        </el-form-item>
      </el-form>
    </div>
    <div class="dialog-footer">
      <p @click="cancle">取消</p>
      <p @click="submit">确定</p>
    </div>
  </div>
</template>
<script>
import { getLoginUrl } from "@/utils/auth";
import { updateInfo, updatePass } from "@/api/modules/userCenter";
export default {
  name: "",
  props: {},
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else {
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.passwordData.newPass) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
    return {
      tabType: "1",
      userInfo: {
        name: "",
        phone: "",
        workNumber: "",
        email: "",
        avgServiceTime: "",
        maxServiceNum: "",
        servicePostion: "",
        memo: "",
      },
      passwordData: {
        oldPass: "",
        newPass: "",
        newPassConfirm: "",
      },
      userInfoRules: {
        name: [{ required: true, message: "请输入姓名", trigger: "blur" }],
        phone: [
          { required: true, message: "请输入手机号码", trigger: "blur" },
          {
            pattern: /^((0\d{2,3}-\d{7,8})|(1[3584]\d{9}))$/,
            message: "请输入正确手机号码",
            trigger: "blur",
          },
        ],
        avgServiceTime: [
          { required: true, message: "请输入平均服务时长", trigger: "blur" },
        ],
        maxServiceNum: [
          { required: true, message: "请输入最大服务人数", trigger: "blur" },
        ],
      },
      passwordRules: {
        oldPass: [{ required: true, message: "请输入密码", trigger: "blur" }],
        newPass: [{ required: true, validator: validatePass, trigger: "blur" }],
        newPassConfirm: [
          { required: true, validator: validatePass2, trigger: "blur" },
        ],
      },
    };
  },
  computed: {
    basicUserInfo() {
      return this.$store.state.user.basicUserInfo;
    },
  },
  mounted() {
    this.fillUserInfo();
  },
  methods: {
    changeTab(type) {
      this.tabType = type;
    },
    fillUserInfo() {
      Object.keys(this.userInfo).forEach((item) => {
        this.userInfo[item] = this.basicUserInfo[item];
      });
    },
    submit() {
      if (this.tabType === "1") {
        this.$refs["baseFillInfo"].validate((valid) => {
          if (valid) {
            updateInfo(this.userInfo).then((res) => {
              if (res.code === 200) {
                this.$message.success("用户信息修改成功");
                this.$emit("closePersonalCenter");
                this.$store.dispatch("GetInfo");
              }
            });
          }
        });
      } else {
        this.$refs["password"].validate((valid) => {
          if (valid) {
            updatePass(this.passwordData).then((res) => {
              if (res.code === 200) {
                this.$message.success("修改密码成功");
                this.$emit("closePersonalCenter");
                this.$store.dispatch("LogOut").then(() => {
                  location.href = getLoginUrl();
                });
              }
            });
          }
        });
      }
    },
    cancle() {
      this.$emit("closePersonalCenter");
    },
  },
};
</script>
<style lang="scss" scoped>
.content-body {
  width: 100%;
  height: 100%;

  .dialogContentTitle {
    text-align: left;
    padding-left: 1.375rem;
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

  .tabHeader {
    width: 100%;
    height: 4.375rem;
    background: #f3f6f8;
    border-radius: 2.1875rem;
    display: flex;
    align-items: center;
    justify-content: center;

    p {
      width: 50%;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 1.625rem;
      font-family: Source Han Sans CN;
      font-weight: 500;
      color: #373737;
      cursor: pointer;
    }

    .active {
      height: 3.75rem;
      // background: linear-gradient(90deg, #bf9e63 0%, #dfca98 100%);
      background: linear-gradient(90deg, #2473ff 0%, #56b1fd 100%);
      border-radius: 1.875rem;
      color: #ffffff;
    }
  }

  .userInfoList {
    width: 100%;
    height: auto;

    .essentialinformationForm {
      width: 100%;
      height: auto;
      display: flex;
      align-items: flex-start;
      justify-content: flex-start;
      border: 0.0625rem solid #c8cdd3;
      margin-top: 1.0625rem;
      box-sizing: content-box;
      flex-wrap: wrap;

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

          // &:nth-child(3),
          // &:nth-child(4) {
          //   border: none;
          // }

          ::v-deep .el-form-item__label {
            height: 100%;
            display: flex;
            align-items: center;
            justify-content: center;
            background: #edf0f5;
          }

          ::v-deep .el-form-item__content {
            height: 100%;
            width: calc(100% - 16.85rem);

            .el-input {
              width: 100%;
              height: 100%;

              .el-input__inner {
                width: 100%;
                height: 100%;
                border: none;
              }
            }

            .el-form-item__error {
              top: 70%;
            }
          }
        }
      }
    }
  }

  .modifyPass {
    width: 100%;
    height: auto;

    .el-form {
      width: 100%;
      height: auto;
      margin-top: 2.625rem;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      flex-wrap: wrap;

      .el-form-item {
        height: 3.75rem;
        width: 50%;

        ::v-deep .el-form-item__label {
          font-size: 1.5rem;
          font-family: Source Han Sans CN;
          font-weight: 500;
          color: #373737;
        }

        ::v-deep .el-form-item__content {
          height: 100%;
        }
      }
    }
  }

  .dialog-footer {
    width: 100%;
    height: auto;
    padding: 2rem 0;
    display: flex;
    align-items: center;
    justify-content: center;

    p {
      height: 4.8571rem;
      padding: 0 4.4375rem;
      border-radius: 1.5625rem;
      font-size: 1.625rem;
      display: flex;
      align-items: center;
      justify-content: center;

      font-family: Microsoft YaHei;
      font-weight: 400;

      &:nth-child(1) {
        background: #ffffff;
        border: 1px solid #4988f2;
        box-shadow: 0px 0px 1.8125rem 0px rgba(204, 177, 121, 0.31);
        color: #2473ff;
        cursor: pointer;
        margin-right: 2.1875rem;
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
