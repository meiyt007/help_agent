<template>
  <el-scrollbar class="form-config-add-main">
    <div class="form-config-add-main-container">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="dataForm"
        label-width="120px"
      >
        <el-form-item label="存储对象名称：" prop="objectName">
          <el-input
            v-model.trim="dataForm.objectName"
            placeholder="请输入存储对象名称"
            maxlength="50"
            show-word-limit
            style="width: 50%"
          ></el-input>
        </el-form-item>
        <el-form-item label="所属模块：" prop="moduleOid">
          <el-select
            v-model="dataForm.moduleOid"
            @change="handleModuleOidChange"
            filterable
            clearable
            placeholder="请选择"
            style="width: 50%"
          >
            <el-option
              v-for="item in moduleOptions"
              :key="item.moduleOid"
              :label="item.moduleName"
              :value="item.moduleOid"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="数据源：" prop="datasourceOid">
          <el-select
            placeholder="请选择"
            style="width: 50%"
            v-model="dataForm.datasourceOid"
            filterable
            clearable
            @change="handleDataSourceChange"
          >
            <el-option
              v-for="item in dataSourceOptions"
              :key="item.datasourceOid"
              :label="item.connectionName"
              :value="item.datasourceOid"
            />
          </el-select>
          <el-button
            type="primary"
            size="small"
            style="margin-left: 10px"
            @click="handleEditDatasource"
          >
            编辑
          </el-button>
        </el-form-item>
        <el-form-item
          label="数据库表名："
          prop="objectForm"
          v-if="!!dataForm.datasourceOid && !isLogicFormMain"
        >
          <el-input
            style="width: 50%"
            v-model.trim="dataForm.objectForm"
            placeholder="请输入数据库表名"
            maxlength="50"
            show-word-limit
          >
          </el-input>
        </el-form-item>
      </el-form>

      <FormConfigAddRelation
        ref="formConfigRelation"
        v-if="isFormMain === '0' && !isLogicFormMain && !isSingle"
        :columnKeys="columnKeys"
        :formObjectExtand="formObjectExtand"
      />

      <Column
        v-if="params.contentOid && !isLogicFormMain"
        ref="columnChild"
        :params="params"
        :type="'0'"
        :columnTypeMap="columnTypeMap"
        :columnDataTypeMap="columnDataTypeMap"
        :modifyColumnList="columnList"
        @setColumnkeys="setColumnkeys"
      />
    </div>

    <el-dialog
      v-dialog-drag
      :visible.sync="visible"
      title="编辑数据源"
      @close="closeView"
      width="1100px"
      height="800px"
      append-to-body
      scrollbar
    >
      <DatasourceIndex
        :authorizeKey="authorizeKey"
        :moduleOid="dataForm.moduleOid"
      />
      <div slot="footer" align="center">
        <el-button @click="closeView">关 闭</el-button>
      </div>
    </el-dialog>
  </el-scrollbar>
</template>

<script>
import { dataSourcelist, modulelist, } from "@/api/form/manager";
import DatasourceIndex from "@/views/zc/form/datasourceIndex";
import Column from "./columnIndex";
import FormConfigAddRelation from './formConfigAddRelation.vue';

