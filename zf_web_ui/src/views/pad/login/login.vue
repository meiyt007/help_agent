<template>
  <div class="login" :class="{'whitePlace':isKeyboard}">
    <div class="loginBack" ref="loginBack"></div>
    <div class="loginArea">
      <img class="eyeImg" :src="eyeImg" alt="">
      <el-form ref="loginForm" :model="loginForm" class="login-form">
        <div class="title-box display_flex align-items_center">
          <p class="title">
            <img
              :src="require('@/assets/logo/logo.png')"
              alt=""
            />智能帮办业务应用系统建设项目
          </p>
        </div>
        <el-form-item prop="account">
          <el-input
            v-model="loginForm.account"
            auto-complete="off"
            placeholder="请输入用户名"
            clearable
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
            clearable
            auto-complete="off"
            placeholder="请输入密码"
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
            <!-- <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" /> -->
          </el-input>
        </el-form-item>
        <el-form-item prop="checkCode" class="">
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
            <img
              :src="codeUrl"
              v-trigger
              @click="getCode"
            />
          </div>
        </el-form-item>
        <div class="operatePassword">
          <el-checkbox class="remember" v-model="loginForm.rememberMe"
            >记住密码</el-checkbox
          >
          <!-- <div class="find-password" @click="findPassword">找回密码</div> -->
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
import { getCodeImg, getLoginCodeFlag } from "@/api/login";
import Cookies from "js-cookie";
import { Opanorama } from "@/utils/opanorama/opanorama.js";
import { encrypt, decrypt } from "@/utils/jsencrypt";

export default {
  name: "Login",
  data() {
    return {
      eyeUrl:[
        require('@/assets/images/login/eye.png'),
        require('@/assets/images/login/eye-account.gif'),
        require('@/assets/images/login/eye-up.gif'),
        require('@/assets/images/login/eye-xia.png'),
        require('@/assets/images/login/eye-down.gif'),
      ],
      eyeImg:require('@/assets/images/login/eye.png'),
      title: "上海市黄浦区智能帮办业务应用系统",
      codeFlag: false,
      imgurl: require("@/assets/images/pad/pagLoginBack.jpg"),
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
      isKeyboard:false,
      sourceType:false,
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
  computed:{
    getPass(){
      return this.loginForm.pcode;
    }
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
  },
  mounted() {
    // this.creatBack();
    this.sourceType = navigator.userAgent.match(
                  /(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i
                );
    this.getCode();
  },
  methods: {
    creatBack() {
      var longitude = 0,
        mark = "add";
      new Opanorama({
        offsetLongitude: 0,
        offsetLatitude: 90,
        fov: 90,
        url: this.imgurl,
        container: document.getElementsByClassName("loginBack")[0],
        onFrame: function (lon, lat) {
          // if (isMobile) {
          //   return;
          // }
          // if (longitude == 360) {
          //   mark = "sub";
          // }
          // if (longitude === 0) {
          //   mark = "add";
          // }

          // switch (mark) {
          //   case "add":
          //     longitude += 0.05;
          //     break;
          //   case "sub":
          //     longitude -= 0.05;
          //     break;
          // }
          return { lon: 0, lat: -90 };
        },
      });
    },
    //切换用户状态 1 导服 2帮带办
    changeUserType(type) {
      this.$set(this.loginForm, "userType", type);
    },
    //获取是否是验证码标识
    getCode() {
      // getCodeImg().then((res) => {
      //   this.codeUrl = res;
      // });
      this.codeUrl =
        process.env.VUE_APP_CASE_API +
        "/ha/login/getLoginCheckCode?date=" +
        new Date(new Date());
    },
    getCookie() {
      const account = Cookies.get("account");
      const pcode = Cookies.get("pcode");
      const rememberMe = Cookies.get("rememberMe");
      this.loginForm = {
        userType: "1",
        account: account === undefined ? this.loginForm.account : account,
        pcode: pcode === undefined ? this.loginForm.pcode : decrypt(pcode),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe),
      };
    },
    findPassword() {
      location.href = "./findPassword";
    },
    focusPassword() {
      this.passwordFocus = true;
      this.eyeImg =  this.eyeUrl[2];
      if(this.sourceType){
        this.isKeyboard = true;
        this.$nextTick(()=>{
          $("body").scrollTop(2000);
        })
      }

    },
    blurPassword() {
      if (!this.loginForm.pcode) {
        this.passwordFocus = false;
      }
      this.eyeImg =  this.eyeUrl[4];
      if(this.sourceType){
        this.isKeyboard = false;
      }
    },
    focusAccount() {
      this.accountFocus = true;
      this.eyeImg = this.eyeUrl[1];
    },
    blurAccount() {
      if (!this.loginForm.account) {
        this.accountFocus = false;
      }
      this.eyeImg =  this.eyeUrl[0];
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
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          // this.loading = true;
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
              this.loading = false;
              if (res.code === 200) {
                let flag = navigator.userAgent.match(
                  /(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i
                );
                // this.$router.push({
                //   path: this.redirect || "/padAssistantHome",
                // });
                if (flag) {
                  this.$router.push({
                    path: this.redirect || "/padAssistantHome",
                  });
                } else {
                  this.$router.push({
                    path: this.redirect || "/roundTableAssistant",
                  });
                }
              }
            })
            .catch(() => {
              that.loading = false;
              //that.msgError("服务器异常，请稍后再试！");
              that.getCode();
            });
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
  @import './index.scss';
</style>
