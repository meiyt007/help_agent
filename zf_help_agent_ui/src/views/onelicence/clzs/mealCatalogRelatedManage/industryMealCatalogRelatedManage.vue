/**
* 一件事目录关联管理
* @author: chenjm
* @date: 2020-11-06
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
              <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai"  @click="handleInit(scope.row)" >目录关联</el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>
      </el-col>
    </el-row>
    <!-- 添加或修改信息对话框 -->
    <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="openInit" v-if="openInit" width="1050px" append-to-body>
          <el-form ref="form" :model="form" :rules="rules" label-width="0px" class="dialog-table">
       <table cellspacing="0" cellpadding="0" border="0" class="el-table__body" width="910px">
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
               </table>
               <br/>
              <el-table v-loading="loading" :data="form.serviceMaterList" border>
                <el-table-column label="序号" width="65" type="index" align="center">
                  <template slot-scope="scope">
                    <span>{{ scope.$index + 1 }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="材料名称" align="center" width="355" prop="materialName"  />
                 <el-table-column label="目录名称" >
                   <template slot-scope="scope">
                     <el-form-item prop="materialCatalogOid">
                       <el-col :span="24">
                         <el-select  placeholder="请选择对应目录" v-model="scope.row.materialCatalogOid" :clearable="true">
                           <el-option v-for="catalog in catalogList" :key="catalog.id" :label="catalog.catalogName" :value="catalog.materialCatalogOid"></el-option>
                         </el-select>
                       </el-col>
                     </el-form-item>
                   </template>
                  </el-table-column>
            </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 信息详细 -->
    <el-dialog :title="title" :visible.sync="openView" width="1000px" append-to-body>
      <div class="el-table__header-wrapper dialog-table">
        <el-form ref="form" :model="form" label-width="140px" size="mini">
          <table cellspacing="0" cellpadding="0" border="0" class="el-table__body">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td>
                <b>一件事目录名称：</b>
              </td>
              <td colspan="3">
                {{form.comboDirectoryName}}
              </td>
            </tr>
            <tr>
              <td>
                <b>所属区划：</b>
              </td>
              <td>
                {{form.districtName}}
              </td>
              <td>
                <b>所属一件事分类：</b>
              </td>
              <td>
                {{form.themeName}}
              </td>
            </tr>
            <tr>
              <td>
                <b>服务对象：</b>
              </td>
              <td colspan="3">
                {{form.comboServiceObjectName}}
              </td>
            </tr>
            <tr>
              <td>
                <b>申报须知：</b>
              </td>
              <td colspan="3">
                {{form.declareNeedKnow}}
              </td>
            </tr>
            <tr>
              <td>
                <b>主办部门：</b>
              </td>
              <td>
                {{form.mainOrganName}}
              </td>
              <td>
                <b>协办部门：</b>
              </td>
              <td>
                {{form.assistOrganName}}
              </td>
            </tr>
            <tr>
              <td>
                <b>是否收费：</b>
              </td>
              <td>
                {{reversedIfCharge}}
              </td>
              <td>
                <b>网办地址：</b>
              </td>
              <td>
                {{form.webUrl}}
              </td>
            </tr>
            <tr>
              <td>
                <b>收费标准：</b>
              </td>
              <td colspan="3">
                {{form.chargeStandard}}
              </td>
            </tr>
            <tr>
              <td>
                <b>收费依据：</b>
              </td>
              <td colspan="3">
                {{form.chargeGist}}
              </td>
            </tr>
            <tr>
              <td>
                <b>办理地址：</b>
              </td>
              <td colspan="3">
                {{form.manageAddr}}
              </td>
            </tr>
            <tr>
              <td>
                <b>办理时间：</b>
              </td>
              <td colspan="3">
                {{form.manageTime}}
              </td>
            </tr>
            <tr>
              <td>
                <b>办事流程图：</b>
              </td>
              <td>
                <el-form-item >
                  <div v-show="null!=form.handleFlow && ''!=form.handleFlow">
                    <span>{{form.handleFlowName}}</span>
                    <el-link type="primary" @click="downloadFile(form.handleFlow)">下载</el-link> |
                    <el-link type="primary" @click="viewFile(form.handleFlow)">预览</el-link>
                  </div>
                </el-form-item>
              </td>
              <td>
                <b>有无中介服务：</b>
              </td>
              <td>
                {{reversedIsZjfw}}
              </td>
            </tr>
            <tr>
              <td>
                <b>咨询方式：</b>
              </td>
              <td colspan="3">
                {{form.zixunType}}
              </td>
            </tr>
            <tr>
              <td>
                <b>监督方式：</b>
              </td>
              <td colspan="3">
                {{form.jianduType}}
              </td>
            </tr>
            <tr>
              <td>
                <b>联办能力：</b>
              </td>
              <td>
                {{reversedUnionOrganFlag}}
              </td>
              <td>
                <b>是否支持预约办理：</b>
              </td>
              <td>
                {{reversedAppointmentFlag}}
              </td>
            </tr>
            <tr>
              <td>
                <b>办理形式：</b>
              </td>
              <td colspan="3">
                {{reversedHandleForm}}
              </td>
            </tr>
            <tr>
              <td>
                <b>承诺时限(工作日)：</b>
              </td>
              <td>
                {{form.promiseLimit}}
              </td>
              <td>
                <b>法定时限(工作日)：</b>
              </td>
              <td>
                {{form.legalLimit}}
              </td>
            </tr>
            <tr>
              <td>
                <b>是否网上支付：</b>
              </td>
              <td>
                {{reversedOnlinePayFlag}}
              </td>
              <td>
                <b>是否支持物流快递：</b>
              </td>
              <td>
                {{reversedExpressFlag}}
              </td>
            </tr>
            <tr>
              <td>
                <b>线下跑动次数：</b>
              </td>
              <td colspan="3">
                {{reversedCountToScence}}
              </td>
            </tr>
            <tr>
              <td>
                <b>线下跑动的原因和环节：</b>
              </td>
              <td colspan="3">
                {{form.reasonToScence}}
              </td>
            </tr>
          </table>
          <br/>
          <h3>材料信息</h3>
          <el-table v-loading="loading" :data="form2.serviceMaterListShow">
            <el-table-column label="序号" width="165" type="index" align="center" >
              <template slot-scope="scope">
                <span>{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column label="材料名称" align="center" width="555" prop="materialName" />
          </el-table>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { queryDirectoryMaterialList,pageList,page,getMealOne,saveOrUpdateComboDirectoryMaterial} from "@/api/onething/clzs/mealCatalogRelatedManage/mealCatalogRelatedManage.js";
 import { queryServiceTypeSimpleTree } from "@/api/sxpt/serviceType";
import Treeselect from '@riophae/vue-treeselect';
import '@riophae/vue-treeselect/dist/vue-treeselect.css';
export default {
  name: "sxServiceRegistrar",
  components: {Treeselect},
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
          comboDirectoryName: "",
          serviceMaterList:[],
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
      form1: {
        serviceOid: "",
        materialOids:[],
        materialCatalogOids:[]
      },
      form2:{
        //材料列表
        serviceMaterListShow:[]
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
      let _that = this;
      this.reset();
      const oid = row.comboDirectoryOid;
      getMealOne(oid).then(response => {
        this.form = response.data;
        this.openView = true;
        this.title = "查看一件事目录信息";
      });
      queryDirectoryMaterialList(oid).then(response => {
        this.form2.serviceMaterListShow=[];
        this.form2.serviceMaterListShow=  response.data;
      });
 /*     pageServiceOidList(oid).then(response => {
         this.form2.serviceMaterListShow=[];
        let comboServiceList= response.data;
        for(let comboService of comboServiceList){
          getSxServiceOne(comboService.serviceOid).then(response1 => {
            let sxServiceMaterialsGet=  response1.data.sxServiceMaterials;
            for(let cailiao of sxServiceMaterialsGet){
              this.form2.serviceMaterListShow.push(cailiao);
            }
          });
        }
      });*/
    },
    /** 新增和修改按钮操作 */
    handleInit(row) {
    let _that = this;
          _that.reset();
          _that.form.comboDirectoryName = row.comboDirectoryName;
          if(row.comboDirectoryOid){
           queryDirectoryMaterialList(row.comboDirectoryOid).then(response => {
              _that.openInit = true;
              let sxServiceMaterials=  response.data;
              for(let cailiao of sxServiceMaterials){
                this.form.serviceMaterList.push(cailiao);
              }
            });
   /*       pageServiceOidList(row.comboDirectoryOid).then(response => {
            let comboServiceList= response.data;
            for(let comboService of comboServiceList){
              getSxServiceOne(comboService.serviceOid).then(response => {
                _that.form.serviceOid = comboService.serviceOid;
                let sxServiceMaterials=  response.data.sxServiceMaterials;
                for(let cailiao of sxServiceMaterials){
                  this.form.serviceMaterList.push(cailiao);
                }
              });
            }
            })*/
          }else{
            _that.openInit = false;
          }

          _that.title ="一件事目录关联配置";

    },
    /** 提交按钮 */
    submitForm: function() {
      let _that = this;
      saveOrUpdateComboDirectoryMaterial(this.form.serviceMaterList).then(response => {
        if (response.code === 200) {
          //如果关联目录发生变化，删除已配置的验证规则 这个功能没有做
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
</style>
