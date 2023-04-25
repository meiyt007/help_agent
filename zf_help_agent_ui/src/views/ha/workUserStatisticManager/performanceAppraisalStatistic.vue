/**
* @Author: dingsn
* @Date: 2022-11-04
* @Description: 帮办人绩效考核统计
*/
<template>
  <div class="app-container">
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
      <el-form-item label="帮办人姓名" prop="workUserName" label-width="90px">
        <el-input
          v-model.trim="queryParams.workUserName"
          placeholder="请输入帮办人员姓名"
          clearable
          size="small"
        />
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
          class="btn-reset"
        >重置</el-button
        >
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['im:perAppraisalStatistic:listExp']"
        >导出</el-button
        >
      </el-col>
    </el-row>

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
        label="帮办人员姓名"
        align="center"
        prop="workUserName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="服务时长（分钟）"
        align="center"
        prop="serviceDuration"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="加分项目时长（分钟）"
        align="center"
        prop="plusDuration"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="云客服语音时长（分钟）"
        align="center"
        prop="phoneDuration"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="季度工作量小计"
        align="center"
        prop="jiduSum"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="管理系数"
        align="center"
        prop="manageFactor"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="季度累计积分"
        align="center"
        prop="leijiSum"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="工作日天数"
        align="center"
        prop="fdWorkCount"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="日工作量"
        align="center"
        prop="dayWorkSum"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="工作效能百分比（含管理系数）"
        align="center"
        prop="gzxnPercent"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="服务人数（总数）"
        align="center"
        prop="servicePeopleNum"
        :show-overflow-tooltip="true"
      />

      <!--<el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="getPerformanceAppraisalStatisticVoList(scope.row)" v-hasPermi="['im:perAppraisalStatistic:view']" >查看</el-button>
        </template>
      </el-table-column>-->

    </el-table>

    <pagination
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 帮办人员绩效列表start -->
    <el-dialog
      v-dialog-drag
      class="dialog-header"
      :visible.sync="haPerformanceAppraisalStatisticShow"
      v-show="haPerformanceAppraisalStatisticShow"
      title="帮办人员绩效列表"
      v-for="(haPerformanceAppraisalStatistic, index) in haPerformanceAppraisalStatisticOptions"
      :key="index"
      @close="closeResultView"
      :close-on-click-modal="false"
      width="1100px"
      height='700px'
      scrollbar
      append-to-body
    >

      <!--<ql-case-sx-list-zero
        :beginTime="haPerformanceAppraisalStatistic.beginTime"
        :endTime="haPerformanceAppraisalStatistic.endTime"
      ></ql-case-sx-list-zero>-->

    </el-dialog>
  </div>
</template>

<script>
  import { performanceAppraisalStatisticVoList,listExp } from "@/api/ha/workUserStatisticManager/performanceAppraisalStatistic";
  /*import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";*/
  export default {
    name: "performanceAppraisalStatisticList",
    // components: { Treeselect },
    data () {
      return {
        // 遮罩层
        loading: false,
        // 总条数
        total: 0,
        // 应用表格数据
        haPerformanceAppraisalStatisticVoList: [],
        //帮办人员绩效统计列表条件
        haPerformanceAppraisalStatisticOptions: [],
        haPerformanceAppraisalStatisticShow: false,
        // 弹出层标题
        title: "",
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          beginTime: null,
          endTime: this.formatDate(new Date()),
          workUserName: null,
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
      /** 查询绩效统计列表 */
      getList () {
        this.loading = true;
        performanceAppraisalStatisticVoList(this.queryParams).then(response => {
          this.haPerformanceAppraisalStatisticVoList = response.data.data;
          this.total = response.data.total;
          this.loading = false;
        });
      },
      //查看某个帮办人绩效统计数据
      getPerformanceAppraisalStatisticVoList (row) {
        let item = { show: true, workUserName: row.workUserName, serviceDuration: row.serviceDuration, plusDuration : row.plusDuration, beginTime: this.queryParams.beginTime, endTime: this.queryParams.endTime };
        // console.log(item);
        this.haPerformanceAppraisalStatisticOptions.push(item);
        this.haPerformanceAppraisalStatisticShow = true;
      },
      //关闭弹框
      closeResultView () {
        this.haPerformanceAppraisalStatisticShow = false;
        this.haPerformanceAppraisalStatisticOptions = [];
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
      /** 导出按钮操作 */
      handleExport () {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出列表数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          listExp(queryParams);
        }).catch(function () {});
      }


    },
  };
</script>
<style lang="scss" scoped>
  table {
    border-collapse: collapse;
  }
  /*.treeselect {
    width: 200px;
  }*/
</style>
