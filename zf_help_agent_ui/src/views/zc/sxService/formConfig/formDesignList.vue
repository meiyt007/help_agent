/** * @Author: xiayj * @date:2021-07-15 */
<template>
  <div>
    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleInit"
          >新增</el-button
        >
      </el-col>
    </el-row>

    <el-table :data="formConnectList" border="">
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <!--<el-table-column label="事项名称" align="center" prop="serviceName" :show-overflow-tooltip="true" />-->
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
        label="启用状态"
        align="center"
        width="100"
        prop="isAble"
      >
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
        prop="status"
        :show-overflow-tooltip="true"
        :formatter="statusFormat"
      />
      <el-table-column
        label="操作"
        align="center"
        width="360px"
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
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="handleDelete(scope.row)"
            >删除
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleTable(scope.row)"
            >存储设置
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-caidan-fangkuai"
            @click="handleDesign(scope.row)"
            >设计表单
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-yulan"
            @click="handleShow(scope.row)"
            >预览表单
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openView"
      v-if="openView"
      width="1100px"
      append-to-body
    >
      <!--<h3>事项标题</h3>-->
      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
        <colgroup>
          <col width="20%" />
          <col width="30%" />
          <col width="20%" />
          <col width="30%" />
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
        <tr>
          <td><b>表单链接：</b></td>
          <td colspan="3">
            {{ designForm.formCode }}
          </td>
        </tr>
      </table>

      <div slot="footer" class="dialog-footer">
        <el-button @click="close">关闭</el-button>
      </div>
    </el-dialog>
    <!-- 添加或修改应用信息对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      v-if="openInit"
      :visible.sync="openInit"
      width="1100px"
      append-to-body
    >
      <el-form
        ref="designForm"
        :model="designForm"
        :rules="rules"
        label-width="0px"
        class="demo-ruleForm"
      >
        <el-input v-show="false" v-model="designForm.formOid" />
        <el-input v-show="false" value="" v-model="designForm.authorizeKey" />
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><b>事项名称：</b></td>
            <td colspan="3">{{ serviceName }}</td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>分类名称：</b></td>
            <td>
              <el-form-item prop="fieldTypeOid">
                <el-select
                  v-model.trim="designForm.fieldTypeOid"
                  placeholder="所属分类"
                  size="small"
                  style="width: 240px"
                  clearable
                  @change="selectFieldType"
                >
                  <el-option
                    v-for="dept in fieldTypeList"
                    :key="dept.fieldTypeOid"
                    :label="dept.fieldTypeName"
                    :value="dept.fieldTypeOid"
                  />
                </el-select>
              </el-form-item>
            </td>
            <td>
              <el-popover
                placement="top-start"
                width="322"
                trigger="hover"
                :title="tip_title"
                :content="tip_content"
              >
                <el-button class="tip" slot="reference"></el-button>
              </el-popover>
              <i class="require">*</i><b>表单编码：</b>
            </td>
            <td>
              <el-form-item prop="formCode">
                <el-input
                  v-model.trim="designForm.formCode"
                  style="width: 150px"
                  :disabled="true"
                  maxlength="20"
                ></el-input>
                <el-button
                  type="primary"
                  size="small"
                  style="margin-left: 15px"
                  :disabled="isSaveTable"
                  @click="createTable"
                  >存储设置</el-button
                >
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <!-- <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div> -->
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
        :designOid="designOid"
        :formCode="formCode"
        :isPublish="true"
      />
    </el-dialog>

    <el-dialog
      v-dialog-drag
      :visible.sync="designShow"
      v-if="designShow"
      title="预览表单"
      @close="closeFormView"
      width="1100px"
      append-to-body
    >
      <FormView
        :formCode="formCode"
        :designOid="designOid"
        :authorizeKey="authorizeKey"
        :disabled="true"
        :isShowDefaultVal="true"
      ></FormView>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeFormView">关 闭</el-button>
      </div>
    </el-dialog>

    <!--存储设置-->
    <el-dialog
      v-dialog-drag
      :visible.sync="openCreateTable"
      v-if="openCreateTable"
      title="表单存储配置"
      width="1100px"
      scrollbar
      height="calc(100vh - 220px)"
      append-to-body
    >
      <object-index
        :objectName="objectName"
        type="0"
        :contentOid="allValOptions.contentOid"
        :fieldTypeOid="allValOptions.fieldTypeOid"
        :authorizeKey="allValOptions.authorizeKey"
        :formCode="allValOptions.formCode"
        :serviceOid="serviceOid"
        @design-close="designClose"
        @cancle-close="cancleClose"
      >
      </object-index>
    </el-dialog>
  </div>
