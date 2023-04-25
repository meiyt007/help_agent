/** * @Author: yupeng */
<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <el-form :model="queryParams" ref="queryAppointment" :inline="true" label-width="110px">
      <el-form-item label="预约的工作人员" prop="workUserId">
        <el-select v-model.trim="queryParams.workUserId" placeholder="请选择预约的工作人员" filterable>
          <el-option
            v-for="workUser in workUserList"
            :key="workUser.id"
            :label="workUser.name"
            :value="workUser.id">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="预约状态" prop="status">
        <el-col :span="24">
          <el-select v-model.trim="queryParams.status" placeholder="请选择预约状态" size="small">
            <el-option label="未处理" value="1"></el-option>
            <el-option label="已处理" value="2"></el-option>
            <el-option label="取消预约" value="3"></el-option>
          </el-select>
        </el-col>
      </el-form-item>

      <el-form-item label="预约人姓名" prop="name">
        <el-input v-model.trim="queryParams.name" placeholder="请输入预约人姓名" clearable/>
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
    <el-table :data="appointmentList" stripe style="width: 100%" v-loading="loading">

      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="预约人姓名" align="center" prop="name" :show-overflow-tooltip="true"/>
      <el-table-column label="身份证号" align="center" prop="cardNo" :show-overflow-tooltip="true"/>
      <el-table-column label="办理企业名称" align="center" prop="companyName" :show-overflow-tooltip="true"/>
      <el-table-column label="企业统一社会信用代码" align="center" prop="companyCode" :show-overflow-tooltip="true"/>
      <el-table-column label="预约时间" align="center" prop="appointmentTime" :show-overflow-tooltip="true"/>
      <el-table-column label="预约的工作人员" align="center" prop="workUserName" :show-overflow-tooltip="true"/>
      <el-table-column
        label="预约状态"
        align="center"
        prop="appointmentStatus"
        :show-overflow-tooltip="true">
        <template slot-scope="scope">
<!--          1-未处理，2-已处理,3-取消预约-->
          <p v-if="scope.row.appointmentStatus==1">未处理</p>
          <p v-if="scope.row.appointmentStatus==2">已处理</p>
          <p v-if="scope.row.appointmentStatus==3">取消预约</p>
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
  getAppointmentList,
  getAppointmentWorkUser
} from "@/api/ha/customerManagement/customerManagement.js";
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
        name: "",
        workUserId: "",
        status: ""
      },
      workUserList: [],
      appointmentList: []
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
      getAppointmentList(this.queryParams).then(response => {
        if (response.code == '200') {
          this.appointmentList = response.data.data;
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
      this.resetForm("queryAppointment");
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


