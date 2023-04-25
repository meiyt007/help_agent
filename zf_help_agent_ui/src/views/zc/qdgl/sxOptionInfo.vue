/**
* 选项配置页面
* @Author: shimh
* @date: 2021-08-04
*/
<template>
  <div>
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="108px" @submit.native.prevent>
      <el-form-item label="选项标题" prop="name">
        <el-input v-model.trim="queryParams.name" placeholder="请输入选项标题" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item class="fr mr0">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置
        </el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button type="default" icon="el-icon-plus" size="mini" @click="handleInit" v-hasPermi="['service:mag:init']">
          新增选项信息</el-button>
        <el-button type="default" icon="iconfont zfsoft-xiugai" size="mini" @click="handleRelList"
          v-hasPermi="['service:OptionRel:init']">选项关系配置</el-button>
        <el-button size="mini" type="default" icon="iconfont zfsoft-chakan" @click="handlerelatrionPic"
          v-hasPermi="['service:qxcl:view']">选项关系图</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="optionTitleList" border height="500px">
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="选项标题" align="center" prop="name" :show-overflow-tooltip="true" />
      <el-table-column label="是否必填" align="center" show-overflow-tooltip>
        <template slot-scope="scope">
          {{ scope.row.fillFlag == 1 ? '是' : '否' }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="250" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai" @click="handleInit(scope.row)"
            v-hasPermi="['sys:reg:view']">修改</el-button>
          <el-button size="mini" type="text" icon="iconfont zfsoft-shanchu" @click="handleDel(scope.row)"
            v-hasPermi="['service:qx:init']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!--新增/修改选项弹出框开始-->
    <el-dialog v-dialog-drag :visible.sync="optionEditView" v-if="optionEditView" title="选项配置添加" width="1100px"
      height="700px" scrollbar append-to-body>
      <sx-option-info-edit ref="sxOptionInfo" :serviceOid="serviceOid" :titleOid="titleOid"
        @init-serv-option-close="serviceOptionClose"></sx-option-info-edit>
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="
            () => {
              $refs.sxOptionInfo.submitForm()
            }
          ">确 定</el-button>
        <el-button @click="serviceOptionClose">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 关系配置 -->
    <el-dialog v-dialog-drag :visible.sync="relConfigView" v-if="relConfigView" title="配置信息" width="1100px"
      height="700px" scrollbar append-to-body>
      <relation-list :serviceOid="serviceOid"></relation-list>
      <div slot="footer" style="text-align: center">
        <el-button @click="relConfigView = false"> 关闭 </el-button>
      </div>
    </el-dialog>

    <!-- 关系图查看 -->
    <el-dialog v-dialog-drag :visible.sync="relatrionPicView" v-if="relatrionPicView" title="关系图信息" width="900px"
      height="720px" scrollbar append-to-body>
      <view-relation-picture :serviceOid="serviceOid" :serviceName="serviceNamePic"></view-relation-picture>
    </el-dialog>
  </div>
</template>
<script>
  import {
    pageOption
  } from "@/api/zc/qdgl/sxService.js";
  import {
    delSxServiceOptionTitleByOid
  } from "@/api/zc/qdgl/sxOptionInfo.js";
  import sxOptionInfoEdit from "@/views/zc/qdgl/sxOptionInfoEdit";
  import ViewRelationPicture from "@/views/zc/qdgl/viewRelationPicture";
  import RelationList from "@/views/zc/qdgl/relationList";
  export default {
    name: "sxOptionInfo",
    components: {
      sxOptionInfoEdit,
      ViewRelationPicture,
      RelationList
    },
    props: ["serviceOid", "serviceName"],
    data() {
      return {
        form: {},
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 应用表格数据
        optionTitleList: [],

        // 弹出层标题
        title: "",
        // 查看显示弹出层
        infoView: false,
        openInit: false,
        optionView: false,
        titleOid: "",
        optionEditView: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          serviceOid: this.serviceOid,
          name: ""

        },
        relatrionPicView: false,
        serviceNamePic: "",
        relConfigView: false,
      };
    },

    //获取父页面的值
    mounted() {
      this.form.serviceOid = this.serviceOid;
      this.queryParams.serviceOid = this.serviceOid;
      this.getList();
    },
    methods: {
      handleRowChange(data) {},
      /** 新增和修改按钮操作 */
      handleInit(row) {
        this.titleOid = row.oid;
        this.optionEditView = true;
      },
      //关闭选项form弹出框
      serviceOptionClose() {
        // debugger;
        // this.optionTitleList.pop();
        this.optionEditView = false;
        this.getList();
      },

      //删除
      handleDel(row) {
        const oid = row.oid;
        this.$confirm('是否确认删除?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delSxServiceOptionTitleByOid(oid);
        }).then(() => {
          this.msgSuccess("删除成功");
          this.getList();
        }).catch(function () {});
      },

      /** 查看按钮操作 */
      handleMa(row) {
        this.serviceOid = row.serviceOid;
        this.infoViewMa = true;
      },
      /** 查询列表 */
      getList() {
        this.loading = true;
        pageOption(this.queryParams).then(response => {
          this.optionTitleList = response.data.data.data;
          this.total = response.data.data.total;
          this.loading = false;
        });
        this.loading = false;
      },
      // 取消按钮
      cancel() {
        this.addUpdateFlag = false;
        this.resetSxOption();
      },
      resetSxOption() {
        var chServiceOid = this.form.serviceOid;
        this.form = {};
        this.resetForm('form');
        this.form.serviceOid = chServiceOid;
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
      /** 查看按钮操作 */
      handleView(row) {
        this.serviceOid = row.serviceOid;
        this.infoView = true;
      },
      viewServiceClose() {
        this.infoView = false;
      },

      //关闭新增页面
      initServiceClose() {
        this.openInit = false;
        this.getList()
      },
      handlePlace(row) {
        this.serviceOid = row.serviceOid;
        this.placeFlag = true;
      },

      configOptionClose() {
        this.optionView = false;
      },
      handlerelatrionPic() {
        this.relatrionPicView = true;
        this.serviceOid = this.serviceOid;
        this.serviceNamePic = this.serviceName;
      },
      handleRelList() {
        this.relConfigView = true;
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
