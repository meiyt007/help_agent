/**
* 关联推荐配置页面
* @Author: gaohongyu
* @date: 2022-05-20
*/
<template>
  <div>
<!--    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="108px" @submit.native.prevent>
      <el-form-item label="选项标题" prop="name">
        <el-input v-model.trim="queryParams.name" placeholder="请输入选项标题" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item class="fr mr0">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置
        </el-button>
      </el-form-item>
    </el-form>-->

    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
<!--        <el-button type="default" icon="el-icon-plus" size="mini" @click="handleInit" v-hasPermi="['service:mag:init']">
          新增选项信息</el-button>-->
       <el-button type="default" icon="iconfont zfsoft-xiugai" size="mini" @click="handleRecommendService"
          v-hasPermi="['service:recommend:init']">新增关联事项</el-button>
<!--        <el-button size="mini" type="default" icon="iconfont zfsoft-chakan" @click="handlerelatrionPic"
          v-hasPermi="['service:qxcl:view']">选项关系图</el-button>-->
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="serviceList" border height="500px"  >
<!--      <el-table-column type="selection" width="50" align="center"   />-->
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="事项名称" align="center" prop="serviceName" :show-overflow-tooltip="true" />
      <el-table-column label="实施编码" align="center"   prop="implementCode" :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" width="250" class-name="small-padding fixed-width">
        <template slot-scope="scope">
<!--          <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai" @click="handleInit(scope.row)"
            v-hasPermi="['sys:reg:view']">修改</el-button>-->
          <el-button size="mini" type="text" icon="iconfont zfsoft-shanchu" @click="handleDel(scope.row)"
            v-hasPermi="['service:recommend:del']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />


    <!-- 推荐事项配置 -->
    <el-dialog v-dialog-drag :visible.sync="relConfigView" v-if="relConfigView" title="推荐关联事项" width="1100px"
      height="700px" scrollbar append-to-body>
      <recommend-list :serviceOid="serviceOid"></recommend-list>
      <div slot="footer" style="text-align: center">
        <el-button @click="recommendClose"> 关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {
  page,
  pageOption, pageRecommendList, saveOrUpdate
} from "@/api/zc/qdgl/sxService.js";
  import {
    delSxServiceOptionTitleByOid
  } from "@/api/zc/qdgl/sxOptionInfo.js";
  import sxOptionInfoEdit from "@/views/zc/qdgl/sxOptionInfoEdit";
  import ViewRelationPicture from "@/views/zc/qdgl/viewRelationPicture";
  import recommendList from "@/views/zc/qdgl/recommendList";
import {getSxServiceOne} from "@/api/onething/clzs/materialCheckKeyManage/comboMaterialCheckKeyManage";
  export default {
    name: "sxOptionInfo",
    components: {
      sxOptionInfoEdit,
      ViewRelationPicture,
      recommendList
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
        serviceList: [],
        checkedDataList:[],
        recommendedServiceOids:[],
        serviceVals:[],
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
          serviceOid: this.serviceOid
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
      // 推荐关联事项列表
      handleRecommendService () {
        this.relConfigView = true;
        this.serviceOid =  this.form.serviceOid;
      },
      recommendClose(){
        this.relConfigView = false;
        this.getList();
      },
      handleDel(row) {
        let $this = this;
        this.loading =true;
        let rowServiceOid = row.serviceOid;
        this.$confirm('是否删除配置?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          getSxServiceOne($this.form.serviceOid).then(response => {
            $this.serviceVals = response.data.sxService;
            let serviceValsStr = $this .serviceVals.recommendedServiceOids.split(",");
            serviceValsStr.pop(rowServiceOid);
            //this.recommendedServiceOids = this.multipleSelection.join(",");
            $this .serviceVals.recommendedServiceOids=serviceValsStr.join(",");
            saveOrUpdate($this .serviceVals).then(res => {
              $this.$message.success("保存成功!");
              $this.getList();
            });
          });
        })
      },
      /** 查询列表 */
      getList() {
        this.loading = true;
        pageRecommendList(this.queryParams).then(response => {
          this.serviceList = response.data;
          this.loading = false;
        });
      }
    }
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
