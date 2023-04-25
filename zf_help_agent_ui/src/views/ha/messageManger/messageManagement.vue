/** * @Author: yupeng */
<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <el-form :model="queryParams" ref="queryMessage" :inline="true" label-width="110px">
      <el-form-item label="消息接收人" prop="receiver">
        <el-select v-model.trim="queryParams.receiver" placeholder="请选择消息接收人" filterable>
          <el-option
            v-for="workUser in workUserList"
            :key="workUser.id"
            :label="workUser.name"
            :value="workUser.id">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="消息来源" prop="sender">
        <el-col :span="24">
          <el-select v-model.trim="queryParams.sender" placeholder="请选择消息来源" size="small">
            <el-option
              v-for="workUser in workUserList"
              :key="workUser.id"
              :label="workUser.name"
              :value="workUser.id">
            </el-option>
          </el-select>
        </el-col>
      </el-form-item>

      <el-form-item label="消息标题" prop="title">
        <el-input v-model.trim="queryParams.title" placeholder="请输入消息标题" clearable/>
      </el-form-item>

      <el-form-item class="fr mr0">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
          搜索
        </el-button>
        <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">
          重置
        </el-button>
      </el-form-item>
    </el-form>
    <!--表头 -->
    <el-table :data="messageList" stripe style="width: 100%" v-loading="loading">

      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="消息标题" align="center" prop="title" :show-overflow-tooltip="true"/>
      <el-table-column label="消息接收人" align="center" prop="receiver" :show-overflow-tooltip="true"/>
      <el-table-column label="消息来源" align="center" prop="sender" :show-overflow-tooltip="true"/>
      <el-table-column label="发送时间" align="center" prop="sendTime" :show-overflow-tooltip="true"/>
      <el-table-column label="读取状态" align="center" prop="readStatus" :show-overflow-tooltip="true"/>
      <el-table-column label="读取时间" align="center" prop="readTime" :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="iconfont zfsoft-shouquan"
                     @click="view(scope.row)">
            查看
          </el-button>
        </template>
      </el-table-column>

    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
                style="padding: 30px 0; text-align: center;" layout="total, sizes, prev, pager, next, jumper"
                @pagination="getList"/>
    <!-- 查看按钮 -->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="title" :visible.sync="showDetail" v-if="showDetail"
               width="900px" height="700px" append-to-body>
      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
        <colgroup>
          <col width="15%"/>
          <col width="35%"/>
          <col width="15%"/>
          <col width="35%"/>
          <col width="35%"/>
          <col width="35%"/>
        </colgroup>
        <tr>
          <td>
            <b>消息标题：</b>
          </td>
          <td colspan="3">
            <el-col :span="24">{{ message.title }}</el-col>
          </td>
        </tr>
        <tr>
          <td>
            <b>消息内容：</b>
          </td>
          <td colspan="3">
            <el-col :span="24">{{ message.content }}</el-col>
          </td>
        </tr>
        <tr>
          <td>
            <b>链接地址：</b>
          </td>
          <td colspan="3">
            <el-col :span="24">{{ message.url }}</el-col>
          </td>
        </tr>
        <tr>
          <td>
            <b>消息接收人：</b>
          </td>
          <td>
            <el-col :span="24">{{ message.receiver }}</el-col>
          </td>
          <td>
            <b>消息来源：</b>
          </td>
          <td>
            <el-col :span="24">{{ message.sender }}</el-col>
          </td>
        </tr>
        <tr>
          <td>
            <b>消息发送时间：</b>
          </td>
          <td>
            <el-col :span="24">{{ message.sendTime }}</el-col>
          </td>
          <td>
            <b>消息状态：</b>
          </td>
          <td>
            <el-col :span="24">{{ message.readStatus }}</el-col>
          </td>
        </tr>
        <tr>
          <td>
            <b>消息读取时间：</b>
          </td>
          <td colspan="3">
            <el-col :span="24">{{ message.readTime }}</el-col>
          </td>
        </tr>
      </table>
      <div slot="footer" class="zf-text-center">
        <el-button @click="showDetail=false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {getAppointmentWorkUser} from "@/api/ha/customerManagement/customerManagement.js";
import {getMessageList} from "@/api/ha/messageManagement/messageManagement.js";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";


export default {
  name: "appointmentMangerment",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      //人员查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        receiver: "",
        sender: "",
        title: ""
      },
      workUserList: [],
      messageList: [],
      title: "消息详情",
      showDetail: false,
      message: {}
    };
  },
  created() {
    this.getList();
    this.getWorkUser();
  },
  methods: {

    //加载列表
    getList() {
      this.loading = true;
      getMessageList(this.queryParams).then(response => {
        if (response.code == '200') {
          this.messageList = response.data.data;
          this.total = response.data.total;
          this.loading = false;
        } else {
          this.loading = false;
        }
      })
    },

    //获取所有的预约的工作人员
    getWorkUser() {
      getAppointmentWorkUser().then(response => {
        if (response.code == '200') {
          this.workUserList = response.data;
        }
      })
    },

    //搜索
    handleQuery() {
      this.queryParams.pageNumber = 1;
      this.queryParams.pageSize = 10;
      this.getList();
    },

    //搜索重置
    resetQuery() {
      this.resetForm("queryMessage");
      this.handleQuery();
    },

    //查看消息详情
    view(row) {
      this.message = row;
      this.showDetail = true;
    }
  }
}
</script>
<style lang="scss" scoped>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}

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


