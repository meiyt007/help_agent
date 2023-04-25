/**
* @Author: dingsn
* @Date: 2022-11-04
* @Description: 帮办人绩效考核统计
*/
<template>
  <div class="app-container" style="height: 100%">
    <!--区划数据-->
    <el-form
        :model="queryParams"
        ref="queryForm"
        :inline="true"
        label-width="68px"
    >
      <el-form-item label="查询时间">
        <el-date-picker
            v-model.trim="queryParams.beginTime"
            type="date"
            value-format="yyyy-MM-dd HH:mm:ss"
            :picker-options="pickerOptionsStart"
            placeholder="请选择开始日期"
        >
        </el-date-picker
        >-
        <el-date-picker
            v-model.trim="queryParams.endTime"
            type="date"
            value-format="yyyy-MM-dd HH:mm:ss"
            :picker-options="pickerOptionsEnd"
            placeholder="请选择结束日期"
        >
        </el-date-picker>
      </el-form-item>
<!--      <el-form-item label="帮办人姓名" prop="workUserName" label-width="90px">-->
<!--        <el-input-->
<!--            v-model.trim="queryParams.workUserName"-->
<!--            placeholder="请输入帮办人员姓名"-->
<!--            clearable-->
<!--            size="small"-->
<!--        />-->
<!--      </el-form-item>-->

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
            class="btn-reset"
        >重置</el-button
        >
      </el-form-item>
    </el-form>


    <el-table
        v-loading="loading"
        :data="haPerformanceAppraisalStatisticVoList"
        border
        :fit="true"
        height="calc(100% - 160px)"
    >
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>

      <el-table-column
          label="办事人姓名"
          align="center"
          prop="caseWorkerName"
          :show-overflow-tooltip="true"
      />
      <el-table-column
          label="服务开始时间"
          align="center"
          prop="serviceBeginTime"
          :show-overflow-tooltip="true"
      />
      <el-table-column
          label="服务结束时间"
          align="center"
          prop="serviceEndTime"
          :show-overflow-tooltip="true"
      />
      <el-table-column
          label="服务时长（分钟）"
          align="center"
          prop="serviceDuration"
          :show-overflow-tooltip="true"
      />
    </el-table>

<!--    <pagination-->
<!--        :total="total"-->
<!--        :page.sync="queryParams.pageNum"-->
<!--        :limit.sync="queryParams.pageSize"-->
<!--        @pagination="getList"-->
<!--    />-->
    <el-pagination style="margin-top: 1.5rem;text-align: right;" @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="queryParams.pageNum"
                   :page-sizes="[10, 20, 30, 50, 100]" :page-size="queryParams.pageSize" layout="total, sizes, prev, pager, next, jumper"
                   :total="total">
    </el-pagination>

  </div>
</template>

<script>
import { queryHaPerRegRecordPageResult } from "@/api/modules/performanceAppraisalStatistic.js";
/*import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";*/
export default {
  name: "performanceServiceMessage",
  props: {
    workUserOid: {
      type: String,
      default: ''
    }
  },
  computed: {
    basicUserInfo() {
      return this.$store.state.user.basicUserInfo;
    },
  },
  // components: { Treeselect },
  data () {
    return {
      // 遮罩层
      loading: false,
      // 总条数
      total: 0,
      // 应用表格数据
      haPerformanceAppraisalStatisticVoList: [],
      // 弹出层标题
      title: "",
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        beginTime: null,
        endTime: this.formatDate(new Date()),
        workUserName: null,
        workUserOid: this.workUserOid,
      },
      pickerOptionsStart: {
        disabledDate: time => {
          const endTimeVal = new Date(this.queryParams.endTime).getTime();
          if (endTimeVal) {
            return time.getTime() > endTimeVal - 0;
          }
        }
      },
      pickerOptionsEnd: {
        disabledDate: time => {
          const beginTimeVal = new Date((new Date(this.queryParams.beginTime) - 24 * 60 * 60 * 1000)).getTime();
          if (beginTimeVal) {
            return time.getTime() < beginTimeVal - 0;
          }
        }
      },
    };
  },
  created () {
    this.initBeginTime();
    this.getList();
  },
  watch: {

  },
  methods: {
    initBeginTime () {
      let time = new Date(new Date().setDate(1));
      this.queryParams.beginTime = this.formatDate(time);
    },

    formatDate (d) {
      var date = new Date(d);
      var YY = date.getFullYear() + '-';
      var MM =
          (date.getMonth() + 1 < 10
              ? '0' + (date.getMonth() + 1)
              : date.getMonth() + 1) + '-';
      var DD = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
      var hh =
          (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
      var mm =
          (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) +
          ':';
      var ss =
          date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds();
      return YY + MM + DD + ' ' + hh + mm + ss;
    },
    handleSizeChange(pageSize) {
      this.queryParams.pageSize = pageSize;
      this.getList();
    },
    handleCurrentChange(pageNum) {
      this.queryParams.pageNum = pageNum;
      this.getList();
    },
    /** 查询绩效统计列表 */
    getList () {
      this.loading = true;
      queryHaPerRegRecordPageResult(this.queryParams).then(response => {
        this.haPerformanceAppraisalStatisticVoList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },

    // 取消按钮
    cancel () {
      this.openInit = false;
      this.reset();
    },
    // 表单重置
    reset () {
      Object.assign(this.form, this.$options.data().form)
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.resetForm("queryForm");
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        beginTime: this.formatDate(new Date(new Date().setDate(1))),
        endTime: this.formatDate(new Date()),
        workUserName : "",
      };
      this.handleQuery();
    },


  },
};
</script>
<style lang="scss" scoped>
table {
  border-collapse: collapse;
}
</style>
