<!--author:ningzz-->
<template>
  <div>
    <el-table v-loading="loading" :data="comboCaseList" border :fit="true">
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="办件编号"
        align="center"
        prop="caseNumber"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="项目名称"
        align="center"
        prop="projectName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="申请人"
        align="center"
        prop="applyUserName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="证件号"
        align="center"
        prop="credentialNumber"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="登记日期"
        align="center"
        prop="createDate"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-dengji"
            @click="acceptanceCaseSelect(scope.row)"
            v-hasPermi="['business:temporary:select']"
            >选择
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
            v-hasPermi="['sys:yjsSupervise:view']"
            >查看</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 一件事办件查看开始 -->
    <el-dialog
      v-dialog-drag
      :visible.sync="caseOpenView"
      v-if="caseOpenView"
      @close="closeOpenView"
      :title="title"
      append-to-body
      width="1100px"
      scrollbar
      height="700px"
    >
      <view-combo-case-new :caseOid="indexCaseOid" @view-case="closeOpenView" />
    </el-dialog>
    <!--一件事办件查看结束-->
  </div>
</template>

<script>
import ViewComboCaseNew from "@/views/onething/comboManager/comboAccept/viewComboCaseNew";
import { getUserInfoList } from "@/api/onething/comboManager/comboAccept/applyUserList";
export default {
  name: "ApplyUserOnethingList",
  components: { ViewComboCaseNew },
  props: ['applyCardNum','cegisterType'],
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      comboCaseList: [],
      // 弹出层标题
      title: "",
      // 办件查看
      caseOpenView: false,
      indexCaseOid: '',
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        credentialNumber: this.applyCardNum,
        cegisterType:this.cegisterType
      },
    };
  },
  created () {
    this.getList();
  },
  methods: {
    getList () {
      this.loading = true;
      getUserInfoList(this.queryParams).then(response => {
        this.comboCaseList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },

    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNum = 1;
      this.getList();
    },

    /** 重置按钮操作 */
    resetQuery () {
      this.queryParams.applyUserName = '';
      this.queryParams.caseNumber = '';
      this.handleQuery()
    },
    /** 办件查看按钮操作 */
    handleView (row) {
      this.indexCaseOid = row.caseOid;
      this.caseOpenView = true;
      this.title = "一件事办件信息";
    },
    /** 办件查看关闭按钮操作 */
    closeOpenView () {
      this.caseOpenView = false;
    },
    // 点击清空按钮时触发
    delValue () {
      console.log(11)
    },
    // 办件修改关闭按钮
    acceptanceCaseSelect (row) {
      this.$emit("selectUserOk", row);
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
.treeselect {
  width: 200px;
}
.treeselect240 {
  width: 240px;
}
</style>
