<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="100px">
      <el-form-item label="存储对象名称" prop="objectName">
        <el-input
          v-model="queryParams.objectName"
          placeholder="请输入存储对象名称"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="存储对象编码" prop="objectCode">
        <el-input
          v-model="queryParams.objectCode"
          placeholder="请输入存储对象编码"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="存储类型">
        <el-select
          v-model="queryParams.saveType"
          placeholder="请选择存储类型"
          clearable
          size="small"
        >
          <el-option
            v-for="(value, key) in objectOptions" :key="key"
            :label="value"
            :value="key"
          />
        </el-select>
      </el-form-item>
      <el-form-item class="fr mr0">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="ml5">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-plus"
          size="mini"
          @click="handleInit"
        >新增</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="objectList" border>
      <!--<el-table-column type="selection" width="55" align="center"  />-->
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="存储对象名称" align="center" prop="objectName" :show-overflow-tooltip="true" />
      <el-table-column label="存储对象编码" align="center" prop="objectCode" :show-overflow-tooltip="true"/>
      <el-table-column label="数据库名称" align="center" prop="datasourceName" :show-overflow-tooltip="true"/>
      <el-table-column label="创建时间" align="center" prop="createDate" width="150" :show-overflow-tooltip="true"/>
      <el-table-column label="状态" align="center"  width="80" prop="isAble" >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isAble"
            :active-value="1"
            :inactive-value="0"
            @change="handleAble(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="250" align="center" class-name="small-padding fixed-width">
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
            icon="iconfont zfsoft-xiugai"
            @click="handleInit(scope.row)"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-daochu"
            @click="handleTemplate(scope.row)"
          >输出模板</el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="handleDelete(scope.row)"
          >删除</el-button>
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


    <!-- 添加或修改存储对象信息对话框 -->
    <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="openInit" width="90%" append-to-body>
      <div class="el-table__header-wrapper dialog-table" v-if="this.openInit">
        <!--<h3>标题</h3>-->
        <el-form ref="form" :model="form" :rules="rules" label-width="0px" class="demo-ruleForm">
          <el-input v-show="false" v-model="form.id" />
          <el-input v-show="false" v-model="form.isAble" />
          <el-input v-show="false" v-model="form.isDelete" />
          <el-input v-show="false" v-model="form.createDate" />
          <table cellspacing="0" cellpadding="0" border="0" class="el-table__body">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><i class="require">*</i><b>存储对象名称：</b></td>
              <td>
                <el-form-item prop="objectName">
                  <el-input v-model.trim="form.objectName" placeholder="请输入存储对象名称" maxlength="50" show-word-limit/>
                </el-form-item>
              </td>
              <td><i class="require">*</i><b>存储对象编码：</b></td>
              <td>
                <el-form-item prop="objectCode">
                  <el-input v-model.trim="form.objectCode" placeholder="请输入存储对象编码" maxlength="50" show-word-limit/>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>是否保存：</b></td>
              <td colspan="3">
                <el-form-item prop="isSave">
                  <el-radio v-model="form.isSave" :label="1">是</el-radio>
                  <el-radio v-model="form.isSave" :label="0">否</el-radio>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>所属模块：</b></td>
              <td>
                <el-form-item prop="moduleOid">
                  <el-select v-model="form.moduleOid" @change="moduleOidChange" filterable clearable placeholder="请选择">
                    <el-option
                      v-for="ty in moduleOptions"
                      :key="ty.moduleOid"
                      :label="ty.moduleName"
                      :value="ty.moduleOid"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </td>
              <td><b>数据源：</b></td>
              <td>
                <el-form-item>
                  <el-select v-model="form.datasourceOid" filterable clearable @change="dataSourceChange" placeholder="请选择">
                    <el-option
                      v-for="ty in dataSourceOptions"
                      :key="ty.datasourceOid"
                      :label="ty.connectionName"
                      :value="ty.datasourceOid"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </td>
            </tr>
            <tr v-if="null !=form.datasourceOid && '' != form.datasourceOid">
              <td><i class="require" >*</i><b>数据库表名：</b></td>
              <td colspan="3" class="form-table-div">
                <div class="formTable">
                  <el-form-item prop="objectForm"  >
                    <el-input v-model.trim="form.objectForm" placeholder="请输入数据库表名" maxlength="50" show-word-limit/>
                  </el-form-item>
                </div>
                <div>
                  <el-button type="primary" size="small" @click="editTable">设计表</el-button>
                </div>
              </td>
            </tr>
          </table>
          <Column ref="columnChild"  :params="params" ></Column>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>


    <!-- 查看存储对象信息详细 -->
    <el-dialog :title="title" :visible.sync="openView" width="90%" append-to-body>
      <div class="el-table__header-wrapper dialog-table"  v-if="this.openView">
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
              <td><b>存储对象名称：</b></td>
              <td>
                {{ form.objectName }}
              </td>
              <td><b>存储对象编码：</b></td>
              <td>
                {{ form.objectCode }}
              </td>
            </tr>
            <tr>
              <td><b>是否保存：</b></td>
              <td colspan="3">
                {{ form.isSave ==1?'是':'否' }}
              </td>
            </tr>
            <tr>
              <td><b>所属模块：</b></td>
              <td>
                <span v-for="ty in moduleOptions" v-if="form.moduleOid == ty.moduleOid">{{ty.moduleName}}</span>
              </td>
              <td><b>数据源：</b></td>
              <td>
                <span v-for="ty in dataSourceOptions" v-if="form.datasourceOid == ty.datasourceOid">{{ty.connectionName}}</span>
              </td>
            </tr>
            <tr v-if="undefined != form.datasourceOid">
              <td><b>数据库表名：</b></td>
              <td colspan="3">
                {{ form.objectForm }}
              </td>
            </tr>
          </table>
        </el-form>
        <ColumnView  :key="'view'+form.datasourceOid" :params="params" ></ColumnView>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>

    <el-dialog :close-on-click-modal="false"  v-dialog-drag :visible.sync="view.show" v-for="view in tableViewOptions" title="创建表"
               @close="closeView" width="90%" append-to-body>
      <TableIndex :tableName="view.tableName" :datasourceOid="view.datasourceOid" @closeView="closeView"></TableIndex>
    </el-dialog>

    <el-dialog :close-on-click-modal="false"  v-dialog-drag :visible.sync="view.docxTemplateShow"  v-for="view in docxTemplateOptions" title="输出模板配置"
               @close="closeView" width="90%" append-to-body>
      <DocxTemplate :key="view.formObject.objectOid" :formObject="view.formObject" ></DocxTemplate>
    </el-dialog>
  </div>
