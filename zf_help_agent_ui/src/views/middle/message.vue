<template>
  <div class="app-container typeApp">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="100px"
    >
      <el-form-item label="中台用户名称" prop="middleUserName">
        <el-input
          v-model.trim="queryParams.middleUserName"
          placeholder="请输入中台用户名称"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="消息" prop="message">
        <el-input
          v-model.trim="queryParams.message"
          placeholder="请输入消息"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="是否已读">
        <el-radio-group v-model="queryParams.readStatus">
          <el-radio
            v-for="(status, key) in statusOptions"
            :key="key"
            :label="key"
          >{{ status }}</el-radio
          >
        </el-radio-group>
      </el-form-item>
      <el-form-item label="消息类型">
        <el-select
          v-model="queryParams.messageType"
          size="small"
          style="width: 240px"
        >
          <el-option
            v-for="(value, key) in messageTypeMap"
            :key="key"
            :label="value"
            :value="key"
          />
        </el-select>
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

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-delete"
          size="mini"
          @click="submitBatchForm"
        >删除</el-button
        >
      </el-col>
    </el-row>

    <el-table
      ref="multipleTable"
      :data="messageList"
      tooltip-effect="dark"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="60" align="center"> </el-table-column>
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="中台用户名称"
        align="center"
        prop="middleUserName"
        width="300"
        show-overflow-tooltip
      />
      <el-table-column
        label="消息"
        align="center"
        prop="message"
        width="700"
        show-overflow-tooltip
      />
      <el-table-column
        label="是否已读"
        align="center"
        prop="readStatus"
        width="100"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <p v-if="scope.row.readStatus=='Y'">是</p>
          <p v-if="scope.row.readStatus=='N'">否</p>
        </template>
      </el-table-column>
      <el-table-column
        label="消息类型"
        align="center"
        prop="messageType"
        width="150"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <p v-if="scope.row.messageType=='0'">停用通知</p>
          <p v-if="scope.row.messageType=='1'">服务通知</p>
          <p v-if="scope.row.messageType=='2'">申请通知</p>
          <p v-if="scope.row.messageType=='3'">升级通知</p>
        </template>
      </el-table-column>
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
            v-hasPermi="['im:message:view']"
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
          <td><b>中台用户名称：</b></td>
          <td colspan="3">
            {{ messageInfo.middleUserName }}
          </td>
        </tr>
        <tr>
          <td><b>消息：</b></td>
          <td colspan="3">
            {{ messageInfo.message }}
          </td>
        </tr>
        <tr>
          <td><b>是否已读：</b></td>
          <td>
            {{ messageInfo.readStatus == 'Y'?'是':'否' }}
          </td>
          <td><b>消息类型：</b></td>
          <td>
            {{ messageInfo.messageType | messageTypeHandle }}
          </td>
        </tr>
        <tr>
          <td><b>已读时间：</b></td>
          <td>
            {{ messageInfo.readDate }}
          </td>
          <td><b>创建时间：</b></td>
          <td>
            {{ messageInfo.createDate }}
          </td>
        </tr>
        <tr>
          <td><b>备注：</b></td>
          <td colspan="3">
            {{ messageInfo.memo }}
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
  batchDelete
} from "@/api/middle/message";
export default {
  components: {},
  name: "Message",
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      title: "",
      // 查看显示弹出层
      openView: false,
      statusOptions: { "": "全部","Y": "是", "N": "否" },
      // 消息类型
      messageTypeMap: { "0": "停用通知", "1": "服务通知", "2": "申请通知", "3": "升级通知" },
      // 数据表格
      messageList: [],
      messageInfo: {},
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
        middleUserName: "",
        message: "",
        readStatus: "",
        messageType: ""
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
        this.messageList = response.data.datas;
        this.total = response.data.totalRows;
        this.loading = false;
      });
    },
    handleSelectionChange(val){
      this.multipleSelection = val;
    },
    /** 查看按钮操作 */
    handleView (row) {
      const oid = row.oid;
      getOne(oid).then(response => {
        this.messageInfo = response.data;
        this.openView = true;
        this.title = "查看应用信息";
      });
    },
    submitBatchForm: function() {
      let _that = this;
      var oid = [];
      if (_that.multipleSelection.length <= 0) {
        _that.$message.error("请选择删除!");
        return false;
      }
      for (let ss = 0; ss < _that.multipleSelection.length; ss++) {
        oid.push(_that.multipleSelection[ss].oid);
      }
      var oids = oid.join(",");
      batchDelete(oids).then(response => {
        if (response.code === 200) {
          _that.msgSuccess("删除成功");
          this.getList();
        }
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
    // 取消按钮
    cancel () {
      this.openView = false;
    },
  },
  filters:{
    messageTypeHandle(data) {
      if(!data) {
        return '停用通知'
      }
      const statusMap = {
        0: '停用通知',
        1: '服务通知',
        2: '申请通知',
        3: '升级通知'
      }
      return statusMap[data]
    }
  }
};
</script>
<style></style>
