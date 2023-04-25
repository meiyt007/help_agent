/**
* 内资公司变更法定代表人表单
*/
<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="基本信息" name="first">
        <el-form ref="form" :model="form" :rules="rules" label-width="0px">
        <div class="el-table__header-wrapper dialog-table">
          <h3 class="title"><i class="el-icon-s-grid"></i>基本信息</h3>
            <table cellspacing="0" cellpadding="0" border="0" >
              <colgroup>
                <col width="15%" />
                <col width="35%" />
                <col width="15%" />
                <col width="35%" />
              </colgroup>
              <tr>
                <td><i class="require">*</i><b>名称：</b></td>
                <td colspan="3">
                  <el-form-item prop="companyName">
                    <el-input v-model.trim="form.companyName" placeholder="请输入名称(集团母公司需填写：集团名称：分情形，有集团填写)" maxlength="50" show-word-limit/>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td><i class="require">*</i><b>统一社会信用代码：</b></td>
                <td colspan="3">
                  <el-form-item prop="socialCreditCode">
                    <el-input v-model.trim="form.socialCreditCode" placeholder="请输入统一社会信用代码" maxlength="50" show-word-limit/>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td><i class="require">*</i><b>联系电话：</b></td>
                <td>
                  <el-form-item prop="mobile">
                    <el-input v-model.trim="form.mobile" placeholder="请输入联系电话" maxlength="20" show-word-limit/>
                  </el-form-item>
                </td>
                <td><i class="require">*</i><b>邮政编码：</b></td>
                <td>
                  <el-form-item prop="code">
                    <el-input v-model.trim="form.code" placeholder="请输入邮政编码" maxlength="6" show-word-limit/>
                  </el-form-item>
                </td>
              </tr>
            </table>
        </div>

        <div class="el-table__header-wrapper dialog-table">
          <h3 class="title"><i class="el-icon-s-grid"></i>指定代表/委托代理人</h3>
            <table cellspacing="0" cellpadding="0" border="0" >
              <colgroup>
                <col width="15%" />
                <col width="35%" />
                <col width="15%" />
                <col width="35%" />
              </colgroup>
              <tr>
                <td><i class="require">*</i><b>姓名：</b></td>
                <td colspan="3">
                  <el-form-item prop="contactUserName">
                    <el-input v-model.trim="form.contactUserName" placeholder="请输入姓名" maxlength="50" show-word-limit/>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td><i class="require">*</i><b>身份证件类型：</b></td>
                <td>
                  <el-form-item prop="contactCredentialType">
                    <el-input v-model.trim="form.contactCredentialType" placeholder="请输入身份证件类型" maxlength="50" show-word-limit/>
                  </el-form-item>
                </td>
                <td><i class="require">*</i><b>身份证件号码：</b></td>
                <td>
                  <el-form-item prop="contactCredentialNumber">
                    <el-input v-model.trim="form.contactCredentialNumber" placeholder="请输入身份证件号码" maxlength="50" show-word-limit/>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td><i class="require">*</i><b>固定电话：</b></td>
                <td>
                  <el-form-item prop="contactMobile">
                    <el-input v-model.trim="form.contactMobile" placeholder="请输入固定电话" maxlength="25" show-word-limit/>
                  </el-form-item>
                </td>
                <td><i class="require">*</i><b>移动电话：</b></td>
                <td>
                  <el-form-item prop="contactPhone">
                    <el-input v-model.trim="form.contactPhone" placeholder="请输入移动电话" maxlength="11" show-word-limit/>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td><i class="require">*</i><b>委托权限：</b></td>
                <td colspan="3">
                  <el-form-item prop="checkAuthority">1、
                    <el-radio-group v-model.trim="form.checkAuthority" @change="changeFlag">
                      <el-radio label="1">同意</el-radio>
                      <el-radio label="0">不同意</el-radio>
                    </el-radio-group>
                    <span>核对登记材料中的复印件并签署核对意见</span>
                  </el-form-item>
                  <el-form-item prop="updateErrorFile">2、
                    <el-radio-group v-model.trim="form.updateErrorFile" @change="changeFlag">
                      <el-radio label="1">同意</el-radio>
                      <el-radio label="0">不同意</el-radio>
                    </el-radio-group>
                    <span>修改企业自备文件的错误</span>
                  </el-form-item>
                  <el-form-item prop="updateTableFile">3、
                    <el-radio-group v-model.trim="form.updateTableFile" @change="changeFlag">
                      <el-radio label="1">同意</el-radio>
                      <el-radio label="0">不同意</el-radio>
                    </el-radio-group>
                    <span>修改有关表格的填写错误</span>
                  </el-form-item>
                  <el-form-item prop="receiveAuthority">4、
                    <el-radio-group v-model.trim="form.receiveAuthority" @change="changeFlag">
                      <el-radio label="1">同意</el-radio>
                      <el-radio label="0">不同意</el-radio>
                    </el-radio-group>
                    <span>领取营业执照和有关文书</span>
                  </el-form-item>
                </td>
              </tr>
            </table>
        </div>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="变更事项登记" name="second">
        <div class="el-table__header-wrapper dialog-table">
          <el-button type="primary" style="float:right" @click="addBgService"  class="data-btn">增加</el-button>
          <h3 class="title"><i class="el-icon-s-grid"></i>变更事项</h3>
          <el-form ref="bgForm" :model="bgForm"  label-width="0px">
            <table cellspacing="0" cellpadding="0" border="0" class="data-table">
              <colgroup>
                <col width="20%" />
                <col width="35%" />
                <col width="35%" />
                <col width="10%" />
              </colgroup>
              <tr>
                <th>变更事项</th>
                <th>原登记内容</th>
                <th>变更后登记内容</th>
                <th>操作</th>
              </tr>
              <tbody v-for="(item,index) in bgForm.bglist">
