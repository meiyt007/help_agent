/**
* @Author: wangxl
*/
<template>
  <div>
    <!-- <el-row :gutter="20">
      <el-col :span="24" :xs="24"> -->
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="108px">
          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-refresh" size="mini" @click="batchChoose" class="btn-reset">批量选择
            </el-button>
          </el-form-item>
        </el-form>
        <el-table ref="multipleTable" :data="sxServiceList" tooltip-effect="dark" style="width: 100%"
          @selection-change="handleSelectionChange" border class="table1">
          <el-table-column type="selection" width="60" align="center">
          </el-table-column>
          <el-table-column prop="serviceName" label="事项名称" align="center" :show-overflow-tooltip="true">
          </el-table-column>
          <el-table-column prop="resultName" label="证照名称" align="center" :show-overflow-tooltip="true">
          </el-table-column>
          <el-table-column prop="resultSampleName" label="证照样本" align="center" :show-overflow-tooltip="true">
          </el-table-column>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="iconfont zfsoft-penzhi" v-if="scope.row.ifCombo==0"
                @click="chooseRe(scope.row)" v-hasPermi="['combo:directory:service']">选择</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
          @pagination="getSxServiceList" />
      <!-- </el-col>
    </el-row> -->
  </div>
</template>

<script>
  import {
    saveComboResult,
    querySxServiceResultPage
  } from "@/api/onething/sxpz/chooseResult";
  export default {
    components: {},
    name: "SxServiceResultList",
    props: ["direOid"],
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        attatotal: 0,
        // 应用表格数据
        sxServiceList: [],
        //查询目录名称参数
        sxServiceName: '',
        comboDirectoryOid: "",
        // 弹出层标题
        title: "",
        multipleSelection: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
        },
        defaultProps: {
          children: "children",
          label: "label"
        }
      };
    },

    created() {
      this.getSxServiceList();
      this.mounted();
    },
    methods: {
      //获取父页面的值
      mounted() {
        this.comboDirectoryOid = this.direOid;
      },

      /** 查询目录列表 */
      getSxServiceList() {
        this.loading = true;
        querySxServiceResultPage(this.queryParams, this.direOid).then(response => {
          this.sxServiceList = response.data.data;
          this.total = response.data.total;
          this.loading = false;
        });
      },

      // 取消按钮
      cancel() {
        this.reset();
      },
      // 表单重置
      reset() {
        this.resetForm("form")
      },
      //保存选中的结果
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      /**  批量选择**/
      batchChoose() {
        let serviceOids = "";
        //获取选中结果
        this.multipleSelection.forEach(ser => {
          serviceOids += ser.serviceOid + ",";
        });
        if (serviceOids == "") {
          this.msgWarning("请选择事项结果！");
          return false;
        }
        if (serviceOids) {
          serviceOids = serviceOids.substring(0, serviceOids.length - 1);
        }
        saveComboResult(serviceOids, this.direOid).then(response => {
          if (response.code === 200) {
            this.msgSuccess("选择成功");
            this.$emit('sx-dialog-close');
          }
        });
      },
      /**  选择事项 **/
      chooseRe(row) {
        const serviceOid = row.serviceOid;
        saveComboResult(serviceOid, this.direOid).then(response => {
          if (response.code === 200) {
            this.msgSuccess(" 选择成功");
            this.$emit('sx-dialog-close');
          }
        });
      }
    }
  };
</script>
<style lang="scss" scoped>
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
  }

  .dialog-table table tr td {
    color: #606266;
    text-align: left !important;
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

  .required {
    color: #FF0000;
    font-size: 20px;
    display: inline-block;
    vertical-align: middle;
    margin: 3px 5px 0px 0px;
  }
</style>
