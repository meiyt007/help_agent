<template>
  <div class="app-container">

    <div class="el-table__header-wrapper dialog-table">
      <h3>注册授权</h3>
      <el-form ref="form" :model="form" label-width="0px" class="demo-ruleForm">
        <el-input v-show="false" v-model="form.applyOid"/>
        <el-input v-show="false" v-model="form.tomcatOid" />
        <el-input v-show="false" v-model="form.productCode" />
        <el-input v-show="false" v-model="form.customerName" />
        <el-input v-show="false" v-model="form.machineId" />
        <table cellspacing="0" cellpadding="0" border="0" class="el-table__body">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><b>客户名称：</b></td>
            <td colspan="3">
              {{form.customerName }}
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>产品特征码：</b></td>
            <td colspan="3">
              <div style="word-wrap:break-word;overflow:hidden;">{{form.machineId }}</div>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>注册授权文件：</b></td>
            <td>
              <el-form-item prop="iconName">
                <el-upload
                  class="upload-demo"
                  accept=".data"
                  :action="uploadUrl()"
                  :on-error="uploadError"
                  :file-list="fileList"
                  :on-success="uploadSuccess">
                  <el-button type="primary" size="small">上传附件<i class="el-icon-upload el-icon--right"></i></el-button>
                  <span class="tip">&nbsp;&nbsp;注意：只能上传格式为.data的文件！</span>
                </el-upload>
                <div v-show="null != form.attaOid && '' != form.attaOid">
                  <span>{{form.attaName}}</span>
                </div>
              </el-form-item>
              <el-input v-show="false" v-model="form.attaOid" />
            </td>
            <td><i class="require">*</i><b>授权开始时间：</b></td>
            <td>
              <el-input v-model="startDate" readonly />
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>授权提醒时间：</b></td>
            <td>
              <el-input v-model="warnDate" readonly />
            </td>
            <td><i class="require">*</i><b>授权到期时间：</b></td>
            <td>
              <el-input v-model="expireDate" readonly />
            </td>
          </tr>
        </table>
      </el-form>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" size="medium" class="center" @click="submitForm">注  册</el-button>
    </div>
  </div>
</template>

<script>
  import { checkRegisterLicense, initRegister, uploadRegister,uploadFile} from "@/api/sys/register";
  export default {
    name: "Register",
    watch: {
      startDate(val){
        this.form.startDate  = val;
      },
      expireDate(val){
        this.form.expireDate  = val;
      },
      warnDate(val){
        this.form.warnDate  = val;
      }
    },
    data() {
      return {
        // 遮罩层
        loading: true,
        fileList:[],
        // 表单参数
        form: {},
        expireDate:'',
        startDate:'',
        warnDate:'',
      };
    },
    created() {
      this.initRegister();
    },
    methods: {
      /** 查询应用列表 */
      initRegister() {
        this.loading = true;
        initRegister().then(response => {
          this.form = response;
          this.loading = false;
        });
      },
      /** 提交按钮 */
      submitForm: function() {
        if('' == this.form.attaOid || null==this.form.attaOid){
          this.msgError("请先上传注册文件！");
          return false;
        }
        if('' == this.form.startDate || null==this.form.startDate){
          this.msgError("请先上传注册文件！");
          return false;
        }
        var that=this;
        checkRegisterLicense(this.form).then(response => {
          this.$confirm('注册成功！请先退出，再登录！', "温馨提示", {
            confirmButtonText: "确定",
            type: "warning"
          }).then(function() {
            that.$router.push({ path: "/" });
          }).catch(function() {

          });
        });
      },
      /** 上传后的数据解析 */
       uploadRegister(){
        var that = this;
        uploadRegister(this.form.attaOid).then(response => {
          this.expireDate  = response.data.expireDate;
          this.warnDate  = response.data.warnDate;
          this.startDate  = response.data.startDate;
        }).catch(function(res) {
          that.fileList=[];
          that.form.attaOid = '';
          that.form.attaName = '';
        });
      },
      //成功后返回
      async uploadSuccess(resp){
        this.fileList=[];
        if(200 !== resp.code){
          return this.msgError(resp.message);
        }
        this.form.attaOid = resp.data.oid;
        this.form.attaName = resp.data.name;
        this.uploadRegister();
      },
      //失败后返回
      uploadError(resp){
        this.msgError("文件上传失败");
      },
      uploadUrl(){
        return uploadFile();
      }
    }
  };
</script>
<style>
  .center{
    margin-left: 47%;
    width: 100px;
  }
  .tip{
    color: red;
  }
</style>
