/**
* @Author: liangss
* @Date: 2020-11-09 14:41:48
* @Last Modified by: liangss
* @Description: 识别模板配置
*/
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--咨询数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="70px">
          <el-form-item label="目录编码" prop="catalogCode">
            <el-input v-model.trim="queryParams.catalogCode" placeholder="请输入目录编码" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="目录子项名称" prop="catalogName" label-width="108px">
            <el-input v-model.trim="queryParams.catalogName" placeholder="请输入目录子项名称" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置</el-button>
          </el-form-item>
        </el-form>

   <!--     <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="default"
              icon="el-icon-plus"
              size="mini"
              @click="openTempletePic()"
            >新增</el-button>
          </el-col>
        </el-row>-->

        <el-table v-loading="loading" :data="materialCategoryList" border :fit="true">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>

          <el-table-column label="目录编码"  align="center" prop="bigCataCode" :show-overflow-tooltip="true"/>
          <el-table-column label="目录名称"  align="center" prop="bigCataName" :show-overflow-tooltip="true"/>
          <el-table-column label="目录子项名称"  align="center" prop="catalogName" :show-overflow-tooltip="true"/>
          <el-table-column label="状态"  align="center" prop="stateDesc"/>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button  size="mini" type="text"
                          icon="iconfont zfsoft-xiugai"
                          @click="handleChildView(scope.row.materialCatalogOid,scope.row.materialParentOid)" >修改</el-button>
           <!--   <el-button  size="mini" type="text" icon="iconfont zfsoft-shanchu" @click="handleDelete(scope.row)" v-hasPermi="['sys:district:delete']">删除</el-button>-->
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>
      </el-col>
    </el-row>


    <!-- 识别模板信息列表 start-->
    <el-dialog v-dialog-drag :visible.sync="item.show" v-for="item in templateOptions" @close="handleClose"
               :title="item.title" width="1100px" height="600px" scrollbar append-to-body>
       <fa-model-template-list :materialCatalogOid="item.materialCatalogOid"
                               :materialParentOid="item.materialParentOid"
                               :title="item.title"   @father-click="handleChildView" >

       </fa-model-template-list>
    </el-dialog>
    <!-- 识别模板信息列表 end-->

    <!-- 模板底图 start-->
    <el-dialog v-dialog-drag :visible.sync="item.show" v-for="item in templatePicOptions" @close="outerVisible"
               :title="item.title" width="1100px" height="600px" scrollbar append-to-body>
        <fa-model-template-pic :materialCatalogOid="item.materialCatalogOid"
                                :materialParentOid="item.materialParentOid"
                                :title="item.title"   @father-click="openTempletePic" >

        </fa-model-template-pic>
    </el-dialog>
    <!-- 模板底图 end-->

  </div>
</template>

<script>
import {page} from "@/api/zc/clzs/modelManagement/faModelTemplate.js";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import faModelTemplateList from '@/views/zc/clzs/modelManagement/faModelTemplateList';
import faModelTemplatePic from '@/views/zc/clzs/modelManagement/faModelTemplatePic';
export default {
  name: "FaModelTemplate",
  components: {faModelTemplateList,faModelTemplatePic},
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      materialCategoryList: [],
      //识别模板信息列表
      templateOptions:[],
      //底图
      templatePicOptions:[],
      // 弹出层标题
      title: "",
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        catalogName: null,
        catalogCode:null
      },
      defaultProps: {
        children: "children",
        label: "label"
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询材料目录列表 */
    getList() {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.materialCategoryList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.openInit = false;
      this.reset();
    },
    // 表单重置
    reset() {
      Object.assign(this.form, this.$options.data().form)
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },

    //识别模板信息列表页面
    handleChildView(materialCatalogOid,materialParentOid){
      let item = {materialCatalogOid:materialCatalogOid,show:true,materialParentOid:materialParentOid,title:'识别模板信息列表'};
      this.templateOptions.push(item);
    },
    openTempletePic(){
      let item = {show:true,title:'底图列表'};
      this.templatePicOptions.push(item);
    },
    handleClose(){
      this.queryParams.pageNum = 1;
      this.getList();
    },

  },
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
  text-align: right;
}
.dialog-table table tr td:nth-of-type(2n) {
  color: #606266;
  text-align: left;
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
</style>
