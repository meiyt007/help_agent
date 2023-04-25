/**
* @Author: liangss
* @Date: 2020-11-13
* @Description: 模板制作
*/
<template>
  <div class="app-container">




  </div>
</template>

<script>
import Treeselect from '@riophae/vue-treeselect';
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
/*import  UTIL from "@/js/fa/util";
import  BLOCK from "@js/fa/block.js";
import  CORE  from "@js/fa/core.js";
import  myztree from "@js/fa/myztree.js";
import  business from "@js/fa/business.js";
import  context from "@js/fa/contextjs/js/context.js";
import  ruler1 from "@js/fa/jqueryui-ruler/js/jquery-ui.min.js";
import  ruler2 from "@js/js/fa/jqueryui-ruler/js/jquery.ui.ruler.js";*/


export default {

  props: ['faModelTemplateOid','materialCatalogOid'],

  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      materialCategoryList: [],
      materialCatalog:{},
      // 弹出层标题
      title: "",
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

    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询材料类别列表 */
    getList() {
      this.loading = false;
     /* page(this.queryParams).then(response => {
        this.materialCategoryList = response.data.pageResult.data;
        this.total = response.data.pageResult.total;
        this.materialCatalog=response.data.materialCatalog;
        this.loading = false;
      });*/
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
