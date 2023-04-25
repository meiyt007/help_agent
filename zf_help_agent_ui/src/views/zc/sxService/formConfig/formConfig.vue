<template>
  <div class="form-config">
    <div class="form-config-header">
      <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleFormConfigAdd">
        新增
      </el-button>
    </div>

    <el-table :data="formConnectList" :loading="loading" border :stripe="false" height="480px">
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="分类名称" align="center" prop="fieldTypeName" :show-overflow-tooltip="true" />
      <el-table-column label="表单编码" align="center" prop="formCode" :show-overflow-tooltip="true" />
      <el-table-column label="启用状态" align="center" width="80" prop="isAble">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.isAble" :active-value="1" :inactive-value="0" @change="handleAble(scope.row)">
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" width="80" prop="status" :show-overflow-tooltip="true"
                       :formatter="statusFormat" />
      <el-table-column label="操作" align="center" width="500px" class-name="opt-btns">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="handleView(scope.row)">
            <img src="@/assets/image/formConfig/view.png" width="15px" height="14px" alt="" />
            查看
          </el-button>
          <el-button size="mini" type="text" @click="handleDelete(scope.row)">
            <img src="@/assets/image/formConfig/delete.png" width="14px" height="15px" alt="" />
            删除
          </el-button>
          <el-button size="mini" type="text" @click="handleTable(scope.row)">
            <img src="@/assets/image/formConfig/save.png" width="15px" height="14px" alt="" />
            存储设置
          </el-button>
          <el-button size="mini" type="text" @click="handleDesign(scope.row)">
            <img src="@/assets/image/formConfig/design.png" width="15px" height="14px" alt="" />
            设计表单
          </el-button>
          <el-button size="mini" type="text" @click="handleShow(scope.row)">
            <img src="@/assets/image/formConfig/preview.png" width="15px" height="11px" alt="" />
            预览表单
          </el-button>
          <el-button class="exportBtn" size="mini" type="text" icon="iconfont zfsoft-daochu"
                     @click="handleTemplate(scope.row)">输出模板</el-button>
          <!-- <el-button
            size="mini"
            type="text"
            @click="handleFormConfigReset(scope.row)"
          >
            <img
              src="@/assets/image/formConfig/reset.png"
              width="15px"
              height="16px"
              alt=""
            />
            重设表单
          </el-button> -->
        </template>
      </el-table-column>
    </el-table>

    <div class="form-config-footer">
      <el-button @click="$emit('close')"> 关闭 </el-button>
    </div>

    <el-dialog title="表单信息新增" append-to-body v-dialog-drag v-if="formConfigAddDialogVisible"
               :visible.sync="formConfigAddDialogVisible" bodyBackgroundColor="#E3E8EB" width="1100px" height="800px">
      <FormConfigAddComponent :serviceName="serviceName" :serviceOid="serviceOid" :columnTypeMap="columnTypeMap"
                              :columnDataTypeMap="columnDataTypeMap" :isResetFormConfig="isResetFormConfig"
                              @close="formConfigAddDialogVisible = false" @save-close="handleSaveClose" />
    </el-dialog>

    <!-- 查看 -->
    <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="openView" v-if="openView" width="900px"
               append-to-body>
      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
        <colgroup>
          <col width="20%" />
          <col width="25%" />
          <col width="20%" />
          <col width="35%" />
        </colgroup>
        <tr>
          <td><b>事项名称：</b></td>
          <td>{{ designForm.serviceName }}</td>
          <td><b>分类名称：</b></td>
          <td>
            {{ designForm.fieldTypeName }}
          </td>
        </tr>
        <tr>
          <td><b>授权key：</b></td>
          <td>
            {{ designForm.authorizeKey }}
          </td>
          <td><b>表单编码：</b></td>
          <td>
            {{ designForm.formCode }}
          </td>
        </tr>
        <!--        <tr>
          <td><b>表单链接：</b></td>
          <td colspan="3">
            {{ designForm.formCode }}
          </td>
        </tr>-->
      </table>

      <div slot="footer" align="center">
        <el-button @click="openView = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 预览表单 -->
    <el-dialog v-dialog-drag :visible.sync="designShow" v-if="designShow" title="预览表单" @close="closeFormView"
               width="1100px" height="700px" scrollbar append-to-body>
      <FormView :formCode="formCode" :designOid="designOid" :authorizeKey="authorizeKey" :disabled="true"
                :isShowDefaultVal="true" />
      <div slot="footer" align="center">
        <el-button @click="closeFormView">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 存储设置 -->
    <el-dialog v-dialog-drag :title="title" append-to-body v-if="openCreateTable" :visible.sync="openCreateTable"
               bodyBackgroundColor="#E3E8EB" width="1100px" height="800px">
      <FormConfigAddComponent isModify :serviceName="serviceName" :serviceOid="serviceOid" :formType="formType"
                              :modifyObject="modifyObject" :columnTypeMap="columnTypeMap"
                              :columnDataTypeMap="columnDataTypeMap" :formOid="formOid" @close="openCreateTable = false"
                              @save-close="handleSaveClose" />
    </el-dialog>
    <!-- 输出模板配置 -->
    <el-dialog :close-on-click-modal="false" v-dialog-drag :visible.sync="view.docxTemplateShow"
               v-for="view in docxTemplateOptions" title="输出模板配置" @close="closeView" width="90%" append-to-body>
      <DocxTemplate v-if="view.docxTemplateShow" :key="view.formObject.objectOid" :formObject="view.formObject">
      </DocxTemplate>
    </el-dialog>
  </div>
