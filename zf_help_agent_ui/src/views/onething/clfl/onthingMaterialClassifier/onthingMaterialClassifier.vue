/**
* 一件事材料分类器
* @author: liangss
* @date: 2021-06-11
*/
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--一件事目录列表数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="158px">
          <el-form-item label="一件事目录名称" prop="comboDirectoryName">
            <el-input
              v-model.trim="queryParams.comboDirectoryName"
              placeholder="请输入一件事目录名称"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置</el-button>
          </el-form-item>
        </el-form>

        <el-table v-loading="loading" :data="comboDirectoryList" border :fit="true">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{scope.$index + 1}}</span>
            </template>
          </el-table-column>
          <el-table-column label="所属主题"   align="center" prop="themeName" :show-overflow-tooltip="true"/>
          <!--<el-table-column label="一件事目录编号" align="center" prop="comboDirectoryCode" />-->
          <el-table-column label="一件事目录名称"    align="center" prop="comboDirectoryName" :show-overflow-tooltip="true"/>
          <el-table-column label="所属区划"   align="center" prop="districtName" :show-overflow-tooltip="true"/>
          <el-table-column label="主办部门"    align="center" prop="mainOrganName" :show-overflow-tooltip="true"/>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleView(scope.row)"  >查看</el-button>
              <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai"  @click="handleInit(scope.row)" >材料分类配置</el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>
      </el-col>
    </el-row>
    <!-- 添加或修改信息对话框 -->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="title" :visible.sync="openInit" v-if="openInit" width="1050px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="0px" class="dialog-table">
        <table cellspacing="0" cellpadding="0" border="0" width="910px" class="zf-zc-table">
          <colgroup>
            <col width="10%" />
            <col width="40%" />
            <col width="10%" />
            <col width="40%" />
          </colgroup>
          <tr>
            <td colspan="1">
              <b>一件事名称：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="comboDirectoryName">
                <el-col :span="24">
                  {{ form.comboDirectoryName }}
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>是否开启自动分类状态：</b></td>
            <td colspan="3">
              <el-form-item prop="autoClassificationStatus">
                <el-radio-group v-model="form.autoClassificationStatus" @change="changeProxyFlag">
                  <el-radio label="Y">是</el-radio>
                  <el-radio label="N">否</el-radio>
                </el-radio-group>
              </el-form-item>
            </td>
          </tr>

          <tr  v-if="proxy_show">
            <td><i class="require">*</i><b>分类器ID：</b></td>
            <td colspan="3">
              <el-form-item prop="classifierId">
                <el-col :span="24">
                  <el-input v-model.trim="form.classifierId" name="classifierId" maxlength="6"
                          ></el-input>
                </el-col>
              </el-form-item>
            </td>

          </tr>
        </table>
        <br/>

        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table" v-if="proxy_show">
          <colgroup>
            <col width="50" />
            <col width="100" />
            <col width="300" />

          </colgroup>
          <tr>
            <th>序号</th>
            <th>材料名称</th>
            <th>百度模板id</th>
          </tr>
          <template v-for="(ruleForm,index) in form.directoryMaterList">

            <tr>
              <td>
                <el-form-item prop="xuhao">
                  {{index+1}}
                </el-form-item>
              </td>
              <td>
                <el-form-item>
                  <el-input v-model.trim="ruleForm.materialName" readonly></el-input>
                </el-form-item>
              </td>
              <td>
                <el-form-item>
                  <el-input v-model.trim="ruleForm.baiduTemplateIds"></el-input>
                </el-form-item>
              </td>
            </tr>
          </template>

          <template v-for="(serviceMater,index) in form.comboDireMaterRelList">
            <tr>
              <td>
                <el-form-item prop="xuhao">
                  {{index+1+form.directoryMaterList.length}}
                </el-form-item>
              </td>
              <td>
                <el-form-item>
                  <el-input v-model.trim="serviceMater.sxServiceMaterial.materialName" readonly></el-input>
                </el-form-item>
              </td>
              <td>
                <el-form-item>
                  <el-input v-model.trim="serviceMater.baiduTemplateIds"></el-input>
                </el-form-item>
              </td>
            </tr>
          </template>

        </table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 信息详细 -->
    <el-dialog v-dialog-drag :visible.sync="openView" v-if="openView"  title="目录信息" width="70%" height="600px" scrollbar append-to-body>
        <combo-directory-view :comboDirectoryOid="comboDirectoryOidView" @combo-directory="comboDirectoryClose"></combo-directory-view>
    </el-dialog>
  </div>
