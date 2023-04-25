<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-09-07 15:25:51
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-09-09 10:03:15
 * @FilePath: \zf_web_ui\src\views\pc\operationFlow\components\commonProblem.vue
 * @Description: 常见问题列表
-->
<template>
    <div class="commonProblem">
        <div class="content-Body">
          <el-form
            :model="questionInfo"
            class="questionArea"
            :rules="questionRules"
            ref="questionInfo"
            label-width="10rem"
            >
            <el-form-item label="发起人：" prop="workUserName">
                <el-input readonly v-model="questionInfo.workUserName"></el-input>
            </el-form-item>
            <el-form-item label="预约主体：" prop="companyName">
                <el-input readonly v-model="questionInfo.companyName"></el-input>
            </el-form-item>
            <el-form-item label="预约时间：" prop="appDate">
              <el-input readonly v-model="questionInfo.appDate"></el-input>
            </el-form-item>
            <el-form-item label="业务类型：">
              <el-input readonly value="一桌联办"></el-input>
            </el-form-item>
            <el-form-item label="提示：" prop="desc">
                <el-input
                readonly
                type="textarea"
                :autosize="{ minRows: 2, maxRows: 1 }"
                v-model="questionInfo.desc"
              ></el-input> 
            </el-form-item>
            <el-form-item label="备注：" prop="desc2">
                <el-input
                type="textarea"
                :autosize="{ minRows: 4, maxRows: 3 }"
                v-model="questionInfo.desc2"
              ></el-input> 
            </el-form-item>
        </el-form>
          <div class="dialog-footer">
            <p @click="cancel">取消</p>
            <p @click="submit">确定</p>
          </div>
        </div>
    </div>
  </template>
  <script>
  import {
    conAppointment,
  } from "@/api/modules/helpAgent";
  export default {
    name: "appointInfo",
    data() {
      return {
        questionInfo: {
          workUserName:'',
          companyName:'',
          appDate:'',
          desc:'',
          desc2:''
        },
        questionRules: {
            question: [{ required: true, message: "请输入问题", trigger: "blur" }],
            answer: [{ required: true, message: "请输入答案", trigger: "blur" }],
        },
        obj:{}
      };
    },
    props:{
      dataObj: {
          type: String,
          default: () => "",
      },
    },
    mounted() {
      
      let obj = JSON.parse(this.dataObj);
      console.log(obj);
      this.questionInfo = {
        workUserName:obj.desMes.deskMes.workUserName,
        companyName:obj.desMes.deskMes.companyName,
        appDate:obj.desMes.deskMes.appDate,
        desc:obj.desMes.deskMes.desc,
      }
      this.obj = obj;
    },
    methods: {
        cancel(){
            this.$emit('closeAppoint')
        },  
        //修改问题
        updateQuestion() {

        },
        //确认
        submit() {
          let that = this;
          conAppointment({
            id:this.obj.desMes.deskDepMes.id,   //部门id
            confirmFlag:'1',
            desc:this.questionInfo.desc2
          }).then(res=>{
            if(res.code == 200){
              that.$message.success("确认成功！");
              that.$emit("closeAppoint");
            }else{
              console.log(res);
            }
          }).catch(err=>{
            console.log(err)
          })
        },
    },
  };
  </script>
  <style lang="scss" scoped>
  .commonProblem {
    width: 80%;
    margin-left: 10%;
    height: 100%;
    .tab-header {
      width: 100%;
      height: 5.7143rem;
      display: flex;
      align-content: center;
      justify-content: space-between;
      .left {
        width: 50%;
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: flex-start;
        .el-input {
          width: calc(100% - 5rem);
          margin-left: 1.5rem;
        }
      }
      .right {
        width: 50%;
        display: flex;
        align-items: center;
        justify-content: flex-end;
      }
    }
    .tableArea {
      width: 100%;
      height: calc(100% - 8rem);
    }
  }
  ::v-deep  textArea{
    font-family: Source Han Sans CN;
  }
  .questionDialog {
    height: 100vh !important;
    ::v-deep .el-dialog {
      height: 60vh !important;
      .el-dialog__body {
        height: calc(100% - 3.75rem) !important;
        max-height: 100%;
        .content-Body {
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
              height: auto;
              width: 100%;
  
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
  </style>
  