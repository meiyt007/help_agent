<template>
  <div class="login">
    <div class="loginArea">
      <el-form ref="loginForm" :model="loginForm" class="login-form">
        <div class="title-box display_flex align-items_center">
          <p class="title">
            <img
              :src="require('@/assets/logo/logo.png')"
              alt=""
            />智能帮办业务应用系统
          </p>
        </div>
        <el-form-item prop="account">
          <el-input
            v-model="loginForm.account"
            auto-complete="off"
            clearable
            placeholder="请输入用户名"
            @focus="focusAccount"
            @blur="blurAccount"
          >
            <template slot="prepend">
              <div class="area" :class="accountFocus ? 'active' : ''">
                <img
                  :src="
                    accountFocus
                      ? require('@/assets/images/login/userLogoChoose.png')
                      : require('@/assets/images/login/userLogo.png')
                  "
                  style="width: 15px"
                  alt=""
                />
              </div>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="pcode">
          <el-input
            v-model="loginForm.pcode"
            type="password"
            show-password
            auto-complete="off"
            placeholder="请输入密码"
            clearable
            oncut="return false"
            @focus="focusPassword"
            @blur="blurPassword"
            @keyup.enter.native="handleLogin"
          >
          <!-- @paste.native.capture.prevent="handlePaste" -->
            <template slot="prepend">
              <div class="area" :class="passwordFocus ? 'active' : ''">
                <img
                  :src="
                    passwordFocus
                      ? require('@/assets/images/login/passwordLogoChoose.png')
                      : require('@/assets/images/login/passwordLogo.png')
                  "
                  style="width: 15px"
                  alt=""
                />
              </div>
            </template>
            <!-- <svg-icon slot="prefix" icon-class="pcode" class="el-input__icon input-icon" /> -->
          </el-input>
        </el-form-item>
        <el-form-item prop="checkCode" v-if="codeFlag === '1'">
          <el-input
            v-model="loginForm.checkCode"
            auto-complete="off"
            placeholder="请输入验证码"
            style="width: 63%"
            @focus="focusCheckCode"
            @blur="blurCheckCode"
            @keyup.enter.native="handleLogin"
          >
            <template slot="prepend">
              <div class="area" :class="checkCodeFocus ? 'active' : ''">
                <img
                  :src="
                    checkCodeFocus
                      ? require('@/assets/images/login/checkCodeChoose.png')
                      : require('@/assets/images/login/checkCode.png')
                  "
                  alt=""
                />
              </div>
            </template>
            <!-- <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" /> -->
          </el-input>
          <div class="login-code">
            <img @click="getCode" :src="codeUrl" />
          </div>
        </el-form-item>
        <div class="operatePassword">
          <el-checkbox class="remember" v-model="loginForm.rememberMe"
            >记住密码</el-checkbox
          >
          <el-dropdown  @command="handleCommand">
            <span class="el-dropdown-link">
              {{commandText}}<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown" >
              <el-dropdown-item  :key="item.id" v-for="item in list" :command="item.name" >{{ item.name }}</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>

        <el-form-item style="width: 100%" class="login-btn-form">
          <el-button
            :loading="loading"
            size="medium"
            type="primary"
            class="login-box"
            @click.native.prevent="handleLogin"
          >
            <span v-if="!loading">登 录</span>
            <span v-else>登 录 中...</span>
          </el-button>
        </el-form-item>
        <div class="el-login-footer">
          <span>Copyright © 2022 版权所有 上海卓繁信息技术股份有限公司</span>
        </div>
      </el-form>
    </div>

    <!--  底部  -->
  </div>
</template>

<script>
import { getCodeImg, getLoginCodeFlag, checkCodeFlag,queryLoginLocation,saveLoingLocationName } from "@/api/login";
import Cookies from "js-cookie";
import { encrypt, decrypt } from "@/utils/jsencrypt";

