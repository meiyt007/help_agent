/**
* @Author: liangss
* @Date: 2020-11-09 14:41:48
* @Last Modified by: liangss
* @Description: 识别模板信息配置
*/
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--咨询数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="108px">
          <el-form-item label="模板编号" prop="templateCode">
            <el-input v-model.trim="queryParams.templateCode" placeholder="请输入模板编号" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="模板名称" prop="templateName">
            <el-input v-model.trim="queryParams.templateName" placeholder="请输入模板名称" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置</el-button>
          </el-form-item>
        </el-form>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="default" icon="el-icon-plus" size="mini" @click="openmodelTemplateManage('',materialCatalogOid)" >新增</el-button>
          </el-col>
        </el-row>

        <div class="zf-zc-table--title">[ 所属材料目录：{{ materialCatalog.catalogCode }}-{{materialCatalog.catalogName}}]</div>
        <el-table v-loading="loading" :data="materialCategoryList" border>
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>

          <el-table-column label="模板编号" width="200" align="center" prop="templateCode"/>
          <el-table-column label="模板名称" :show-overflow-tooltip="true" align="center" prop="templateName"/>
          <el-table-column label="版本号" width="100" align="center" prop="version"/>
          <el-table-column label="状态" width="100" type="index" align="center">
            <template slot-scope="{row: {templateStatus}}">
              <span v-if="+templateStatus === 1">启用</span>
              <span v-else-if="+templateStatus === 0">禁用</span>
              <span v-else-if="+templateStatus === 2">暂存</span>
            </template>
          </el-table-column>
         <!-- <el-table-column label="状态" width="250" align="center" prop="templateStatus"/>-->
          <el-table-column label="修改时间" width="230" align="center" prop="modifyDate"/>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button v-show="scope.row.templateStatus==0 || scope.row.templateStatus==1"  size="mini" type="text" icon="iconfont zfsoft-chakan"@click="openmodelTemplateManageView(scope.row.faModelTemplateOid,scope.row.materialCatalogOid,'view')"  >查看</el-button>
              <el-button v-show="scope.row.templateStatus==0"   size="mini" type="text" icon="iconfont zfsoft-qiyong"  @click="ableFaModelTemplate(scope.row.id,scope.row.materialCatalogOid,'Y')" >启用</el-button>
              <el-button v-show="scope.row.templateStatus==1"   size="mini" type="text" icon="iconfont zfsoft-jinyong"  @click="ableFaModelTemplate(scope.row.id,scope.row.materialCatalogOid,'N')" >禁用</el-button>

              <el-button v-show="scope.row.templateStatus==2"   size="mini" type="text" icon="iconfont zfsoft-xiugai"  @click="openmodelTemplateManage(scope.row.faModelTemplateOid,scope.row.materialCatalogOid)" >修改</el-button>
              <el-button v-show="scope.row.templateStatus==2"   size="mini" type="text" icon="iconfont zfsoft-shanchu"  @click="deleteFaModelTemplate(scope.row.id,scope.row.materialCatalogOid)" >删除</el-button>
              <!--   <el-button  size="mini" type="text" icon="iconfont zfsoft-shanchu" @click="handleDelete(scope.row)" v-hasPermi="['sys:district:delete']">删除</el-button>-->
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>
      </el-col>
    </el-row>
    <!-- 添加或修改材料类别 -->
 <!-- <el-dialog v-dialog-drag :visible.sync="item.show" v-for="item in modelTemplateManageOptions" @close="outerVisible"
               :title="item.title" width="80%" append-to-body>
      <el-scrollbar style="height:800px;">
      <fa-model-template-manage :faModelTemplateOid="item.faModelTemplateOid" :materialCatalogOid="item.materialCatalogOid"
                                     :title="item.title"  @father-click="closeChildDetail"   >
        </fa-model-template-manage>
      </el-scrollbar>
    </el-dialog>-->

   <!-- <el-dialog v-dialog-drag title="文件配置" :visible.sync="openInit"
               @close="closeFileView" width="60%"  append-to-body>
      <iframe :src=url  rameborder="0" width="100%" height="600px"></iframe>
    </el-dialog>-->

    <el-dialog
      v-dialog-drag
      title="模板配置"
      :visible.sync="dialogVisible"
      width="80%"
      height="100%"
      @close="handleClose"
      append-to-body
    >
      <iframe :src="editUrl" frameborder="0" width="100%" height="600px"></iframe>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleClose">关闭</el-button>
      </span>
    </el-dialog>

    <el-dialog
      v-dialog-drag
      title="模板查看"
      :visible.sync="dialogVisibleView"
      width="80%"
      height="100%"
      append-to-body
    >
      <iframe :src="editUrl" frameborder="0" width="100%" height="600px"></iframe>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleCloseView">关闭</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import {page,delTemplate,able} from "@/api/zc/clzs/modelManagement/faModelTemplateList.js";
