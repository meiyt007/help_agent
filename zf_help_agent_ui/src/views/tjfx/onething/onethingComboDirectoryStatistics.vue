/**
* @Author: liangss
* @Date: 2021-04-21 14:18:16
* @Description: 一件事目录统计
*/
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--区划数据-->
      <el-col :span="24" :xs="24">
        <el-form
          :model="queryParams"
          ref="queryForm"
          :inline="true"
          label-width="68px"
        >
          <!--          <el-form-item label="所属区划" prop="districtOidSelect">
            <treeselect  class="treeselect" :defaultExpandLevel="1" noOptionsText = "暂无数据" noResultsText="暂无数据" :show-count="true" v-model="queryParams.districtOidSelect" :options="districtOptions" placeholder="请输入所属区划" />
          </el-form-item>
          <el-form-item label="所属机构" prop="organOidSelect">
            <treeselect  class="treeselect" :defaultExpandLevel="1" noOptionsText = "暂无数据" noResultsText="暂无数据" :show-count="true" v-model="queryParams.organOidSelect" :options="organOptions" placeholder="请输入所属机构" />
          </el-form-item>-->
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

          <el-form-item label="所属分类">
            <treeselect
              class="treeselect"
              :defaultExpandLevel="1"
              noOptionsText="暂无数据"
              noResultsText="暂无数据"
              :show-count="true"
              v-model="queryParams.themeOid"
              :options="themeOptions"
              placeholder="请输入所属一件事分类"
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
              >导出</el-button
            >
          </el-col>
        </el-row>
        <el-table
          v-loading="loading"
          :data="comboDirectoryList"
          border
          :fit="true"
        >
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="目录名称"
            align="center"
            prop="comboDirectoryName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="分类名称"
            align="center"
            prop="themeName"
            :show-overflow-tooltip="true"
          />
          <el-table-column label="办件数" align="center">
            <template slot-scope="scope">
              <a
                href="javascript:void(0);"
                @click="getCaseComboList(scope.row, '')"
                >{{ scope.row.caseNum }}</a
              >
            </template>
          </el-table-column>
          <el-table-column label="受理数" align="center">
            <template slot-scope="scope">
              <a
                href="javascript:void(0);"
                @click="getCaseComboList(scope.row, 'SL')"
                >{{ scope.row.slMum }}</a
              >
            </template>
          </el-table-column>
          <el-table-column label="办结数" align="center">
            <template slot-scope="scope">
              <a
                href="javascript:void(0);"
                @click="getCaseComboList(scope.row, 'BJ')"
                >{{ scope.row.bjNUm }}</a
              >
            </template>
          </el-table-column>
          <!--
   <el-table-column label="办件数"  align="center" prop="caseNum" :show-overflow-tooltip="true"/>
          <el-table-column label="受理数"  align="center" prop="slMum" :show-overflow-tooltip="true"/>
          <el-table-column label="办结数"  align="center" prop="bjNUm" :show-overflow-tooltip="true"/>-->
          <!--          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              {{scope.row.bjNUm}}
            </template>
          </el-table-column>-->
        </el-table>

        <pagination
          v-show="total > 0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </el-col>
    </el-row>

    <!-- 办件列表start -->
    <el-dialog
      v-dialog-drag
      class="dialog-header"
      :visible.sync="caseCombo.show"
      :title="caseCombo.title"
      v-for="caseCombo in caseComboOptions"
      @close="closeResultView"
      :close-on-click-modal="false"
      width="80%"
      append-to-body
    >
      <el-scrollbar style="height: 500px">
        <case-combo-list
          :comboDireOid="caseCombo.comboDireOid"
          :caseType="caseCombo.caseType"
          :themeOid="caseCombo.themeOid"
          :startDate="caseCombo.startDate"
          :endDate="caseCombo.endDate"
        >
        </case-combo-list>
      </el-scrollbar>
    </el-dialog>
    <!-- 办件列表end-->
  </div>
</template>

<script>
import { getOnethingComboDirectoryStatistics, listExpOnethingComboDirectoryStatistics } from "@/api/tjfx/oneThingStatistics";
import { queryComboThemeSimpleTree } from "@/api/onething/sxpz/comboTheme";
import { queryDistrictSimpleTree } from "@/api/sys/district";
import { queryOrganTree } from "@/api/sys/organ";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import caseComboList from "@/views/tjfx/onething/comboCaseList";
export default {
  name: "onethingComboDirectoryStatistics",
  components: { Treeselect, caseComboList },
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      comboDirectoryList: [],
      //区划Tree
      districtOptions: [],
      //组织机构Tree
      organOptions: [],
      //一件事分类Tree
      themeOptions: [],
      //办件列表条件
      caseComboOptions: [],
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
        themeOid: null,
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
    /* this.getDistrictTree();*/
    //一件事分类树
    this.getThemeTree();
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
      getOnethingComboDirectoryStatistics(this.queryParams).then(response => {
        /* alert(JSON.stringify(response.data))*/
        this.comboDirectoryList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    /**获取一件事分类树**/
    getThemeTree (themeOid) {
      queryComboThemeSimpleTree(themeOid).then(response => {
        this.themeOptions = response.data;
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
    //调整办件列表页面
    getCaseComboList (row, caseType) {
      let title = "办件列表";
      let startDate = this.queryParams.startDate;
      let endDate = this.queryParams.endDate;
      let item = { show: true, comboDireOid: row.comboDirectoryOid, themeOid: row.themeOid, caseType: caseType, startDate: startDate, endDate: endDate, title: title };
      this.caseComboOptions.push(item)
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
        themeOid: null,
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
        listExpOnethingComboDirectoryStatistics(queryParams);
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
