/** * @Author: yupeng */
<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <el-form :model="queryParams" ref="querySms" :inline="true" label-width="110px">
      <el-form-item label="工作人员" prop="workUserId">
        <el-select v-model.trim="queryParams.workUserId" placeholder="请选择工作人员" filterable clearable="">
          <el-option
            v-for="workUser in workUserList"
            :key="workUser.id"
            :label="workUser.name"
            :value="workUser.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="标题" prop="title">
        <el-col :span="24">
          <el-select v-model.trim="queryParams.title" placeholder="请选择标题" size="small" clearable="">
            <el-option label="材料补交提醒" value="材料补交提醒"></el-option>
            <el-option label="取号机取号通知" value="取号机取号通知"></el-option>
          </el-select>
        </el-col>
      </el-form-item>
      <el-form-item label="发送状态" prop="result">
        <el-col :span="24">
          <el-select v-model.trim="queryParams.result" placeholder="请选择发送状态" size="small" clearable="">
            <el-option label="成功" value="1"></el-option>
            <el-option label="失败" value="2"></el-option>
          </el-select>
        </el-col>
      </el-form-item>

      <el-form-item label="目标手机号" prop="phone">
        <el-input v-model.trim="queryParams.phone" placeholder="请输入手机号" clearable/>
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
    <el-table :data="smsList" stripe style="width: 100%" v-loading="loading">

      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="工作人员姓名" align="center" prop="workUserName" :show-overflow-tooltip="true"/>
      <el-table-column label="标题" align="center" prop="title" :show-overflow-tooltip="true"/>
      <el-table-column label="手机号" align="center" prop="phone" :show-overflow-tooltip="true"/>
      <el-table-column label="短信内容" align="center" prop="message" :show-overflow-tooltip="true"/>
      <el-table-column label="发送时间" align="center" prop="sendDate" :show-overflow-tooltip="true"/>
      <el-table-column
        label="发送结果"
        align="center"
        prop="result"
        :show-overflow-tooltip="true">
        <template slot-scope="scope">
<!--          1-未处理，2-已处理,3-取消预约-->
          <p v-if="scope.row.result==1">成功</p>
          <p v-if="scope.row.result==2">失败</p>
        </template>
      </el-table-column>


    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
                style="padding: 30px 0; text-align: center;" layout="total, sizes, prev, pager, next, jumper"
                @pagination="getList"/>
  </div>
</template>
<script>
import {
  queryPage,
  getAppointmentWorkUser
} from "@/api/ha/smsManagement/smsManagement.js";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "smsMangerment",
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
        phone: "",
        workUserId: "",
        result: "",
        title: "",
      },
      workUserList: [],
      smsList: []
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
      queryPage(this.queryParams).then(response => {
        if (response.code == '200') {
          this.smsList = response.data.data;
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
      this.resetForm("querySms");
      this.handleQuery();
    },
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


