<template>
  <div class="content">
    <div class="dialog--title" v-show="cegisterType==='1'">申请人信息</div>
     <div class="dialog--title" v-show="cegisterType !='1'">申请单位信息</div>
    <div class="container">
      <div class="top">
        <div class="left">
          <div class="name">证件类型：</div>
          <div class="body">
            <el-select width="100%" v-model="ruleForm.credentialType" placeholder="请选择">
              <el-option
                v-for="item in certificateTypeList"
                :key="item.dictOid"
                  :label="item.name"
                  :value="item.dictOid"
              >
              </el-option>
            </el-select>
          </div>
        </div>
        <div class="right">
          <div class="scanBtn" v-show="cegisterType==='1'" @click="handleScanCard('0')">
            <img
              :src="require('@/assets/image/intelligent/scanIcon.png')"
              alt=""
            />
            <span>读取证件信息</span>
          </div>
           <!-- <div class="scanBtn" v-show="cegisterType==='1'" @click="handleScanCard('1')">
            <img
              :src="require('@/assets/image/intelligent/scanIcon.png')"
              alt=""
            />
            <span>扫描证件信息</span>
          </div> -->
        </div>
      </div>
      <div class="blew">
        <div class="left">
          <div class="name" v-show="cegisterType==='1'">申请人姓名：</div>
            <div class="name" v-show="cegisterType !='1'">申请单位名称：</div>
          <div class="body">
            <el-input v-model="ruleForm.applyUserName" width="100%"></el-input>
          </div>
        </div>
        <div class="right">
           <div class="name">证件号码：</div>
           <div class="body">
            <el-input v-model="ruleForm.credentialNumber" width="100%" maxlength="25" show-word-limit></el-input>
           </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import BaseFormJs from "@/mixins/baseForm.js";
import {
    getCertificateType,
} from "@/api/zc/businessManagement/windowAcceptance";
// import { initFaModelRule } from "../../../../../api/zc/businessManagement/faModelRuleValidation";
import DEVEICETYPE, {
  ID_CARD_V1,
  ID_CARD_V2,
  ID_CARD_V3,
  ID_CARD_V4
} from "@/components/HiSpeedCamera/config.js";
export default {
    mixins: [BaseFormJs],
  name:'informationEntryPopup',
  props:{
        cegisterType: {
      type: String,
      default: "0"
    },
    checkUserInfo:{
      type:Boolean,
      default:false
    },
    serviceOid: String,
    caseOid:String
      },
  data(){
    return{
      ruleForm:{
          credentialType:'',
      applicantName:'',
      applicantCard:''
      },
      certificateTypeList:[],
      credentialType:[]
    }
  },
  mounted(){
     this.getSelectCertificateType();
  },
  methods:{

  },
  watch:{
   checkUserInfo:{
    handler(val){
      if(val){
        this.$emit("checkUserData",this.ruleForm)
      }
    }
   },
  cegisterType:{
    handler(val){
      if(val){
        this.getSelectCertificateType()
        this.ruleForm.applyUserName = ''
        this.ruleForm.credentialNumber = ''
      }
    }
  }
  }
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
    height: 160px;
    border: 1px solid #e0e6f0;
    .name{
          width: 153px;
          height: 100%;
          background: #edf0f5;
          display: flex;
          align-items: center;
          justify-content: flex-end;
          padding-right: 19px;
          &:before{
            content: '*';
            color: red;
          }
    }
     .body {
          width: calc(100% - 153px);
          height: 100%;
          display: flex;
          align-items: center;
          justify-content: flex-start;
          padding: 0 20px;
        }
    .top {
      height: 50%;
      width: 100%;
      border-bottom:1px solid #e0e6f0 ;
      display: flex;
      .left {
        width: 50%;
        height: 100%;
        display: flex;
        .name {
          border-right: 1px solid #e0e6f0;
        }
       .body{
        padding: 0;
        padding-left: 20px;
       }
      }
      .right {
        width: 50%;
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: flex-end;
        padding-right: 19px;
        .scanBtn {
          width: 171px;
          height: 35px;
          background: #2e7dff;
          border-radius: 5px;
          padding: 0 21px;
          cursor: pointer;
          display: flex;
          align-items: center;
          justify-content: space-between;
          img {
            width: 20px;
            height: 19px;
          }
          span {
            font-size: 16px;
font-family: Microsoft YaHei;
font-weight: 400;
color: #FFFFFF;
          }
        }
      }
    }
    .blew {
      height: 50%;
      width: 100%;
      display: flex;
      .left{
        width: 50%;
        height: 100%;
        display: flex;
        .name{
           border-right: 1px solid #e0e6f0;
        }
      }
      .right{
                width: 50%;
        height: 100%;
        display: flex;
 .name{
           border-right: 1px solid #e0e6f0;
          border-left: 1px solid #E0E6F0;
        }
      }
    }
  }
}
</style>
