/*
* @Description:一件事主题情形配置
* @Author: dxl
**/
<template>
  <div class="app-container">
    <!--一件事目录列表数据-->
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="70px"
      @submit.native.prevent
    >
      <el-form-item label="目录名称" prop="comboDirectoryName">
        <el-input
          v-model.trim="queryParams.comboDirectoryName"
          placeholder="请输入目录名称"
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
          >重置
        </el-button>
      </el-form-item>
    </el-form>
    <el-table
      v-loading="loading"
      :data="comboDirectoryList"
      height="calc(100% - 110px)"
    >
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="分类名称"
        align="center"
        prop="themeName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="目录名称"
        align="center"
        prop="comboDirectoryName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="所属区划"
        align="center"
        prop="districtName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="主办部门"
        align="center"
        prop="mainOrganName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="操作"
        width="500"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
            v-hasPermi="['combo:directory:view']"
            >查看</el-button
          >

          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-tubiao"
            @click="handleSituation(scope.row)"
            v-hasPermi="['combo:stu:save']"
            >情形添加</el-button
          >
          <!--              <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai" @click="handleForm(scope.row)">表单配置
              </el-button>-->
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-tubiao"
            @click="handleSituationRel(scope.row)"
            v-hasPermi="['combo:stu:relsave']"
            >关系配置</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-tubiao"
            @click="handleAtta(scope.row)"
            v-hasPermi="['combo:stu:materialsave']"
            >材料配置</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-tubiao"
            @click="handleResult(scope.row)"
            v-hasPermi="['combo:stu:resultsave']"
            >事项结果配置</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-tubiao"
            @click="handleLabel(scope.row)"
            v-hasPermi="['combo:stu:noticsave']"
            >告知单标签配置</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-tubiao"
            @click="handleStop(scope.row)"
            v-hasPermi="['combo:stu:zcsave']"
            >阻塞配置</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-hudongguanli"
            @click="handleFillField(scope.row)"
            v-hasPermi="['service:qx:init']"
          >基础表单填充</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!--一件事主题查看开始-->
    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      :visible.sync="openView"
      v-if="openView"
      title="主题详细信息"
      width="1100px"
      scrollbar
      height="800px"
      append-to-body
    >
      <combo-directory-view
        :comboDirectoryOid="comboDirectoryOidView"
        @combo-directory="comboDirectoryClose"
      >
      </combo-directory-view>
    </el-dialog>
    <!--一件事主题查看结束-->

    <!--一件事表单配置开始-->
    <!--    <el-dialog v-dialog-drag :close-on-click-modal="false" :visible.sync="initComboFormshow"
      v-if="initComboFormshow" title="表单配置" scrollbar width="1100px" height="800px" append-to-body>
        <combo-form :comboDirectoryOid="allValOptions.comboDirectoryOid" @combo-form="comboFormClose">
        </combo-form>
    </el-dialog>-->
    <!--一件事表单配置结束-->

    <!--情形配置-->
    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      :visible.sync="openSituation"
      v-if="openSituation"
      title="情形配置"
      width="1100px"
      height="800px"
      append-to-body
      scrollbar
    >
      <situation-index
        :comboDirectoryOid="allValOptions.comboDirectoryOid"
        @combo-situation-close="comboSituationClose"
      >
      </situation-index>
    </el-dialog>

    <!--情形关系配置-->
    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      :visible.sync="openOptionRel"
      v-if="openOptionRel"
      title="情形关系配置"
      width="1100px"
      height="800px"
      scrollbar
      append-to-body
    >
      <situation-option-rel-index
        :comboDirectoryOid="allValOptions.comboDirectoryOid"
        @combo-situation-close="comboSituationClose"
      >
      </situation-option-rel-index>
    </el-dialog>

    <!-- 材料配置 -->
    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      title="材料配置"
      :visible.sync="openAttaRel"
      v-if="openAttaRel"
      width="1100px"
      height="800px"
      scrollbar
      append-to-body
    >
      <situation-atta-rel
        :comboDirectoryOid="allValOptions.comboDirectoryOid"
        @combo-situation-close="comboSituationClose"
      >
      </situation-atta-rel>
    </el-dialog>

    <!-- 事项结果配置 -->
    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      title="事项结果配置"
      :visible.sync="openResultRel"
      v-if="openResultRel"
      width="1100px"
      height="800px"
      append-to-body
      scrollbar
    >
      <situation-result-rel
        :comboDirectoryOid="allValOptions.comboDirectoryOid"
        @combo-situation-close="comboSituationClose"
      >
      </situation-result-rel>
    </el-dialog>

    <!-- 告知单标签配置开始 -->
    <el-dialog
      v-dialog-drag
      title="告知单标签配置"
      :close-on-click-modal="false"
      :visible.sync="labelCgShow"
      v-if="labelCgShow"
      width="1100px"
      height="800px"
      scrollbar
      append-to-body
    >
      <combo-opt-inform-label-rel-config
        :comboDirectoryOid="allValOptions.comboDirectoryOid"
        @close-inform-label-rel-config="closeInformLabelRelConfig"
      >
      </combo-opt-inform-label-rel-config>
    </el-dialog>

    <!-- 阻塞配置 -->
    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      title="情形阻塞配置"
      :visible.sync="openStopRel"
      v-if="openStopRel"
      width="1100px"
      height="800px"
      append-to-body
    >
      <situation-stop-rel
        :comboDirectoryOid="allValOptions.comboDirectoryOid"
        @combo-situation-close="comboSituationClose"
      >
      </situation-stop-rel>
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
      <comboform-basic-field-rel-config
        :comboDirectoryOid="allValOptions.comboDirectoryOid"
        :serviceName="allValOptions.serviceName"
        @service-option-close="fieldFillClose"
      >
      </comboform-basic-field-rel-config>
    </el-dialog>
  </div>
