/**
* @Author: liangss
* @Date: 2021-04-20 17:15:16
* @Description: 一件事办件列表
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
        <el-table v-loading="loading" :data="comboCaseList" border :fit="true">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="目录名称"
            align="center"
            prop="comboDirectoryName"
            :show-overflow-tooltip="true"
          />
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

    <!-- 一件事办件信息详细 -->
    <!--    <el-dialog v-dialog-drag :visible.sync="openView" v-if="openView"  title="办件信息" width="80%" append-to-body>
      <el-scrollbar style="height:500px;">
        <view-case-info :caseNumber="indexCaseNumber" @view-case="viewCaseClose"></view-case-info>
      </el-scrollbar>
    </el-dialog>-->
    <el-dialog
      v-dialog-drag
      :visible.sync="caseOpenView"
      v-if="caseOpenView"
      :title="title"
      width="70%"
      append-to-body
    >
      <el-scrollbar style="height: 500px" :native="false">
        <view-combo-case-new
          :caseOid="indexCaseOid"
          @view-case="closeOpenView"
        ></view-combo-case-new>
      </el-scrollbar>
    </el-dialog>
  </div>
</template>

<script>
import { selectComboCaseListByTypePage } from "@/api/tjfx/oneThingStatistics";
import viewCaseInfo from "@/views/zc/businessManagement/windowAcceptance/viewCaseInfo";
import ViewComboCaseNew from "@/views/onething/comboManager/comboAccept/viewComboCaseNew";
export default {
  name: "comboCaseList",
  components: { viewCaseInfo, ViewComboCaseNew },
  props: ["comboDireOid", "caseType", "themeOid", "startDate", "endDate"],
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
      // 查看显示弹出层
      openView: false,
      // 办件查看
      caseOpenView: false,
      indexCaseOid: "",
      indexCaseNumber: '',
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        districtOid: "",
        organOid: "",
        startDate: "",
        endDate: "",
        industryType: "",
      },
    };
  },
  created () {
    this.queryParams.comboDireOid = this.comboDireOid;
    this.queryParams.caseType = this.caseType;
    this.queryParams.themeOid = this.themeOid;
    this.queryParams.startDate = this.startDate;
    this.queryParams.endDate = this.endDate;
    this.getList();
  },
  methods: {
    /** 查询零办件列表 */
    getList () {
      this.loading = true;
      selectComboCaseListByTypePage(this.queryParams).then(response => {
        this.comboCaseList = response.data.data;
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
      this.indexCaseOid = row.caseOid;
      this.caseOpenView = true;
      this.title = "一件事办件信息";
      /*  this.indexCaseNumber = row.caseNumber;
        this.openView = true;
        this.title = "查看一件事办件信息";*/
    },
    closeOpenView () {
      this.caseOpenView = false;
    }


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