export default {
  name: 'FormConfigAddMain',
  props: {
    isSingle: Boolean,
    isFormMain: String,
    isLogicFormMain: Boolean,
    contentOid: String,
    fieldTypeOid: String,
    authorizeKey: String,
    formCode: String,
    serviceOid: String,
    fieldTypeName: String,
    columnList: {
      type: Array,
      default: () => []
    },
    formObjectExtand: {
      type: Object,
      default: () => ({})
    },
    isModify: Boolean,
    formData: Object,
    columnTypeMap: {
      type: Array,
      default: () => []
    },
    columnDataTypeMap: {
      type: Array,
      default: () => []
    },
    labelOid: String,
    typeOid: String,
  },
  components: { DatasourceIndex, Column, FormConfigAddRelation },
  data () {
    const validateObjectForm = (rule, value, callback) => {
      if (value == undefined) {
        callback("数据库表名不能为空");
      } else {
        if (value.length <= 0) {
          callback("数据库表名不能为空");
        }
        if (
          value.length < 7 ||
          (value.length >= 7 && value.substring(0, 7) !== "t_form_")
        ) {
          callback("数据库表名前缀需是t_form_");
        }

        if (value === "t_form_") {
          callback("数据库表名不能是t_form_");
        }

        callback();
      }
    };
    return {
      dataForm: {
        objectName: '',
        moduleOid: '',
        objectForm: 't_form_',
        datasourceOid: '',
        objectOid: '',
      },

      moduleOptions: [],

      dataSourceOptions: [],

      // 表单校验
      rules: {
        objectName: [{ required: true, message: "存储对象名称不能为空", trigger: "blur" }],
        objectForm: [{ required: true, validator: validateObjectForm, trigger: "blur" }],
        moduleOid: [{ required: true, message: "请选择所属模块", trigger: "change" }],
        datasourceOid: [{ required: true, message: "请选择数据源", trigger: "blur" }]
      },

      visible: false,

      columnKeys: []

    }
  },
  computed: {
    params ({ typeOid, labelOid, contentOid, fieldTypeOid, dataForm: { objectOid, datasourceOid, moduleOid } }) {
      return {
        contentOid,
        datasourceOid,
        fieldTypeOid,
        moduleOid,
        objectOid,
        labelOid,
        typeOid,

      }
    }
  },
  created () {
    this.dataForm.objectName = this.fieldTypeName;
    if (this.isLogicFormMain && this.dataForm.objectForm === 't_form_') {
      this.dataForm.objectForm = this.dataForm.objectForm + Date.now();
    }
  },
  mounted () {
    this.init();
    this.queryMoudleList();
  },
  methods: {
    // 获取所属模块字典
    queryMoudleList () {
      modulelist({
        authorizeKey: this.authorizeKey
      }).then(({ code, data }) => {
        if (code == 200) {
          this.moduleOptions = data || [];
        }
      });
    },

    getDataSourcelist (moduleOid) {
      dataSourcelist({
        authorizeKey: this.authorizeKey,
        moduleOid: moduleOid
      }).then(({ code, data }) => {
        if (code === 200) {
          this.dataSourceOptions = data || [];
        }
      })
    },

    handleEditDatasource () {
      this.$refs.dataForm.validateField('moduleOid', (msg) => {
        if (!msg) {
          this.visible = true;
        }
      });
    },

    //模块的切换
    handleModuleOidChange (moduleOid) {
      this.dataForm.moduleOid = moduleOid;
      this.dataSourceOptions = [];
      this.$refs.dataForm.validateField('moduleOid');
      if (!!moduleOid) {
        this.getDataSourcelist(moduleOid);
      }
    },

    //数据源切换
    handleDataSourceChange (data) {
      if ("" === data) {
        this.$message.error("数据库配置异常，请检查或重新配置");
      } else {
        this.dataForm.datasourceOid = data;
      }
      this.$refs["dataForm"].clearValidate();
    },

    closeView () {
      this.visible = false;
      if (this.dataForm.moduleOid) {
        this.getDataSourcelist(this.dataForm.moduleOid);
      }
    },

    //初始化数据
    init () {
      if (!this.authorizeKey) {
        this.msgError("参数错误：authorizeKey is null");
        return false;
      }
      this.getForm();
    },

    getForm () {
      if (this.isModify && Object.keys(this.formData).length > 0 && this.formCode) {
        this.dataForm = { ...this.formData };
        this.getDataSourcelist(this.dataForm.moduleOid);
      }
    },

    setColumnkeys (data) {
      this.columnKeys = data;
    },

    validate () {
      return new Promise((resolve) => {
        this.$refs.dataForm.validate((valid) => {
          if (this.$refs.formConfigRelation) {
            this.$refs.formConfigRelation.$refs.ruleForm.validate((_valid) => {
              resolve(_valid && valid);
            });
          } else {
            resolve(valid);
          }
        });
      })
    }
  }
}
</script>

<style scoped lang="scss">
.form-config-add-main {
  flex: 5;
  background: #ffffff;
  border-radius: 5px 5px 0 0;
  height: calc(100% - 10px);
  margin-top: 10px;
  margin-left: 10px;

  .form-config-add-main-container {
    padding: 20px;
    animation: show 1s;
  }
}
</style>
