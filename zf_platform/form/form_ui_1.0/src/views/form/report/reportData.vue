<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="开始时间" prop="formName">
        <el-date-picker v-model="queryParams.startTime" type="date" value-format="yyyy-MM-dd" format="yyyy-MM-dd"
                        placeholder="开始时间" @change="startTimeStatus"
                        :picker-options="pickerOptionsStart" style="margin-right: 10px;">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="结束时间" prop="formCode">
        <el-date-picker v-model="queryParams.endTime" type="date" value-format="yyyy-MM-dd" format="yyyy-MM-dd"
                        placeholder="结束时间" @change="endTimeStatus"
                        :picker-options="pickerOptionsEnd" style="margin-left: 10px;">
        </el-date-picker>
      </el-form-item>
      <el-form-item class="fr mr0">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="ml5">重置</el-button>
      </el-form-item>
      <!--<div style="color: red;margin-bottom: 10px">注意：当前列表只展示数据存储类型为表单系统的记录</div>-->
    </el-form>

    <el-table v-loading="loading" :data="formReportList" border>
      <!--<el-table-column type="selection" width="55" align="center"  />-->
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="表单名称" align="center" prop="formName" :show-overflow-tooltip="true" />
      <el-table-column label="填报数据" align="center" prop="formData" :show-overflow-tooltip="true"/>
      <el-table-column label="版本" align="center"  width="80" prop="version" :show-overflow-tooltip="true"/>
      <!--<el-table-column label="业务key" align="center"  width="100" prop="businessKey" :show-overflow-tooltip="true"/>-->
      <el-table-column label="填报时间" align="center" prop="createDate" :show-overflow-tooltip="true"/>
      <el-table-column label="存储类型" align="center" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <!--数据库存储类型  0本地  1API  2数据库-->
          <span v-if="scope.row.saveDataType == 0">表单系统</span>
          <span v-if="scope.row.saveDataType == 1">API</span>
          <span v-if="scope.row.saveDataType == 2">数据库</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" width="240" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-fangdichanpinggu"
            @click="handleFormView(scope.row)"
          >查看表单</el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleDataView(scope.row)"
          >查看数据</el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-zaixianshenbao"
            @click="handleReport(scope.row)"
          >修改数据</el-button>
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
        <el-form :model="viewForm" label-width="0px" class="demo-ruleForm">
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
                {{ viewForm.formName }}
              </td>
              <td><b>表单版本：</b></td>
              <td>
                {{ viewForm.version }}
              </td>
            </tr>
            <tr>
              <td><b>数据存储类型：</b></td>
              <td>
                <span  v-for="item in saveDataTypeMap"
                       :label="item.key"
                       :key="item.key" v-if="viewForm.saveDataType == item.key">
                  {{item.value}}
                </span>
              </td>
              <td><b>创建时间：</b></td>
              <td>
                {{ viewForm.createDate }}
              </td>
            </tr>
            <tr>
              <td><b>填报数据：</b></td>
              <td colspan="3">
                {{ viewForm.formData }}
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
    <el-dialog v-dialog-drag :visible.sync="view.designShow" :close-on-click-modal="false"  v-for="view in reportFormOptions" title="填报表单" @close="closeFormView" @handleRequestSuccess="handleRequestSuccess" width="90%" append-to-body>
      <FormReport :reportForm="view.reportForm" :authorizeKey="view.authorizeKey" :isShowDefaultVal="false" :checkFlag="true"  @closeFormView="closeFormView"></FormReport>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeFormView">关 闭</el-button>
      </div>
    </el-dialog>

    <!--查看表单-->
    <el-dialog v-dialog-drag :visible.sync="view.designShow"  v-for="view in formViewOptions" title="查看表单" @close="closeFormView"  width="90%" append-to-body>
      <!--<FormView :key="'view'+view.designOid" :designOid="view.designOid" :reportOid="view.reportOid" :authorizeKey="view.authorizeKey" :disabled="true"></FormView>-->
      <FormView :key="'view'+view.designOid" :formDataConfig="view.formDataConfig" :initFormData="view.initFormData" :authorizeKey="view.authorizeKey" :disabled="true"/>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="exportToWord">导出到word</el-button>
        <el-button @click="closeFormView">关 闭</el-button>
      </div>
    </el-dialog>

   <el-dialog title="请选择需要导出的模板" v-dialog-drag center :close-on-click-modal="false" :visible.sync="show" width="60%" append-to-body>
        <SelectTemplate :key="new Date().getTime()" :templateParams="templateParams"></SelectTemplate>
    </el-dialog>

  </div>
