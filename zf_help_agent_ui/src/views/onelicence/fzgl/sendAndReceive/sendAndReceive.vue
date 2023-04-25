<!--author:ningzz-->
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="24" :xs="24">
        <el-form ref="queryForm" :model="queryParams" :inline="true" label-width="108px">
          <el-form-item label="办件编号">
            <el-input
              v-model.trim="queryParams.caseNumber"
              placeholder="请输入办件编号"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="申请人" prop="name">
            <el-input
              v-model.trim="queryParams.applyUserName"
              placeholder="请输入申请人名称"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="登记日期">
            <el-date-picker
              v-model="queryParams.caseStartDate"
              type="date"
              value-format="yyyy-MM-dd"
              :picker-options="pickerOptionsStart"
              placeholder="请选择开始日期"
            ></el-date-picker>-
            <el-date-picker
              v-model="queryParams.caseEndDate"
              type="date"
              value-format="yyyy-MM-dd"
              :picker-options="pickerOptionsEnd"
              placeholder="请选择结束日期"
            ></el-date-picker>
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置</el-button>
          </el-form-item>
        </el-form>

        <!--已办业务列表-->
        <el-table v-loading="loading" :data="industryCaseList" border :fit="true">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>

          <el-table-column label="一业一证目录名称" align="center" prop="comboDirectoryName" :show-overflow-tooltip="true" />
          <el-table-column label="办件编号" align="center" prop="caseNumber" :show-overflow-tooltip="true" />
          <el-table-column label="项目名称" align="center" prop="projectName" :show-overflow-tooltip="true" />
          <el-table-column label="申请人" align="center" prop="applyUserName" :show-overflow-tooltip="true" />
          <el-table-column label="登记日期" align="center" prop="createDate" :show-overflow-tooltip="true" />
          <el-table-column label="办件状态" align="center" prop="caseStatus" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <p v-if="scope.row.caseStatus == 3">办结</p>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="200px" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xinwen"
                @click="openSignList(scope.row)"
                v-hasPermi="['sys:sendAndReceive:sign']"
              >签收信息</el-button>
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-fenxiang"
                @click="openIssuedList(scope.row)"
                v-hasPermi="['sys:sendAndReceive:issued']"
              >发证信息</el-button>
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

    <!-- 查看证照签收记录-->
    <el-dialog
      v-dialog-drag
      :visible.sync="signView"
      v-if="signView"
      :title="signTitle"
      width="1100px"
      :close-on-click-modal="false"
      :modal-append-to-body="false"
      append-to-body
    >
      <license-sign-page @case-close="closeSignView" :industryCaseOid="industryCaseOid"></license-sign-page>
      <div slot="footer" class="zf-text-center">
        <el-button @click="closeSignView">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 查看证照发证记录-->
    <el-dialog
      v-dialog-drag
      :visible.sync="issuedView"
      v-if="issuedView"
      :title="issuedTitle"
      width="1100px"
      :close-on-click-modal="false"
      :modal-append-to-body="false"
      append-to-body
    >
      <license-issued-page @case-close="closeView" :industryCaseOid="industryCaseOid"></license-issued-page>
      <div slot="footer" class="zf-text-center">
        <el-button @click="closeView">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { page } from "@/api/onelicence/industryManager/industryCaseAccept/sendAndReceive";
import licenseSignPage from "@/views/onelicence/fzgl/sendAndReceive/licenseSignPage";
import licenseIssuedPage from "@/views/onelicence/fzgl/sendAndReceive/licenseIssuedPage";
export default {
  name: "sendAndReceive",
  components: { licenseSignPage, licenseIssuedPage },
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      industryCaseList: [],
      // 弹出层标题
      title: "",
      signTitle: "",
      issuedTitle: "",
      // 列表查看
      openView: false,
      signView: false,
      issuedView: false,
      industryCaseOid: "",
      // 查询参数默认值
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        caseStartDate: '',
        caseEndDate: '',
        caseNumber: ''
      },
      pickerOptionsStart: {
        disabledDate: time => {
          const endDateVal = new Date(this.queryParams.caseEndDate).getTime()
          if (endDateVal) {
            return time.getTime() > endDateVal - 0
          }
        }
      },
      pickerOptionsEnd: {
        disabledDate: time => {
          const beginDateVal = new Date((new Date(this.queryParams.caseStartDate) - 24 * 60 * 60 * 1000)).getTime()
          if (beginDateVal) {
            return time.getTime() < beginDateVal - 0
          }
        }
      },
    };
  },
  created () {
    this.getList();
  },
  methods: {
    /** 查询办结业务列表 */
    getList () {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.industryCaseList = response.data.data;
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
      this.queryParams.caseEndDate = null;
      this.queryParams.caseStartDate = null;
      this.handleQuery()
    },
    openSignList (row) {
      let _that = this;
      _that.industryCaseOid = row.caseOid;
      _that.signView = true;
      _that.signTitle = "证照签收记录";
    },
    openIssuedList (row) {
      let _that = this;
      _that.industryCaseOid = row.caseOid;
      _that.issuedView = true;
      _that.issuedTitle = "证照发证记录";
    },
    // 关闭按钮
    closeView () {
      let _that = this;
      _that.issuedView = false;
      _that.getList();
    },
    // 关闭按钮
    closeSignView () {
      let _that = this;
      _that.signView = false;
      _that.getList();
    },
  },
};
</script>
