/**
* @Author: chenjm
* @Date: 2021-04-21 11:15:16
* @Description: 事项办件-办件列表
*/
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--区划数据-->
      <el-col :span="24" :xs="24">
        <!--        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">

          <el-form-item label="所属区划" prop="districtOidSelect">
            <treeselect  class="treeselect" :defaultExpandLevel="1" noOptionsText = "暂无数据" noResultsText="暂无数据" :show-count="true" v-model="queryParams.districtOidSelect" :options="districtOptions" placeholder="请输入所属区划" />
          </el-form-item>
          <el-form-item label="所属机构" prop="organOidSelect">
            <treeselect  class="treeselect" :defaultExpandLevel="1" noOptionsText = "暂无数据" noResultsText="暂无数据" :show-count="true" v-model="queryParams.organOidSelect" :options="organOptions" placeholder="请输入所属机构" />
          </el-form-item>
          <el-form-item label="时间">
            <el-date-picker
              v-model="queryParams.startDate"
              type="date" value-format="yyyy-MM-dd"
              :picker-options="pickerOptionsStart"
              placeholder="请选择开始日期">
            </el-date-picker>-
            <el-date-picker
              v-model="queryParams.endDate"
              type="date" value-format="yyyy-MM-dd"
              :picker-options="pickerOptionsEnd"
              placeholder="请选择结束日期">
            </el-date-picker>
          </el-form-item>

          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置</el-button>
          </el-form-item>
        </el-form>-->
        <el-table
          v-loading="loading"
          :data="serviceCaselist"
          border
          :fit="true"
        >
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="实施清单名称"
            align="center"
            prop="serviceName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="办件编号"
            align="center"
            prop="caseNumber"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="事项类型"
            align="center"
            prop="caseType"
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
                icon="iconfont zfsoft-chakan"
                @click="handleView(scope.row)"
                >查看</el-button
              >
              <!-- <el-button size="mini" type="text" icon="iconfont zfsoft-shanchu" @click="handleDelete(scope.row)" v-hasPermi="['sys:district:delete']">删除</el-button> -->
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
      </el-col>
    </el-row>

    <el-dialog
      v-dialog-drag
      :visible.sync="openView"
      title="查看办件信息"
      width="1100px"
      v-if="openView"
      append-to-body
    >
      <el-scrollbar style="height: 600px">
        <view-case-info
          :caseNumber="indexCaseNumber"
          @view-case="viewServiceClose"
        ></view-case-info>
      </el-scrollbar>
    </el-dialog>
  </div>
</template>

<script>
import { qlCaseServiceCaseList } from "@/api/tjfx/qlCase/qlCaseStatistics";
import viewCaseInfo from "@/views/zc/businessManagement/windowAcceptance/viewCaseInfo";
export default {
  name: "qlCaseServiceCaseList",
  components: { viewCaseInfo },
  props: ["viewType", "serviceTypeOid", "startDate", "endDate", "serviceOid"],
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      serviceCaselist: [],
      indexCaseNumber: "",
      // 弹出层标题
      title: "",
      // 查看显示弹出层
      openView: false,
      sxServiceOid: '',
      serviceDialogOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        viewType: "",
        serviceTypeOid: "",
        startDate: "",
        endDate: "",
        serviceOid: "",
      },
    };
  },
  created () {
    this.queryParams.serviceTypeOid = this.serviceTypeOid;
    this.queryParams.viewType = this.viewType;
    this.queryParams.startDate = this.startDate;
    this.queryParams.endDate = this.endDate;
    this.queryParams.serviceOid = this.serviceOid;
    this.getList();
  },
  methods: {
    /** 查询零办件列表 */
    getList () {
      this.loading = true;
      qlCaseServiceCaseList(this.queryParams).then(response => {
        this.serviceCaselist = response.data.data;
        this.total = response.data.total;
        /*alert(JSON.stringify(response))*/
        this.loading = false;
      });
    },
    // 取消按钮
    cancel () {
      this.openInit = false;
      this.reset();
    },
    /** 查看按钮操作 */
    handleView (row) {
      this.indexCaseNumber = row.caseNumber;
      this.openView = true;
      this.dialogTitle = "查看详情";
    },
    //关闭弹框
    viewServiceClose () {
      this.openView = false;
      this.indexCaseNumber = '';
    },

  },
};
</script>
<style lang="scss" scoped>
table {
  border-collapse: collapse;
}
.treeselect {
  width: 200px;
}
</style>