</template>

<script>
// api
import { list, init, save, able, del, queryFormInfoExistList } from "@/api/zc/sxService/serviceFormConfig/formDesign";
import { getFieldList } from "@/api/zc/sxService/serviceFormConfig/formFillField";
import { getTable, getDataSourceTypeByNameList, getObjectFieldSaveTypeList, getFormMainByFormMainOid } from "@/api/form/manager";
// components
import FormConfigAddComponent from './components/formConfigAddComponent.vue';
import DocxTemplate from '@/views/zc/sxService/formConfig/components/docxTemplate'

export default {
  name: 'FormConfig',
  props: {
    serviceOid: String,
    serviceName: String
  },
  components: { FormConfigAddComponent, DocxTemplate },
  data() {
    return {
      formConnectList: [],
      formConfigAddDialogVisible: false,
      designForm: {},
      title: '', // dialog title
      openView: false,
      formCode: "",
      formNames: "",
      designOid: "",
      authorizeKey: "",
      designShow: false,
      openCreateTable: false,
      modifyObject: {},
      formType: '',
      isResetFormConfig: false,
      formOid: '',
      loading: false,
      columnTypeMap: [],
      columnDataTypeMap: [],
      docxTemplateOptions: []
    }
  },
  mounted() {
    this.getList();

    this.getDataSourceTypeByNameList();

    this.getObjectFieldSaveTypeList();

    this.listenStorage = e => {
      const { key, newValue } = e;
      if (key === "UPDATE_DESIGN_OID" && newValue === "1") {
        this.getList();
        localStorage.removeItem("UPDATE_DESIGN_OID");
      }
    };

    window.addEventListener("storage", this.listenStorage);
  },

  beforeDestroy() {
    window.removeEventListener("storage", this.listenStorage);
  },

  methods: {
    getDataSourceTypeByNameList() {
      getDataSourceTypeByNameList({ datasourceTypeName: 'mysql' }).then(({ code, data }) => {
        if (code !== 200) return;
        this.columnTypeMap = data || [];
      })
    },

    getObjectFieldSaveTypeList() {
      getObjectFieldSaveTypeList().then(({ code, data }) => {
        if (code !== 200) return;
        this.columnDataTypeMap = data || [];
      })
    },

    handleSaveClose() {
      this.formConfigAddDialogVisible = false;
      this.openCreateTable = false;
      this.getList();
    },

    handleFormConfigAdd() {
      this.formConfigAddDialogVisible = true;
    },

    handleFormConfigReset(row) {
      if (!row.formOid) return;
      this.formOid = row.formOid;
      this.isResetFormConfig = true;
      this.formConfigAddDialogVisible = true;
    },

    /** 查询目录清单列表 */
    getList() {
      this.loading = true;
      list(this.serviceOid).then(response => {
        this.loading = false;
        this.formConnectList = response.data;
      }).catch(() => {
        this.loading = false;
      });
    },

    // 状态
    statusFormat(row) {
      return this.selectMapLabel(
        {
          "3": "草稿",
          "5": "发布"
        },
        row.status
      );
    },

    /** 启禁用按钮操作 */
    handleAble(row) {
      const oid = row.id;
      let ableMessage = row.isAble === 1 ? "启用" : "禁用";
      this.$confirm("你确定要" + ableMessage + "吗？", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        return able(oid);
      }).then(({ code, data }) => {
        if (code == 200) {
          !data ? this.msgSuccess(ableMessage + "成功") : this.$message.warning(data);
          if (data) {
            row.isAble = row.isAble === 0 ? 1 : 0;
          }
        }
      }).catch(() => {
        row.isAble = row.isAble === 0 ? 1 : 0;
      });
    },

    /** 查看按钮操作 */
    handleView(row) {
      init(row.formOid).then(({ code, data }) => {
        if (code !== 200) return this.$message.warning('信息查询失败');
        if (!data) return this.$message.warning('未查询到表单配置相关信息');
        this.designForm = data;
        this.openView = true;
        this.title = "表单信息查看";
      });
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      const oid = row.formOid;
      this.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        return del(oid);
      }).then(() => {
        this.msgSuccess("删除成功");
        this.getList();
      }).catch(() => { });
    },

    // 列表中存储设置修改
    async handleTable(row) {
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

      const { isSave, children } = data;

      this.formType = isSave == '1' ? (children?.length ?? 0 > 0 ? 'masterSlave' : 'single') : 'subForm';

      this.title = '表单存储设置';

      this.formOid = row.formOid;

      const relationMap = (row?.relationObjectBusinessId ?? "").split(',').map(item => item.split(':'));

      this.modifyObject = { ...data, relationMap, originId: row.id };

      this.$nextTick(() => {
        this.openCreateTable = true;
      })
    },

    /** 设计表单 */
    handleDesign(row) {
      const { authorizeKey, designOid, formCode, formOid, serviceOid } = row;
      this.authorizeKey = authorizeKey;
      this.designOid = designOid;
      this.formCode = formCode;
      // this.ifDesign = true;
      this.formOid = formOid;
      this.serviceOid = serviceOid;

      const { href } = this.$router.resolve({
        path: "/formDesign",
        query: {
          authorizeKey,
          designOid,
          formCode,
          formOid,
          serviceOid
        }
      });
      localStorage.removeItem("UPDATE_DESIGN_OID");
      window.open(href, "_blank");
    },

    /** 表单预览 */
    handleShow(row) {
      getFieldList(row.serviceOid, row.fieldTypeOid).then(response => {
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

    //导出模板配置
    handleTemplate(row) {
      getFormMainByFormMainOid(row.formMainOid).then(res => {
        if (res.code === 200 && res.data) {
          row = Object.assign(row, res.data)
          let params = {
            formObject: row,
            docxTemplateShow: true
          };
          this.docxTemplateOptions.push(params);
        }
      }).catch(err => {
        this.$message.error(err)
      })

    },
    closeView() {
      this.docxTemplateOptions.pop();
    },
    closeFormView() {
      this.designShow = false;
    },
  }
}
</script>

<style scoped lang="scss">
.form-config {
  background-color: #fff;
  height: 100%;
  padding: 0 20px;
  border-radius: 5px;

  ::v-deep .el-button.el-button--text.el-button--mini>span {
    display: flex;
    align-items: center;

    >img {
      margin-right: 4px;
      object-fit: contain;
    }
  }

  ::v-deep .opt-btns .cell {
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .form-config-header {
    padding: 20px 0;
  }

  .form-config-footer {
    width: 100%;
    text-align: center;
    margin-top: 20px;
  }
}

.exportBtn {
  display: flex;
  align-items: center;

  >>>span {
    margin-left: 4px;
  }
}
</style>