import Treeselect from '@riophae/vue-treeselect';
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import faModelTemplateManage from '@/views/zc/clzs/modelManagement/faModelTemplateManage';
export default {
  name: "advisoryRegistration",
  props: ['materialCatalogOid','materialParentOid','cataName'],
  components: {Treeselect,faModelTemplateManage},
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      modelTemplateManageOptions:[],
      // 应用表格数据
      materialCategoryList: [],
      materialCatalog:{},
      // 弹出层标题
      title: "",
      url:process.env.BASE_URL+'bc/index.html',
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      dialogVisible:false,
      dialogVisibleView:false,
      editUrl:'',
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        categoryName: null,
        categoryCode:null,
        materialCatalogOid:this.materialCatalogOid,
      },
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单参数
      form: {
        id: '', //逻辑主键
        materialCategoryOid:'',
        categoryName: '', // 类别名称
        categoryCode: '', // 类别编码
        note: '', // 备注
        sort: 0,//排序
      },
      // 表单校验
      rules: {
        categoryName: [
          { required: true, message: "类别名称为空", trigger: "blur" }
        ],
        categoryCode: [
          { required: true, message: "类别编码为空", trigger: "blur" }
        ],
        sort: [
          { required: true, message: "排序为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询材料类别列表 */
    getList() {
  /*    alert(this.materialCatalogOid)*/
     /* alert(this.queryParams)*/
      this.loading = true;
      page(this.queryParams).then(response => {
        this.materialCategoryList = response.data.pageResult.data;
        this.total = response.data.pageResult.total;
        this.materialCatalog=response.data.materialCatalog;
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
    //查看模板编制页面
    openmodelTemplateManageView(faModelTemplateOid,materialCatalogOid,viewflag){
    /*  let viewOrEdit='view';
      let url=process.env.BASE_URL+'bc/index.html?faModelTemplateOid='+faModelTemplateOid+'&materialCatalogOid='+materialCatalogOid+'&viewOrEdit='+viewOrEdit;
      window.open(url);*/
      /*alert(1)
      let pages=window.open(url);*/
/*      var loop = setInterval(function() {
        alert(window.open(url))
      },  2000);*/
      let viewOrEdit='view';
      let url=process.env.BASE_URL+'bc/index.html?faModelTemplateOid='+faModelTemplateOid+'&materialCatalogOid='+materialCatalogOid+'&viewOrEdit='+viewOrEdit;
      this.editUrl=url;
      this.dialogVisibleView=true;
      this.loading=false;
    },
    //编辑模板页面
    openmodelTemplateManage(faModelTemplateOid,materialCatalogOid){
      let viewOrEdit='edit';
      let url=process.env.BASE_URL+'bc/index.html?faModelTemplateOid='+faModelTemplateOid+'&materialCatalogOid='+materialCatalogOid+'&viewOrEdit='+viewOrEdit;
      this.editUrl=url;
      this.dialogVisible=true;
      this.loading=false;
     /* let viewOrEdit='edit';
      let url=process.env.BASE_URL+'bc/index.html?faModelTemplateOid='+faModelTemplateOid+'&materialCatalogOid='+materialCatalogOid+'&viewOrEdit='+viewOrEdit;
      var winObj=window.open(url);*/
    /*  var loop = setInterval(function() {
          if(winObj.closed) {
            alert(333333333)
          }
            clearInterval(loop);
            parent.location.reload();

          },  1);*/

    },
    handleClose(){
      this.getList();
      this.dialogVisible=false;
    },
    handleCloseView(){
      this.dialogVisibleView=false;
    },
    ableFaModelTemplate(faModelTemplateOid,materialCatalogOid,ableFlag){
      if(ableFlag=='Y'){
        this.$confirm("是否确认启用?", "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(function() {
            return able(faModelTemplateOid,materialCatalogOid,ableFlag);
          })
          .then(() => {
            this.getList();
            this.msgSuccess("启用成功");
          })
          .catch(function() {});

      }else{
        this.$confirm("是否确认禁用?", "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(function() {
            return able(faModelTemplateOid,materialCatalogOid,ableFlag);
          })
          .then(() => {
            this.getList();
            this.msgSuccess("禁用成功");
          })
          .catch(function() {});
      }
     /* able*/
    },
    deleteFaModelTemplate(faModelTemplateOid,materialCatalogOid){
      this.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return delTemplate(faModelTemplateOid);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(function() {});
    }


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
