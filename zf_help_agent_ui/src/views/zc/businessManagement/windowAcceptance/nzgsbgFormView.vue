/**
* 内资公司变更法定代表人表单
*/
<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="基本信息" name="first">
        <el-form ref="form" :model="form" label-width="0px">
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
                <td><b>名称：</b></td>
                <td colspan="3">
                  {{form.companyName}}
                </td>
              </tr>
              <tr>
                <td><b>统一社会信用代码：</b></td>
                <td colspan="3">
                  {{form.socialCreditCode}}
                </td>
              </tr>
              <tr>
                <td><b>联系电话：</b></td>
                <td>
                 {{form.mobile}}
                </td>
                <td><b>邮政编码：</b></td>
                <td>
                  {{form.code}}
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
                <td><b>姓名：</b></td>
                <td colspan="3">
                  {{form.contactUserName}}
                </td>
              </tr>
              <tr>
                <td><b>身份证件类型：</b></td>
                <td>
                  {{form.contactCredentialType}}
                </td>
                <td><b>身份证件号码：</b></td>
                <td>
                  {{form.contactCredentialNumber}}
                </td>
              </tr>
              <tr>
                <td><b>固定电话：</b></td>
                <td>
                  {{form.contactMobile}}
                </td>
                <td><b>移动电话：</b></td>
                <td>
                  {{form.contactPhone}}
                </td>
              </tr>
              <tr>
                <td><b>委托权限：</b></td>
                <td colspan="3">
                  <p>
                  1、<span v-if="form.checkAuthority==1">同意</span><span v-else>不同意</span>
                    <span>核对登记材料中的复印件并签署核对意见</span>
                  </p>
                  <p>
                  2、<span v-if="form.updateErrorFile==1">同意</span><span v-else>不同意</span>
                    <span>修改企业自备文件的错误</span>
                  </p>
                  <p>
                  3、<span v-if="form.updateTableFile==1">同意</span><span v-else>不同意</span>
                    <span>修改有关表格的填写错误</span>
                  </p>
                 4、<span v-if="form.receiveAuthority==1">同意</span><span v-else>不同意</span>
                    <span>领取营业执照和有关文书</span>
                </td>
              </tr>
            </table>
        </div>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="变更事项登记" name="second">
          <h3 class="title"><i class="el-icon-s-grid"></i>变更事项</h3>
            <el-table :data="bgForm.bglist" border>
              <el-table-column label="序号" align="center" width="50">
                <template slot-scope="scope">
                  {{ scope.$index + 1 }}
                </template>
              </el-table-column>
              <el-table-column label="变更事项" align="center" :show-overflow-tooltip="true" prop="serviceName" />
              <el-table-column label="原登记内容" align="center" :show-overflow-tooltip="true" prop="oldService">
              </el-table-column>
              <el-table-column label="变更后登记内容" align="center" :show-overflow-tooltip="true" prop="newService" />
            </el-table>
      </el-tab-pane>
      <el-tab-pane label="法定代表人信息" name="third">
        <div class="el-table__header-wrapper dialog-table">
          <h3 class="title"><i class="el-icon-s-grid"></i>新法定代表人信息</h3>
          <el-form ref="formLegal" :model="form" label-width="0px">
            <table cellspacing="0" cellpadding="0" border="0" >
              <colgroup>
                <col width="15%" />
                <col width="35%" />
                <col width="15%" />
                <col width="35%" />
              </colgroup>
              <tr>
                <td><b>姓名：</b></td>
                <td>
                 {{form.legalPersonName}}
                </td>
                <td><b>国别(地区)：</b></td>
                <td>
                 {{form.national}}
                </td>
              </tr>
              <tr>
                <td><b>职务：</b></td>
                <td>
                      <p v-if="form.legalPersonPost==0">董事长</p>
                      <p v-if="form.legalPersonPost==1">执行董事</p>
                      <p v-if="form.legalPersonPost==2">经理</p>
                </td>
                <td><b>产生方式：</b></td>
                <td>
                  {{form.prodMethod}}
                </td>
              </tr>
              <tr>
                <td><b>身份证件类型：</b></td>
                <td>
                  {{form.legalPersonZjType}}
                </td>
                <td><b>身份证件号码：</b></td>
                <td>
                  {{form.legalPersonNumber}}
                </td>
              </tr>
              <tr>
                <td><b>固定电话：</b></td>
                <td>
                  {{form.legalPersonMobile}}
                </td>
                <td><b>移动电话：</b></td>
                <td>
                  {{form.legalPersonPhone}}
                </td>
              </tr>
              <tr>
                <td><b>住所：</b></td>
                <td>
                 {{form.legalPersonAddress}}
                </td>
                <td><b>电子邮箱：</b></td>
                <td>
                  {{form.legalPersonEmail}}
                </td>
              </tr>
            </table>
          </el-form>
        </div>
      </el-tab-pane>
      <el-tab-pane label="股东(发起人)、外国投资者出资情况" name="fourth">
          <h3 class="title"><i class="el-icon-s-grid"></i>股东(发起人)、外国投资者出资情况</h3>
        <el-table :data="form.czqkList" border>
          <el-table-column label="序号" align="center" width="50">
            <template slot-scope="scope">
              {{ scope.$index + 1 }}
            </template>
          </el-table-column>
          <el-table-column label="姓名" align="center" :show-overflow-tooltip="true" prop="username" />
          <el-table-column label="国别(地区)" align="center" :show-overflow-tooltip="true" prop="national">
          </el-table-column>
          <el-table-column label="证件类型" align="center" :show-overflow-tooltip="true" prop="zjType" />
          <el-table-column label="证件号码" align="center" :show-overflow-tooltip="true" prop="zjNumber">
          </el-table-column>
          <el-table-column label="认缴出资额" align="center" :show-overflow-tooltip="true" prop="rjczNum" />
          <el-table-column label="实缴出资额" align="center" :show-overflow-tooltip="true" prop="sjczNum"/>
          <el-table-column label="出资(认缴)时间" align="center" :show-overflow-tooltip="true" prop="czrjTime"/>
          <el-table-column label="出资方式" align="center" :show-overflow-tooltip="true" prop="czfs"/>
          <el-table-column label="出资比例" align="center" :show-overflow-tooltip="true" prop="czbl"/>
        </el-table>
      </el-tab-pane>
    </el-tabs>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">关闭</el-button>
      </div>
  </div>
</template>

<script>
import {getCaseFormInfo} from "@/api/zc/businessManagement/windowAcceptance";
export default {
  components: {},
  name: "NzgsbgForm",
  props:['caseOid'],
  data() {
    return {
      activeName: 'first',
      form:{caseOid:this.caseOid,czqkList:[]},
      czform:[],
      bgForm:{bglist:[]},
    };
  },
  created() {
    if(this.caseOid){
      this.getFormInfo();
    }
  },
  methods: {
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
      this.$emit("close-bgnzForm");
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