<!--              <tr v-if="item.up!=0">
                <td>{{item.serviceName}}</td>
                <td>{{item.oldService}}</td>
                <td>{{item.newService}}</td>
                <td><span>修改</span><span>删除</span></td>
              </tr>-->
              <tr>
                <td>
                  <el-form-item :prop="'bglist.' + index + '.serviceName'" :rules="bgRules.serviceName">
                    <el-input v-model.trim="item.serviceName" placeholder="请输入变更事项" maxlength="100" show-word-limit/>
                  </el-form-item>
                </td>
                <td>
                  <el-form-item :prop="'bglist.' + index + '.oldService'" :rules="bgRules.oldService">
                    <el-input v-model.trim="item.oldService" placeholder="请输入原登记内容" maxlength="1500" show-word-limit/>
                  </el-form-item>
                </td>
                <td>
                  <el-form-item prop="'bglist.' + index + '.newService'" :rules="bgRules.newService">
                    <el-input v-model.trim="item.newService" placeholder="请输入变更后登记内容" maxlength="1500" show-word-limit/>
                  </el-form-item>
                </td>
                <td>
                  <el-button
                    size="mini"
                    type="text"
                    icon="iconfont zfsoft-shanchu"
                    @click="delBg(index)">删除
                  </el-button>
                </td>
              </tr>
              </tbody>
            </table>
          </el-form>
        </div>
      </el-tab-pane>
      <el-tab-pane label="法定代表人信息" name="third">
        <div class="el-table__header-wrapper dialog-table">
          <h3 class="title"><i class="el-icon-s-grid"></i>新法定代表人信息</h3>
          <el-form ref="formLegal" :model="form" :rules="rules" label-width="0px">
            <table cellspacing="0" cellpadding="0" border="0" >
              <colgroup>
                <col width="15%" />
                <col width="35%" />
                <col width="15%" />
                <col width="35%" />
              </colgroup>
              <tr>
                <td><i class="require">*</i><b>姓名：</b></td>
                <td>
                  <el-form-item prop="legalPersonName">
                    <el-input v-model.trim="form.legalPersonName" placeholder="请输入姓名" maxlength="50" show-word-limit/>
                  </el-form-item>
                </td>
                <td><i class="require">*</i><b>国别(地区)：</b></td>
                <td>
                  <el-form-item prop="national">
                    <el-input v-model.trim="form.national" placeholder="请输入国别(地区)" maxlength="50" show-word-limit/>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td><i class="require">*</i><b>职务：</b></td>
                <td>
                  <el-form-item prop="legalPersonPost">
                    <el-radio-group v-model.trim="form.legalPersonPost" @change="changeFlag">
                      <el-radio label="0">董事长</el-radio>
                      <el-radio label="1">执行董事</el-radio>
                      <el-radio label="2">经理</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </td>
                <td><i class="require">*</i><b>产生方式：</b></td>
                <td>
                  <el-form-item prop="prodMethod">
                    <el-input v-model.trim="form.prodMethod" placeholder="请输入产生方式" maxlength="100" show-word-limit/>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td><i class="require">*</i><b>身份证件类型：</b></td>
                <td>
                  <el-form-item prop="legalPersonZjType">
                    <el-input v-model.trim="form.legalPersonZjType" placeholder="请输入身份证件类型" maxlength="50" show-word-limit/>
                  </el-form-item>
                </td>
                <td><i class="require">*</i><b>身份证件号码：</b></td>
                <td>
                  <el-form-item prop="legalPersonNumber">
                    <el-input v-model.trim="form.legalPersonNumber" placeholder="请输入身份证件号码" maxlength="50" show-word-limit/>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td><i class="require">*</i><b>固定电话：</b></td>
                <td>
                  <el-form-item prop="legalPersonMobile">
                    <el-input v-model.trim="form.legalPersonMobile" placeholder="请输入固定电话" maxlength="25" show-word-limit/>
                  </el-form-item>
                </td>
                <td><i class="require">*</i><b>移动电话：</b></td>
                <td>
                  <el-form-item prop="legalPersonPhone">
                    <el-input v-model.trim="form.legalPersonPhone" placeholder="请输入移动电话" maxlength="11" show-word-limit/>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td><i class="require">*</i><b>住所：</b></td>
                <td>
                  <el-form-item prop="legalPersonAddress">
                    <el-input v-model.trim="form.legalPersonAddress" placeholder="请输入住所" maxlength="100" show-word-limit/>
                  </el-form-item>
                </td>
                <td><i class="require">*</i><b>电子邮箱：</b></td>
                <td>
                  <el-form-item prop="legalPersonEmail">
                    <el-input v-model.trim="form.legalPersonEmail" placeholder="请输入电子邮箱" maxlength="25" show-word-limit/>
                  </el-form-item>
                </td>
              </tr>
            </table>
          </el-form>
        </div>
      </el-tab-pane>
      <el-tab-pane label="股东(发起人)、外国投资者出资情况" name="fourth">
        <div class="el-table__header-wrapper dialog-table">
          <el-button type="primary" style="float:right" @click="addCzqk" class="data-btn">增加</el-button>
          <h3 class="title"><i class="el-icon-s-grid"></i>股东(发起人)、外国投资者出资情况</h3>
          <el-form ref="czform" :model="czform" :rules="rules" label-width="0px">
            <table cellspacing="0" cellpadding="0" border="0" class="data-table">
              <colgroup>
                <col width="15%" />
                <col width="10%" />
                <col width="10%" />
                <col width="15%" />
                <col width="10%" />
                <col width="10%" />
                <col width="10%" />
                <col width="10%" />
                <col width="10%" />
              </colgroup>
              <tr>
                <th>姓名</th>
                <th>国别(地区)</th>
                <th>证件类型</th>
                <th>证件号码</th>
                <th>认缴出资额</th>
                <th>实缴出资额</th>
                <th>出资(认缴)时间</th>
                <th>出资方式</th>
                <th>出资比例</th>
              </tr>
              <tbody v-for="(czItem,dz) in form.czqkList">
              <tr>
                <td>
                  <el-form-item>
                    <el-input v-model.trim="czItem.username" placeholder="请输入姓名" maxlength="50" show-word-limit/>
                  </el-form-item>
                </td>
                <td>
                  <el-form-item>
                    <el-input v-model.trim="czItem.national" placeholder="请输入国别(地区)" maxlength="50" show-word-limit/>
                  </el-form-item>
                </td>
                <td>
                  <el-form-item>
                    <el-input v-model.trim="czItem.zjType" placeholder="请输入证件类型" maxlength="20" show-word-limit/>
                  </el-form-item>
                </td>
                <td>
                  <el-form-item>
                    <el-input v-model.trim="czItem.zjNumber" placeholder="请输入证件号码" maxlength="50" show-word-limit/>
                  </el-form-item>
                </td>
                <td>
                  <el-form-item>
                    <el-input v-model.trim="czItem.rjczNum" placeholder="请输入认缴出资额" maxlength="10" show-word-limit/>
                  </el-form-item>
                </td>
                <td>
                  <el-form-item>
                    <el-input v-model.trim="czItem.sjczNum" placeholder="请输入实缴出资额" maxlength="10" show-word-limit/>
                  </el-form-item>
                </td>
                <td>
                  <el-form-item>
                    <el-input v-model.trim="czItem.czrjTime" placeholder="请输入出资(认缴)时间" show-word-limit/>
                  </el-form-item>
                </td>
                <td>
                  <el-form-item>
                    <el-input v-model.trim="czItem.czfs" placeholder="请输入出资方式" maxlength="100" show-word-limit/>
                  </el-form-item>
                </td>
                <td>
                  <el-form-item>
                    <el-input v-model.trim="czItem.czbl" placeholder="请输入出资比例" maxlength="10" show-word-limit/>
                  </el-form-item>
                </td>
                <td>
                  <el-button
                    size="mini"
                    type="text"
                    icon="iconfont zfsoft-shanchu"
                    @click="delBgCz(dz)">删除
                  </el-button>
                </td>
              </tr>
              </tbody>
            </table>
          </el-form>
        </div>
      </el-tab-pane>
    </el-tabs>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
  </div>
