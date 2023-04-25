<template>
  <div class="report-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="表单名称" prop="formName">
        <el-input
          v-model="queryParams.formName"
          placeholder="请输入表单名称"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="表单编码" prop="formCode">
        <el-input
          v-model="queryParams.formCode"
          placeholder="请输入表单编码"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item class="fr mr0">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="ml5">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="formMainList" border>
      <!--<el-table-column type="selection" width="55" align="center"  />-->
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="表单名称" align="center" prop="formName" :show-overflow-tooltip="true" />
      <el-table-column label="表单编码" align="center" prop="formCode" :show-overflow-tooltip="true"/>
      <el-table-column label="表单状态" align="center"  width="80" prop="formStatus" :formatter="formStatusFormat" :show-overflow-tooltip="true"/>
      <el-table-column label="最新版本" align="center"  width="80" prop="version" :show-overflow-tooltip="true"/>
      <el-table-column label="创建时间" align="center" prop="createDate" :show-overflow-tooltip="true"/>
      <el-table-column label="状态" align="center"  width="70" prop="isAble" >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isAble"
            :active-value="1"
            :inactive-value="0"
            disabled
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="240" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-zaixianshenbao"
            :disabled="scope.row.isAble == 0"
            :class="{'disabled-btn': (scope.row.isAble == 0)}"
            @click="handleReport(scope.row)"
          >填报测试</el-button>
          <el-button
            size="mini"
            type="text"
            icon="icon iconfont zfsoft-biangengjilu"
            @click="handleReportData(scope.row)"
          >填报历史</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 查看表单信息详细 -->
    <el-dialog :title="title" :visible.sync="openView" width="90%" append-to-body>
      <div class="el-table__header-wrapper dialog-table">
        <!--<h3>事项标题</h3>-->
        <el-form :model="form" label-width="0px" class="demo-ruleForm">
          <table cellspacing="0" cellpadding="0" border="0" class="el-table__body">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><b>表单名称：</b></td>
              <td>
                {{ form.formName }}
              </td>
              <td><b>表单编码：</b></td>
              <td>
                {{ form.formCode }}
              </td>
            </tr>
            <tr>
              <td><b>所属模块：</b></td>
              <td>
                {{ form.moduleName }}
              </td>
              <td><b>关联存储对象：</b></td>
              <td>
                {{ form.objectName }}
              </td>
            </tr>
            <tr>
              <td><b>表单状态：</b></td>
              <td>
                {{formStatusMap[form.formStatus]}}
              </td>
              <td><b>数据存储类型：</b></td>
              <td>
                <!--{{saveDataTypeMap[form.saveDataType].value}}-->
                <span  v-for="item in saveDataTypeMap"
                       :label="item.key"
                       :key="item.key" v-if="form.saveDataType == item.key">
                  {{item.value}}
                </span>
              </td>
              <tr v-if="1 == form.saveDataType">
                <td><b>API接口地址：</b></td>
                <td colspan="3">
                  {{form.apiUrl}}
                </td>
              </tr>
              <tr>
              <td><b>表单版本：</b></td>
              <td>
                {{ form.version }}
              </td>
              <td><b>创建时间：</b></td>
              <td>
                {{ form.createDate }}
              </td>
            </tr>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!--填报表单-->
    <el-dialog v-dialog-drag :visible.sync="view.designShow" :close-on-click-modal="false"  v-for="view in reportFormOptions" title="填报表单" @close="closeFormView"  width="90%" append-to-body>
      <FormReport ref="formReport":baseFormData="baseFormData"  @sendRes="sendRes" :reportForm="view.reportForm" :checkFlag="true" @closeFormView="closeFormView"></FormReport>
      <div slot="footer" class="dialog-footer">
        <el-button type="warning" @click="validateFormReport">验证</el-button>
        <el-button type="primary" @click="saveFormView">保存</el-button>
        <el-button @click="closeFormView">关 闭</el-button>
      </div>
    </el-dialog>

    <!--填报历史列表->-->
    <el-dialog v-dialog-drag :visible.sync="report.show"  title="填报历史列表" v-for="report in reportDialogOptions" @close="colseReportView" width="90%" append-to-body>
      <ReportData :formMainOid="report.formMainOid" :authorizeKey="report.authorizeKey"></ReportData>
    </el-dialog>

  </div>
