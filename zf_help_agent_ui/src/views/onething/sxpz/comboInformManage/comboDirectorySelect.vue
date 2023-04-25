/*
* @Description:一件事主题选择
* @Author: dxl
**/
<template>
  <div>
      <!--一件事主题列表数据-->
      <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="70px">
          <el-form-item label="目录名称" prop="comboDirectoryName">
            <el-input v-model.trim="queryParams.comboDirectoryName" placeholder="请输入目录名称" clearable size="small"
              @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="所属主题">
            <treeselect class="treeselect" :defaultExpandLevel="1" noOptionsText="暂无数据" noResultsText="暂无数据"
              :show-count="true" v-model="queryParams.themeOidSelect" :options="themeOptions" placeholder="请输入所属主题" />
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置
            </el-button>
          </el-form-item>
        </el-form>
      <!--<label style="color: #bd2c00;margin-top: 5px" class="mb10 inlineBlock">&nbsp;&nbsp;&nbsp;&nbsp;（一件事主题必须为已发布、已授权、已上架才能登记办件！）</label>-->

      <el-table v-loading="loading" :data="comboDirectoryList" :fit="true">
        <el-table-column label="序号" width="55" type="index" align="center">
          <template slot-scope="scope">
            <span>{{scope.$index + 1}}</span>
          </template>
        </el-table-column>
        <el-table-column label="分类名称" align="center" prop="themeName" :show-overflow-tooltip="true" />
        <el-table-column label="目录名称" align="center" prop="comboDirectoryName" :show-overflow-tooltip="true" />
        <el-table-column label="所属区划" align="center" prop="districtName" :show-overflow-tooltip="true" />
        <el-table-column label="主办部门" align="center" prop="mainOrganName" :show-overflow-tooltip="true" />
          :show-overflow-tooltip="true" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="iconfont" @click="handleSelect(scope.row)"
              v-hasPermi="['combo:directory:view']">选择</el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
        @pagination="getList" />
  </div>
</template>

<script>
  import {pageOfCombo} from "@/api/onething/comboAuthorize/comboRegisterAuthorize";
 import Treeselect from "@riophae/vue-treeselect";
 import "@riophae/vue-treeselect/dist/vue-treeselect.css";
 import {queryComboThemeSimpleTree} from "@/api/onething/sxpz/comboTheme";

 export default {
    components: {Treeselect},
    name: "ComboDirectorySelect",
    props:['notContainsComboDirectoryOids','industryType'],
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 应用表格数据
        comboDirectoryList: [],
        themeOptions:[],

        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          comboDirectoryName: '',
          status: '1',
          themeOid: null,
          notContainOids:[],
          industryType:this.industryType
        },

        defaultProps: {
          children: "children",
          label: "label"
        },

      };
    },
    watch: {
      //切换所属主题目录
      'queryParams.themeOidSelect': {
        handler(val, oldVal) {
          let dataId = null != val ? val : '';
          this.queryParams.themeOid = dataId;
        }
      },

    },
    created() {
      this.getList();
      //一件事目录树
      this.getThemeTree();

    },
    methods: {

      comboDirectoryClose() {
        this.openView = false;
      },

      /** 查询一件事主题列表 */
      getList() {
        this.loading = true;
        if(this.notContainsComboDirectoryOids!=undefined){
            this.queryParams.notContainOids=this.notContainsComboDirectoryOids;
        }
        console.log("11111111111111",JSON.stringify(this.queryParams));
        pageOfCombo(this.queryParams).then(response => {
          this.comboDirectoryList = response.data.data;
          this.total = response.data.total;
          this.loading = false;
        });
      },
      /**获取一件事目录树**/
      /**获取分类树**/
      getThemeTree (themeOid) {
        queryComboThemeSimpleTree(themeOid).then(response => {
          this.themeOptions = response.data;
        });
      },


      // 取消按钮
      cancel() {
        this.openInit = false;
        this.openCancel = false;
        this.reset();
      },

      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.queryParams.themeOidSelect = null;
        this.queryParams.themeOid = '';
        this.handleQuery();
      },
      /** 选择按钮操作 */
      handleSelect(row) {
        this.$emit("select-directory",row.comboDirectoryOid,row.comboDirectoryName);
        this.$emit("combo-directory-select-close");
      },

    }
  };

</script>
<style lang="scss" scoped>
  .treeselect {
    width: 200px;
  }

  .treeselect240 {
    width: 240px;
  }

  table {
    border-collapse: separate !important;
  }

</style>