</template>

<script>
import {
  page
} from "@/api/onething/comboSituation/comboSituationIndex.js";
import comboDirectoryView from "@/views/onething/sxpz/comboDirectory/comboDirectoryView";
import situationIndex from "@/views/onething/sxpz/comboSituation/situation/situationIndex.vue";
import situationOptionRelIndex from "@/views/onething/sxpz/comboSituation/situationRel/situationOptionRelIndex.vue";
import situationStopRel from "@/views/onething/sxpz/comboSituation/situationWarning/situationWarningRelIndex.vue";
import situationAttaRel from "@/views/onething/sxpz/comboSituation/situationAtta/situationAttaRelIndex.vue";
import situationResultRel from "@/views/onething/sxpz/comboSituation/situationResult/situationResultRelIndex.vue";
import comboOptInformLabelRelConfig from "@/views/onething/sxpz/comboSituation/informLable/comboOptInformLabelRelConfig.vue";
import comboformBasicFieldRelConfig from "@/views/onething/sxpz/comboSituation/comboForm/comboformBasicFieldRelConfig.vue";
export default {
  components: {
    comboDirectoryView, situationIndex, situationOptionRelIndex, situationStopRel, situationAttaRel, situationResultRel, comboOptInformLabelRelConfig ,comboformBasicFieldRelConfig
  },
  name: "ComboSituationIndex",
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      attatotal: 0,
      // 应用表格数据
      comboDirectoryList: [],
      // 弹出层标题
      title: "",
      // 查看显示弹出层
      openView: false,
      //表单配置
      initComboFormOptions: [],
      initFormConfig: [],
      //配置选项弹出层
      initComboTitleOptions: [],
      //选项材料与证照弹出层
      comboMaterResultOptions: [],
      //情形配置弹出层
      comboSituationOptions: [],
      comboDirectoryOid:"",
      serviceName:"",
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        comboDirectoryName: '',
        comboDirectoryOid: '',
        status: 1
      },
      openSituation: false,
      openOptionRel: false,
      openAttaRel: false,
      openResultRel: false,
      openStopRel: false,
      allValOptions: {},
      comboLabelOptions: [],
      comboDirectoryOidView: "",
      labelCgShow: false,
      initComboFormshow: false,
      openFillField:false,

    };
  },
  created () {
    this.getList();
  },
  methods: {

    /** 查询一件事目录列表 */
    getList () {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.comboDirectoryList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
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
      this.comboDirectoryOidView = row.comboDirectoryOid;
      this.openView = true;
      this.title = "查看一件事主题信息";
    },
    //关闭查看一件事主题
    comboDirectoryClose () {
      this.openView = false;
    },
    //关闭表单配置
    comboFormClose () {
      this.initComboFormshow = false;
    },
    //配置表单
    handleForm (row) {
      this.initComboFormshow = true;
      this.allValOptions.comboDirectoryOid = row.comboDirectoryOid;
    },

    /** 配置选项按钮操作 */
    handleOption (row) {
      let oid = row.comboDirectoryOid;
      let item = {
        show: true,
        comboDirectoryOid: oid
      };
      this.initComboTitleOptions.push(item);
    },
    //关闭配置选项页
    comboOptionClose () {
      this.initComboTitleOptions.pop();
    },

    /** 配置情形按钮操作 */
    handleSituationList (row) {
      let item = {
        show: true,
        comboDirectoryOid: row.comboDirectoryOid
      };
      this.comboSituationOptions.push(item)
    },
    //关闭配置情形页
    comboSituationClose () {
      this.comboSituationOptions.pop();
    },
    //一件事目录材料整合
    queryMaterialList (row) {
      let item = {
        show: true,
        comboDirectoryOid: row.comboDirectoryOid
      };
      this.comboMaterResultOptions.push(item)
    },
    // 关闭按钮
    comboMaterResultClose () {
      this.comboMaterResultOptions.pop();
    },
    //关闭告知单标签配置情形页
    closeInformLabelRelConfig () {
      this.labelCgShow = false;
    },
    /** 情形配置 */
    handleSituation (row) {
      this.openSituation = true;
      this.allValOptions.comboDirectoryOid = row.comboDirectoryOid;
    },
    /** 情形关系配置 */
    handleSituationRel (row) {
      this.openOptionRel = true;
      this.allValOptions.comboDirectoryOid = row.comboDirectoryOid;
    },
    /* 材料配置按钮操作 */
    handleAtta (row) {
      this.openAttaRel = true;
      this.allValOptions.comboDirectoryOid = row.comboDirectoryOid;
    },
    /* 事项结果配置 */
    handleResult (row) {
      this.openResultRel = true;
      this.allValOptions.comboDirectoryOid = row.comboDirectoryOid;
    },
    /* 告知单标签配置按钮操作 */
    handleLabel (row) {
      this.labelCgShow = true;
      this.allValOptions.comboDirectoryOid = row.comboDirectoryOid;
    },
    /* 阻塞配置 */
    handleStop (row) {
      this.openStopRel = true;
      this.allValOptions.comboDirectoryOid = row.comboDirectoryOid;
    },
    handleFillField(row){
      this.openFillField=true;
      this.allValOptions.comboDirectoryOid = row.comboDirectoryOid;
      this.allValOptions.serviceName=row.comboDirectoryName;
      this.title = "基础表单填充配置";
    },
    fieldFillClose () {
      this.openFillField = false;
    },
  }
};

</script>
