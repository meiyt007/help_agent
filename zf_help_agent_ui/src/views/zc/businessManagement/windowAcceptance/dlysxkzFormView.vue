<template>
  <div class="app-container">
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
            <td><b>业户名称：</b></td>
            <td colspan="3">
              {{form.companyName}}
            </td>
          </tr>
          <tr>
            <td><b>地址：</b></td>
            <td colspan="3">
              {{form.address}}
            </td>
          </tr>
          <tr>
            <td><b>法定代表人身份证号：</b></td>
            <td>
              {{form.legalIdcard}}
            </td>
            <td><b>联系电话：</b></td>
            <td>
              {{form.mobile}}
            </td>
          </tr>
        </table>
      </div>

        <h3 class="title"><i class="el-icon-s-grid"></i>货运车辆基本情况</h3>
        <el-table :data="form.carList" border>
          <el-table-column label="序号" align="center" width="50">
            <template slot-scope="scope">
              {{ scope.$index + 1 }}
            </template>
          </el-table-column>
          <el-table-column label="运输证号" align="center" :show-overflow-tooltip="true" prop="transportLicenseNo" />
          <el-table-column label="车辆牌照" align="center" :show-overflow-tooltip="true" prop="carLicense">
          </el-table-column>
          <el-table-column label="厂牌型号" align="center" :show-overflow-tooltip="true" prop="cpxhNum" />
          <el-table-column label="车辆类型" align="center" :show-overflow-tooltip="true" prop="carType">
          </el-table-column>
          <el-table-column label="吨位（吨）" align="center" :show-overflow-tooltip="true" prop="weightNum" />
          <el-table-column label="燃料" align="center" :show-overflow-tooltip="true" prop="fuel"/>
          <el-table-column label="轴数" align="center" :show-overflow-tooltip="true" prop="axesNum"/>
          <el-table-column label="技术等级" align="center" :show-overflow-tooltip="true" prop="technicalLevel"/>
        </el-table>

        <h3 class="title"><i class="el-icon-s-grid"></i>驾驶员基本情况</h3>
        <el-table :data="form.driverList" border>
          <el-table-column label="序号" align="center" width="50">
            <template slot-scope="scope">
              {{ scope.$index + 1 }}
            </template>
          </el-table-column>
          <el-table-column label="姓名" align="center" :show-overflow-tooltip="true" prop="driverName" />
          <el-table-column label="性别" align="center" :show-overflow-tooltip="true" prop="sex">
          </el-table-column>
          <el-table-column label="年龄" align="center" :show-overflow-tooltip="true" prop="age" />
          <el-table-column label="文化程度" align="center" :show-overflow-tooltip="true" prop="eduLevel">
          </el-table-column>
          <el-table-column label="取得驾驶证时间" align="center" :show-overflow-tooltip="true" prop="linceseTime" />
          <el-table-column label="准驾车型" align="center" :show-overflow-tooltip="true" prop="carTypeUse"/>
          <el-table-column label="从业资格证号" align="center" :show-overflow-tooltip="true" prop="cyzgNum"/>
        </el-table>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="cancel">关闭</el-button>
    </div>
  </div>
</template>

<script>
import {getCaseFormInfo} from "@/api/zc/businessManagement/windowAcceptance";
export default {
  components: {},
  name: "dlysxkzFormView",
  props: ['caseOid'],
  data() {
    return {
      form:{caseOid:this.caseOid,carList:[],driverList:[]},

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
          this.form.carList=res.carList;
          this.form.driverList=res.driverList;
          this.form.companyName=res.companyName;
          this.form.address=res.address;
          this.form.legalIdcard=res.legalIdcard;
          this.form.mobile=res.mobile;
        }
      })
    },
    cancel(){
      this.$emit("close-bgViewForm");
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

