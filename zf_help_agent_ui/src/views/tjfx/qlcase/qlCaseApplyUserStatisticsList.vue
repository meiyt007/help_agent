/**
* @Author: chenjm
* @Date: 2021-04-21 11:15:16
* @Description: 办事人统计
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
      <el-form-item label="所属区划" prop="districtOidSelect">
        <treeselect
          class="treeselect"
          :defaultExpandLevel="1"
          noOptionsText="暂无数据"
          noResultsText="暂无数据"
          :show-count="true"
          v-model="queryParams.districtOidSelect"
          :options="districtOptions"
          placeholder="请输入所属区划"
        />
      </el-form-item>
      <el-form-item label="所属机构" prop="organOidSelect">
        <treeselect
          class="treeselect"
          :defaultExpandLevel="1"
          noOptionsText="暂无数据"
          noResultsText="暂无数据"
          :show-count="true"
          v-model="queryParams.organOidSelect"
          :options="organOptions"
          placeholder="请输入所属机构"
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

      <el-form-item label="时间维度" prop="dateTypeOid">
        <el-select
          v-model="queryParams.dateTypeOid"
          placeholder="请选择时间维度"
          @change="selectChanged"
        >
          <el-option
            v-for="dict in datetypeOptions"
            :key="dict.code"
            :label="dict.name"
            :value="dict.code"
          ></el-option>
        </el-select>
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
          >导出</el-button
        >
      </el-col>
    </el-row>
    <el-table
      v-loading="loading"
      :data="applyUserList"
      border
      :fit="true"
      :height="calcHeight2"
    >
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="区划名称"
        align="center"
        prop="districtName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="机构名称"
        align="center"
        prop="organName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="事项名称"
        align="center"
        prop="serviceName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="申请人数"
        align="center"
        prop="caseNum"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="性别/男"
        align="center"
        prop="manNum"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="性别/女"
        align="center"
        prop="womenNum"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="30岁以下(百分比)"
        align="center"
        prop="thirtyBelowNumZb"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="30-50岁(百分比)"
        align="center"
        prop="betweenThirtyAndFiftyNumZb"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="50岁以上(百分比)"
        align="center"
        prop="moreThenFiftyNumZb"
        :show-overflow-tooltip="true"
      />
      <!-- <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="getzeroCaseSxList(scope.row)" v-hasPermi="['sys:AreaSite:view']" >查看</el-button>
              &lt;!&ndash; <el-button size="mini" type="text" icon="iconfont zfsoft-shanchu" @click="handleDelete(scope.row)" v-hasPermi="['sys:district:delete']">删除</el-button> &ndash;&gt;
            </template>
          </el-table-column>-->
    </el-table>

    <pagination
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
import { qlCaseApplyUserStatisticsList, listExpApplyUser } from "@/api/tjfx/qlCase/qlCaseStatistics";
import { getMonday, getMonth, getYear } from "@/api/tjfx/timeDimension";
import { queryDistrictSimpleTree } from "@/api/sys/district";
import { queryOrganTree } from "@/api/sys/organ";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import Resolution from '@/mixins/resolution.js';
export default {
  name: "qlCaseApplyUserStatisticsList",
  mixins: [Resolution],
  components: { Treeselect },
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      applyUserList: [],
      //区划Tree
      districtOptions: [],
      //组织机构Tree
      organOptions: [],
      //零办件事项列表条件
      zeroSxOptions: [],
      zeroCaseShow: false,
      // 弹出层标题
      title: "",
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        districtOid: null,
        organOid: null,
        districtOidSelect: null,
        organOidSelect: null,
        startDate: "",
        endDate: "",
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
      datetypeOptions: [{ code: 0, name: '自定义' }, { code: 1, name: '本周' }, { code: 2, name: '本月' }, { code: 3, name: '本年' }],
    };
  },


  /*datetypeOptions  selectChanged  code name*/
  created () {
    this.getList();
    this.getDistrictTree();
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
    /** 查询零办件列表 */
    getList () {
      this.loading = true;
      qlCaseApplyUserStatisticsList(this.queryParams).then(response => {
        this.applyUserList = response.data.data;
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
        districtOid: null,
        organOid: null,
        districtOidSelect: null,
        organOidSelect: null,
        startDate: "",
        endDate: "",
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
        listExpApplyUser(queryParams);
      }).catch(function () { });
    },
    //选择时间维度
    selectChanged (value) {
      if (value == 1 || value == '1') {
        let bzstart = getMonday("s", 0);
        let bzend = getMonday("e", 0);
        this.queryParams.startDate = bzstart;
        this.queryParams.endDate = bzend;
      } else if (value == 2 || value == '2') {
        let monthStart = getMonth("s", 0);
        let monthEnd = getMonth("e", 0);
        this.queryParams.startDate = monthStart;
        this.queryParams.endDate = monthEnd;
      } else if (value == 3 || value == '3') {
        let yearStart = getYear("s", 0);
        let yearEnd = getYear("e", 0);
        this.queryParams.startDate = yearStart;
        this.queryParams.endDate = yearEnd;
      } else {
        this.queryParams.startDate = '';
        this.queryParams.endDate = '';
      }
    },

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
