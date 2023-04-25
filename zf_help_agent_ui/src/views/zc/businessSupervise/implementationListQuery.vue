/** * @Author: wangns */
<template>
  <div class="app-container">
    <!--事项数据-->
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="108px"
    >
      <el-form-item label="事项名称" prop="serviceName">
        <el-input
          v-model.trim="queryParams.serviceName"
          placeholder="请输入事项名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="实施编码" prop="implementCode">
        <el-input
          v-model.trim="queryParams.implementCode"
          placeholder="请输入实施编码"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item class="fr mr0">
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button
          type="warning"
          icon="el-icon-refresh"
          size="mini"
          @click="resetQuery"
          class="btn-reset"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-table
      v-loading="loading"
      :data="districtList"
      border
      :fit="true"
      height="calc(100% - 120px)"
    >
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <!--<el-table-column label="所属区划" align="center" width="150" prop="districtName"/>-->
      <el-table-column
        label="所属机构"
        align="center"
        prop="organName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="事项名称"
        align="center"
        prop="serviceName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="实施编码"
        align="center"
        prop="implementCode"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="基础编码"
        align="center"
        prop="basicCode"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="事项类型"
        align="center"
        prop="serviceTypeName"
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
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
            v-hasPermi="['sys:reg:view']"
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

    <!-- 信息详细 -->
    <el-dialog
      v-dialog-drag
      :visible.sync="sxServiceInfo.show"
      v-for="(sxServiceInfo, index) in serviceDialogOptions"
      :key="index"
      title="查看实施清单详情"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <view-sx-service-info :sxServiceOid="sxServiceOid" />
      <div slot="footer" style="text-align:center">
        <el-button @click="viewServiceClose">
          关闭
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { page } from "@/api/zc/businessManagement/sxServiceRegistrar.js";
import viewSxServiceInfo from "@/views/zc/businessSupervise/viewSxServiceInfo";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
  name: "ImplementationListQuery",
  components: { Treeselect, viewSxServiceInfo },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      districtList: [],

      // 弹出层标题
      title: "",
      // 查看显示弹出层
      openView: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        serviceName: "",
        implementCode: "",
        serviceStatus: "3"
      },
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
      userOidArr: [],
      props: {
        label: "label", //这里是树结构中需显示的数据（即接口返回的需展示在页面上的参数）
        children: "children",
        isLeaf: "leaf"
      },
      //事项oid
      sxServiceOid: "",
      serviceDialogOptions: []
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.districtList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.openInit = false;
      this.reset();
    },
    // 表单重置
    reset() {
      Object.assign(this.form, this.$options.data().form);
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
      this.sxServiceOid = row.serviceOid;
      let item = {
        show: true,
        sxServiceOid: row.serviceOid
      };
      this.serviceDialogOptions.push(item);
    },
    viewServiceClose() {
      this.serviceDialogOptions.pop();
    }
  }
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
