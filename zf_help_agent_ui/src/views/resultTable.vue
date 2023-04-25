<template>
  <div class="primary-table">
    <h3>
      <i class="el-icon-document"></i>审核结果
    </h3>
    <el-table :data="tableData" border style="width: 100%">
      <el-table-column type="index" width="70"></el-table-column>
      <el-table-column label="审核名称" prop="name" width="350"></el-table-column>
      <el-table-column label="审核结果" prop="result" width="350"></el-table-column>
      <el-table-column label="查看审核详情" prop="btn">
        <template slot-scope="scope">
          <el-button v-if="scope.row.btn==1" type="primary" icon="el-icon-edit" size="mini" round>查看</el-button>
        </template>
        
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  data() {
    return {
      tableData: [
        {
          name: "身份证",
          result: "审核未通过",
          btn: 0,
        },
        {
          name: "-- 身份证正面",
          result: "审核未通过",
          btn:1
        },
        {
          name: "-- 身份证反面",
          result: "审核通过",
          btn:1
        },
      ],
    };
  },
  methods: {},
};
</script>

<style lang="scss" scoped>
.primary-table h3 {
  font-size: 14px;
  font-weight: normal;
  color: #333;
  margin: 0px 0px 10px 0px;
}
.primary-table {
  padding: 20px;
  box-sizing: border-box;
}
.primary-table .handle-btn {
  border: none;
  background: none;
  padding: 0;
  font-size: 16px;
}
.primary-table .el-table,
.primary-table .el-table th {
  font-size: 13px;
}
.primary-table .el-form-item:first-child {
  margin-left: 8px;
}
.primary-table .el-form-item {
  margin-bottom: 0;
  width: 130px;
  margin-right: 0;
  margin-left: 14px;
  font-size: 13px;
}
.primary-table .el-form-item__content {
  font-size: 13px;
}
.primary-table .inputTable .el-form-item:first-child {
  margin-left: 0;
}
.primary-table .inputTable .el-form-item {
  width: 100px;
}

.ruleForm-table table {
  border: 1px solid #ebeef5;
  border-collapse: collapse;
}
.ruleForm-table table tr th {
  color: #515a6e;
  font-size: 13px;
  background-color: #f8f8f9;
  text-align: left;
}
.ruleForm-table table tr th,
.ruleForm-table table tr td {
  padding: 17px 10px;
  font-size: 13px;
  color: #606266;
  border: 1px solid #ebeef5;
}
.ruleForm-table .el-form-item {
  margin-left: 0px !important;
  width: 120px;
}
</style>
