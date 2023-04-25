<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-09-23 13:07:46
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-10-13 13:33:31
 * @FilePath: \zf_web_ui\src\views\pc\roundTableAssistant\components\stagingServiceList.vue
 * @Description: 暂存服务列表
-->
<template>
  <div class="app-container">
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
            @click="getQueryHelpServiceHistoryList"
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
        :data="stagingServiceList"
        :cell-style="{ 'text-align': 'center' }"
        :header-cell-style="{ 'text-align': 'center' }"
        border
      >
        <el-table-column type="index" width="50" label="序号">
        </el-table-column>
        <template v-for="(item, index) in columnList">
          <el-table-column
            v-if="item.prop === 'operate'"
            :key="index"
            :label="item.label"
            :prop="item.prop"
            width="200"
          >
            <template slot-scope="scope">
              <div class="operateArea">
                <p @click="seeDetail(scope.row)">查看</p>
                <p v-show="scope.row.staging" @click="proceedStep(scope.row)">
                  继续办理
                </p>
              </div>
            </template>
          </el-table-column>
          <el-table-column
            v-else
            :key="index"
            :label="item.label"
            :prop="item.prop"
            show-overflow-tooltip
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
    <el-dialog
      title="办件信息"
      :visible.sync="handlingInformation"
      width="90%"
      class="guidelinesForHandlingAffairs"
      v-dialogDrag
    >
      <handlingInformation
        v-if="handlingInformation"
        :caseOid="caseOid"
        :useInfo="useInfo"
      >
      </handlingInformation>
    </el-dialog>
  </div>
</template>
<script>
// import { queryHelpServiceHistoryList } from "@/api/modules/workingGroup";
import { getTempServiceList } from "@/api/modules/helpAgent";
import handlingInformation from "../../operationFlow/components/handlingInformation.vue";
export default {
  name: "stagingServiceList",
  components: {
    handlingInformation,
  },
  data() {
    return {
      loadingTableData: false,
      stagingServiceList: [],
      tableHeight: 200,
      searchParams: {
        date: "",
        name: "",
        phone: "",
      },
      columnList: [
        { label: "办理事项", prop: "serviceMemo" },
        { label: "办事主体名称", prop: "applyUserName" },
        { label: "办事人姓名", prop: "name" },
        { label: "办事人手机号", prop: "phone" },
        { label: "上次办理日期", prop: "updateDate" },
        { label: "上次帮办人员", prop: "createBy" },
        { label: "操作", prop: "operate" },
      ],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      handlingInformation: false,
      dateValue: "",
      caseOid: "",
      useInfo: {},
    };
  },
  computed: {
    staffInformation() {
      return this.$store.state.pageData.staffInformation;
    },
  },
  mounted() {
    this.getQueryHelpServiceHistoryList();
  },
  methods: {
    //切换时间
    changeDate() {
      this.searchParams.beginTime = this.dateValue[0];
      this.searchParams.endTime = this.dateValue[1];
    },
    //查看暂存办件详情
    seeDetail(row) {
      this.handlingInformation = true;
      this.useInfo = row;
      this.caseOid = row.qlCaseId;
    },

    //继续办件
    proceedStep(row) {
      this.$emit("setProceedStepData", row);
    },

    resetData() {
      this.searchParams = {
        date: "",
        name: "",
        phone: "",
      };
      this.dateValue = "";
      this.getQueryHelpServiceHistoryList();
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.getQueryHelpServiceHistoryList();
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.getQueryHelpServiceHistoryList();
    },
    //获取暂存办件记录
    getQueryHelpServiceHistoryList() {
      const params = {
        name: this.searchParams.name,
        cardNo: "",
        phone: this.searchParams.phone,
        companyName: "",
        queueStatus: "",
        serviceStatus: "",
        beginTime: this.searchParams.beginTime
          ? this.searchParams.beginTime + " 00:00:00"
          : "",
        endTime: this.searchParams.endTime
          ? this.searchParams.endTime + " 23:59:59"
          : "",
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      };
      this.loadingTableData = true;
      getTempServiceList(params)
        .then((res) => {
          this.loadingTableData = false;
          if (res.code === 200) {
            this.tableHeight = this.$refs["tableArea"].clientHeight;
            this.stagingServiceList = res.data.data;
            this.total = res.data.total;
            this.stagingServiceList.forEach((item) => {
              item.staging = false;
              if (this.staffInformation.length) {
                this.staffInformation.forEach((ite) => {
                  if (
                    item.name === ite.name &&
                    item.phone === ite.phone &&
                    item.companyCode === ite.companyCode
                  ) {
                    item.staging = true;
                  }
                });
              }
              if (item.companyName) {
                item.applyUserName = item.companyName;
              } else {
                item.applyUserName = item.name;
              }
            });
          }
        })
        .catch((err) => {
          this.loadingTableData = false;
        });
    },
  },
};
</script>
<style lang="scss" scoped>
.app-container {
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
    height: calc(100% - 11rem);
  }
  .el-pagination {
    margin-top: 1.5rem;
    text-align: right;
  }
  .operateArea {
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: flex-start;
    padding-left: 10%;
    p {
      margin: 0;
      cursor: pointer;
      width: auto;
      height: 2.5rem;
      padding: 0 1.2rem;
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 1.25rem;
      font-size: 1.2857rem;
      font-family: Microsoft YaHei;
      font-weight: 400;
      &:nth-child(1) {
        background-color: #3ba6ff;
        color: #fff;
      }
      &:nth-child(2) {
        margin-left: 1.2rem;
        background: #edad0a;
        color: #fff;
      }
    }
  }
}
</style>