</template>

<script>
  import { init, save, del ,getOne, page, able,checkReportDataExist } from "@/api/form/object";
  import { dataSourcelist } from "@/api/form/dataSource";
  import Column from '@/views/form/column';
  import ColumnView from '@/views/form/column/view.vue';
  import TableIndex from '@/views/form/table/tableIndex.vue';
  import DocxTemplate from '@/views/form/docxTemplate/index.vue';
  import { modulelist } from "@/api/form/module";
  export default {
    name: "Object",
    components: {Column,ColumnView,TableIndex,DocxTemplate },
    props:['authorizeKey'],
    data() {
      return {
        //授权id
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 存储对象表格数据
        objectList: [],
        objectOptions: {'0':'存储对象','1':'逻辑对象'},
        tableViewOptions:[],
        docxTemplateOptions:[],
        formObject:{},
        //参数
        params:{},
        // 弹出层标题
        title: "",
        // 新增/修改显示弹出层
        openInit: false,
        // 查看显示弹出层
        openView: false,
        docxTemplateShow:false,
        // 启用状态
        ableMap:{'1':'启用','0':'禁用'},
        moduleOptions:[],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: ''
        },
        // 表单参数
        form: {},
        dataSourceOptions:[],
        columnDialogOptions:[],
        // 表单校验
        rules: {
          objectName: [
            { required: true, message: "存储对象名称不能为空", trigger: "blur" },
          ],
          objectCode: [
            { required: true, message: "存储对象编码不能为空", trigger: "blur" },
          ],
          objectForm: [
            { required: true, message: "数据库表名不能为空", trigger: "blur" },
          ] ,
          moduleOid: [
            { required: true, message: "请选择所属模块", trigger: "blur" },
          ],

        }
      };
    },
    created() {
      this.params.authorizeKey = this.authorizeKey;//给子组件ColumnView传值，authorizeKey传过去
      this.getList();
      //this.getDataSourcelist();
      this.queryMoudleList();
    },
    watch:{
      // 数据库表名的填写
      'form.objectForm': {
        handler(val, oldVal) {
          this.params.objectForm = val;
        }
      },
    },
    methods: {
      editTable(){
        let params = {
          tableName:this.form.objectForm,
          datasourceOid:this.form.datasourceOid,
          show:true
        };
        if(!params.tableName){
          this.msgError("数据库表名不能为空！");
          return false;
        }
        this.tableViewOptions.push(params);
      },
      closeView(){
        this.tableViewOptions.pop();
        this.docxTemplateOptions.pop();
      },
      init() {
        this.queryMoudleList();
        this.getList();
      },
      getDataSourcelist(moduleOid){
        let params = {authorizeKey:this.authorizeKey,moduleOid:moduleOid};
        dataSourcelist(params).then(response => {
          this.dataSourceOptions = response.data;
        }).catch(function () {

        });
      },
      queryMoudleList(){
        let par = {authorizeKey:this.authorizeKey};
        modulelist(par).then(response => {
          this.moduleOptions = response.data;
        });
      },
      //模块的切换
      moduleOidChange(moduleOid){
        this.$set(this.form, "datasourceOid", null);
        this.$set(this.params, "datasourceOid", null);
        this.dataSourceOptions = [];
        this.$refs.columnChild.$set(this.$refs.columnChild.form, "columnList", []);
        this.$refs.columnChild.loading = true;
        this.$refs.columnChild.$nextTick(() => this.$refs.columnChild.loading = false);
        if(''!=moduleOid){
          this.getDataSourcelist(moduleOid);
        }
      },
      /** 查询存储对象列表 */
      getList() {
        this.loading = true;
        let that = this;
        if(undefined == this.queryParams.authorizeKey)
          this.queryParams.authorizeKey = this.authorizeKey;
        page(this.queryParams).then(response => {
          this.objectList = response.data.data;
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
      // 取消按钮
      cancel() {
        this.openInit = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          name: null,
          isSave: 1
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
      /** 查看按钮操作 */
      handleView(row) {
        this.reset();
        const oid = row.id;
        // this.params = {};
        this.params.authorizeKey=this.authorizeKey;
        getOne(oid).then(response => {
          this.form = response.data;
          this.params.objectOid = this.form.objectOid;
          this.$set(this.params, "datasourceOid", this.form.datasourceOid);
          this.openView = true;
          this.title = "查看存储对象信息";
        });
      },
      /** 新增和修改按钮操作 */
      handleInit(row) {
        console.log(row)
        if(row.objectOid){
        //   checkReportDataExist(row.objectOid).then(response => {
        //     if(true == response.data){
        //       this.msgError("存储对象已存在填报的数据，无法修改");
        //       return false;
        //     }else {
              this.reset();
              this.queryMoudleList();
              const oid = row.id;
              if(undefined == this.form.authorizeKey){
                this.form.authorizeKey = this.authorizeKey;
              }
              if(oid == undefined){
                this.dataSourceOptions = [];
                this.form = {isSave: 1};
                this.params = {};
                this.params.authorizeKey=this.authorizeKey;
                this.openInit = true;
                this.title = "新增存储对象信息";
              }else {
                init(oid).then(response => {
                  this.form = response.data;
                  this.getDataSourcelist(this.form.moduleOid);
                  this.params.objectOid = this.form.objectOid;
                  this.$set(this.params, "datasourceOid", this.form.datasourceOid)
                  this.params.authorizeKey=this.authorizeKey;
                  this.openInit = true;
                  this.title = "修改存储对象信息";
                });
              }
          //   }
          // });
        }else {
          this.dataSourceOptions = [];
          this.form = {isSave: 1};
          this.params = {};
          this.params.authorizeKey=this.authorizeKey;
          this.openInit = true;
          this.title = "新增存储对象信息";
        }


      },
      /** 提交按钮 */
      async submitForm(){
        if(null == this.form.authorizeKey)
          this.form.authorizeKey = this.authorizeKey;
        this.$refs["form"].validate(valid => {
          if (valid) {
            //调用子类的方法获取表单内容
            this.$refs.columnChild.getFormData().then(column=>{
              if(null == column ||null==column.columnList|| column.columnList.length==0){
                this.msgError("存储对象表结构不能为空");
                return false;
              }else {
                //formObject.getSaveType()
                if(null != this.form.datasourceOid && '' != this.form.datasourceOid){
                  this.form.saveType = 0;
                }
                this.form.columnList = column.columnList;
                this.form.extandList = column.objectExtandList;
                save(this.form).then(response => {
                  if (response.code === 200) {
                    this.msgSuccess("保存成功");
                    this.openInit = false;
                    this.getList();
                  }
                });
              }
            });
          }else {
            return false;
          }
        });
      },
      //导出模板配置
      handleTemplate(row){
          let params = {
            formObject:row,
            docxTemplateShow:true
          };
          this.docxTemplateOptions.push(params);
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const oid = row.id;
        this.$confirm('是否确认删除吗?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return del(oid);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 启禁用按钮操作 */
      handleAble(row) {
        const oid = row.id;
        let ableMessage = row.isAble === 1 ? '启用' : '禁用'
        this.$confirm('你确定要' + ableMessage + '吗？', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return able(oid);
        }).then(() => {
          this.msgSuccess(ableMessage+"成功");
        }).catch(function() {
          row.isAble = row.isAble === 0 ? 1 : 0
        });
      },
      //数据源切换
      dataSourceChange(data){
        if('' == data){
          this.$set(this.form, "datasourceOid", '');
          this.$set(this.form, "objectForm", '');
        }
        this.$set(this.params, "datasourceOid", data)
        this.$refs['form'].clearValidate();
        this.$refs.columnChild.$set(this.$refs.columnChild.form, "columnList", []);
        this.$refs.columnChild.$set(this.$refs.columnChild.form, "objectExtandList", []);
        this.$refs.columnChild.loading = true;
        this.$refs.columnChild.$nextTick(() => this.$refs.columnChild.loading = false);
      },
      //打开表结构弹框
      openColumnView(row){
        let item = {show:true,columnOid:row.columnOid};
        this.columnDialogOptions.push(item);
      },
      //关闭表结构弹框
      closeColumnView(){
        this.columnDialogOptions.pop();
      }
    }
  };
</script>
<style scoped >
  .form-table-div div{
    float: left;
  }
  .formTable ::v-deep .el-form-item__content{
    width: 400px !important;
    margin-right: 10px !important;
  }
</style>
