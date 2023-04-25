/* * @Description:一件事智慧表单 * @Author: dxl **/
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--一件事目录列表数据-->
      <el-col :span="24" :xs="24">
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
        <el-table v-loading="loading" :data="comboDirectoryList" :fit="true">
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
            width="540"
            align="center"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-tubiao"
                @click="handleDisgin(scope.row)"
                v-hasPermi="['combo:form:disgin']"
                >表单配置
              </el-button>
              <!--              <el-button size="mini" type="text" icon="iconfont zfsoft-teshuchengxu" @click="handleSign(scope.row)"
                         v-hasPermi="['combo:form:sign']">表单会签
              </el-button>-->
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-zhengcefagui"
                @click="handleField(scope.row)"
                v-hasPermi="['combo:form:field']"
                >情形字段配置
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-shenchagongzuoxize"
                @click="handleFieldVal(scope.row)"
                v-hasPermi="['combo:form:fieldVal']"
                >情形字段值填充
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-chakan"
                @click="handleView(scope.row)"
                v-hasPermi="['combo:form:view']"
                >查看</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xiugai"
                @click="handleFillField(scope.row)"
                v-hasPermi="['form:config:add']"
              >
                字段值填充配置
              </el-button>
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
      </el-col>
    </el-row>

    <!--一件事主题查看开始-->
    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      :visible.sync="openView"
      v-if="openView"
      title="详细信息"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <combo-directory-view
        :comboDirectoryOid="comboDirectoryOidView"
        @combo-directory="comboDirectoryClose"
      >
      </combo-directory-view>
    </el-dialog>
    <!--一件事主题查看结束-->

    <!-- 表单设计 -->
    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openFormDisgin"
      v-if="openFormDisgin"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <form-manage
        :comboDirectoryOid="allValOptions.comboDirectoryOid"
        :comboDirectoryName="allValOptions.comboDirectoryName"
      >
      </form-manage>
      <div slot="footer" align="center">
        <el-button @click="openFormDisgin = false"> 关闭 </el-button>
      </div>
    </el-dialog>

    <!-- 表单会签 -->
    <!--    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="title" :visible.sync="openFormSign" v-if="openFormSign"
               width="80%" append-to-body>
      <div style="height:calc(100vh - 220px);">
        <form-sign-manage-index :comboDirectoryOid="allValOptions.comboDirectoryOid">
        </form-sign-manage-index>
      </div>
    </el-dialog>-->

    <!-- 字段配置 -->
    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openField"
      v-if="openField"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <situation-field-rel :comboDirectoryOid="allValOptions.comboDirectoryOid">
      </situation-field-rel>
    </el-dialog>

    <!-- 字段值填充 -->
    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openFieldVal"
      v-if="openFieldVal"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <situation-field-val-rel
        :comboDirectoryOid="allValOptions.comboDirectoryOid"
      >
      </situation-field-val-rel>
    </el-dialog>
    <!--字段值填充配置-->
    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      :visible.sync="openFillField"
      v-if="openFillField"
      :title="title"
      width="1100px"
      scrollbar
      height="800px"
      append-to-body
    >
      <comboform-field-rel-config :comboDirectoryOid="allValOptions.comboDirectoryOid" :comboDirectoryName="allValOptions.comboDirectoryName"></comboform-field-rel-config>
    </el-dialog>
  </div>
</template>

<script>
import { page } from "@/api/onething/comboSituation/comboSituationIndex.js";
import comboDirectoryView from "@/views/onething/sxpz/comboDirectory/comboDirectoryView.vue";
import formManage from "@/views/onething/sxpz/comboSituation/comboForm/formManage.vue";
import situationFieldRel from "@/views/onething/sxpz/comboSituation/situationField/situationFieldRelIndex.vue";
import situationFieldValRel from "@/views/onething/sxpz/comboSituation/situationFieldVal/situationFieldValRelIndex.vue";
import comboformFieldRelConfig from "@/views/onething/sxpz/comboSituation/comboForm/comboformFieldRelConfig.vue";
//import formSignManageIndex from "@/views/oneThing/comboSituation/formSignManage/formSignManageIndex";
export default {
  components: {
    comboDirectoryView,
    formManage,
    situationFieldRel,
    situationFieldValRel,
    comboformFieldRelConfig
  },
  name: "ComboFormIndex",
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
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        comboDirectoryName: "",
        comboDirectoryOid: "",
        status: 1
      },
      openFormDisgin: false,
      openFormSign: false,
      openField: false,
      openFieldVal: false,
      allValOptions: {},
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
      this.initComboFormOptions.pop();
    },
    //配置表单
    handleForm (row) {
      let oid = row.comboDirectoryOid;
      let item = {
        show: true,
        comboDirectoryOid: oid
      };
      this.initComboFormOptions.push(item);
      this.title = "表单配置【" + row.comboDirectoryName + "】";
    },
    //关闭配置情形页
    comboSituationClose () {
      this.comboSituationOptions.pop();
    },
    // 表单设计
    handleDisgin (row) {
      this.openFormDisgin = true;
      this.allValOptions.comboDirectoryOid = row.comboDirectoryOid;
      this.allValOptions.comboDirectoryName = row.comboDirectoryName;
      this.title = "表单配置【" + row.comboDirectoryName + "】";
    },
    // 表单会签
    handleSign (row) {
      this.openFormSign = true;
      this.allValOptions.comboDirectoryOid = row.comboDirectoryOid;
      this.title = "表单会签【" + row.comboDirectoryName + "】";
    },
    // 字段配置
    handleField (row) {
      this.openField = true;
      this.allValOptions.comboDirectoryOid = row.comboDirectoryOid;
      this.title = "字段配置【" + row.comboDirectoryName + "】";
    },
    // 字段值填充
    handleFieldVal (row) {
      this.openFieldVal = true;
      this.allValOptions.comboDirectoryOid = row.comboDirectoryOid;
      this.title = "字段值填充【" + row.comboDirectoryName + "】";
    },
    // 电子表单填充
    handleFillField(row){
      this.openFillField=true;
      this.allValOptions.comboDirectoryOid = row.comboDirectoryOid;
      this.allValOptions.comboDirectoryName = row.comboDirectoryName;
      this.title = "字段值填充配置【" + row.comboDirectoryName + "】";
    },
    fieldFillClose () {
      this.openFillField = false;
    },
  }
};
</script>
