<template>
  <div class="findPassword">
      <div class="findPassword-box">
          <div class="title">
            {{title}}<span>找回密码</span>
          </div>
          <div class="find-step">
            <div  :class="[{'step-item': ('1' != active)},{'step-item step-item-active': ('1' == active)}]">
              <span>1</span>
              输入登录名
            </div>
            <div :class="[{'step-item': ('2' != active)},{'step-item step-item-active': ('2' == active)}]">
              <span>2</span>
              验证信息
            </div>
            <div :class="[{'step-item': ('3' != active)},{'step-item step-item-active': ('3' == active)}]">
              <span>3</span>
              重置密码
            </div>
            <div :class="[{'step-item': ('4' != active)},{'step-item step-item-active': ('4' == active)}]">
              <span>4</span>
              找回成功
            </div>
          </div>
          <!-- 步骤 -->
          <div class="step-box">
              <el-form v-show="'1' == active"  :model="fristForm" status-icon ref="fristForm" class="step-first step-content" >
                  <el-form-item class="input-label" prop="account" :rules="{required: true, message: '登录名不能为空', trigger: 'blur'}">
                    <el-input
                      placeholder="请输入登录名"
                      prefix-icon="el-icon-user"
                      v-model="fristForm.account">
                    </el-input>
                  </el-form-item>
                  <el-form-item class="input-label input-label-code" prop="code" :rules="{required: true, message: '验证码不能为空', trigger: 'blur'}">
                    <el-input
                      placeholder="验证码"
                      prefix-icon="el-icon-circle-check"
                      v-model="fristForm.code">
                    </el-input>
                    <label>
                      <img :src="codeUrl" @click="getCode" />
                    </label>
                  </el-form-item>
                  <el-button type="primary" class="btn btn-next" @click="nextFrist()">下一步</el-button>
                  <el-button type="success" class="btn btn-back" @click="goBack">返回首页</el-button>
              </el-form>
              <el-form v-show="'2' == active"  :rules="secondRules"  :model="secondForm" status-icon ref="secondForm" class="step-first step-content" style="display:none">
                  <el-form-item class="input-label"  prop="cardNo">
                    <el-input
                      placeholder="请输入身份证号码"
                      prefix-icon="el-icon-user"
                      v-model="secondForm.cardNo" maxlength="18" show-word-limit>
                    </el-input>
                  </el-form-item>
                  <el-form-item class="input-label"  prop="mobile">
                    <el-input
                      placeholder="请输入手机号码"
                      prefix-icon="el-icon-phone-outline"
                      maxlength="11" show-word-limit
                      v-model="secondForm.mobile">
                    </el-input>
                  </el-form-item>
                <el-button type="primary" class="btn btn-next" @click="nextSecond()">下一步</el-button>
                <el-button type="success" class="btn btn-back" @click="goBack">返回首页</el-button>
              </el-form>
              <el-form v-show="'3' == active"  :model="thirdForm" :rules="thirdRules" status-icon ref="thirdForm"  class="step-first step-content" style="display:none">
                  <el-form-item class="input-label"  prop="newPassword">
                    <el-input type="password"
                      placeholder="新密码"
                      prefix-icon="el-icon-lock"
                      v-model="thirdForm.newPassword"
                              oncut="return false"
                              @keyup.enter.native="handleLogin"
                              @paste.native.capture.prevent="handlePaste">

                    </el-input>
                  </el-form-item>
                  <el-form-item class="input-label"  prop="confirmPassword">
                    <el-input  type="password" class="confirmLable"
                               placeholder="确认密码"
                      prefix-icon="el-icon-lock"
                      v-model="thirdForm.confirmPassword"
                               oncut="return false"
                               @keyup.enter.native="handleLogin"
                               @paste.native.capture.prevent="handlePaste">
                    </el-input>
                  </el-form-item>
                <el-button type="primary" class="btn btn-next" @click="nextThird()">下一步</el-button>
                <el-button type="success" class="btn btn-back" @click="goBack">返回首页</el-button>
              </el-form>
              <div v-show="'4' == active"  class="step-last step-content" style="display:none">
                  <p>您好<span>“{{name}}”</span>，<br/>你已完成密码找回，请去点登录按钮进入登录界面！</p>
                  <el-button type="primary" class="btn btn-next" @click="goBack">去登录</el-button>
              </div>
          </div>
      </div>
  </div>
