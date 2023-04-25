<template>
  <div class="app-container typeApp">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="100px"
    >
      <el-form-item label="接口名称" prop="interfaceName">
        <el-input
          v-model.trim="queryParams.interfaceName"
          placeholder="请输入接口名称"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="服务名称" prop="serverName">
        <el-input
          v-model.trim="queryParams.serverName"
          placeholder="请输入服务名称"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="接口地址" prop="interfaceUrl">
        <el-input
          v-model.trim="queryParams.interfaceUrl"
          placeholder="请输入接口地址"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="类型">
          <el-radio-group v-model="queryParams.lastCheckResult">
          <el-radio
            v-for="(status, key) in statusOptions"
            :key="key"
            :label="key"
          >{{ status }}</el-radio
          >
        </el-radio-group>
      </el-form-item>
      <el-form-item label="检测时间">
        <el-date-picker v-model.trim="queryParams.startDate" type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd"
                        :picker-options="pickerOptionsStart" placeholder="请选择开始日期">
        </el-date-picker>
        -
        <el-date-picker v-model.trim="queryParams.endDate" type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd"
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
      :data="interfaceCheckLogList"
      tooltip-effect="dark"
      style="width: 100%"
    >
      <el-table-column label="序号" width="50" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="接口名称"
        align="center"
        prop="interfaceName"
        width="200"
        show-overflow-tooltip
      />
      <el-table-column
        label="服务名称"
        align="center"
        prop="serverName"
        width="300"
        show-overflow-tooltip
      />
      <el-table-column
        label="接口地址"
        align="center"
        prop="interfaceUrl"
        width="500"
        show-overflow-tooltip
      />
      <el-table-column
        label="检测时间"
        align="center"
        prop="createDate"
        width="150"
        show-overflow-tooltip
      />
      <el-table-column
        label="检测结果"
        align="center"
        prop="lastCheckResult"
        width="80"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <p v-if="scope.row.lastCheckResult=='0'">通过</p>
          <p v-if="scope.row.lastCheckResult=='1'">不通过</p>
        </template>
      </el-table-column>
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
            v-hasPermi="['im:interfaceCheckLog:view']"
          >查看</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNumber"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    <!-- 查看应用信息详细 -->
    <el-dialog
      title="查看"
      :close-on-click-modal="false"
      :visible.sync="openView"
      width="900px"
      append-to-body
    >
      <table
        cellspacing="0"
        cellpadding="0"
        border="0"
        class="zf-zc-table"
      >
        <colgroup>
          <col width="20%" />
          <col width="30%" />
          <col width="20%" />
          <col width="30%" />
        </colgroup>
        <tr>
          <td><b>接口名称：</b></td>
          <td>
            {{ interfaceCheckLog.interfaceName }}
          </td>
          <td><b>服务名称：</b></td>
          <td>
            {{ interfaceCheckLog.serverName }}
          </td>
        </tr>
        <tr>
          <td><b>接口地址：</b></td>
          <td colspan="3">
            {{ interfaceCheckLog.interfaceUrl }}
          </td>
        </tr>
        <tr>
          <td><b>检测结果：</b></td>
          <td>
            {{ interfaceCheckLog.lastCheckResult == '0'?'通过':'不通过' }}
          </td>
          <td><b>检测时间：</b></td>
          <td>
            {{ interfaceCheckLog.createDate }}
          </td>
        </tr>
      </table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  page,
  getOne
} from "@/api/middle/interfaceCheckLog";
export default {
  components: {},
  name: "InterfaceCheckLog",
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      title: "",
      // 查看显示弹出层
      openView: false,
      statusOptions: {'0': "通过", '1': "不通过", '2': '全部'},
      // 数据表格
      interfaceCheckLogList: [],
      interfaceCheckLog: {},
      pickerOptionsStart: {
        disabledDate: time => {
          const endDateVal = new Date(this.queryParams.endDate).getTime()
          if (endDateVal) {
            return time.getTime() > endDateVal
          }else {
            return time.getTime() > new Date()
          }
        }
      },
      pickerOptionsEnd: {
        disabledDate: time => {
          const beginDateVal = new Date(new Date(this.queryParams.startDate)-24*60*60*1000).getTime()
          if (beginDateVal) {
            return time.getTime() < beginDateVal || time.getTime() > new Date()
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
        interfaceName: "",
        serverName: "",
        interfaceUrl: "",
        lastCheckResult: "2"
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
      if(null == this.queryParams.lastCheckResult || this.queryParams.lastCheckResult == '') {
        this.queryParams.lastCheckResult = "2";
      }
      page(this.queryParams).then(response => {
        this.interfaceCheckLogList = response.data.datas;
        this.total = response.data.totalRows;
        this.loading = false;
      });
    },
    /** 查看按钮操作 */
    handleView (row) {
      const oid = row.oid;
      getOne(oid).then(response => {
        this.interfaceCheckLog = response.data;
        this.openView = true;
        this.title = "查看应用信息";
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
      this.queryParams = {
        lastCheckResult: "2",
        pageNumber: 1,
        pageSize: 10
      };
      this.handleQuery();
    },
    // 取消按钮
    cancel () {
      this.openView = false;
    },
  }
};
</script>
<style></style>
