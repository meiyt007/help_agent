/**
* @Author: chenjm
* @Date: 2021-04-21 11:15:16
* @Description: 单办件事项办件统计
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
      <!-- <el-form-item label="所属区划" prop="districtOidSelect">
            <treeselect  class="treeselect" :defaultExpandLevel="1" noOptionsText = "暂无数据" noResultsText="暂无数据" :show-count="true" v-model="queryParams.districtOidSelect" :options="districtOptions" placeholder="请输入所属区划" />
          </el-form-item>
          <el-form-item label="所属机构" prop="organOidSelect">
            <treeselect  class="treeselect" :defaultExpandLevel="1" noOptionsText = "暂无数据" noResultsText="暂无数据" :show-count="true" v-model="queryParams.organOidSelect" :options="organOptions" placeholder="请输入所属机构" />
          </el-form-item>-->
      <el-form-item label="事项类型" prop="serviceTypeOid">
        <treeselect
          v-model.trim="queryParams.serviceTypeOid"
          :options="serviceTypeOptions"
          style="width: 240px"
          noOptionsText="暂无数据"
          :defaultExpandLevel="1"
          placeholder="请选择事项类型"
        />
      </el-form-item>
      <el-form-item label="时间">
        <el-date-picker
          v-model="queryParams.startDate"
          type="date"
          value-format="yyyy-MM-dd"
          :picker-options="pickerOptionsStart"
          placeholder="请选择开始日期"
        >
        </el-date-picker
        >-
        <el-date-picker
          v-model="queryParams.endDate"
          type="date"
          value-format="yyyy-MM-dd"
          :picker-options="pickerOptionsEnd"
          placeholder="请选择结束日期"
        >
        </el-date-picker>
      </el-form-item>

      <el-form-item class="fr mr0">
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >统计</el-button
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
          >导出</el-button
        >
      </el-col>
    </el-row>
    <el-table
      v-loading="loading"
      :data="serviceCaseList"
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
        label="事项名称"
        align="center"
        prop="serviceName"
        :show-overflow-tooltip="true"
      />
      <!-- <el-table-column label="事项编码"  align="center" prop="organName" :show-overflow-tooltip="true"/>-->
      <el-table-column
        label="事项类型"
        align="center"
        prop="serviceTypeName"
        :show-overflow-tooltip="true"
      />
      <el-table-column label="受理数" align="center">
        <template slot-scope="scope">
          <a
            href="javascript:void(0);"
            @click="getServiceCaseSxList(scope.row, 'sl')"
            >{{ scope.row.slNum }}</a
          >
        </template>
      </el-table-column>
      <el-table-column label="办结数" align="center">
        <template slot-scope="scope">
          <a
            href="javascript:void(0);"
            @click="getServiceCaseSxList(scope.row, 'bj')"
            >{{ scope.row.bjNum }}</a
          >
        </template>
      </el-table-column>
      <!--          <el-table-column label="受理量"    align="center" prop="slNum" :show-overflow-tooltip="true"/>
          <el-table-column label="办结量"    align="center" prop="bjNum" :show-overflow-tooltip="true"/>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="getServiceCaseSxList(scope.row,'sl')" v-hasPermi="['sys:AreaSite:view']" >受理查看</el-button>
              <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="getServiceCaseSxList(scope.row,'bj')" v-hasPermi="['sys:AreaSite:view']" >办结查看</el-button>
            </template>
          </el-table-column>-->
    </el-table>

    <pagination
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog
      v-dialog-drag
      class="dialog-header"
      :visible.sync="serviceCaseShow"
      v-show="serviceCaseShow"
      :title="serviceCase.titleShow"
      v-for="(serviceCase, index) in serviceCaseOptions"
      :key="index"
      @close="closeResultView"
      :close-on-click-modal="false"
      width="1100px"
      height='700px'
      scrollbar
      append-to-body
    >

        <ql-case-service-case-list
          :viewType="serviceCase.viewType"
          :serviceTypeOid="serviceCase.serviceTypeOid"
          :startDate="serviceCase.startDate"
          :endDate="serviceCase.endDate"
          :serviceOid="serviceCase.serviceOid"
        ></ql-case-service-case-list>

    </el-dialog>
  </div>
</template>

<script>
import { qlCaseServiceCaseStatisticsList, listExpServiceCase } from "@/api/tjfx/qlCase/qlCaseStatistics";
import { validateNumber, validIntNoZero } from "@/utils/validate";
import { queryDistrictSimpleTree } from "@/api/sys/district";
import { queryOrganTree } from "@/api/sys/organ";
import { queryServiceTypeSimpleTree } from "@/api/sxpt/serviceType";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import qlCaseServiceCaseList from "@/views/tjfx/qlcase/qlCaseServiceCaseList";
export default {
  name: "qlCaseServiceCaseStatisticsList",
  components: { Treeselect, qlCaseServiceCaseList },
  data () {
    return {
      // 遮罩层
      loading: false,
      // 总条数
      total: 0,
      // 应用表格数据
      serviceCaseList: [],
      //区划Tree
      districtOptions: [],
      //组织机构Tree
      organOptions: [],
      //零办件事项列表条件
      serviceCaseOptions: [],
      serviceCaseShow: false,
      // 事项类型
      serviceTypeOptions: [],
      // 弹出层标题
      title: "",
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        startDate: "",
        endDate: "",
        serviceTypeOid: null, // 事项类型
      },
      pickerOptionsStart: {
        disabledDate: time => {
          const endDateVal = new Date(this.queryParams.endDate).getTime()
          if (endDateVal) {
            return time.getTime() > endDateVal - 0
          }
        }
      },
      pickerOptionsEnd: {
        disabledDate: time => {
          const beginDateVal = new Date((new Date(this.queryParams.startDate) - 24 * 60 * 60 * 1000)).getTime()
          if (beginDateVal) {
            return time.getTime() < beginDateVal - 0
          }
        }
      },
    };
  },
  created () {
    this.getList();
    this.getServiceTypeTree();
    //this.getDistrictTree();
  },
  watch: {
    'queryParams.districtOidSelect': {
      handler (val, oldVal) {
        let dataId = null != val ? val.substring(val.lastIndexOf('-') + 1, val.length) : '';
        //机构加载
        this.queryParams.districtOid = dataId;
        this.$set(this.queryParams, 'organOid', null);
        this.$set(this.queryParams, 'organOidSelect', null);
        if (dataId) {
          this.getOrganTree(dataId);
        } else {
          this.organOptions = [];
        }
      },
      deep: true
    },
    'queryParams.organOidSelect': {
      handler (val, oldVal) {
        let dataId = null != val ? val.substring(val.lastIndexOf('-') + 1, val.length) : '';
        this.queryParams.organOid = dataId;
      }
    }
  },
  methods: {
    /** 获取事项类型树 */
    getServiceTypeTree (serviceType) {
      let _that = this;
      queryServiceTypeSimpleTree(serviceType).then(response => {
        _that.serviceTypeOptions = response.data;
      });
    },
    /** 查询事项办件列表 */
    getList () {
      this.loading = true;
      qlCaseServiceCaseStatisticsList(this.queryParams).then(response => {
        this.serviceCaseList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    /** 获取区划树 */
    getDistrictTree (districtOid) {
      queryDistrictSimpleTree(districtOid).then(response => {
        this.districtOptions = response.data;
      });
    },
    /** 获取区划树 */
    getOrganTree (districtOid) {
      queryOrganTree(districtOid).then(response => {
        this.organOptions = response.data;
      });
    },
    //查看事项列表
    getServiceCaseSxList (row, viewType) {
      let titleShow = '办件查看';
      if (viewType === 'sl') {
        titleShow = '受理办件列表';
      } else {
        titleShow = '办结办件列表';
      }
      let item = { show: true, titleShow: titleShow, serviceOid: row.serviceOid, viewType: viewType, serviceTypeOid: this.queryParams.serviceTypeOid, startDate: this.queryParams.startDate, endDate: this.queryParams.endDate };
      this.serviceCaseOptions.push(item)
      this.serviceCaseShow = true;
    },
    //关闭弹框
    closeResultView () {
      this.serviceCaseShow = false;
      this.serviceCaseOptions = [];
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
        startDate: "",
        endDate: "",
        serviceTypeOid: null, // 事项类型
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
        listExpServiceCase(queryParams);
      }).catch(function () { });
    }


  },
};
</script>
<style lang="scss" scoped>
table {
  border-collapse: collapse;
}
.treeselect {
  width: 200px;
}
</style>