</template>

<script>
  import {page,getFormConfigAndData} from "@/api/form/reportLog";
  import Template from '../template/index';
  import SelectTemplate from '../docxTemplate/selectTemplate.vue';
  import { Loading } from 'element-ui';
  export default {
    name: "ReportData",
    components: { Template,SelectTemplate },
    props:['formMainOid','authorizeKey'],
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 表单表格数据
        formReportList: [],
        // 弹出层标题
        title: "",
        templateParams:{},
        // 新增/修改显示弹出层
        openInit: false,
        // 查看显示弹出层
        openView: false,
        show:false,
        // 启用状态
        ableMap:{'1':'启用','0':'禁用'},
        // 表单状态 0修订 1发布 2变更
        formStatusMap:{'0':'修订','1':'发布','2':'变更'},
        //数据存储类型  0本地  1API  2数据库
        saveDataTypeMap:[{key:0,value:'表单系统'},{key:1,value:'API'},{key:2,value:'数据库'}],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
        },
        // 表单参数
        form: {},
        viewForm:{},
        reportFormOptions:[],
        formViewOptions:[],
        pickerOptionsStart: {
          disabledDate: time => {
            let endDateVal = this.queryParams.endTime;
            if (endDateVal) {
              return time.getTime() > new Date(endDateVal).getTime();
            }
          }
        },
        pickerOptionsEnd: {
          disabledDate: time => {
            let beginDateVal = this.queryParams.startTime;
            if (beginDateVal) {
              return (
                time.getTime() < new Date(beginDateVal).getTime()
              );
            }
          },
        },
      };
    },
    created() {
      this.getList();
    },
    watch:{
    },
    methods: {
      //保存数据返回
      handleRequestSuccess(){

      },
      // 时间开始选择器
      startTimeStatus:function(value){
        this.queryParams.startTime = value
      },
      // 时间结束选择器
      endTimeStatus:function(value){
        this.queryParams.endTime = value
      },
      /** 查询表单列表 */
      getList() {
        this.loading = true;
        let that = this;
        this.queryParams.formMainOid = this.formMainOid;
        this.queryParams.authorizeKey = this.authorizeKey;
        page(this.queryParams).then(response => {
          this.formReportList = response.data.data;
          this.total = response.data.total;
          this.loading = false;
        }).catch(function () {
          that.loading = false;
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
      //导出到word模板
      exportToWord(){
        this.show = true;
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

      // 表单填报修改
      handleReport(row) {
        let formView = {
          'designShow':true,
          'reportForm':row,
          'authorizeKey':this.authorizeKey
        };
        this.reportFormOptions.push(formView);

      },
      closeFormView(){
        this.reportFormOptions.pop();
        this.formViewOptions.pop();
        this.getList();
      },
      /** 查看按钮操作 */
      handleDataView(row) {
        this.viewForm = {};
        this.viewForm = row;
        this.openView = true;
        this.title = "查看填报数据";
      },
      // 表单查看
      handleFormView(row) {
        this.templateParams.designOid = row.designOid;
        this.templateParams.reportOid = row.reportOid;
        this.templateParams.designAndReportOid = row.designOid +'-'+ row.reportOid;
        //this.templateParams.isPdf = true;
        let loadingInstance = Loading.service({ fullscreen: true, background: 'rgba(0, 0, 0, 0.7)', text: '玩命加载中...' });
        getFormConfigAndData(row.id).then(response => {
          let formView = {'designShow':true,'initFormData':response.data.formData,'formDataConfig':response.data.formConfig,'authorizeKey':this.authorizeKey};
          this.formViewOptions.push(formView);
          this.$nextTick(() => {
            loadingInstance.close();
          });
        });
      },
    }
  };
</script>
<style >

</style>
