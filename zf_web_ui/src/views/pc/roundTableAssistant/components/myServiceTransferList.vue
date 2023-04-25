<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-09-13 13:54:18
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-10-14 15:27:56
 * @FilePath: \zf_web_ui\src\views\pc\roundTableAssistant\components\myServiceTransferList.vue
 * @Description: 我的服务转派列表
-->
<template>
  <div class="transferService">
    <div class="tab-header">
      <el-form :model="searchParams" ref="searchForm">
        <el-form-item label="办理日期">
          <el-date-picker
            value-format="yyyy-MM-dd"
            v-model="dateValue"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :append-to-body="false"
            @change="changeDate"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="办事人姓名">
          <el-input v-model="searchParams.name"></el-input>
        </el-form-item>
        <el-form-item label="办事人手机号">
          <el-input v-model="searchParams.phone"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            @click="getTurnService"
            type="primary"
            icon="el-icon-search"
            size="medium"
            >搜索</el-button
          >
          <el-button
            size="medium"
            @click="resetData"
            icon="el-icon-refresh-right"
            >重置</el-button
          >
        </el-form-item>
      </el-form>
    </div>
    <div class="tableArea" ref="tableArea" v-loading="loadingTableData">
      <el-table
        ref="table"
        :height="tableHeight"
        :data="turnVerifyList"
        :cell-style="{ 'text-align': 'center' }"
        :header-cell-style="{ 'text-align': 'center' }"
        border
      >
        <el-table-column type="index" width="50" label="序号">
        </el-table-column>
        <template v-for="(item, index) in columnList">
          <el-table-column
            :key="index"
            :label="item.label"
            :prop="item.prop"
            show-overflow-tooltip
            :width="item.width"
            v-if="item.prop === 'turnStatus'"
          >
            <template slot-scope="scope">
              {{
                scope.row.turnStatus === "1"
                  ? "未审核"
                  : scope.row.turnStatus === "2"
                  ? "审核通过"
                  : "审核不通过"
              }}
            </template>
          </el-table-column>
          <el-table-column
            v-else
            :key="index"
            :label="item.label"
            :prop="item.prop"
            show-overflow-tooltip
            :width="item.width"
          >
          </el-table-column>
        </template>
      </el-table>
    </div>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pageNum"
      :page-sizes="[10, 20, 30, 50, 100]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
    >
    </el-pagination>
  </div>
</template>
<script>
import { getTurnService } from "@/api/modules/helpAgent";
import { second } from "@/utils/index";
export default {
  name: "myServiceTransferList",
  data() {
    return {
      loadingTableData: false,
      tableHeight: 300,
      turnVerifyList: [],
      dateValue: "",
      searchParams: {
        date: "",
        name: "",
        phone: "",
      },
      columnList: [
        { label: "办事人姓名", prop: "name", width: "" },
        { label: "办事人手机号", prop: "phone", width: "" },
        { label: "办理日期", prop: "turnTime", width: "" },
        { label: "服务时长", prop: "serviceDuration", width: "" },
        { label: "帮办人员", prop: "serviceWorkUserName" },
        { label: "服务状态", prop: "turnStatus" },
        { label: "描述", prop: "rollbackMemo" },
      ],
      pageNum: 1,
      pageSize: 10,
      total: 0,
    };
  },
  mounted() {
    this.getTurnService();
  },
  methods: {
    //切换时间
    changeDate() {
      this.searchParams.beginTime = this.dateValue[0];
      this.searchParams.endTime = this.dateValue[1];
    },
    resetData() {
      this.searchParams = {
        date: "",
        name: "",
        phone: "",
      };
      this.dateValue = [];
      this.getTurnService();
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.getTurnService();
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.getTurnService();
    },
    getTurnService() {
      const params = {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        name: this.searchParams.name,
        phone: this.searchParams.phone,
        beginTime: this.searchParams.beginTime
          ? this.searchParams.beginTime + " 00:00:00"
          : "",
        endTime: this.searchParams.endTime
          ? this.searchParams.endTime + " 23:59:59"
          : "",
      };
      getTurnService(params).then((res) => {
        if (res.code === 200) {
          this.tableHeight = this.$refs.tableArea.clientHeight;
          this.turnVerifyList = res.data.data;
          this.total = res.data.total;
          this.turnVerifyList.forEach((item) => {
            if (item.serviceDuration) {
              item.serviceDuration = second(item.serviceDuration);
            }
          });
        }
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.transferService {
  width: 100%;
  height: 100%;
  .tab-header {
    width: 100%;
    height: 5.7143rem;
    padding: 0.7143rem 0;

    .el-form {
      width: 100%;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: space-between;

      .el-form-item {
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: space-between;
        width: 23%;
        &:nth-child(1) {
          width: 30%;
          ::v-deep .el-form-item__content {
            .el-date-editor {
              .el-range-separator {
                width: 2rem;
              }
            }
          }
        }
        &:nth-last-child(1) {
          width: 16%;
        }
        ::v-deep .el-form-item__label {
          width: 10rem;
          height: 100%;
        }

        ::v-deep .el-form-item__content {
          flex: 1;

          .el-input {
            width: 100%;
          }

          .el-date-editor {
            width: 100%;
          }
        }
      }
    }
  }
  .tableArea {
    width: 100%;
    height: calc(100% - 12rem);
  }
  .el-pagination {
    margin-top: 1.5rem;
    text-align: right;
  }
}
</style>
