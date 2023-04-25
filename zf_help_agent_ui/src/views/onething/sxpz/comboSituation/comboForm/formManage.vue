<template>
  <div>
    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleSync"
          >同步数据</el-button
        >
      </el-col>
      <!--      <el-col :span="1.5">-->
      <!--        <el-button-->
      <!--          type="primary"-->
      <!--          icon="el-icon-plus"-->
      <!--          size="mini"-->
      <!--          @click="handleSync"-->
      <!--        >查看历史表单</el-button>-->
      <!--      </el-col>-->
    </el-row>

    <el-table :data="formInfoList" border :stripe="false" height="500px">
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="分类名称" align="center" prop="thingName" :show-overflow-tooltip="true" />-->
      <el-table-column
        label="分类名称"
        align="center"
        prop="fieldTypeName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="表单编码"
        align="center"
        prop="formCode"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createDate"
        :show-overflow-tooltip="true"
      />
      <el-table-column label="启用状态" align="center" width="80" prop="isAble">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isAble"
            :active-value="1"
            :inactive-value="0"
            @change="handleAble(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column
        label="状态"
        align="center"
        width="80"
        prop="status"
        :show-overflow-tooltip="true"
        :formatter="statusFormat"
      />
      <!--      <el-table-column label="版本" align="center" prop="version" :show-overflow-tooltip="true" />-->
      <el-table-column
        label="操作"
        align="center"
        width="420px"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
            >查看</el-button
          >
          <el-button
            size="mini"
            :disabled="scope.row.formCode"
            type="text"
            icon="iconfont zfsoft-hebing"
            @click="handleMerge(scope.row)"
            >合并管理
          </el-button>
          <!-- <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleConnect(scope.row)"
            >存储设置
          </el-button> -->
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleStorageConfig(scope.row)"
            >存储设置
          </el-button>
          <el-button
            size="mini"
            :disabled="scope.row.status == 1"
            type="text"
            icon="iconfont zfsoft-caidan-fangkuai"
            @click="handleDesign(scope.row)"
            >设计表单</el-button
          >
          <el-button
            :disabled="scope.row.status == 1"
            size="mini"
            type="text"
            icon="iconfont zfsoft-yulan"
            @click="handleShow(scope.row)"
            >预览表单</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openView"
      width="1100px"
      append-to-body
    >
      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
        <colgroup>
          <col width="20%" />
          <col width="30%" />
          <col width="20%" />
          <col width="30%" />
        </colgroup>
        <tr>
          <td><b>分类名称：</b></td>
          <td>{{ form.thingName }}</td>
          <td><b>分类名称：</b></td>
          <td>
            {{ form.fieldTypeName }}
          </td>
        </tr>
        <tr>
          <td><b>授权key：</b></td>
          <td>
            {{ form.authorizeKey }}
          </td>
          <td><b>表单编码：</b></td>
          <td>
            {{ form.formCode }}
          </td>
        </tr>
        <tr>
          <td><b>版本：</b></td>
          <td>
            {{ form.version }}
          </td>
          <td><b>创建日期：</b></td>
          <td>
            {{ form.createDate }}
          </td>
        </tr>
      </table>
      <div slot="footer" align="center">
        <el-button @click="close">关闭</el-button>
      </div>
    </el-dialog>

    <!--合并管理开始-->
    <el-dialog
      v-dialog-drag
      :visible.sync="openMerge"
      v-if="openMerge"
      title="合并管理"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <form-merge
        :fieldTypeOid="fieldTypeOid"
        :thingOid="thingOid"
        :comboFormOid="comboFormOid"
        @service-option-close="openMerge = false"
      >
      </form-merge>
    </el-dialog>
    <!--合并管理结束-->

    <!--存储设置-->
    <el-dialog
      v-dialog-drag
      :visible.sync="openCreateTable"
      v-if="openCreateTable"
      title="表单存储配置"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <combo-object-index
        type="1"
        :contentOid="contentOid"
        :fieldTypeOid="fieldTypeOid"
        :authorizeKey="authorizeKey"
        :formCode="formCode"
        @design-close="designClose"
        @select-datasource="selectDatasource"
      >
      </combo-object-index>
    </el-dialog>

    <!--存储设置2.0-->
    <el-dialog
      v-dialog-drag
      :visible.sync="storageConfigVisible"
      v-if="storageConfigVisible"
      :title="isModify ? `表单存储配置-修改` : `表单存储配置-新增`"
      width="1100px"
      height="800px"
      bodyBackgroundColor="#E3E8EB"
      append-to-body
    >
      <FormConfigAddComponent
        type="1"
        :isModify="isModify"
        :comboDirectoryOid="comboDirectoryOid"
        :comboDirectoryName="comboDirectoryName"
        :modifyObject="modifyObject"
        :columnTypeMap="columnTypeMap"
        :formOid="formOid"
        :columnDataTypeMap="columnDataTypeMap"
        @close="storageConfigVisible = false"
        @save-close="handleSaveClose"
      />
    </el-dialog>

    <el-dialog
      v-dialog-drag
      :visible.sync="ifDesign"
      v-if="ifDesign"
      title="设计表单"
      @close="closeDesignView"
      width="98%"
      append-to-body
    >
      <FormDesign
        :authorizeKey="authorizeKey"
        :formCode="formCode"
        :isPublish="true"
      />
    </el-dialog>

    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      :visible.sync="designShow"
      v-if="designShow"
      title="预览表单"
      @close="closeFormView"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <FormView
        :formCode="formCode"
        :designOid="designOid"
        :authorizeKey="authorizeKey"
        :disabled="true"
        :isShowDefaultVal="true"
      ></FormView>
      <!--      <form-view :key="new Date().getTime()" :formCode="formCode" :formNames="formNames" :authorizeKey="authorizeKey"
        :disabled="true"></form-view>-->
      <div slot="footer" align="center">
        <el-button @click="closeFormView">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  list,
  get,
  able,
  syncComboFormInfo,
  connectFormCode,
  queryComboFieldList,
  submitFormInfo
} from "@/api/onething/comboForm/formManage.js";
import { getTable, getDataSourceTypeByNameList, getObjectFieldSaveTypeList } from "@/api/form/manager";
import formMerge from "@/views/onething/sxpz/comboSituation/comboForm/formMerge.vue";
import comboObjectIndex from "@/views/onething/sxpz/comboSituation/comboForm/comboObejctIndex.vue";
import FormConfigAddComponent from './components/formConfigAddComponent.vue';
export default {
  components: {
    formMerge,
    comboObjectIndex,
    FormConfigAddComponent
  },
  props: ["comboDirectoryOid", "comboDirectoryName"],
  name: "formManage",
  data () {
    return {
      // 应用表格数据
      status: 0,
      formInfoList: [],
      title: "",
      authorizeKey: "",
      formName: "",
      formCode: "",
      formNames: "",
      designShow: false,
      // 新增/修改显示弹出层
      openMerge: false,
      // 查看显示弹出层
      openView: false,
      allValOptions: {},
      openCreateTable: false,
      fieldTypeOid: "",
      thingOid: "",
      designOid: "",
      formOid: "",
      comboFormOid:"",
      contentOid: "",
      // 表单参数
      form: {},
      ifDesign: false,
      storageConfigVisible: false, // 存储配置弹框
      isModify: false, // 是否是存储配置修改
      modifyObject: {},
      columnTypeMap: [],
      columnDataTypeMap: [],
    };
  },
  created () {
    this.getList();
    this.getDataSourceTypeByNameList();
    this.getObjectFieldSaveTypeList();

    this.listenStorage = e => {
      const { key, newValue } = e;
      if (key === "UPDATE_COMBO_DESIGN_OID" && newValue === "1") {
        this.getList();
        localStorage.removeItem("UPDATE_COMBO_DESIGN_OID");
      }
    };

    window.addEventListener("storage", this.listenStorage);
  },
  beforeDestroy () {
    window.removeEventListener("storage", this.listenStorage);
  },

  methods: {
    getDataSourceTypeByNameList () {
      getDataSourceTypeByNameList({ datasourceTypeName: 'mysql' }).then(({ code, data }) => {
        if (code !== 200) return;
        this.columnTypeMap = data || [];
      })
    },

    getObjectFieldSaveTypeList () {
      getObjectFieldSaveTypeList().then(({ code, data }) => {
        if (code !== 200) return;
        this.columnDataTypeMap = data || [];
      })
    },

    /** 查询目录清单列表 */
    getList () {
      list(this.comboDirectoryOid).then(response => {
        this.formInfoList = response.data;
        if (this.formInfoList.length > 0) {
          this.status = this.formInfoList[0].status;
        }
      });
    },

    close () {
      this.openView = false;
    },

    /** 查看按钮操作 */
    handleView (row) {
      this.openView = true;
      this.title = "表单信息查看";
      get(row.formOid).then(response => {
        this.form = response.data;
      });
    },
    // 状态
    statusFormat (row) {
      return this.selectMapLabel(
        {
          "1": "未设计",
          "3": "草稿",
          "5": "发布"
        },
        row.status
      );
    },
    handleSync () {
      let oid = this.comboDirectoryOid;
      let content;
      if (this.status === 1) {
        content = "是否确认同步?";
      } else {
        content = "同步将清除当前数据配置，是否确认同步?";
      }
      this.$confirm(content, "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function () {
          return syncComboFormInfo(oid);
        })
        .then(() => {
          this.msgSuccess("同步成功");
          this.getList();
        })
        .catch(function (e) {
          console.log(e);
        });
    },

    handleMerge (row) {
      this.fieldTypeOid = row.fieldTypeOid;
      this.thingOid = row.thingOid;
      this.comboFormOid=row.formOid;
      this.openMerge = true;
    },

    handleConnect (row) {
      this.openCreateTable = true;
      this.authorizeKey = process.env.VUE_APP_FORM_AUTHORIZE_KEY;
      this.contentOid = this.comboDirectoryOid;
      this.fieldTypeOid = row.fieldTypeOid;
      this.formOid = row.formOid;
      this.formCode = row.formCode;
      this.formName = row.fieldTypeName;
    },

    handleStorageConfig (row) {
      const { fieldTypeOid, formOid, formCode, fieldTypeName, authorizeKey } = row;
      // 是否是修改
      this.isModify = !!authorizeKey;
 
      if (this.isModify) {
        this.handleTable(row);
      } else {
        this.modifyObject = {
          contentOid: this.comboDirectoryOid,
          fieldTypeOid: fieldTypeOid,
          formOid: formOid,
          formCode: formCode,
          formName: fieldTypeName,
          authorizeKey: authorizeKey || process.env.VUE_APP_FORM_AUTHORIZE_KEY
        };

        this.formOid = formOid;

        this.storageConfigVisible = true;
      }
    },

    // 列表中存储设置修改
    async handleTable (row) {
      if (!row.formCode) return;
      const loading = this.$loading({
        lock: true,
        text: '正在查询表单存储配置信息',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });

      const { code, data } = await getTable({ formCode: row.formCode });

      if (code !== 200) {
        loading.close();
        return;
      }

      loading.close();

      this.formOid = row.formOid;

      const relationMap = (row?.relationObjectBusinessId ?? "").split(',').map(item => item.split(':'));

      this.modifyObject = { ...data, relationMap, originId: row.id };

      this.$nextTick(() => {
        this.storageConfigVisible = true;
      })
    },

    handleSaveClose () {
      this.storageConfigVisible = false;
      this.getList();
    },

    handleSubmit (row) {
      this.$confirm("是否确认提交?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function () {
          return submitFormInfo(row.formOid);
        })
        .then(() => {
          this.msgSuccess("合并提交成功");
          this.getList();
        })
        .catch(function (e) {
          console.log(e);
        });
    },

    handleDesign (row) {
      const { authorizeKey, designOid, formCode, formOid } = row;
      this.authorizeKey = authorizeKey;
      this.designOid = designOid;
      this.formCode = formCode;
      //this.ifDesign = true;
      this.formOid = formOid;
      const { href } = this.$router.resolve({
        path: "/comboFormDesign",
        query: {
          authorizeKey,
          designOid,
          formCode,
          formOid
        }
      });
      localStorage.removeItem("UPDATE_COMBO_DESIGN_OID");
      window.open(href, "_blank");
    },

    handleShow (row) {
      queryComboFieldList(row.thingOid, row.fieldTypeOid).then(response => {
        if (response.code !== 200) {
          this.msgError(response.message);
          return false;
        }
        if (response.data.length === 0) {
          this.msgError("找不到该类别表单字段");
          return false;
        }
        let fieldList = response.data;
        let formNames = "";
        fieldList.forEach(field => {
          if (field.fieldCode) {
            if (formNames === "") {
              formNames += field.fieldCode;
            } else {
              formNames += ",";
              formNames += field.fieldCode;
            }
          }
        });
        this.designOid = row.designOid;
        this.formCode = row.formCode;
        this.authorizeKey = row.authorizeKey;
        this.formNames = formNames;
        this.designShow = true;
      });
    },

    closeFormView () {
      this.designShow = false;
    },

    designClose () {
      this.openCreateTable = false;
    },
    /*选择数据源存储*/
    selectDatasource (form) {
      if (this.formOid !== "") {
        let formOid = this.formOid;
        let authorizeKey = process.env.VUE_APP_FORM_AUTHORIZE_KEY;
        connectFormCode(
          formOid,
          authorizeKey,
          form.formCode,
          this.formName
        ).then(response => {
          if (response.code === 200) {
            this.getList();
          }
        });
        this.formOid = "";
      }
      this.openCreateTable = false;
    },
    closeDesignView () {
      this.ifDesign = false;
    },
    /** 启禁用按钮操作 */
    handleAble (row) {
      const oid = row.id;
      let ableMessage = row.isAble === 1 ? "启用" : "禁用";
      this.$confirm("你确定要" + ableMessage + "吗？", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          return able(oid);
        })
        .then(({ code, data }) => {
          if (code == 200) {
            !data ? this.msgSuccess(ableMessage + "成功") : this.$message.warning(data);
            if (data) {
              row.isAble = row.isAble === 0 ? 1 : 0;
            }
          }
        })
        .catch(() => {
          row.isAble = row.isAble === 0 ? 1 : 0;
        });
    }
  }
};
</script>