export default {
  name: "Login",
  data() {
    return {
      title: "上海市黄浦区智能帮办业务应用系统",
      codeFlag: 0,
      codeUrl: "",
      cookiePassword: "",
      loginForm: {
        account: "",
        pcode: "",
        rememberMe: false,
        checkCode: "",
        haType: "2",
      },
      accountFocus: false,
      passwordFocus: false,
      checkCodeFocus: false,
      loginRules: {
        account: [
          { required: true, trigger: "blur", message: "用户名不能为空" },
        ],
        pcode: [{ required: true, trigger: "blur", message: "密码不能为空" }],
        checkCode: [
          { required: true, trigger: "change", message: "验证码不能为空" },
        ],
      },
      loading: false,
      redirect: undefined,
      commandText:'请选择所在位置',
      commandId:'',
      list:[]
    };
  },
  computed: {
    noticeText() {
      return !this.loginForm.account
        ? "用户名不能为空"
        : !this.loginForm.pcode
        ? "密码不能为空"
        : "";
    },
  },
  watch: {
    $route: {
      handler: function (route) {
        this.redirect = route.query && route.query.redirect;
        console.log("this.redirect=", this.redirect);
      },
      immediate: true,
    },
  },
  directives: {
    trigger: {
      inserted(el, binging) {
        el.click();
      },
    },
  },
  created() {
    this.getCookie();
    this.checkCodeFlag();
    queryLoginLocation().then(res=>{
      this.list = res.data;
    })
    if(localStorage.getItem('region')){
      this.commandText = localStorage.getItem('region');
    }
  },
  mounted() {
    this.getCode();
  },
  methods: {
    handleCommand(command) {
        this.commandText = command;
        localStorage.setItem("region",this.commandText);
    },
    getCookie() {
      const account = Cookies.get("account");
      const pcode = Cookies.get("pcode");
      const rememberMe = Cookies.get("rememberMe");
      this.loginForm = {
        account: account === undefined ? this.loginForm.account : account,
        pcode: pcode === undefined ? this.loginForm.pcode : decrypt(pcode),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe),
      };
    },
    //用户是否需要验证码
    checkCodeFlag() {
      checkCodeFlag().then((res) => {
        if (res.code === 200) {
          this.codeFlag = res.data.flag;
          if (this.codeFlag === 1) {
            this.getCode();
          }
        }
      });
    },
    //获取是否是验证码标识
    getCode() {
      this.codeUrl =
        process.env.VUE_APP_CASE_API +
        "/ha/login/getLoginCheckCode?date=" +
        new Date(new Date());
    },
    findPassword() {
      location.href = "./findPassword";
    },
    focusPassword() {
      this.passwordFocus = true;
    },
    blurPassword() {
      if (!this.loginForm.pcode) {
        this.passwordFocus = false;
      }
    },
    focusAccount() {
      this.accountFocus = true;
    },
    blurAccount() {
      if (!this.loginForm.account) {
        this.accountFocus = false;
      }
    },
    focusCheckCode() {
      this.checkCodeFocus = true;
    },
    blurCheckCode() {
      if (!this.loginForm.checkCode) {
        this.checkCodeFocus = false;
      }
    },
    handleLogin() {
      var that = this;
      if(this.commandText.indexOf("选择") > -1){
        this.$message.warning("请选择所在位置");
        return;
      }
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.loading = true;
          if (this.loginForm.rememberMe) {
            Cookies.set("account", this.loginForm.account, { expires: 30 });
            Cookies.set("pcode", encrypt(this.loginForm.pcode), {
              expires: 30,
            });
            Cookies.set("rememberMe", this.loginForm.rememberMe, {
              expires: 30,
            });
          } else {
            Cookies.remove("account");
            Cookies.remove("pcode");
            Cookies.remove("rememberMe");
          }
          this.loginForm.haType = "2";
          this.$store
            .dispatch("Login", this.loginForm)
            .then((res) => {
              saveLoingLocationName({
                id:res.data.id,
                loginLocationName:that.commandText
              });
              this.loading = false;
              if (res.code === 200) {
                let userType = res.data.userType;
                let groupPost = res.data.groupPost;
                if(userType == '3'){
                  localStorage.setItem('noNextOperate',false);
                }else if(userType == '2'){
                  if(groupPost == '1' || groupPost == '2'){
                    localStorage.setItem('noNextOperate',false);
                  }else{
                    localStorage.setItem('noNextOperate',true);
                  }
                }else{
                  localStorage.setItem('noNextOperate',true);
                }
                let flag = navigator.userAgent.match(
                  /(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i
                );
                // this.$router.push({
                //   path: this.redirect || "/roundTableAssistant",
                // });
                if (flag) {
                  this.$router.push({
                    path: this.redirect || "/padAssistantHome",
                  });
                } else {
                  this.$router.push({
                    path: this.redirect || "/",
                  });
                }
              }
            })
            .catch(() => {
              that.loading = false;
              //that.msgError("服务器异常，请稍后再试！");
              if (this.codeFlag === 1) {
                this.getCode();
              }
            });
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
// 改变input框背景颜色
::v-deep .el-input__inner {
  &:focus {
    border: 1px solid #4988f2;
  }
}
::v-deep .el-dropdown{
  position: relative;

  &::before{
    content:'*';
    font-size: 20px;
    color: red;
    position: absolute;
    top: 4px;
    left: -10px;
  }
  .el-dropdown-link {
    cursor: pointer;
    color: #409EFF;
  }
 .el-icon-arrow-down {
    font-size: 12px;
  }
}

.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url("../../../assets/images/login/loginBack-new.png");
  background-size: cover;
}

.loginArea {
  min-width: 46.25rem;
  min-height: 42rem;
  border-radius: 5.0625rem;
  box-shadow: 0 0 0 1.25rem RGBA(188, 222, 238, 1), -1.25rem 0 0 #f3fafd;

  .login-form {
    width: 100%;
    height: 100%;

    border: 2px solid #ffffff;
    border-radius: 5.0625rem;
    padding: 2.375rem 1.875rem 0px 1.875rem;

    .title-box {
      margin: 0 0 30px 0;

      .title {
        padding: 0;
        margin: 0;
        width: 100%;
        height: 40px;
        display: flex;
        align-items: center;
        justify-content: flex-start;
        font-family: FZZYJW--GB1-0;
        font-size: 28px;
        font-weight: normal;
        font-stretch: normal;
        letter-spacing: 0px;
        color: #6497d5;

        img {
          width: 30px;
        }
      }

      .iconfont {
        position: relative;
        font-weight: 400;
        font-size: 40px;
      }

      .zfsoft-zfsoft-logo:before {
        content: "\e944";
        width: 36px;
        height: 36px;
        display: inline-block;
        position: absolute;
        left: -5px;
        top: -9px;
      }

      .icon {
        width: 36px;
        height: 36px;
        border-radius: 50%;
        margin-right: 12px;
      }
    }

    .chooseArea {
      width: 100%;
      height: 40px;
      margin-top: 4vh;
      margin-bottom: 5vh;
      display: flex;
      align-items: center;
      justify-content: center;

      .chooseBtn {
        width: 40%;
        height: 100%;
        background-color: #e6ecf8;
        display: flex;
        align-items: center;
        justify-content: center;
        font-family: MicrosoftYaHei;

        &:nth-child(1) {
          border-top-left-radius: 2px;
          border-bottom-left-radius: 2px;
        }

        &:nth-child(2) {
          border-top-right-radius: 2px;
          border-bottom-right-radius: 2px;
        }

        img {
          width: 18px;
          margin-right: 5px;
        }

        span {
          font-size: 18px;
          font-family: Microsoft YaHei;
          font-weight: 400;
          color: #be8c27;
        }
      }

      .active {
        background: linear-gradient(90deg, #2473ff 0%, #56b1fd 100%);
        position: relative;

        span {
          color: #ffffff;
        }

        &:before {
          display: block;
          content: "";
          border-width: 10px 10px 10px 10px;
          border-style: solid;
          border-color: #4988f2 transparent transparent transparent;
          position: absolute;
          top: 40px;
          left: calc(50% - 5px);
        }
      }
    }

    .notice-box {
      margin-bottom: 17px;

      .notice-title {
        font-size: 20px;
        font-family: Microsoft YaHei;
        font-weight: 400;
        color: #333333;
        margin-right: 21px;
      }

      .error-notice {
        font-size: 12px;
        font-family: Microsoft YaHei;
        font-weight: 400;
        color: #ff5629;
      }
    }

    .operatePassword {
      width: 100%;
      height: 30px;
      display: flex;
      align-items: center;
      justify-content: space-between;
    }

    ::v-deep .el-form-item {
      width: 100%;

      .el-form-item__content {
        width: 100%;
        height: 40px;
        display: flex;
        align-items: center;
        justify-content: space-between;

        .el-input {
          .el-input-group__prepend {
            padding: 0;
            width: 60px;
            overflow: hidden;
            border: none;

            .area {
              width: 100%;
              height: 40px;
              display: flex;
              align-items: center;
              justify-content: center;
              border: none;

              img {
                width: 15px;
              }
            }

            .active {
              background: linear-gradient(90deg, #2473ff 0%, #56b1fd 100%);
              // background: linear-gradient(90deg, #66A6F3 0%, #1861B4 100%);
            }
          }
        }
      }
    }
  }
}

.login-btn-form {
  margin-top: 3vh;

  .login-box {
    width: 70%;
    height: 5.875rem;
    border-radius: 2.9375rem;
    background: linear-gradient(90deg, #2473ff 0%, #56b1fd 100%);
    padding: 0.625rem;
    border: none;

    //
    span {
      font-family: MicrosoftYaHei;
      font-size: 14px;
      font-weight: normal;
      font-stretch: normal;
      letter-spacing: 0px;
      color: #ffffff;
    }
  }
}

.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}

.login-code {
  width: 33%;
  height: 80%;
  margin-left: 4%;
  border-radius: 5%;

  img {
    width: 100%;
    height: 100%;
    cursor: pointer;
  }
}

.el-login-footer {
  height: 40px;
  bottom: 0;
  width: 100%;
  margin-top: 5vh;
  font-family: MicrosoftYaHei;
  font-size: 12px;
  font-weight: normal;
  font-stretch: normal;
  letter-spacing: 0px;
  color: #999999;
}

.remember {
  .el-checkbox__label {
    font-size: 14px;
    font-family: Microsoft YaHei;
    font-weight: 400;
    color: #958d85;
  }

  .el-checkbox__inner {
    //background: #fff!important;
    border: 1px solid #000000 !important;
    border-radius: 4px;
  }

  .is-checked .el-checkbox__inner:after {
    color: #000000 !important;
  }
}

.find-pcode {
  float: right;
  cursor: pointer;
  font-size: 14px;
  font-family: Microsoft YaHei;
  font-weight: 400;
  color: #999999;
}
</style>
