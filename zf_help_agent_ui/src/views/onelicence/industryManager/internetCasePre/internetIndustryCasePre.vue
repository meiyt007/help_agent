/**
* @Author: liangxm
* @Date: 2020-12-01
* @Description: 办件预审
*/
<template>
  <div class="app-container">
    <!--列表数据-->
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="70px">
      <el-row>
        <el-form-item label="申请人" prop="applyUserName">
          <el-input v-model.trim="queryParams.applyUserName" placeholder="请输入申请人" clearable size="100" @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item label="办件编号" prop="caseNumber" label-width="80px">
          <el-input v-model.trim="queryParams.caseNumber" placeholder="请输入办件编号" clearable size="100" @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item label="预审状态" label-width="108px">
          <el-radio-group v-model="queryParams.caseStatus" @change="handleRowChange">
            <el-radio v-for="(status,key) in statusOptions" :key="key" :label="key">{{status}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item class="fr mr0">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置</el-button>
        </el-form-item>
      </el-row>
    </el-form>
    <!-- 列表信息-->
    <el-table  :data="caseRegList" v-loading="loading" border :fit="true">
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="申请项目名称"  align="center" prop="projectName" :show-overflow-tooltip="true"/>
      <el-table-column label="办件编号"  align="center" prop="caseNumber" :show-overflow-tooltip="true"/>

      <el-table-column label="申请人"  align="center" prop="applyUserName" :show-overflow-tooltip="true"/>
      <el-table-column label="证件号"  align="center" prop="credentialNumber" :show-overflow-tooltip="true"/>
      <el-table-column label="登记日期"  align="center" prop="createDate" :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="iconfont zfsoft-chakan"  @click="viewAccept(scope.row)" v-hasPermi="['sys:internetCasePre:view']" >查看</el-button>
          <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai" v-if="scope.row.caseStatus==1"  @click="handleInit(scope.row)" v-hasPermi="['sys:internetCasePre:init']">预审</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="queryParams.total > 0" :total="queryParams.total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>
    <!-- 办件预审信息 -->
    <el-dialog :close-on-click-modal="false" :title="dialogTitle" :visible.sync="openInit" append-to-body>
      <div class="el-table__header-wrapper dialog-table">
        <el-form ref="form" :model="form" :rules="rules" label-width="0px" class="demo-ruleForm">
          <table cellspacing="0" cellpadding="0" border="0" class="el-table__body">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><b>预审状态：</b></td>
              <td colspan="3">
                <el-form-item >
                  <el-radio-group v-model="form.auditResult" @change="handleRowChange">
                    <el-radio :label="2">通过</el-radio>
                    <el-radio :label="3">不通过</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
            </tr>

            <tr>
              <td><b>意见说明：</b></td>
              <td colspan="3">
                <el-form-item prop="resultDesc">
                  <el-input v-model.trim="form.resultDesc"
                            type="textarea" placeholder="请输入意见说明"
                            maxlength="200"
                            show-word-limit></el-input>
                </el-form-item>
              </td>
            </tr>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 办件详细信息 -->
    <el-dialog v-dialog-drag :visible.sync="openView" v-if="openView"  :title="title" width="70%" append-to-body>
      <el-scrollbar style="height:500px;">
        <view-case-info @view-case="closeView" :industryCaseOid="industryCaseOid"></view-case-info>
      </el-scrollbar>
    </el-dialog>
  </div>
</template>

<script>
  import { page,saveOrUpdate,saveCaseAccpet} from "@/api/onelicence/industryManager/industryCaseAccept/internetIndustryCasePre";
  import viewCaseInfo from "@/views/onelicence/industryManager/industryCaseAccept/viewCaseInfo";
  export default {
    name: "InternetIndustryCasePre",
    components: {viewCaseInfo},
    data() {
      return {

        // 列表数据
        caseRegList: [],
        tableData:[],
        caseViewOptions:[],
        rowNum:1,
        // 弹窗标题
        dialogTitle: '',
        openView: false,
        // 新增/修改显示弹出层
        openInit: false,
        // 表单参数
        form: {caseOid:'',caseStatus:2,auditResult:2,resultDesc:'',ifAccept:''},
        // 表单校验
        rules: {
          resultDesc: [
            { required: true, message: "意见说明不能为空", trigger: "blur" }
          ]
        },
        industryCaseOid:"",
        indexCaseNumber:"",
        // 预审状态
        statusOptions: {'1':'待预审','2':'预审通过'},
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          caseNumber: "",
          caseStatus:'1',
          sourceApp:'2',
          total:0,
        }
      };
    },
    methods: {
      handleRowChange(data) {
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      // 取消按钮
      cancel() {
        this.openInit = false;
        this.reset();
      },
      /** 查询列表 */
      getList() {
        this.loading = true;
        page(this.queryParams).then(response => {
          this.caseRegList = response.data.data;
        this.queryParams.total = response.data.total;
        this.loading = false;
      });
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.queryParams.caseStatus='1';//重置radio无效
        this.handleQuery();
      },
      // 表单重置
      reset() {
        Object.assign(this.form, this.$options.data().form)
        this.resetForm("form");
      },
      /** 查看按钮操作 */
      handleView(row) {
        this.indexCaseNumber= row.caseOid;
        this.openView = true;
        this.dialogTitle = "查看详情";
      },
      /**业态办件修改**/
      viewAccept(row){
        this.industryCaseOid =row.caseOid;
        this.openView = true
        this.title = '办件详细信息'
      },
      // 关闭按钮
      closeView() {
        let _that = this;
        _that.openView= false;
        _that.getList();
      },
      /** 出库操作 */
      handleInit(row) {
        if(row.caseOid) {
          this.form.caseOid=row.caseOid;
          this.openInit = true;
        } else {
          this.openInit = true;
        }
        this.dialogTitle ="办件预审";
      },
      /** 提交按钮 */
      submitForm: function() {
        let _that = this;
        _that.form.caseStatus=_that.form.auditResult;
        if(_that.form.auditResult == '2'){
          _that.form.ifAccept = '1';
        }else{
          _that.form.ifAccept = '2';
        }

        this.$refs["form"].validate(valid => {
          if (valid) {
            saveOrUpdate (_that.form).then(response => {

                saveCaseAccpet (_that.form).then(response => {
                  if (response.code === 200) {
                    _that.msgSuccess("预审成功");
                    _that.openInit = false;
                    setTimeout(() => {
                      _that.getList();
                    }, 10);
                  }
                });

            });
          }
        });

      },

    },
    watch: {},
    created() {
      this.getList();
    },
  };
</script>
<style lang="scss" scoped>
  .treeselect{
    width: 200px;
  }
  .treeselect240{
    width: 240px;
  }
</style>
