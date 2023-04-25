/**
* @Author: liangss
* @Description: 智能预审结果
*/
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="24" :xs="24">
        <!--<el-table v-loading="loading" :data="preTrialResultVoList" border>
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <template v-if="scope.row.childStatus!='Y'">
                 <span>{{ scope.row.xuhao }}</span>
              </template>
            </template>
          </el-table-column>

          <el-table-column label="审核项名称" width="380" align="center" prop="name"/>
          <el-table-column label="审核结果" width="380"  align="center">
            <template slot-scope="scope">
              <template v-if="scope.row.resultStatus=='0'||scope.row.resultStatus==0">
                通过
              </template>
              <template v-if="scope.row.resultStatus=='1'||scope.row.resultStatus==1">
                不通过
              </template>
              <template v-if="scope.row.resultStatus=='2'||scope.row.resultStatus==2">
                无需审核
              </template>
              <template v-if="scope.row.resultStatus=='3'||scope.row.resultStatus==3">
                无审核验证规则
              </template>
            </template>
          </el-table-column>

          <el-table-column label="查看审核详情" width="350"  type="index" align="center">
            <template slot-scope="scope">
              <template v-if="scope.row.parentStatus!='Y' && scope.row.childStatus=='Y' ">
                <el-button  size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleView(scope.row)"  >查看</el-button>
              </template>
            </template>
          </el-table-column>

        </el-table>-->

        <template>
          <div class="primary-table">
            <h3>
              <i class="el-icon-document"></i>审核结果
            </h3>
            <el-table :data="preTrialResultVoList" border style="width: 100%">
              <el-table-column type="index" width="50" label="序号" align="center">
                <template slot-scope="scope">
                  <template v-if="scope.row.childStatus!='Y'">
                    <span>{{ scope.row.xuhao }}</span>
                  </template>
                </template>
              </el-table-column>
              <el-table-column label="审核名称" prop="name" width="300">
              </el-table-column>
              <el-table-column label="审核结果"  width="200">
                <template slot-scope="scope">
                  <template v-if="scope.row.resultStatus=='0'||scope.row.resultStatus==0">
                    通过
                  </template>
                  <template v-if="scope.row.resultStatus=='1'||scope.row.resultStatus==1">
                    不通过
                  </template>
                  <template v-if="scope.row.resultStatus=='2'||scope.row.resultStatus==2">
                    无需审核
                  </template>
                  <template v-if="scope.row.resultStatus=='3'||scope.row.resultStatus==3">
                    无审核验证规则
                  </template>
                </template>

              </el-table-column>
              <el-table-column label="查看审核详情" prop="btn">
                <template slot-scope="scope">
                  <template v-if="scope.row.parentStatus!='Y' && scope.row.childStatus=='Y' && (scope.row.resultStatus=='0'||scope.row.resultStatus==0 || scope.row.resultStatus=='1'||scope.row.resultStatus==1)">
                    <el-button  type="primary"  size="mini" icon="el-icon-edit"  @click="handleView(scope.row)"  >查看</el-button>
                  </template>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </template>
      </el-col>
    </el-row>

    <el-dialog title="查看审核详情" :visible.sync="openView" width="800px" v-if="openView" append-to-body>
      <el-form ref="form" :model="form" label-width="0" size="mini" >
        <el-table v-loading="loading" :data="preTrialResultview" border :show-header="false">
          <el-table-column  width="200" align="center" prop="name"/>
          <el-table-column   >
            <template slot-scope="scope">
              <template v-if="scope.row.memo!=''&&scope.row.memo!=null  ">
             <span style="color:red"> ({{scope.row.memo}})</span>
              </template>
              {{scope.row.words}}
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import Treeselect from '@riophae/vue-treeselect';
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {intelligentPretrial,viewResult} from "@/api/onelicence/industryManager/industryCaseAccept/industryIntelligentPretrial";
export default {
  name: "advisoryRegistration",
  props: ['caseOid','caseMaterialOid','caseFileRecOid','cataOid'],
  components: {Treeselect},
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      preTrialResultVoList:[],
      preTrialResultview:[],
      openView:false,
    };
  },
  created() {
    this.loading = true;
    this.getintelligentPretrial();
    /*this.loading = false;*/
  },
  methods: {
    //获取材料预审结果
    getintelligentPretrial(){
      let caseOid=this.caseOid;
      let caseMaterialOid=this.caseMaterialOid;
      let caseFileRecOid=this.caseFileRecOid;
      let cataOid=this.cataOid;
      intelligentPretrial(caseOid).then(response => {
        this.preTrialResultVoList=response.data.preTrialResultVoList;
       /* alert(this.preTrialResultVoList)*/
      /*  console.log(response.data)*/
        this.loading = false;
      })
    },
    handleView(row){
      let caseOid=this.caseOid;
      let caseMaterialOid=row.industryCaseMaterial.caseMaterialOid;
      let cataOid=row.catalogOid;
      let caseFileRecOid="";
      viewResult(caseOid,caseFileRecOid,caseMaterialOid,cataOid).then(response => {
        this.openView=true;
        this.preTrialResultview=response.data.data;
       // alert(JSON.stringify(this.preTrialResultview))
     /* alert(JSON.stringify(response.data))
        alert(this.preTrialResultview)*/
      })
     /* "catalogOid":"064539e108a24b0d97b81d6f7fe07844","materialOid":"297ea8f9738ee02c01738f28ad71009f",*/
    }

  },
};
</script>
<style lang="scss" scoped>
.dialog-table {
  padding: 20px;
  box-sizing: border-box;
}
.dialog-table h3 {
  font-size: 14px;
  font-weight: normal;
  color: #333;
  margin: 0px 0px 10px 0px;
}
.dialog-table table {
  width: 100%;
}
.dialog-table table {
  border: 1px solid #dfe6ec;
  border-collapse: collapse;
}

.dialog-table table tr td {
  border: 1px solid #dfe6ec;
  padding: 17px 8px;
  font-size: 12px;
  color: #515a6e;
  text-align: right;
}
.dialog-table table tr td:nth-of-type(2n) {
  color: #606266;
  text-align: left;
}
.dialog-table .el-form-item {
  margin-bottom: 0;
}
.el-form-item--small .el-form-item_content {
  margin-left: 0px !important;
}
.line {
  text-align: center;
}
</style>
