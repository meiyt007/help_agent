<template>
  <div class="app-container typeApp">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="100px"
    >
      <el-form-item label="用户名称" prop="userName">
        <el-input
          v-model.trim="queryParams.userName"
          placeholder="请输入用户名称"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="日志名称" prop="logName">
        <el-input
          v-model.trim="queryParams.logName"
          placeholder="请输入日志名称"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="日志详细信息" prop="message">
        <el-input
          v-model.trim="queryParams.message"
          placeholder="请输入日志详细信息"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="时间区间">
        <el-date-picker v-model.trim="queryParams.startDate" type="month" format="yyyy-MM" value-format="yyyy-MM"
                        :picker-options="pickerOptionsStart" placeholder="请选择开始日期">
        </el-date-picker>
        -
        <el-date-picker v-model.trim="queryParams.endDate" type="month" format="yyyy-MM" value-format="yyyy-MM"
                        :picker-options="pickerOptionsEnd" placeholder="请选择结束日期">
        </el-date-picker>
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
          class="ml5"
        >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-table
      ref="multipleTable"
      :data="logOperationEsDataList"
      tooltip-effect="dark"
      style="width: 100%"
    >
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="日志名称"
        align="center"
        prop="logName"
        width="150"
        show-overflow-tooltip
      />
      <el-table-column
        label="用户名称"
        align="center"
        prop="userName"
        width="150"
        show-overflow-tooltip
      />
      <el-table-column
        label="是否成功"
        align="center"
        prop="status"
        width="100"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <p v-if="scope.row.sucessFlag==0">成功</p>
          <p v-if="scope.row.sucessFlag==1">失败</p>
        </template>
      </el-table-column>
      <el-table-column
        label="日志详细信息"
        align="center"
        prop="message"
        width="900"
        show-overflow-tooltip
      />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createDate"
        width="190"
        show-overflow-tooltip
      />
      <el-table-column
        label="服务端ip"
        align="center"
        prop="serverIp"
        width="150"
        show-overflow-tooltip
      />
    </el-table>
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNumber"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
import {
  page,
} from "@/api/middle/log";
export default {
  components: {},
  name: "Log",
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      title: "",
      // 数据表格
      logOperationEsDataList: [],
      pickerOptionsStart: {
        disabledDate: time => {
          const endDateVal = new Date(this.queryParams.endDate).getTime()
          if (endDateVal) {
            return time.getTime() > endDateVal - 0
          }else {
            return time.getTime() > new Date()
          }
        }
      },
      pickerOptionsEnd: {
        disabledDate: time => {
          const beginDateVal = new Date(this.queryParams.startDate).getTime()
          if (beginDateVal) {
            return time.getTime() < beginDateVal - 0 || time.getTime() > new Date()
          }else {
            return time.getTime() > new Date()
          }
        }
      },
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
        startDate: "",
        endDate: "",
        userName: "",
        logName: "",
        message: ""
      },
    };
  },
  watch: {},
  created () {
    this.getList();
  },
  methods: {
    /** 查询业务层级列表 */
    getList () {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.logOperationEsDataList = response.data.page.datas;
        this.queryParams.startDate = response.data.logOperationQueryDto.startDate;
        this.queryParams.endDate = response.data.logOperationQueryDto.endDate;
        this.total = response.data.page.totalRows;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNumber = 1;
      this.queryParams.pageSize = 10;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.queryParams = {};
      this.handleQuery();
    },
  }
};
</script>
<style></style>
