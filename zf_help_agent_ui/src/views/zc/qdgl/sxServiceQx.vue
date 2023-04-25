/**
* @Author: xldong
*/
<template>
  <div class="app-container">
    <!--事项数据-->
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="108px"
    >
      <el-form-item label="事项名称" prop="serviceName">
        <el-input
          v-model.trim="queryParams.serviceName"
          placeholder="请输入事项名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="实施编码" prop="implementCode">
        <el-input
          v-model.trim="queryParams.implementCode"
          placeholder="请输入实施编码"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
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
    <el-table
      v-loading="loading"
      :data="sxServiceList"
      border
      :fit="true"
      height="calc(100% - 110px)"
    >
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="所属机构"
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
        label="实施编码"
        align="center"
        prop="implementCode"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="事项类型"
        align="center"
        prop="serviceTypeName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="操作"
        align="center"
        width="620"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
            v-hasPermi="['sys:reg:view']"
          >查看</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-tubiao"
            @click="handleSituationInit(scope.row)"
            v-hasPermi="['service:qx:init']"
          >情形配置</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-tubiao"
            @click="handleOptionInit(scope.row)"
            v-hasPermi="['service:qxxx:init']"
          >选项配置</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleMaterialRelInit(scope.row)"
            v-hasPermi="['service:qxcl:init']"
          >选项材料配置</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleSituationFieldInit(scope.row)"
            v-hasPermi="['service:qxcl:init']"
          >选项字段配置</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="situationsFieldFill(scope.row)"
            v-hasPermi="['service:qxcl:init']"
          >选项字段值填充</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="commonProblem(scope.row)"
            v-hasPermi="['service:pro:init']"
          >选项常见问题配置
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleMaterialResult(scope.row)"
            v-hasPermi="['service:qxcl:view']"
          >精细化梳理结果</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-hudongguanli"
            @click="handleFillField(scope.row)"
            v-hasPermi="['service:qx:init']"
          >基础表单填充</el-button
          >
          <!-- 条件预检配置 -->
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-hudongguanli"
            @click="initConditionalRules(scope.row)"
            v-hasPermi="['service:rules:init']"
          >条件预检配置</el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-touzishenpi"
            @click="initMpConditionalRules(scope.row)"
            v-hasPermi="['service:rules:init']"
          >秒批规则配置</el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-hudongguanli"
            @click="recommendInit(scope.row)"
            v-hasPermi="['service:tj:init']"
          >关联推荐配置</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 信息详细 -->
    <el-dialog
      v-dialog-drag
      :visible.sync="infoView"
      v-if="infoView"
      title="事项信息"
      height="700px"
      scrollbar
      width="1100px"
      append-to-body
    >
      <view-sx-service-info
        :sxServiceOid="serviceOid"
        @view-service="viewServiceClose"
      ></view-sx-service-info>
      <div slot="footer" class="zf-text-center">
        <el-button @click="infoView = false"> 关闭 </el-button>
      </div>
    </el-dialog>

    <!-- 情形配置 -->
    <el-dialog
      v-dialog-drag
      :visible.sync="situationView"
      v-if="situationView"
      title="配置情形"
      height="700px"
      scrollbar
      width="1100px"
      append-to-body
    >
      <sx-situation-info
        :serviceOid="serviceOid"
        @config-service="configServiceClose"
      ></sx-situation-info>
      <div slot="footer" class="zf-text-center">
        <el-button @click="situationView = false"> 关闭 </el-button>
      </div>
    </el-dialog>

    <!-- 选项材料配置 -->
    <el-dialog
      v-dialog-drag
      :visible.sync="optionMaterialView"
      v-if="optionMaterialView"
      title="选项材料配置"
      height="720px"
      scrollbar
      width="900px"
      append-to-body
    >
      <option-material-rel
        :serviceOid="serviceOid"
        @config-material="configMaterialClose"
      ></option-material-rel>
      <div slot="footer" style="text-align: center">
        <el-button @click="optionMaterialView = false"> 关闭 </el-button>
      </div>
    </el-dialog>

    <!--情形选项字段配置-->
    <el-dialog
      v-dialog-drag
      :visible.sync="situationFieldView"
      v-if="situationFieldView"
      title="情形选项字段配置"
      height="720px"
      scrollbar
      width="900px"
      append-to-body
    >
      <situation-field
        :serviceOid="serviceOid"
        @config-material="situationFieldClose"
      ></situation-field>
      <div slot="footer" style="text-align: center">
        <el-button @click="situationFieldView = false"> 关闭 </el-button>
      </div>
    </el-dialog>

    <!--情形字段填充-->
    <el-dialog
      v-dialog-drag
      :visible.sync="situationFieldFillView"
      v-if="situationFieldFillView"
      title="选项字段值填充"
      height="700px"
      scrollbar
      width="900px"
      append-to-body
    >
      <situation-field-fill
        :serviceOid="serviceOid"
        @config-material="situationFieldFillClose"
      ></situation-field-fill>
      <div slot="footer" style="text-align: center">
        <el-button @click="situationFieldFillView = false"> 关闭 </el-button>
      </div>
    </el-dialog>

    <!--情形字段填充-->
    <el-dialog
      v-dialog-drag
      :visible.sync="commonProblemView"
      v-if="commonProblemView"
      title="选项常见问题配置"
      height="700px"
      scrollbar
      width="900px"
      append-to-body
    >
      <common-problem
        :serviceOid="serviceOid"
        @config-material="closeCommonProblem"
      ></common-problem>
      <div slot="footer" style="text-align: center">
        <el-button @click="commonProblemView = false"> 关闭 </el-button>
      </div>
    </el-dialog>

    <!-- 精细化梳理查看 -->
    <el-dialog
      v-dialog-drag
      :visible.sync="titleValMaterialView"
      v-if="titleValMaterialView"
      title="精细化梳理查看"
      height="700px"
      scrollbar
      width="1100px"
      append-to-body
    >
      <title-val-material-rel
        :serviceOid="serviceOid"
        @closeResult="closeResult"
      />
      <div slot="footer" style="text-align: center">
        <el-button @click="titleValMaterialView = false"> 关闭 </el-button>
      </div>
    </el-dialog>

    <!-- 情形选项配置 -->
    <el-dialog
      v-dialog-drag
      :visible.sync="optionView"
      v-if="optionView"
      title="选项配置"
      height="800px"
      scrollbar
      width="1100px"
      append-to-body
    >
      <sx-option-info
        :serviceOid="serviceOid"
        :serviceName="serviceNamePic"
        @config-option="configOptionClose"
      ></sx-option-info>
      <div slot="footer" class="zf-text-center">
        <el-button @click="optionView = false"> 关闭 </el-button>
      </div>
    </el-dialog>

    <!-- 推荐关联配置 -->
    <el-dialog
      v-dialog-drag
      :visible.sync="recommendView"
      v-if="recommendView"
      title="推荐关联配置"
      height="800px"
      scrollbar
      width="1100px"
      append-to-body
    >
      <recommend-Info
        :serviceOid="serviceOid"
        :serviceName="serviceNamePic"
        @config-option="recommendClose"
      ></recommend-Info>
      <div slot="footer" class="zf-text-center">
        <el-button @click="recommendView = false"> 关闭 </el-button>
      </div>
    </el-dialog>

    <!-- 预检条件配置 -->
    <el-dialog
      v-dialog-drag
      :visible.sync="openRulesInit"
      v-if="openRulesInit"
      title="预检条件配置"
      height="800px"
      scrollbar
      width="1100px"
      append-to-body
    >
      <initConditionalRules
        ref="initConditionalRules"
        :serviceOid="serviceOid"
        :ruleType="ruleType"
        @serviceRulesClose="serviceRulesClose"
      ></initConditionalRules>
      <div slot="footer" class="zf-text-center">
        <el-button
          type="primary"
          @click="
            () => {
              $refs.initConditionalRules.submitForm()
            }
          "
        >确 定</el-button
        >
        <el-button
          @click="
            () => {
              $refs.initConditionalRules.cancel()
            }
          "
        >取 消</el-button
        >
      </div>
    </el-dialog>


    <!-- 秒批规则配置 -->
    <el-dialog
      v-dialog-drag
      :visible.sync="openMpRulesInit"
      v-if="openMpRulesInit"
      title="秒批规则配置"
      height="800px"
      scrollbar
      width="1100px"
      append-to-body
    >
      <initConditionalRules
        ref="initMpConditionalRules"
        :serviceOid="serviceOid"
        :ruleType="ruleType"
        @serviceRulesClose="serviceRulesClose"
      ></initConditionalRules>
      <div slot="footer" class="zf-text-center">
        <el-button
          type="primary"
          @click="
            () => {
              $refs.initMpConditionalRules.submitForm()
            }
          "
        >确 定</el-button
        >
        <el-button
          @click="
            () => {
              $refs.initMpConditionalRules.cancel()
            }
          "
        >取 消</el-button
        >
      </div>
    </el-dialog>
    <el-dialog
      v-dialog-drag
      :visible.sync="openFillField"
      v-if="openFillField"
      :title="title"
      width="1100px"
      scrollbar
      height="800px"
      append-to-body
    >
      <form-basic-field-rel-config
        :serviceOid="serviceOid"
        :serviceName="serviceName"
        @service-option-close="fieldFillClose"
      >
      </form-basic-field-rel-config>
    </el-dialog>
  </div>