</template>

<script>
import { getFindPasswordPublicKey,updatePassword,checkAccountAndCode,checkCardNoAndMobile } from "@/api/login";
import {validIDCard, validatePhoneTwo,validatePassword} from '@/utils/validate'
import { encryptPwd } from '@/utils/jsencrypt'
import { getLoginUrl } from '@/utils/auth'
export default {
  data() {
    const equalToPassword = (rule, value, callback) => {
      if (this.thirdForm.newPassword !== value) {
        callback(new Error("两次输入的密码不一致"));
      } else {
        callback();
      }
    };
    return {
      title:this.Common.title,
      active:'1',
      name:'',
      fristForm:{
        account:'',
        code:''
      },
      secondForm:{
        cardNo:'',
        mobile:''
      },
      thirdForm:{
        newPassword:'',
        confirmPassword:''
      },
      codeUrl:'',
      secondRules: {
        cardNo: [
          {required: true, message: '身份证号码不能为空', trigger: 'blur'},
          {validator: validIDCard, message: '请输入正确的身份证号码', trigger: 'blur'}
        ],
        mobile: [
          {required: true, message: '手机号码不能为空', trigger: 'blur'},
          {validator: validatePhoneTwo, message: '请输入正确的手机号码', trigger: 'blur'}
        ],
      },
      thirdRules: {
        newPassword: [
          { required: true, message: "新密码不能为空", trigger: "blur" },
          { required: true, validator:validatePassword, trigger: 'blur'}
        ],
        confirmPassword: [
          { required: true, message: "确认密码不能为空", trigger: "blur" },
          { required: true, validator:validatePassword, trigger: 'blur'},
          { required: true, validator: equalToPassword, trigger: "blur" }
        ]
      },
    };
  },
  created() {
    var that = this ;
    setTimeout(function () {
      that.getCode();
    },600);
    this.getkey();
  },
  methods: {
    //返回首页
    goBack() {
      this.$router.push({ path: "/" });
    },
    getCode() {
      this.codeUrl= process.env.VUE_APP_BASE_API+'/captcha-image?'+new Date();
    },
    getkey() {
      getFindPasswordPublicKey().then(res=>{
        this.thirdForm.config=res.data.config
        this.thirdForm.publicKey=res.data.publicKey
      })
    },
    nextFrist(){
      var that = this ;
      this.$refs.fristForm.validate(valid => {
        if(valid){
          const data = {
            account: this.fristForm.account,
            code: this.fristForm.code
          }
          checkAccountAndCode(data).then(response => {
            if (response.code === 200) {
              this.secondForm.previous = response.previous;
              this.secondForm.userOid = response.userOid;
              this.active = '2';
            }else {
              that.getCode();
            }
          }).catch(function() {
              that.getCode();
          });
        }
      });
    },
    nextSecond(){
      this.$refs.secondForm.validate(valid => {
        if(valid){
          const data = {
            cardNo: this.secondForm.cardNo,
            mobile: this.secondForm.mobile,
            userOid:this.secondForm.userOid
          }
          checkCardNoAndMobile(data).then(response => {
            if (response.code === 200) {
              this.active = '3';
            }
          });
        }
      });
    },
    nextThird(){
      this.$refs.thirdForm.validate(valid => {
        if(valid){
          let newPassword=this.thirdForm.newPassword
          let confirmPassword=this.thirdForm.confirmPassword
          let previous = this.secondForm.previous;
          if("1"==this.thirdForm.config){
            newPassword=encryptPwd(this.thirdForm.publicKey,newPassword);
            confirmPassword=encryptPwd(this.thirdForm.publicKey,confirmPassword);
          }
          const data = {
            password: newPassword,
            confirmPassword: confirmPassword,
            userOid:this.secondForm.userOid,
            previous:previous
          }
          updatePassword(data).then(response => {
            if (response.code === 200) {
              this.active = '4';
              this.name = response.data;
            }
          });
        }
      });
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  html,body,.findPassword{
    height: 100%;
  }
  .findPassword {
    display: flex;
    justify-content: center;
    align-items: center;
    height: calc(100vh);
    background-image: url(../assets/image/findPassword-bg.png);
    background-size: cover;
  }
  .findPassword-box{
    background-color: #fff;
    border-radius: 5px;
    width: 1100px;
    height: 540px;
  }
  .title{
    font-size: 18px;
    color: #085fa1;
    margin-top: 20px;
    position: relative;
    padding-left: 20px;
    margin-bottom: 30px;
  }
  .title:before{
    position: absolute;
    content: '';
    width: 9px;
    height: 30px;
    background-color: #085fa1;
    top: -5px;
    left: 0px;
  }
  .title span{
    font-size: 14px;
    color: #333;
    padding: 0px 20px;
    position: relative;
  }
  .title span:before{
    position: absolute;
    content: '';
    width: 0.5px;
    height: 20px;
    background-color: #999;
    top: -3px;
    left: 10px;
  }
  .find-step{
    height: 60px;
    line-height: 60px;
    text-align: center;
    border-bottom: 1px solid #dbdfe7;
  }
  .find-step .step-item{
    display: inline-block;
    vertical-align: middle;
    font-size: 16px;
    padding: 0px 10px;
    position: relative;
    margin: 0px 25px;
    cursor: pointer;
  }
  .find-step .step-item:after{
    position: absolute;
    content: '';
    width: 20px;
    height: 2px;
    background-color: #e0e0e0;
    right: -30px;
    top: 30px;
  }
  .find-step .step-item:last-child:after{
    width: 0px;
  }
  .find-step .step-item span{
    display: inline-block;
    vertical-align: middle;
    color: #fff;
    font-size: 14px;
    width: 20px;
    height: 20px;
    line-height: 20px;
    text-align: center;
    background-color: #e0e0e0;
    border-radius: 100%;
  }
  .find-step .step-item-active:before{
    position: absolute;
    content: '';
    width: 100%;
    height: 2px;
    background-color: #0b76c8;
    bottom: 0px;
    left: 0;
  }
  .find-step .step-item-active{
    color: #0b76c8;
  }
  .find-step .step-item-active span{
    background-color: #0b76c8;
  }
  .step-box{
    width: 350px;
    margin: 70px auto;
  }
  .input-label{
    margin-bottom: 20px;
  }
  .input-label-code .el-input{
    display: inline-block;
    vertical-align: middle;
    width: 220px;
    margin-right: 10px;
    box-shadow: 0px 0px 10px #eee;
  }
  .input-label-code label{
    display: inline-block;
    vertical-align: middle;
    width: 110px;
    cursor:pointer
  }
  .input-label-code label>img{
    width: 100%;
  }
  .el-input__prefix{
    color: #0b76c8!important;
    font-size: 16px;
  }
  .btn{
    display: block;
    width: 46%;
    height: 40px;
    line-height: 40px;
    font-size: 14px;
    margin-top: 20px !important;
    padding: 0px;
    float: left;
    margin-right: 9px;
    margin-left: 0px;
  }
  .step-content p{
    font-size: 14px;
    color: #333;
    letter-spacing: 1px;
  }
  .step-content p>span{
    color: #0b76c8;
    font-size: 18px;
  }
  .confirmLable{
    margin-top: 15px !important;
  }


</style>
