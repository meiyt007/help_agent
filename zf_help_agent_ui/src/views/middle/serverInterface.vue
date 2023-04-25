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
      <el-form-item label="服务商" prop="middleUserName">
        <el-input
          v-model.trim="queryParams.middleUserName"
          placeholder="请输入服务商"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="类型">
        <el-radio-group v-model="queryParams.responseStatus">
          <el-radio
            v-for="(status, key) in statusOptions"
            :key="key"
            :label="key"
          >{{ status }}</el-radio
          >
        </el-radio-group>
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
      :data="interfaceVisitRecList"
      tooltip-effect="dark"
      style="width: 100%"
    >
      <el-table-column label="序号" width="55" type="index" align="center">
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
        prop="server.serverName"
        width="200"
        show-overflow-tooltip
      />
      <el-table-column
        label="类型"
        align="center"
        prop="responseStatus"
        width="50"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <p v-if="scope.row.responseStatus=='200'">成功</p>
          <p v-if="scope.row.responseStatus=='500'">失败</p>
        </template>
      </el-table-column>
      <el-table-column
        label="服务商"
        align="center"
        prop="middleUserName"
        width="150"
        show-overflow-tooltip
      />
      <el-table-column
        label="请求参数"
        align="center"
        prop="requestMessage"
        width="350"
        show-overflow-tooltip
      />
      <el-table-column
        label="访问结果"
        align="center"
        prop="responseMessage"
        width="350"
        show-overflow-tooltip
      />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createDate"
        width="150"
        show-overflow-tooltip
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
            v-hasPermi="['im:serverInterface:view']"
          >查看</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="handleDelete(scope.row)"
            v-hasPermi="['im:serverInterface:delete']"
          >删除</el-button
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
            {{ interfaceVisitRec.interfaceName }}
          </td>
          <td><b>类型：</b></td>
          <td>
            {{ interfaceVisitRec.responseStatus == '200'?'成功':'失败' }}
          </td>
        </tr>
        <tr>
          <td><b>服务名称：</b></td>
          <td>
            {{ server.serverName }}
          </td>
          <td><b>服务版本：</b></td>
          <td>
            {{ server.serverVersion }}
          </td>
        </tr>
        <tr>
          <td><b>服务商：</b></td>
          <td colspan="3">
            {{ interfaceVisitRec.middleUserName }}
          </td>
        </tr>
        <tr>
          <td><b>请求参数：</b></td>
          <td colspan="3">
            {{ interfaceVisitRec.requestMessage }}
          </td>
        </tr>
        <tr>
          <td><b>访问结果：</b></td>
          <td colspan="3">
            {{ interfaceVisitRec.responseMessage }}
          </td>
        </tr>
        <tr>
          <td><b>创建时间：</b></td>
          <td colspan="3">
            {{ interfaceVisitRec.createDate }}
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
  getOne,
  del
} from "@/api/middle/serverInterface";
export default {
  components: {},
  name: "ServerInterface",
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      title: "",
      // 查看显示弹出层
      openView: false,
      statusOptions: { "": "全部","200": "成功", "500": "失败" },
      // 数据表格
      interfaceVisitRecList: [],
      interfaceVisitRec: {},
      server: {},
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
        interfaceName: "",
        middleUserName: "",
        responseStatus: ""
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
        this.interfaceVisitRecList = response.data.datas;
        this.total = response.data.totalRows;
        this.loading = false;
      });
    },
    /** 查看按钮操作 */
    handleView (row) {
      const oid = row.oid;
      getOne(oid).then(response => {
        this.interfaceVisitRec = response.data.interfaceVisitRec;
        this.server = response.data.interfaceVisitRec.server;
        this.openView = true;
        this.title = "查看应用信息";
      });
    },
    /** 删除按钮操作 */
    handleDelete (row) {
      const oid = row.oid;
      this.$confirm(
        "删除接口记录可能会导致对应标签数据出错，是否确认删除?",
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(function () {
          return del(oid);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(function () { });
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
    // 取消按钮
    cancel () {
      this.openView = false;
    },
  }
};
</script>
<style></style>
