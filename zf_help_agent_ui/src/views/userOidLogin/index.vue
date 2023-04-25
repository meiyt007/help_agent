<template>
  <div>
    {{errorInfo}}
  </div>
</template>
<script>
import { getToken } from '@/utils/auth'
  export default {
    data() {
      return {
        errorInfo:"",
        userOid:"",
        currentAppOid:"",
        redirect:"",
        loginForm: {
          username: "",
          password: "",
          rememberMe: false,
          captcha: "",
          uuid: "",
          publicKey: "",
        },
      };
    },
    created() {
      this.userOid = this.$route.query.userOid;
      this.currentAppOid = this.$route.query.currentAppOid;
      this.redirect = this.$route.query.redirect;
      this.loginForm.username = this.userOid;
      //登录
      this.userOidLogin();
    },
    methods: {
      userOidLogin(){
        // //获取PublicKey
        // getPublicKey().then(res=>{
        //   this.loginForm.config=res.config;
        //   this.loginForm.publicKey=res.publicKey;
        //   this.$store
        //     .dispatch("customeLogin", this.loginForm)
        //     .then(() => {
        //       //debugger
        //       //设置本地的currentAppOid
        //       sessionStorage.setItem('currentAppOid',this.currentAppOid);
        //       //跳转
        //       this.$router.push(this.redirect || "/");
        //     })
        //     .catch(() => {
        //       //debugger
        //       //that.msgError("服务器异常，请稍后再试！");
        //       this.errorInfo = "登录出错了";
        //     });
        // });
        sessionStorage.setItem('currentAppOid',this.currentAppOid);
        if (getToken()) {
          debugger
          let  _that = this;
          this.$store.dispatch("GetInfo", this.loginForm)
            .then(() => {
              debugger
              //设置本地的currentAppOid
              //sessionStorage.setItem('currentAppOid',_that.currentAppOid);
              this.$router.push(_that.redirect || "/");
            })
            .catch(() => {
              debugger
              let _that_ = _that;
              this.$store.dispatch("customeLogin", _that.loginForm)
                .then(() => {
                  debugger
                  //设置本地的currentAppOid
                  //sessionStorage.setItem('currentAppOid',_that_.currentAppOid);
                  //跳转
                  this.$router.push(_that_.redirect || "/");
                })
                .catch(() => {
                  debugger
                  _that_.errorInfo = "登录出错了";
                });
            });
        }else {
          this.$store.dispatch("customeLogin", this.loginForm)
            .then(() => {
              //设置本地的currentAppOid
              //sessionStorage.setItem('currentAppOid',this.currentAppOid);
              //跳转
              this.$router.push(this.redirect || "/");
            })
            .catch(() => {
              this.errorInfo = "登录出错了";
            });
        }

      }
    }
  };
</script>
<style scoped>

</style>