</template>

<script>
  import { getOne, page } from '@/api/form/main'
  import { modulelist } from '@/api/form/module'
  import ReportData from '@/views/form/report/reportData.vue'

  export default {
    name: "Report",
    components: {ReportData },
    props:['authorizeKey'],
    data() {
      return {
        baseFormData: {
          name: "姓名",
           className: "请问企鹅"
        },
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 表单表格数据
        formMainList: [],
        // 弹出层标题
        title: "",
        // 新增/修改显示弹出层
        openInit: false,
        // 查看显示弹出层
        openView: false,
        //关联业务id
        businessKey:'000000000000000000000',
        // 启用状态
        ableMap:{'1':'启用','0':'禁用'},
        // 表单状态 {'0':'未设计','1':'草稿','2':'发布','3':'变更'},
        formStatusMap:{'0':'未设计','1':'草稿','2':'发布','3':'变更'},
        //数据存储类型  0本地  1API  2数据库
        saveDataTypeMap:[{key:0,value:'表单系统'},{key:1,value:'API'},{key:2,value:'数据库'}],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
        },
        // 表单参数
        form: {},
        moduleOptions:[],
        reportFormOptions:[],
        reportDialogOptions:[],
        dataList:[],
      };
    },
    created() {
      this.getList();
      this.queryMoudleList();
    },
    watch:{
    },
    methods: {
      init() {
        this.queryMoudleList();
        this.getList();
      },
      //保存数据返回
      saveFormView(){
         this.saveFormReport();
      },
      sendRes(data){
        console.log(data)
      },
      saveFormReport(){
        this.$refs.formReport[0].handleSubmitForm();
      },
      validateFormReport(){
        let that = this;
        this.$refs.formReport[0].validateForm().then(function onFulfilled(value){
          if(value.status){
            that.msgSuccess("验证成功！");
            return false;
          }
        });
      },
      /** 查询表单列表 */
      getList() {
        this.loading = true;
        let that = this;
        if(undefined == this.queryParams.authorizeKey)
          this.queryParams.authorizeKey = this.authorizeKey;
        this.queryParams.formStatus = 2;
        page(this.queryParams).then(response => {
          this.formMainList = response.data.data;
          this.total = response.data.total;
          this.loading = false;
        }).catch(function () {
          that.loading = false;
        });
      },
      queryMoudleList(){
        let par = {authorizeKey:this.authorizeKey};
        modulelist(par).then(response => {
          this.moduleOptions = response.data;
        });
      },
      // 启用状态
      statusFormat(row) {
        return this.selectMapLabel(this.ableMap, row.isAble);
      },
      // 表单状态
      formStatusFormat(row) {
        return this.selectMapLabel(this.formStatusMap, row.formStatus);
      },
      // 取消按钮
      cancel() {
        this.openInit = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
        };
        this.resetForm("form");
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.queryParams = {};
        this.queryParams.pageNum = 1;
        this.queryParams.pageSize = 10;
        this.handleQuery();
      },
      //历史版本
      handleDesign(row){
        let design = {'show':true,'formMainOid':row.formMainOid}
        this.designDialogOptions.push(design);
      },
      /** 查看按钮操作 */
      handleView(row) {
        this.reset();
        const oid = row.id;
        getOne(oid).then(response => {
          this.form = response.data;
          this.openView = true;
          this.title = "查看表单信息";
        });
      },
      // 表单填报
      handleReport(row) {
        let reportForm = {
          'designShow':true,
          'formMainOid':row.formMainOid,
          'authorizeKey':this.authorizeKey,
          //关联业务oid
          'businessKey':''
        };
        let formView = {
          'designShow':true,
          'reportForm':reportForm
        };
        this.reportFormOptions.push(formView);
      },
      closeFormView(){
        this.reportFormOptions.pop();
      },
      //填报记录列表
      handleReportData(row){
        let formView = {'show':true,'formMainOid':row.formMainOid,'authorizeKey':this.authorizeKey};
        this.reportDialogOptions.push(formView);
      },
      colseReportView(){
        this.reportDialogOptions.pop();
      }
    }
  };
</script>
<style lang="less">
.report-container {
  .disabled-btn {
    color:#C0C4CC !important;
  }
}
</style>