</template>

<script>
import {
  validatePhone,
  validatePostCode,
  validateEmails,
  validUnifiedCredit
} from '@/utils/validate';
import {saveOrUpdateInfo,getCaseFormInfo} from "@/api/zc/businessManagement/windowAcceptance";
export default {
  components: {},
  name: "NzgsbgForm",
  props:['caseOid'],
  data() {
    return {
      activeName: 'first',
      form:{caseOid:this.caseOid,czqkList:[]},
      rules:{
        companyName:[{
          required: true,
          message: "必填项",
          trigger: "blur"
        }],
        socialCreditCode:[{
          required: true,
          message: "必填项",
          trigger: "blur"
        }/*,{
          validator: validUnifiedCredit,
          message: "请输入正确信用代码证",
          trigger: 'blur'
        }*/],
        mobile:[{
          required: true,
          message: "必填项",
          trigger: "blur"
        }],
        code:[{
          required: true,
          message: "必填项",
          trigger: "blur"
        },{
          validator: validatePostCode,
          trigger: 'blur'
        }],
        contactUserName:[{
          required: true,
          message: "必填项",
          trigger: "blur"
        }],
        contactCredentialType:[{
          required: true,
          message: "必填项",
          trigger: "blur"
        }],
        contactCredentialNumber:[{
          required: true,
          message: "必填项",
          trigger: "blur"
        }],
        contactMobile:[{
          required: true,
          message: "必填项",
          trigger: "blur"
        }],
        contactPhone:[{
          required: true,
          message: "必填项",
          trigger: "blur"
        },{
          validator: validatePhone,
          trigger: 'blur'
        }],
        /*checkAuthority:[{
          required: true,
          message: "必填项",
          trigger: "blur"
        }],
        updateErrorFile:[{
          required: true,
          message: "必填项",
          trigger: "blur"
        }],
        updateTableFile:[{
          required: true,
          message: "必填项",
          trigger: "blur"
        }],
        receiveAuthority:[{
          required: true,
          message: "必填项",
          trigger: "blur"
        }],*/
        legalPersonName:[{
          required: true,
          message: "必填项",
          trigger: "blur"
        }],
        national:[{
          required: true,
          message: "必填项",
          trigger: "blur"
        }],
        legalPersonPost:[{
          required: true,
          message: "必填项",
          trigger: "blur"
        }],
        prodMethod:[{
          required: true,
          message: "必填项",
          trigger: "blur"
        }],
        legalPersonZjType:[{
          required: true,
          message: "必填项",
          trigger: "blur"
        }],
        legalPersonNumber:[{
          required: true,
          message: "必填项",
          trigger: "blur"
        }],
        legalPersonMobile:[{
          required: true,
          message: "必填项",
          trigger: "blur"
        }],
        legalPersonPhone:[{
          required: true,
          message: "必填项",
          trigger: "blur"
        },{
          validator: validatePhone,
          trigger: 'blur'
        }],
        legalPersonAddress:[{
          required: true,
          message: "必填项",
          trigger: "blur"
        }],
        legalPersonEmail:[{
          required: true,
          message: "必填项",
          trigger: "blur"
        },{
          validator: validateEmails,
          trigger: 'blur'
        }],
      },
      czform:[],
      bgForm:{bglist:[]},
      //动态表单验证
      bgRules:{
        serviceName: [{
          required: true,
          message: "必填项",
          trigger: "blur"
        }],
        oldService: [{
          required: true,
          message: "必填项",
          trigger: "blur"
        }],
        newService: [{
          required: true,
          message: "必填项",
          trigger: "blur"
        }],
      },
    };
  },
  created() {
    if(this.caseOid){
      this.getFormInfo();
    }
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    },

    // 表单重置
    reset() {
      this.form = {
        name: null
      };
      this.resetForm("form");
    },
    addBgService(){
      let service={bgOid:"",caseOid:"",serviceName:"",oldService:"",newService:"",up:"0"};//当up为0时候可以进行编辑
      this.bgForm.bglist.push(service)
    },
    delBg(index){
      this.bgForm.bglist.splice(index,1);
    },
    addCzqk(){
      let czqk={czOid:"",caseOid:"",username:"",national:"",zjType:"",zjNumber:"",rjczNum:"",sjczNum:"",czrjTime:"",czfs:"",czbl:""};
      this.form.czqkList.push(czqk)

    },
    delBgCz(index){
      this.form.czqkList.splice(index,1);
    },
    /** 提交按钮 */
    submitForm () {
      this.form.bglist=this.bgForm.bglist;
      this.form.czform=this.czform;
      this.$refs["form"].validate(valid => {
        if (valid) {
          saveOrUpdateInfo(this.form).then(response=>{
            let res=response.data;
            if(res.status=='true'){
              this.cancel();
            }else{
              this.$message.error("保存失败！");
            }
          })
        }
      });
    },
    getFormInfo(){
      getCaseFormInfo(this.caseOid).then(response=>{
        let res=response.data;
        if(res){
          this.form=res;
          this.form.czqkList=res.czqkList;
          this.bgForm.bglist=res.bglist;
          this.czform=res.czform;
          this.form.bglist= {};
          this.form.czform= {};
        }
      })
    },
    changeFlag(val) {
      let _that = this;
    },
    cancel(){
      this.$emit("close-bgForm");
    }
  }
}
</script>
<style scoped>
  .el-tree>.el-tree-node{
    min-width:100%;
    display: inline-block;
  }
</style>