</template>

<script>
import { getFieldList } from "@/api/zc/sxService/serviceFormConfig/formFillField";
import { getFieldTypeList } from "@/api/zc/sxService/serviceFormConfig/formFillLabel";
import {
  list,
  init,
  save,
  able,
  del,
  queryFormInfoExistList
} from "@/api/zc/sxService/serviceFormConfig/formDesign";
import objectIndex from "@/views/zc/form/obejctIndex";
import formViewDemo from "@/views/formViewDemo";
import { validateLegalStrNoNumber } from "@/utils/validate";
import { localeData } from "moment";
export default {
  components: {
    objectIndex,
    formViewDemo
  },
  props: ["serviceOid", "serviceName"],
  name: "formFillFieldList",
  data () {
    const validateFormCode = (rule, value, callback) => {
      if (this.designForm.formCode == null || this.designForm.formCode == "") {
        callback(new Error("表单编码不能为空"));
      } else {
        callback();
      }
    };
    return {
      // 应用表格数据
      formConnectList: [],
      fieldTypeList: [],
      fieldTypeObject: {},
      title: "",
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      openCreateTable: false,
      allValOptions: {},
      formCode: "",
      formNames: "",
      designOid: "",
      authorizeKey: "",
      designShow: false,
      isSaveTable: false,
      tip_title: "存储设置：",
      tip_content:
        "设置申报表单的保存数据源。存储设置完成之后会自动生成表单编码，和表单系统进行关联。",
      // 表单参数
      designForm: {},
      ifDesign: false,
      objectName: "",
      formOid: "",
      // 表单校验
      rules: {
        formCode: [
          {
            validator: validateFormCode,
            trigger: "blur"
          },
          {
            validator: validateLegalStrNoNumber,
            trigger: "blur"
          }
        ],
        fieldTypeOid: [
          {
            required: true,
            message: "分类不能为空",
            trigger: "blur"
          }
        ]
      }
    };
  },

  created () {
    this.getList();
    this.getFieldTypeList();

    this.listenStorage = e => {
      const { key, newValue } = e;
      if (key === "UPDATE_DESIGN_OID" && newValue === "1") {
        this.getList();
        localStorage.removeItem("UPDATE_DESIGN_OID");
      }
    };

    window.addEventListener("storage", this.listenStorage);
  },

  beforeDestroy () {
    window.removeEventListener("storage", this.listenStorage);
  },

  methods: {
    /** 查询目录清单列表 */
    getList () {
      list(this.serviceOid).then(response => {
        this.formConnectList = response.data;
      });
    },

    getFieldTypeList () {
      getFieldTypeList().then(response => {
        this.fieldTypeList = response.data;
      });
    },
    // 状态
    statusFormat (row) {
      return this.selectMapLabel(
        {
          "3": "草稿",
          "5": "发布"
        },
        row.status
      );
    },

    close () {
      this.openView = false;
    },
    // 取消按钮
    cancel () {
      this.openInit = false;
      this.reset();
    },
    // 表单重置
    reset () {
      this.designForm = {};
      this.resetForm("designForm");
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
      this.openView = true;
      this.title = "表单信息查看";
      init(row.formOid).then(response => {
        this.designForm = response.data;
      });
    },
    /** 新增和修改按钮操作 */
    handleInit () {
      this.designForm = {
        authorizeKey: process.env.VUE_APP_FORM_AUTHORIZE_KEY,
        serviceOid: this.serviceOid
      };
      this.openInit = true;
      this.title = "表单信息新增";
    },
    handleDesign (row) {
      const { authorizeKey, designOid, formCode, formOid } = row;
      this.authorizeKey = authorizeKey;
      this.designOid = designOid;
      this.formCode = formCode;
      // this.ifDesign = true;
      this.formOid = formOid;

      const { href } = this.$router.resolve({
        path: "/formDesign",
        query: {
          authorizeKey,
          designOid,
          formCode,
          formOid
        }
      });
      window.open(href, "_blank");
    },
    selectFieldType (value) {
      this.fieldTypeObject = this.fieldTypeList.find(item => {
        return item.fieldTypeOid === value;
      });
    },
    handleShow (row) {
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

    closeFormView () {
      this.designShow = false;
    },
    closeDesignView () {
      this.ifDesign = false;
    },
    /** 提交按钮 */
    submitForm () {
      this.designForm.status = 3;
      this.designForm.formName = this.fieldTypeObject.fieldTypeName;
      this.$refs["designForm"].validate(valid => {
        if (valid) {
          save(this.designForm).then(response => {
            if (response.code === 200) {
              this.msgSuccess("保存成功");
              this.openInit = false;
              this.getList();
            }
          });
        } else {
          return false;
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete (row) {
      const oid = row.formOid;
      this.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          return del(oid);
        })
        .then(() => {
          this.msgSuccess("删除成功");
          this.getList();
        })
        .catch(() => { });
    },
    // 设置存储
    createTable () {
      this.allValOptions = {};
      this.objectName = "";
      if (this.designForm.fieldTypeOid == undefined) {
        this.msgError("请先选择分类名称！");
        return false;
      }
      queryFormInfoExistList(
        this.serviceOid,
        this.designForm.fieldTypeOid
      ).then(resp => {
        if (resp.data != "") {
          this.$message.error("分类已存在设计表单");
        } else {
          this.openCreateTable = true;
          this.allValOptions.authorizeKey =
            process.env.VUE_APP_FORM_AUTHORIZE_KEY;
          this.allValOptions.contentOid = this.serviceOid;
          this.allValOptions.fieldTypeOid = this.designForm.fieldTypeOid;
          this.allValOptions.formCode = "";
        }
      });
    },
    /*选择数据源存储*/
    designClose (formData, objectName) {
      this.designForm.formCode = formData.formCode;
      this.designForm.designOid = formData.designOid;
      this.designForm.formName = objectName;
      this.openCreateTable = false;
      this.allValOptions = {};
      this.objectName = objectName;
      this.isSaveTable = true;
      this.submitForm();
    },
    cancleClose () {
      this.openCreateTable = false;
    },
    // 列表中存储设置修改
    handleTable (row) {
      this.openCreateTable = true;
      this.allValOptions.authorizeKey = process.env.VUE_APP_FORM_AUTHORIZE_KEY;
      this.allValOptions.contentOid = this.serviceOid;
      this.allValOptions.fieldTypeOid = row.fieldTypeOid;
      this.allValOptions.formCode = row.formCode;
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
        .then(() => {
          this.msgSuccess(ableMessage + "成功");
        })
        .catch(() => {
          row.isAble = row.isAble === 0 ? 1 : 0;
        });
    }
  }
};
</script>
<style lang="scss" scoped>
.tip {
  border: 0;
  padding: 0;
  width: 18px;
  height: 18px;
  margin-top: 0px;
  margin-left: 70px;
  float: left;
  background: url(../../../../assets/image/tipMsg.png);
  background-size: 100% 100%;
}

.tip:hover {
  background: url(../../../../assets/image/tipMsg_active.png);
  background-size: 100% 100%;
}
</style>
