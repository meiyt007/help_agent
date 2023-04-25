/**
* @Author: liyanqing
*/
<template>
  <el-tabs v-model="activeTab" @tab-click="activeTabChange">
    <el-tab-pane label="网站移动端" name="webMobile" >
    </el-tab-pane>
    <el-tab-pane label="自助终端" name="selfHelp">
    </el-tab-pane>
    <div>
      <el-col :span="24" :xs="24">
        <el-form :model="queryParamsForCombo" ref="queryFormForCombo" :inline="true" label-width="110px">
          <el-form-item label="目录名称" prop="comboDirectoryName">
            <el-input v-model.trim="queryParamsForCombo.comboDirectoryName" placeholder="请输入目录名称" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="目录编码" prop="comboDirectoryCode">
            <el-input v-model.trim="queryParamsForCombo.comboDirectoryCode" placeholder="请输入目录编码" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery()">搜索</el-button>
            <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery()" class="btn-reset" style="margin-right: 20px;">重置</el-button>
          </el-form-item>
        </el-form>
        <el-table  v-loading="loading" :data="comboDirectories" border :fit="true">
          <el-table-column  label="序号" width="100" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="目录名称" align="center"  prop="comboDirectoryName" :show-overflow-tooltip="true"/>
          <el-table-column label="编码" align="center" prop="comboDirectoryCode" :show-overflow-tooltip="true"/>
          <el-table-column v-if="queryParamsForCombo.type == 0" label="上架排序号" width="200" align="center" prop="webSort" :show-overflow-tooltip="true"/>
          <el-table-column v-if="queryParamsForCombo.type == 1" label="上架排序号" width="200" align="center" prop="selfHelpSort" :show-overflow-tooltip="true"/>
          <el-table-column label="操作"  align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text"
                         icon="el-icon-setting"  @click="setSortShow(scope.row)" v-hasPermi="['sys:comboUpperShelfSort:set']">设置序号</el-button>
              <el-button size="mini" type="text" v-if="queryParamsForCombo.type == 0 && scope.row.webSort != ''&& scope.row.webSort != null "
                         icon="el-icon-upload2"  @click="setSortUpAndDown(scope.row, 0)" v-hasPermi="['sys:comboUpperShelfSort:move']">上移</el-button>
              <el-button size="mini" type="text" v-if="queryParamsForCombo.type == 0 && scope.row.webSort != ''&& scope.row.webSort != null "
                         icon="el-icon-download"  @click="setSortUpAndDown(scope.row, 1)" v-hasPermi="['sys:comboUpperShelfSort:move']">下移</el-button>
              <el-button size="mini" type="text" v-if="queryParamsForCombo.type == 1 && scope.row.selfHelpSort != ''&& scope.row.selfHelpSort != null "
                         icon="el-icon-upload2"  @click="setSortUpAndDown(scope.row, 0)" v-hasPermi="['sys:comboUpperShelfSort:move']">上移</el-button>
              <el-button size="mini" type="text" v-if="queryParamsForCombo.type == 1 && scope.row.selfHelpSort != ''&& scope.row.selfHelpSort != null "
                         icon="el-icon-download"  @click="setSortUpAndDown(scope.row, 1)" v-hasPermi="['sys:comboUpperShelfSort:move']">下移</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="comboTotal > 0" :total="comboTotal" :page.sync="queryParamsForCombo.pageNumber" :limit.sync="queryParamsForCombo.pageSize" @pagination="getListForCombo"/>
      </el-col>
    </div>
    <!-- 撤销操作 -->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="title" v-if="setSortView" :visible.sync="setSortView" width="240px" append-to-body>
      <el-form ref="form" class="dialog-table">
        <el-input-number v-model.trim="form.sortNumber" :min="1" :max="999999999"/>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="setSort" style="width: 145px;margin-right: 10px;">设 置</el-button>
      </div>
    </el-dialog>
  </el-tabs>
</template>


<script>
import {setSort,setSortUpAndDown, pageUpperShelf} from "@/api/onething/sxpz/comboUpperShelf";
export default {
  name: "formRunConfiguration",
  data() {
    return {
      // 遮罩层
      loading: true,
      title: '',
      //Tbar选项
      activeTab: 'webMobile',
      //总条数
      comboTotal: 0,
      //目录数据
      comboDirectories: [],
      //查询参数
      queryParamsForCombo: {
        pageNumber: 1,
        pageSize: 10,
        comboDirectoryName: "",
        comboDirectoryCode: "",
        type: 0
      },
      form: {
        sortNumber: ''
      },
      setSortView: false,
    };
  },
  created() {
    this.getListForCombo();
  },
  methods: {
    /** 查询列表*/
    getListForCombo() {
      this.loading = true;
      pageUpperShelf(this.queryParamsForCombo).then(response => {
        this.comboDirectories = response.data.data;
        this.comboTotal = response.data.total;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParamsForCombo.pageNumber = 1;
      this.getListForCombo();
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryFormForCombo');
      this.handleQuery();
    },
    /*上下架处理*/
    submitUpper(row){
      let _that = this;
    },
    //tbar 切换
    activeTabChange(tab, event) {
      if(tab.name == 'webMobile'){
        this.queryParamsForCombo.type = 0;
      }else{
        this.queryParamsForCombo.type = 1;
      }
      this.getListForCombo();
    },
    //设置序号显示
    setSortShow(row){
      this.setSortView = true;
      this.title = '设置排序号';
      this.form.id = row.id;
      this.form.type = this.queryParamsForCombo.type;
    },
    //设置序号
    setSort(){
      let _that = this;
      if(!(/(^[1-9]\d*$)/.test(_that.form.sortNumber))){
        _that.msgError("请输入正整数");
        return
      }
      setSort(_that.form).then(response => {
        _that.msgSuccess("操作已完成");
        _that.setSortView = false;
        setTimeout(() => {
          _that.getListForCombo();
        }, 10);
        _that.reset();
      });
    },
    //上移下移操作
    setSortUpAndDown(row, upAndDown){
      let _that = this;
      _that.form.id = row.id;
      _that.form.type = this.queryParamsForCombo.type;
      _that.form.upAndDown = upAndDown;
      setSortUpAndDown(_that.form).then(response => {
        _that.msgSuccess("操作已完成");
        _that.setSortView = false;
        setTimeout(() => {
          _that.getListForCombo();
        }, 10);
        _that.reset();
      });
    },
    reset() {
      this.form = {
        sortNumber: ''
      }
    }
  }

};
</script>

