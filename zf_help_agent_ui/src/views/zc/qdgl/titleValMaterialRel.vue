/**
* @Author: xldong
*/
<template>
  <div>
    <div class="primary-table">
      <el-table :data="titleValList" border :fit="true">
        <el-table-column label="序号" width="55" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="条件名称"
          width="300"
          prop="name"
          align="center"
          :show-overflow-tooltip="true"
        />
        <el-table-column label="选项" align="center">
          <template slot-scope="props">
            <el-table
              :data="props.row.sxServiceOptionVals"
              border
              :row-class-name="getRowClassName"
            >
              <el-table-column prop="name" label="选项值" align="center" />
              <el-table-column type="expand" prop="sxServiceMaterials">
                <template slot-scope="valprops">
                  <template
                    v-for="(item, index) in valprops.row.sxServiceMaterials"
                  >
                    <div :key="index">
                      <span>{{ index + 1 }}</span>
                      <span>、</span>
                      <span>{{ item.materialName }}</span>
                    </div>
                  </template>
                </template>
              </el-table-column>
            </el-table>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import { getListTitleAndOptionMaterial } from "@/api/zc/qdgl/sxService.js";
export default {
  name: "TitleValMaterialRel",
  props: ["serviceOid"],
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      titleValList: [],
    };
  },
  created () {
    this.getList();
  },

  methods: {
    /**
     * 查询所有的标题和选项
     */
    getList () {
      this.loading = true;
      getListTitleAndOptionMaterial(this.serviceOid).then(response => {
        this.titleValList = response.data;
        this.loading = false;
      });
    },

    //控制隐藏展开和收缩
    getRowClassName ({ row, rowIndex }) {
      if (row.sxServiceMaterials === null) {
        return 'row-expand-cover';
      } else {
        return '';
      }

    }
  },
};
</script>
<style lang="scss" scoped>
.primary-table h3 {
  font-size: 14px;
  font-weight: normal;
  color: #333;
  margin: 0px 0px 10px 0px;
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
  margin-right: 0;
  margin-left: 0px !important;
  padding: 0;
  box-sizing: border-box;
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

.add-btn {
  margin: 10px 0px;
  float: right;
}

.dialog-table table.tc tr td {
  text-align: center;
}

::v-deep .row-expand-cover .el-table__expand-icon {
  visibility: hidden;
}
</style>