</template>

<script>
import { page } from "@/api/zc/qdgl/sxService.js";
import viewSxServiceInfo from "@/views/zc/businessSupervise/viewSxServiceInfo";
import sxSituationInfo from "@/views/zc/qdgl/sxSituationInfo.vue";
import OptionMaterialRel from "@/views/zc/qdgl/optionMaterialRel";
import SituationField from "@/views/zc/qdgl/situationField";
import TitleValMaterialRel from "@/views/zc/qdgl/titleValMaterialRel";
import sxOptionInfo from "@/views/zc/qdgl/sxOptionInfo.vue";
import FormBasicFieldRelConfig from '@/views/zc/sxService/formConfig/formBasicFieldRelConfig'
import recommendInfo from "@/views/zc/qdgl/recommendInfo.vue";
import initConditionalRules from "@/views/zc/qdgl/initConditionalRules.vue";
import SituationFieldFill from "@/views/zc/qdgl/situationFieldFill.vue";
import CommonProblem from "@/views/ha/serviceCommonProblem/haServiceCommonProblem.vue";

export default {
  name: "SxServiceQx",
  components: {initConditionalRules, FormBasicFieldRelConfig, OptionMaterialRel, viewSxServiceInfo, sxSituationInfo,
    TitleValMaterialRel, sxOptionInfo, SituationField, recommendInfo, SituationFieldFill,CommonProblem},
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      sxServiceList: [],

      // 弹出层标题
      title: "",
      // 查看显示弹出层
      infoView: false,
      openInit: false,
      situationView: false,
      optionMaterialView: false,
      titleValMaterialView: false,
      relatrionPicView: false,
      situationFieldView: false,
      situationFieldFillView: false,
      commonProblemView: false,
      serviceNamePic: "",
      optionView: false,
      recommendView:false,
      openRulesInit: false,
      openMpRulesInit: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        serviceName: "",
        implementCode: "",
        serviceStatus: "3"

      },
      // 表单参数
      form: {},

      //事项oid
      serviceOid: "",
      ruleType: "",
      createUser: "",
      organOid: "",
      openFillField:false,
      serviceName: "",
    };
  },
  created () {
    this.getList();
  },
  methods: {
    handleFillField(row){
      this.openFillField=true;
      this.serviceOid = row.serviceOid;
      this.serviceName = row.serviceName;
      this.title = "基础表单填充配置【" + this.serviceName + "】";
    },
    recommendInit(row){
      this.recommendView = true;
      this.serviceOid = row.serviceOid;
    },
    recommendClose() {
      this.recommendView = false;
    },
    fieldFillClose () {
      this.openFillField = false;
    },
    /** 查看按钮操作 */
    handleMa (row) {
      this.serviceOid = row.serviceOid;
      this.infoViewMa = true;
    },
    /** 查询列表 */
    getList () {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.sxServiceList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel () {
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
      this.handleQuery();
    },
    /** 查看按钮操作 */
    handleView (row) {
      this.serviceOid = row.serviceOid;
      this.infoView = true;
    },
    viewServiceClose () {
      this.infoView = false;
    },

    handleInit (row) {
      this.serviceOid = row.serviceOid;
      this.openInit = true;
    },
    //关闭新增页面
    initServiceClose () { this.openInit = false; this.getList() },
    handlePlace (row) {
      this.serviceOid = row.serviceOid;
      this.placeFlag = true;
    },

    handleSituationInit (row) {
      this.situationView = true;
      this.serviceOid = row.serviceOid;
    },
    configServiceClose () {
      this.situationView = false;
    },
    // 选项材料配置
    handleMaterialRelInit (row) {
      this.optionMaterialView = true;
      this.serviceOid = row.serviceOid;
    },
    configMaterialClose () {
      this.optionMaterialView = false;
    },
    // 情形选项字段配置
    handleSituationFieldInit(row) {
      this.situationFieldView = true;
      this.serviceOid = row.serviceOid;
    },
    situationFieldClose() {
      this.situationFieldView = false;
    },
    // 情形字段值填充
    situationsFieldFill(row) {
      this.situationFieldFillView = true;
      this.serviceOid = row.serviceOid;
    },
    situationFieldFillClose() {
      this.situationFieldFillView = false;
    },
    // 常见问题
    commonProblem(row) {
      this.commonProblemView = true;
      this.serviceOid = row.serviceOid;
    },
    closeCommonProblem(){
      this.commonProblemView = false;
    },
    // 精细化梳理结果
    handleMaterialResult (row) {
      this.titleValMaterialView = true;
      this.serviceOid = row.serviceOid;
    },
    closeResult () {
      this.titleValMaterialView = false;
    },

    //情形选项 add by shimh 2021-08-04
    handleOptionInit (row) {
      this.optionView = true;
      this.serviceOid = row.serviceOid;
      this.serviceNamePic = row.serviceName;
    },
    initConditionalRules(row){
      this.openRulesInit = true;
      this.serviceOid = row.serviceOid;
      this.ruleType = "1";
    },
    initMpConditionalRules(row){
      this.openMpRulesInit = true;
      this.serviceOid = row.serviceOid;
      this.ruleType = "2";
    },
    serviceRulesClose(){
      this.openRulesInit = false;
      this.openMpRulesInit = false;
    },
    configOptionClose () {
      this.optionView = false;
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
