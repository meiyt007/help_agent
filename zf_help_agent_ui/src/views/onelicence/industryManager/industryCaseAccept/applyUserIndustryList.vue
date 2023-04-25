<!--author:ningzz-->
<template>
  <div>
        <!--列表-->
        <el-table v-loading="loading" :data="comboCaseList" border :fit="true">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="办件编号" align="center" prop="caseNumber" :show-overflow-tooltip="true"/>
          <el-table-column label="项目名称" align="center" prop="projectName" :show-overflow-tooltip="true"/>
          <el-table-column label="申请人" align="center" prop="applyUserName" :show-overflow-tooltip="true"/>
          <el-table-column label="证件号"  align="center" prop="credentialNumber" :show-overflow-tooltip="true"/>
          <el-table-column label="登记日期" align="center" prop="createDate" :show-overflow-tooltip="true"/>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="iconfont zfsoft-dengji" @click="acceptanceCaseSelect(scope.row)" v-hasPermi="['business:temporary:select']">选择
              </el-button>
              <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleView(scope.row)" v-hasPermi="['sys:yjsSupervise:view']" >查看</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <!-- 办件详细信息 -->
    <el-dialog v-dialog-drag :visible.sync="openView" v-if="openView"  :title="title" width="1100px" height="700px" append-to-body scrollbar>
        <view-case-info @view-case="closeViewDetail" :caseId="caseId" :caseStatus="caseStatus" :caseOid="industryCaseOid"></view-case-info>
        <div slot="footer" class="zf-text-center">
          <el-button @click="closeViewDetail">关 闭</el-button>
        </div>
    </el-dialog>
  </div>
</template>

<script>
import viewCaseInfo from "@/views/onelicence/industryManager/industryCaseAccept/viewCaseInfo";
import {getUserInfoList} from "@/api/onelicence/industryManager/industryCaseAccept/applyUserIndustryList";
export default {
  name: "ApplyUserIndustryList",
  components: {viewCaseInfo},
  props: ['applyCardNum','cegisterType'],
  data() {
    return {
      userOptions: [],
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      comboCaseList: [],
      // 弹出层标题
      title: "",
      caseId:"",
      caseStatus:"",
      // 列表查看弹窗
      openView: false,
      activeTab: 'info',
      // 关联办件查看弹窗
      childCaseOpenView: false,
      indexCaseNumber: "",
      industryCaseOid:"",

      // 查询参数
      queryParams: {
        credentialNumber:this.applyCardNum,
        pageNum: 1,
        pageSize: 10,
        cegisterType:this.cegisterType
      },

    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      getUserInfoList(this.queryParams).then(response => {
        this.comboCaseList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },

    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams.applyUserName='';
      this.queryParams.caseNumber='';
      this.handleQuery()
    },

    /** 列表查看按钮操作 */
    handleView(row) {
      this.caseId =row.id;
      this.caseStatus=row.caseStatus;
      this.industryCaseOid =row.caseOid;
      this.openView = true
      this.title = '业态办件信息'
    },
    // 关闭按钮
    closeView() {
      let _that = this;
      _that.openView=false;
      _that.childCaseOpenView= false;
      _that.getList();
    },
    closeViewDetail(){
      let _that = this;
      _that.openView=false;
    },
    // 办件修改关闭按钮
    acceptanceCaseSelect(row) {
      this.$emit("selectUserOk",row);
    },
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

<style lang="scss" scoped>
.treeselect{
  width: 200px;
}
.treeselect240{
  width: 240px;
}
</style>