</template>

<script>
import { queryDirectoryMaterialList,pageList,page} from "@/api/onething/clzs/mealCatalogRelatedManage/mealCatalogRelatedManage.js";
import {queryComboDireSxMaterList,saveOrUpdateMaterialClassifier} from "@/api/onething/clfl/onthingMaterialClassifier/onthingMaterialClassifier";
import { queryServiceTypeSimpleTree } from "@/api/sxpt/serviceType";
import Treeselect from '@riophae/vue-treeselect';
import '@riophae/vue-treeselect/dist/vue-treeselect.css';
import ComboDirectoryView from "@/views/onething/sxpz/comboDirectory/comboDirectoryView";
export default {
  name: "MealCatalogRelatedManage",
  components: {ComboDirectoryView, Treeselect},
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 一件事目录数据
      comboDirectoryList: [],
      // 事项类型
      serviceTypeOptions: [],
      //目录列表
      catalogList:[],

      // 弹出层标题
      title: "",
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,

      //是否开启分类
      proxy_show:false,
      // 查看显示弹出层目录主键
      comboDirectoryOidView: '',
      expandedKeys: [],
      userExpandedKeys:[],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        comboDirectoryName: ""

      },
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单参数
      form: {
        id: '', //逻辑主键
        serviceOid: "",
        comboDirectoryOid:"",
        comboDirectoryName: "",
        autoClassificationStatus:"N",
        directoryMaterList:[],
        comboDireMaterRelList:[],
        materialCatalogOid:[],
        catalogName:[],
        ifCharge : 0,
        isZjfw:0,
        appointmentFlag:0,
        handleForm:'0',
        onlinePayFlag:0,
        expressFlag:0,
        unionOrganFlag:0,
        countToScence:0


      },
      // 表单校验
      rules: {},
    };
  },
  computed: {
    // 计算属性的 getter
    reversedCountToScence: function () {
      if (this.form.countToScence) {
        if(this.form.countToScence=="0"){
          return '0次';
        }else if(this.form.countToScence=="1"){
          return '1次';
        }else if(this.form.countToScence=="2"){
          return '2次';
        }else if(this.form.sex=="3"){
          return '多次';
        }
      }
      return ''
    },
    reversedExpressFlag: function () {
      if (this.form.expressFlag) {
        if(this.form.expressFlag==0){
          return '否';
        }else if(this.form.expressFlag==1){
          return '是';
        }
      }
      return ''
    },
    reversedOnlinePayFlag: function () {
      if (this.form.onlinePayFlag) {
        if(this.form.onlinePayFlag==0){
          return '否';
        }else if(this.form.onlinePayFlag==1){
          return '是';
        }
      }
      return ''
    },
    reversedHandleForm: function () {
      if (this.form.handleForm) {
        if(this.form.handleForm=="0"){
          return '窗口办理';
        }else if(this.form.handleForm=="1"){
          return '网上办理';
        }else if(this.form.handleForm=="2"){
          return '一体化办理';
        }
      }
      return ''
    },
    reversedAppointmentFlag: function () {
      if (this.form.appointmentFlag) {
        if(this.form.appointmentFlag==0){
          return '否';
        }else if(this.form.appointmentFlag==1){
          return '是';
        }
      }
      return ''
    },
    reversedUnionOrganFlag: function () {
      if (this.form.unionOrganFlag) {
        if(this.form.unionOrganFlag==0){
          return '否';
        }else if(this.form.unionOrganFlag==1){
          return '是';
        }
      }
      return ''
    },
    reversedIsZjfw: function () {
      if (this.form.isZjfw) {
        if(this.form.isZjfw==0){
          return '否';
        }else if(this.form.isZjfw==1){
          return '是';
        }
      }
      return ''
    },
    reversedIfCharge: function () {
      if (this.form.ifCharge) {
        if(this.form.ifCharge==0){
          return '否';
        }else if(this.form.ifCharge==1){
          return '是';
        }
      }
      return ''
    }

  },
  created() {
    this.getList();
    this.getServiceTypeTree();
    this.getMaterialCatalogList();
  },
  methods: {

    /** 查询列表 */
    getList() {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.comboDirectoryList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });

      console.log(this.queryParams)
    },
    /** 是否启动分类器*/
    changeProxyFlag(val) {
      this.proxy_show = (val === 'Y') ? true : false;
    },
    /** 获取事项类型树 */
    getServiceTypeTree(serviceTypeOid) {
      let _that = this;
      queryServiceTypeSimpleTree(serviceTypeOid).then(response => {
        _that.serviceTypeOptions = response.data;
      });
    },
    /** 查询目录列表 */
    getMaterialCatalogList() {
      let _that = this;
      pageList().then(res => {
        this.catalogList=res.data;
      });
    },

    // 取消按钮
    cancel() {
      this.openInit = false;
      this.reset();
    },
    // 表单重置
    reset() {
      Object.assign(this.form, this.$options.data().form)
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 查看按钮操作 */
    handleView(row) {
      this.comboDirectoryOidView = row.comboDirectoryOid;
      this.openView = true;
      this.title = "查看一件事目录信息";
    },
    comboDirectoryClose(){
      this.openView=false;
    },
    /** 新增和修改按钮操作 */
    handleInit(row) {
      let _that = this;
      _that.reset();
      //alert(row.autoClassificationStatus);
      //console.log(JSON.stringify(row));
      _that.form.comboDirectoryName = row.comboDirectoryName;
      _that.form.comboDirectoryOid = row.comboDirectoryOid;
      _that.form.id = row.id;
      if(row.autoClassificationStatus==null){
        _that.form.autoClassificationStatus = "N";
        _that.proxy_show = false ;
      }else {
        if(row.autoClassificationStatus=='Y'){
          _that.proxy_show = true ;
        }else{
          _that.proxy_show = false ;
        }
        _that.form.autoClassificationStatus = row.autoClassificationStatus;
        _that.form.classifierId = row.classifierId;
      }


      if(row.comboDirectoryOid){
        queryDirectoryMaterialList(row.comboDirectoryOid).then(response => {
          _that.openInit = true;
          let directoryMaters=  response.data;
          for(let cailiao of directoryMaters){
            this.form.directoryMaterList.push(cailiao);
          }
        });
        queryComboDireSxMaterList(row.comboDirectoryOid).then(response => {
          _that.openInit = true;
          let sxServiceMaterials=  response.data;
         /* alert(sxServiceMaterials)*/
          for(let cailiao of sxServiceMaterials){
            this.form.comboDireMaterRelList.push(cailiao);
          }
        });

      }else{
        _that.openInit = false;
      }

      _that.title ="一件事目录材料分类配置";

    },
    /** 提交按钮 */
    submitForm: function() {
      let _that = this;

       console.log(JSON.stringify(_that.form));
      if (this.form.autoClassificationStatus == 'Y') {
        if (!this.form.classifierId) {
          this.$message.error("分类器ID不能为空！");
          return false;
        }
      }

      saveOrUpdateMaterialClassifier(_that.form).then(response => {
        if (response.code === 200) {
          _that.msgSuccess("保存成功");
          _that.addDialogShow = false;
          setTimeout(() => {
            _that.getList();
            _that.openInit = false;
          }, 10);
        }
      });


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
// .dialog-table table {
//   width: 100%;
// }
// .dialog-table table {
//   border: 1px solid #dfe6ec;
//   border-collapse: collapse;
// }

.dialog-table table tr td {
  // border: 1px solid #dfe6ec;
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
.primary-table h3 {
  font-size: 14px;
  font-weight: normal;
  color: #333;
  margin: 0px 0px 10px 0px;
}
.primary-table {
  padding: 20px;
  box-sizing: border-box;
}
.primary-table .handle-btn {
  border: none;
  background: none;
  padding: 0;
  font-size: 16px;
}
.primary-table .el-table,
.primary-table .el-table th {
  font-size: 13px;
}
.primary-table .el-form-item:first-child {
  margin-left: 8px;
}
.primary-table .el-form-item {
  margin-bottom: 0;
  width: 130px;
  margin-right: 0;
  margin-left: 14px;
  font-size: 13px;
}
.primary-table .el-form-item__content {
  font-size: 13px;
}
.primary-table .inputTable .el-form-item:first-child {
  margin-left: 0;
}
.primary-table .inputTable .el-form-item {
  width: 100px;
}
.el-select {
  width: 90%;
}
.table1 table tr td{
  background-color: transparent !important;
}
</style>
